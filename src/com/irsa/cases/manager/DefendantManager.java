package com.irsa.cases.manager;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.model.Defendant;
import com.irsa.cases.model.DefendantFile;
import com.jdbc.manager.BaseManager;

@Service
public class DefendantManager extends BaseManager {
    
    public Map<String, Object> getMap(String id) {
        Map<String, Object> data = getMap(Defendant.SELECT_BY_ID, new Object[]{id});
        getFileUrl(data);
        return data;
    }
    
    public static void getFileUrl(Map<String, Object> data) {
        if (data != null) {
            if (data.get("file_1") != null && !"".equals(data.get("file_1").toString().trim())) {
                data.put("file_1", Utils4File.getFileUrl(Utils4File.PATH_DEFENDANT, data.get("file_1").toString()));
            }
            if (data.get("file_2") != null && !"".equals(data.get("file_2").toString().trim())) {
                data.put("file_2", Utils4File.getFileUrl(Utils4File.PATH_DEFENDANT, data.get("file_2").toString()));
            }
            if (data.get("file_3") != null && !"".equals(data.get("file_3").toString().trim())) {
                data.put("file_3", Utils4File.getFileUrl(Utils4File.PATH_DEFENDANT, data.get("file_3").toString()));
            }
        }
    }
    
    public void saveFile(String id, String mode, Map<String, Object> tempFile) {
        executeSQL(DefendantFile.DELETE_BY_RESID_MODE, new Object[]{id, mode});
        Utils4File.copyTempToReal(Utils4File.PATH_DEFENDANT, tempFile.get("file_name").toString());
        executeSQL(DefendantFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), mode, 
                tempFile.get("type"), id, tempFile.get("real_name"), tempFile.get("size"), tempFile.get("ext"), tempFile.get("file_name")});
    }
}
