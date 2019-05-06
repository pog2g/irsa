package com.irsa.cases.manager;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.model.Party;
import com.jdbc.manager.BaseManager;

@Service
public class PartyManager extends BaseManager {
    
    public Map<String, Object> getMap(String id) {
        Map<String, Object> data = getMap(Party.SELECT_BY_ID, new Object[]{id});
        getFileUrl(data);
        return data;
    }
    
    public static void getFileUrl(Map<String, Object> data) {
        if (data != null && StringUtils.isNotBlank(Utils.toString(data.get("id_file")))) {
            data.put("id_file", Utils4File.getFileUrl(Utils4File.PATH_PARTY, data.get("id_file").toString()));
        }
    }
}
