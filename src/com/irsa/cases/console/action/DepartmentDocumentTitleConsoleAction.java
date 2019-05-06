package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.DepartmentDocumentTitleConsoleService;

@Controller
@RequestMapping("/console/department_document_title")
public class DepartmentDocumentTitleConsoleAction {
    Logger log = LoggerFactory.getLogger(DepartmentDocumentTitleConsoleAction.class);
    @Autowired
    DepartmentDocumentTitleConsoleService departmentDocumentTitleConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="department_id") String departmentId) {
        Map<String, Object> result = departmentDocumentTitleConsoleService.getList(departmentId);
        log.info("[/console/department_document_title/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="title_id") String id, String title) {
        Map<String, Object> result = departmentDocumentTitleConsoleService.save(id, title); 
        log.info("[/console/department_document_title/save] {}", result);
        return result;
    }

    public DepartmentDocumentTitleConsoleService getDepartmentDocumentTitleConsoleService() {
        return departmentDocumentTitleConsoleService;
    }
    public void setDepartmentDocumentTitleConsoleService(
            DepartmentDocumentTitleConsoleService departmentDocumentTitleConsoleService) {
        this.departmentDocumentTitleConsoleService = departmentDocumentTitleConsoleService;
    }
}
