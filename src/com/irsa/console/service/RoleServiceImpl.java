package com.irsa.console.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irsa.console.manager.RoleManager;
import com.irsa.console.model.Account;
import com.irsa.console.model.Role;
import com.irsa.console.model.RoleCompetence;
import com.common.utils.Utils;

@Service
public class RoleServiceImpl implements RoleService {
    Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    RoleManager roleManager;
    
    @Override
    public Map<String, Object> getList(String accountId) {
        try {
            Map<String, Object> account = roleManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            
            StringBuffer sql = new StringBuffer(Role.SELECT);
            Object[] param = null;
            if (!"1".equals(account.get("department_id"))) {
                sql.append("and r.department_id = ? ");
                param = new Object[]{account.get("department_id")};
            } 
            sql.append("order by r.department_id,competences desc,r.title");
            List<Map<String, Object>> roleList = roleManager.getList(sql.toString(), param);
            for (Map<String, Object> role : roleList) {
                List<Map<String, Object>> competenceIdList = roleManager.getList(RoleCompetence.SELECT_BY_ROLEID, new Object[]{role.get("id")});
                StringBuffer competenceIds = new StringBuffer();
                for (int i = 0; i < competenceIdList.size(); i++) {
                    Map<String, Object> competenceId = competenceIdList.get(i);
                    if (i == competenceIdList.size() - 1) {
                        competenceIds.append(competenceId.get("competence_id"));
                    } else {
                        competenceIds.append(competenceId.get("competence_id")).append(",");
                    }
                }
                role.put("competence_ids", competenceIds.toString());
                role.put("competence", roleManager.getRoleCompetenceList(role.get("id")));
            }
            return Utils.getSuccessMap(roleList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String deparmentId, String title, String remark, List<String> competenceIds) {
        try {
            if (StringUtils.isBlank(deparmentId) || "-1".equals(deparmentId)) {
                return Utils.getErrorMap("请选择所在单位");
            }
            if (competenceIds == null || competenceIds.size() == 0) {
                return Utils.getErrorMap("请选择权限");
            }
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写名称");
            }
            int num = roleManager.getListCount(Role.SELECT_EXIST_DEPARTMENTID_TITLE, new Object[]{id, deparmentId, title});
            if (num != 0) {
                return Utils.getErrorMap("名称已存在");
            }
            num = roleManager.getListCount(Role.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
                id = Utils.getId();
                roleManager.executeSQL(Role.INSERT, new Object[]{id, Utils.getCreateTime(), deparmentId, title, remark});
            } else {
                roleManager.executeSQL(Role.UPDATE, new Object[]{deparmentId, title, remark, id});
            }
            //先清空
            roleManager.executeSQL(RoleCompetence.DELETE_BY_ROLEID, new Object[]{id});
            //后保存
            for (String competenceId : competenceIds) {
                roleManager.executeSQL(RoleCompetence.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), id, competenceId});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> delete(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择数据");
            }
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                int num = roleManager.getListCount(Account.SELECT_EXIST_ROLEID, new Object[]{id});
                if (num != 0) {
                    return Utils.getErrorMap("正在使用中不能删除");
                }
                roleManager.executeSQL(Role.DELETE, new Object[]{id});
                roleManager.executeSQL(RoleCompetence.DELETE_BY_ROLEID, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList(String parent) {
        try {
            return Utils.getSuccessMap(Utils.changeToSelectList(roleManager.getList(Role.SELECT_4_CHOOSE, new Object[]{parent})));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
}
