package com.irsa.cases.model;

/**
 * 被申请人证件
 * @author EZ
 *
 */
public class DefendantFile {
    /**社会统一信用代码证*/
    public static String MODE_1 = "1";
    /**法定代表人证明书*/
    public static String MODE_2 = "2";
    /**法定代表人身份证*/
    public static String MODE_3 = "3";
    
    String irsa_defendant_file;
    String id;
    String createtime;
    
    String mode;//1=社会统一信用代码证，2=法定代表人证明书，3=法定代表人身份证
    String type;//1=doc/docx，2=xls/xlsx，3=jpg/png，4=rar/zip
    String res_id;//
    String real_name;//展示名称，示例：身份证件照
    String size;
    String ext;
    String file_name;//服务器文件名，示例：0e5429ae24224a5ba9d9e34f1e7bdbe6.doc
    
    public static String INSERT = "insert into irsa_defendant_file(id,createtime,mode,type,res_id,real_name,size,ext,file_name) values(?,?,?,?,?,?,?,?,?)";
    public static String DELETE_BY_RESID_MODE = "delete from irsa_defendant_file where res_id = ? and mode = ?";
    public static String SELECT_BY_RESID = "select * from irsa_defendant_file where res_id = ?";
}
