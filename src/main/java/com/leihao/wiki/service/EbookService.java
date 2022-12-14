package com.leihao.wiki.service;

import com.leihao.wiki.domain.Demo;
import com.leihao.wiki.domain.Ebook;
import com.leihao.wiki.mapper.DemoMapper;
import com.leihao.wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list(){
        return ebookMapper.selectByExample(null);
    }
}
