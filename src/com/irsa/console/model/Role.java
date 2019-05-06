package com.irsa.console.model;

/**
 * 角色
 * @author EZ
 *
 */
public class Role {
    String irsa_console_role;
    String id;//id
    String trash;//删除（未删除，已删除）
    /**未删除*/
    public static String trash_0 = "0";
    /**已删除*/
    public static String trash_1 = "1";
    String createtime;//创建时间
    
    String department_id;
    String title;
    String remark;
    
    public static String INSERT = "insert into irsa_console_role(id,createtime,department_id,title,remark) value(?,?,?,?,?)";
    public static String UPDATE = "update irsa_console_role set department_id = ?,title = ?,remark = ? where id = ?";
    public static String DELETE = "update irsa_console_role set trash = '1' where id = ?";
    
    public static String SELECT = "select r.id,r.department_id,r.title,r.remark," +
    		"(select d.title from irsa_department d where d.id = r.department_id) department_name," +
    		"(select count(*) from irsa_console_role_competence rc where rc.role_id = r.id) competences " +
    		"from irsa_console_role r where r.trash = '0' ";
    public static String SELECT_EXIST_ID = "select id,department_id from irsa_console_role where trash = '0' and id = ?";
    public static String SELECT_EXIST_DEPARTMENTID_TITLE = "select id from irsa_console_role where trash = '0' and id != ? and department_id = ? and title = ?";
    
    public static String SELECT_4_CHOOSE = "select r.id,r.title text,(select count(*) from irsa_console_role_competence rc where rc.role_id = r.id) competences " +
    		"from irsa_console_role r where r.trash = '0' and r.department_id = ? order by competences desc,r.title";
}
