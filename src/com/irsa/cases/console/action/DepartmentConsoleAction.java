package com.irsa.cases.console.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.utils.Utils;
import com.irsa.cases.console.service.DepartmentConsoleService;

@Controller
@RequestMapping("/console/department")
public class DepartmentConsoleAction {
    Logger log = LoggerFactory.getLogger(DepartmentConsoleAction.class);
    @Autowired
    DepartmentConsoleService departmentConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(HttpServletRequest request) {
        Map<String, Object> result = departmentConsoleService.getList(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/department/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String title) {
        Map<String, Object> result = departmentConsoleService.save(id, title); 
        log.info("[/console/department/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = departmentConsoleService.delete(ids);
        log.info("[/console/department/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = departmentConsoleService.sort(ids);
        log.info("[/console/department/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(HttpServletRequest request) {
        Map<String, Object> result = departmentConsoleService.getChooseList(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/department/get_choose_list] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getMap(String id) {
        Map<String, Object> result = departmentConsoleService.getMap(id);
        log.info("[/console/department/get_object] {}", result);
        return result;
    }

    public DepartmentConsoleService getDepartmentConsoleService() {
        return departmentConsoleService;
    }
    public void setDepartmentConsoleService(
            DepartmentConsoleService departmentConsoleService) {
        this.departmentConsoleService = departmentConsoleService;
    }
}
