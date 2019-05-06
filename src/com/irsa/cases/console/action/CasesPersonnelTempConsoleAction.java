package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.CasesPersonnelTempConsoleService;

@Controller
@RequestMapping("/console/cases_personnel_temp")
public class CasesPersonnelTempConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesPersonnelTempConsoleAction.class);
    @Autowired
    CasesPersonnelTempConsoleService casesPersonnelTempConsoleService;
    
    @RequestMapping("/get_choose_list_4_apply")
    @ResponseBody
    public Map<String, Object> getChooseList4Apply(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesPersonnelTempConsoleService.getChooseList4Apply(casesId);
        log.info("[/console/cases_personnel_temp/get_choose_list_4_apply] {}", result);
        return result;
    }
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="cases_id") String casesId, String type) {
        Map<String, Object> result = casesPersonnelTempConsoleService.getList(casesId, type);
        log.info("[/console/cases_personnel_temp/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(required=true, name="id") String id) {
        Map<String, Object> result = casesPersonnelTempConsoleService.delete(id);
        log.info("[/console/cases_personnel_temp/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/set_representative")
    @ResponseBody
    public Map<String, Object> setRepresentative(String ids) {
        Map<String, Object> result = casesPersonnelTempConsoleService.setRepresentative(ids);
        log.info("[/console/cases_personnel_temp/set_representative] {}", result);
        return result;
    }
    
    @RequestMapping("/delete_representative")
    @ResponseBody
    public Map<String, Object> deleteRepresentative(String id) {
        Map<String, Object> result = casesPersonnelTempConsoleService.deleteRepresentative(id);
        log.info("[/console/cases_personnel_temp/delete_representative] {}", result);
        return result;
    }
    
    @RequestMapping("/get_list_4_representative")
    @ResponseBody
    public Map<String, Object> getList4Representative(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesPersonnelTempConsoleService.getList4Representative(casesId);
        log.info("[/console/cases_personnel_temp/get_list_4_representative] {}", result);
        return result;
    }

    public CasesPersonnelTempConsoleService getCasesPersonnelTempConsoleService() {
        return casesPersonnelTempConsoleService;
    }
    public void setCasesPersonnelTempConsoleService(
            CasesPersonnelTempConsoleService casesPersonnelTempConsoleService) {
        this.casesPersonnelTempConsoleService = casesPersonnelTempConsoleService;
    }
}
