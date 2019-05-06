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
import com.irsa.cases.manager.CasesFileManager;
import com.irsa.cases.model.CasesFile;
import com.irsa.cases.model.CasesPersonnel;

@Service
public class CasesFileConsoleServiceImpl implements CasesFileConsoleService {
    Logger log = LoggerFactory.getLogger(CasesFileConsoleServiceImpl.class);
    @Autowired
    CasesFileManager casesFileManager;
    
    @Override
    public Map<String, Object> getList(String casesId, String mode) {
        try {
            return Utils.getSuccessMap(casesFileManager.getList(casesId, mode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> upload(HttpServletRequest request) {
        try {
            String resId = request.getParameter("res");
            String mode = Utils.toString(request.getParameter("mode"));
            if (StringUtils.isBlank(resId)) {
                return Utils.getErrorMap("操作失败");
            }
            List<FileItem> fileList = Utils4File.getUploadFileList(request, false); 
            if (fileList == null) {
                return Utils.getErrorMap("文件格式错误");
            }
            if (fileList.size() == 0) {
                return Utils.getErrorMap("请选择文件");
            }
            if (CasesFile.MODE_3.equals(mode)) {
                int num = casesFileManager.getListCount(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{resId, CasesPersonnel.PERSONNEL_TYPE_3});
                if (num == 0) {
                    return Utils.getErrorMap("案件没有第三人，不能追加第三人证据信息");
                }
            }
            
            casesFileManager.saveFileList(resId, mode, fileList);
            return Utils.getSuccessMap(casesFileManager.getList(resId, mode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getMap(String id) {
        try {
            return Utils.getSuccessMap(casesFileManager.getMap(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> save(String id, String html) {
        try {
            if (StringUtils.isBlank(html)) {
                return Utils.getErrorMap("文书不能为空");
            }
            
            String fileName = Utils.getId() + ".doc";
            Utils4File.createDoc(fileName, html);
            casesFileManager.executeSQL(CasesFile.UPDATE_DOC, new Object[]{fileName, html, id});
            
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public CasesFileManager getCasesFileManager() {
        return casesFileManager;
    }
    public void setCasesFileManager(CasesFileManager casesFileManager) {
        this.casesFileManager = casesFileManager;
    }
}
