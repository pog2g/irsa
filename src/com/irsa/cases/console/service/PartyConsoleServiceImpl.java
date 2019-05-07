package com.irsa.cases.console.service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.manager.CasesPersonnelManager;
import com.irsa.cases.manager.PartyManager;
import com.irsa.cases.model.*;
import com.jdbc.utils.Utils4JDBC;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PartyConsoleServiceImpl implements PartyConsoleService {
    Logger log = LoggerFactory.getLogger(PartyConsoleServiceImpl.class);
    @Autowired
    CasesPersonnelManager casesPersonnelManager;
    @Autowired
    PartyManager partyManager;

    @Override
    public Map<String, Object> getList(String page, String key) {
        try {
            StringBuffer sql = new StringBuffer(Party.SELECT).append(" and (p.id_no is not null && p.id_no != '') ");
            Object[] param = null;
            if (StringUtils.isNotBlank(key)) {
                sql.append("and (p.name like ? or p.id_no like ? )");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + key + "%", "%" + key + "%"});
            }
            sql.append("order by p.createtime ");
            Map<String, Object> result = partyManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("data_list");
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, Object> data : dataList) {
                    PartyManager.getFileUrl(data);
                }
            }
            return Utils.getSuccessMap(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    /*partyConsoleService.save(id, CasesPersonnel.PERSONNEL_TYPE_1, casesId, personnelId, type, Utils.toString(name), Utils.toString(other_name), nature, gender, Utils.toString(birthday), idTypeId, Utils.toString(idNo), Utils.toString(phone),  Utils.toString(domicile),  Utils.toString(zipCode),  Utils.toString(contact), abode,
    unitName, Utils.toString(unitContact), unitIdTypeId, Utils.toString(unitIdNo))*/

    @Override
    public Map<String, Object> save(String actId, String casesPersonnelType, String casesId, String personnelId,
                                    String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                    String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        // 必填字段数据验证
        if (StringUtils.isBlank(name)) {
            return Utils.getErrorMap("请填写姓名");
        }
        String regex = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
        if (!birthday.matches(regex)) {
            return Utils.getErrorMap("请输入正确的出生日期");
        }
        if (StringUtils.isBlank(idTypeId) || "-1".equalsIgnoreCase(idTypeId)) {
            return Utils.getErrorMap("请选择证件类型");
        }
        if (StringUtils.isBlank(idNo)) {
            return Utils.getErrorMap("请填写证件号码");
        }
        if (StringUtils.isBlank(phone)) {
            return Utils.getErrorMap("请填写电话号码");
        }
        if (StringUtils.isBlank(domicile)) {
            return Utils.getErrorMap("请填写户籍所在地");
        }
        if (StringUtils.isBlank(abode)) {
            return Utils.getErrorMap("请填写法律文书送达地址");
        }
        try {
            Map<String, Object> tempFile = partyManager.getMap(TempFile.SELECT_BY_RESID, new Object[]{actId});

            String pid = "";
            if (StringUtils.isNotBlank(personnelId)) {
                pid = personnelId;
            } else {
                if (StringUtils.isNotBlank(idNo)) {
                    Map<String, Object> party = partyManager.getMap(Party.SELECT_EXIST_BY_IDNO, new Object[]{idNo});
                    if (party != null) {
                        pid = Utils.toString(party.get("id"));
                    }
                }
            }

            if (CasesPersonnel.PERSONNEL_TYPE_7.equals(casesPersonnelType)) {
                if (StringUtils.isNotBlank(pid)) {
                    personnelId = pid;
                    int isPersonnelExist = partyManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
                    if (isPersonnelExist != 0) {
                        return Utils.getErrorMap("当事人已存在");
                    }

                    /*update irsa_party set type = ?,name = ?,other_name= ?, nature = ?,gender = ?,birthday = ?,id_type_id= ?,id_no= ?,phone = ?,domicile= ?,zip_code= ?,contact= ?,abode= ?," +
                    "unit_name = ?,unit_contact = ?,unit_id_type_id = ?,unit_id_no = ?,unit_abode = ?,legal_person_type = ?  where id = ?"*/
                    partyManager.executeSQL(Party.UPDATE, new Object[]{type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                            unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson, personnelId});
                } else {
                    personnelId = Utils.getId();
                    partyManager.executeSQL(Party.INSERT_LEGAL, new Object[]{Utils.getCreateTime(), personnelId, type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                            unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson});
                }

                if (tempFile != null) {
                    partyManager.executeSQL(PartyFile.DELETE_BY_RESID, new Object[]{personnelId});
                    Utils4File.copyTempToReal(Utils4File.PATH_PARTY, tempFile.get("file_name").toString());
                    partyManager.executeSQL(PartyFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), tempFile.get("type"), personnelId,
                            tempFile.get("real_name"), tempFile.get("size"), tempFile.get("ext"), tempFile.get("file_name")});
                }

                partyManager.executeSQL(CasesPersonnelTemp.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesPersonnelType, casesId, personnelId, null});
                return Utils.getSuccessMap(null);
            }

            // 案件是否存在
            int isCasesExist = partyManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            /*if (Party.TYPE_2.equals(type)) {
                if (StringUtils.isBlank(legalPerson)) {
                    return Utils.getErrorMap("请填写单位名称");
                }*/
                /*if (StringUtils.isBlank(unitIdTypeId) || "-1".equals(unitIdTypeId)) {
                    return Utils.getErrorMap("请选择单位证件类型");
                }*/
               /* Map<String, Object> unitIdType = partyManager.getMap(UnitIdType.SELECT_BY_ID, new Object[]{unitIdTypeId});
                if (unitIdType == null) {
                    return Utils.getErrorMap("证件类型不存在");
                }
                if (StringUtils.isBlank(unitIdNo)) {
                    return Utils.getErrorMap("请填写单位证件号码");
                }
                if (StringUtils.isBlank(unitContact)) {
                    return Utils.getErrorMap("请填写单位联系方式");
                }*/
            /*}*/
            /*if (StringUtils.isBlank(idTypeId) || "-1".equals(idTypeId)) {
                return Utils.getErrorMap("请选择证件类型");
            }*/
