package com.irsa.cases.model;

/**
 * 节假日日期
 * 日期按天统计是为了适应补班的时间不连续，参考2013年十一假期
 * @author EZ
 *
 */
public class HolidayDate {
    String irsa_holiday_date;
    String id;
    String createtime;
    
    String holiday_id;
    String type;//1=休息日，2=补班日
    String date;
    
    public static String INSERT = "insert into irsa_holiday_date(id,createtime,holiday_id,type,date) values(?,?,?,?,?)";
    public static String UPDATE = "update irsa_holiday_date set type = ?,date = ? where id = ?";
    public static String DELETE = "delete from irsa_holiday_date where id = ?";
    
    public static String SELECT = "select id,type,date_format(date,'%Y-%m-%d') label_date," +
    		"case type when '1' then '休息日' else '补班日' end label_type from irsa_holiday_date ";
    public static String SELECT_BY_HOLIDAYID = SELECT + "where holiday_id = ? order by type,date";
    
    public static String SELECT_EXIST = "select id from irsa_holiday_date ";
    public static String SELECT_EXIST_BY_DATE = SELECT_EXIST + "where id != ? and date = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "where id = ?";
    
    public static String SELECT_BY_YEAR_TYPE = "select date_format(hd.date,'%Y-%m-%d') date_time " +
    		"from irsa_holiday h,irsa_holiday_date hd " +
    		"where h.year = ? and hd.holiday_id = h.id and hd.type = ? order by hd.date";
}
