package com.irsa.cases.model;

/**
 *  请求类别类型
 */
public class CasesRequestCategory {

    String id;
    String cases_id;
    String request_category_id;
    String createtime;

    public static String INSERT = "insert into irsa_cases_request_category(id,cases_id,request_category_id,createtime) values(?,?,?,?)";
    public static String DELETE = "delete from irsa_cases_request_category where cases_id = ?";
    public static String SELECT = "select request_category_id from irsa_cases_request_category where cases_id = ? ";


}
