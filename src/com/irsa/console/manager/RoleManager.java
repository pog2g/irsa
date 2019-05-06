package com.irsa.console.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import com.irsa.console.model.RoleCompetence;
import com.jdbc.manager.BaseManager;

@Service
public class RoleManager extends BaseManager {
    public Map<String, Object> getRoleCompetenceList(Object roleId) {
        LinkedHashMap<String, Object> competence = new LinkedHashMap<String, Object>();
        List<Map<String, Object>> roleCompetenceList = getList(RoleCompetence.SELECT_TREE_BY_ROLEID, new Object[]{roleId});
        for (int i = 0; i < roleCompetenceList.size() ; i++){
            Map<String, Object> roleCompetence = roleCompetenceList.get(i);
            boolean exist = false;
            if (competence.containsKey(roleCompetence.get("parent"))) {
                exist = true;
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> children = (List<Map<String, Object>>)competence.get(roleCompetence.get("parent"));
                children.add(roleCompetence);
                continue;
            }
            if (!exist) {
                List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
                children.add(roleCompetence);
                competence.put(roleCompetence.get("parent").toString(), children);
            }
        }
        return competence;
    }
    
    public List<Map<String, Object>> getRoleCompetenceTree(Object roleId, String mode) {
        String sql = "";
        Object[] param = null;
        if ("0".equals(mode)) {
            sql = RoleCompetence.SELECT_TREE_BY_ROLEID; 
            param = new Object[]{roleId};
        } else {
            sql = RoleCompetence.SELECT_TREE_BY_ROLEID_STATE; 
            param = new Object[]{roleId, mode};
        }
        List<Map<String, Object>> competenceList = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> roleCompetenceList = getList(sql, param);
        for (Map<String, Object> roleCompetence : roleCompetenceList){
            boolean exist = false;
            for (Map<String, Object> competence : competenceList) {
                if (competence.get("id").equals(roleCompetence.get("pid"))) {
                    exist = true;
                    Map<String, Object> child = new HashMap<String, Object>();
                    child.put("id", roleCompetence.get("id"));
                    child.put("text", roleCompetence.get("text"));
                    child.put("url", roleCompetence.get("url"));
                    child.put("icon", roleCompetence.get("icon"));
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>)competence.get("children");
                    children.add(child);
                    break;
                } 
            }
            if (!exist) {
                Map<String, Object> child = new HashMap<String, Object>();
                child.put("id", roleCompetence.get("id"));
                child.put("text", roleCompetence.get("text"));
                child.put("url", roleCompetence.get("url"));
                child.put("icon", roleCompetence.get("icon"));
                List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
                children.add(child);
                
                Map<String, Object> parent = new HashMap<String, Object>();
                parent.put("id", roleCompetence.get("pid"));
                parent.put("text", roleCompetence.get("parent"));
                parent.put("icon", roleCompetence.get("parent_icon"));
                parent.put("children", children);
                
                competenceList.add(parent);
            }
        }
        return competenceList;
    }
}
