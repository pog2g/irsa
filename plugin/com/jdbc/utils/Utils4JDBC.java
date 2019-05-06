package com.jdbc.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Utils4JDBC {
    private static Logger log = LoggerFactory.getLogger(Utils4JDBC.class);
    private static DataSource dataSource = null;
    
    static {
        dataSource = new ComboPooledDataSource("database");
    }
    
    /**
     * 合并Object数组
     * @param objArr1
     * @param objArr2
     * @return
     */
    public static Object[] mergeObjectArray(Object[] objArr1, Object[] objArr2) {
        if (objArr1 == null) {
            objArr1 = new Object[]{};
        }
        if (objArr2 == null) {
            objArr2 = new Object[]{};
        }
        Object[] objArr = new Object[objArr1.length+objArr2.length];
        for (int i = 0; i < objArr1.length; i++) {
            objArr[i] = objArr1[i];
        }
        for (int j = 0; j < objArr2.length; j++) {
            objArr[j+objArr1.length] = objArr2[j];
        }
        return objArr;
    }
    
    /**
     * 查询分页列表
     * @param sql 查询sql
     * @param param 查询参数
     * @param pageIndex 页码
     * @param perNum 每页数据数目
     * @param listName list对应的key值
     */
    public static Map<String, Object> getPageList(String sql, Object[] param, int pageIndex, int perNum, String listName) {
        Map<String,Object> data = new HashMap<String, Object>();
        //起始序号
        int startNum = (pageIndex-1) * perNum;
        //从第1页到当前页的数据总数
        int pageNum = pageIndex * perNum;
        //查询总数
        int total = getListCount(sql, param);
        data.put("data_total", total);
        //是否已返回全部数据
        if (pageNum >= total) {
            data.put("is_all", "1");
        } else {
            data.put("is_all", "0");
        }
        //总页数
        int pageTotal = total / perNum;
        if (pageTotal * perNum < total) {
            pageTotal += 1;
        }
        data.put("page_total", pageTotal);
        //当前页数
        data.put("page", pageIndex);
        //分页查询sql
        sql += " limit ?,?";
        //分页查询参数
        Object[] pageParam = new Object[]{startNum, perNum};
        if(param != null && param.length > 0 ) {
            pageParam = mergeObjectArray(param, pageParam);
        }
        List<Map<String, Object>> list = getList(sql, pageParam);
        data.put(listName, list);
        return data;
    }
    
    /**
     * 
     * @param sql
     * @param params
     * @return
     */
    public static int getListCount(String sql, Object[] params) {
        sql = "select count(*) total from (" + sql + ") totaltmp";
        Map<String, Object> result = getMap(sql, params);
        return Integer.parseInt(result.get("total").toString());
    }
    
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getList(String sql, Object[] params) {
        return (List<Map<String, Object>>)runExecute(sql, params);
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(String sql, Object[] params) {
        List<Map<String, Object>> list = (List<Map<String, Object>>)runExecute(sql, params);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    
    public static int executeQuery(String sql, Object[] params) {
        return (Integer)runExecute(sql, params);
    }
    
    private static Object runExecute(String sql, Object[] params) {
        StringBuffer p = new StringBuffer(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
            int updateRecordNumber = 0;
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    ps.setString(i + 1, param == null ? null : param.toString());
                    p.append("@").append(param == null ? "" : param.toString());
                }
            }
            if (isSelectStatement(sql)) {
                log.info(p.toString());
                res = ps.executeQuery();
                ResultSetMetaData md = res.getMetaData();
                int column = md.getColumnCount();
                while(res.next()) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    for (int i = 1;i <= column; i++) {
                        data.put(md.getColumnName(i), res.getObject(i) == null ? "" : res.getObject(i).toString());
                    }
                    list.add(data);
                }
                return list;
            }
            updateRecordNumber = ps.executeUpdate();
            log.info(updateRecordNumber + "@" +p.toString());
            return updateRecordNumber;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException e) {
                    log.info(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info(e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }
    
    private static boolean isSelectStatement(String sql) {
        String select = sql.substring(0, 6);
        return "select".equalsIgnoreCase(select);
    }
    
    public static void main(String[] args) {
        try {
            System.out.println(Utils4JDBC.getMap("select icon_html title from dictionary_icon where (icon_html = ? and id = ?) or 1 = 1", new Object[]{null, null}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
