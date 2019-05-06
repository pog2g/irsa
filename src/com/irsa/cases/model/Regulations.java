package com.irsa.cases.model;

public class Regulations {
    String irsa_regulations;
    String id;
    String createtime;
    String trash;
    
    String type_id;//类型id
    String department;//制定部门
    String publish_number;//颁布文号
    String publish_time;//颁布日期
    String execute_time;//实行日期
    String title;//标题
    String content;//内容
    
    public static String INSERT = "insert into irsa_regulations(id,createtime,type_id,department,publish_number,publish_time,execute_time,title,content) " +
    		"values(?,?,?,?,?,?,?,?,?)"; 
    public static String UPDATE = "update irsa_regulations set type_id = ?,department = ?,publish_number = ?,publish_time = ?,execute_time = ?,title = ?,content = ? where id = ?";
    public static String DELETE = "update irsa_regulations set trash = '1' where id = ?";
    
    private static String SELECT_EXIST = "select id from irsa_regulations where trash = '0' ";
    public static String SELECT_EXIST_BY_TITLE = SELECT_EXIST + "and id != ? and title = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT = "select r.id,r.type_id,r.department,r.publish_number,r.title,r.content," +
    		"date_format(r.publish_time, '%Y-%m-%d') publish_time,date_format(r.execute_time, '%Y-%m-%d') execute_time," +
    		"(select t.title from irsa_regulations_type t where t.id = r.type_id) label_type from irsa_regulations r where r.trash = '0' ";
    public static String SELECT_BY_ID = SELECT + "and id = ?";
}
