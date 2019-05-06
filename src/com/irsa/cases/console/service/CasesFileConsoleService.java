package com.irsa.cases.console.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CasesFileConsoleService {
    Map<String, Object> upload(HttpServletRequest request);

    Map<String, Object> getList(String casesId, String mode);
    
    Map<String, Object> getMap(String id);
    
    Map<String, Object> save(String id, String html);
}
