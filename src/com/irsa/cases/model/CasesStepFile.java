package com.irsa.cases.model;

/**
 * 案件相关文件
 * @author EZ
 *
 */
public class CasesStepFile {
    String irsa_cases_step_file;
    String id;
    String createtime;
    
    String res_id;
    String file_name;//服务器文件名，示例：0e5429ae24224a5ba9d9e34f1e7bdbe6.doc
    
    public static String INSERT = "insert into irsa_cases_step_file(id,createtime,res_id,file_name) values(?,?,?,?)";
}
