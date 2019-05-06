package com.irsa.console.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irsa.console.manager.AccountManager;
import com.irsa.console.manager.RoleManager;
import com.irsa.console.model.Account;
import com.irsa.console.model.AccountLog;
import com.irsa.console.model.Role;
import com.common.utils.Utils;

@Service
public class AccountServiceImpl implements AccountService {
    Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    AccountManager accountManager;
    @Autowired
    RoleManager roleManager;
    
    @Override
    public Map<String, Object> getAccountList(String accountId) {
        try {
            Map<String, Object> account = accountManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            List<Map<String, Object>> accountList = accountManager.getList(Account.SELECT_CHOOSE_BY_DEOARTMENTID, new Object[]{account.get("department_id")});
            return Utils.getSuccessMap(accountList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getList(String accountId) {
        try {
            Map<String, Object> account = accountManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            String sql = "";
            Object[] param = null;
            if (!"1".equals(account.get("department_id"))) {
                sql = Account.SELECT_BY_DEOARTMENTID;
                param = new Object[]{account.get("department_id")};
            } else {
                sql = Account.SELECT_ALL;
            }
            List<Map<String, Object>> accountList = accountManager.getList(sql, param);
//            for (Map<String, Object> data : accountList) {
//                data.put("competence", roleManager.getRoleCompetenceList(account.get("role_id")));
//            }
            return Utils.getSuccessMap(accountList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String account, String nickname, String gender, String phone, String idNumber, String pwd, String confirmPwd, String roleId) {
        try {
            if (StringUtils.isBlank(roleId) || "-1".equals(roleId)) {
                return Utils.getErrorMap("请选择角色");
            }
            Map<String, Object> role = accountManager.getMap(Role.SELECT_EXIST_ID, new Object[]{roleId});
            if (role == null) {
                return Utils.getErrorMap("管理组不存在");
            }
            if (StringUtils.isBlank(account)) {
                return Utils.getErrorMap("请填写用户名");
            }
            int num = accountManager.getListCount(Account.SELECT_EXIST_ACCOUNT, new Object[]{role.get("department_id"), id, account});
            if (num != 0) {
                return Utils.getErrorMap("用户名已存在");
            }
            if (StringUtils.isBlank(nickname)) {
                return Utils.getErrorMap("请填写姓名");
            }
            if (StringUtils.isBlank(gender) || (!Account.GENDER_1.equals(gender) && !Account.GENDER_2.equals(gender) && !Account.GENDER_3.equals(gender))) {
                return Utils.getErrorMap("请选择性别");
            }
            if (StringUtils.isBlank(phone)) {
                return Utils.getErrorMap("请填写联系方式");
            }
            if (StringUtils.isBlank(idNumber)) {
                return Utils.getErrorMap("请填写证件号");
            }
            if (StringUtils.isBlank(pwd)) {
                return Utils.getErrorMap("请填写密码");
            }
            if (StringUtils.isBlank(confirmPwd)) {
                return Utils.getErrorMap("请填写确认密码");
            }
            if (!Utils.checkPwd(pwd)) {
                return Utils.getErrorMap("密码格式错误");
            }
            if (!pwd.equals(confirmPwd)) {
                return Utils.getErrorMap("密码不一致");
            }
            num = accountManager.getListCount(Account.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
                accountManager.executeSQL(Account.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), account, nickname, gender, phone, idNumber, pwd, roleId});
            } else {
                accountManager.executeSQL(Account.UPDATE, new Object[]{account, nickname, gender, phone, idNumber, pwd, roleId, id});
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
                accountManager.executeSQL(Account.DELETE, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> login(HttpServletRequest request, String departmentId, String account, String pwd) {
        try {
            if (StringUtils.isBlank(departmentId)){
                departmentId = "1";
            }
            if (StringUtils.isBlank(account)) {
                return Utils.getErrorMap("请填写用户名");
            }
            if (StringUtils.isBlank(pwd)) {
                return Utils.getErrorMap("请填写密码");
            }
            
            int num = accountManager.getListCount(Account.SELECT_EXIST_ACCOUNT, new Object[]{departmentId, "", account});
            if (num == 0) {
                return Utils.getErrorMap("用户名不存在");
            }
            Map<String, Object> user = accountManager.getMap(Account.SELECT_BY_ACCOUNT_PWD, new Object[]{departmentId, account, pwd});
            if (user == null) {
                return Utils.getErrorMap("密码错误");
            } 
            request.getSession().setAttribute("account", user.get("id"));
            accountManager.executeSQL(AccountLog.INSERT_1, new Object[]{Utils.getId(), Utils.getCreateTime(), user.get("id"), request.getRemoteAddr()});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getAccount(HttpServletRequest request) {
        try {
            Object id = request.getSession().getAttribute("account");
            if (id == null || StringUtils.isBlank(id.toString())) {
                return Utils.getErrorMap(null);
            }
            Map<String, Object> account = accountManager.getMap(Account.SELECT_BY_ID, new Object[]{id});
            if (account == null) {
                return Utils.getErrorMap(null);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("role", account.get("role_name"));
            data.put("nickname", account.get("nickname"));
            data.put("phone", account.get("phone"));
            data.put("last_ip", account.get("last_ip"));
            data.put("last_time", account.get("last_time"));
            data.put("department", account.get("department_name"));
            data.put("competence", roleManager.getRoleCompetenceTree(account.get("role_id"), "1"));
            //data.put("department_number", roleManager.getList(DepartmentDocumentNumber.SELECT_BY_DEPARTMENTID, new Object[]{account.get("department_id")}));
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
    public RoleManager getRoleManager() {
        return roleManager;
    }
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
}
