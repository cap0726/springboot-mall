package com.capliao.springbootmall.model;

import javax.validation.constraints.NotBlank;

public class UserSalt {

    @NotBlank
    String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
