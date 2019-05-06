package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.RegulationsConsoleService;

@Controller
@RequestMapping("/console/regulations")
public class RegulationsConsoleAction {
    Logger log = LoggerFactory.getLogger(RegulationsConsoleAction.class);
    @Autowired
    RegulationsConsoleService regulationsConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(String page, @RequestParam(required=false, name="search_type") String typeId, 
            @RequestParam(required=false, name="search_title") String title, 
            @RequestParam(required=false, name="search_department") String department, 
            @RequestParam(required=false, name="search_publish_number") String publishNumber, 
            @RequestParam(required=false, name="search_publish_time") String publishTime, 
            @RequestParam(required=false, name="search_execute_time") String executeTime,
            @RequestParam(required=false, name="search_key") String key) {
        Map<String, Object> result = regulationsConsoleService.getList(page, typeId, title, department, publishNumber, publishTime, executeTime, key);
        log.info("[/console/regulations/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, @RequestParam(required=false, name="type_id") String typeId, 
            String department, @RequestParam(required=false, name="publish_number") String publishNumber, 
            @RequestParam(required=false, name="publish_time") String publishTime, 
            @RequestParam(required=false, name="execute_time") String executeTime, String title, String content) {
        Map<String, Object> result = regulationsConsoleService.save(id, typeId, department, publishNumber, publishTime, executeTime, title, content); 
        log.info("[/console/regulations/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = regulationsConsoleService.delete(ids);
        log.info("[/console/regulations/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getObject(String id) {
        Map<String, Object> result = regulationsConsoleService.getMap(id);
        log.info("[/console/regulations/get_object] {}", result);
        return result;
    }

    public RegulationsConsoleService getRegulationsConsoleService() {
        return regulationsConsoleService;
    }
    public void setRegulationsConsoleService(
            RegulationsConsoleService regulationsConsoleService) {
        this.regulationsConsoleService = regulationsConsoleService;
    }
}
