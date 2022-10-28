package com.sparrow.security.controller.impl;

import com.sparrow.cache.exception.CacheNotFoundException;
import com.sparrow.constant.SparrowError;
import com.sparrow.constant.User;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginToken;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.passport.api.UserLoginService;
import com.sparrow.security.controller.UserLoginController;
import com.sparrow.passport.controller.assemble.LoginControllerAssemble;
import com.sparrow.passport.controller.protocol.query.LoginQuery;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserLoginControllerImpl implements UserLoginController {
    @Inject
    private ServletContainer servletContainer;

    @Inject
    @Named("userLoginApplicationService")
    private UserLoginService userLoginService;

    @Inject
    private LoginControllerAssemble loginControllerAssemble;

    private void validateCode(String validateCode, String userValidateCode) throws BusinessException {
        boolean expression = validateCode == null
            || !validateCode.equalsIgnoreCase(userValidateCode);
        Asserts.isTrue(expression, SparrowError.GLOBAL_VALIDATE_CODE_ERROR, USER_LOGIN_VALIDATE_CODE);
    }

    @Override
    public LoginToken login(LoginQuery login,
        ClientInformation client) throws BusinessException, CacheNotFoundException {
        String validateCode = servletContainer.flash(Constant.VALIDATE_CODE);
        this.validateCode(validateCode, login.getValidateCode());
        LoginToken loginResult = this.userLoginService.login(this.loginControllerAssemble.vo2dto(login, client));
        servletContainer
            .rootCookie(User.PERMISSION, loginResult.getPermission(), loginResult.getDays());
        return loginResult;
    }

    @Override
    public LoginToken shortcut(LoginQuery login, ClientInformation client) throws BusinessException {
        String validateCode = servletContainer.flash(Constant.VALIDATE_CODE);
        this.validateCode(validateCode, login.getValidateCode());
        return this.userLoginService.login(this.loginControllerAssemble.vo2dto(login, client));
    }

    @Override
    public void logout() {

    }
}
