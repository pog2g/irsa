package com.irsa.cases.model;

/**
 * 立案草稿
 * @author EZ
 *
 */
public class CasesTemp {
    String irsa_cases_temp;
    String id;
    String createtime;
    String trash;

    String type;//案件主体类型
    String year_number;//案件年号    
    String number;//案件编号
    String administrative_category_id;//行政类别
    String reason_id;//案由
    String reason_another;//案由其他备注
    String specific_behavior;//被拒具体行政行为
    String apply_request;//行政复议请求
    String facts_reasons;//事实和理由
    String apply_mode;//申请方式，1=默认，2=邮寄，3=其他
    String apply_mode_another;//申请方式其他备注
    String apply_time;//立案时间，示例：2018-10-01
    String labels;//关键字标签
    String entry_account_id;//录入人
    
    public static String INSERT = "insert into irsa_cases_temp(createtime,id,type,year_number,number,administrative_category_id,reason_id,reason_another,specific_behavior,apply_request,facts_reasons," +
    		"apply_mode,apply_mode_another,apply_time,labels,entry_account_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_cases_temp set year_number = ?,number = ?,administrative_category_id = ?,reason_id = ?,reason_another = ?,specific_behavior = ?,apply_request = ?,facts_reasons = ?," +
            "apply_mode = ?,apply_mode_another = ?,apply_time = ?,labels = ? where id = ?";
    public static String DELETE = "update irsa_cases_temp set trash = '1' where id = ?";
    
    public static String SELECT_EXIST_BY_ID = "select id from irsa_cases_temp where trash = '0' and id = ?";
    
    public static String SELECT = "select '1' state,c.id,c.type,c.year_number year_no,c.number no,c.administrative_category_id,c.reason_id,c.reason_another,c.specific_behavior,c.apply_request,c.facts_reasons," +
    		"c.apply_mode,c.apply_mode_another,c.labels," +
    		"case c.type when '1' then '个人' when '2' then '多人' when '3' then '法人组织' else '其他申请' end label_type," +
    		"(select CONCAT(p.title,'-',r.title) from irsa_administrative_category r,irsa_administrative_category p where p.id = r.parent_id and r.id = c.administrative_category_id) label_administrative_category," +
    		"case c.reason_id when '-2' then reason_another else (select r.title from irsa_cases_reason r where r.id = c.reason_id) end label_reason," +
    		"(select GROUP_CONCAT(pp.name separator '，') from irsa_party pp,irsa_cases_personnel_temp p where pp.id = p.personnel_id and p.cases_id = c.id and p.type = '1') apply," +
    		"(select GROUP_CONCAT(pp.name separator '，') from irsa_party pp,irsa_cases_personnel_temp p where pp.id = p.personnel_id and p.cases_id = c.id and p.type = '7') apply_third_party," +
    		"(select GROUP_CONCAT(pp.name separator '，') from irsa_party pp,irsa_cases_personnel_temp p where pp.id = p.personnel_id and p.cases_id = c.id and p.type = '3') third_party," +
    		"(select GROUP_CONCAT(d.name separator '，') from irsa_defendant d,irsa_cases_personnel_temp p where d.id = p.personnel_id and p.cases_id = c.id and p.type = '2') defendant," +
    		"date_format(c.apply_time,'%Y-%m-%d') apply_time,date_format(c.createtime,'%Y-%m-%d %H:%i:%s') create_time " +
    		"from irsa_cases_temp c where c.trash = '0' ";
    public static String SELECT_BY_ID = SELECT + "and c.id = ?";
    public static String SELECT_BY_ENTRYACCOUNTID = SELECT + "and c.entry_account_id = ? order by createtime desc";
    public static String SELECT_SWITCH_STATE_BY_ID="select * from irsa_temp_file where mode='13' and res_id=?";
}
