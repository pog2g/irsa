package com.irsa.console.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irsa.console.manager.CompetenceManager;
import com.irsa.console.model.Account;
import com.irsa.console.model.Competence;
import com.irsa.console.model.RoleCompetence;
import com.common.utils.Utils;

@Service
public class CompetenceServiceImpl implements CompetenceService {
    Logger log = LoggerFactory.getLogger(CompetenceServiceImpl.class);
    @Autowired
    CompetenceManager competenceManager;
    
    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(competenceManager.getList(Competence.SELECT_ALL, null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String parentId, String id, String icon, String title, String url, String state, String remark) {
        try {
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写名称");
            }
            if (StringUtils.isBlank(icon)) {
                return Utils.getErrorMap("请选择图标");
            }
            
            if (StringUtils.isBlank(parentId) || "-1".equals(parentId)) {//父级
                int num = competenceManager.getListCount(Competence.SELECT_EXIST_TITLE_4_PARENT, new Object[]{id, title});
                if (num != 0) {
                    return Utils.getErrorMap("名称已存在");
                }
                
                parentId = "";
                url = "";
                state = "1";
            } else {//子级
                int num = competenceManager.getListCount(Competence.SELECT_EXIST_TITLE_4_CHILD, new Object[]{id, parentId, title});
                if (num != 0) {
                    return Utils.getErrorMap("名称已存在");
                }
                if (StringUtils.isBlank(url)) {
                    return Utils.getErrorMap("请填写链接");
                }
                num = competenceManager.getListCount(Competence.SELECT_EXIST_URL, new Object[]{id, url});
                if (num != 0) {
                    return Utils.getErrorMap("链接已存在");
                }
                if (StringUtils.isBlank(state) || (!Competence.STATE_1.equals(state) && !Competence.STATE_2.equals(state))) {
                    return Utils.getErrorMap("请选择状态");
                }
            }
            int num = competenceManager.getListCount(Competence.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
                int sequence = competenceManager.getListCount(Competence.SELECT_EXIST, null);
                competenceManager.executeSQL(Competence.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), sequence + 1, parentId, icon, title, url, state, remark});
            } else {
                competenceManager.executeSQL(Competence.UPDATE, new Object[]{parentId, icon, title, url, state, remark, id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> delete(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择数据");
            }
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                competenceManager.executeSQL(Competence.DELETE, new Object[]{id});
                competenceManager.executeSQL(RoleCompetence.DELETE_BY_COMPETENCEID, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> sort(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择数据");
            }
            String[] idArr = ids.split(",");
            for (int i = 0; i < idArr.length; i++) {
                competenceManager.executeSQL(Competence.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList4Parent() {
        try {
            return Utils.getSuccessMap(Utils.changeToSelectList(competenceManager.getList(Competence.SELECT_4_PARENT, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList(String accountId) {
        try {
            Map<String, Object> account = competenceManager.getMap(Account.SELECT_EXIST_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap(null);    
            }
            
            if ("8f1cf600984342f4947214d8f7770414".equals(account.get("role_id"))) {
                List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
                List<Map<String, Object>> parentList = competenceManager.getList(Competence.SELECT_4_PARENT, null);
                for (Map<String, Object> parent : parentList) {
                    List<Map<String, Object>> childList = competenceManager.getList(Competence.SELECT_4_CHILD, new Object[]{parent.get("id")});
                    if (childList != null && childList.size() > 0) {
                        parent.put("children", childList);
                        data.add(parent);
                    }
                }
                return Utils.getSuccessMap(data);
            }
            
            List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
            List<Map<String, Object>> parentList = competenceManager.getList(Competence.SELECT_4_PARENT_BY_ROLEID, new Object[]{account.get("role_id")});
            for (Map<String, Object> parent : parentList) {
                List<Map<String, Object>> childList = competenceManager.getList(Competence.SELECT_4_CHILD_BY_ROLEID, new Object[]{account.get("role_id"), parent.get("id")});
                if (childList != null && childList.size() > 0) {
                    parent.put("children", childList);
                    data.add(parent);
                }
            }
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getMap(String id) {
        try {
            return Utils.getSuccessMap(competenceManager.getMap(Competence.SELECT_EXIST_ID, new Object[]{id}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getList4Menu(String accountId) {
        try {
            Map<String, Object> account = competenceManager.getMap(Account.SELECT_EXIST_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap(null); 
            }
            return Utils.getSuccessMap(competenceManager.getList(Competence.SELECT_BY_ROLEID, new Object[]{account.get("role_id"), account.get("role_id")}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> saveMenu(String id, String title, String state, String remark) {
        try {
            Map<String, Object> data = competenceManager.getMap(Competence.SELECT_EXIST_ID, new Object[]{id});
            if (data == null) {
                return Utils.getErrorMap(null);
            }
            if (data.get("parent_id") == null || "".equals(data.get("parent_id"))) {
                competenceManager.executeSQL(Competence.UPDATE_MENU_1, new Object[]{title, id});
            } else {
                competenceManager.executeSQL(Competence.UPDATE_MENU_2, new Object[]{title, state, remark, id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public CompetenceManager getCompetenceManager() {
        return competenceManager;
    }
    public void setCompetenceManager(CompetenceManager competenceManager) {
        this.competenceManager = competenceManager;
    }
}
