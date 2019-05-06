package com.irsa.cases.console.service;

import java.util.Map;

public interface DefendantConsoleService {
    Map<String, Object> getList(String page, String key);
    
    /*Map<String, Object> save4Personnel(String actId, String casesId, String personnelId, String type, String name, String address, String legalPerson,String job,String legalPersonType);*/

    Map<String, Object> save4Personnel(String actId, String casesId, String personnelId, String type, String unit_name, String unit_abode, String unit_contact, String name ,String legal_person_type, String duty);
    
    Map<String,Object> getChooseList(String page, String key);
    
    Map<String, Object> save(String resId, String id, String typeId, String unit_name, String unit_abode, String unit_contact, String name, String legal_person_type, String duty, String account, String pwd, String pwdConfirm);
}
