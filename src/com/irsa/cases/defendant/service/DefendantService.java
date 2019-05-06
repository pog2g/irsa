package com.irsa.cases.defendant.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface DefendantService {
    Map<String, Object> register(String id, String name, String address, String legalPerson, String account, String pwd, String pwdConfirm);
    
    Map<String, Object> login(HttpServletRequest request, String name, String pwd);
    
    Map<String, Object> getMap(HttpServletRequest request);
    
    Map<String, Object> getCasesList(HttpServletRequest request);
    
    Map<String, Object> reply(String bookId, String fileId, String casesId, String reply, String remark);
}
