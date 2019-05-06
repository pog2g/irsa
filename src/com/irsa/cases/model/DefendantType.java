package com.irsa.cases.model;

/**
 * 被申请人类型
 * @author EZ
 */
public class DefendantType {
    String irsa_defendant_type;
    String id;
    String createtime;
    String trash;
    
    String sequence;
    String title;
    
    public static String INSERT = "insert into irsa_defendant_type(id,createtime,sequence,title) values(?,?,?,?)";
    public static String UPDATE = "update irsa_defendant_type set title = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_defendant_type set sequence = ? where id = ?";
    public static String DELETE = "update irsa_defendant_type set trash = '1' where id = ?";
    
    private static String SELECT = "select id,title text from irsa_defendant_type where trash = '0' ";
    public static String SELECT_ALL = SELECT + "order by sequence";
    public static String SELECT_BY_ID = SELECT + "and id = ?";
}
