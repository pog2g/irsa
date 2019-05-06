package com.irsa.console.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.utils.Utils;
import com.irsa.console.service.AccountService;

@Controller
@RequestMapping("/console/account")
public class AccountAction {
    Logger log = LoggerFactory.getLogger(AccountAction.class);
    @Autowired
    AccountService accountService;
    
    @RequestMapping("/get_account_list")
    @ResponseBody
    public Map<String, Object> getAccountList(HttpServletRequest request) {
        Map<String, Object> result = accountService.getAccountList(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/account/get_account_list] {}", result);
        return result;
    }
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(HttpServletRequest request) {
        Map<String, Object> result = accountService.getList(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/account/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String account, String nickname, 
            String gender, String phone, @RequestParam(required=false, name="id_number") String idNumber, 
            String pwd, @RequestParam(required=false, name="confirm_pwd") String confirmPwd, 
            @RequestParam(required=false, name="role_id") String roleId) {
        Map<String, Object> result = accountService.save(id, account, nickname, gender, phone, idNumber, pwd, confirmPwd, roleId);
        log.info("[/console/account/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = accountService.delete(ids);
        log.info("[/console/account/delete] {}", result);
        return result;
    }

    public AccountService getAccountService() {
        return accountService;
    }
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
