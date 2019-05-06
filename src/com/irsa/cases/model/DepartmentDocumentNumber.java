package com.irsa.cases.model;

public class DepartmentDocumentNumber {
    String irsa_department_document_number;
    String id;
    String createtime;
    
    String department_id;
    String state;
    String number_1;
    String number_2;
    String number_3;
    String html;
    
    public static String INSERT = "insert into irsa_department_document_number(id,createtime,department_id,state,number_1,number_2,number_3,html) " +
    		"values(?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_department_document_number set number_1 = ?,number_2 = ?,number_3 = ? where id = ?";
    
    public static String UPDATE_HTML = "update irsa_department_document_number set html = ? where id = ?";
    
    public static String SELECT = "select n.id,n.department_id,n.state,n.number_1,n.number_2,n.number_3,n.html," +
    		"(select t.title from irsa_department_document_title t where t.department_id = n.department_id) document_title " +
    		"from irsa_department_document_number n ";
    public static String SELECT_BY_DEPARTMENTID = SELECT + "where n.department_id = ? order by state";
}
