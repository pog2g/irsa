package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.LitigationConsoleService;

@Controller
@RequestMapping("/console/litigation")
public class LitigationConsoleAction {
    Logger log = LoggerFactory.getLogger(LitigationConsoleAction.class);
    @Autowired
    LitigationConsoleService litigationConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(String page, @RequestParam(required=false, name="search_year") String year, 
            @RequestParam(required=false, name="search_reason") String reasonId, 
            @RequestParam(required=false, name="search_mode") String mode, 
            @RequestParam(required=false, name="search_type") String type, 
            @RequestParam(required=false, name="search_court") String court, 
            @RequestParam(required=false, name="search_title") String title,
            @RequestParam(required=false, name="search_key") String keys) {
        Map<String, Object> result = litigationConsoleService.getList(page, year, reasonId, mode, type, court, title, keys);
        log.info("[/console/litigation/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String year, @RequestParam(required=false, name="reason_id") String reasonId, 
            String court, String mode, String type, String title, String content) {
        Map<String, Object> result = litigationConsoleService.save(id, year, reasonId, court, mode, type, title, content); 
        log.info("[/console/litigation/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = litigationConsoleService.delete(ids);
        log.info("[/console/litigation/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getObject(String id) {
        Map<String, Object> result = litigationConsoleService.getMap(id);
        log.info("[/console/litigation/get_object] {}", result);
        return result;
    }

    public LitigationConsoleService getLitigationConsoleService() {
        return litigationConsoleService;
    }
    public void setLitigationConsoleService(
            LitigationConsoleService litigationConsoleService) {
        this.litigationConsoleService = litigationConsoleService;
    }
}
