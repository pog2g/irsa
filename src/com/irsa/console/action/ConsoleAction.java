package com.irsa.console.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.console.service.AccountService;
import com.common.utils.Utils;

@Controller
@RequestMapping("/console")
public class ConsoleAction {
    Logger log = LoggerFactory.getLogger(ConsoleAction.class);
    @Autowired
    AccountService accountService;
    
    @RequestMapping("/logon")
    @ResponseBody
    public Map<String, Object> logon(HttpServletRequest request, String department, String name, String pwd) {
        Map<String, Object> result = accountService.login(request, department, name, pwd);
        log.info("[/console/logon] {}", result);
        return result;
    }
    
    @RequestMapping("/get_account")
    @ResponseBody
    public Map<String, Object> getAccount(HttpServletRequest request) {
        Map<String, Object> result = accountService.getAccount(request);
        log.info("[/console/get_account] {}", result);
        return result;
    }
    
    @RequestMapping("/get_act_id")
    @ResponseBody
    public Map<String, Object> getActId() {
        Map<String, Object> result = Utils.getSuccessMap(Utils.getId());
        log.info("[/console/get_act_id] {}", result);
    	return result;
    }

    public AccountService getAccountService() {
        return accountService;
    }
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
