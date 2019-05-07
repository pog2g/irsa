package com.irsa.cases.console.action;

import com.common.utils.Utils;
import com.irsa.cases.console.service.PartyConsoleService;
import com.irsa.cases.model.CasesPersonnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/console/party")
public class PartyConsoleAction {
    Logger log = LoggerFactory.getLogger(PartyConsoleAction.class);
    @Autowired
    PartyConsoleService partyConsoleService;

    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(String page, @RequestParam(required = false, name = "search_key") String key) {
        Map<String, Object> result = partyConsoleService.getList(page, key);
        log.info("[/console/party/get_list] {}", result);
        return result;
    }


    /*Map<String, Object> save(String actId, String casesPersonnelType, String casesId, String personnelId, String type, String name, String nature, String ws_address, String zw, String legalPerson,
                             String gender, String birthday, String phone, String contact, String zipCode,
                             String provinceId, String cityId, String countyId, String address, String idTypeId, String idNo,
                             String unitIdTypeId, String unitIdNo, String unitContact, String domicile);*/
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, @RequestParam(required = true, name = "cases_id") String casesId, @RequestParam(required = true, name = "personnel_id") String personnelId, String type, String name, String other_name,
                                    @RequestParam(required = false, name = "nature") String nature, String gender, String birthday,
                                    @RequestParam(required = false, name = "id_type_id") String id_type_id,
                                    @RequestParam(required = false, name = "id_no") String id_no, String phone, String domicile,
                                    @RequestParam(required = false, name = "zip_code") String zip_code, String contact,
                                    @RequestParam(required = false, name = "abode") String abode,

                                    @RequestParam(required = false, name = "unit_name") String unit_name,
                                    @RequestParam(required = false, name = "unit_contact") String unit_contact,
                                    @RequestParam(required = false, name = "unit_id_type_id") String unit_id_type_id,
                                    @RequestParam(required = false, name = "unit_id_no") String unit_id_no,
                                    @RequestParam(required = false, name = "unit_abode") String unit_abode,
                                    @RequestParam(required = false, name = "legal_person_type") String legal_person_type
    ) {
        Map<String, Object> result = partyConsoleService.save(id, CasesPersonnel.PERSONNEL_TYPE_1, casesId, personnelId, type, Utils.toString(name), Utils.toString(other_name), nature, gender, Utils.toString(birthday), id_type_id, Utils.toString(id_no), Utils.toString(phone), Utils.toString(domicile), Utils.toString(zip_code), Utils.toString(contact), abode,
                unit_name, Utils.toString(unit_contact), unit_id_type_id, Utils.toString(unit_id_no), unit_abode, legal_person_type);
        log.info("[/console/party/save] {}", result);
        return result;
    }

    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(String page, String key, String type) {
        Map<String, Object> result = partyConsoleService.getChooseList(page, key, type);
        log.info("[/console/party/get_choose_list] {}", result);
        return result;
    }

    public PartyConsoleService getPartyConsoleService() {
        return partyConsoleService;
    }

    public void setPartyConsoleService(PartyConsoleService partyConsoleService) {
        this.partyConsoleService = partyConsoleService;
    }
}
