package com.irsa.cases.manager;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.model.TempFile;
import com.jdbc.manager.BaseManager;

@Service
public class TempFileManager extends BaseManager {
    Logger log = LoggerFactory.getLogger(TempFileManager.class);
    
    public void saveTempFileList(String resId, String mode, List<FileItem> fileList) {
        File pathTemp = new File(Utils4File.getPathTemp());
        if (!pathTemp.exists()) {
            pathTemp.mkdirs();
        }  
        for (FileItem file : fileList) {
            String realName = file.getName();
            String ext = realName.substring(realName.lastIndexOf(".")+1).toLowerCase();
            String showName = realName.substring(0, realName.lastIndexOf("."));
            String fileName = Utils.getId() + "." + ext;
            String fileTempPath = Utils4File.getPathTemp() + fileName;
            File fileTemp = new File(fileTempPath);
            try {
                file.write(fileTemp);
                executeSQL(TempFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), mode, Utils4File.getFileType(ext), resId, showName, file.getSize(), ext, fileName});
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    
    public List<Map<String, Object>> getTempFileList(String resId, String mode) {
        List<Map<String, Object>> tempFileList = null;
        if (StringUtils.isBlank(mode)) {
            tempFileList = getList(TempFile.SELECT_BY_RESID, new Object[]{resId});
        } else {
            tempFileList = getList(TempFile.SELECT_BY_RESID_MODE, new Object[]{resId, mode});
        }
        for (Map<String, Object> tempFile : tempFileList) {
            tempFile.put("url", Utils4File.getFileUrl(Utils4File.PATH_TEMP, tempFile.get("file_name").toString()));
        }
        return tempFileList;
    }
}
