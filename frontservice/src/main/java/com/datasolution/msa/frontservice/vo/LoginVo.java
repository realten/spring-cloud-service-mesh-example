package com.datasolution.msa.frontservice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class LoginVo {
    private String id;
    private String password;
}
