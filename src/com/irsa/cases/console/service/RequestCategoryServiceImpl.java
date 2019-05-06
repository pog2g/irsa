package com.irsa.cases.console.service;

import com.common.utils.Utils;
import com.irsa.cases.manager.RequestCategoryManager;
import com.irsa.cases.model.RequestCategory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RequestCategoryServiceImpl implements RequestCategoryService {

    Logger log = LoggerFactory.getLogger(RequestCategoryServiceImpl.class);

    @Autowired
    RequestCategoryManager requestCategoryManager;

    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(requestCategoryManager.getList(RequestCategory.SELECT_ALL, null));
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
            int num = requestCategoryManager.getListCount(RequestCategory.SELECT_BY_ID, new Object[]{id});
            if (num == 0) {
                int sequence = requestCategoryManager.getListCount(RequestCategory.SELECT_ALL, null);
                requestCategoryManager.executeSQL(RequestCategory.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), sequence + 1, title});
            } else {
                requestCategoryManager.executeSQL(RequestCategory.UPDATE, new Object[]{title, id});
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
                requestCategoryManager.executeSQL(RequestCategory.DELETE, new Object[]{id});
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
                requestCategoryManager.executeSQL(RequestCategory.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
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
            return Utils.getSuccessMap(Utils.changeToSelectList(requestCategoryManager.getList(RequestCategory.SELECT_ALL, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public RequestCategoryManager getRequestCategoryManager() {
        return requestCategoryManager;
    }
    public void setRequestCategoryManager(
            RequestCategoryManager requestCategoryManager) {
        this.requestCategoryManager = requestCategoryManager;
    }
}
