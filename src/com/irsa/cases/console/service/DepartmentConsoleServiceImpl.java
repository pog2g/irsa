package com.irsa.cases.console.service;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.DepartmentManager;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.Department;
import com.irsa.cases.model.DepartmentDocumentNumber;
import com.irsa.cases.model.DepartmentDocumentTitle;
import com.irsa.console.model.Account;

@Service
public class DepartmentConsoleServiceImpl implements DepartmentConsoleService {
    Logger log = LoggerFactory.getLogger(DepartmentConsoleServiceImpl.class);
    @Autowired
    DepartmentManager departmentManager;
    
    @Override
    public Map<String, Object> getList(String accountId) {
        try {
            Map<String, Object> account = departmentManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            
            String sql = "";
            Object[] param = null;
            if (!"1".equals(account.get("department_id"))) {
                sql = Department.SELECT_BY_ID;
                param = new Object[]{account.get("department_id")};
            } else {
                sql = Department.SELECT_ALL;
            }
            return Utils.getSuccessMap(departmentManager.getList(sql, param));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String title) {
        try {
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写名称");
            }
            int num = departmentManager.getListCount(Department.SELECT_BY_TITLE, new Object[]{id, title});
            if (num != 0) {
                return Utils.getErrorMap("名称已存在");
            }
            
            num = departmentManager.getListCount(Department.SELECT_BY_ID, new Object[]{id});
            if (num == 0) {
                int sequence = departmentManager.getListCount(Department.SELECT_ALL, null);
                id = Utils.getId();
                departmentManager.executeSQL(Department.INSERT, new Object[]{id, Utils.getCreateTime(), sequence + 1, title});
                
                //保存默认文号
                for (Entry<String, String> entry : Cases.DOCUMENT_NUMBER.entrySet()) {
                    String[] numberArr = entry.getValue().split(",");
                    departmentManager.executeSQL(DepartmentDocumentNumber.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), id, 
                            entry.getKey(), numberArr[0], numberArr[1], numberArr[2], Cases.DOCUMENT_HTML.get(entry.getKey())});
                }
                departmentManager.executeSQL(DepartmentDocumentTitle.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), id, Cases.DOCUMENT_TITLE});
            } else {
                departmentManager.executeSQL(Department.UPDATE, new Object[]{title, id});
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
                departmentManager.executeSQL(Department.DELETE, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> sort(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择数据");
            }
            String[] idArr = ids.split(",");
            for (int i = 0; i < idArr.length; i++) {
                departmentManager.executeSQL(Department.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList(String accountId) {
        try {
            Map<String, Object> account = departmentManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            String sql = "";
            Object[] param = null;
            if (!"1".equals(account.get("department_id"))) {
                sql = Department.SELECT_BY_ID;
                param = new Object[]{account.get("department_id")};
            } else {
                sql = Department.SELECT_ALL;
            }
            return Utils.getSuccessMap(Utils.changeToSelectList(departmentManager.getList(sql, param)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getMap(String id) {
        try {
            return Utils.getSuccessMap(departmentManager.getMap(Department.SELECT_BY_ID, new Object[]{id}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public DepartmentManager getDepartmentManager() {
        return departmentManager;
    }
    public void setDepartmentManager(DepartmentManager departmentManager) {
        this.departmentManager = departmentManager;
    }
}
