package com.irsa.cases.console.service;

import java.util.Map;

public interface HolidayConsoleService {
    Map<String, Object> getList();
    
    Map<String, Object> save(String id, String year, String title);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> getMap(String id);
}
