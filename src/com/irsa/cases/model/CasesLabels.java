package com.irsa.cases.model;

/**
 * 案件标签
 * @author EZ
 *
 */
public class CasesLabels {
    String irsa_cases_labels;
    String id;
    String createtime;
    
    String cases_id;
    String label_id;
    
    public static String INSERT = "insert into irsa_cases_labels(id,createtime,cases_id,label_id) values(?,?,?,?)";
    public static String DELETE = "delete from irsa_cases_labels where cases_id = ?";
}
