package com.leihao.wiki.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserResetPasswordRequest {
    private Long id;


    @NotNull(message = "密码不能为空")
//    @Length(min=6,max=20,message = "【密码】6~20位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$",
            message = "【密码】至少包含数字和英文,长度6-32位")
    private String password;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserResetPasswordRequest{" +
                "id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}
