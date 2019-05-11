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
                // 处理casesPersonnelType为1的情况--申请人
                return dealWithType1(tempFile, actId, casesPersonnelType, casesId, personnelId,
                        type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone,
                        domicile, zipCode, contact, abode, unitName, unitContact, unitIdTypeId,
                        unitIdNo, unitAbode, legalPerson);
            } else if (CasesPersonnel.PERSONNEL_TYPE_3.equals(casesPersonnelType) || CasesPersonnel.PERSONNEL_TYPE_73.equals(casesPersonnelType)) {
                // 处理casesPersonnelType为3或73的情况的情况(73为从建议添加为正式第三人，因此存储入库局是73要改变为3)
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
     * 处理casesPersonnelType为1--申请人
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
     * @return Map ${@link Map}
     * @date 2019-05-08 11:23
     */
    private Map<String, Object> dealWithType1(Map<String, Object> tempFile, String actId, String casesPersonnelType, String casesId, String personnelId,
                                              String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                              String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        int isCasesExist = partyManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
        // 字段验证
        Map<String, Object> map = checkFeilds(tempFile, isCasesExist, casesPersonnelType, casesId,
                personnelId, type, name, birthday, idTypeId, idNo, phone, domicile,
                abode, unitName, unitContact, unitIdTypeId, unitIdNo);
        if (map != null) {
            return map;
        }
        if (StringUtils.isNotBlank(personnelId)) {
            partyManager.executeSQL(Party.UPDATE, new Object[]{type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone, domicile, zipCode, contact, abode,
                    unitName, unitContact, unitIdTypeId, unitIdNo, unitAbode, legalPerson, personnelId});
        } else {
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
     * 处理casesPersonnelType为3正式第三人
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
     * @return Map ${@link Map}
     * @date 2019-05-08 11:23
     */
    private Map<String, Object> dealWithType3(Map<String, Object> tempFile, String actId, String casesPersonnelType, String casesId, String personnelId,
                                              String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                              String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        // 验证必填字段，与申请人一致
        int isCasesExist = partyManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
        // 字段验证
        Map<String, Object> map = checkFeilds(tempFile, isCasesExist, casesPersonnelType, casesId,
                personnelId, type, name, birthday, idTypeId, idNo, phone, domicile,
                abode, unitName, unitContact, unitIdTypeId, unitIdNo);
        if (map != null) {
            return map;
        }
        return dealWithType7(tempFile, actId, casesPersonnelType, casesId, personnelId,
                type, name, other_name, nature, gender, birthday, idTypeId, idNo, phone,
                domicile, zipCode, contact, abode, unitName, unitContact, unitIdTypeId,
                unitIdNo, unitAbode, legalPerson);
    }


    /**
     * 处理casesPersonnelType为7建议第三人
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
     * @return Map ${@link Map}
     * @date 2019-05-08 11:23
     */
    private Map<String, Object> dealWithType7(Map<String, Object> tempFile, String actId, String casesPersonnelType, String casesId, String personnelId,
                                              String type, String name, String other_name, String nature, String gender, String birthday, String idTypeId, String idNo, String phone, String domicile, String zipCode, String contact, String abode,
                                              String unitName, String unitContact, String unitIdTypeId, String unitIdNo, String unitAbode, String legalPerson) {
        // 建议第三人只需要验证用户姓名不能为空
        if (StringUtils.isBlank(name)) {
            return Utils.getErrorMap("姓名不能为空");
        }
        // 如果personnelId存在则检查是否与当前案件绑定，如果绑定则直接返回，否则重新绑定
        if (StringUtils.isNotBlank(personnelId)) {
            int isPersonnelExist = partyManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
            if (isPersonnelExist != 0 && !CasesPersonnel.PERSONNEL_TYPE_73.equals(casesPersonnelType)) {
                return Utils.getErrorMap("第三人已存在或当前录入第三人也是申请人、被申请人");
            }
            // 存储信息
            if (CasesPersonnel.PERSONNEL_TYPE_73.equals(casesPersonnelType)) {
                casesPersonnelType = "3";
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
     * 处理申请人(个人与组织)、正式第三人字段验证
     *
     * @param tempFile           ${@link Map}
     * @param isCasesExist       ${@link Integer}
     * @param casesPersonnelType ${@link String}
     * @param casesId            ${@link String}
     * @param personnelId        ${@link String}
     * @param type               ${@link String}
     * @param name               ${@link String}
     * @param birthday           ${@link String}
     * @param idTypeId           ${@link String}
     * @param idNo               ${@link String}
     * @param phone              ${@link String}
     * @param domicile           ${@link String}
     * @param abode              ${@link String}
     * @param unitName           ${@link String}
     * @param unitContact        ${@link String}
     * @param unitIdTypeId       ${@link String}
     * @param unitIdNo           ${@link String}
     * @return Map ${@link Map}
     * @date 2019-05-11 10:55
     */
    private Map<String, Object> checkFeilds(Map<String, Object> tempFile, int isCasesExist, String casesPersonnelType, String casesId, String personnelId,
                                            String type, String name, String birthday, String idTypeId, String idNo, String phone, String domicile, String abode,
                                            String unitName, String unitContact, String unitIdTypeId, String unitIdNo) {
        //公共验证字段
        // 验证证件信息
        Map<String, Object> idType = partyManager.getMap(IdType.SELECT_BY_ID, new Object[]{idTypeId});
        Map<String, Object> file = partyManager.getMap(PartyFile.SELECT_BY_RESID, new Object[]{personnelId});
        if (idType == null || idType.isEmpty()) {
            return Utils.getErrorMap("请选择证件类型");
        }
        if (!"未知".equals(idType.get("text")) && tempFile == null && file == null) {
            return Utils.getErrorMap("请填上传证件信息");
        }
        // 验证文书送达地址
        if (StringUtils.isBlank(abode)) {
            return Utils.getErrorMap("请填写法律文书送达地址");
        }
        // 验证手机号码
        if (StringUtils.isBlank(phone) || !Utils.checkPhone(phone)) {
            return Utils.getErrorMap("请填写正确的手机号码");
        }
        // 验证姓名
        if (StringUtils.isBlank(name)) {
            return Utils.getErrorMap("请填姓名");
        }
        if ("1".equalsIgnoreCase(casesPersonnelType)) {
            if (StringUtils.isBlank(type) || "-1".equals(type)) {
                return Utils.getErrorMap("请选择申请人类型");
            }
        } else {
            if (StringUtils.isBlank(type) || "-1".equals(type)) {
                return Utils.getErrorMap("请选择第三人类型");
            }
        }

        // 其余字段验证
        if (Party.TYPE_1.equals(type)) {
            // 个人字段信息验证
            // 验证出生日期
            if (StringUtils.isBlank(birthday)) {
                return Utils.getErrorMap("请选择或输入生日");
            }
            String regex = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
            if (!birthday.matches(regex)) {
                return Utils.getErrorMap("请输入正确的出生日期");
            }
            // 证件类型
            if (idType == null) {
                return Utils.getErrorMap("证件类型不存在");
            }
            // 验证证件号码
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
            // 验证户籍所在地
            if (StringUtils.isBlank(domicile)) {
                return Utils.getErrorMap("请填写户籍所在地");
            }
        } else if (Party.TYPE_2.equals(type)) {
            // 组织字段验证
            if (StringUtils.isBlank(unitName)) {
                return Utils.getErrorMap("请填写单位名称");
            }
            // 单位联系方式
            if (StringUtils.isBlank(unitContact)) {
                return Utils.getErrorMap("请填写单位联系方式");
            }
            // 证件类型
            if (StringUtils.isBlank(unitIdTypeId) || "-1".equals(unitIdTypeId)) {
                return Utils.getErrorMap("请选择单位证件类型");
            }
            Map<String, Object> unitIdType = partyManager.getMap(UnitIdType.SELECT_BY_ID, new Object[]{unitIdTypeId});
            if (unitIdType == null) {
                return Utils.getErrorMap("证件类型不存在");
            }
            // 证件号码
            if (StringUtils.isBlank(unitIdNo)) {
                return Utils.getErrorMap("请填写单位证件号码");
            }

        }
        // 查看当事人是否存在
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
                    // 如果是从建议第三人添加为第三人，此时不需要验证用户信息是否重复
                    if (isPersonnelExist != 0 && !CasesPersonnel.PERSONNEL_TYPE_73.equals(casesPersonnelType)) {
                        return Utils.getErrorMap("当事人已存在");
                    }
                }
            }
        }
        return null;
    }
}
