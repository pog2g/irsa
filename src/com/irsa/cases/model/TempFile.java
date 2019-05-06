package com.irsa.cases.model;

/**
 * 临时文件
 * @author EZ
 *
 */
public class TempFile {
    String irsa_temp_file;
    String id;
    String createtime;
    
    String type;//参考CasesFile类type字段
    String mode;//参考CasesFile类mode字段，DefendantFile类mode字段，
    String res_id;//相关资源id
    String real_name;//展示名称，示例：身份证件照
    String size;//大小
    String ext;//后缀
    String file_name;//服务器文件名，示例：0e5429ae24224a5ba9d9e34f1e7bdbe6.doc
    String personnel_id;//
    
    public static String INSERT = "insert into irsa_temp_file(id,createtime,mode,type,res_id,real_name,size,ext,file_name) values(?,?,?,?,?,?,?,?,?)";
    public static String DELETE_BY_RESID = "delete from irsa_temp_file where res_id = ?";
    public static String DELETE_BY_RESID_MODE = "delete from irsa_temp_file where res_id = ? and mode = ?";
    public static String DELETE_BY_ID = "delete from irsa_temp_file where id = ?";
    public static String UPDATE_REALNAME = "update irsa_temp_file set real_name = ? where id = ?";
    public static String UPDATE_PERSONNELID = "update irsa_temp_file set personnel_id = ? where id = ?";
    
    private static String SELECT = "select id,type,mode,res_id,real_name,size,ext,file_name,date_format(createtime, '%Y-%m-%d %H:%i:%s') create_time from irsa_temp_file ";
    public static String SELECT_BY_ID = SELECT + "where id = ?";
    public static String SELECT_BY_RESID = SELECT + "where res_id = ? order by createtime,real_name";
    public static String SELECT_BY_RESID_MODE = SELECT + "where res_id = ? and mode = ? order by createtime,real_name";
    public static String SELECT_BY_PERSONNELID = SELECT + "where personnel_id = ? order by createtime";
}
