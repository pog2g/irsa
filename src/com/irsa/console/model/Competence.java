package com.irsa.console.model;

/**
 * 权限（菜单）
 * @author EZ
 *
 */
public class Competence {
    /**未删除*/
    public static String TRASH_0 = "0";
    /**已删除*/
    public static String TRASH_1 = "1";
    /**可用*/
    public static String STATE_1 = "1";
    /**不可用*/
    public static String STATE_2 = "2";
    
    String irsa_console_competence;
    String id;//id
    String trash;//是否已删除，1=删除，0=未删除
    String createtime;//创建时间
    
    String sequence;//排序
    String state;//状态，1=可用，2=不可用
    String parent_id;
    String icon;
    String title;
    String url;
    String remark;
    
    public static String INSERT = "insert into irsa_console_competence(id,createtime,sequence,parent_id,icon,title,url,state,remark) values(?,?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_console_competence set parent_id = ?,icon = ?,title = ?,url = ?,state = ?,remark = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_console_competence set sequence = ? where id = ?";
    public static String UPDATE_MENU_1 = "update irsa_console_competence set title = ? where id = ?";
    public static String UPDATE_MENU_2 = "update irsa_console_competence set title = ?,state = ?,remark = ? where id = ?";
    public static String DELETE = "update irsa_console_competence set trash = '1' where id = ?";
    
    public static String SELECT_ALL = "select a.parent_id,a.parent_title,case a.parent_id when null then a.sequence when '' then a.sequence else a.parent_sequence end ps,"+ 
                            "a.icon,a.id,a.title,a.url,a.state,a.sequence,a.remark "+
                            "from ("+
                            "select c.parent_id,"+
                            "(select cc.title from irsa_console_competence cc where cc.id = c.parent_id) parent_title,"+
                            "(select cc.sequence from irsa_console_competence cc where cc.id = c.parent_id) parent_sequence,"+
                            "c.icon," +
                            "c.id,c.title,c.url,c.state,c.sequence,c.remark "+
                            "from irsa_console_competence c where c.trash = '0'"+
                            ") a order by ps,a.sequence";
    public static String SELECT_BY_ROLEID = "select a.parent_id,a.parent_title,case a.parent_id when null then a.sequence when '' then a.sequence else a.parent_sequence end ps,a.icon,a.id,a.title,a.url,a.state,a.sequence,a.remark from (select c.parent_id,(select cc.title from irsa_console_competence cc where cc.id = c.parent_id) parent_title,(select cc.sequence from irsa_console_competence cc where cc.id = c.parent_id) parent_sequence,c.icon,c.id,c.title,c.url,c.state,c.sequence,c.remark from irsa_console_competence c where c.trash = '0'" +
    		"and (" +
    		"c.id in (select rc.competence_id from irsa_console_role_competence rc where rc.role_id = ?)" +
    		"or " +
    		"c.id in (select DISTINCT cc.parent_id from irsa_console_role_competence rc,irsa_console_competence cc where rc.role_id = ? and rc.competence_id = cc.id)" +
    		")) a order by ps,a.sequence";
    
    public static String SELECT_EXIST = "select c.id,c.title text,c.remark,c.parent_id from irsa_console_competence c where c.trash = '0' ";
    public static String SELECT_EXIST_TITLE_4_PARENT = SELECT_EXIST + "and c.id != ? and (c.parent_id is null or c.parent_id = '') and c.title = ?";
    public static String SELECT_EXIST_TITLE_4_CHILD = SELECT_EXIST + "and c.id != ? and c.parent_id = ? and c.title = ?";
    public static String SELECT_EXIST_URL = SELECT_EXIST + "and c.id != ? and c.url = ?";
    public static String SELECT_EXIST_ID = SELECT_EXIST + "and c.id = ?";
    
    public static String SELECT_4_PARENT = SELECT_EXIST + "and (c.parent_id is null or c.parent_id = '') order by c.sequence";
    public static String SELECT_4_CHILD = SELECT_EXIST + "and parent_id = ? order by sequence";
    
    public static String SELECT_4_PARENT_BY_ROLEID = SELECT_EXIST + "and c.id in " +
    		"(select DISTINCT cc.parent_id from irsa_console_role_competence rc,irsa_console_competence cc where rc.role_id = ? and rc.competence_id = cc.id) " +
    		"order by sequence";
    public static String SELECT_4_CHILD_BY_ROLEID = SELECT_EXIST + "and c.id in " +
    		"(select rc.competence_id from irsa_console_role_competence rc where rc.role_id = ?) " +
    		"and parent_id = ? " +
    		"order by sequence";
}
