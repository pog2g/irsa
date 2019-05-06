package com.irsa.cases.console.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface TempFileConsoleService {
    Map<String, Object> getList(String resId, String mode);
    
    Map<String, Object> img(HttpServletRequest request);
    
    Map<String, Object> imgs(HttpServletRequest request);
    
    Map<String, Object> file(HttpServletRequest request);
    
    Map<String, Object> files(HttpServletRequest request);
    
    Map<String, Object> rename(String id, String name);
    
    Map<String, Object> delete(String id);
}
