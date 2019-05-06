package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.RegulationsTypeConsoleService;

@Controller
@RequestMapping("/console/regulations_type")
public class RegulationsTypeConsoleAction {
    Logger log = LoggerFactory.getLogger(RegulationsTypeConsoleAction.class);
    @Autowired
    RegulationsTypeConsoleService regulationsTypeConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = regulationsTypeConsoleService.getList();
        log.info("[/console/regulations_type/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String title) {
        Map<String, Object> result = regulationsTypeConsoleService.save(id, title); 
        log.info("[/console/regulations_type/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = regulationsTypeConsoleService.delete(ids);
        log.info("[/console/regulations_type/delete] {}", result);
        return result;
    }

    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = regulationsTypeConsoleService.sort(ids);
        log.info("[/console/regulations_type/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = regulationsTypeConsoleService.getChooseList();
        log.info("[/console/regulations_type/get_choose_list] {}", result);
        return result;
    }
    
    public RegulationsTypeConsoleService getRegulationsTypeConsoleService() {
        return regulationsTypeConsoleService;
    }
    public void setRegulationsTypeConsoleService(
            RegulationsTypeConsoleService regulationsTypeConsoleService) {
        this.regulationsTypeConsoleService = regulationsTypeConsoleService;
    }
}
