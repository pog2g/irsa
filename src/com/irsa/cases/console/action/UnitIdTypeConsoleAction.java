package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.UnitIdTypeConsoleService;

@Controller
@RequestMapping("/console/unit_id_type")
public class UnitIdTypeConsoleAction {
    Logger log = LoggerFactory.getLogger(UnitIdTypeConsoleAction.class);
    @Autowired
    UnitIdTypeConsoleService unitIdTypeConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = unitIdTypeConsoleService.getList();
        log.info("[/console/id_type/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String title) {
        Map<String, Object> result = unitIdTypeConsoleService.save(id, title); 
        log.info("[/console/id_type/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = unitIdTypeConsoleService.delete(ids);
        log.info("[/console/id_type/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = unitIdTypeConsoleService.sort(ids);
        log.info("[/console/id_type/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = unitIdTypeConsoleService.getChooseList();
        log.info("[/console/id_type/get_choose_list] {}", result);
        return result;
    }

    public UnitIdTypeConsoleService getUnitIdTypeConsoleService() {
        return unitIdTypeConsoleService;
    }
    public void setUnitIdTypeConsoleService(
            UnitIdTypeConsoleService unitIdTypeConsoleService) {
        this.unitIdTypeConsoleService = unitIdTypeConsoleService;
    }
}
