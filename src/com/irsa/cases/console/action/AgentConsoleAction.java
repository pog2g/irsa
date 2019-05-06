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
import com.irsa.cases.console.service.AgentConsoleService;

@Controller
@RequestMapping("/console/agent")
public class AgentConsoleAction {
    Logger log = LoggerFactory.getLogger(AgentConsoleAction.class);
    @Autowired
    AgentConsoleService agentConsoleService;
    
    @RequestMapping("/get_list")
    @ResponseBody
    public Map<String, Object> getList(String page, @RequestParam(required=false, name="search_key") String key) {
        Map<String, Object> result = agentConsoleService.getList(page, key);
        log.info("[/console/agent/get_list] {}", result);
        return result;
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(String id, 
            @RequestParam(required=true, name="cases_id") String casesId,
            @RequestParam(required=false, name="personnel_id") String personnelId,
            @RequestParam(required=false, name="client_id") String clientId,
            String name, String nature, String gender, String birthday,
            @RequestParam(required=false, name="idTypeId") String idTypeId,
            @RequestParam(required=false, name="idNo") String idNo,
            String phone, String abode, String unit_name,
            @RequestParam(required=false, name="kinsfolk") String kinsfolk,
            @RequestParam(required=false, name="identity") String identity, String type
            ) {
        Map<String, Object> result = agentConsoleService.save(id, casesId, clientId, personnelId, Utils.toString(name), Utils.toString(nature), gender, Utils.toString(birthday), idTypeId, Utils.toString(idNo), Utils.toString(phone), Utils.toString(abode), Utils.toString(unit_name), Utils.toString(identity), Utils.toString(kinsfolk), Utils.toString(type)
                );
        log.info("[/console/agent/save] {}", result);
        return result;
    }
    
    @RequestMapping("/get_choose_list")
    @ResponseBody
    public Map<String, Object> getChooseList(String page, String key) {
        Map<String, Object> result = agentConsoleService.getChooseList(page, key);
        log.info("[/console/agent/get_choose_list] {}", result);
        return result;
    }

    public AgentConsoleService getAgentConsoleService() {
        return agentConsoleService;
    }
    public void setAgentConsoleService(AgentConsoleService agentConsoleService) {
        this.agentConsoleService = agentConsoleService;
    }
}
