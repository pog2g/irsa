package com.irsa.cases.model;

/**
 * 行政部门
 * @author EZ
 *
 */
public class Department {
    String irsa_department;
    String id;
    String createtime;
    String trash;
    
    String sequence;
    String title;
    
    public static String INSERT = "insert into irsa_department(id,createtime,sequence,title) values(?,?,?,?)";
    public static String UPDATE = "update irsa_department set title = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_department set sequence = ? where id = ?";
    public static String DELETE = "update irsa_department set trash = '1' where id = ?";
    
    private static String SELECT = "select id,title text from irsa_department where trash = '0' ";
    public static String SELECT_ALL = SELECT + "order by sequence";
    public static String SELECT_BY_ID = SELECT + "and id = ?";
    public static String SELECT_BY_TITLE = SELECT + "and id != ? and title = ?";
}
