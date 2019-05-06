package com.irsa.cases.model;

/**
 * 案件相关人员-临时
 * @author EZ
 *
 */
public class CasesPersonnelTemp {
    String irsa_cases_personnel_temp;
    String id;
    String createtime;
    
    String type;// 参考CasesPersonnel的personnel_type字段
    String cases_id;
    String personnel_id;
    String client_id;// 委托人：
    String representative; // 是否为申请人代表，0=否，1=是
    
    public static String INSERT = "insert into irsa_cases_personnel_temp(id,createtime,type,cases_id,personnel_id,client_id) values(?,?,?,?,?,?)";
    public static String UPDATE_REPRESENTATIVE = "update irsa_cases_personnel_temp set representative = ? where id = ?";
    
    public static String SELECT_EXIST = "select id,type,personnel_id,client_id,representative from irsa_cases_personnel_temp ";
    public static String SELECT_EXIST_BY_CASESID_PERSONNELID = SELECT_EXIST + "where cases_id = ? and personnel_id = ?";
    public static String SELECT_EXIST_BY_CASESID_TYPE = SELECT_EXIST + "where cases_id = ? and type = ? order by createtime";
    public static String SELECT_EXIST_BY_CASESID_REPRESENTATIVE = SELECT_EXIST + "where cases_id = ? and type = ? and representative = ? order by createtime,personnel_id";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "where id = ?";
    public static String SELECT_EXIST_BY_CASESID_PERSONNELID_CLIENTID = SELECT_EXIST + "where cases_id = ? and personnel_id = ? and client_id = ?";
    
    public static String DELETE_BY_ID = "delete from irsa_cases_personnel_temp where id = ?";
    public static String DELETE_BY_TYPE = "delete from irsa_cases_personnel_temp where cases_id = ? and type = ?";
    
    public static String SELECT_CHOOSE_BY_CASESID_TYPE_1 = "select id," +
    		"(select p.name from irsa_party p where p.id = t.personnel_id) text " +
    		"from irsa_cases_personnel_temp t where cases_id = ? and type = '1' order by createtime";
    
   /* public static String SELECT_PARYT_BY_CASESID = "select t.id,t.type personnel_type,t.personnel_id,t.representative," +
            "p.type,p.name,p.legal_person,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.phone,p.contact,p.zip_code,p.county_id,p.address,p.id_type_id,p.id_no,p.unit_id_type_id,p.unit_id_no,p.unit_contact,p.domicile," +
            "case p.type when '1' then '个人' else '法人组织' end label_type," +
            "case p.gender when '1' then '男' else '女' end label_gender,p.nature," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select i.title from irsa_unit_id_type i where i.id = p.unit_id_type_id) label_unit_id_type," +
            "(select (select (select rrr.title from irsa_region rrr where rrr.id = rr.parent_id) from irsa_region rr where rr.id = r.parent_id) from irsa_region r where r.id = p.county_id) province," +
            "(select (select rr.parent_id from irsa_region rr where rr.id = r.parent_id) from irsa_region r where r.id = p.county_id) province_id," +
            "(select (select rr.title from irsa_region rr where rr.id = r.parent_id) from irsa_region r where r.id = p.county_id) city," +
            "(select r.parent_id from irsa_region r where r.id = p.county_id) city_id," +
            "(select r.title from irsa_region r where r.id = p.county_id) county," +
            "(select f.id from irsa_party_file f where f.res_id = p.id) id_file_id," +
            "(select f.type from irsa_party_file f where f.res_id = p.id) id_file_type," +
            "(select f.ext from irsa_party_file f where f.res_id = p.id) id_file_ext," +
            "(select f.real_name from irsa_party_file f where f.res_id = p.id) id_file_name," +
            "(select f.file_name from irsa_party_file f where f.res_id = p.id) id_file " +
    		"from irsa_cases_personnel_temp t,irsa_party p where p.trash = '0' and t.personnel_id = p.id ";*/

    public static String SELECT_PARYT_BY_CASESID = "select t.id,t.type personnel_type,t.personnel_id,t.representative," +
            "p.type,p.name,p.other_name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no,p.phone,p.domicile,p.zip_code,p.contact,p.abode," +
            "p.unit_name,p.unit_contact,p.unit_id_type_id,p.unit_id_no,p.unit_abode,p.legal_person_type," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select i.title from irsa_unit_id_type i where i.id = p.unit_id_type_id) label_unit_id_type," +
            "(select f.id from irsa_party_file f where f.res_id = p.id) id_file_id," +
            "(select f.type from irsa_party_file f where f.res_id = p.id) id_file_type," +
            "(select f.ext from irsa_party_file f where f.res_id = p.id) id_file_ext," +
            "(select f.real_name from irsa_party_file f where f.res_id = p.id) id_file_name," +
            "(select f.file_name from irsa_party_file f where f.res_id = p.id) id_file " +
            "from irsa_cases_personnel_temp t,irsa_party p where p.trash = '0' and t.personnel_id = p.id ";
    public static String SELECT_BY_CASESID_TYPE_137 = SELECT_PARYT_BY_CASESID + "and t.cases_id = ? and t.type = ? order by t.createtime desc limit 0,1";
    public static String SELECT_BY_CASESID_REPRESENTATIVE = SELECT_PARYT_BY_CASESID + 
            "and t.cases_id = ? and t.type = ? and t.representative = ? order by t.createtime,t.personnel_id";
    
    public static String SELECT_BY_CASESID_TYPE_2 = "select t.id,t.type personnel_type,t.personnel_id," +
            "p.type_id type,p.name,p.legal_person_type,p.unit_name,p.unit_abode,p.unit_contact,p.duty," +
            "(select t.title from irsa_defendant_type t where t.id = p.type_id) label_type," +
            "(select f.id from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_id_1," +
            "(select f.type from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_type_1," +
            "(select f.ext from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_ext_1," +
            "(select f.real_name from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_name_1," +
            "(select f.file_name from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_1," +
            "(select f.id from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_id_2," +
            "(select f.type from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_type_2," +
            "(select f.ext from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_ext_2," +
            "(select f.real_name from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_name_2," +
            "(select f.file_name from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_2," +
            "(select f.id from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_id_3," +
            "(select f.type from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_type_3," +
            "(select f.ext from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_ext_3," +
            "(select f.real_name from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_name_3," +
            "(select f.file_name from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_3 " +
            "from irsa_cases_personnel_temp t,irsa_defendant p where p.trash = '0' and t.personnel_id = p.id " +
            "and t.cases_id = ? and t.type = '2' order by t.createtime";


    
    public static String SELECT_BY_CASESID_CLIENTID = "select t.id,t.type personnel_type,t.personnel_id," +
            "p.name,p.unit_name,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.phone,p.domicile,p.id_type_id,p.id_no,p.identity," +
            "case p.gender when '1' then '男' else '女' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type, " +
            "(select f.real_name from irsa_temp_file f where f.personnel_id = t.id) agent_file_name," +
            "(select f.file_name from irsa_temp_file f where f.personnel_id = t.id) agent_file " +
            "from irsa_cases_personnel_temp t,irsa_agent p where p.trash = '0' and t.personnel_id = p.id " +
            "and t.cases_id = ? and t.client_id = ? order by t.createtime desc limit 0,1";

}
