package com.chunlei.mall.portal.service.Impl;

import com.chunlei.mall.mapper.UmsMemberReceiveAddressMapper;
import com.chunlei.mall.model.UmsMember;
import com.chunlei.mall.model.UmsMemberReceiveAddress;
import com.chunlei.mall.model.UmsMemberReceiveAddressExample;
import com.chunlei.mall.portal.service.UmsMemberReceiveAddressService;
import com.chunlei.mall.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public int add(UmsMemberReceiveAddress address) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        return 0;
    }

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember member = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        return memberReceiveAddressMapper.selectByExample(example);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        return null;
    }
}
