package com.irsa.cases.model;

/**
 *  请求类别类型
 */
public class RequestCategory {

    String irsa_request_category;
    String id;
    String createtime;
    String trash;

    String sequence;
    String title;

    public static String INSERT = "insert into irsa_request_category(id,createtime,sequence,title) values(?,?,?,?)";
    public static String DELETE = "update irsa_request_category set trash = '1' where id = ?";
    public static String UPDATE = "update irsa_request_category set title = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_request_category set sequence = ? where id = ?";

    private static String SELECT = "select id,title text from irsa_request_category where trash = '0' ";
    public static String SELECT_ALL = SELECT + "order by sequence";
    public static String SELECT_BY_ID = SELECT + "and id = ?";


}
