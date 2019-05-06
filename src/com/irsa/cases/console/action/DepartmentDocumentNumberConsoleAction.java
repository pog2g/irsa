package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.DepartmentDocumentNumberConsoleService;

@Controller
@RequestMapping("/console/department_document_number")
public class DepartmentDocumentNumberConsoleAction {
    Logger log = LoggerFactory.getLogger(DepartmentDocumentNumberConsoleAction.class);
    @Autowired
    DepartmentDocumentNumberConsoleService departmentDocumentNumberConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="department_id") String departmentId) {
        Map<String, Object> result = departmentDocumentNumberConsoleService.getList(departmentId);
        log.info("[/console/department_document_number/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="number_id") String id, 
            @RequestParam(required=false, name="number_1") String number1, 
            @RequestParam(required=false, name="number_2") String number2, 
            @RequestParam(required=false, name="number_3") String number3) {
        Map<String, Object> result = departmentDocumentNumberConsoleService.save(id, number1, number2, number3); 
        log.info("[/console/department_document_number/save] {}", result);
        return result;
    }
    
    @RequestMapping("/save_html")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="html_id") String id, String html) {
        Map<String, Object> result = departmentDocumentNumberConsoleService.saveHtml(id, html); 
        log.info("[/console/department_document_number/save_html] {}", result);
        return result;
    }
    
    @RequestMapping("/get_document_html")
    @ResponseBody
    public Map<String, Object> getDocumentHtml(@RequestParam(required=false, name="cases_id") String casesId, String state) {
        Map<String, Object> result = departmentDocumentNumberConsoleService.getDocumentHtml(casesId, state); 
        log.info("[/console/department_document_number/get_document_html] {}", result);
        return result;
    }

    public DepartmentDocumentNumberConsoleService getDepartmentDocumentNumberConsoleService() {
        return departmentDocumentNumberConsoleService;
    }
    public void setDepartmentDocumentNumberConsoleService(
            DepartmentDocumentNumberConsoleService departmentDocumentNumberConsoleService) {
        this.departmentDocumentNumberConsoleService = departmentDocumentNumberConsoleService;
    }
}
