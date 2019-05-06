package com.irsa.console.model;

/**
 * 用户
 * @author EZ
 *
 */
public class Account {
    /**未删除*/
    public static String trash_0 = "0";
    /**已删除*/
    public static String trash_1 = "1";
    /**男*/
    public static String GENDER_1 = "1";
    /**女*/
    public static String GENDER_2 = "2";
    /**保密*/
    public static String GENDER_3 = "3";
    
    String irsa_console_account;
    String id;//id
    String trash;//0=未删除，1=已删除
    String createtime;//创建时间
    
    String account;//账户名
    String nickname;//昵称
    String gender;//性别，1=男，2=女，3=保密
    String phone;//联系方式
    String id_number;//证件号
    String password;//密码
    String role_id;//角色
    
    public static String INSERT = "insert into irsa_console_account(id,createtime,account,nickname,gender,phone,id_number,password,role_id) " +
    		"values(?,?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_console_account set account = ?,nickname = ?,gender = ?,phone = ?,id_number = ?,password = ?,role_id = ? where id = ?";
    public static String DELETE = "update irsa_console_account set trash = '1' where id = ?";
    
    private static String SELECT_EXIST = "select id,role_id from irsa_console_account where trash = '0' ";
    public static String SELECT_EXIST_ACCOUNT = "select a.id from irsa_console_account a,irsa_console_role r where a.role_id = r.id and r.department_id = ? and a.id != ? and a.account = ?";
    public static String SELECT_EXIST_ID = SELECT_EXIST + "and id = ?";
    public static String SELECT_EXIST_ROLEID = SELECT_EXIST + "and role_id = ?";
    
//    private static String SELECT = "select aa.role_id,aa.id,aa.account,aa.nickname,aa.gender,aa.phone,aa.id_number," +
//    		"aa.label_gender,aa.role_name,aa.department_id,aa.department_name,aa.last_ip,aa.last_time from (" +
//    		"select a.createtime,a.role_id,a.id,a.account,a.nickname,a.gender,a.phone,a.id_number,a.password," +
//    		"case a.gender when '1' then '男' when '2' then '女' else '保密' end label_gender," +
//    		"(select r.title from irsa_console_role r where r.id = a.role_id) role_name," +
//    		"(select r.department_id from irsa_console_role r where r.id = a.role_id) department_id," +
//    		"(select (select d.title from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) department_name," +
//            "(select l.ip from irsa_console_account_log l where l.account_id = a.id order by l.createtime desc limit 1,1) last_ip," +
//            "(select date_format(l.createtime,'%Y-%m-%d %H:%i:%s') from irsa_console_account_log l where l.account_id = a.id order by l.createtime desc limit 1,1) last_time " +
//    		"from irsa_console_account a where a.trash = '0') aa ";
    
    private static String SELECT = "select a.createtime,a.role_id,a.id,a.account,a.nickname,a.gender,a.phone,a.id_number,a.password," +
    		"case a.gender when '1' then '男' when '2' then '女' else '保密' end label_gender, " +
    		"r.title role_name,r.department_id,d.title department_name," +
    		"(select l.ip from irsa_console_account_log l where l.account_id = a.id order by l.createtime desc limit 1) last_ip," +
    		"(select date_format(l.createtime,'%Y-%m-%d %H:%i:%s') from irsa_console_account_log l where l.account_id = a.id order by l.createtime desc limit 1) last_time " +
    		"from irsa_console_account a,irsa_console_role r,irsa_department d " +
    		"where a.role_id = r.id and r.department_id = d.id ";
    
    public static String SELECT_ALL = SELECT + "order by a.createtime desc";
    public static String SELECT_BY_DEOARTMENTID = SELECT + "and d.id = ? order by a.createtime desc";
    public static String SELECT_BY_ACCOUNT_PWD = SELECT + "and d.id = ? and a.account = ? and a.password = ?";
    public static String SELECT_BY_ID = SELECT + "and a.id = ?";
    
    public static String SELECT_CHOOSE_BY_DEOARTMENTID = "select a.id,a.nickname text " +
    		"from irsa_console_account a,irsa_console_role r " +
    		"where a.trash = '0' and a.role_id = r.id and r.department_id = ? and r.title like '%承办人%' order by a.account";
}
