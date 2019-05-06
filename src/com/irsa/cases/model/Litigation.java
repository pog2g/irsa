package com.irsa.cases.model;

/**
 * 诉讼案件
 * @author EZ
 *
 */
public class Litigation {
    String irsa_litigation;
    String id;
    String createtime;
    String trash;
    
    String year;//年份
    String reason_id;//案由
    String court;//审理法院
    String mode;//审理程序，1=一审，2=二审，3=再审
    String type;//文书性质，1=决定，2=判决，3=裁定，4=调解，5=通知
    String title;
    String content;
    
    public static String INSERT = "insert into irsa_litigation(id,createtime,year,reason_id,court,mode,type,title,content) " +
    		"values(?,?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_litigation set year = ?,reason_id = ?,court = ?,mode = ?,type = ?,title = ?,content = ? where id = ?";
    public static String DELETE = "update irsa_litigation set trash = '1' where id = ?";
    
    public static String SELECT_EXIST = "select id from irsa_litigation where trash = '0' ";
    public static String SELECT_EXIST_BY_YEAR_TITLE = SELECT_EXIST + "and id != ? and year = ? and title = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT = "select * from (" +
    		"select a.trash,a.id,a.year,a.reason_id,a.court,a.mode,a.type,a.title,a.content," +
    		"(select r.title from irsa_cases_reason r where r.id = a.reason_id) label_reason," +
    		"case a.mode when '1' then '一审' when '2' then '二审' else '再审' end label_mode," +
    		"case a.type when '1' then '决定' when '2' then '判决' when '3' then '裁定' when '4' then '调解' else '通知' end label_type " +
    		"from irsa_litigation a) l where l.trash = '0' ";
    public static String SELECT_BY_ID = SELECT + " and id = ?";
}
