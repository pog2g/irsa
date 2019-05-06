package com.irsa.cases.console.action;

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
import com.irsa.cases.console.service.CasesStepConsoleService;


@Controller
@RequestMapping("/console/cases_step")
public class CasesStepConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesStepConsoleAction.class);
    @Autowired
    CasesStepConsoleService casesStepConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesStepConsoleService.getList(casesId);
        log.info("[/console/cases_step/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, String id, String mode,
            @RequestParam(required=false, name="cases_id") String casesId, String state, String refine,
            @RequestParam(required=false, name="limit_day") String limitDay, 
            @RequestParam(required=false, name="text_1") String text1, 
            @RequestParam(required=false, name="text_2") String text2, 
            @RequestParam(required=false, name="text_3") String text3, 
            @RequestParam(required=false, name="text_4") String text4,
            @RequestParam(required=false, name="text_5") String text5,
            @RequestParam(required=false, name="text_6") String text6,
            @RequestParam(required=false, name="text_7") String text7,
            @RequestParam(required=false, name="radio_1") String radio1,
            @RequestParam(required=false, name="regulations_list") List<String> regulationsList,
            @RequestParam(required=false, name="first_personnel") String firstPersonnel,
            @RequestParam(required=false, name="second_personnel") String secondPersonnel,
            @RequestParam(required=false, name="html_1") String html1,
            @RequestParam(required=false, name="html_2") String html2,
            @RequestParam(required=false, name="html_3") String html3) {
        Map<String, Object> result = casesStepConsoleService.save(Utils.toString(request.getSession().getAttribute("account")), id, 
                casesId, mode, state, Utils.toString(limitDay), Utils.toString(text1), Utils.toString(text2), Utils.toString(text3), 
                Utils.toString(text4), Utils.toString(text5), Utils.toString(text6), Utils.toString(text7),
                radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
        log.info("[/console/cases_step/save] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getObject(HttpServletRequest request, 
            @RequestParam(required=false, name="cases_id") String casesId, String state) {
        Map<String, Object> result = casesStepConsoleService.getMap(Utils.toString(request.getSession().getAttribute("account")), casesId, state);
        log.info("[/console/cases_step/get_object] {}", result);
        return result;
    }

    public CasesStepConsoleService getCasesStepConsoleService() {
        return casesStepConsoleService;
    }
    public void setCasesStepConsoleService(
            CasesStepConsoleService casesStepConsoleService) {
        this.casesStepConsoleService = casesStepConsoleService;
    }
}