/*            Map<String, Object> idType =partyManager.getMap(IdType.SELECT_BY_ID, new Object[]{idTypeId});
            if (idType == null) {
                return Utils.getErrorMap("证件类型不存在");
            }*/
            /*if (!"未知".equals(idType.get("text"))&&type!=null&&!type.equals("2")) {

                if (StringUtils.isBlank(idNo)) {
                    return Utils.getErrorMap("请填写证件号码");
                }
                if ("中国居民二代身份证".equals(idType.get("text"))) {
                    if (!Utils.checkIdNo(idNo)) {
                        return Utils.getErrorMap("证件号码格式错误");
                    }
                }
                if (StringUtils.isBlank(phone)) {
                    return Utils.getErrorMap("请填写手机号码");
                }
                if (!Utils.checkPhone(phone)) {
                    return Utils.getErrorMap("手机号码格式错误");
                }
            }*/

            if (StringUtils.isNotBlank(pid)) {
                personnelId = pid;
                if (isCasesExist == 0) {
                    int isPersonnelExist = partyManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
                    if (isPersonnelExist != 0) {
                        return Utils.getErrorMap("当事人已存在");
                    }
                } else {
                    if (CasesPersonnel.PERSONNEL_TYPE_3.equals(casesPersonnelType)) {
                        int isPersonnelExist = partyManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNELID_WITHOUT_APPLYTHIRDPARTY, new Object[]{casesId, personnelId});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    } else {
                        int isPersonnelExist = partyManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    }
                }
                
               /* Map<String, Object> file = partyManager.getMap(PartyFile.SELECT_BY_RESID, new Object[]{personnelId});
                if (!"未知".equals(idType.get("text")) && tempFile == null && file == null) {
                    return Utils.getErrorMap("请填上传证件信息");
                }*/

                partyManager.executeSQL(Party.UPDATE, new Object[]{type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                        unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson, personnelId});
                /*partyManager.executeSQL(Party.UPDATE, new Object[]{type, name, legalPerson, gender, birthday, phone, contact, zipCode,
                        countyId, address, idTypeId, idNo, unitIdTypeId, unitIdNo, unitContact, domicile,nature,ws_address,zw, personnelId});*/
            } else {
                /*if (!"未知".equals(idType.get("text")) && tempFile == null) {
                    return Utils.getErrorMap("请填上传证件信息");
                }*/

                personnelId = Utils.getId();
                partyManager.executeSQL(Party.INSERT_LEGAL, new Object[]{Utils.getCreateTime(), personnelId, type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                        unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson});
                /*partyManager.executeSQL(Party.INSERT, new Object[]{Utils.getCreateTime(), personnelId, type,
                        name, legalPerson, gender, birthday, phone, contact, zipCode, countyId, address, idTypeId, idNo, unitIdTypeId, unitIdNo, unitContact, domicile,nature,ws_address,zw});*/
            }
            if (tempFile != null) {
                partyManager.executeSQL(PartyFile.DELETE_BY_RESID, new Object[]{personnelId});
                Utils4File.copyTempToReal(Utils4File.PATH_PARTY, tempFile.get("file_name").toString());
                partyManager.executeSQL(PartyFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), tempFile.get("type"), personnelId,
                        tempFile.get("real_name"), tempFile.get("size"), tempFile.get("ext"), tempFile.get("file_name")});
            }
            // 案件是否存在
            if (isCasesExist == 0) {
                partyManager.executeSQL(CasesPersonnelTemp.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesPersonnelType, casesId, personnelId, null});
                return Utils.getSuccessMap(null);
            }
            casesPersonnelManager.save4Party(casesPersonnelType, casesId, personnelId, null, null);
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> getChooseList(String page, String key, String caseType) {
        try {
            StringBuffer sql = new StringBuffer(Party.SELECT_4_CHOOSE);
            Object[] param = new Object[]{"%" + key + "%", "%" + key + "%", "%" + key + "%"};
            String type = "";

            if (caseType != null && caseType != "") {
                type = caseType;
                sql.append("and p.type = ? order by p.createtime desc");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{type});
            } else {
                sql.append("order by p.createtime desc");
            }
            Map<String, Object> result = partyManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("data_list");
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    PartyManager.getFileUrl(data);
                }
            }
            return Utils.getSuccessMap(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public CasesPersonnelManager getCasesPersonnelManager() {
        return casesPersonnelManager;
    }

    public void setCasesPersonnelManager(CasesPersonnelManager casesPersonnelManager) {
        this.casesPersonnelManager = casesPersonnelManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public void setPartyManager(PartyManager partyManager) {
        this.partyManager = partyManager;
    }
}
