package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.HolidayDateConsoleService;

@Controller
@RequestMapping("/console/holiday_date")
public class HolidayDateConsoleAction {
    Logger log = LoggerFactory.getLogger(HolidayDateConsoleAction.class);
    @Autowired
    HolidayDateConsoleService holidayDateConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="holiday_id") String holidayId) {
        Map<String, Object> result = holidayDateConsoleService.getList(holidayId);
        log.info("[/console/holiday_date/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, @RequestParam(required=false, name="holiday_id") String holidayId, String type, String date) {
        Map<String, Object> result = holidayDateConsoleService.save(id, holidayId, type, date); 
        log.info("[/console/holiday_date/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = holidayDateConsoleService.delete(ids);
        log.info("[/console/holiday_date/delete] {}", result);
        return result;
    }

    public HolidayDateConsoleService getHolidayDateConsoleService() {
        return holidayDateConsoleService;
    }
    public void setHolidayDateConsoleService(
            HolidayDateConsoleService holidayDateConsoleService) {
        this.holidayDateConsoleService = holidayDateConsoleService;
    }
}
