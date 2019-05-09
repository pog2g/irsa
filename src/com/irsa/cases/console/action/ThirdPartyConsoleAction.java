package com.irsa.cases.console.action;

import com.irsa.cases.console.service.PartyConsoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/console/third_party")
public class ThirdPartyConsoleAction {
    Logger log = LoggerFactory.getLogger(ThirdPartyConsoleAction.class);
    @Autowired
    PartyConsoleService partyConsoleService;
    
 /*   @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(@RequestParam(required=true, name="cases_id") String casesId,
            @RequestParam(required=true, name="cases_personnel_type") String casesPersonnelType,
            @RequestParam(required=true, name="personnel_id") String personnelId,
            String id, String type, String name, String nature,
            @RequestParam(required=false, name="legal_person") String legalPerson, 
            String gender, String birthday, String phone, String contact,
            @RequestParam(required=false, name="zip_code") String zipCode,
            @RequestParam(required=false, name="province_id") String provinceId,
            @RequestParam(required=false, name="city_id") String cityId,
            @RequestParam(required=false, name="county_id") String countyId, String address, 
            @RequestParam(required=false, name="id_type_id") String idTypeId, 
            @RequestParam(required=false, name="id_no") String idNo,
            @RequestParam(required=false, name="unit_id_type_id") String unitIdTypeId,
            @RequestParam(required=false, name="unit_id_no") String unitIdNo,
            @RequestParam(required=false, name="unit_contact") String unitContact, String domicile) {
        Map<String, Object> result = partyConsoleService.save(id, casesPersonnelType, casesId, personnelId, type, Utils.toString(name),nature,"","", Utils.toString(legalPerson), gender, Utils.toString(birthday), Utils.toString(phone), Utils.toString(contact), Utils.toString(zipCode), 
                provinceId, cityId, countyId, Utils.toString(address), idTypeId, Utils.toString(idNo), unitIdTypeId, Utils.toString(unitIdNo), Utils.toString(unitContact), Utils.toString(domicile));
        log.info("[/console/third_party/save] {}", result);
        return result;
    }*/

    /**
     * 建议追加第三人
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id,
                                    @RequestParam(required = true, name = "cases_id") String casesId,
                                    @RequestParam(required = true, name = "personnel_id") String personnelId,
                                    @RequestParam(required = true, name = "cases_personnel_type") String cases_personnel_type,
                                    String type, String name,
                                    @RequestParam(required = false, name = "other_name") String other_name,
                                    @RequestParam(required = false, name = "nature") String nature,
                                    String gender, String birthday,
                                    @RequestParam(required = false, name = "id_type") String id_type,
                                    @RequestParam(required = false, name = "id_no") String id_no,
                                    String phone, String domicile,
                                    @RequestParam(required = false, name = "zip_code") String zip_code,
                                    String contact,
                                    @RequestParam(required = true, name = "abode") String abode,
                                    String unit_name, String unit_contact, String unit_id_type_id, String unit_id_no, String unit_abode, String legal_person_type) {
        Map<String, Object> result = partyConsoleService.save(id, cases_personnel_type, casesId, personnelId, type, name, other_name, nature, gender, birthday, id_type, id_no, phone, domicile, zip_code, contact, abode, unit_name, unit_contact, unit_id_type_id, unit_id_no, unit_abode, legal_person_type);
        log.info("[/console/third_party/save] {}", result);
        return result;
    }

    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(String page, String key) {
        Map<String, Object> result = partyConsoleService.getChooseList(page, key, null);
        log.info("[/console/third_party/get_choose_list] {}", result);
        return result;
    }

    public PartyConsoleService getPartyConsoleService() {
        return partyConsoleService;
    }

    public void setPartyConsoleService(PartyConsoleService partyConsoleService) {
        this.partyConsoleService = partyConsoleService;
    }
}
