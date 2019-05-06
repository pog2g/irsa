package com.irsa.cases.model;

/**
 * 案件相关文件
 * @author EZ
 *
 */
public class CasesFile {
    String irsa_cases_file;
    String id;
    String createtime;
    
    String mode;//1=申请人证据，2=被申请人证据，3=第三人证据，4=申请人委托代理书，5=电子卷宗，6=文书，7=被申请人答复书照片，8=被申请人委托代理书，9=申请人代表推选凭证，10=第三人委托代理书
    String type;//1=jpg/png 2=doc/docx，3=xls/xlsx，4=pdf, 5=video，6=rar/zip，7=其他
    String res_id;
    String real_name;//展示名称，示例：身份证件照
    String size;
    String ext;//示例：doc,txt
    String file_name;//服务器文件名，示例：0e5429ae24224a5ba9d9e34f1e7bdbe6.doc
    String html;
    String personnel_id;
    
    /**申请人证据*/
    public static String MODE_1 = "1";
    /**被申请人证据*/
    public static String MODE_2 = "2";
    /**第三人证据*/
    public static String MODE_3 = "3";
    /**申请人委托代理书*/
    public static String MODE_4 = "4";
    /**电子卷宗*/
    public static String MODE_5 = "5";
    /**文书*/
    public static String MODE_6 = "6";
    /**被申请人答复书照片*/
    public static String MODE_7 = "7";
    /**被申请人委托代理书*/
    public static String MODE_8 = "8";
    /**行政复议代表推选材料(原：申请人代表推选凭证)*/
    public static String MODE_9 = "9";
    /**第三人委托代理书*/
    public static String MODE_10 = "10";
    
    /**规范性文件转送函*/
    public static String MODE_11 = "11";
    
    public static String INSERT = "insert into irsa_cases_file(id,createtime,mode,type,res_id,real_name,size,ext,file_name,personnel_id) values(?,?,?,?,?,?,?,?,?,?)";
    public static String INSERT_DOC = "insert into irsa_cases_file(id,createtime,mode,type,res_id,real_name,ext,file_name,html) values(?,?,'6','2',?,?,'doc',?,?)";
    public static String UPDATE_DOC = "update irsa_cases_file set file_name = ?,html = ? where id = ?";
    public static String UPDATE_REALNAME = "update irsa_cases_file set real_name = ? where id = ?";
    
    public static String DELETE = "delete from irsa_cases_file where res_id = ?";
    public static String DELETE_BY_ID = "delete from irsa_cases_file where id = ?";
    
    public static String SELECT_BY_CASESID_MODE = "select * from irsa_cases_file where res_id = ? and mode = ? order by createtime,real_name";
    public static String SELECT_BY_ID = "select * from irsa_cases_file where id = ?";
}
