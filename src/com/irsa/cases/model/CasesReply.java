package com.irsa.cases.model;

/**
 * 案件答复
 * @author EZ
 *
 */
public class CasesReply {
    String irsa_cases_reply;
    String id;
    String createtime;
    
    String type;//1=申请人答复，被申请人答复
    String reply;//答复
    String remark;//备注
    String last_time;//最后修改时间
}
