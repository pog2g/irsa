package com.irsa.cases.model;

/**
 * 案件相关人员
 *
 * @author EZ
 */
public class CasesPersonnel {
    /**
     * 申请人
     */
    public static String PERSONNEL_TYPE_1 = "1";
    /**
     * 被申请人
     */
    public static String PERSONNEL_TYPE_2 = "2";
    /**
     * 正式第三人
     */
    public static String PERSONNEL_TYPE_3 = "3";
    /**
     * 申请人委托代理人
     */
    public static String PERSONNEL_TYPE_4 = "4";
    /**
     * 第三人委托人代理人
     */
    public static String PERSONNEL_TYPE_5 = "5";
    /**
     * 被申请人委托人代理人
     */
    public static String PERSONNEL_TYPE_6 = "6";
    /**
     * 建议第三人
     */
    public static String PERSONNEL_TYPE_7 = "7";
    /**
     * 从建议添加第三人
     */
    public static String PERSONNEL_TYPE_73 = "73";

    /**
     * 是否申请人代表：否
     */
    public static String REPRESENTATIVE_0 = "0";
    /**
     * 是否申请人代表：是
     */
    public static String REPRESENTATIVE_1 = "1";

    String irsa_cases_personnel;
    String id;
    String createitme;

    String cases_id;
    String personnel_type;// 1=申请人，2=被申请人，3=第三人，4=申请人代理人，5=第三人人代理人，6=被申请人代理人，7=（申请人建议追加）第三人
    String personnel_client;// 委托人： 1=申请人，2=被申请人，{id}=第三人
    String representative; // 是否为申请人代表，0=否，1=是
    String personnel_id;// 当事人id

    String type;
    String name;
    String other_name;
    String nature;//民族
    String gender;
    String birthday;
    String id_type_id;
    String id_no;
    String phone;
    String domicile;//户籍所在地
    String zip_code;//邮编
    String contact;//其他联系方式
    String abode;//法律文书送达地址/通讯地址

    String unit_name;
    String unit_contact;//单位联系方式
    String unit_id_type_id;//单位证件类型id
    String unit_id_no;//单位证件号码
    String unit_abode;//单位住所
    String legal_person_type;//法人类型   1、法定代表人2、负责人

    String identity;//身份类型
    String kinsfolk;//亲属关系
    String duty;//职务


    //保存申请人/第三人/建议第三人SQL
    public static String INSERT_APPLY_OR_THIRDPARTY = "insert into irsa_cases_personnel(createtime,id,cases_id,personnel_type,representative,personnel_id," +
            "type,name,other_name,nature,gender,birthday,id_type_id,id_no,phone,domicile,zip_code,contact,abode,unit_name,unit_contact,unit_id_type_id,unit_id_no,unit_abode,legal_person_type)" +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    //保存被申请人SQL
    public static String INSERT_DEFENDANT = "insert into irsa_cases_personnel(createtime,id,cases_id,personnel_type,personnel_id," +
            "type,name,unit_name,unit_abode,unit_contact,legal_person_type)" +
            "values(?,?,?,?,?,?,?,?,?,?,?)";

    //保存委托代理人SQL
    public static String INSERT_AGENT = "insert into irsa_cases_personnel(createtime, id, cases_id, personnel_type, personnel_client, personnel_id," +
            "name, nature, gender, birthday, id_type_id, id_no, phone, domicile, unit_name, identity, kinsfolk, legal_person_type)" +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    //修改申请人/第三人SQL
    public static String UPDATE_APPLY_OR_THIRDPARTY = "update irsa_cases_personnel set type = ?, name = ?,other_name = ?, nature = ? , gender = ?, birthday = ?, id_type_id = ?, id_no = ?, phone = ?," +
            "domicile = ?, zip_code = ?, contact = ?, abode = ?, unit_name = ?, unit_contact = ?, unit_id_type_id = ?, unit_id_no = ?, unit_abode = ?, legal_person_type = ?  where id = ?";

    //修改被申请人SQL
    public static String UPDATE_DEFENDANT = "update irsa_cases_personnel set type = ?, name = ?," +
            "unit_name = ?, unit_contact = ?, unit_abode = ?, legal_person_type = ?  where id = ?";


