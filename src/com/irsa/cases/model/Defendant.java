package com.irsa.cases.model;

/**
 * 被申请人
 * @author EZ
 *
 */
public class Defendant {
    String irsa_defendant;
    String id;
    String createtime;
    String trash;
    
    String type_id;
    String unit_name;
    String unit_abode;
    String unit_contact;

    String name;
    String account;
    String pwd;
    String legal_person_type;//法人类型
    String duty;//职位

    String phone;//待用字段


    //保存被申请人
    public static String INSERT = "insert into irsa_defendant(id,createtime,type_id,unit_name,unit_contact,unit_abode,name,legal_person_type,duty,account,pwd) values(?,?,?,?,?,?,?,?,?,?,?)";
    
    /*public static String INSERT = "insert into irsa_defendant(id,createtime,type_id,name,address,legal_person,legalPersonType,job,account,pwd) values(?,?,?,?,?,?,?,?,?,?)";*/

    public static String UPDATE = "update irsa_defendant set type_id = ?, unit_name = ?, unit_contact = ?, unit_abode = ?, name = ?, legal_person_type = ?, duty = ? where id = ?";
    /*public static String UPDATE = "update irsa_defendant set type_id = ?,name = ?,address = ?,legal_person = ?,legalPersonType = ?,job = ? where id = ?";*/

    public static String UPDATE_1 = "update irsa_defendant set type_id = ?,unit_name = ?, unit_contact = ?, unit_abode = ?, name = ?, account = ? where id = ?";
    /*public static String UPDATE_1 = "update irsa_defendant set type_id = ?,name = ?,address = ?,legal_person = ?,account = ? where id = ?";*/


    public static String UPDATE_2 = "update irsa_defendant set type_id = ?,unit_name = ?, unit_contact = ?, unit_abode = ?, name = ?, legal_person_type = ?, account = ?,pwd = ? where id = ?";
    /*public static String UPDATE_2 = "update irsa_defendant set type_id = ?,name = ?,address = ?,legal_person = ?,account = ?,pwd = ? where id = ?";*/

    public static String UPDATE_3 = "update irsa_defendant set unit_name = ?, unit_contact = ?, unit_abode = ?, name = ?, legal_person_type = ?, account = ?,pwd = ? where id = ?";
    /*public static String UPDATE_3 = "update irsa_defendant set name = ?,address = ?,legal_person = ?,account = ?,pwd = ? where id = ?";*/
    
    public static String SELECT = "select date_format(p.createtime,'%Y-%m-%d %H:%i:%s') createtime," +
    		"p.id,p.type_id type,p.unit_name,p.unit_abode,p.unit_contact,p.name,p.legal_person_type,p.duty,p.account," +
    		"(select t.title from irsa_defendant_type t where t.id = p.type_id) label_type," +
    		"(select f.real_name from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_name_1," +
    		"(select f.file_name from irsa_defendant_file f where f.mode = '1' and f.res_id = p.id) file_1," +
    		"(select f.real_name from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_name_2," +
            "(select f.file_name from irsa_defendant_file f where f.mode = '2' and f.res_id = p.id) file_2," +
            "(select f.real_name from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_name_3," +
            "(select f.file_name from irsa_defendant_file f where f.mode = '3' and f.res_id = p.id) file_3 " +
    		"from irsa_defendant p where p.trash = '0' ";
    
    public static String SELECT_EXIST = "select id,account from irsa_defendant where trash = '0' ";
    public static String SELECT_EXIST_BY_NAME = SELECT_EXIST + "and name = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ? ";
    public static String SELECT_EXIST_BY_ACCOUNT = SELECT_EXIST + "and account = ?";
    public static String SELECT_EXIST_BY_NOID_ACCOUNT = SELECT_EXIST + "and id != ? and account = ?";
    public static String SELECT_EXIST_BY_ACCOUNT_PWD = SELECT_EXIST + "and account = ? and pwd = ?";
    
    public static String SELECT_ACCOUNT_BY_ID = "select p.unit_name,p.unit_abode,p.unit_contact,name,legal_person_type,duty" +
            "(select t.title from irsa_defendant_type t where t.id = p.type_id) label_type " +
    		"from irsa_defendant p where p.trash = '0' and p.id = ?";
    
    public static String SELECT_BY_ID = SELECT + "and p.id = ?";
    public static String SELECT_4_CHOOSE = SELECT + "and p.unit_name like ? ";
    public static String SELECT_TEMP_DEFENDANT = "SELECT t.createtime,t.personnel_id,t.type,d.type_id,d.unit_name,d.unit_abode,unit_contact,d.name,d.legal_person_type,d.duty FROM irsa_cases_personnel_temp t"
    		+ " LEFT JOIN irsa_defendant d ON t.personnel_id=d.id"
    		+ " where t.cases_id= ? ";
    
}
