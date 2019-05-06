package com.irsa.console.action;

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
import com.irsa.console.service.CompetenceService;

@Controller
@RequestMapping("/console/competence")
public class CompetenceAction {
    Logger log = LoggerFactory.getLogger(CompetenceAction.class);
    @Autowired
    CompetenceService competenceService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = competenceService.getList();
        log.info("[/console/competence/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=false, name="parent_id") String parentId, 
            String id, String icon, String title, String url, String state, String remark) {
        Map<String, Object> result = competenceService.save(parentId, id, icon, title, url, state, remark); 
        log.info("[/console/competence/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = competenceService.delete(ids);
        log.info("[/console/competence/delete] {}", result);
        return result;
    }
    
    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = competenceService.sort(ids);
        log.info("[/console/competence/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list_4_parent")
    @ResponseBody
    public Map<String, Object> getChooseList4Parent() {
        Map<String, Object> result = competenceService.getChooseList4Parent();
        log.info("[/console/competence/get_choose_list_4_parent] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(HttpServletRequest request) {
        Map<String, Object> result = competenceService.getChooseList(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/competence/get_choose_list] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getMap(String id) {
        Map<String, Object> result = competenceService.getMap(id);
        log.info("[/console/competence/get_object] {}", result);
        return result;
    }
    
    @RequestMapping("/get_list_4_menu")
    @ResponseBody
    public Map<String, Object> getList4Menu(HttpServletRequest request) {
        Map<String, Object> result = competenceService.getList4Menu(Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/competence/get_list_4_menu] {}", result);
        return result;
    }
    
    @RequestMapping("/save_menu")
    @ResponseBody
    public Map<String, Object> save(String id, String title, String state, String remark) {
        Map<String, Object> result = competenceService.saveMenu(id, title, state, remark); 
        log.info("[/console/competence/save_menu] {}", result);
        return result;
    }

    public CompetenceService getCompetenceService() {
        return competenceService;
    }
    public void setCompetenceService(CompetenceService competenceService) {
        this.competenceService = competenceService;
    }
}
