package com.irsa.cases.console.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.DepartmentDocumentTitleManager;
import com.irsa.cases.model.DepartmentDocumentTitle;

@Service
public class DepartmentDocumentTitleConsoleServiceImpl implements DepartmentDocumentTitleConsoleService {
    Logger log = LoggerFactory.getLogger(DepartmentDocumentTitleConsoleServiceImpl.class);
    @Autowired
    DepartmentDocumentTitleManager departmentDocumentTitleManager;
    
    @Override
    public Map<String, Object> getList(String departmentId) {
        try {
            return Utils.getSuccessMap(departmentDocumentTitleManager.getList(DepartmentDocumentTitle.SELECT_BY_DEPARTMENTID, new Object[]{departmentId}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> save(String id, String title) {
        try {
            departmentDocumentTitleManager.executeSQL(DepartmentDocumentTitle.UPDATE, new Object[]{title, id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public DepartmentDocumentTitleManager getDepartmentDocumentTitleManager() {
        return departmentDocumentTitleManager;
    }
    public void setDepartmentDocumentTitleManager(
            DepartmentDocumentTitleManager departmentDocumentTitleManager) {
        this.departmentDocumentTitleManager = departmentDocumentTitleManager;
    }
 }
