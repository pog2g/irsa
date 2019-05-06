package com.irsa.cases.console.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CasesConsoleService {
    Map<String, Object> getDraftList(String accountId, String mode, String page);
    
    Map<String, Object> draft(String id, String type, String yearNo, String no, String administrativeCategoryId, String reasonId, String reasonAnother, 
            String specificBehavior, String applyRequest, String factsReasons, String applyMode, String applyModeAnother, String applyTime, String labels, String accountId, String [] requestCategoryArr);
    
    Map<String, Object> deleteDraft(String id, String mode);
    
    Map<String, Object> existDraft(String id, String mode);
    
    Map<String, Object> getDraft(String id);
    int getSwitchState(String id);
    
    /**
     * 
     * @param accountId
     * @param page
     * @param no
     * @param apply
     * @param defendant
     * @param state
     * @param applyTime
     * @param key
     * @param mode 案件阶段，空=全部阶段，1=受理阶段，2=受理中转阶段，3=受理结案阶段，4=审理阶段，5=审理结案阶段，6=审批阶段
     * @return
     */
    Map<String, Object> getList(String accountId, String page, String yearNo, String no, String apply, String defendant, String state, String applyTime1, String applyTime2, String key, String mode);
    List<Map<String, Object>> getCasesList(String accountId, String page, String yearNo, String no, String apply, String defendant, String state, String applyTime1, String applyTime2, String key, String mode);
    
    Map<String, Object> save(String id, String type, String yearNo, String no, String administrativeCategoryId, String reasonId, String reasonAnother, 
            String specificBehavior, String applyRequest, String factsReasons, String applyMode, String applyModeAnother, String applyTime, String labels, String html, String accountId, String [] requestCategoryArr);
    
    Map<String, Object> getMap(String id, String isWithPersonnel);
    
    Map<String, Object> saveContent(String type, String id, String content);
    
    Map<String, Object> saveLabels(String id, String labels);
    
    Map<String, Object> getLabelList(String casesId);
    
    Map<String, Object> total(HttpServletRequest request);
    
    Map<String, Object> createPDF(String casesId);
}
