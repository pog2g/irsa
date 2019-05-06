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
import com.irsa.cases.console.service.CasesFileConsoleService;

@Controller
@RequestMapping("/console/cases_file")
public class CasesFileConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesFileConsoleAction.class);
    @Autowired
    CasesFileConsoleService casesFileConsoleService;
    
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest request) {
        Map<String, Object> result = casesFileConsoleService.upload(request);
        log.info("[/console/cases_file/upload] {}", result);
        return result;
    }
    
    //获取文件列表
    @RequestMapping("/files")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="cases_id") String id, String mode) {
        Map<String, Object> result = casesFileConsoleService.getList(id, mode);
        log.info("[/console/cases_file/files] {}", result);
        return result;
    }
     
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> apply(String id) {
        Map<String, Object> result = casesFileConsoleService.getMap(id);
        log.info("[/console/cases_file/get_object] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, String html) {
        Map<String, Object> result = casesFileConsoleService.save(id, Utils.toString(html));
        log.info("[/console/cases_file/save] {}", result);
        return result;
    }

    public CasesFileConsoleService getCasesFileConsoleService() {
        return casesFileConsoleService;
    }
    public void setCasesFileConsoleService(
            CasesFileConsoleService casesFileConsoleService) {
        this.casesFileConsoleService = casesFileConsoleService;
    }
}
