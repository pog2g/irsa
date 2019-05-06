package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.RegulationsManager;
import com.irsa.cases.model.Regulations;
import com.jdbc.utils.Utils4JDBC;

@Service
public class RegulationsConsoleServiceImpl implements RegulationsConsoleService {
    Logger log = LoggerFactory.getLogger(RegulationsConsoleServiceImpl.class);
    @Autowired
    RegulationsManager regulationsManager;
    
    @Override
    public Map<String, Object> getList(String page, String typeId, String title, String department, String publishNumber, String publishTime, String executeTime, String key) {
        try {
            StringBuffer sql = new StringBuffer(Regulations.SELECT);
            Object[] param = null;
            if (StringUtils.isNotBlank(typeId)) {
                sql.append("and r.type_id = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{typeId});
            }
            if (StringUtils.isNotBlank(title)) {
                sql.append("and r.title like ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + title + "%"});
            }
            if (StringUtils.isNotBlank(department)) {
                sql.append("and r.department like ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + department + "%"});
            }
            if (StringUtils.isNotBlank(publishNumber)) {
                sql.append("and r.publish_number like ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + publishNumber + "%"});
            }
            if (StringUtils.isNotBlank(publishTime)) {
                sql.append("and r.publicTime = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{publishTime});
            }
            if (StringUtils.isNotBlank(executeTime)) {
                sql.append("and r.executeTime = ? ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{executeTime});
            }
            if (StringUtils.isNotBlank(key)) {
                sql.append("and (r.title like ? or r.department like ? or r.publish_number like ?) ");
                param = Utils4JDBC.mergeObjectArray(param, new Object[]{"%" + key + "%", "%" + key + "%", "%" + key + "%"});
            }
            sql.append("order by r.type_id,r.department,r.title");
            return Utils.getSuccessMap(regulationsManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String typeId, String department, String publishNumber, String publishTime, String executeTime, String title, String content) {
        try {
            if (StringUtils.isBlank(typeId) || "-1".equals(typeId)) {
                return Utils.getErrorMap("请选择属性");
            }
            if (StringUtils.isBlank(department)) {
                return Utils.getErrorMap("请填写制定机关");
            }
            if (StringUtils.isBlank(publishNumber)) {
                return Utils.getErrorMap("请填写颁布文号");
            }
            if (StringUtils.isBlank(publishTime)) {
                return Utils.getErrorMap("请填写颁布时间");
            }
            if (StringUtils.isBlank(executeTime)) {
                return Utils.getErrorMap("请填写施行时间");
            }
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写标题");
            }
            int num = regulationsManager.getListCount(Regulations.SELECT_EXIST_BY_TITLE, new Object[]{id, title});
            if (num != 0) {
                return Utils.getErrorMap("标题已存在");
            }
            if (StringUtils.isBlank(content)) {
                return Utils.getErrorMap("请填写内容");
            }
            num = regulationsManager.getListCount(Regulations.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                regulationsManager.executeSQL(Regulations.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), typeId, department, publishNumber, publishTime, executeTime, title, content});
            } else {
                regulationsManager.executeSQL(Regulations.UPDATE, new Object[]{typeId, department, publishNumber, publishTime, executeTime, title, content, id});
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
                regulationsManager.executeSQL(Regulations.DELETE, new Object[]{id});
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
            Map<String, Object> data = regulationsManager.getMap(Regulations.SELECT_BY_ID, new Object[]{id});
            if (data == null) {
                return Utils.getErrorMap("信息已不存在");
            }
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public RegulationsManager getRegulationsManager() {
        return regulationsManager;
    }
    public void setRegulationsManager(RegulationsManager regulationsManager) {
        this.regulationsManager = regulationsManager;
    }
}
