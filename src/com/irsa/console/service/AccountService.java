package com.irsa.console.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {
    Map<String, Object> getAccountList(String accountId);
    
    Map<String, Object> getList(String accountId);
    
    Map<String, Object> save(String id, String account, String nickname, String gender, String phone, String idNumber, String pwd, String confirmPwd, String roleId);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> login(HttpServletRequest request, String departmentId, String name, String pwd);
    
    Map<String, Object> getAccount(HttpServletRequest request);
}