    //修改代理人SQL
    public static String UPDATE_AGENT = "update irsa_cases_personnel set type = ?, name = ?, nature = ?, gender = ?, birthday = ?, id_type_id = ?, id_no = ?, phone = ?," +
            "domicile = ?, unit_name = ?, identity = ?, kinsfolk = ?, legal_person_type = ?  where id = ?";


    //查询申请人/第三人/建议第三人SQL
    public static String SELECT_TYPE1_3_7_BY_CASESID = "select p.createtime,p.personnel_type,p.representative,p.personnel_id," +
            "p.id,p.type,p.name,p.other_name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no,p.phone," +
            "p.domicile,p.zip_code,p.contact,p.abode," +
            "p.unit_name,p.unit_contact,p.unit_id_type_id,p.unit_id_no,p.unit_abode,p.legal_person_type" +
            "case p.type when '1' then '个人' when '2' then '法人组织' else '' end label_type," +
            "case p.gender when '1' then '男' when '2' then '女' else '' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select i.title from irsa_unit_id_type i where i.id = p.unit_id_type_id) label_unit_id_type," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id limit 1) id_file_name," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id limit 1) id_file " +
            "from irsa_cases_personnel p where p.cases_id = ? and p.personnel_type = ? ";


    //查询被申请人SQL
    public static String SELECT_TYPE2_BY_CASESID_ = "select p.createtime,p.personnel_type,p.personnel_id," +
            "p.id,p.type,p.name,p.unit_name,unit_contact,unit_abode,p.legal_person_type," +
            "(select t.title from irsa_defendant_type t where t.id = p.type) label_type," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '1') file_name_1," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '1') file_1," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '2') file_name_2," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '2') file_2," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '3') file_name_3," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '3') file_3," +
            "(select count(*) from irsa_cases c,irsa_cases_personnel cp where c.id = cp.cases_id and cp.personnel_id = p.personnel_id) defendant_cases_number " +
            "from irsa_cases_personnel p where p.cases_id = ? and p.personnel_type = '2' order by p.createtime,p.id";


    //查询代理人SQL
    public static String SELECT_AGENT_BY_CASESID_AND_CLIENTID = "select p.createtime,p.personnel_type,p.personnel_id," +
            "p.id,p.name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no,p.phone,p.domicile,p.unit_name,p.identity,p.kinsfolk,p.legal_person_type" +
            "case p.gender when '1' then '男' when '2' then '女' else '' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select f.real_name from irsa_cases_file f where f.personnel_id = p.id limit 1) agent_file_name," +
            "(select f.file_name from irsa_cases_file f where f.personnel_id = p.id limit 1) agent_file " +
            "from irsa_cases_personnel p where p.cases_id = ? and p.personnel_client = ? ";


    public static String INSERT = "insert into irsa_cases_personnel(createtime, id, cases_id, personnel_type, personnel_client, personnel_id," +
            "                    name, nature, gender, birthday, id_type_id, id_no, phone, domicile, unit_name, identity, kinsfolk, legal_person_type) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String INSERT_2 = "insert into irsa_cases_personnel(createtime,id,cases_id,personnel_type,personnel_id,type,name,legal_person,address) " +
            "values(?,?,?,'2',?,?,?,?,?)";

    public static String UPDATE13 = "update irsa_cases_personnel set name = ?,legal_person = ?,id_type_id = ?,id_no = ?," +
            "gender = ?,birthday = ?,phone = ?,contact = ?,zip_code = ?,county_id = ?,address = ?, " +
            "unit_id_type_id = ?,unit_id_no = ?,unit_contact = ?,domicile = ? where id = ?";
    public static String UPDATE2 = "update irsa_cases_personnel set type = ?,name = ?,address = ?,legal_person = ? where id = ?";
    public static String UPDATE6 = "update irsa_cases_personnel set name = ?,unit_name = ?,id_type_id = ?,id_no = ?," +
            "gender = ?,birthday = ?,phone = ?,contact = ?,zip_code = ?,county_id = ?,address = ? where id = ?";

    public static String DELETE = "delete from irsa_cases_personnel where id = ?";

    public static String SELECT_EXIST = "select cases_id,personnel_type,id,type,name from irsa_cases_personnel ";
    public static String SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE = SELECT_EXIST + "where cases_id = ? and personnel_type = ?";
    public static String SELECT_EXIST_BY_CASESID_ID = SELECT_EXIST + "where cases_id = ? and id = ?";
    public static String SELECT_EXIST_BY_ID = SELECT_EXIST + "where id = ?";
    public static String SELECT_EXIST_BY_CASESID = SELECT_EXIST + "where cases_id = ?";
    public static String SELECT_EXIST_BY_CASESID_PERSONNELID = SELECT_EXIST + "where cases_id = ? and personnel_id = ?";
    public static String SELECT_EXIST_BY_CASESID_PERSONNELID_CLIENTID = SELECT_EXIST + "where cases_id = ? and personnel_id = ? and personnel_client = ?";
    public static String SELECT_EXIST_BY_CASESID_PERSONNELID_WITHOUT_APPLYTHIRDPARTY = SELECT_EXIST + "where cases_id = ? and personnel_id = ? and personnel_type != '7'";


    //根据案件编号查询相关申请人/第三人信息
    public static String SELECT_BY_CASESID_TYPE137 = "select p.createtime,p.personnel_type,p.representative,p.personnel_id," +
            "p.id,p.type,p.name,p.other_name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no," +
            "p.phone,p.domicile,p.zip_code,p.contact,p.abode," +
            "p.unit_name,p.unit_contact,p.unit_id_type_id,p.unit_id_no,p.unit_abode,p.legal_person_type," +
            "case p.type when '1' then '个人' when '2' then '法人组织' else '' end label_type," +
            "case p.gender when '1' then '男' when '2' then '女' else '' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select i.title from irsa_unit_id_type i where i.id = p.unit_id_type_id) label_unit_id_type," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id limit 1) id_file_name," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id limit 1) id_file " +
            "from irsa_cases_personnel p where p.cases_id = ? and p.personnel_type = ? ";


    //查询被申请人
    public static String SELECT_BY_CASESID_TYPE2 = "select p.createtime,p.personnel_type,p.personnel_id," +
            "p.id,p.type,p.name,p.legal_person_type,p.unit_name,p.unit_abode,p.unit_contact,p.duty," +
            "(select t.title from irsa_defendant_type t where t.sequence = p.type) label_type," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '1') file_name_1," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '1') file_1," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '2') file_name_2," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '2') file_2," +
            "(select f.real_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '3') file_name_3," +
            "(select f.file_name from irsa_cases_personnel_file f where f.res_id = p.id and mode = '3') file_3," +
            "(select count(*) from irsa_cases c,irsa_cases_personnel cp where c.id = cp.cases_id and cp.personnel_id = p.personnel_id) defendant_cases_number " +
            "from irsa_cases_personnel p where p.cases_id = ? and p.personnel_type = '2' order by p.createtime,p.id";


    //查询委托代理人
    public static String SELECT_BY_CASESID_CLIENTID = "select p.createtime,p.personnel_type,p.personnel_id," +
            "p.id,p.name,p.nature,p.gender,date_format(p.birthday,'%Y-%m-%d') birthday,p.id_type_id,p.id_no,p.phone,p.domicile,p.unit_name,p.identity,p.kinsfolk,p.legal_person_type," +
            "case p.gender when '1' then '男' when '2' then '女' else '' end label_gender," +
            "(select i.title from irsa_id_type i where i.id = p.id_type_id) label_id_type," +
            "(select f.real_name from irsa_cases_file f where f.personnel_id = p.id limit 1) agent_file_name," +
            "(select f.file_name from irsa_cases_file f where f.personnel_id = p.id limit 1) agent_file " +
            "from irsa_cases_personnel p where p.cases_id = ? and p.personnel_client = ? ";


    //查询案件申请人
    public static String SELECT_APPLY_BY_CASESID = "SELECT * from irsa_cases_personnel WHERE personnel_type = ? and cases_id = ? order by createtime,id";

}
