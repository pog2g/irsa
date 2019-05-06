package com.irsa.cases.model;

/**
 * 当事人证件
 * @author EZ
 *
 */
public class PartyFile {
    String irsa_party_file;
    String id;
    String createtime;
    
    String type;//1=doc/docx，2=xls/xlsx，3=jpg/png，4=rar/zip
    String res_id;//
    String real_name;//展示名称，示例：身份证件照
    String size;
    String ext;
    String file_name;//服务器文件名，示例：0e5429ae24224a5ba9d9e34f1e7bdbe6.doc
    
    public static String INSERT = "insert into irsa_party_file(id,createtime,type,res_id,real_name,size,ext,file_name) values(?,?,?,?,?,?,?,?)";
    public static String DELETE_BY_RESID = "delete from irsa_party_file where res_id = ?";
    public static String SELECT_BY_RESID = "select * from irsa_party_file where res_id = ?";
}
