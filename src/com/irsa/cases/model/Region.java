package com.irsa.cases.model;

/**
 * 行政区划
 * @author EZ
 *
 */
public class Region {
    String irsa_region;
    String id;//id
    String trash;//删除（未删除，已删除）
    /**未删除*/
    public static String TRASH_0 = "0";
    /**已删除*/
    public static String TRASH_1 = "1";
    String createtime;//创建时间
    
    String sequence;//排序
    String parent_id;
    String grade;//等级，1=省，2=市，3=区
    /**省*/
    public static String GRADE_1 = "1";
    /**市*/
    public static String GRADE_2 = "2";
    /**区*/
    public static String GRADE_3 = "3";
    String title;
    String province;
    String city;
    
    public static String INSERT_1 = "insert into irsa_region(id,createtime,sequence,grade,title,province) values(?,?,?,'1',?,?)";
    public static String INSERT_23 = "insert into irsa_region(id,createtime,sequence,grade,parent_id,title,province,city) values(?,?,?,?,?,?,?,?)";
    public static String UPDATE_1 = "update irsa_region set title = ? where id = ?";
    public static String UPDATE_2 = "update irsa_region set parent_id = ?,title = ?,province = ? where id = ?";
    public static String UPDATE_3 = "update irsa_region set parent_id = ?,title = ?,province = ?,city = ? where id = ?";
    public static String UPDATE_SEQUENCE = "update irsa_region set sequence = ? where id = ?";
    public static String DELETE = "update irsa_region set trash = '1' where id = ?";
    
    public static String SELECT = "select r.id,r.grade,r.title," +
    		"(select p.sequence from irsa_region p where p.id = r.province) province_seq," +
    		"(select p.sequence from irsa_region p where p.id = r.city) city_seq," +
    		"r.parent_id pid,(select p.title from irsa_region p where p.id = r.parent_id) ptitle," +
    		"(select p.parent_id from irsa_region p where p.id = r.parent_id) ppid," +
    		"(select (select pp.title from irsa_region pp where pp.id = p.parent_id ) from irsa_region p where p.id = r.parent_id) pptitle " +
    		"from irsa_region r where trash = '0' ";
    
    public static String SELECT_EXIST = "select id,title text from irsa_region where trash = '0' ";
    public static String SELECT_EXIST_ID = SELECT_EXIST + "and id = ?";
    public static String SELECT_EXIST_TITLE_4_PARENT = SELECT_EXIST + "and grade = '1' and id != ? and title = ?";
    public static String SELECT_EXIST_TITLE_4_CHILD = SELECT_EXIST + "and id != ? and title = ? and parent_id = ?";
    
    public static String SELECT_4_PARENT = SELECT_EXIST + "and grade = '1' order by sequence";
    public static String SELECT_BY_PARENTID = SELECT_EXIST + "and parent_id = ? order by sequence";
    
    
    
    
    
    
    public static String SELECT_COUNT_4_PARENT = "select id from gox_match_region where trash = '0' and (parent_id is null or parent_id = '')";
    public static String SELECT_COUNT_4_CHILD = "select id from gox_match_region where trash = '0' and parent_id = ?";
    
    public static String APP_BY_PNAME = "select id from gox_match_region where trash = '0' and (parent_id is null or parent_id = '') and title = ?";
    public static String APP_BY_PID_NAME = "select id from gox_match_region where trash = '0' and parent_id = ? and title = ?";
}
