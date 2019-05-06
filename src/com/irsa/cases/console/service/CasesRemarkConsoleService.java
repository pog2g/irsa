package com.irsa.cases.console.service;

import java.util.Map;

public interface CasesRemarkConsoleService {
    Map<String, Object> getList(String casesId, String accountId);
    
    Map<String, Object> save(String id, String casesId, String accountId, String remark);
    
    Map<String, Object> delete(String id);
}
