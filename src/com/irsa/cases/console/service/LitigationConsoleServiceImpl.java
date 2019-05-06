package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.LitigationManager;
import com.irsa.cases.model.Litigation;
import com.jdbc.utils.Utils4JDBC;

@Service
public class LitigationConsoleServiceImpl implements LitigationConsoleService {
    Logger log = LoggerFactory.getLogger(LitigationConsoleServiceImpl.class);
    @Autowired
    LitigationManager litigationManager;
    
    @Override
    public Map<String, Object> getList(String page, String year, String reasonId, String mode, String type, String court, String title, String keys) {
        try {
            StringBuffer sql = new StringBuffer(Litigation.SELECT);
            Object[] param = null;
            if (StringUtils.isNotBlank(year) && !"-1".equals(reasonId)) {
                sql.append("and l.year = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{year});
            }
            if (StringUtils.isNotBlank(reasonId) && !"-1".equals(reasonId)) {
                sql.append("and l.reason_id = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{reasonId});
            }
            if (StringUtils.isNotBlank(mode) && !"-1".equals(mode)) {
                sql.append("and l.mode = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{mode});
            }
            if (StringUtils.isNotBlank(type) && !"-1".equals(type)) {
                sql.append("and l.type = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{type});
            }
            if (StringUtils.isNotBlank(court)) {
                sql.append("and r.court = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{court});
            }
            if (StringUtils.isNotBlank(title)) {
                sql.append("and r.title = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{title});
            }
            if (StringUtils.isNotBlank(keys)) {
            	String[] keyArr = keys.split(",");
            	for (String key : keyArr) {
            		sql.append("and (l.title like ? or l.label_reason like ?) ");
            		param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + key + "%", "%" + key + "%"});
            	}
            }
            sql.append("order by l.year,l.reason_id,l.mode,l.type,l.title");
            return Utils.getSuccessMap(litigationManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String year, String reasonId, String court, String mode, String type, String title, String content) {
        try {
            if (StringUtils.isBlank(year) || "-1".equals(year)) {
                return Utils.getErrorMap("请选择年份");
            }
            if (StringUtils.isBlank(reasonId) || "-1".equals(reasonId)) {
                return Utils.getErrorMap("请选择案由");
            }
            if (StringUtils.isBlank(court)) {
                return Utils.getErrorMap("请填写审理法院");
            }
            if (StringUtils.isBlank(mode) || "-1".equals(mode)) {
                return Utils.getErrorMap("请选择审理程序");
            }
            if (StringUtils.isBlank(type) || "-1".equals(type)) {
                return Utils.getErrorMap("请选择文书性质");
            }
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写标题");
            }
            int num = litigationManager.getListCount(Litigation.SELECT_EXIST_BY_YEAR_TITLE, new Object[]{id, year, title});
            if (num != 0) {
                return Utils.getErrorMap("标题已存在");
            }
            if (StringUtils.isBlank(content)) {
                return Utils.getErrorMap("请填写内容");
            }
            num = litigationManager.getListCount(Litigation.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                litigationManager.executeSQL(Litigation.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), year, reasonId, court, mode, type, title, content});
            } else {
                litigationManager.executeSQL(Litigation.UPDATE, new Object[]{year, reasonId, court, mode, type, title, content, id});
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
                litigationManager.executeSQL(Litigation.DELETE, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getMap(String id) {
        try {
            Map<String, Object> data = litigationManager.getMap(Litigation.SELECT_BY_ID, new Object[]{id});
            if (data == null) {
                return Utils.getErrorMap("信息已不存在");
            }
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public LitigationManager getLitigationManager() {
        return litigationManager;
    }
    public void setLitigationManager(LitigationManager litigationManager) {
        this.litigationManager = litigationManager;
    }
}
