package com.irsa.cases.model;

/**
 * 节假日
 * @author EZ
 *
 */
public class Holiday {
    String irsa_holiday;
    String id;
    String createtime;
    
    String year;
    String title;
    
    public static String INSERT = "insert into irsa_holiday(id, createtime, year, title) values(?,?,?,?)";
    public static String UPDATE = "update irsa_holiday set year = ?, title = ? where id = ?";
    public static String DELETE = "delete from irsa_holiday where id = ?";
    
    public static String SELECT_EXIST = "select id,title from irsa_holiday ";
    public static String SELECT_EXIST_BY_YEAR_TITLE = SELECT_EXIST + "where id != ? and year = ? and title = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "where id = ?";
    
    public static String SELECT = "select h.id,h.year,h.title," +
    		"(select group_concat(date_format(d.date, '%Y-%m-%d') order by date) from irsa_holiday_date d where d.type = '1' and d.holiday_id = h.id) rest_date," +
    		"(select group_concat(date_format(d.date, '%Y-%m-%d') order by date) from irsa_holiday_date d where d.type = '2' and d.holiday_id = h.id) work_date " +
    		" from irsa_holiday h ";
    public static String SELECT_ALL = SELECT + "order by h.year,h.title";
    public static String SELECT_BY_ID = SELECT + "where h.id = ?";
}
