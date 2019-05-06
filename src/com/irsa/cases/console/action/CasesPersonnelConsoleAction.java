package com.irsa.cases.console.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.utils.Utils;
import com.irsa.cases.console.service.CasesPersonnelConsoleService;

@Controller
@RequestMapping("/console/cases_personnel")
public class CasesPersonnelConsoleAction {
    Logger log = LoggerFactory.getLogger(CasesPersonnelConsoleAction.class);
    @Autowired
    CasesPersonnelConsoleService casesPersonnelConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> apply(@RequestParam(required=false, name="cases_id") String id, String type) {
        Map<String, Object> result = casesPersonnelConsoleService.getList(id, type);
        log.info("[/console/cases_personnel/get_list] {}", result);
        return result;
    }

    @RequestMapping("/get_cases_apply")
    @ResponseBody
    public Map<String, Object> getCasesApply(@RequestParam(required=false, name="casesId") String casesId, String personnelType) {
        Map<String, Object> result = casesPersonnelConsoleService.getPersonnelInfoList(casesId, personnelType);
        log.info("[/console/cases_personnel/get_cases_apply] {}", result);
        return result;
    }
    
    @RequestMapping("/get_list_4_representative")
    @ResponseBody
    public Map<String, Object> getList4Representative(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesPersonnelConsoleService.getList4Representative(casesId);
        log.info("[/console/cases_personnel/get_list_4_representative] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list_4_client")
    @ResponseBody
    public Map<String, Object> getChooseList4Agent(@RequestParam(required=false, name="cases_id") String casesId, String type) {
        Map<String, Object> result = casesPersonnelConsoleService.getChooseList4Client(casesId, type);
        log.info("[/console/cases_personnel/get_choose_list_4_client] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list_4_apply_third_party")
    @ResponseBody
    public Map<String, Object> getChooseList4ApplyThirdParty(@RequestParam(required=false, name="cases_id") String casesId) {
        Map<String, Object> result = casesPersonnelConsoleService.getChooseList4ApplyThirdParty(casesId);
        log.info("[/console/cases_personnel/get_choose_list_4_apply_third_party] {}", result);
        return result;
    }
    
    @RequestMapping("/update13")
    @ResponseBody
    public Map<String, Object> update13(String id, String name, @RequestParam(required=false, name="legal_person") String legalPerson, 
            @RequestParam(required=false, name="id_type") String idType, 
            @RequestParam(required=false, name="id_no") String idNo, String gender, String birthday, String phone, String contact,
            @RequestParam(required=false, name="zip_code") String zipCode, 
            @RequestParam(required=false, name="province_id") String provinceId, 
            @RequestParam(required=false, name="city_id") String cityId, 
            @RequestParam(required=false, name="county_id") String countyId, String address,
            @RequestParam(required=false, name="unit_id_type") String unitIdType,
            @RequestParam(required=false, name="unit_id_no") String unitIdNo,
            @RequestParam(required=false, name="unit_contact") String unitContact, String domicile) {
        Map<String, Object> result = casesPersonnelConsoleService.update13(id, Utils.toString(name), Utils.toString(legalPerson), 
                idType, Utils.toString(idNo), gender, Utils.toString(birthday), 
                Utils.toString(phone), Utils.toString(contact), Utils.toString(zipCode), provinceId, cityId, countyId, Utils.toString(address),
                unitIdType, Utils.toString(unitIdNo), Utils.toString(unitContact), Utils.toString(domicile));
        log.info("[/console/cases_personnel/update13] {}", result);
        return result;
    }
    
    @RequestMapping("/update2")
    @ResponseBody
    public Map<String, Object> update2(String id, String type, String name,  String address, @RequestParam(required=false, name="legal_person") String legalPerson) { 
        Map<String, Object> result = casesPersonnelConsoleService.update2(id, type, Utils.toString(name), Utils.toString(address), Utils.toString(legalPerson));
        log.info("[/console/cases_personnel/update2] {}", result);
        return result;
    }
    
    @RequestMapping("/update6")
    @ResponseBody
    public Map<String, Object> update6(String id, String name, String unit_name,
            @RequestParam(required=false, name="id_type") String idType, 
            @RequestParam(required=false, name="id_no") String idNo, String gender, String birthday, String phone, String contact,
            @RequestParam(required=false, name="zip_code") String zipCode, 
            @RequestParam(required=false, name="province_id") String provinceId, 
            @RequestParam(required=false, name="city_id") String cityId, 
            @RequestParam(required=false, name="county_id") String countyId, String address) {
        Map<String, Object> result = casesPersonnelConsoleService.update6(id, Utils.toString(name), Utils.toString(unit_name), idType, Utils.toString(idNo),
                gender, Utils.toString(birthday), Utils.toString(phone), Utils.toString(contact), Utils.toString(zipCode), provinceId, cityId, countyId, Utils.toString(address));
        log.info("[/console/cases_personnel/update6] {}", result);
        return result;
    }

    public CasesPersonnelConsoleService getCasesPersonnelConsoleService() {
        return casesPersonnelConsoleService;
    }
    public void setCasesPersonnelConsoleService(
            CasesPersonnelConsoleService casesPersonnelConsoleService) {
        this.casesPersonnelConsoleService = casesPersonnelConsoleService;
    }
}
