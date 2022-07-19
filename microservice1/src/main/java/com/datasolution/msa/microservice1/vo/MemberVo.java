package com.datasolution.msa.microservice1.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MemberVo {
    private String id;
    private String password;
    private String name;
    private String age;
    private Role role;
}
