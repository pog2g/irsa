package com.irsa.cases.model;

/**
 * 案件相关人员文件
 * @author EZ
 *
 */
public class CasesPersonnelFile {
    String irsa_cases_personnel_file;
    String id;
    String createtime;
    
    String res_id;
    String mode;
    String type;
    String real_name;
    String size;
    String ext;
    String file_name;
    
    public static String INSERT = "insert into irsa_cases_personnel_file(createtime, id, res_id, mode, type, real_name, size, ext, file_name) " +
    		"values(?,?,?,?,?,?,?,?,?)";
    
    public static String DELETE_BY_RESID = "delete from irsa_cases_personnel_file where res_id = ?";
}
