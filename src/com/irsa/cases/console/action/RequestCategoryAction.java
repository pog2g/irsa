package com.irsa.cases.console.action;

import com.irsa.cases.console.service.RequestCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/console/request_category")
public class RequestCategoryAction {
    Logger log = LoggerFactory.getLogger(RequestCategoryAction.class);
    @Autowired
    RequestCategoryService requestCategoryService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList() {
        Map<String, Object> result = requestCategoryService.getList();
        log.info("[/console/request_category/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String title) {
        Map<String, Object> result = requestCategoryService.save(id, title);
        log.info("[/console/request_category/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String ids) {
        Map<String, Object> result = requestCategoryService.delete(ids);
        log.info("[/console/request_category/delete] {}", result);
        return result;
    }

    @RequestMapping("/sort")
    @ResponseBody
    public Map<String, Object> sort(String ids) {
        Map<String, Object> result = requestCategoryService.sort(ids);
        log.info("[/console/request_category/sort] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList() {
        Map<String, Object> result = requestCategoryService.getChooseList();
        log.info("[/console/request_category/get_choose_list] {}", result);
        return result;
    }
    
    public RequestCategoryService getRequestCategoryService() {
        return requestCategoryService;
    }
    public void setRequestCategoryService(
            RequestCategoryService requestCategoryService) {
        this.requestCategoryService = requestCategoryService;
    }
}
