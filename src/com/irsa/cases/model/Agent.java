package com.irsa.cases.model;

/**
 * 委托代理人
 * @author EZ
 *
 */
public class Agent {
    /**男*/
    public static String GENDER_1 = "1";
    /**女*/
    public static String GENDER_2 = "2";
    
    String irsa_agent;
    String id;
    String createtime;
    String trash;//0=为删除，1=删除
    
    String name;//姓名
    String nature;//民族
    String gender;//性别，1=男，2=女
    String birthday;//出生日期
    String id_type_id;//证件类型id
    String id_no;//证件号码
    String phone;//手机号码
    String abode;//通讯地址
    String unit_name;//工作单位

    String identity;//身份类型
    String kinsfolk;//亲属关系
    String type;//代理人类型


    String address;//户籍所在地/联系地址/详细地址[暂停使用字段]
    String zip_code;//邮编  [暂停使用字段]
    String county_id;//区 [暂停使用字段]
    String contact;//其他联系方式 [暂停使用字段]
    String domicile;//户籍地址 [暂停使用字段]


    
    /*public static String INSERT = "insert into irsa_agent(createtime,id,name,type,unit_name,gender,birthday,phone,kinsfolk,identity,address,id_type_id,id_no) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";*/

    public static String INSERT = "insert into irsa_agent(createtime,id,name,nature,gender,birthday,id_type_id, id_no, phone,abode,unit_name,identity, kinsfolk,type) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static String UPDATE = "update irsa_agent set name = ?,nature = ?,gender = ?,birthday = ?, id_type_id = ?, id_no = ?,phone = ?,abode =?,unit_name = ?, identity = ?,kinsfolk = ?,type = ? where id = ?";
    private static String SELECT_EXIST = "select id from irsa_agent where trash = '0' ";
    public static String SELECT_EXIST_BY_IDNO = SELECT_EXIST + "and id_no = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "and id = ?";
    
    public static String SELECT = "select date_format(p.createtime,'%Y-%m-%d %H:%i:%s') createtime," +
            "p.id,p.name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no,p.phone,p.abode,p.unit_name,p.identity,p.kinsfolk,p.type," +
            "case p.gender when '1' then '男' else '女' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type " +
            "from irsa_agent p where p.trash = '0' ";
    public static String SELECT_BY_ID = SELECT + "and p.id = ?";
    public static String SELECT_4_CHOOSE = SELECT + "and (p.id_no like ? or p.name like ?) order by p.createtime desc";
}
