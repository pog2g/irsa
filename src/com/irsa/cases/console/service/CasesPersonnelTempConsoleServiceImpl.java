package com.irsa.cases.console.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.manager.AgentManager;
import com.irsa.cases.manager.CasesPersonnelTempManager;
import com.irsa.cases.manager.DefendantManager;
import com.irsa.cases.manager.PartyManager;
import com.irsa.cases.model.CasesPersonnel;
import com.irsa.cases.model.CasesPersonnelTemp;

@Service
public class CasesPersonnelTempConsoleServiceImpl implements CasesPersonnelTempConsoleService {
    Logger log = LoggerFactory.getLogger(CasesPersonnelTempConsoleServiceImpl.class);
    @Autowired
    AgentManager agentManager;
    @Autowired
    CasesPersonnelTempManager casesPersonnelTempManager;
    @Autowired
    DefendantManager defendantManager;
    @Autowired
    PartyManager partyManager;
    
    @Override
    public Map<String,Object> getChooseList4Apply(String casesId) {
        try {
            return Utils.getSuccessMap(partyManager.getList(CasesPersonnelTemp.SELECT_CHOOSE_BY_CASESID_TYPE_1 , new Object[]{casesId}));
        } catch(Exception e) {
            log.error(e.getMessage(),e); 
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getList(String casesId, String casesPersonnelType) {
        try {
            if (CasesPersonnel.PERSONNEL_TYPE_1.equals(casesPersonnelType) || 
                    CasesPersonnel.PERSONNEL_TYPE_3.equals(casesPersonnelType) ||
                    CasesPersonnel.PERSONNEL_TYPE_7.equals(casesPersonnelType)) {
                List<Map<String, Object>> list = casesPersonnelTempManager.getList(CasesPersonnelTemp.SELECT_BY_CASESID_TYPE_137, new Object[]{casesId, casesPersonnelType});
                if (list != null && list.size() > 0) {
                    for(Map<String, Object> data : list) {
                        PartyManager.getFileUrl(data);
                        List<Map<String, Object>> agentList = casesPersonnelTempManager.getList(CasesPersonnelTemp.SELECT_BY_CASESID_CLIENTID, new Object[]{casesId, data.get("id")});
                        if (agentList != null && agentList.size() > 0) {
                            for (Map<String, Object> agent : agentList) {
                                agent.put("agent_file", Utils4File.getFileUrl(Utils4File.PATH_TEMP, agent.get("agent_file").toString()));
                                agentManager.getIdFiles(agent);
                            }
                        }
                        data.put("agent_list", agentList);
                    }
                }
                return Utils.getSuccessMap(list);
            }
            
            List<Map<String, Object>> list = casesPersonnelTempManager.getList(CasesPersonnelTemp.SELECT_BY_CASESID_TYPE_2, new Object[]{casesId});
            if (list != null && list.size() > 0) {
                for(Map<String, Object> data : list) {
                    DefendantManager.getFileUrl(data);
                }
            }
            return Utils.getSuccessMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> delete(String id) {
        try {
            casesPersonnelTempManager.executeSQL(CasesPersonnelTemp.DELETE_BY_ID, new Object[]{id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> setRepresentative(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择申请人");
            }
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                casesPersonnelTempManager.executeSQL(CasesPersonnelTemp.UPDATE_REPRESENTATIVE, new Object[]{CasesPersonnel.REPRESENTATIVE_1 , id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> deleteRepresentative(String id) {
        try {
            casesPersonnelTempManager.executeSQL(CasesPersonnelTemp.UPDATE_REPRESENTATIVE, new Object[]{CasesPersonnel.REPRESENTATIVE_0 , id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getList4Representative(String casesId) {
        try {
            List<Map<String, Object>> list = casesPersonnelTempManager.getList(CasesPersonnelTemp.SELECT_BY_CASESID_REPRESENTATIVE, 
                    new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1, CasesPersonnel.REPRESENTATIVE_1});
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    PartyManager.getFileUrl(data);
                }
            }
            return Utils.getSuccessMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    public AgentManager getAgentManager() {
        return agentManager;
    }
    public void setAgentManager(AgentManager agentManager) {
        this.agentManager = agentManager;
    }
    public CasesPersonnelTempManager getCasesPersonnelTempManager() {
        return casesPersonnelTempManager;
    }
    public void setCasesPersonnelTempManager(
            CasesPersonnelTempManager casesPersonnelTempManager) {
        this.casesPersonnelTempManager = casesPersonnelTempManager;
    }
    public DefendantManager getDefendantManager() {
        return defendantManager;
    }
    public void setDefendantManager(DefendantManager defendantManager) {
        this.defendantManager = defendantManager;
    }
    public PartyManager getPartyManager() {
        return partyManager;
    }
    public void setPartyManager(PartyManager partyManager) {
        this.partyManager = partyManager;
    }
}
