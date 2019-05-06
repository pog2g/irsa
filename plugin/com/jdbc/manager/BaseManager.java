package com.jdbc.manager;

import java.util.List;
import java.util.Map;

import com.jdbc.utils.Utils4JDBC;

public class BaseManager {

    public void executeSQL(String sql, Object[] params) {
        Utils4JDBC.executeQuery(sql, params);
    }
    
    public List<Map<String, Object>> getList(String sql, Object[] params) {
        return Utils4JDBC.getList(sql, params);
    }
    
    public Map<String, Object> getMap(String sql, Object[] params) {
        List<Map<String, Object>> list = Utils4JDBC.getList(sql, params);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    
    public int getListCount(String sql, Object[] params) {
        return Utils4JDBC.getListCount(sql, params);
    }
    
    public Map<String, Object> getPageList(String sql, Object[] param, int pageIndex, int perNum, String listName) {
        return Utils4JDBC.getPageList(sql, param, pageIndex, perNum, listName);
    }
}
