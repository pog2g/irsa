package com.irsa.cases.model;

/**
 * 案件步骤
 * @author EZ
 *
 */
public class CasesStep {
    /**正式*/
    public static String MODE_1 = "1";
    /**草稿*/
    public static String MODE_2 = "2";
    
    String irsa_cases_step;
    String id;
    String createtime;
    
    String account_id;
    String cases_id;
    String mode;//1=正式，2=草稿
    String state;//步骤，-1=立案时间，0=录入时间，其他值参考Cases的state字段
    String limit_day;//期限日期
    String text_1;//富文本；
    String text_2;//富文本；
    String text_3;//富文本；
    String text_4;//富文本；
    String text_5;//富文本；
    String text_6;//富文本；
    String text_7;//富文本；
    String radio_1;//单选；
    String first_personnel;//主承办人；
    String second_personnel;//辅承办人;
    String refine;
    
    public static String INSERT = "insert into irsa_cases_step(id,createtime,account_id,cases_id,mode,state," +
    		"limit_day,text_1,text_2,text_3,text_4,text_5,text_6,text_7,radio_1," +
    		"first_personnel,second_personnel,refine) " +
    		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_cases_step set limit_day = ?,text_1 = ?,text_2 = ?,text_3 = ?,text_4 = ?,text_5 = ?,text_6 = ?,text_7 = ?," +
    		"radio_1 = ?,first_personnel = ?,second_personnel = ?,refine = ? where id = ?";
    
    public static String DELETE_BY_MODE_CASESID = "delete from irsa_cases_step where mode = ? and cases_id = ?";
    public static String DELETE_BY_ID = "delete from irsa_cases_step where id = ?";
    
    public static String SELECT_EXIST_BY_ID = "select id from irsa_cases_step where id = ?";
    public static String SELECT_EXIST_BY_ACCOUNTID_CASESID_MODE_STATE = "select id from irsa_cases_step where account_id = ? and cases_id = ? and mode = ? and state = ?";
    
    public static String SELECT = "select cs.createtime,id,cs.account_id,cs.cases_id,cs.state,cs.refine," +
    		"cs.limit_day,cs.text_1,cs.text_2,cs.text_3,cs.text_4,cs.text_5,cs.text_6,cs.text_7,cs.radio_1,cs.first_personnel,cs.second_personnel," +
    		"date_format(cs.createtime,'%Y-%m-%d %H:%i:%s') create_time," +
    		"(select group_concat(csr.regulations_id) from irsa_cases_step_regulations csr where csr.res_id = cs.id) regulations," +
    		"(select aa.nickname from irsa_console_account aa where aa.id = cs.account_id) label_account," +
    		"(select (select (select d.title from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = aa.role_id) from irsa_console_account aa where aa.id = cs.account_id) label_department " +
    		"from irsa_cases_step cs ";
    public static String SELECT_BY_CASESID_MODE = SELECT + "where cs.cases_id = ? and mode = ? order by createtime,state";
    public static String SELECT_BY_MODE_ACCOUNTID_2 = SELECT + "where cs.mode = ? and cs.account_id = ? and cs.state < 12 order by createtime,state";
    public static String SELECT_BY_MODE_ACCOUNTID_3 = SELECT + "where cs.mode = ? and cs.account_id = ? and cs.state > 12 order by createtime,state";
    public static String SELECT_BY_ACCOUNTID_CASESID_MODE_STATE = SELECT + "where cs.account_id = ? and cs.cases_id = ? and cs.mode = ? and cs.state = ?";
}
