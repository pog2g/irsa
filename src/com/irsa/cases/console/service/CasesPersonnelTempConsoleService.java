package com.irsa.cases.console.service;

import java.util.Map;

public interface CasesPersonnelTempConsoleService {
    Map<String,Object> getChooseList4Apply(String casesId);
    
    Map<String, Object> getList(String casesId, String casesPersonnelType);
    
    Map<String, Object> delete(String id);
    
    Map<String, Object> setRepresentative(String ids);
    
    Map<String, Object> deleteRepresentative(String id);
    
    Map<String, Object> getList4Representative(String casesId);
}
