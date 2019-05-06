package com.irsa.cases.manager;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.model.Agent;
import com.irsa.cases.model.AgentFile;
import com.irsa.cases.model.CasesFile;
import com.irsa.cases.model.CasesPersonnel;
import com.irsa.cases.model.CasesPersonnelFile;
import com.irsa.cases.model.Defendant;
import com.irsa.cases.model.DefendantFile;
import com.irsa.cases.model.Party;
import com.irsa.cases.model.PartyFile;
import com.jdbc.manager.BaseManager;

@Service
public class CasesPersonnelManager extends BaseManager {

    //将申请人/第三人/建议第三人保存至CasesPersonnel案件当事人
    @SuppressWarnings("unchecked")
    public void save4Party(String personnelType, String casesId, String personnelId, String representative, List<Map<String, Object>> agentList) {
        Map<String, Object> personnel = getMap(Party.SELECT_BY_ID, new Object[]{personnelId});
        if (personnel != null) {
            String id = Utils.getId();

            System.out.println("personnel=========" + personnel.size());

            //现SQL参数
            /*insert into irsa_cases_personnel(createtime,id,cases_id,personnel_type,representative,personnel_id,
                    type,name,other_name,nature,gender,birthday,id_type_id,id_no,phone,domicile,zip_code,contact,abode,
                    unit_name,unit_contact,unit_id_type_id,unit_id_no,unit_abode,legal_person_type)*/
            executeSQL(CasesPersonnel.INSERT_APPLY_OR_THIRDPARTY, new Object[]{Utils.getCreateTime(), id, casesId, personnelType, representative, personnelId,
                    personnel.get("type"),personnel.get("name"),personnel.get("other_name"),personnel.get("nature"),personnel.get("gender"),
                    StringUtils.isBlank(Utils.toString(personnel.get("birthday")))?null:personnel.get("birthday"),personnel.get("id_type_id"), personnel.get("id_no"),
                    personnel.get("phone"), personnel.get("domicile"),
                    personnel.get("zip_code"), personnel.get("contact"), personnel.get("abode"), personnel.get("unit_name"), personnel.get("unit_contact"),
                    personnel.get("unit_id_type_id"), personnel.get("unit_id_no"),  personnel.get("unit_abode"), personnel.get("legal_person_type")});
            List<Map<String, Object>> fileList = getList(PartyFile.SELECT_BY_RESID, new Object[]{personnelId});
            if (fileList != null && fileList.size() > 0) {
                for (Map<String, Object> file : fileList) {
                    executeSQL(CasesPersonnelFile.INSERT, new Object[]{Utils.getCreateTime(), Utils.getId(), id,
                            null, file.get("type"), file.get("real_name"), file.get("size"), file.get("ext"), file.get("file_name")});
                }
            }
            // 保存申请人委托代理人
            if (agentList != null && agentList.size() > 0) {
                String personnelType4Agent = CasesPersonnel.PERSONNEL_TYPE_4;
                if (CasesPersonnel.PERSONNEL_TYPE_3.equals(personnelType)) {
                    personnelType4Agent = CasesPersonnel.PERSONNEL_TYPE_5;
                }
                for (Map<String, Object> agent : agentList) {
                    save4Agent(personnelType4Agent, casesId, id, agent.get("personnel_id").toString(), (Map<String, Object>)agent.get("agent_file"));
                }
            }
        }
    }
    
    
    public void save4Agent(String personnelType, String casesId, String clientId, String personnelId, Map<String, Object> agentFile) {
        Map<String, Object> personnel = getMap(Agent.SELECT_BY_ID, new Object[]{personnelId});
        if (personnel != null) {
            String id = Utils.getId();
            //现SQL参数
            /*insert into irsa_cases_personnel(createtime, id, cases_id, personnel_type, personnel_client, personnel_id,
                    name, nature, gender, birthday, id_type_id, id_no, phone, domicile, unit_name, identity, kinsfolk, legal_person_type)*/

            executeSQL(CasesPersonnel.INSERT, new Object[]{Utils.getCreateTime(), id, casesId, personnelType, clientId, personnelId,
                    personnel.get("name"), personnel.get("nature"), personnel.get("gender"),
                    StringUtils.isBlank(Utils.toString(personnel.get("birthday")))?null:personnel.get("birthday"),
                    personnel.get("id_type_id"), personnel.get("id_no"), personnel.get("phone"), personnel.get("domicile"),
                    personnel.get("unit_name"), personnel.get("identity"), personnel.get("kinsfolk"), personnel.get("legal_person_type")});
            List<Map<String, Object>> fileList = getList(AgentFile.SELECT_BY_RESID, new Object[]{personnelId});
            if (fileList != null && fileList.size() > 0) {
                for (Map<String, Object> file : fileList) {
                    executeSQL(CasesPersonnelFile.INSERT, new Object[]{Utils.getCreateTime(), Utils.getId(), id,
                            null, file.get("type"), file.get("real_name"), file.get("size"), file.get("ext"), file.get("file_name")});
                }
            }
            if (agentFile != null) {
                String mode = "";
                if (CasesPersonnel.PERSONNEL_TYPE_4.equals(personnelType)) {
                    mode = CasesFile.MODE_4;
                } else if (CasesPersonnel.PERSONNEL_TYPE_5.equals(personnelType)) {
                    mode = CasesFile.MODE_10;
                } else {
                    mode = CasesFile.MODE_8;
                }
                Utils4File.copyTempToReal(Utils4File.PATH_CASES, agentFile.get("file_name").toString());
                executeSQL(CasesFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(),
                        mode, agentFile.get("type"), casesId, agentFile.get("real_name"), agentFile.get("size"), agentFile.get("ext"), agentFile.get("file_name"), id});
            }
        }
    }

    //保存被申请人到案件当事人
    public void save4Defendant(String casesId, String personnelId) {
    	Map<String, Object> personnel = getMap(Defendant.SELECT_BY_ID, new Object[]{personnelId});
    	if(personnel!=null) {
    		String id = Utils.getId();
            List<Map<String, Object>> fileList = getList(DefendantFile.SELECT_BY_RESID, new Object[]{personnelId});
            if (fileList != null && fileList.size() > 0) {
                for (Map<String, Object> file : fileList) {
                    executeSQL(CasesPersonnelFile.INSERT, new Object[]{Utils.getCreateTime(), Utils.getId(), id,
                            file.get("mode"), file.get("type"), file.get("real_name"), file.get("size"), file.get("ext"), file.get("file_name")});
                }
            }
    	}
    	List<Map<String, Object>> tempList = getList(Defendant.SELECT_TEMP_DEFENDANT, new Object[]{casesId});
    	if(tempList.size()>0) {
    		for(int i=0;i<tempList.size();i++) {
    			String id = Utils.getId();
                /*insert into irsa_cases_personnel(createtime,id,cases_id,personnel_type,personnel_id," +
                        "type,name,unit_name,unit_abode,unit_contact,legal_person_type)*/
                executeSQL(CasesPersonnel.INSERT_DEFENDANT, new Object[]{Utils.getCreateTime(), id, casesId,CasesPersonnel.PERSONNEL_TYPE_2, tempList.get(i).get("personnel_id"),
                		tempList.get(i).get("type"), tempList.get(i).get("name"), tempList.get(i).get("unit_name"),tempList.get(i).get("unit_abode"),tempList.get(i).get("unit_contact"),tempList.get(i).get("legal_person_type")});
    		}
    	}
    }
}
