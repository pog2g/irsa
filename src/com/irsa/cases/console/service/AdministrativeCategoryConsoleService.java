package com.irsa.cases.console.service;

import java.util.Map;

public interface AdministrativeCategoryConsoleService {
    Map<String, Object> getList();
    
    Map<String, Object> save(String parentId, String id, String title);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> sort(String ids);
    
    Map<String, Object> getChooseList4Parent();
    
    Map<String, Object> getChooseList();
}
