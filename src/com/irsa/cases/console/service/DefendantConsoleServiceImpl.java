package com.irsa.cases.console.service;

import com.common.utils.Utils;
import com.irsa.cases.manager.CasesPersonnelManager;
import com.irsa.cases.manager.DefendantManager;
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
public class DefendantConsoleServiceImpl implements DefendantConsoleService {
    Logger log = LoggerFactory.getLogger(DefendantConsoleServiceImpl.class);
    @Autowired
    CasesPersonnelManager casesPersonnelManager;
    @Autowired
    DefendantManager defendantManager;

    @Override
    public Map<String, Object> getList(String page, String key) {
        try {
            StringBuffer sql = new StringBuffer(Defendant.SELECT);
            Object[] param = null;
            if (StringUtils.isNotBlank(key)) {
                sql.append("and p.name like ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + key + "%"});
            }
            sql.append("order by p.account desc,p.type_id,p.name,p.createtime ");

            Map<String, Object> result = defendantManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("data_list");
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, Object> data : dataList) {
                    DefendantManager.getFileUrl(data);
                }
            }
            return Utils.getSuccessMap(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    /**
     * 保存被申请人到案件
     */
    @Override
    public Map<String, Object> save4Personnel(String actId, String casesId, String personnelId, String type, String unit_name, String unit_abode, String unit_contact, String name, String legal_person_type, String duty) {
        try {
            if (StringUtils.isBlank(type) || "-1".equals(type)) {
                return Utils.getErrorMap("请选择被申请人类型");
            }
            if (StringUtils.isBlank(unit_name)) {
                return Utils.getErrorMap("请填写被申请人名称");
            }
            if (StringUtils.isBlank(unit_abode)) {
                return Utils.getErrorMap("请填写被申请人住所");
            }
            if (StringUtils.isBlank(legal_person_type)) {
                return Utils.getErrorMap("请填写法定负责人的类型");
            }

            if (StringUtils.isBlank(name) && "2".equalsIgnoreCase(legal_person_type)) {
                return Utils.getErrorMap("请填写法定代表人姓名");
            }

            if (StringUtils.isBlank(name) && "1".equalsIgnoreCase(legal_person_type)) {
                return Utils.getErrorMap("请填写负责人姓名");
            }

            Map<String, Object> tempFile1 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{actId, DefendantFile.MODE_1});
            Map<String, Object> tempFile2 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{actId, DefendantFile.MODE_2});
            Map<String, Object> tempFile3 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{actId, DefendantFile.MODE_3});

            String pid = "";
            if (StringUtils.isNotBlank(personnelId)) {
                pid = personnelId;
            } else {
                Map<String, Object> defendant = defendantManager.getMap(Defendant.SELECT_EXIST_BY_NAME, new Object[]{name});
                if (defendant != null) {
                    pid = Utils.toString(defendant.get("id"));
                }
            }

            // 案件是否存在
            int isCasesExist = defendantManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            if (StringUtils.isNotBlank(pid)) {
                personnelId = pid;
                if (isCasesExist == 0) {
                    int isPersonnelExist = defendantManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
                    if (isPersonnelExist != 0) {
                        return Utils.getErrorMap("当事人已存在");
                    }
                } else {
                    int isPersonnelExist = defendantManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, personnelId});
                    if (isPersonnelExist != 0) {
                        return Utils.getErrorMap("当事人已存在");
                    }
                }

