package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.DefendantConsoleService;

@Controller
@RequestMapping("/console/defendant")
public class DefendantConsoleAction {
    Logger log = LoggerFactory.getLogger(DefendantConsoleAction.class);
    @Autowired
    DefendantConsoleService defendantConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(String page, @RequestParam(required=false, name="search_key") String key) {
        Map<String, Object> result = defendantConsoleService.getList(page, key);
        log.info("[/console/defendant/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, 
            @RequestParam(required=false, name="cases_id") String casesId,
            @RequestParam(required=false, name="personnel_id") String personnelId,
            String type, String unit_name, String unit_abode, String unit_contact,
            @RequestParam(required=false, name="name") String name,
            @RequestParam(required=false, name="legal_person_type") String leal_peron_type, String duty) {
        Map<String, Object> result = defendantConsoleService.save4Personnel(id, casesId, personnelId, type, unit_name, unit_abode, unit_contact, name, leal_peron_type, duty);
        log.info("[/console/defendant/save] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(String page, String key) {
        Map<String, Object> result = defendantConsoleService.getChooseList(page, key);
        log.info("[/console/defendant/get_choose_list] {}", result);
        return result;
    }
    
    @RequestMapping("/update")
    @ResponseBody
    public Map<String, Object> update(
            @RequestParam(required=false, name="res_id") String resId, String id,
            @RequestParam(required=false, name="type_id") String typeId,
            String unit_name, String unit_abode, String unit_contact,
            @RequestParam(required=false, name="name") String name, String legal_person_type, String duty,
            String account, String pwd,
            @RequestParam(required=false, name="pwd_confirm") String pwdConfirm) {
        Map<String, Object> result = defendantConsoleService.save(resId, id, typeId, unit_name, unit_abode, unit_contact, name, legal_person_type, duty, account, pwd, pwdConfirm);
        log.info("[/console/defendant/update] {}", result);
        return result;
    }

    public DefendantConsoleService getDefendantConsoleService() {
        return defendantConsoleService;
    }
    public void setDefendantConsoleService(
            DefendantConsoleService defendantConsoleService) {
        this.defendantConsoleService = defendantConsoleService;
    }
}
