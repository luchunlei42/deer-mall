package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.CmsSubjectMapper;
import com.chunlei.mall.model.CmsSubject;
import com.chunlei.mall.model.CmsSubjectExample;
import com.chunlei.mall.service.CmsSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsSubjectServiceImpl implements CmsSubjectService {
    @Autowired
    CmsSubjectMapper subjectMapper;

    @Override
    public List<CmsSubject> listAll() {
        CmsSubjectExample example = new CmsSubjectExample();
        return subjectMapper.selectByExample(example);
    }
}
