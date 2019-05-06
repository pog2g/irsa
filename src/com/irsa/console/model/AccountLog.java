package com.irsa.console.model;

/**
 * 用户日志
 * @author EZ
 *
 */
public class AccountLog {
    String irsa_console_account_log;
    
    String id;
    String account_id;
    String type;//类型，1=登陆日志
    String ip;
    String createtime;
    
    public static String INSERT_1 = "insert into irsa_console_account_log(id,createtime,type,account_id,ip) values(?,?,'1',?,?)";
}
