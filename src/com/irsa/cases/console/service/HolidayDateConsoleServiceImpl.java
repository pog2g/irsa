package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.HolidayDateManager;
import com.irsa.cases.model.Holiday;
import com.irsa.cases.model.HolidayDate;

@Service
public class HolidayDateConsoleServiceImpl implements HolidayDateConsoleService {
    Logger log = LoggerFactory.getLogger(HolidayDateConsoleServiceImpl.class);
    @Autowired
    HolidayDateManager holidayDateManager;
    
    @Override
    public Map<String, Object> getList(String holidayId) {
        try {
            return Utils.getSuccessMap(holidayDateManager.getList(HolidayDate.SELECT_BY_HOLIDAYID, new Object[]{holidayId}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save(String id, String holidayId, String type, String date) {
        try {
            int num = holidayDateManager.getListCount(Holiday.SELECT_EXIST_BY_ID, new Object[]{holidayId});
            if (num == 0) {
                return Utils.getErrorMap("节假日不存在");
            }
            if (StringUtils.isBlank(type) || "-1".equals(type)) {
                return Utils.getErrorMap("请选择类型");
            }
            if (StringUtils.isBlank(date)) {
                return Utils.getErrorMap("请选择日期");
            }
            num = holidayDateManager.getListCount(HolidayDate.SELECT_EXIST_BY_DATE, new Object[]{id, date});
            if (num != 0) {
                return Utils.getErrorMap("日期已存在");
            }
            num = holidayDateManager.getListCount(HolidayDate.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                holidayDateManager.executeSQL(HolidayDate.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), holidayId, type, date});
            } else {
                holidayDateManager.executeSQL(HolidayDate.UPDATE, new Object[]{type, date, id});
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
                holidayDateManager.executeSQL(HolidayDate.DELETE, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public HolidayDateManager getHolidayDateManager() {
        return holidayDateManager;
    }
    public void setHolidayDateManager(HolidayDateManager holidayDateManager) {
        this.holidayDateManager = holidayDateManager;
    }
}
