package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.AdministrativeCategoryManager;
import com.irsa.cases.model.AdministrativeCategory;

@Service
public class AdministrativeCategoryConsoleServiceImpl implements AdministrativeCategoryConsoleService {
    Logger log = LoggerFactory.getLogger(AdministrativeCategoryConsoleServiceImpl.class);
    @Autowired
    AdministrativeCategoryManager administrativeCategoryManager;
    
    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(administrativeCategoryManager.getList(AdministrativeCategory.SELECT_ALL, null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String parentId, String id, String title) {
        try {
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写名称");
            }
            
            if (StringUtils.isBlank(parentId) || "-1".equals(parentId)) {//父级
                int num = administrativeCategoryManager.getListCount(AdministrativeCategory.SELECT_EXIST_TITLE_4_PARENT, new Object[]{id, title});
                if (num != 0) {
                    return Utils.getErrorMap("名称已存在");
                }
                
                parentId = null;
            } else {//子级
                int num = administrativeCategoryManager.getListCount(AdministrativeCategory.SELECT_EXIST_TITLE_4_CHILD, new Object[]{id, parentId, title});
                if (num != 0) {
                    return Utils.getErrorMap("名称已存在");
                }
            }
            int num = administrativeCategoryManager.getListCount(AdministrativeCategory.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
                int sequence = administrativeCategoryManager.getListCount(AdministrativeCategory.SELECT_EXIST, null);
                administrativeCategoryManager.executeSQL(AdministrativeCategory.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), sequence + 1, parentId, title});
            } else {
                administrativeCategoryManager.executeSQL(AdministrativeCategory.UPDATE, new Object[]{parentId, title, id});
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
                administrativeCategoryManager.executeSQL(AdministrativeCategory.DELETE, new Object[]{id});
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
                administrativeCategoryManager.executeSQL(AdministrativeCategory.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList4Parent() {
        try {
            return Utils.getSuccessMap(Utils.changeToSelectList(administrativeCategoryManager.getList(AdministrativeCategory.SELECT_4_CHOOSE_PARENT, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList() {
        try {
            return Utils.getSuccessMap(Utils.changeToSelectList(administrativeCategoryManager.getList(AdministrativeCategory.SELECT_4_CHOOSE, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public AdministrativeCategoryManager getAdministrativeCategoryManager() {
        return administrativeCategoryManager;
    }
    public void setAdministrativeCategoryManager(
            AdministrativeCategoryManager administrativeCategoryManager) {
        this.administrativeCategoryManager = administrativeCategoryManager;
    }
}
