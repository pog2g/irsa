package com.irsa.cases.console.service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.CasesFileManager;
import com.irsa.cases.manager.CasesManager;
import com.irsa.cases.manager.CasesStepManager;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.CasesFile;
import com.irsa.cases.model.CasesPersonnel;
import com.irsa.cases.model.CasesStep;
import com.irsa.cases.model.CasesStepFile;
import com.irsa.cases.model.CasesStepRegulations;
import com.irsa.cases.model.CasesTime;
import com.irsa.cases.model.TempFile;

@Service
public class CasesStepConsoleServiceImpl implements CasesStepConsoleService {
    Logger log = LoggerFactory.getLogger(CasesStepConsoleServiceImpl.class);
    @Autowired
    CasesFileManager casesFileManager;
    @Autowired
    CasesStepManager casesStepManager;
    @Autowired
    CasesManager casesManager;
    
    @Override
    public Map<String, Object> getList(String casesId) {
        try {
            List<Map<String, Object>> casesStepList = new ArrayList<Map<String,Object>>(); 
            Map<String, Object> cases =  casesStepManager.getMap(Cases.SELECT_BY_ID, new Object[]{casesId});
            if (cases != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", "-1");
                data.put("state", "-1");
                data.put("create_time", cases.get("apply_time"));
                data.put("label_account", "");
                data.put("label_department", "");
                
                casesStepList.add(data);
                data.put("id", "0");
                data = new HashMap<String, Object>();
                data.put("state", "0");
                data.put("create_time", cases.get("create_time"));
                data.put("label_account", cases.get("create_account"));
                data.put("label_department", cases.get("create_department"));
                casesStepList.add(data);
            }
            List<Map<String, Object>> list = casesStepManager.getList(CasesStep.SELECT_BY_CASESID_MODE, new Object[]{casesId, CasesStep.MODE_1});
            if (list != null && list.size() > 0) {
              for (Map<String, Object> data : list) {
                  casesStepList.add(data);
              }  
            }
            return Utils.getSuccessMap(casesStepList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    void saveCasesStepRegulations(String stepId, List<String> regulationsList) {
    	casesStepManager.executeSQL(CasesStepRegulations.DELETE_BY_RESID, new Object[]{stepId});
        if (regulationsList != null && regulationsList.size() > 0) {
            for(String regulations : regulationsList) {
                casesStepManager.executeSQL(CasesStepRegulations.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, regulations});
            }
        }
    }
    
    void saveCasesStep(String accountId, String stepId, String casesId, String mode, String state, String limitDay, 
            String text1, String text2, String text3, String text4, String text5, String text6, String text7, String radio1,  
            List<String> regulationsList, String firstPersonnel, String secondPersonnel, String html1, String html2, String html3, 
            String refine) {
    	casesStepManager.executeSQL(CasesStep.INSERT, new Object[]{stepId, Utils.getCreateTime(), accountId, casesId, mode, state,
                limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, firstPersonnel, secondPersonnel, refine});
        casesStepManager.executeSQL(Cases.UPDATE_STATE, new Object[]{state, casesId});
        if (Cases.STATE_10.equals(state)) {
            casesStepManager.executeSQL(CasesStep.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), accountId, casesId, mode, Cases.STATE_12,
                    limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, firstPersonnel, secondPersonnel, ""});
            casesStepManager.executeSQL(Cases.UPDATE_STATE, new Object[]{Cases.STATE_12, casesId});
        }
        saveCasesStepRegulations(stepId, regulationsList);
        //删除草稿
        casesStepManager.executeSQL(CasesStep.DELETE_BY_MODE_CASESID, new Object[]{CasesStep.MODE_2, casesId});
    }
    
