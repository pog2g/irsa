package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.IdTypeConsoleService;

@Controller
@RequestMapping("/console/id_type")
public class IdTypeConsoleAction {
    Logger log = LoggerFactory.getLogger(IdTypeConsoleAction.class);
    @Autowired
    IdTypeConsoleService idTypeConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = idTypeConsoleService.getList();
        log.info("[/console/id_type/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String title) {
        Map<String, Object> result = idTypeConsoleService.save(id, title); 
        log.info("[/console/id_type/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = idTypeConsoleService.delete(ids);
        log.info("[/console/id_type/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = idTypeConsoleService.sort(ids);
        log.info("[/console/id_type/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = idTypeConsoleService.getChooseList();
        log.info("[/console/id_type/get_choose_list] {}", result);
        return result;
    }

    public IdTypeConsoleService getIdTypeConsoleService() {
        return idTypeConsoleService;
    }
    public void setIdTypeConsoleService(IdTypeConsoleService idTypeConsoleService) {
        this.idTypeConsoleService = idTypeConsoleService;
    }
}
