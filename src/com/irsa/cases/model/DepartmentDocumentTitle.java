package com.irsa.cases.model;

public class DepartmentDocumentTitle {
    String irsa_department_document_title;
    String id;
    String createtime;
    
    String department_id;
    String title;
    
    public static String INSERT = "insert into irsa_department_document_title(id,createtime,department_id,title) values(?,?,?,?)";
    public static String UPDATE = "update irsa_department_document_title set title = ? where id = ?";
    
    public static String SELECT_BY_DEPARTMENTID = "select * from irsa_department_document_title where department_id = ?";
    
}
