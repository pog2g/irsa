package com.irsa.cases.console.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.CasesReasonManager;
import com.irsa.cases.model.CasesReason;

@Service
public class CasesReasonConsoleServiceImpl implements CasesReasonConsoleService {
    Logger log = LoggerFactory.getLogger(CasesReasonConsoleServiceImpl.class);
    @Autowired
    CasesReasonManager casesReasonManager;
    
    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(casesReasonManager.getList(CasesReason.SELECT_ALL, null));
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
            int num = casesReasonManager.getListCount(CasesReason.SELECT_BY_ID, new Object[]{id});
            if (num == 0) {
                int sequence = casesReasonManager.getListCount(CasesReason.SELECT_ALL, null);
                casesReasonManager.executeSQL(CasesReason.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), sequence + 1, title});
            } else {
                casesReasonManager.executeSQL(CasesReason.UPDATE, new Object[]{title, id});
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
                casesReasonManager.executeSQL(CasesReason.DELETE, new Object[]{id});
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
                casesReasonManager.executeSQL(CasesReason.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList() {
        try {
            List<Map<String, Object>> list = casesReasonManager.getList(CasesReason.SELECT_ALL, null);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", "-2");
            data.put("text", "自定义案由");
            list.add(data);
            return Utils.getSuccessMap(Utils.changeToSelectList(list));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public CasesReasonManager getCasesReasonManager() {
        return casesReasonManager;
    }
    public void setCasesReasonManager(CasesReasonManager casesReasonManager) {
        this.casesReasonManager = casesReasonManager;
    }
}
