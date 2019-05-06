package com.irsa.console.service;

import java.util.Map;

public interface CompetenceService {
    Map<String, Object> getList();
    
    Map<String, Object> save(String parentId, String id, String icon, String title, String url, String state, String remark);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> sort(String ids);
    
    Map<String, Object> getChooseList4Parent();
    
    Map<String, Object> getChooseList(String accountId);
    
    Map<String, Object> getMap(String id);
    
    Map<String, Object> getList4Menu(String accountId);
    
    Map<String, Object> saveMenu(String id, String title, String state, String remark);
}
