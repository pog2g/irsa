package com.irsa.cases.console.service;

import java.text.MessageFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.DepartmentDocumentNumberManager;
import com.irsa.cases.model.DepartmentDocumentNumber;

@Service
public class DepartmentDocumentNumberConsoleServiceImpl implements DepartmentDocumentNumberConsoleService {
    Logger log = LoggerFactory.getLogger(DepartmentDocumentNumberConsoleServiceImpl.class);
    @Autowired
    DepartmentDocumentNumberManager departmentDocumentNumberManager;
    
    @Override
    public Map<String, Object> getList(String departmentId) {
        try {
            return Utils.getSuccessMap(departmentDocumentNumberManager.getList(DepartmentDocumentNumber.SELECT_BY_DEPARTMENTID, new Object[]{departmentId}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> save(String id, String numbner1, String numbner2, String numbner3) {
        try {
            departmentDocumentNumberManager.executeSQL(DepartmentDocumentNumber.UPDATE, new Object[]{numbner1, numbner2, numbner3, id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> saveHtml(String id, String html) {
        try {
            departmentDocumentNumberManager.executeSQL(DepartmentDocumentNumber.UPDATE_HTML, new Object[]{html, id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getDocumentHtml(String casesId, String state) {
        try {
            String html = "";
            String sql = "select t.title,n.number_1,n.number_2,n.number_3,n.html " +
            		"from irsa_cases c,irsa_console_account a,irsa_console_role r,irsa_department_document_number n,irsa_department_document_title t " +
            		"where c.id = ? and n.state = ? " +
            		"and c.entry_account_id = a.id and a.role_id = r.id  and n.department_id = r.department_id and t.department_id = r.department_id";
            Map<String, Object> document = departmentDocumentNumberManager.getMap(sql, new Object[]{casesId, state});
            if (document == null) {
            	return Utils.getSuccessMap(html);
            }
            
            if ("9".equals(state) || "15".equals(state)) {
        		html = MessageFormat.format(Utils.toString(document.get("html")), 
        				document.get("title"), document.get("number_1"), "", document.get("number_2"), "", document.get("number_3"),
        				"", "", "", "", "", "", "", "", "");
        		return Utils.getSuccessMap(html);
        	}
            if ("10".equals(state)) {
        		html = MessageFormat.format(Utils.toString(document.get("html")), 
        				document.get("title"), document.get("number_1"), "", document.get("number_2"), "", document.get("number_3"),
        				"", "", "", "", "", "");
        		return Utils.getSuccessMap(html);
        	}
            if ("11".equals(state)) {
        		html = MessageFormat.format(Utils.toString(document.get("html")), 
        				document.get("title"), document.get("number_1"), "", document.get("number_2"), "", document.get("number_3"),
        				"", "", "", "", "", "", "", "");
        		return Utils.getSuccessMap(html);
        	}
            html = MessageFormat.format(Utils.toString(document.get("html")), 
    				document.get("title"), document.get("number_1"), document.get("number_2"), document.get("number_3"));
            return Utils.getSuccessMap(html);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
        
    }

    public DepartmentDocumentNumberManager getDepartmentDocumentNumberManager() {
        return departmentDocumentNumberManager;
    }
    public void setDepartmentDocumentNumberManager(
            DepartmentDocumentNumberManager departmentDocumentNumberManager) {
        this.departmentDocumentNumberManager = departmentDocumentNumberManager;
    }
 }
