package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.RegionConsoleService;

@Controller
@RequestMapping("/console/region")
public class RegionConsoleAction {
    Logger log = LoggerFactory.getLogger(RegionConsoleAction.class);
    @Autowired
    RegionConsoleService regionConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(String page,
            @RequestParam(required=false, name="search_grade") String grade, 
            @RequestParam(required=false, name="search_province") String provinceId, 
            @RequestParam(required=false, name="search_city") String cityId) {
        Map<String, Object> result = regionConsoleService.getList(page, grade, provinceId, cityId);
        log.info("[/console/region/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save1")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="province_id") String id, 
            @RequestParam(required=false, name="province_title") String title) {
        Map<String, Object> result = regionConsoleService.save1(id, title);
        log.info("[/console/region/save1] {}", result);
        return result;
    }
    
    @RequestMapping("/save2")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="city_id") String id, 
            @RequestParam(required=false, name="city_title")  String title, 
            @RequestParam(required=false, name="city_province")  String provinceId) {
        Map<String, Object> result = regionConsoleService.save2(id, title, provinceId);
        log.info("[/console/region/save2] {}", result);
        return result;
    }
    
    @RequestMapping("/save3")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="county_id") String id, 
            @RequestParam(required=false, name="county_title") String title, 
            @RequestParam(required=false, name="county_province") String provinceId, 
            @RequestParam(required=false, name="county_city") String cityId) {
        Map<String, Object> result = regionConsoleService.save3(id, title, provinceId, cityId);
        log.info("[/console/region/save3] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = regionConsoleService.delete(ids);
        log.info("[/console/region/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = regionConsoleService.sort(ids);
        log.info("[/console/competence/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list_4_parent")
    @ResponseBody
    public Map<String, Object> getChooseList4Parent() {
        Map<String, Object> result = regionConsoleService.getChooseList4Parent();
        log.info("[/console/region/get_choose_list_4_parent] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list_by_parent")
    @ResponseBody
    public Map<String, Object> getChooseListByParent(String parent) {
        Map<String, Object> result = regionConsoleService.getChooseListByParent(parent);
        log.info("[/console/region/get_choose_list_by_parent] {}", result);
        return result;
    }

    public RegionConsoleService getRegionConsoleService() {
        return regionConsoleService;
    }
    public void setRegionConsoleService(RegionConsoleService regionConsoleService) {
        this.regionConsoleService = regionConsoleService;
    }
}
