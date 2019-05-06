package com.irsa.cases.console.action;

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
import com.irsa.cases.console.service.CasesRemarkConsoleService;

@Controller
@RequestMapping("/console/cases_remark")
public class CasesRemarkConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesRemarkConsoleAction.class);
    @Autowired
    CasesRemarkConsoleService casesRemarkConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(HttpServletRequest request, 
            @RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesRemarkConsoleService.getList(casesId, Utils.toString(request.getSession().getAttribute("account")));
        log.info("[/console/cases_remark/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, String id, @RequestParam(required=false, name="cases_id") String casesId, String remark) {
        Map<String, Object> result = casesRemarkConsoleService.save(id, casesId, Utils.toString(request.getSession().getAttribute("account")), remark);
        log.info("[/console/cases_remark/save] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String id) {
        Map<String, Object> result = casesRemarkConsoleService.delete(id);
        log.info("[/console/cases_remark/delete] {}", result);
        return result;
    }

    public CasesRemarkConsoleService getCasesRemarkConsoleService() {
        return casesRemarkConsoleService;
    }
    public void setCasesRemarkConsoleService(
            CasesRemarkConsoleService casesRemarkConsoleService) {
        this.casesRemarkConsoleService = casesRemarkConsoleService;
    }
}
