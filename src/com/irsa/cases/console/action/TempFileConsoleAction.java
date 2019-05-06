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

import com.irsa.cases.console.service.TempFileConsoleService;

@Controller
@RequestMapping("/console/temp")
public class TempFileConsoleAction {
    Logger log = LoggerFactory.getLogger(TempFileConsoleAction.class);
    @Autowired
    TempFileConsoleService tempFileConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="res") String resId, String mode) {
        Map<String, Object> result = tempFileConsoleService.getList(resId, mode);
        log.info("[/console/temp/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/img")
    @ResponseBody
    public Map<String, Object> img(HttpServletRequest request) {
        Map<String, Object> result = tempFileConsoleService.img(request);
        log.info("[/console/temp/img] {}", result);
        return result;
    }
    
    @RequestMapping("/imgs")
    @ResponseBody
    public Map<String, Object> imgs(HttpServletRequest request) {
        Map<String, Object> result = tempFileConsoleService.imgs(request);
        log.info("[/console/temp/imgs] {}", result);
        return result;
    }
    
    @RequestMapping("/file")
    @ResponseBody
    public Map<String, Object> file(HttpServletRequest request) {
        Map<String, Object> result = tempFileConsoleService.file(request);
        log.info("[/console/temp/file] {}", result);
        return result;
    }
    
    @RequestMapping("/files")
    @ResponseBody
    public Map<String, Object> files(HttpServletRequest request) {
        Map<String, Object> result = tempFileConsoleService.files(request);
        log.info("[/console/temp/files] {}", result);
        return result;
    }
    
    @RequestMapping("/rename")
    @ResponseBody
    public Map<String, Object> rename(@RequestParam(required=false, name="rename_id") String id, 
            @RequestParam(required=false, name="rename_name") String name) {
        Map<String, Object> result = tempFileConsoleService.rename(id, name);
        log.info("[/console/temp/rename] {}", result);
        return result;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(String id) {
        Map<String, Object> result = tempFileConsoleService.delete(id);
        log.info("[/console/temp/delete] {}", result);
        return result;
    }

    public TempFileConsoleService getTempFileConsoleService() {
        return tempFileConsoleService;
    }
    public void setTempFileConsoleService(
            TempFileConsoleService tempFileConsoleService) {
        this.tempFileConsoleService = tempFileConsoleService;
    }
}
