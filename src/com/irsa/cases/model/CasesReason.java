package com.irsa.cases.model;

/**
 * 案由
 * @author EZ
 */
public class CasesReason {
    String irsa_cases_reason;
    String id;
    String createtime;
    String trash;
    
    String sequence;
    String title;
    
    public static String INSERT = "insert into irsa_cases_reason(id,createtime,sequence,title) values(?,?,?,?)";
    public static String UPDATE = "update irsa_cases_reason set title = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_cases_reason set sequence = ? where id = ?";
    public static String DELETE = "update irsa_cases_reason set trash = '1' where id = ?";
    
    private static String SELECT = "select id,title text from irsa_cases_reason where trash = '0' ";
    public static String SELECT_ALL = SELECT + "order by sequence";
    public static String SELECT_BY_ID = SELECT + "and id = ?";
    public static String SELECT_BY_TITLE = SELECT + "and title = ?";
}
