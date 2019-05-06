package com.irsa.cases.manager;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.model.CasesFile;
import com.jdbc.manager.BaseManager;

@Service
public class CasesFileManager extends BaseManager {
    Logger log = LoggerFactory.getLogger(CasesFileManager.class);
    
    public List<Map<String, Object>> getList(String casesId, String mode) {
        List<Map<String, Object>> list = getList(CasesFile.SELECT_BY_CASESID_MODE, new Object[]{casesId, mode});
        if (list.size() > 0) {
            for (Map<String, Object> data :list) {
                data.put("url", Utils4File.getFileUrl(Utils4File.PATH_CASES, data.get("file_name").toString()));
            }
        }
        return list;
    }
    
    public void saveFileList(String resId, String mode, List<FileItem> fileList) {
        File pathTemp = new File(Utils4File.getPathCases());
        if (!pathTemp.exists()) {
            pathTemp.mkdirs();
        }  
        for (FileItem file : fileList) {
            String realName = file.getName();
            String ext = realName.substring(realName.lastIndexOf(".")+1).toLowerCase();
            String showName = realName.substring(0, realName.lastIndexOf("."));
            String fileName = Utils.getId() + "." + ext;
            String fileTempPath = Utils4File.getPathCases() + fileName;
            File fileTemp = new File(fileTempPath);
            try {
                file.write(fileTemp);
                executeSQL(CasesFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), mode, Utils4File.getFileType(ext), resId, showName, file.getSize(), ext, fileName, null});
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    
    public void saveFile(String resId, String mode, Map<String, Object> data) {
        Utils4File.copyTempToReal(Utils4File.PATH_CASES, data.get("file_name").toString());
        executeSQL(CasesFile.INSERT, new Object[]{Utils.getId(), data.get("create_time"), mode, data.get("type"), resId, data.get("real_name"), data.get("size"), data.get("ext"), data.get("file_name"), null});
    }
    
    public String saveDocument(String casesId, String documentName, String html) {
        String fileName = Utils.getId() + ".doc";
        Utils4File.createDoc(fileName, html);
        executeSQL(CasesFile.INSERT_DOC, new Object[]{Utils.getId(), Utils.getCreateTime(), casesId, documentName, fileName, html});
        return fileName;
    }
    
    public Map<String, Object> getMap(String id) {
        Map<String, Object> data =  getMap(CasesFile.SELECT_BY_ID, new Object[]{id});
        if (data != null) {
            data.put("url", Utils4File.getFileUrl(Utils4File.PATH_CASES, data.get("file_name").toString()));
        }
        return data;
    }
}
