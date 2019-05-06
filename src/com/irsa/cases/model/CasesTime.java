package com.irsa.cases.model;

/**
 * 案件时间
 * @author EZ
 *
 */
public class CasesTime {
    String irsa_cases_time;
    String id;
    String createtime;
    
    String cases_id;
    String type;// 1=受理时限，2=中转时限，3=审理时限
    String limit_time;
    
    public static String INSERT = "insert into irsa_cases_time(id,createtime,cases_id,type,limit_time) values(?,?,?,?,?)";
}
