package com.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils4File {
    private static Logger log = LoggerFactory.getLogger(Utils4File.class);
    private final static String PATH_DOMAIN = "path_domain";
    private final static String PATH = "path";
    private final static String PATH_SERVER = "path_server";
    public final static String PATH_TEMP = "path_temp";
    public final static String PATH_PARTY = "path_party";
    public final static String PATH_DEFENDANT = "path_defendant";
    public final static String PATH_AGENT = "path_agent";
    public final static String PATH_CASES = "path_cases";
    
    private static String getPropertiesValue(String key) {
        try {
            Properties properties = new Properties();
            properties.load(Utils4File.class.getResourceAsStream("/file.properties"));
            String value = properties.getProperty(key);
            return value;
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static String getPathTemp() {
        return getPropertiesValue(PATH) + getPropertiesValue(PATH_TEMP);
    }
    
    public static String getPathCases() {
        return getPropertiesValue(PATH) + getPropertiesValue(PATH_CASES);
    }
    
    public static String getFileUrl(String pathKey, String fileName) {
        File serverFile = new File(getPropertiesValue(PATH_SERVER) + getPropertiesValue(pathKey) + fileName);
        if (!serverFile.exists()) {
            File file = new File(getPropertiesValue(PATH) + getPropertiesValue(pathKey) + fileName);
            if (file.exists()) {
                File serverPath = new File(getPropertiesValue(PATH_SERVER) + getPropertiesValue(pathKey));
                if (!serverPath.exists()) {
                    serverPath.mkdirs();
                } 
                fileChannelCopy(file, serverFile);
            }
        }
        return getPropertiesValue(PATH_DOMAIN) + getPropertiesValue(pathKey) + fileName;
    }
    
    public static void delete(String pathKey, String fileName) {
        File serverFile = new File(getPropertiesValue(PATH_SERVER) + getPropertiesValue(pathKey) + fileName);
        if (serverFile.isFile() && serverFile.exists()) {
            serverFile.delete();
        }
        File realFile = new File(getPropertiesValue(PATH) + getPropertiesValue(pathKey) + fileName);
        if (realFile.isFile() && realFile.exists()) {
            realFile.delete();
        }
    }
    
    public static void copyTempToReal(String pathKey, String fileName) {
        File realPath = new File(getPropertiesValue(PATH) + getPropertiesValue(pathKey));
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        fileChannelCopy(new File(getPropertiesValue(PATH) + getPropertiesValue(PATH_TEMP) + fileName), new File(getPropertiesValue(PATH) + getPropertiesValue(pathKey) + fileName));
    }
    
    private static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
        } catch(IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                fi.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                fo.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 获取上传文件
     * @param request
     * @param isImg 是否进行图片类型校验
     * @return
     * @throws FileUploadException
     */
    public static List<FileItem> getUploadFileList(HttpServletRequest request, boolean isImg) throws FileUploadException {
        //获得上传文件
        FileItemFactory fac = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setHeaderEncoding("utf-8");
        List<FileItem> uploadFileList = upload.parseRequest(request);
        if (uploadFileList == null || uploadFileList.size() == 0) {
            return null;
        }
        
        List<FileItem> fileList = new ArrayList<FileItem>(); 
        boolean isAllRight = true;
        for (FileItem uploadFile : uploadFileList) {
            //判断该表单项是否是普通类型
            if (uploadFile.isFormField()) {
                continue;
            }
            String name = uploadFile.getName();//文件名
            String extName = "";//扩展名，格式：png 
            if (name.lastIndexOf(".") >= 0) {
                extName = name.substring(name.lastIndexOf(".")+1).toLowerCase();
            }
            if (isImg && !checkImageFile(extName)) {
                isAllRight = false;break;
            }
            fileList.add(uploadFile);
        }
        if (!isAllRight) {
            return null;
        }
        return fileList;
    }
    
    private static boolean checkImageFile(String extName) {  
        if ("jpg".equalsIgnoreCase(extName)) {
            return true;  
        }  
        if ("png".equalsIgnoreCase(extName)) {
            return true;  
        }  
        return false;  
    }
    
    @SuppressWarnings("unused")
    private static boolean checkOfficeFile(String extName) {  
        if ("doc".equalsIgnoreCase(extName)) {
            return true;  
        }  
        if ("docx".equalsIgnoreCase(extName)) {
            return true;  
        }
        if ("pdf".equalsIgnoreCase(extName)) {
            return true;  
        }  
        return false;  
    }
    
    @SuppressWarnings("unused")
    private static boolean checkRarFile(String extName) {  
        if ("rar".equalsIgnoreCase(extName)) {
            return true;  
        }  
        if ("zip".equalsIgnoreCase(extName)) {
            return true;  
        }  
        return false;  
    }
    
    /**
     * 文件类型<br>
     * 1=jpg/png，2=doc/docx，3=xls/xlsx，4=pdf，5=video，6=rar/zip，7=其他
     * @param ext
     * @return
     */
    public static String getFileType(String ext) {
        if ("jpg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext)) {
            return "1";
        } else if ("doc".equalsIgnoreCase(ext) || "docx".equalsIgnoreCase(ext)) {
            return "2";
        } else if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
            return "3";
        } else if ("pdf".equalsIgnoreCase(ext)) {
            return "4";
        } else if ("mp4".equalsIgnoreCase(ext) || "flv".equalsIgnoreCase(ext)) {
            return "5";
        } else if ("rar".equalsIgnoreCase(ext) || "zip".equalsIgnoreCase(ext)) {
            return "6";
        } else {
            return "7";
        }
    }
    
    public static void createDoc(String fileName, String html){
        ByteArrayInputStream byteStream = null;
        FileOutputStream outStream = null;
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<html><body>").append(html).append("</body></html>");
            byte[] contentBytes = buffer.toString().getBytes("UTF-8");
            byteStream = new ByteArrayInputStream(contentBytes);
            
            POIFSFileSystem poifSystem = new POIFSFileSystem();
            DirectoryNode root = poifSystem.getRoot();
            root.createDocument("WordDocument", byteStream);
            
            File outFile = new File(getPathCases() + fileName);
            outStream = new FileOutputStream(outFile); 
            poifSystem.writeFilesystem(outStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                byteStream.close();
            } catch (Exception e2) {
                log.error(e2.getMessage(), e2);
            }
            try {
                outStream.close();
            } catch (Exception e2) {
                log.error(e2.getMessage(), e2);
            }
        }
    }
}
