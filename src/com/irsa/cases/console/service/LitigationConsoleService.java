package com.irsa.cases.console.service;

import java.util.Map;

public interface LitigationConsoleService {
    Map<String, Object> getList(String page, String year, String reasonId, String mode, String type, String court, String title, String keys);
    
    Map<String, Object> save(String id, String year, String reasonId, String court, String mode, String type, String title, String content);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> getMap(String id);
}