    @Override
    public Map<String, Object> save(String accountId, String id, String casesId, String mode, String state, String limitDay, 
            String text1, String text2, String text3, String text4, String text5, String text6, String text7, String radio1,  
            List<String> regulationsList, String firstPersonnel, String secondPersonnel, String html1, String html2, String html3, 
            String refine) {
        try {
            if (StringUtils.isBlank(casesId)) {
                return Utils.getErrorMap("案件已不存在");
            }
            Map<String, Object> cases = casesStepManager.getMap(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            if (cases == null) {
                return Utils.getErrorMap("案件已不存在");
            }
            
            if (Cases.STATE_3.equals(cases.get("state")) || Cases.STATE_4.equals(cases.get("state")) || Cases.STATE_5.equals(cases.get("state")) || 
            		Cases.STATE_6.equals(cases.get("state")) || Cases.STATE_7.equals(cases.get("state")) || Cases.STATE_8.equals(cases.get("state")) ||
            		Cases.STATE_18.equals(cases.get("state")) || Cases.STATE_19.equals(cases.get("state")) ||
            		Cases.STATE_17.equals(cases.get("state")) || Cases.STATE_21.equals(cases.get("state")) || Cases.STATE_22.equals(cases.get("state")) || 
            		Cases.STATE_23.equals(cases.get("state")) || Cases.STATE_24.equals(cases.get("state")) || Cases.STATE_25.equals(cases.get("state")) || Cases.STATE_26.equals(cases.get("state")) ||
            		Cases.STATE_15.equals(cases.get("state")) || Cases.STATE_16.equals(cases.get("state"))) {
                return Utils.getErrorMap("案件已结案");
            }
            
            
            // 保存草稿
            if (CasesStep.MODE_2.equals(mode)) {
            	Map<String, Object> data = casesStepManager.getMap(CasesStep.SELECT_EXIST_BY_ACCOUNTID_CASESID_MODE_STATE, new Object[]{accountId, casesId, CasesStep.MODE_2, state});
                if (data == null) {
                    casesStepManager.executeSQL(CasesStep.INSERT, new Object[]{id, Utils.getCreateTime(), accountId, casesId, mode, state,
                            limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, firstPersonnel, secondPersonnel, refine});
                } else {
                    id = Utils.toString(data.get("id"));
                    casesStepManager.executeSQL(CasesStep.UPDATE, new Object[]{limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, firstPersonnel, secondPersonnel, refine, id});
                }
                // 保存法条
                saveCasesStepRegulations(id, regulationsList);
                return Utils.getSuccessMap(null);
            }
            
            // 保存流程操作
            if (cases.get("state").equals(state)) {
                return Utils.getErrorMap("案件不能重复处理");
            }
            String stepId = Utils.getId();
            if (Cases.STATE_2.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写补正材料");
                }
                if (StringUtils.isBlank(limitDay)) {
                    return Utils.getErrorMap("请填写补正期限");
                }
                if (Integer.parseInt(limitDay) > 5) {
                    return Utils.getErrorMap("补正期限超过最大天数，最大5天");
                }
                
                List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String html = Utils.createDocument(html1, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "补正行政复议申请通知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_3.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写新行政复议机关");
                }
                
                List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String htmlTemp = html1;
                    if (applyList.size() > 0) {
                        if ("1".equals(apply.get("type"))) {
                            htmlTemp = html1.replaceAll("你们", "你");
                        } else {
                            htmlTemp = html1.replaceAll("你们", "你单位");
                        }
                    }
                    String html = Utils.createDocument(htmlTemp, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "行政复议告知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_4.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写接受转办的行政复议机关");
                }
                
                String sql = "select c.id,c.year_number,c.number,c.specific_behavior,DATE_FORMAT(c.apply_time,'%Y年%m月%d日') apply_time," +
                		"t.title,n.number_1,n.number_2,n.number_3,n.html," +
                		"(select GROUP_CONCAT(p.name separator '，') from irsa_cases_personnel p where p.personnel_type = '1' and p.cases_id = c.id) apply," +
                		"(select GROUP_CONCAT(p.name separator '，') from irsa_cases_personnel p where p.personnel_type = '2' and p.cases_id = c.id) defendant " +
                		"from irsa_cases c,irsa_console_account a,irsa_console_role r,irsa_department_document_number n,irsa_department_document_title t " +
                		"where c.entry_account_id = a.id and a.role_id = r.id and n.department_id = r.department_id and t.department_id = r.department_id " +
                		"and c.id = ? and n.state = ?";
                Map<String, Object> casesDocument1 = casesStepManager.getMap(sql, new Object[]{casesId, "9"});
                Map<String, Object> casesDocument2 = casesStepManager.getMap(sql, new Object[]{casesId, "10"});
                Map<String, Object> casesDocument3 = casesStepManager.getMap(sql, new Object[]{casesId, "11"});
                if (casesDocument1 == null || casesDocument2 == null || casesDocument3 == null) {
                    return Utils.getErrorMap(null);
                }
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String applyType = "你";
                    if ("2".equals(apply.get("type"))) {
                    	applyType = "你单位";
                    }
                	String document1 = MessageFormat.format(Utils.toString(casesDocument1.get("html")), 
                            casesDocument1.get("title"), casesDocument1.get("number_1"), casesDocument1.get("year_number"), casesDocument1.get("number_2"), casesDocument1.get("number"), casesDocument1.get("number_3"),
                            apply.get("name"), applyType, casesDocument1.get("defendant"), casesDocument1.get("specific_behavior"), casesDocument1.get("apply_time"), 
                            "《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》", applyType, applyType, sdf.format(new Date())); 
                    String fileName = casesFileManager.saveDocument(casesId, "行政复议受理通知书（"+apply.get("name")+"）", document1);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }
                List<Map<String, Object>> thirdPartList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_3});
                if (thirdPartList != null && thirdPartList.size() > 0) {
                    for (Map<String, Object> thirdPart : thirdPartList) {
                    	String document3 = MessageFormat.format(Utils.toString(casesDocument3.get("html")), 
                                casesDocument1.get("title"), casesDocument3.get("number_1"), casesDocument3.get("year_number"), casesDocument3.get("number_2"), casesDocument3.get("number"), casesDocument3.get("number_3"),
                                thirdPart.get("name"), casesDocument3.get("apply"), casesDocument3.get("defendant"), casesDocument3.get("specific_behavior"), casesDocument3.get("apply_time"),
                                "《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》", "《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》",
                                sdf.format(new Date())); 
                        String fileName = casesFileManager.saveDocument(casesId, "行政复议受理通知书-第三人（"+thirdPart.get("name")+"）", document3);
                        casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                    }
                }
                String document2 = MessageFormat.format(Utils.toString(casesDocument2.get("html")), 
                        casesDocument1.get("title"), casesDocument3.get("number_1"), casesDocument3.get("year_number"), casesDocument3.get("number_2"), casesDocument3.get("number"), casesDocument3.get("number_3"),
                        casesDocument2.get("defendant"), casesDocument2.get("apply"), casesDocument2.get("specific_behavior"), casesDocument2.get("apply_time"), 
                        "《中华人民共和国行政复议法》、《中华人民共和国行政复议法实施条例》", sdf.format(new Date()));
                String fileName2 = casesFileManager.saveDocument(casesId, "行政复议答复通知书（"+casesDocument2.get("defendant")+"）", document2);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName2});
                
                String fileName = casesFileManager.saveDocument(casesId, "行政复议案件转办函", html1);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_5.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写接受转送的行政复议机关");
                }
                String fileName = casesFileManager.saveDocument(casesId, "行政复议案件转送函", html1);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_6.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写不予受理理由和依据");
                }
                
                String fileName = casesFileManager.saveDocument(casesId, "不予受理行政复议申请决定书", html1);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_7.equals(state)) {
                if (!Cases.STATE_2.equals(cases.get("state"))) {
                    return Utils.getErrorMap("案件没有处在限期补正状态");
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_8.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写主动放弃申请的事由");
                }
                List<Map<String, Object>> fileList = casesStepManager.getList(TempFile.SELECT_BY_RESID, new Object[]{id});
                if (fileList == null || fileList.size() == 0) {
                    return Utils.getErrorMap("请添加放弃申请权利相关材料和证据");
                }
                
                for(Map<String, Object> file : fileList) {
                    casesFileManager.saveFile(casesId, CasesFile.MODE_1, file);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, file.get("file_name")});                            
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_9.equals(state)) {
                if (StringUtils.isBlank(firstPersonnel)) {
                    return Utils.getErrorMap("请选择主承办人");
                }
                if (StringUtils.isNotBlank(secondPersonnel) && firstPersonnel.equals(secondPersonnel)) {
                    return Utils.getErrorMap("主承办人和辅承办人不能为同一人");
                }
                //审理阶段暂时取消参考规定条例项
//                if (regulationsList == null || regulationsList.size() == 0) {
//                    return Utils.getErrorMap("请选择参考规定条例");
//                }
                if (StringUtils.isBlank(html1)) {
                    return Utils.getErrorMap("受理通知文书未生成");
                }
                if (StringUtils.isBlank(html2)) {
                    return Utils.getErrorMap("答复通知文书未生成");
                }
                if (StringUtils.isBlank(html3)) {
                    return Utils.getErrorMap("第三人受理通知文书未生成");
                }
                
                List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String htmlTemp = html1;
                    if (applyList.size() > 0) {
                        if ("1".equals(apply.get("type"))) {
                            htmlTemp = html1.replaceAll("你们", "你");
                        } else {
                            htmlTemp = html1.replaceAll("你们", "你单位");
                        }
                    }
                    String html = Utils.createDocument(htmlTemp, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "行政复议受理通知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }
                List<Map<String, Object>> thirdPartList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_3});
                if (thirdPartList != null && thirdPartList.size() > 0) {
                    for (Map<String, Object> thirdPart : thirdPartList) {
                        String fileName = casesFileManager.saveDocument(casesId, "行政复议受理通知书-第三人（"+thirdPart.get("name")+"）", Utils.createDocument(html3, Utils.toString(thirdPart.get("name"))));
                        casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                    }
                }
                Map<String, Object> defendant = casesStepManager.getMap(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_2});
                String fileName = casesFileManager.saveDocument(casesId, "行政复议答复通知书（"+defendant.get("name")+"）", Utils.createDocument(html2, Utils.toString(defendant.get("name"))));
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                // 案件时限
                String nowTime = Utils.SDF_YYYYMMDD.format(new Date());
                for (int i=0;i<7;i++) {
                    nowTime = Utils.modifyDate("+", "D", 1, nowTime);
                    while (!casesManager.isWorkDate(nowTime)) {
                        nowTime = Utils.modifyDate("+", "D", 1, nowTime);
                    }
                }
                nowTime = Utils.modifyDate("+", "D", 10, nowTime);
                casesStepManager.executeSQL(CasesTime.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesId, 2, nowTime});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_10.equals(state)) {
                if (!Cases.STATE_9.equals(cases.get("state"))) {
                    return Utils.getErrorMap("案件已被处理，请返回列表刷新");
                }
                
                casesManager.executeSQL(Cases.UPDATE_REPLY, new Object[]{"被申请人未按照《中华人民共和国行政复议法》第二十三条的规定：提出书面答复，并提交当初作出具体行政行为的证据、依据和其他有关材料。", null, Cases.STATE_10, casesId});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_27.equals(state)) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            	String sql = "select c.id,c.year_number,c.number,c.specific_behavior,DATE_FORMAT(c.apply_time,'%Y年%m月%d日') apply_time," +
                		"t.title,n.number_1,n.number_2,n.number_3,n.html," +
                		"(select GROUP_CONCAT(p.name separator '，') from irsa_cases_personnel p where p.personnel_type = '2' and p.cases_id = c.id) defendant " +
                		"from irsa_cases c,irsa_console_account a,irsa_console_role r,irsa_department_document_number n,irsa_department_document_title t " +
                		"where c.entry_account_id = a.id and a.role_id = r.id and n.department_id = r.department_id and t.department_id = r.department_id " +
                		"and c.id = ? and n.state = ?";
                Map<String, Object> casesDocument15 = casesStepManager.getMap(sql, new Object[]{casesId, "15"});
                String document15 = MessageFormat.format(Utils.toString(casesDocument15.get("html")), 
                		casesDocument15.get("title"), casesDocument15.get("number_1"), casesDocument15.get("year_number"), casesDocument15.get("number_2"), casesDocument15.get("number"), casesDocument15.get("number_3"),
                		casesManager.toHtml4Party(casesId, CasesPersonnel.PERSONNEL_TYPE_1), casesManager.toHtml4Party(casesId, CasesPersonnel.PERSONNEL_TYPE_3), casesManager.toHtml4Defendant(casesId), 
                		casesDocument15.get("specific_behavior"), casesDocument15.get("apply_time"),
                		"行政复议期间，申请人自愿撤回行政复议申请。", 
                		"根据《中华人民共和国行政复议法实施条例》第三十八条第一款规定，结合本案实际情况，本机关同意其撤回行政复议申请。根据 《中华人民共和国行政复议法》第二十五条之规定，",
                        sdf.format(new Date())); 
                String fileName = casesFileManager.saveDocument(casesId, "行政复议终止决定书", document15);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                
                saveCasesStep(accountId, stepId, casesId, mode, Cases.STATE_15, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_12.equals(state)) {
                if (Cases.STATE_9.equals(cases.get("state"))) {
                    return Utils.getErrorMap("被申请人未答复，案件不能提交");
                }
                if (!Cases.STATE_11.equals(cases.get("state"))) {
                    return Utils.getErrorMap("案件已被处理，请返回列表刷新");
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_13.equals(state)) {
                if (regulationsList == null || regulationsList.size() == 0) {
                    return Utils.getErrorMap("请选择参考规定条例");
                }
                if (StringUtils.isBlank(limitDay)) {
                    return Utils.getErrorMap("请填写延期天数");
                }
                if (Integer.parseInt(limitDay) > 30) {
                    return Utils.getErrorMap("延期天数超过最大天数，最大30天");
                }
                
                List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String htmlTemp = html1;
                    if (applyList.size() > 0) {
                        if ("1".equals(apply.get("type"))) {
                            htmlTemp = html1.replaceAll("你们", "你");
                        } else {
                            htmlTemp = html1.replaceAll("你们", "你单位");
                        }
                    } 
                    String html = Utils.createDocument(htmlTemp, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "延期审理行政复议通知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            } 
            if (Cases.STATE_14.equals(state)) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写中止审查的事由");
                }
                
                List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String htmlTemp = html1;
                    if (applyList.size() > 0) {
                        if ("1".equals(apply.get("type"))) {
                            htmlTemp = html1.replaceAll("你们", "你");
                        } else {
                            htmlTemp = html1.replaceAll("你们", "你单位");
                        }
                    } 
                    String html = Utils.createDocument(htmlTemp, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "行政复议中止通知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_18.equals(state) || Cases.STATE_19.equals(state) || 
                    Cases.STATE_17.equals(state) || Cases.STATE_21.equals(state) || Cases.STATE_22.equals(state) || 
                    Cases.STATE_23.equals(state) || Cases.STATE_24.equals(state) || Cases.STATE_25.equals(state) || Cases.STATE_26.equals(state) ) {
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写申请人请求");
                }
                if (StringUtils.isBlank(text2)) {
                    return Utils.getErrorMap("请填写申请人称");
                }
                if (StringUtils.isBlank(text3)) {
                    return Utils.getErrorMap("请填写被申请人称");
                }
                if (StringUtils.isBlank(text5)) {
                    return Utils.getErrorMap("请填写经审理查明");
                }
                if (StringUtils.isBlank(text6)) {
                    return Utils.getErrorMap("请填写本机关认为");
                }
                if ((Cases.STATE_17.equals(state) || Cases.STATE_21.equals(state) || Cases.STATE_22.equals(state) || 
                        Cases.STATE_23.equals(state) || Cases.STATE_24.equals(state) || Cases.STATE_25.equals(state) || 
                        Cases.STATE_26.equals(state)) && StringUtils.isBlank(text7)) {
                    return Utils.getErrorMap("请填写决定");
                }
                if (Cases.STATE_18.equals(state) && StringUtils.isBlank(radio1)) {
                    return Utils.getErrorMap("请选择法律依据");
                }
                
                String fileName = casesFileManager.saveDocument(casesId, "行政复议决定书", html1);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_15.equals(state) || Cases.STATE_16.equals(state)) {
                if (StringUtils.isBlank(radio1)) {
                    return Utils.getErrorMap("请选择终止类型");
                }
                if (StringUtils.isBlank(text1)) {
                    return Utils.getErrorMap("请填写终止审查的事由");
                }
                List<Map<String, Object>> fileList = casesStepManager.getList(TempFile.SELECT_BY_RESID, new Object[]{id});
                if (fileList == null || fileList.size() == 0) {
                    if (Cases.STATE_15.equals(state)) {
                    	return Utils.getErrorMap("请添加撤回申请书");
                    } else {
                    	return Utils.getErrorMap("请添加相关证据材料");
                    }
                }
                
                for(Map<String, Object> file : fileList) {
                    casesFileManager.saveFile(casesId, CasesFile.MODE_1, file);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, file.get("file_name")});                            
                }
                String fileName = casesFileManager.saveDocument(casesId, "行政复议终止决定书", html1);
                casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                
                saveCasesStep(accountId, stepId, casesId, mode, state, limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_28.equals(state)) {
                
                /*List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String htmlTemp = html1;
                    if (applyList.size() > 0) {
                        if ("1".equals(apply.get("type"))) {
                            htmlTemp = html1.replaceAll("你们", "你");
                        } else {
                            htmlTemp = html1.replaceAll("你们", "你单位");
                        }
                    } 
                    String html = Utils.createDocument(htmlTemp, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "行政复议中止通知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }*/
                
                saveCasesStep(accountId, stepId, casesId, mode, "16", limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
                return Utils.getSuccessMap(null);
            }
            if (Cases.STATE_29.equals(state)) {
            	
            	/*List<Map<String, Object>> applyList = casesStepManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
                for (Map<String, Object> apply : applyList) {
                    String htmlTemp = html1;
                    if (applyList.size() > 0) {
                        if ("1".equals(apply.get("type"))) {
                            htmlTemp = html1.replaceAll("你们", "你");
                        } else {
                            htmlTemp = html1.replaceAll("你们", "你单位");
                        }
                    } 
                    String html = Utils.createDocument(htmlTemp, Utils.toString(apply.get("name")));
                    String fileName = casesFileManager.saveDocument(casesId, "行政复议中止通知书（"+apply.get("name")+"）", html);
                    casesStepManager.executeSQL(CasesStepFile.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), stepId, fileName});
                }*/
            	
            	saveCasesStep(accountId, stepId, casesId, mode, "12", limitDay, text1, text2, text3, text4, text5, text6, text7, radio1, regulationsList, firstPersonnel, secondPersonnel, html1, html2, html3, refine);
            	return Utils.getSuccessMap(null);
            }
            return Utils.getErrorMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getMap(String accountId, String casesId, String state) {
        try {
            Map<String, Object> data = casesStepManager.getMap(CasesStep.SELECT_BY_ACCOUNTID_CASESID_MODE_STATE, new Object[]{accountId, casesId, CasesStep.MODE_2, state});
            if ((Cases.STATE_17.equals(state) || Cases.STATE_21.equals(state) || Cases.STATE_22.equals(state) || Cases.STATE_23.equals(state) || Cases.STATE_24.equals(state) || Cases.STATE_25.equals(state) || Cases.STATE_26.equals(state) ||
                    Cases.STATE_18.equals(state) || Cases.STATE_19.equals(state)) && data == null) {
                Map<String, Object> cases = casesStepManager.getMap(Cases.SELECT_BY_ID, new Object[]{casesId});
                if (cases != null) {
                    data = new HashMap<String, Object>();
                    data.put("id", Utils.getId());
                    data.put("text_1", cases.get("apply_request"));
                    data.put("text_2", cases.get("facts_reasons"));
                    data.put("text_3", cases.get("defendant_reply"));
                    data.put("state_refine", "");
                }
            }
            return Utils.getSuccessMap(data);
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
    public CasesStepManager getCasesStepManager() {
        return casesStepManager;
    }
    public void setCasesStepManager(CasesStepManager casesStepManager) {
        this.casesStepManager = casesStepManager;
    }
    public CasesManager getCasesManager() {
        return casesManager;
    }
    public void setCasesManager(CasesManager casesManager) {
        this.casesManager = casesManager;
    }
}
