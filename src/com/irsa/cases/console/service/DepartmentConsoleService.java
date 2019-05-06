package com.irsa.cases.console.service;

import java.util.Map;

public interface DepartmentConsoleService {
    Map<String, Object> getList(String accountId);
    
    Map<String, Object> save(String id, String title);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> sort(String ids);
    
    Map<String, Object> getChooseList(String accountId);
    
    Map<String, Object> getMap(String id);
}
