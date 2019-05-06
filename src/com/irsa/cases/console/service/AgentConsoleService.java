package com.irsa.cases.console.service;

import java.util.Map;

public interface AgentConsoleService {
    Map<String, Object> getList(String page, String key);
    
    Map<String, Object> save(String actId, String casesId, String clientId, String personnelId,
                             String name, String nature, String gender, String birthday,
                             String idTypeId, String idNo,  String phone, String abode ,
                             String unit_name, String identity, String kinsfolk,String type
                             );
    
    Map<String,Object> getChooseList(String page, String key);
}
