package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.DefendantTypeConsoleService;

@Controller
@RequestMapping("/console/defendant_type")
public class DefendantTypeConsoleAction {
    Logger log = LoggerFactory.getLogger(DefendantTypeConsoleAction.class);
    @Autowired
    DefendantTypeConsoleService defendantTypeConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = defendantTypeConsoleService.getList();
        log.info("[/console/defendant_type/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String title) {
        Map<String, Object> result = defendantTypeConsoleService.save(id, title); 
        log.info("[/console/defendant_type/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = defendantTypeConsoleService.delete(ids);
        log.info("[/console/defendant_type/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = defendantTypeConsoleService.sort(ids);
        log.info("[/console/defendant_type/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = defendantTypeConsoleService.getChooseList();
        log.info("[/console/defendant_type/get_choose_list] {}", result);
        return result;
    }

    public DefendantTypeConsoleService getDefendantTypeConsoleService() {
        return defendantTypeConsoleService;
    }
    public void setDefendantTypeConsoleService(
            DefendantTypeConsoleService defendantTypeConsoleService) {
        this.defendantTypeConsoleService = defendantTypeConsoleService;
    }
}
