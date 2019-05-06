package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.HolidayConsoleService;

@Controller
@RequestMapping("/console/holiday")
public class HolidayConsoleAction {
    Logger log = LoggerFactory.getLogger(HolidayConsoleAction.class);
    @Autowired
    HolidayConsoleService holidayConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = holidayConsoleService.getList();
        log.info("[/console/holiday/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String year, String title) {
        Map<String, Object> result = holidayConsoleService.save(id, year, title); 
        log.info("[/console/holiday/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = holidayConsoleService.delete(ids);
        log.info("[/console/holiday/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getMap(String id) {
        Map<String, Object> result = holidayConsoleService.getMap(id);
        log.info("[/console/holiday/get_object] {}", result);
        return result;
    }

    public HolidayConsoleService getHolidayConsoleService() {
        return holidayConsoleService;
    }
    public void setHolidayConsoleService(HolidayConsoleService holidayConsoleService) {
        this.holidayConsoleService = holidayConsoleService;
    }
}
