package com.irsa.console.model;

/**
 * 角色权限
 * @author EZ
 *
 */
public class RoleCompetence {
    String irsa_console_role_competence;
    String id;//id
    String createtime;//创建时间
    
    String role_id;
    String competence_id;
    
    public static String INSERT = "insert into irsa_console_role_competence(id,createtime,role_id,competence_id) values(?,?,?,?)";
    public static String DELETE_BY_ROLEID = "delete from irsa_console_role_competence where role_id = ?";
    public static String DELETE_BY_COMPETENCEID = "delete from irsa_console_role_competence where competence_id = ?";
    
    public static String SELECT_BY_ROLEID = "select competence_id from irsa_console_role_competence where role_id = ?";
    
    public static String SELECT_TREE = "select c.id,c.title text,c.url,c.icon," +
    		"p.id pid,p.title parent,p.icon parent_icon " +
            "from irsa_console_role_competence rc,irsa_console_competence c,irsa_console_competence p " +
            "where rc.competence_id = c.id and c.trash = '0' and p.id = c.parent_id ";
    public static String SELECT_TREE_BY_ROLEID = SELECT_TREE + " and rc.role_id = ? order by p.sequence,c.sequence";
    public static String SELECT_TREE_BY_ROLEID_STATE = SELECT_TREE + " and rc.role_id = ? and c.state = ? order by p.sequence,c.sequence";
}
