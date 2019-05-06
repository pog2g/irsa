package com.irsa.cases.model;

/**
 * 证件类型
 * @author EZ
 */
public class IdType {
    String irsa_id_type;
    String id;
    String createtime;
    String trash;
    
    String sequence;
    String title;
    
    public static String INSERT = "insert into irsa_id_type(id,createtime,sequence,title) values(?,?,?,?)";
    public static String UPDATE = "update irsa_id_type set title = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_id_type set sequence = ? where id = ?";
    public static String DELETE = "update irsa_id_type set trash = '1' where id = ?";
    
    private static String SELECT = "select id,title text from irsa_id_type where trash = '0' ";
    public static String SELECT_ALL = SELECT + "order by sequence";
    public static String SELECT_BY_ID = SELECT + "and id = ?";
}
