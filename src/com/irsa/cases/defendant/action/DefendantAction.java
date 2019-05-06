package com.irsa.cases.defendant.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irsa.cases.console.service.CasesConsoleService;
import com.irsa.cases.console.service.CasesFileConsoleService;
import com.irsa.cases.console.service.CasesPersonnelConsoleService;
import com.irsa.cases.console.service.CasesStepConsoleService;
import com.irsa.cases.console.service.TempFileConsoleService;
import com.irsa.cases.defendant.service.DefendantService;
import com.common.utils.Utils;

@Controller
@RequestMapping("/bei")
public class DefendantAction {
    Logger log = LoggerFactory.getLogger(DefendantAction.class);
    @Autowired
    CasesConsoleService casesConsoleService;
    @Autowired
    CasesFileConsoleService casesFileConsoleService;
    @Autowired
    CasesPersonnelConsoleService casesPersonnelConsoleService;
    @Autowired
    CasesStepConsoleService casesStepConsoleService;
    @Autowired
    DefendantService defendantService;
    @Autowired
    TempFileConsoleService tempFileConsoleService;
    
    @RequestMapping("/register")
    @ResponseBody
    public Map<String, Object> register(HttpServletRequest request, 
            String id, String name, String address, 
            @RequestParam(required=false, name="legal_person") String legalPerson,
            String account, String pwd,
            @RequestParam(required=false, name="pwd_confirm") String pwdConfirm) {
        Map<String, Object> result = defendantService.register(id, name, address, legalPerson, account, pwd, pwdConfirm);
        log.info("[/defendant/register] {}", result);
        return result;
    }
    
    @RequestMapping("/logon")
    @ResponseBody
    public Map<String, Object> logon(HttpServletRequest request, 
            @RequestParam(required=false, name="user_name") String name, 
            @RequestParam(required=false, name="user_pwd") String pwd) {
        Map<String, Object> result = defendantService.login(request, name, pwd);
        log.info("[/defendant/logon] {}", result);
        return result;
    }
    
    @RequestMapping("/get_object")
    @ResponseBody
    public Map<String, Object> getAccount(HttpServletRequest request) {
        Map<String, Object> result = defendantService.getMap(request);
        log.info("[/defendant/get_object] {}", result);
        return result;
    }
    
    @RequestMapping("/cases/get_list")
    @ResponseBody
    public Map<String, Object> getCasesList(HttpServletRequest request) {
        Map<String, Object> result = defendantService.getCasesList(request);
        log.info("[/defendant/cases/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/cases/get_object")
    @ResponseBody
    public Map<String, Object> getMap(String id) {
        Map<String, Object> result = casesConsoleService.getMap(id, null);
        log.info("[/defendant/cases/get_object] {}", result);
        return result;
    }
    
    @RequestMapping("/cases/reply")
    @ResponseBody
    public Map<String, Object> reply(@RequestParam(required=false, name="book_id") String bookId,
            @RequestParam(required=false, name="file_id") String fileId,
            @RequestParam(required=false, name="cases_id") String casesId, String reply, String remark) {
        Map<String, Object> result = defendantService.reply(bookId, fileId, casesId, reply, remark);
        log.info("[/defendant/cases/reply] {}", result);
        return result;
    }
    
    @RequestMapping("/cases_personnel/get_list")
    @ResponseBody
    public Map<String, Object> apply(@RequestParam(required=false, name="cases_id") String id, String type) {
        Map<String, Object> result = casesPersonnelConsoleService.getList(id, type);
        log.info("[/defendant/cases_personnel/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/cases_file/files")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="cases_id") String id, String mode) {
        Map<String, Object> result = casesFileConsoleService.getList(id, mode);
        log.info("[/defendant/cases_file/files] {}", result);
        return result;
    }
    
    @RequestMapping("/cases_file/get_object")
    @ResponseBody
    public Map<String, Object> getFile(String id) {
        Map<String, Object> result = casesFileConsoleService.getMap(id);
        log.info("[/defendant/cases_file/get_object] {}", result);
        return result;
    }
    
    @RequestMapping("/cases_step/get_list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesStepConsoleService.getList(casesId);
        log.info("[/defendant/cases_step/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/get_act_id")
    @ResponseBody
    public Map<String, Object> getActId() {
        Map<String, Object> result = Utils.getSuccessMap(Utils.getId());
        log.info("[/defendant/get_act_id] {}", result);
    	return result;
    }
    
    @RequestMapping("/temp/img")
    @ResponseBody
    public Map<String, Object> img(HttpServletRequest request) {
        Map<String, Object> result = tempFileConsoleService.img(request);
        log.info("[/defendant/temp/img] {}", result);
        return result;
    }
    
    @RequestMapping("/temp/imgs")
    @ResponseBody
    public Map<String, Object> file(HttpServletRequest request) {
        Map<String, Object> result = tempFileConsoleService.imgs(request);
        log.info("[/defendant/temp/imgs] {}", result);
        return result;
    }
    
    @RequestMapping("/temp/delete")
    @ResponseBody
    public Map<String, Object> delete(String id) {
        Map<String, Object> result = tempFileConsoleService.delete(id);
        log.info("[/defendant/temp/delete] {}", result);
        return result;
    }

    
    public CasesConsoleService getCasesConsoleService() {
        return casesConsoleService;
    }
    public void setCasesConsoleService(CasesConsoleService casesConsoleService) {
        this.casesConsoleService = casesConsoleService;
    }
    public CasesFileConsoleService getCasesFileConsoleService() {
        return casesFileConsoleService;
    }
    public void setCasesFileConsoleService(
            CasesFileConsoleService casesFileConsoleService) {
        this.casesFileConsoleService = casesFileConsoleService;
    }
    public CasesPersonnelConsoleService getCasesPersonnelConsoleService() {
        return casesPersonnelConsoleService;
    }
    public void setCasesPersonnelConsoleService(
            CasesPersonnelConsoleService casesPersonnelConsoleService) {
        this.casesPersonnelConsoleService = casesPersonnelConsoleService;
    }
    public CasesStepConsoleService getCasesStepConsoleService() {
        return casesStepConsoleService;
    }
    public void setCasesStepConsoleService(
            CasesStepConsoleService casesStepConsoleService) {
        this.casesStepConsoleService = casesStepConsoleService;
    }
    public DefendantService getDefendantService() {
        return defendantService;
    }
    public void setDefendantService(DefendantService defendantService) {
        this.defendantService = defendantService;
    }
    public TempFileConsoleService getTempFileConsoleService() {
        return tempFileConsoleService;
    }
    public void setTempFileConsoleService(
            TempFileConsoleService tempFileConsoleService) {
        this.tempFileConsoleService = tempFileConsoleService;
    }
}
