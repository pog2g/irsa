package com.irsa.cases.console.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.CasesRemarkManager;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.CasesRemark;
import com.irsa.console.model.Account;

@Service
public class CasesRemarkConsoleServiceImpl implements CasesRemarkConsoleService {
    Logger log = LoggerFactory.getLogger(CasesRemarkConsoleServiceImpl.class);
    @Autowired
    CasesRemarkManager casesRemarkManager;
    
    @Override
    public Map<String, Object> getList(String casesId, String accountId) {
        try {
            if (StringUtils.isBlank(accountId)) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            int num = casesRemarkManager.getListCount(Account.SELECT_EXIST_ID, new Object[]{accountId});
            if (num == 0) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            List<Map<String, Object>> list = casesRemarkManager.getList(CasesRemark.SELECT_BY_CASESID, new Object[]{casesId});
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    if (accountId.equals(data.get("account_id"))) {
                        data.put("editable", "1");
                    } else {
                        data.put("editable", "0");
                    }
                }
            }
            return Utils.getSuccessMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String casesId, String accountId, String remark) {
        try {
            if (StringUtils.isBlank(accountId)) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            int num = casesRemarkManager.getListCount(Account.SELECT_EXIST_ID, new Object[]{accountId});
            if (num == 0) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            if (StringUtils.isBlank(remark)) {
                return Utils.getErrorMap("请填写备注");
            }
            
            num = casesRemarkManager.getListCount(CasesRemark.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
                casesRemarkManager.executeSQL(CasesRemark.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesId, accountId, remark});
            } else {
                casesRemarkManager.executeSQL(CasesRemark.UPDATE, new Object[]{remark, id});
            }
            casesRemarkManager.executeSQL(Cases.UPDATE_LASTTIME, new Object[]{Utils.getCreateTime(), casesId});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> delete(String id) {
        try {
            casesRemarkManager.executeSQL(CasesRemark.DELETE, new Object[]{id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public CasesRemarkManager getCasesRemarkManager() {
        return casesRemarkManager;
    }
    public void setCasesRemarkManager(CasesRemarkManager casesRemarkManager) {
        this.casesRemarkManager = casesRemarkManager;
    }
}
