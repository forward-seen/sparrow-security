package com.sparrow.security.controller;

import com.sparrow.cache.exception.CacheNotFoundException;
import com.sparrow.constant.SparrowError;
import com.sparrow.passport.controller.protocol.query.LoginQuery;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginToken;
import com.sparrow.protocol.Result;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
//@RequestMapping("/")
public class SpringUserLoginController {
    @Autowired
    private UserLoginController userLoginController;

    @GetMapping("/session-id")
    public String sessionId(HttpServletRequest request) throws BusinessException {
        return request.getSession().getId();
    }

    @PostMapping("/login.do")
    /**
     * @RequestBody DispatcherServlet Completed 415 UNSUPPORTED_MEDIA_TYPE
     */
    public ModelAndView login(LoginQuery login,
        ClientInformation client) throws BusinessException, CacheNotFoundException {
        try {
            LoginToken loginToken = this.userLoginController.login(login, client);
            ModelAndView mv = new ModelAndView(login.getRedirectUrl());
            mv.addObject(loginToken);
            return mv;
        } catch (BusinessException e) {
            if (e.getCode().equals(SparrowError.USER_NOT_ACTIVATE.getCode())) {
                ModelAndView mv = new ModelAndView("redirect:/email-activate");
                mv.addObject("email", login.getUserName());
                return mv;
            }
            throw e;
        }
    }

    @PostMapping("/shortcut-login.json")
    public LoginToken shortcut(LoginQuery login, ClientInformation client) throws BusinessException {
        return this.userLoginController.shortcut(login, client);
    }

    public void logout() {
        this.userLoginController.logout();
    }
}
