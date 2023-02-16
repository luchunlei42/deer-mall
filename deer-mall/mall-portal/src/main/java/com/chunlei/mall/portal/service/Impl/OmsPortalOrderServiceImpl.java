package com.chunlei.mall.portal.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.exception.ApiException;
import com.chunlei.mall.common.service.RedisService;
import com.chunlei.mall.mapper.*;
import com.chunlei.mall.model.*;
import com.chunlei.mall.portal.component.CancelOrderSender;
import com.chunlei.mall.portal.dao.OmsOrderItemDao;
import com.chunlei.mall.portal.dao.PortalOrderDao;
import com.chunlei.mall.portal.domain.*;
import com.chunlei.mall.portal.service.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    @Autowired
    UmsMemberService memberService;
    @Autowired
    OmsCartItemService cartItemService;
    @Autowired
    UmsMemberReceiveAddressService memberReceiveAddressService;
    @Autowired
    UmsMemberCouponService memberCouponService;
    @Autowired
    PmsSkuStockMapper skuStockMapper;
    @Value("${redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Autowired
    OmsOrderSettingMapper orderSettingMapper;
    @Autowired
    OmsOrderMapper orderMapper;
    @Autowired
    OmsOrderItemDao orderItemDao;
    @Autowired
    SmsCouponHistoryMapper couponHistoryMapper;
    @Autowired
    private PortalOrderDao portalOrderDao;
    @Autowired
    private CancelOrderSender cancelOrderSender;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @Autowired
    private RedisService redisService;
    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(),cartIds);
        result.setCartPromotionItemList(cartPromotionItemList);
        //获取用户收获地址
        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        //获取用户可用优惠卷列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList,1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        //获取用户积分
        //获取积分使用规则
        //计算总金额,活动优惠，应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    @Override
    public Map<String, Object> generateOrder(OrderParam orderParam) {
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        //获取购物车及优惠信息
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), orderParam.getCartIds());
        for (CartPromotionItem cartPromotionItem: cartPromotionItemList){
            //生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartPromotionItem.getProductId());
            orderItem.setProductName(cartPromotionItem.getProductName());
            orderItem.setProductPic(cartPromotionItem.getProductPic());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
            orderItem.setProductSn(cartPromotionItem.getProductSn());
            orderItem.setProductPrice(cartPromotionItem.getPrice());
            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
            orderItemList.add(orderItem);
        }
        //判断购物车中商品是否都有库存
        if (!hasStock(cartPromotionItemList)){
            throw new ApiException("库存不足，无法下单");
        }
        //判断使用优惠卷
        if(orderParam.getCouponId() == null){
            //不用优惠卷
            for (OmsOrderItem orderItem: orderItemList) {
                orderItem.setCouponAmount(new BigDecimal(0));
            }
        }else{
            //使用优惠卷
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
            if (couponHistoryDetail == null){
                throw new ApiException("该优惠卷不可用");
            }
            //对下单商品的优惠卷进行处理
            handleCouponAmount(orderItemList,couponHistoryDetail);
        }
        //积分
        //计算order_item的实付金额
        handleRealAmount(orderItemList);
        //进行库存锁定
        lockStock(cartPromotionItemList);
        //根据商品合计，运费，活动优惠，优惠卷，积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(new BigDecimal(0));
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
        if (orderParam.getCouponId() == null) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }
        order.setPayAmount(calcPayAmount(order));
        //转化为订单信息并插入数据库
        order.setMemberId(currentMember.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        //支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        //订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);
        //收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        //生成订单号
        order.setOrderSn(generateOrderSn(order));
        //设置自动收货天数
        List<OmsOrderSetting> orderSettings = orderSettingMapper.selectByExample(new OmsOrderSettingExample());
        if(CollUtil.isNotEmpty(orderSettings)){
            order.setAutoConfirmDay(orderSettings.get(0).getConfirmOvertime());
        }
        // TODO: 2018/9/3 bill_*,delivery_*
        //插入order表和order_item表
        orderMapper.insert(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        orderItemDao.insertList(orderItemList);
        //如使用优惠券更新优惠券使用状态
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        //如使用积分需要扣除积分
//        if (orderParam.getUseIntegration() != null) {
//            order.setUseIntegration(orderParam.getUseIntegration());
//            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
//        }
        //删除购物车中的下单商品
        deleteCartItemList(cartPromotionItemList, currentMember);
        //TODO: 发送延迟消息取消订单

        Map<String, Object> result = new HashMap<>();
        result.put("order",order);
        result.put("orderItemList", orderItemList);
        return result;
    }



    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        //修改订单状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(1);
        order.setPaymentTime(new Date());
        order.setPayType(payType);
        orderMapper.updateByPrimaryKeySelective(order);
        //恢复所有下单商品的锁定库存，扣除真实库存
        OmsOrderDetail orderDetail = portalOrderDao.getDetail(orderId);
        int count = portalOrderDao.updateSkuStock(orderDetail.getOrderItemList());
        return count;
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        //获取订单超时时间
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = orderSetting.getNormalOrderOvertime()*60*1000;
        //发送延迟消息
        cancelOrderSender.sendMessage(orderId,delayTimes);
    }

    @Override
    public void cancelOrder(Long orderId) {
        //查询未付款的取消订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0)
                .andDeleteStatusEqualTo(0);
        List<OmsOrder> cancelOrderList = orderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(cancelOrderList)){
            return;
        }
        OmsOrder cancelOrder = cancelOrderList.get(0);
        if(cancelOrder != null){
            //修改订单表状态为取消
            cancelOrder.setStatus(4);
            orderMapper.updateByPrimaryKeySelective(cancelOrder);
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            //解除订单商品库存锁定
            if(!CollectionUtils.isEmpty(orderItemList)){
                portalOrderDao.releaseSkuStockLock(orderItemList);
            }
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            //TODO: 返还使用积分
        }
    }

    @Override
    public Integer cancelTimeOutOrder() {
        Integer count = 0;
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        //TODO: 批量取消订单
        return count;
    }

    @Override
    public CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        if(status==-1){
            status=null;
        }
        OmsOrderExample orderExample = new OmsOrderExample();
        UmsMember currentMember = memberService.getCurrentMember();
        PageHelper.startPage(pageNum,pageSize);
        OmsOrderExample.Criteria criteria = orderExample.createCriteria();
        criteria
        .andMemberIdEqualTo(currentMember.getId());
        if(status!=null){
            criteria.andStatusEqualTo(status);
        }
        orderExample.setOrderByClause("create_time desc");
        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        CommonPage<OmsOrder> orderPage = CommonPage.restPage(orderList);
        //设置分页信息
        CommonPage<OmsOrderDetail> resultPage = new CommonPage<>();
        resultPage.setPageNum(orderPage.getPageNum());
        resultPage.setPageSize(orderPage.getPageSize());
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setTotalPage(orderPage.getTotalPage());
        if(CollUtil.isEmpty(orderList)){
            return resultPage;
        }
        //设置数据信息
        List<Long> orderIds = orderList.stream().map(OmsOrder::getId).collect(Collectors.toList());
        OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
        orderItemExample.createCriteria().andOrderIdIn(orderIds);
        List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
        List<OmsOrderDetail> orderDetailList = new ArrayList<>();
        for(OmsOrder omsOrder : orderList){
            OmsOrderDetail orderDetail = new OmsOrderDetail();
            BeanUtil.copyProperties(omsOrder,orderDetail);
            List<OmsOrderItem> relatedItemList = orderItemList.stream().filter(item->item.getOrderId().equals(orderDetail.getId())).collect(Collectors.toList());
            orderDetail.setOrderItemList(relatedItemList);
            orderDetailList.add(orderDetail);
        }
        resultPage.setList(orderDetailList);
        return resultPage;
    }


    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        cartItemService.delete(currentMember.getId(), ids);
    }
    private String generateOrderSn(OmsOrder order){
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key  = REDIS_DATABASE+":"+ REDIS_KEY_ORDER_ID + date;
        Long increment = redisService.incr(key,1);
        sb.append(date);
        sb.append(String.format("%02d",order.getSourceType()));
        sb.append(String.format("%02d",order.getPayType()));
        String incrementStr = increment.toString();
        if(incrementStr.length() <= 6){
            sb.append(String.format("%06d",increment));
        }else{
            sb.append(incrementStr);
        }
        return sb.toString();
    }
    private BigDecimal calcPayAmount(OmsOrder order) {
        //总金额+运费-促销优惠-优惠券优惠-积分抵扣
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
        return payAmount;
    }
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName());
            sb.append(";");
        }
        String result = sb.toString();
        if (result.endsWith(";")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }
    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartPromotionItem.getProductSkuId());
            skuStock.setLockStock(skuStock.getLockStock() + cartPromotionItem.getQuantity());
            skuStockMapper.updateByPrimaryKeySelective(skuStock);
        }
    }
    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            //原价-促销优惠-优惠券抵扣-积分抵扣
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }
    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList){
        for (CartPromotionItem cartPromotionItem: cartPromotionItemList){
            if(cartPromotionItem.getRealStock()==null||cartPromotionItem.getRealStock() <= 0){
                return false;
            }
        }
        return true;
    }
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId){
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList,1);
        for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList){
            if (couponHistoryDetail.getCoupon().getId().equals(couponId)){
                return couponHistoryDetail;
            }
        }
        return null;
    }
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail){
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            //全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            //指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            //指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon){
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem: orderItemList){
            //(商品价格/可用商品总价)*优惠卷面额
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount,3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail,List<OmsOrderItem> orderItemList,int type){
        List<OmsOrderItem> result = new ArrayList<>();
        if(type == 0){
            List<Long> categoryidList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()){
                categoryidList.add(productCategoryRelation.getProductCategoryId());
            }
            for (OmsOrderItem orderItem: orderItemList){
                if(categoryidList.contains(orderItem.getProductCategoryId())){
                    result.add(orderItem);
                }else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }else if (type==1){
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation productRelation: couponHistoryDetail.getProductRelationList()){
                productIdList.add(productRelation.getProductId());
            }
            for (OmsOrderItem orderItem: orderItemList){
                if (productIdList.contains(orderItem.getProductId())){
                    result.add(orderItem);
                }else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return result;
    }
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList){
        BigDecimal totalAmount = new BigDecimal(0);
        for (OmsOrderItem item: orderItemList){
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));

        }
        return totalAmount;
    }
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus){
        if (couponId == null) return;
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        example.createCriteria().andMemberIdEqualTo(memberId)
                .andCouponIdEqualTo(couponId).andUseStatusEqualTo(useStatus==0?1:0);
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(couponHistoryList)) {
            SmsCouponHistory couponHistory = couponHistoryList.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryMapper.updateByPrimaryKeySelective(couponHistory);
        }
    }
}
