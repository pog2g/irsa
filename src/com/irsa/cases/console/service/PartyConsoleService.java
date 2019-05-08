package com.irsa.cases.console.service;

import java.util.Map;

public interface PartyConsoleService {
    Map<String, Object> getList(String page, String key);

    Map<String, Object> save(String actId, String casesPersonnelType, String casesId, String personnelId,
                             String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                             String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPersonType);

    Map<String, Object> getChooseList(String page, String key, String type);

    /**
     * 检查身份证是否存在
     *
     * @param identity ${@link String}  身份证
     * @return Object> ${@link Object>}
     * @date 2019-05-08 16:32
     */
    Map<String, Object> checkIdentity(String identity);
}
