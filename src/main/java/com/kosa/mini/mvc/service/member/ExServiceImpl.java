package com.kosa.mini.mvc.service.member;

import mybatis.dao.ExMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ExServiceImpl implements ExService {

    @Autowired
    ExMapper dao;
}
