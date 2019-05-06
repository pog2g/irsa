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
import com.irsa.cases.manager.CasesPersonnelManager;
import com.irsa.cases.model.Agent;
import com.irsa.cases.model.AgentFile;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.CasesPersonnel;
import com.irsa.cases.model.CasesPersonnelTemp;
import com.irsa.cases.model.IdType;
import com.irsa.cases.model.TempFile;
import com.jdbc.utils.Utils4JDBC;

@Service
public class AgentConsoleServiceImpl implements AgentConsoleService {
    Logger log = LoggerFactory.getLogger(AgentConsoleServiceImpl.class);
    @Autowired
    AgentManager agentManager;
    @Autowired
    CasesPersonnelManager casesPersonnelManager;
    
    @Override
    public Map<String, Object> getList(String page, String key) {
        try {
            StringBuffer sql = new StringBuffer(Agent.SELECT);
            Object[] param = null;
            if (StringUtils.isNotBlank(key)) {
                sql.append("and (p.name like ? or p.id_no like ? )");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + key + "%", "%" + key + "%"});
            }
            sql.append("order by p.createtime ");
            Map<String, Object> result = agentManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list");
//            @SuppressWarnings("unchecked")
//            List<Map<String, Object>> dataList = (List<Map<String, Object>>)result.get("data_list");
//            if (dataList != null && dataList.size() > 0) {
//                for(Map<String, Object> data : dataList) {
//                    agentManager.getIdFiles(data);
//                }
//            }
            return Utils.getSuccessMap(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> save(String actId, String casesId, String clientId, String personnelId,
                                    String name, String nature, String gender, String birthday,
                                    String idTypeId, String idNo,  String phone, String abode ,
                                    String unit_name, String identity, String kinsfolk, String type) {
        try {
            String casesPersonnelType = "";
            if (StringUtils.isBlank(clientId) || "-1".equals(clientId)) {
                return Utils.getErrorMap("请选择委托人");
            }
            if (StringUtils.isBlank(birthday)) {
                birthday = null;
            }
            if (StringUtils.isBlank(name)) {
                return Utils.getErrorMap("请填写姓名");
            }
            if (StringUtils.isBlank(gender) || (!Agent.GENDER_1.equals(gender) && !Agent.GENDER_2.equals(gender))) {
                return Utils.getErrorMap("请选择性别");
            }
            if (StringUtils.isBlank(phone)) {
                return Utils.getErrorMap("请填写手机号码");
            }
            if (!Utils.checkPhone(phone)) {
                return Utils.getErrorMap("手机号码格式错误");
            }

            
            String pid = "";
            if (StringUtils.isNotBlank(personnelId)) {
                pid = personnelId;
            } else {
                if (StringUtils.isNotBlank(idNo)) {
                    Map<String, Object> agent = agentManager.getMap(Agent.SELECT_EXIST_BY_IDNO, new Object[]{idNo});
                    if (agent != null) {
                        pid = Utils.toString(agent.get("id"));
                    }
                }
            }
            
            // 案件是否存在
            int isCasesExist = agentManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            if (isCasesExist == 0) {
                Map<String, Object> client = agentManager.getMap(CasesPersonnelTemp.SELECT_EXIST_BY_ID, new Object[]{clientId});
                /*if (client == null) {
                    return Utils.getErrorMap("委托人不存在");
                }*/
                if (client!=null&&StringUtils.isNotBlank(pid)) {
                    if (CasesPersonnel.PERSONNEL_TYPE_1.equals(client.get("type"))) {
                        casesPersonnelType = CasesPersonnel.PERSONNEL_TYPE_4;
                        int isPersonnelExist = agentManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID_CLIENTID, new Object[]{casesId, pid, clientId});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    } else if (CasesPersonnel.PERSONNEL_TYPE_2.equals(client.get("type"))) {
                        casesPersonnelType = CasesPersonnel.PERSONNEL_TYPE_6;
                        int isPersonnelExist = agentManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, pid});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    } else if (CasesPersonnel.PERSONNEL_TYPE_3.equals(client.get("type"))) {
                        casesPersonnelType = CasesPersonnel.PERSONNEL_TYPE_5;
                        int isPersonnelExist = agentManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, pid});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    }
                }
            } else {
                Map<String, Object> client = agentManager.getMap(CasesPersonnel.SELECT_EXIST_BY_ID, new Object[]{clientId});
                if (client == null) {
                    return Utils.getErrorMap("委托人不存在");
                }
                if (StringUtils.isNotBlank(pid)) {
                    if (CasesPersonnel.PERSONNEL_TYPE_1.equals(client.get("personnel_type"))) {
                        casesPersonnelType = CasesPersonnel.PERSONNEL_TYPE_4;
                        int isPersonnelExist = agentManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNELID_CLIENTID, new Object[]{casesId, pid, clientId});
                       /* if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }*/
                    } else if (CasesPersonnel.PERSONNEL_TYPE_2.equals(client.get("personnel_type"))) {
                        casesPersonnelType = CasesPersonnel.PERSONNEL_TYPE_6;
                        int isPersonnelExist = agentManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, pid});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    } else if (CasesPersonnel.PERSONNEL_TYPE_3.equals(client.get("personnel_type"))) {
                        casesPersonnelType = CasesPersonnel.PERSONNEL_TYPE_5;
                        int isPersonnelExist = agentManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNELID, new Object[]{casesId, pid});
                        if (isPersonnelExist != 0) {
                            return Utils.getErrorMap("当事人已存在");
                        }
                    }
                }
            }
            List<Map<String, Object>> tempFileList1 = agentManager.getList(TempFile.SELECT_BY_RESID_MODE, new Object[]{actId, "1"});
            Map<String, Object> tempFile2 = agentManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{actId, "2"});
            /*if (tempFile2 == null) {
                return Utils.getErrorMap("请上传委托代理书");
            } */
            if (StringUtils.isBlank(pid)) {
               /*if (tempFileList1 == null || tempFileList1.size() == 0) {
                    return Utils.getErrorMap("请上传证件信息");
                }*/ 
                
                personnelId = Utils.getId();
                /*insert into irsa_agent(createtime,id,name,nature,gender,birthday,id_type_id, id_no, phone,domicile,unit_name,identity, kinsfolk,type)*/
                agentManager.executeSQL(Agent.INSERT, new Object[]{Utils.getCreateTime(), personnelId, name, nature, gender, birthday, idTypeId, idNo,  phone, abode, unit_name, identity, kinsfolk, type});
            } else {
                personnelId = pid;
                List<Map<String, Object>> fileList1 = agentManager.getList(AgentFile.SELECT_BY_RESID, new Object[]{personnelId});
                
                agentManager.executeSQL(Agent.UPDATE, new Object[]{name, nature, gender, birthday, idTypeId, idNo,  phone, abode, unit_name, identity, kinsfolk, type, personnelId});
            }
            if (tempFileList1 != null && tempFileList1.size() > 0) {
                agentManager.executeSQL(AgentFile.DELETE_BY_RESID, new Object[]{personnelId});
                for (Map<String, Object> tempFile1 : tempFileList1) {
                    Utils4File.copyTempToReal(Utils4File.PATH_AGENT, tempFile1.get("file_name").toString());
                    agentManager.executeSQL(AgentFile.INSERT, new Object[]{Utils.getId(), tempFile1.get("create_time"), tempFile1.get("type"), personnelId, 
                            tempFile1.get("real_name"), tempFile1.get("size"), tempFile1.get("ext"), tempFile1.get("file_name")});
                }
            }
            
            if (isCasesExist == 0) {
                String id = Utils.getId();
                agentManager.executeSQL(CasesPersonnelTemp.INSERT, new Object[]{id, Utils.getCreateTime(), casesPersonnelType, casesId, personnelId, clientId});
                if (tempFile2 != null) {
                    agentManager.executeSQL(TempFile.UPDATE_PERSONNELID, new Object[]{id, tempFile2.get("id")});
                }
                return Utils.getSuccessMap(null);
            }
            casesPersonnelManager.save4Agent(casesPersonnelType, casesId, clientId, personnelId, tempFile2);
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String,Object> getChooseList(String page, String key) {
        try {
            StringBuffer sql = new StringBuffer(Agent.SELECT_4_CHOOSE);
            Object[] param = new Object[]{"%" + key + "%", "%" + key + "%"};
            Map<String,Object> result = agentManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list");
/*            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = (List<Map<String, Object>>)result.get("data_list");
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    List<Map<String, Object>> agentFileList = agentManager.getList(AgentFile.SELECT_BY_RESID, new Object[]{data.get("id")});
                    if (agentFileList != null && agentFileList.size() > 0) {
                        for (Map<String, Object> agentFile : agentFileList) {
                            AgentManager.getIdFiles(agentFile);
                        }
                    }
                    data.put("agent_file_list", agentFileList);
                }
            }*/
            return Utils.getSuccessMap(result);
        } catch(Exception e) {
            log.error(e.getMessage(),e); 
            return Utils.getErrorMap(null);
        }
    }

    public AgentManager getAgentManager() {
        return agentManager;
    }
    public void setAgentManager(AgentManager agentManager) {
        this.agentManager = agentManager;
    }
    public CasesPersonnelManager getCasesPersonnelManager() {
        return casesPersonnelManager;
    }
    public void setCasesPersonnelManager(CasesPersonnelManager casesPersonnelManager) {
        this.casesPersonnelManager = casesPersonnelManager;
    }
}
