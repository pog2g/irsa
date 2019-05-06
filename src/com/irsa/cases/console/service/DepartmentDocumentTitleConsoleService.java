package com.irsa.cases.console.service;

import java.util.Map;

public interface DepartmentDocumentTitleConsoleService {
    Map<String, Object> getList(String departmentId);
    
    Map<String, Object> save(String id, String title);
 }
