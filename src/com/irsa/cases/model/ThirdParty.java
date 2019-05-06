package com.irsa.cases.model;

/**
 * 第三人
 * @author EZ
 *
 */
public class ThirdParty {
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
    String trash;//0=为删除，1=删除
    
    String type;//申请人类型，1=个人，2=法人组织
    String name;//姓名，法人组织名称
    String legal_person;//英文名/其他语言名/化名/曾用名，法定代表人（负责人）
    String gender;//性别，1=男，2=女
    String birthday;//出生日期  
    String phone;//手机号码
    String contact;//其他联系方式
    String zip_code;//邮编  
    String county_id;//区
    String address;//详细地址
    String id_type_id;//证件类型id
    String id_no;//证件号码  
    
    public static String INSERT = "insert into irsa_party(createtime,id,type,name,legal_person,gender,birthday,phone,contact,zip_code,county_id,address,id_type_id,id_no) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String UPDATE = "update irsa_party set type = ?,name = ?,legal_person = ?,gender = ?,birthday = ?,phone = ?,contact = ?,zip_code = ?,county_id = ?,address = ?,id_type_id = ?,id_no = ? where id = ?";
    
    private static String SELECT_EXIST = "select id from irsa_party where trash = '0' ";
    public static String SELECT_EXIST_BY_IDNO = SELECT_EXIST + "and id_no = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT = "select date_format(p.createtime,'%Y-%m-%d %H:%i:%s') createtime," +
            "p.id,p.type,p.name,p.legal_person,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.phone,p.contact,p.zip_code,p.county_id,p.address,p.id_type_id,p.id_no," +
            "case p.type when '1' then '个人' else '法人组织' end label_type," +
            "case p.gender when '1' then '男' else '女' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select (select (select rrr.title from irsa_region rrr where rrr.id = rr.parent_id) from irsa_region rr where rr.id = r.parent_id) from irsa_region r where r.id = p.county_id) province," +
            "(select (select rr.title from irsa_region rr where rr.id = r.parent_id) from irsa_region r where r.id = p.county_id) city," +
            "(select r.title from irsa_region r where r.id = p.county_id) county," +
            "(select f.real_name from irsa_party_file f where f.res_id = p.id) id_file_name," +
            "(select f.file_name from irsa_party_file f where f.res_id = p.id) id_file " +
            "from irsa_party p where p.trash = '0' ";
    public static String SELECT_BY_ID = SELECT + "and p.id = ?";
    public static String SELECT_4_CHOOSE = SELECT + "and (p.id_no like ? or p.name like ? or p.legal_person like ?) ";
}
