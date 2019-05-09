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
    private final CasesPersonnelManager casesPersonnelManager;
    private final PartyManager partyManager;


    @Autowired
    public PartyConsoleServiceImpl(CasesPersonnelManager casesPersonnelManager, PartyManager partyManager) {
        this.casesPersonnelManager = casesPersonnelManager;
        this.partyManager = partyManager;
    }


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


    @Override
    public Map<String, Object> save(String actId, String casesPersonnelType, String casesId, String personnelId,
                                    String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                    String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        try {
            // 获取证件id
            if (StringUtils.isBlank(personnelId) && StringUtils.isNotBlank(idNo)) {
                Map<String, Object> party = partyManager.getMap(Party.SELECT_EXIST_BY_IDNO, new Object[]{idNo});
                if (party != null) {
                    personnelId = Utils.toString(party.get("id"));
                }
            }
            // 获取资源存储信息
            Map<String, Object> tempFile = partyManager.getMap(TempFile.SELECT_BY_RESID, new Object[]{actId});
            // 处理当事人、第三人等
            if (CasesPersonnel.PERSONNEL_TYPE_1.equals(casesPersonnelType)) {
                // 处理casesPersonnelType为1的情况
                return dealWithType1(tempFile, actId, casesPersonnelType, casesId, personnelId,
                        type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone,
                        domicile, zipCode, contact, abode, unitName, unitContact, unitIdTypeId,
                        unitIdNo, unitAbode, legalPerson);
            } else if (CasesPersonnel.PERSONNEL_TYPE_3.equals(casesPersonnelType)) {
                // 处理casesPersonnelType为3的情况
                return dealWithType3(tempFile, actId, casesPersonnelType, casesId, personnelId,
                        type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone,
                        domicile, zipCode, contact, abode, unitName, unitContact, unitIdTypeId,
                        unitIdNo, unitAbode, legalPerson);
            } else if (CasesPersonnel.PERSONNEL_TYPE_7.equals(casesPersonnelType)) {
                // 处理casesPersonnelType为7的情况
                return dealWithType7(tempFile, actId, casesPersonnelType, casesId, personnelId,
                        type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone,
                        domicile, zipCode, contact, abode, unitName, unitContact, unitIdTypeId,
                        unitIdNo, unitAbode, legalPerson);
            }
            return Utils.getErrorMap("案件人员类型错误");

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


    @Override
    public Map<String, Object> checkIdentity(String identity) {

        return null;
    }


    /**
     * 处理casesPersonnelType为1
     *
     * @param actId              ${@link String}
     * @param casesPersonnelType ${@link String}
     * @param casesId            ${@link String}
     * @param personnelId        ${@link String}
     * @param type               ${@link String}
     * @param name               ${@link String}
     * @param other_name         ${@link String}
     * @param nature             ${@link String}
     * @param gender             ${@link String}
     * @param birthday           ${@link String}
     * @param idTypeId           ${@link String}
     * @param idNo               ${@link String}
     * @param phone              ${@link String}
     * @param domicile           ${@link String}
     * @param zipCode            ${@link String}
     * @param contact            ${@link String}
     * @param abode              ${@link String}
     * @param unitName           ${@link String}
     * @param unitContact        ${@link String}
     * @param unitIdTypeId       ${@link String}
     * @param unitIdNo           ${@link String}
     * @param unitAbode          ${@link String}
     * @param legalPerson        ${@link String}
     * @param tempFile           ${@link Map}
     * @return Object ${@link Object}
     * @date 2019-05-08 11:23
     */
    private Map<String, Object> dealWithType1(Map<String, Object> tempFile, String actId, String casesPersonnelType, String casesId, String personnelId,
                                              String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                              String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        // 必填字段验证，与正式第三人一样
        Map<String, Object> map = checkFeilds(tempFile, personnelId, type, name, birthday, idTypeId, idNo, phone, domicile, abode);
        if (map != null) {
            return map;
        }
        int isCasesExist = partyManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
        if (Party.TYPE_2.equals(type)) {
            if (StringUtils.isBlank(legalPerson)) {
                return Utils.getErrorMap("请填写单位名称");
            }
            if (StringUtils.isBlank(unitIdTypeId) || "-1".equals(unitIdTypeId)) {
                return Utils.getErrorMap("请选择单位证件类型");
            }
            Map<String, Object> unitIdType = partyManager.getMap(UnitIdType.SELECT_BY_ID, new Object[]{unitIdTypeId});
            if (unitIdType == null) {
                return Utils.getErrorMap("证件类型不存在");
            }
            if (StringUtils.isBlank(unitIdNo)) {
                return Utils.getErrorMap("请填写单位证件号码");
            }
            if (StringUtils.isBlank(unitContact)) {
                return Utils.getErrorMap("请填写单位联系方式");
            }
        }
        if (StringUtils.isBlank(idTypeId) || "-1".equals(idTypeId)) {
            return Utils.getErrorMap("请选择证件类型");
        }
        Map<String, Object> idType = partyManager.getMap(IdType.SELECT_BY_ID, new Object[]{idTypeId});
        if (idType == null) {
            return Utils.getErrorMap("证件类型不存在");
        }
        if (!"未知".equals(idType.get("text")) && type != null && !type.equals("2")) {

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
        }
        if (StringUtils.isNotBlank(personnelId)) {
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

            Map<String, Object> file = partyManager.getMap(PartyFile.SELECT_BY_RESID, new Object[]{personnelId});
            if (!"未知".equals(idType.get("text")) && tempFile == null && file == null) {
                return Utils.getErrorMap("请填上传证件信息");
            }

            partyManager.executeSQL(Party.UPDATE, new Object[]{type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                    unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson, personnelId});
        } else {
            if (!"未知".equals(idType.get("text")) && tempFile == null) {
                return Utils.getErrorMap("请填上传证件信息");
            }
            personnelId = Utils.getId();
            partyManager.executeSQL(Party.INSERT_LEGAL, new Object[]{Utils.getCreateTime(), personnelId, type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                    unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson});
        }
        // 处理资源问题
        dealTempFile(tempFile, personnelId);
        // 案件是否存在
        if (isCasesExist == 0) {
            partyManager.executeSQL(CasesPersonnelTemp.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesPersonnelType, casesId, personnelId, null});
            return Utils.getSuccessMap(null);
        }
        casesPersonnelManager.save4Party(casesPersonnelType, casesId, personnelId, null, null);
        return Utils.getSuccessMap(null);
    }


    /**
     * 处理casesPersonnelType为3处理正式第三人(在建议第三人基础上加上验证信息，验证信息与)
     *
     * @param actId              ${@link String}
     * @param casesPersonnelType ${@link String}
     * @param casesId            ${@link String}
     * @param personnelId        ${@link String}
     * @param type               ${@link String}
     * @param name               ${@link String}
     * @param other_name         ${@link String}
     * @param nature             ${@link String}
     * @param gender             ${@link String}
     * @param birthday           ${@link String}
     * @param idTypeId           ${@link String}
     * @param idNo               ${@link String}
     * @param phone              ${@link String}
     * @param domicile           ${@link String}
     * @param zipCode            ${@link String}
     * @param contact            ${@link String}
     * @param abode              ${@link String}
     * @param unitName           ${@link String}
     * @param unitContact        ${@link String}
     * @param unitIdTypeId       ${@link String}
     * @param unitIdNo           ${@link String}
     * @param unitAbode          ${@link String}
     * @param legalPerson        ${@link String}
     * @param tempFile           ${@link Map}
     * @return Object ${@link Object}
     * @date 2019-05-08 11:23
     */
    private Map<String, Object> dealWithType3(Map<String, Object> tempFile, String actId, String casesPersonnelType, String casesId, String personnelId,
                                              String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                              String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        // 验证必填字段，与申请人一致
        Map<String, Object> map = checkFeilds(tempFile, personnelId, type, name, birthday, idTypeId, idNo, phone, domicile, abode);
        if (map != null) {
            return map;
        }
        System.out.println("=========dealWithType3=");
        // 存储信息
        return dealWithType7(tempFile, actId, casesPersonnelType, casesId, personnelId,
                type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone,
                domicile, zipCode, contact, abode, unitName, unitContact, unitIdTypeId,
                unitIdNo, unitAbode, legalPerson);
    }


    /**
     * 处理casesPersonnelType为7---建议第三人
     *
     * @param actId              ${@link String}
     * @param casesPersonnelType ${@link String}
     * @param casesId            ${@link String}
     * @param personnelId        ${@link String}
     * @param type               ${@link String}
     * @param name               ${@link String}
     * @param other_name         ${@link String}
     * @param nature             ${@link String}
     * @param gender             ${@link String}
     * @param birthday           ${@link String}
     * @param idTypeId           ${@link String}
     * @param idNo               ${@link String}
     * @param phone              ${@link String}
     * @param domicile           ${@link String}
     * @param zipCode            ${@link String}
     * @param contact            ${@link String}
     * @param abode              ${@link String}
     * @param unitName           ${@link String}
     * @param unitContact        ${@link String}
     * @param unitIdTypeId       ${@link String}
     * @param unitIdNo           ${@link String}
     * @param unitAbode          ${@link String}
     * @param legalPerson        ${@link String}
     * @param tempFile           ${@link Map}
     * @return Object> ${@link Object>}
     * @date 2019-05-08 11:23
     */
    private Map<String, Object> dealWithType7(Map<String, Object> tempFile, String actId, String casesPersonnelType, String casesId, String personnelId,
                                              String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                              String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        // 建议第三人只需要验证用户姓名不能为空
        if (StringUtils.isBlank(name) && StringUtils.isBlank(other_name)) {
            return Utils.getErrorMap("姓名不能为空");
        }
        // 如果personnelId存在则检查是否与当前案件绑定，如果绑定则直接返回，否则重新绑定
        if (StringUtils.isNotBlank(personnelId)) {
            int isPersonnelExist = partyManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
            if (isPersonnelExist != 0) {
                return Utils.getErrorMap("第三人已存在或当前录入第三人也是申请人、被申请人");
            }
            partyManager.executeSQL(Party.UPDATE, new Object[]{type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                    unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson, personnelId});
            // 如果personnelId不存在创建新的personnelId与案件绑定
        } else {
            personnelId = Utils.getId();
            if (StringUtils.isBlank(birthday)) {
                birthday = null;
            }
            partyManager.executeSQL(Party.INSERT_LEGAL, new Object[]{Utils.getCreateTime(), personnelId, type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                    unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson});
        }

        // 处理资源
        dealTempFile(tempFile, personnelId);
        partyManager.executeSQL(CasesPersonnelTemp.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesPersonnelType, casesId, personnelId, null});
        return Utils.getSuccessMap(null);
    }


    /**
     * 处理资源
     *
     * @param tempFile    ${@link Map}
     * @param personnelId ${@link String}
     * @date 2019-05-08 17:36
     */
    private void dealTempFile(Map<String, Object> tempFile, String personnelId) {
        if (tempFile != null) {
            partyManager.executeSQL(PartyFile.DELETE_BY_RESID, new Object[]{personnelId});
            Utils4File.copyTempToReal(Utils4File.PATH_PARTY, tempFile.get("file_name").toString());
            partyManager.executeSQL(PartyFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), tempFile.get("type"), personnelId,
                    tempFile.get("real_name"), tempFile.get("size"), tempFile.get("ext"), tempFile.get("file_name")});
        }

    }


    /**
     * 必填字段验证
     *
     * @param tempFile    ${@link String}
     * @param personnelId ${@link String}
     * @param type        ${@link String}
     * @param name        ${@link String}
     * @param birthday    ${@link String}
     * @param idTypeId    ${@link String}
     * @param idNo        ${@link String}
     * @param phone       ${@link String}
     * @param domicile    ${@link String}
     * @param abode       ${@link String}
     * @return Object> ${@link Object>}
     * @date 2019-05-09 22:52
     */
    private Map<String, Object> checkFeilds(Map<String, Object> tempFile, String personnelId, String type, String name,
                                            String birthday, String idTypeId, String idNo, String phone, String domicile, String abode) {
        if (StringUtils.isBlank(birthday)) {
            return Utils.getErrorMap("请选择或输入生日");
        }
        String regex = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
        if (!birthday.matches(regex)) {
            return Utils.getErrorMap("请输入正确的出生日期");
        }
        if (StringUtils.isBlank(name)) {
            return Utils.getErrorMap("请输入姓名");
        }
        if (!Utils.checkPhone(phone)) {
            return Utils.getErrorMap("手机号码格式错误");
        }
        Map<String, Object> idType = partyManager.getMap(IdType.SELECT_BY_ID, new Object[]{idTypeId});
        if (idType == null) {
            return Utils.getErrorMap("证件类型不存在");
        }
        if (!"未知".equals(idType.get("text")) && type != null && !type.equals("2")) {
            if (StringUtils.isBlank(idNo)) {
                return Utils.getErrorMap("请填写证件号码");
            }
            if ("中国居民二代身份证".equals(idType.get("text"))) {
                if (!Utils.checkIdNo(idNo)) {
                    return Utils.getErrorMap("证件号码格式错误");
                }
            }
        }
        if (StringUtils.isBlank(domicile)) {
            return Utils.getErrorMap("请填写户籍所在地");
        }
        if (StringUtils.isBlank(abode)) {
            return Utils.getErrorMap("请填写法律文书送达地址");
        }
        Map<String, Object> file = partyManager.getMap(PartyFile.SELECT_BY_RESID, new Object[]{personnelId});
        if (!"未知".equals(idType.get("text")) && tempFile == null && file == null) {
            return Utils.getErrorMap("请填上传证件信息");
        }
        return null;
    }
}
