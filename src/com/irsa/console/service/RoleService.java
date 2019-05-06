package com.irsa.console.service;

import java.util.List;
import java.util.Map;

public interface RoleService {
    Map<String, Object> getList(String accountId);
    
    Map<String, Object> save(String id, String deparmentId, String title, String remark, List<String> competenceIds);
    
    Map<String, Object> delete(String ids);
    
    Map<String, Object> getChooseList(String parent);
}