                /* update irsa_defendant set type_id = ?, unit_name = ?, unit_content = ?, unit_abode = ?, name = ?, legal_person_type = ?, duty = ? where id = ?*/
                defendantManager.executeSQL(Defendant.UPDATE, new Object[]{type, unit_name, unit_contact, unit_abode, name, legal_person_type, duty, personnelId});
            } else {
                personnelId = Utils.getId();
                /*insert into irsa_defendant(id,createtime,type_id,unit_name,unit_content,unit_abode,name,legal_person_type,duty,account,pwd)*/
                // 修改:原sql ps字段对应不上导致被申请人插入错误：defendantManager.executeSQL(Defendant.INSERT, new Object[]{personnelId, Utils.getCreateTime(), type , unit_name, name, unit_contact, unit_abode, name, legal_person_type, duty, "", ""});
                defendantManager.executeSQL(Defendant.INSERT, new Object[]{personnelId, Utils.getCreateTime(), type, unit_name, unit_contact, unit_abode, name, legal_person_type, duty, "", ""});
            }

            if (tempFile1 != null) {
                defendantManager.saveFile(personnelId, DefendantFile.MODE_1, tempFile1);
            }
            if (tempFile2 != null) {
                defendantManager.saveFile(personnelId, DefendantFile.MODE_2, tempFile2);
            }
            if (tempFile3 != null) {
                defendantManager.saveFile(personnelId, DefendantFile.MODE_3, tempFile3);
            }

            // 案件是否存在
            if (isCasesExist == 0) {
//                defendantManager.executeSQL(CasesPersonnelTemp.DELETE_BY_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_2});
                defendantManager.executeSQL(CasesPersonnelTemp.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), CasesPersonnel.PERSONNEL_TYPE_2, casesId, personnelId, null});
                return Utils.getSuccessMap(null);
            }

            casesPersonnelManager.save4Defendant(casesId, personnelId);
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> getChooseList(String page, String key) {
        try {
            StringBuffer sql = new StringBuffer(Defendant.SELECT_4_CHOOSE);
            Object[] param = new Object[]{"%" + Utils.changeISO88591ToUTF8(key) + "%"};
            ;
            return Utils.getSuccessMap(defendantManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }


    //注册被申请人
    @Override
    public Map<String, Object> save(String resId, String id, String typeId, String unit_name, String unit_abode, String unit_contact, String name, String legal_person_type, String duty, String account, String pwd, String pwdConfirm) {
        try {

            int num = defendantManager.getListCount(Defendant.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                return Utils.getErrorMap(null);
            }

            if (StringUtils.isBlank(typeId) || "-1".equals(typeId)) {
                return Utils.getErrorMap("请选择被申请人类型");
            }
            if (StringUtils.isBlank(unit_name)) {
                return Utils.getErrorMap("请填写被申请人名称");
            }
            if (StringUtils.isBlank(unit_abode)) {
                return Utils.getErrorMap("请填写被申请人住所");
            }
            if (StringUtils.isBlank(name)) {
                return Utils.getErrorMap("请填写法定代表人");
            }
            if (StringUtils.isBlank(account)) {
                return Utils.getErrorMap("请填写用户名");
            }
            if (account.length() < 6 || account.length() > 18) {
                return Utils.getErrorMap("用户名格式错误");
            }
            num = defendantManager.getListCount(Defendant.SELECT_EXIST_BY_NOID_ACCOUNT, new Object[]{id, account});
            if (num != 0) {
                return Utils.getErrorMap("用户名已存在");
            }

            if (StringUtils.isBlank(pwd)) {
                /*update irsa_defendant set type_id = ?,unit_name = ?, unit_content = ?, unit_abode = ?, name = ?, account = ? where id = ?*/
                defendantManager.executeSQL(Defendant.UPDATE_1, new Object[]{typeId, unit_name, unit_contact, unit_abode, name, account, id});
            } else {
                if (!Utils.checkPwd(pwd)) {
                    return Utils.getErrorMap("密码格式错误");
                }
                if (!pwd.equals(pwdConfirm)) {
                    return Utils.getErrorMap("两次输入的密码不一致");
                }
                defendantManager.executeSQL(Defendant.UPDATE_2, new Object[]{typeId, unit_name, unit_contact, unit_abode, name, account, Utils.MD5(pwd), id});
            }
            Map<String, Object> tempFile1 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{resId, DefendantFile.MODE_1});
            Map<String, Object> tempFile2 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{resId, DefendantFile.MODE_2});
            Map<String, Object> tempFile3 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{resId, DefendantFile.MODE_3});
            if (tempFile1 != null) {
                defendantManager.saveFile(id, DefendantFile.MODE_1, tempFile1);
            }
            if (tempFile2 != null) {
                defendantManager.saveFile(id, DefendantFile.MODE_2, tempFile2);
            }
            if (tempFile3 != null) {
                defendantManager.saveFile(id, DefendantFile.MODE_3, tempFile3);
            }
            return Utils.getSuccessMap(null);
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

    public DefendantManager getDefendantManager() {
        return defendantManager;
    }

    public void setDefendantManager(DefendantManager defendantManager) {
        this.defendantManager = defendantManager;
    }
}
