package com.irsa.cases.console.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.manager.TempFileManager;
import com.irsa.cases.model.CasesFile;
import com.irsa.cases.model.TempFile;

@Service
public class TempFileConsoleServiceImpl implements TempFileConsoleService{
    Logger log = LoggerFactory.getLogger(TempFileConsoleServiceImpl.class);
    @Autowired
    TempFileManager tempFileManager;
    
    @Override
    public Map<String, Object> getList(String resId, String mode) {
        try {
            return Utils.getSuccessMap(tempFileManager.getTempFileList(resId, mode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> img(HttpServletRequest request) {
        return upload(request, true, false);
    }
    
    @Override
    public Map<String, Object> imgs(HttpServletRequest request) {
        return upload(request, true, true);
    }
    
    @Override
    public Map<String, Object> file(HttpServletRequest request) {
        return upload(request, false, false);
    }
    
    @Override
    public Map<String, Object> files(HttpServletRequest request) {
        return upload(request, false, true);
    }
    
    Map<String, Object> upload(HttpServletRequest request, boolean isImg, boolean isMultiple) {
        try {
            String resId = request.getParameter("res");
            String mode = Utils.toString(request.getParameter("mode"));
            if (StringUtils.isBlank(resId)) {
                return Utils.getErrorMap("操作失败");
            }
            List<FileItem> fileList = Utils4File.getUploadFileList(request, isImg); 
            if (fileList == null) {
                return Utils.getErrorMap("文件格式错误");
            }
            if (fileList.size() == 0) {
                return Utils.getErrorMap("请选择文件");
            }
            if (!isMultiple) {
                if (fileList.size() > 1) {
                    return Utils.getErrorMap("请选择一个文件");
                }
            }
            if (!isMultiple) {
                if (StringUtils.isBlank(mode)) {
                    tempFileManager.executeSQL(TempFile.DELETE_BY_RESID, new Object[]{resId});
                } else {
                    tempFileManager.executeSQL(TempFile.DELETE_BY_RESID_MODE, new Object[]{resId, mode});
                }
            }
            tempFileManager.saveTempFileList(resId, mode, fileList);
            return Utils.getSuccessMap(tempFileManager.getTempFileList(resId, mode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> rename(String id, String name) {
        try {
            int num = tempFileManager.getListCount(TempFile.SELECT_BY_ID, new Object[]{id});
            if (num != 0) {
                tempFileManager.executeSQL(TempFile.UPDATE_REALNAME, new Object[]{name, id});
                return Utils.getSuccessMap(null);
            }
            num = tempFileManager.getListCount(CasesFile.SELECT_BY_ID, new Object[]{id});
            if (num != 0) {
                tempFileManager.executeSQL(CasesFile.UPDATE_REALNAME, new Object[]{name, id});
                return Utils.getSuccessMap(null);
            }
            return Utils.getErrorMap("文件已不存在");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> delete(String id) {
        try {
            tempFileManager.executeSQL(TempFile.DELETE_BY_ID, new Object[]{id});
            tempFileManager.executeSQL(CasesFile.DELETE_BY_ID, new Object[]{id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public TempFileManager getTempFileManager() {
        return tempFileManager;
    }
    public void setTempFileManager(TempFileManager tempFileManager) {
        this.tempFileManager = tempFileManager;
    }
}
