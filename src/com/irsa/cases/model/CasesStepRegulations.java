package com.irsa.cases.model;

/**
 * 案件步骤参考条例
 * @author EZ
 *
 */
public class CasesStepRegulations {
    String irsa_cases_step_regulations;
    String id;
    String createtime;
    
    String res_id;
    String regulations_id;
    
    public static String INSERT = "insert into irsa_cases_step_regulations(id,createtime,res_id,regulations_id) values(?,?,?,?)";
    public static String DELETE_BY_RESID = "delete from irsa_cases_step_regulations where res_id = ?";
}
