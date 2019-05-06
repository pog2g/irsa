package com.irsa.cases.console.service;

import java.util.Map;

public interface PartyConsoleService {
    Map<String, Object> getList(String page, String key);
    
    Map<String, Object> save(String actId, String casesPersonnelType, String casesId, String personnelId,
                    String type, String name, String other_name, String nature, String gender,  String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                    String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPersonType);
    
    Map<String,Object> getChooseList(String page, String key, String type);
}
