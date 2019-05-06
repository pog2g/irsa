package com.irsa.cases.console.service;

import java.util.Map;

public interface DepartmentDocumentNumberConsoleService {
    Map<String, Object> getList(String departmentId);
    
    Map<String, Object> save(String id, String numbner1, String numbner2, String numbner3);
    
    Map<String, Object> saveHtml(String id, String html);
    
    Map<String, Object> getDocumentHtml(String id, String html);
 }
