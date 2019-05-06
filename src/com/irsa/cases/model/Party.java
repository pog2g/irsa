package com.irsa.cases.model;

/**
 * 申请人
 * @author EZ
 *
 */
public class Party {
    /**个人*/
    public static String TYPE_1 = "1";
    /**法人组织*/
    public static String TYPE_2 = "2";
    /**男*/
    public static String GENDER_1 = "1";
    /**女*/
    public static String GENDER_2 = "2";
    
    String irsa_party;
    String id;
    String createtime;
    String type;//申请人类型:1=个人，2=法人
    String trash;//0=为删除，1=删除
    
    String unit_name;//单位名称
    String unit_contact;//单位联系方式
    String unit_id_type_id;//单位证件类型id
    String unit_id_no;//单位证件号码
    String unit_abode;//单位住所
    String legal_person_type;//法定负责人类型，1=法定代表人，2=负责人
        
    String name;//姓名(法人代表名称)
    String other_name;//英文名/其他语言名/化名/曾用名(法人代表别名)
    String nature;//民族(法人代表民族)
    String gender;//性别，1=男，2=女(法人代表性别)
    String birthday;//出生日期  (法人代表出生日期)
    String id_type_id;//证件类型(法人代表证件类型)
    String id_no;//证件号码(法人代表证件号码)
    String phone;//手机号码(法人代表手机号码)
    String domicile;//户籍所在地(法人代表户籍所在地)
    String zip_code;//邮编
    String contact;//其他联系方式  (法人代表其他联系方式)   
    String abode;//法律文书送达地址

 
    
    /**
     * 新增案件申请人sql
     * 参数(创建时间,申请人ID,申请人类型,姓名,[别名/法定代表人],性别[1男/2女],出生日期,手机号码,其他联系方式,邮编,法律文书送达地址,证件类型ID,证件号码,单位证件类型ID,单位证件号码,单位联系方式,户籍所在地,民族,法律文书送达地址,职务)
     */
    public static String INSERT = "insert into irsa_party(createtime,id,type,name,legal_person,gender,birthday,phone,contact,zip_code," +
    		"address,id_type_id,id_no,unit_id_type_id,unit_id_no,unit_contact,domicile,nature,ws_address,zw) " +
    		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    public static String INSERT_PERSON = "insert into irsa_party(createtime,id,type,name,other_name,nature,gender,birthday,id_type_id,id_no,phone,domicile,zip_code,contact,abode) " +
    		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String INSERT_LEGAL = "insert into irsa_party(createtime,id,type,name,other_name,nature,gender,birthday,id_type_id,id_no,phone,domicile,zip_code,contact,abode, " +
    		"unit_name, unit_contact, unit_id_type_id, unit_id_no, unit_abode, legal_person_type)" + 
    		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    public static String UPDATE = "update irsa_party set type = ?,name = ?,other_name= ?, nature = ?,gender = ?,birthday = ?,id_type_id= ?,id_no= ?,phone = ?,domicile= ?,zip_code= ?,contact= ?,abode= ?," +
    		"unit_name = ?,unit_contact = ?,unit_id_type_id = ?,unit_id_no = ?,unit_abode = ?,legal_person_type = ?  where id = ?";
    
    //原修改sql
    /*public static String UPDATE = "update irsa_party set type = ?,name = ?,legal_person = ?,gender = ?,birthday = ?,phone = ?,contact = ?,zip_code = ?," +
    		"county_id = ?,address = ?,id_type_id = ?,id_no = ?,unit_id_type_id = ?,unit_id_no = ?,unit_contact = ?,domicile = ?,nature=?,ws_address=?,zw=?  where id = ?";*/
    
    private static String SELECT_EXIST = "select id from irsa_party where trash = '0' ";
    public static String SELECT_EXIST_BY_IDNO = SELECT_EXIST + "and id_no = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT = "select date_format(p.createtime,'%Y-%m-%d %H:%i:%s') createtime," +
    		"p.id,p.type,p.name,p.other_name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no,p.phone,p.domicile,p.zip_code,p.contact,p.abode," +
            "p.unit_name,p.unit_contact,p.unit_id_type_id,p.unit_id_no,p.unit_abode,p.legal_person_type," +
    		"(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
    		"(select i.title from irsa_unit_id_type i where i.id = p.unit_id_type_id) label_unit_id_type," +
    		"(select f.real_name from irsa_party_file f where f.res_id = p.id) id_file_name," +
    		"(select f.file_name from irsa_party_file f where f.res_id = p.id) id_file " +
    		"from irsa_party p where p.trash = '0' ";
    public static String SELECT_BY_ID = SELECT + "and p.id = ?";
    public static String SELECT_4_CHOOSE = SELECT + "and (p.id_no like ? or p.name like ? or p.unit_name like ?) ";
}
