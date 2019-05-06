package com.irsa.cases.model;

public class CasesRemark {
    String irsa_cases_remark;
    String id;
    String createtime;
    String trash;//0=未删除，1=已删除
    
    String cases_id;
    String account_id;
    String remark;
    
    public static String INSERT = "insert into irsa_cases_remark(id,createtime,cases_id,account_id,remark) values(?,?,?,?,?)";
    public static String UPDATE = "update irsa_cases_remark set remark = ? where id = ?";
    public static String DELETE = "update irsa_cases_remark set trash = '1' where id = ?";
    
    public static String SELECT_EXIST_ID = "select id from irsa_cases_remark where trash = '0' and id = ?";
    
    public static String SELECT = "select r.id,r.cases_id,r.account_id,r.remark,date_format(r.createtime, '%Y-%m-%d %H:%i:%s') create_time," +
    		"(select a.nickname from irsa_console_account a where a.id = r.account_id) label_account," +
    		"(select (select (select d.title from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = r.account_id) label_department " +
    		"from irsa_cases_remark r where r.trash = '0' ";
    public static String SELECT_BY_CASESID = SELECT + "and r.cases_id = ? order by createtime";
}
