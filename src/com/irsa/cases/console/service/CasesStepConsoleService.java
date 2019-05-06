package com.irsa.cases.console.service;

import java.util.List;
import java.util.Map;

public interface CasesStepConsoleService {
    Map<String, Object> getList(String casesId);
    
    Map<String, Object> save(String accountId, String id, String casesId, String mode, String state, String limitDay, 
            String text1, String text2, String text3, String text4, String text5, String text6, String text7, String radio1, 
            List<String> regulationsList, String firstPersonnel, String secondPersonnel, String html1, String html2, String html3, String stateRefine);
    
    Map<String, Object> getMap(String accountId, String casesId, String state);
}
