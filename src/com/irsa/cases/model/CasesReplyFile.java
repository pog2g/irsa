package com.irsa.cases.model;

/**
 * 案件答复相关文件
 * @author EZ
 *
 */
public class CasesReplyFile {
    /**答复书*/
    public static String MODE_1 = "1";
    /**证据信息*/
    public static String MODE_2 = "2";
    
    String irsa_cases_reply_file;
    String id;
    String createtime;
    
    String mode;//1=答复书，2=证据信息
    String type;//1=jpg/png 2=doc/docx，3=xls/xlsx，4=pdf, 5=video，6=rar/zip，7=其他
    String res_id;//
    String real_name;//展示名称，示例：身份证件照
    String size;
    String ext;
    String file_name;//服务器文件名，示例：0e5429ae24224a5ba9d9e34f1e7bdbe6.doc
    String account_id;//上传人
    
    public static String INSERT = "insert into irsa_cases_reply_file(id,createtime,mode,type,res_id,real_name,size,ext,file_name) values(?,?,?,?,?,?,?,?,?)";
    
    public static String DELETE = "delete from irsa_cases_reply_file where res_id = ?";
    
    public static String SELECT_BY_CASESID_MODE = "select * from irsa_cases_reply_file where res_id = ? and mode = ?";
}
