package com.irsa.cases.console.service;

import java.util.Map;

public interface HolidayDateConsoleService {
    Map<String, Object> getList(String holidayId);
    
    Map<String, Object> save(String id, String holidayId, String type, String date);
    
    Map<String, Object> delete(String ids);
}
