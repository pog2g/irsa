package com.irsa.console.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.utils.Utils;
import com.irsa.console.service.RoleService;

@Controller
@RequestMapping("/console/role")
public class RoleAction {
    Logger log = LoggerFactory.getLogger(RoleAction.class);
    @Autowired
    RoleService roleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(HttpServletRequest request) {
        Map<String, Object> result = roleService.getList(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/role/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, @RequestParam(required=false, name="department_id") String departmentId, 
            String title, String remark, @RequestParam(required=false, name="competence_ids") List<String> competenceIds) {
        Map<String, Object> result = roleService.save(id, departmentId, title, remark, competenceIds);
        log.info("[/console/role/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = roleService.delete(ids);
        log.info("[/console/role/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(String parent) {
        Map<String, Object> result = roleService.getChooseList(parent);
        log.info("[/console/role/get_choose_list] {}", result);
        return result;
    }

    public RoleService getRoleService() {
        return roleService;
    }
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
}
