package com.irsa.cases.model;

/**
 * 标签
 * @author EZ
 *
 */
public class Label {
    String irsa_label;
    String id;
    String createtime;
    String title;
    
    public static String INSERT = "insert into irsa_label(id,createtime,title) values(?,?,?)";
    public static String SELECT_BY_TITLE = "select * from irsa_label where title = ?";
}
