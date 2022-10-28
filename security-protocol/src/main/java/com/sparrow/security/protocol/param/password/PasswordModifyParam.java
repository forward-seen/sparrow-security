package com.sparrow.security.protocol.param.password;

import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.POJO;

public class PasswordModifyParam implements POJO {
    private Long userId;
    private String password;
    private String newPassword;
    private ClientInformation client;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ClientInformation getClient() {
        return client;
    }

    public void setClient(ClientInformation client) {
        this.client = client;
    }
}
