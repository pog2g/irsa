package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.CasesReasonConsoleService;

@Controller
@RequestMapping("/console/cases_reason")
public class CasesReasonConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesReasonConsoleAction.class);
    @Autowired
    CasesReasonConsoleService casesReasonConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = casesReasonConsoleService.getList();
        log.info("[/console/cases_reason/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="reason_id") String id, 
            @RequestParam(required=false, name="reason_title") String title) {
        Map<String, Object> result = casesReasonConsoleService.save(id, title); 
        log.info("[/console/cases_reason/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = casesReasonConsoleService.delete(ids);
        log.info("[/console/cases_reason/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = casesReasonConsoleService.sort(ids);
        log.info("[/console/cases_reason/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = casesReasonConsoleService.getChooseList();
        log.info("[/console/cases_reason/get_choose_list] {}", result);
        return result;
    }

    public CasesReasonConsoleService getCasesReasonConsoleService() {
        return casesReasonConsoleService;
    }
    public void setCasesReasonConsoleService(
            CasesReasonConsoleService casesReasonConsoleService) {
        this.casesReasonConsoleService = casesReasonConsoleService;
    }
}
