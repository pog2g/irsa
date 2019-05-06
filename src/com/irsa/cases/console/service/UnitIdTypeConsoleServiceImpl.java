package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.UnitIdTypeManager;
import com.irsa.cases.model.UnitIdType;

@Service
public class UnitIdTypeConsoleServiceImpl implements UnitIdTypeConsoleService {
    Logger log = LoggerFactory.getLogger(UnitIdTypeConsoleServiceImpl.class);
    @Autowired
    UnitIdTypeManager unitIdTypeManager;
    
    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(unitIdTypeManager.getList(UnitIdType.SELECT_ALL, null));
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
            int num = unitIdTypeManager.getListCount(UnitIdType.SELECT_BY_ID, new Object[]{id});
            if (num == 0) {
                int sequence = unitIdTypeManager.getListCount(UnitIdType.SELECT_ALL, null);
                unitIdTypeManager.executeSQL(UnitIdType.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), sequence + 1, title});
            } else {
                unitIdTypeManager.executeSQL(UnitIdType.UPDATE, new Object[]{title, id});
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
                unitIdTypeManager.executeSQL(UnitIdType.DELETE, new Object[]{id});
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
                unitIdTypeManager.executeSQL(UnitIdType.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
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
            return Utils.getSuccessMap(Utils.changeToSelectList(unitIdTypeManager.getList(UnitIdType.SELECT_ALL, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public UnitIdTypeManager getUnitIdTypeManager() {
        return unitIdTypeManager;
    }
    public void setUnitIdTypeManager(UnitIdTypeManager unitIdTypeManager) {
        this.unitIdTypeManager = unitIdTypeManager;
    }
}
