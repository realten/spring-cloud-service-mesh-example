package com.datasolution.msa.microservice1.dao;

import com.datasolution.msa.microservice1.vo.MemberVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AuthDao {
    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate dao;

    public MemberVo selectMemberInfo(MemberVo vo) {
        return dao.selectOne("com.datasolution.msa.microservice1.dao.selectMemberInfo", vo);
    }


}
