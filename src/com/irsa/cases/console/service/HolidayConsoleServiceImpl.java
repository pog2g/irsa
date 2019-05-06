package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.HolidayManager;
import com.irsa.cases.model.Holiday;

@Service
public class HolidayConsoleServiceImpl implements HolidayConsoleService {
    Logger log = LoggerFactory.getLogger(HolidayConsoleServiceImpl.class);
    @Autowired
    HolidayManager holidayManager;
    
    @Override
    public Map<String, Object> getList() {
        try {
            return Utils.getSuccessMap(holidayManager.getList(Holiday.SELECT_ALL, null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String year, String title) {
        try {
            if (StringUtils.isBlank(year) || "-1".equals(year)) {
                return Utils.getErrorMap("请选择年份");
            }
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填写名称");
            }
            int num = holidayManager.getListCount(Holiday.SELECT_EXIST_BY_YEAR_TITLE, new Object[]{id, year, title});
            if (num != 0) {
                return Utils.getErrorMap("名称已存在");
            }
            num = holidayManager.getListCount(Holiday.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                holidayManager.executeSQL(Holiday.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), year, title});
            } else {
                holidayManager.executeSQL(Holiday.UPDATE, new Object[]{year, title, id});
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
                holidayManager.executeSQL(Holiday.DELETE, new Object[]{id});
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
            Map<String, Object> data = holidayManager.getMap(Holiday.SELECT_BY_ID, new Object[]{id});
            if (data == null) {
                return Utils.getErrorMap(null);
            }
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public HolidayManager getHolidayManager() {
        return holidayManager;
    }
    public void setHolidayManager(HolidayManager holidayManager) {
        this.holidayManager = holidayManager;
    }
}
