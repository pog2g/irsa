package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.IdTypeManager;
import com.irsa.cases.model.IdType;

@Service
public class IdTypeConsoleServiceImpl implements IdTypeConsoleService {
    Logger log = LoggerFactory.getLogger(IdTypeConsoleServiceImpl.class);
    @Autowired
    IdTypeManager idTypeManager;
    
    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(idTypeManager.getList(IdType.SELECT_ALL, null));
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
            int num = idTypeManager.getListCount(IdType.SELECT_BY_ID, new Object[]{id});
            if (num == 0) {
                int sequence = idTypeManager.getListCount(IdType.SELECT_ALL, null);
                idTypeManager.executeSQL(IdType.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), sequence + 1, title});
            } else {
                idTypeManager.executeSQL(IdType.UPDATE, new Object[]{title, id});
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
                idTypeManager.executeSQL(IdType.DELETE, new Object[]{id});
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
                idTypeManager.executeSQL(IdType.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
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
            return Utils.getSuccessMap(Utils.changeToSelectList(idTypeManager.getList(IdType.SELECT_ALL, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public IdTypeManager getIdTypeManager() {
        return idTypeManager;
    }
    public void setIdTypeManager(IdTypeManager idTypeManager) {
        this.idTypeManager = idTypeManager;
    }
}
