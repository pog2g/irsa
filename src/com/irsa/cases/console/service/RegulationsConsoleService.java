package com.irsa.cases.console.service;

import java.util.Map;

public interface RegulationsConsoleService {
    Map<String, Object> getList(String page, String typeId, String title, String department, String publishNumber, String publishTime, String executeTime, String key);
    
    Map<String, Object> save(String id, String typeId, String department, String publishNumber, String publishTime, String executeTime, String title, String content);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> getMap(String id);
}
