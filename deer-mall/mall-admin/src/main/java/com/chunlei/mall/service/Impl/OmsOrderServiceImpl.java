package com.chunlei.mall.service.Impl;

import com.chunlei.mall.dao.OmsOrderDao;
import com.chunlei.mall.dao.OmsOrderOperateHistoryDao;
import com.chunlei.mall.dto.OmsOrderDeliveryParam;
import com.chunlei.mall.dto.OmsOrderDetail;
import com.chunlei.mall.dto.OmsOrderQueryParam;
import com.chunlei.mall.dto.OmsReceiverInfoParam;
import com.chunlei.mall.mapper.OmsOrderMapper;
import com.chunlei.mall.mapper.OmsOrderOperateHistoryMapper;
import com.chunlei.mall.model.OmsOrder;
import com.chunlei.mall.model.OmsOrderExample;
import com.chunlei.mall.model.OmsOrderOperateHistory;
import com.chunlei.mall.service.OmsOrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    OmsOrderMapper omsOrderMapper;
    @Autowired
    OmsOrderDao omsOrderDao;
    @Autowired
    OmsOrderOperateHistoryDao omsOrderOperateHistoryDao;
    @Autowired
    OmsOrderOperateHistoryMapper omsOrderOperateHistoryMapper;

    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return omsOrderDao.list(queryParam);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return omsOrderDao.detail(id);
    }

    @Override
    public int deliver(List<OmsOrderDeliveryParam> orderDeliveryParamList) {
        int count = omsOrderDao.delivery(orderDeliveryParamList);
        //添加操作记录
        List<OmsOrderOperateHistory> operateHistoryList = orderDeliveryParamList.stream().
                map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("后台管理员");
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).collect(Collectors.toList());
        omsOrderOperateHistoryDao.insertList(operateHistoryList);
        return count;
    }

    @Override
    public int close(List<Long> ids, String note) {
        OmsOrder order = new OmsOrder();
        order.setStatus(4);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        int count = omsOrderMapper.updateByExampleSelective(order, example);
        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId->{
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setOrderStatus(4);
            history.setCreateTime(new Date());
            history.setOperateMan("后台管理员");
            history.setNote("订单关闭："+note);
            return history;
        }).collect(Collectors.toList());
        omsOrderOperateHistoryDao.insertList(historyList);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrder record = new OmsOrder();
        record.setDeleteStatus(1);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        return omsOrderMapper.deleteByExample(example);
    }

    @Override
    public int updateReceiver(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = omsOrderMapper.updateByPrimaryKeySelective(order);
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        omsOrderOperateHistoryMapper.insert(history);
        return count;
    }


}
