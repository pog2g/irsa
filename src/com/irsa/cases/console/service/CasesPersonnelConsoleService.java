package com.irsa.cases.console.service;

import java.util.Map;

public interface CasesPersonnelConsoleService {
    Map<String, Object> getList(String casesId, String personnelType);

    Map<String, Object> getPersonnelInfoList(String casesId, String personnelType);
    
    Map<String, Object> getList4Representative(String casesId);
    
    Map<String, Object> getChooseList4Client(String casesId, String type);
    
    Map<String, Object> getChooseList4ApplyThirdParty(String casesId);
    
    Map<String, Object> update13(String id, String name, String legalPerson, String idType, String idNo, String gender, String birthday, String phone, String contact,
                                 String zipCode, String provinceId, String cityId, String countyId, String address,
                                 String unitIdType, String unitIdNo, String unitContact, String domicile);
    
    Map<String, Object> update2(String id, String type, String name, String address, String legalPerson);
    
    Map<String, Object> update6(String id, String name, String unit_name, String idType, String idNo, String gender, String birthday, String phone, String contact,
                                String zipCode, String provinceId, String cityId, String countyId, String address);
}
