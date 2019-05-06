package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.AdministrativeCategoryConsoleService;

@Controller
@RequestMapping("/console/administrative_category")
public class AdministrativeCategoryConsoleAction {
    Logger log = LoggerFactory.getLogger(AdministrativeCategoryConsoleAction.class);
    @Autowired
    AdministrativeCategoryConsoleService administrativeCategoryConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = administrativeCategoryConsoleService.getList();
        log.info("[/console/administrative_category/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="category_parent_id") String parentId, 
            @RequestParam(required=false, name="category_id") String id, 
            @RequestParam(required=false, name="category_title") String title) {
        Map<String, Object> result = administrativeCategoryConsoleService.save(parentId, id, title); 
        log.info("[/console/administrative_category/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = administrativeCategoryConsoleService.delete(ids);
        log.info("[/console/administrative_category/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = administrativeCategoryConsoleService.sort(ids);
        log.info("[/console/administrative_category/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list_4_parent")
    @ResponseBody
    public Map<String, Object> getChooseList4Parent() {
        Map<String, Object> result = administrativeCategoryConsoleService.getChooseList4Parent();
        log.info("[/console/administrative_category/get_choose_list_4_parent] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = administrativeCategoryConsoleService.getChooseList();
        log.info("[/console/administrative_category/get_choose_list] {}", result);
        return result;
    }

    public AdministrativeCategoryConsoleService getAdministrativeCategoryConsoleService() {
        return administrativeCategoryConsoleService;
    }
    public void setAdministrativeCategoryConsoleService(
            AdministrativeCategoryConsoleService administrativeCategoryConsoleService) {
        this.administrativeCategoryConsoleService = administrativeCategoryConsoleService;
    }
}
