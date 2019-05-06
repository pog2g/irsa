package com.irsa.cases.model;

/**
 * 行政类别
 * @author EZ
 *
 */
public class AdministrativeCategory {
    String irsa_administrative_category;
    String id;//id
    String trash;//是否已删除，1=删除，0=未删除
    String createtime;//创建时间
    
    String sequence;//排序
    String parent_id;
    String title;
    
    public static String INSERT = "insert into irsa_administrative_category(id,createtime,sequence,parent_id,title) values(?,?,?,?,?)";
    public static String UPDATE = "update irsa_administrative_category set parent_id = ?,title = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_administrative_category set sequence = ? where id = ?";
    public static String DELETE = "update irsa_administrative_category set trash = '1' where id = ?";
    
    public static String SELECT_ALL = "select a.parent_id,a.parent_title,case a.parent_id when null then a.sequence when '' then a.sequence else a.parent_sequence end parent_sequence,"+ 
                            "a.id,a.title,a.sequence "+
                            "from ("+
                            "select c.parent_id,"+
                            "(select cc.title from irsa_administrative_category cc where cc.id = c.parent_id) parent_title,"+
                            "(select cc.sequence from irsa_administrative_category cc where cc.id = c.parent_id) parent_sequence,"+
                            "c.id,c.title,c.sequence "+
                            "from irsa_administrative_category c where c.trash = '0'"+
                            ") a order by parent_sequence,a.sequence";
    
    public static String SELECT_EXIST = "select id,title text from irsa_administrative_category where trash = '0' ";
    public static String SELECT_EXIST_TITLE_4_PARENT = SELECT_EXIST + "and id != ? and (parent_id is null or parent_id = '') and title = ?";
    public static String SELECT_EXIST_TITLE_4_CHILD = SELECT_EXIST + "and id != ? and parent_id = ? and title = ?";
    public static String SELECT_EXIST_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT_4_CHOOSE_PARENT = SELECT_EXIST + "and (parent_id is null or parent_id = '') order by sequence";
    public static String SELECT_4_CHOOSE = "select c.id,CONCAT(p.title,'-',c.title) text " +
    		"from irsa_administrative_category c, irsa_administrative_category p " +
    		"where c.parent_id is not null and c.parent_id = p.id " +
    		"order by p.sequence,c.sequence";
}
