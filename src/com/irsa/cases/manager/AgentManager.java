package com.irsa.cases.manager;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.model.AgentFile;
import com.jdbc.manager.BaseManager;

@Service
public class AgentManager extends BaseManager {
    
    public void getIdFiles(Map<String, Object> agent) {
        List<Map<String, Object>> agentFileList = getList(AgentFile.SELECT_BY_RESID, new Object[]{agent.get("personnel_id")});
        if (agentFileList != null && agentFileList.size() > 0) {
            StringBuffer names = new StringBuffer();
            StringBuffer files = new StringBuffer();
            for (int i = 0; i < agentFileList.size(); i++) {
                Map<String, Object> agentFile = agentFileList.get(i);
                if (i == agentFileList.size() - 1) {
                    names.append(agentFile.get("real_name"));
                    files.append(Utils4File.getFileUrl(Utils4File.PATH_AGENT, Utils.toString(agentFile.get("file_name"))));
                } else {
                    names.append(agentFile.get("real_name")).append("@");
                    files.append(Utils4File.getFileUrl(Utils4File.PATH_AGENT, Utils.toString(agentFile.get("file_name")))).append("@");
                }
            }
            agent.put("agent_id_file_names", names.toString());
            agent.put("agent_id_files", files.toString());
        } else {
            agent.put("agent_id_file_names", "");
            agent.put("agent_id_files", "");
        }
    }
}
