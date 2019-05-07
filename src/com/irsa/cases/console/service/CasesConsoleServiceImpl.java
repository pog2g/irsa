package com.irsa.cases.console.service;

import com.common.utils.Utils;
import com.common.utils.Utils4PDF;
import com.irsa.cases.manager.CasesFileManager;
import com.irsa.cases.manager.CasesManager;
import com.irsa.cases.manager.CasesPersonnelManager;
import com.irsa.cases.manager.RequestCategoryManager;
import com.irsa.cases.model.*;
import com.irsa.console.model.Account;
import com.jdbc.utils.Utils4JDBC;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CasesConsoleServiceImpl implements CasesConsoleService {
    Logger log = LoggerFactory.getLogger(CasesConsoleServiceImpl.class);
    @Autowired
    CasesManager casesManager;
    @Autowired
    CasesFileManager casesFileManager;
    @Autowired
    CasesPersonnelManager casesPersonnelManager;
    @Autowired
    RequestCategoryManager requestCategoryManager;

    @Override
    public Map<String, Object> getDraftList(String accountId, String mode, String page) {
        try {
            if ("1".equals(mode)) {
                return Utils.getSuccessMap(casesManager.getPageList(CasesTemp.SELECT_BY_ENTRYACCOUNTID, new Object[]{accountId}, Integer.parseInt(page), 10, "data_list"));
            }
            String sql = "";
            if ("2".equals(mode)) {
                sql = CasesStep.SELECT_BY_MODE_ACCOUNTID_2;
            } else {
                sql = CasesStep.SELECT_BY_MODE_ACCOUNTID_3;
            }
            Map<String, Object> result = casesManager.getPageList(sql, new Object[]{CasesStep.MODE_2, accountId}, Integer.parseInt(page), 10, "data_list");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("data_list");
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    Map<String, Object> cases = casesManager.getMap(Cases.SELECT_BY_ID, new Object[]{data.get("cases_id")});
                    if (cases != null) {
                        data.put("year_no", cases.get("year_no"));
                        data.put("no", cases.get("no"));
                        data.put("label_type", cases.get("label_type"));
                        data.put("label_administrative_category", cases.get("label_administrative_category"));
                        data.put("label_reason", cases.get("label_reason"));
                        data.put("apply", cases.get("apply"));
                        data.put("apply_third_party", cases.get("apply_third_party"));
                        data.put("third_party", cases.get("third_party"));
                        data.put("defendant", cases.get("defendant"));
                        data.put("apply_time", cases.get("apply_time"));
                    }
                }
            }
            return Utils.getSuccessMap(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> draft(String id, String type, String yearNo, String no, String administrativeCategoryId, String reasonId, String reasonAnother, String specificBehavior, String applyRequest, String factsReasons,
                                     String applyMode, String applyModeAnother, String applyTime, String labels, String accountId, String[] requestCategoryArr) {
        try {
            if (StringUtils.isBlank(accountId)) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            int num = casesManager.getListCount(Account.SELECT_EXIST_ID, new Object[]{accountId});
            if (num == 0) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            if (StringUtils.isBlank(id)) {
                return Utils.getErrorMap("案件数据不存在，请重试");
            }
            if (StringUtils.isBlank(no) || "-".equals(no)) {
                return Utils.getErrorMap("请填写案件编号");
            }

            num = casesManager.getListCount(CasesTemp.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                casesManager.executeSQL(CasesTemp.INSERT, new Object[]{Utils.getCreateTime(), id, type, yearNo, no, administrativeCategoryId, reasonId, reasonAnother, specificBehavior, applyRequest, factsReasons,
                        applyMode, applyModeAnother, StringUtils.isBlank(applyTime) ? Utils.getCreateTime() : applyTime, labels, accountId});
            } else {
                casesManager.executeSQL(CasesTemp.UPDATE, new Object[]{yearNo, no, administrativeCategoryId, reasonId, reasonAnother, specificBehavior, applyRequest, factsReasons,
                        applyMode, applyModeAnother, StringUtils.isBlank(applyTime) ? Utils.getCreateTime() : applyTime, labels, id});
            }

            // 保存请求类别
            updateCasesRequestCategory(id, requestCategoryArr);

            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> deleteDraft(String id, String mode) {
        try {
            if ("1".equals(mode)) {
                casesManager.executeSQL(CasesTemp.DELETE, new Object[]{id});
                return Utils.getSuccessMap(null);
            }
            casesManager.executeSQL(CasesStep.DELETE_BY_ID, new Object[]{id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> existDraft(String id, String mode) {
        try {
            if ("1".equals(mode)) {
                int num = casesManager.getListCount(CasesTemp.SELECT_EXIST_BY_ID, new Object[]{id});
                if (num == 0) {
                    return Utils.getErrorMap("草稿已不存在，请刷新");
                }
                return Utils.getSuccessMap(null);
            }
            int num = casesManager.getListCount(CasesStep.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                return Utils.getErrorMap("草稿已不存在，请刷新");
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> getDraft(String id) {
        try {
            Map<String, Object> data = casesManager.getMap(CasesTemp.SELECT_BY_ID, new Object[]{id});
            if (data == null) {
                return Utils.getErrorMap(null);
            }
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public int getSwitchState(String id) {
        int count = 0;
        try {
            count = casesManager.getListCount(CasesTemp.SELECT_SWITCH_STATE_BY_ID, new Object[]{id});
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return count;
        }
    }

    @Override
    public Map<String, Object> getList(String accountId, String page, String yearNo, String no, String apply, String defendant, String state, String applyTime1, String applyTime2, String key, String mode) {
        try {
            Map<String, Object> account = casesManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            Map<String, Object> resultMap = this.getListSql(accountId, page, yearNo, no, apply, defendant, state, applyTime1, applyTime2, key, mode, account);
            String sql = resultMap.get("sql").toString();
            Object[] param = (Object[]) resultMap.get("param");
            return Utils.getSuccessMap(casesManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public Map<String, Object> getListSql(String accountId, String page, String yearNo, String no, String apply, String defendant, String state, String applyTime1, String applyTime2, String key, String mode, Map<String, Object> account) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(Cases.SELECT);
        sql.append("where 1=1 ");
        Object[] param = null;
        if (!"1".equals(account.get("department_id"))) {
            sql.append("and c.create_department_id  = ? ");
            param = new Object[]{account.get("department_id")};
        }
        if (Cases.MODE_1.equals(mode)) {
            sql.append("and (c.state = ? or c.state = ?) ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{Cases.STATE_1, Cases.STATE_2});
        } else if (Cases.MODE_2.equals(mode)) {
            sql.append("and (c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ?  or c.state = ?) ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{Cases.STATE_3, Cases.STATE_4, Cases.STATE_5, Cases.STATE_6, Cases.STATE_7, Cases.STATE_8});
        } else if (Cases.MODE_3.equals(mode)) {
            sql.append("and (c.state = ? or c.state = ? or c.state = ? or c.state = ?  ) ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{Cases.STATE_9, Cases.STATE_10, Cases.STATE_11, Cases.STATE_32,});
        } else if (Cases.MODE_4.equals(mode)) {
            sql.append("and (c.state = ? or c.state = ? or c.state = ? ) ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{Cases.STATE_12, Cases.STATE_13, Cases.STATE_14,});
        } else if (Cases.MODE_5.equals(mode)) {
            sql.append("and (c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ?) ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{Cases.STATE_17, Cases.STATE_21, Cases.STATE_22, Cases.STATE_23, Cases.STATE_24, Cases.STATE_25, Cases.STATE_26, Cases.STATE_18, Cases.STATE_19, Cases.STATE_15, Cases.STATE_16});
        } else if (Cases.MODE_6.equals(mode)) {
            sql.append("and (c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ? or c.state = ?) ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{Cases.STATE_3, Cases.STATE_4, Cases.STATE_5, Cases.STATE_6, Cases.STATE_7, Cases.STATE_8,
                    Cases.STATE_17, Cases.STATE_21, Cases.STATE_22, Cases.STATE_23, Cases.STATE_24, Cases.STATE_25, Cases.STATE_26,
                    Cases.STATE_18, Cases.STATE_19, Cases.STATE_15, Cases.STATE_16});
        }

        if (StringUtils.isNotBlank(yearNo) && !"-1".equals(yearNo)) {
            sql.append("and c.year_no = ? ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{yearNo});
        }
        if (StringUtils.isNotBlank(no)) {
            sql.append("and c.no = ? ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{no});
        }
        if (StringUtils.isNotBlank(state) && !"-1".equals(state)) {
            sql.append("and c.state = ? ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{state});
        }
        if (StringUtils.isNotBlank(applyTime1)) {
            sql.append("and c.apply_time >= ? ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{applyTime1});
        }
        if (StringUtils.isNotBlank(applyTime2)) {
            sql.append("and c.apply_time <= ? ");
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{applyTime2});
        }
        sql.append("order by c.createtime desc ");
        resultMap.put("sql", sql);
        resultMap.put("param", param);
        return resultMap;
    }

    /**
     * 导出功能获取数据list
     **/
    public List<Map<String, Object>> getCasesList(String accountId, String page, String yearNo, String no, String apply, String defendant, String state, String applyTime1, String applyTime2, String key, String mode) {
        List<Map<String, Object>> list = null;
        Map<String, Object> account = casesManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
        Map<String, Object> resultMap = this.getListSql(accountId, page, yearNo, no, apply, defendant, state, applyTime1, applyTime2, key, mode, account);
        String sql = resultMap.get("sql").toString();
        Object[] param = (Object[]) resultMap.get("param");
        list = casesManager.getList(sql, param);
        return list;
    }

    @Override
    public Map<String, Object> save(String id, String type, String yearNo, String no, String administrativeCategoryId, String reasonId, String reasonAnother,
                                    String specificBehavior, String applyRequest, String factsReasons, String applyMode, String applyModeAnother, String applyTime, String labels, String html, String accountId, String[] requestCategoryArr) {
        try {
            Map<String, Object> account = casesManager.getMap(Account.SELECT_BY_ID, new Object[]{accountId});
            if (account == null) {
                return Utils.getErrorMap("登陆已过期，请重新登陆");
            }
            if (StringUtils.isBlank(id)) {
                return Utils.getErrorMap("案件数据不存在，请重试");
            }
            if (StringUtils.isBlank(no)) {
                return Utils.getErrorMap("请填写案件编号");
            }
            int num = casesManager.getListCount(Cases.SELECT_EXIST_BY_NO, new Object[]{account.get("department_id"), yearNo, no});
            if (num != 0) {
                return Utils.getErrorMap("案件编号已存在");
            }
            if (StringUtils.isBlank(administrativeCategoryId) || "-1".equals(administrativeCategoryId)) {
                return Utils.getErrorMap("请选择行政分类");
            }
            if (StringUtils.isBlank(reasonId) || "-1".equals(reasonId)) {
                return Utils.getErrorMap("请选择案由");
            }
            if ("-2".equals(reasonId)) {
                if (StringUtils.isBlank(reasonAnother)) {
                    return Utils.getErrorMap("请填写案由其他备注");
                }

                Map<String, Object> data = casesManager.getMap(CasesReason.SELECT_BY_TITLE, new Object[]{reasonAnother});
                if (data == null) {
                    reasonId = Utils.getId();
                    num = casesManager.getListCount(CasesReason.SELECT_ALL, null);
                    casesManager.executeSQL(CasesReason.INSERT, new Object[]{reasonId, Utils.getCreateTime(), num + 1, reasonAnother});
                } else {
                    reasonId = data.get("id").toString();
                }
            }

            if (requestCategoryArr.length < 0 || "-1".equalsIgnoreCase(requestCategoryArr[0])) {
                return Utils.getErrorMap("请选择请求类别");
            }
            if (StringUtils.isBlank(specificBehavior)) {
                return Utils.getErrorMap("请填写被诉具体行政行为");
            }
            if (StringUtils.isBlank(applyRequest)) {
                return Utils.getErrorMap("请填写行政复议请求");
            }
            if (StringUtils.isBlank(factsReasons)) {
                return Utils.getErrorMap("请填写事实和理由");
            }
            if (StringUtils.isBlank(applyMode) || (!Cases.APPLY_MODE_1.equals(applyMode) && !Cases.APPLY_MODE_2.equals(applyMode) && !Cases.APPLY_MODE_3.equals(applyMode) && !Cases.APPLY_MODE_4.equals(applyMode))) {
                return Utils.getErrorMap("请选择申请方式");
            }
            if (Cases.APPLY_MODE_3.equals(applyMode)) {
                if (StringUtils.isBlank(applyModeAnother)) {
                    return Utils.getErrorMap("请填写申请方式其他备注");
                }
            }
            if (StringUtils.isBlank(applyTime)) {
                return Utils.getErrorMap("请填写申请时间");
            }
            if (StringUtils.isBlank(labels)) {
                return Utils.getErrorMap("请填写案件标签");
            }
            List<String> labelList = initLabelList(labels);
            if (labelList.size() == 0) {
                return Utils.getErrorMap("请填写案件标签");
            }

            // 案件相关
            // 申请人
            List<Map<String, Object>> applyTempList = casesManager.getList(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_TYPE, new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_1});
            if (applyTempList.size() == 0) {
                return Utils.getErrorMap("请添加申请人");
            }
            // 申请人代表
            num = casesManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_REPRESENTATIVE, new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_1, CasesPersonnel.REPRESENTATIVE_1});
            // 申请人代表推选凭证
            List<Map<String, Object>> representativeFileTempList = null;
            if (num > 0) {
                representativeFileTempList = casesManager.getList(TempFile.SELECT_BY_RESID_MODE, new Object[]{id, CasesFile.MODE_9});
                if (representativeFileTempList.size() == 0) {
                    return Utils.getErrorMap("请添加申请人代表推选凭证");
                }
            }
            // 被申请人
            Map<String, Object> defendantTemp = casesManager.getMap(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_TYPE, new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_2});
            if (defendantTemp == null) {
                return Utils.getErrorMap("请添加被申请人");
            }

            // 申请人证据信息
            List<Map<String, Object>> applyFileTempList = casesManager.getList(TempFile.SELECT_BY_RESID_MODE, new Object[]{id, CasesFile.MODE_1});
            if (applyFileTempList.size() == 0) {
                return Utils.getErrorMap("请添加申请人证据信息");
            }

            // 保存申请人
            for (Map<String, Object> apply : applyTempList) {
                List<Map<String, Object>> agentList = casesManager.getList(CasesPersonnelTemp.SELECT_BY_CASESID_CLIENTID, new Object[]{id, apply.get("id")});
                if (agentList != null && agentList.size() > 0) {
                    for (Map<String, Object> agent : agentList) {
                        Map<String, Object> agentFile = casesManager.getMap(TempFile.SELECT_BY_PERSONNELID, new Object[]{agent.get("id")});
                        agent.put("agent_file", agentFile);
                    }
                }
                casesPersonnelManager.save4Party(CasesPersonnel.PERSONNEL_TYPE_1, id, Utils.toString(apply.get("personnel_id")), Utils.toString(apply.get("representative")), agentList);
            }

            // 保存申请人申请人代表推选凭证
            if (representativeFileTempList != null && representativeFileTempList.size() > 0) {
                for (Map<String, Object> data : representativeFileTempList) {
                    casesFileManager.saveFile(id, CasesFile.MODE_9, data);
                }
            }
            // 保存（申请人建议追加）第三人
            List<Map<String, Object>> applyThirdPartyList = casesManager.getList(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_TYPE, new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_7});
            if (applyThirdPartyList != null && applyThirdPartyList.size() > 0) {
                for (Map<String, Object> data : applyThirdPartyList) {
                    casesPersonnelManager.save4Party(CasesPersonnel.PERSONNEL_TYPE_7, id, Utils.toString(data.get("personnel_id")), null, null);
                }
            }

            // 保存被申请人
            casesPersonnelManager.save4Defendant(id, Utils.toString(defendantTemp.get("personnel_id")));

            // 保存证据信息
            for (Map<String, Object> data : applyFileTempList) {
                casesFileManager.saveFile(id, CasesFile.MODE_1, data);
            }
            // 保存申请书
            casesFileManager.saveDocument(id, "行政复议申请书", html);

            // 保存请求类别
            updateCasesRequestCategory(id, requestCategoryArr);

            // 保存案件标签
            saveLabels(id, labelList);
            // 保存案件时限
            for (int i = 0; i < 3; i++) {
                String limitTime = applyTime;
                if (i == 0) {
                    for (int j = 0; j < 4; j++) {
                        limitTime = Utils.modifyDate("+", "D", 1, limitTime);
                        while (!casesManager.isWorkDate(limitTime)) {
                            limitTime = Utils.modifyDate("+", "D", 1, limitTime);
                        }
                    }
                    casesManager.executeSQL(CasesTime.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), id, i + 1, limitTime});
                    continue;
                }
                if (i == 1) {
                    continue;
                }
                limitTime = Utils.modifyDate("+", "D", 60, applyTime);
                casesManager.executeSQL(CasesTime.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), id, i + 1, limitTime,});
                continue;
            }
            // 保存案件
            String time = Utils.getCreateTime();
            casesManager.executeSQL(Cases.INSERT, new Object[]{time, id, Cases.STATE_1, type, yearNo, no, administrativeCategoryId, reasonId, specificBehavior, applyRequest, factsReasons,
                    applyMode, applyModeAnother, applyTime, Utils.modifyDate("+", "D", 60, applyTime), accountId});
            int defendantTempCount = casesManager.getListCount(CasesPersonnelTemp.SELECT_EXIST_BY_CASESID_TYPE, new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_2});
            //更新案件表被申请人个数，用于判断案件状态是否为部分被申请人答复
            if (defendantTempCount > 1) {
                casesManager.executeSQL(Cases.UPDATE_DEFENDANT_REPLY_NUM, new Object[]{defendantTempCount, id});
            }
            casesManager.executeSQL(Cases.UPDATE_LASTTIME, new Object[]{time, id});
            // 删除草稿
            num = casesManager.getListCount(CasesTemp.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num != 0) {
                casesManager.executeSQL(CasesTemp.DELETE, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            try {
                casesManager.executeSQL(Cases.DELETE, new Object[]{id});
                casesManager.executeSQL(CasesFile.DELETE, new Object[]{id});
                casesManager.executeSQL(CasesLabels.DELETE, new Object[]{id});
                List<Map<String, Object>> list = casesManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID, new Object[]{id});
                for (Map<String, Object> data : list) {
                    casesManager.executeSQL(CasesPersonnel.DELETE, new Object[]{data.get("id")});
                    casesManager.executeSQL(CasesPersonnelFile.DELETE_BY_RESID, new Object[]{data.get("id")});
                }
            } catch (Exception e2) {
                log.error(e.getMessage(), e2);
            }
            return Utils.getErrorMap(null);
        }
    }

    /**


     */
    /**
     * 说明：本方法用于保存请求类别
     * 1、先通过cases_id查询数据库中 请求类别id，如果没有数据，则直接插入前台传递的请求类别
     * 2、与前台传的请求类别进行比对：相同，不做操作；不同，将之前cases_id相关联的数据删除，再进行关系添加
     *
     * @author 小曾
     * @date 2019/4/22 0:28
     * @param:本案件的"cases_id"和”请求类别“
     * @return:
     */
    private void updateCasesRequestCategory(String id, String[] requestCategoryArr) {
        List<Map<String, Object>> requestCategorys = requestCategoryManager.getList(CasesRequestCategory.SELECT, new Object[]{id});
        // 如果数据库没有，说明是第一次添加，直接循环新增记录
        try {
            if (requestCategorys.size() == 0) {
                for (String requestCategory : requestCategoryArr) {
                    requestCategoryManager.executeSQL(CasesRequestCategory.INSERT, new Object[]{
                            Utils.getId(), id, requestCategory, Utils.getCreateTime()
                    });
                }
            } else {
                // 如果有数据，直接删除，
                requestCategoryManager.executeSQL(CasesRequestCategory.DELETE, new Object[]{id});
                // 然后再添加
                for (String requestCategory : requestCategoryArr) {
                    requestCategoryManager.executeSQL(CasesRequestCategory.INSERT, new Object[]{
                            Utils.getId(), id, requestCategory, Utils.getCreateTime()
                    });
                }
            }
        } catch (Exception e) {
            Utils.getErrorMap(null);
            e.printStackTrace();
        }
    }

    List<String> initLabelList(String labels) {
        List<String> labelList = new ArrayList<String>();
        String[] labelArr = labels.split("，");
        for (String str : labelArr) {
            boolean isAdd = StringUtils.isNotBlank(str) && !labelList.contains(str);
            if (isAdd) {
                labelList.add(str.trim());
            }
        }
        return labelList;
    }

    void saveLabels(String casesId, List<String> labelList) {
        casesManager.executeSQL(CasesLabels.DELETE, new Object[]{casesId});
        if (labelList == null || labelList.size() == 0) {
            return;
        }
        for (String str : labelList) {
            Map<String, Object> data = casesManager.getMap(Label.SELECT_BY_TITLE, new Object[]{str});
            String labelId = "";
            if (data == null) {
                labelId = Utils.getId();
                casesManager.executeSQL(Label.INSERT, new Object[]{labelId, Utils.getCreateTime(), str});
            } else {
                labelId = data.get("id").toString();
            }
            casesManager.executeSQL(CasesLabels.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), casesId, labelId});
        }
    }

    @Override
    public Map<String, Object> getMap(String id, String isWithPersonnel) {
        try {
            Map<String, Object> data = casesManager.getMap(Cases.SELECT_BY_ID, new Object[]{id});
            if (data == null) {
                return Utils.getErrorMap(null);
            }
            data.put("apply", Utils.toString(data.get("apply")).replaceAll(",", "、"));
            data.put("third_party", Utils.toString(data.get("third_party")).replaceAll(",", "、"));

            if ("1".equals(isWithPersonnel)) {
                data.put("apply_list", casesManager.getList(CasesPersonnel.SELECT_BY_CASESID_TYPE137 + "order by p.createtime,p.id", new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_1}));
                data.put("defendant_list", casesManager.getList(CasesPersonnel.SELECT_BY_CASESID_TYPE2, new Object[]{id}));
                data.put("third_party_list", casesManager.getList(CasesPersonnel.SELECT_BY_CASESID_TYPE137 + "order by p.createtime,p.id", new Object[]{id, CasesPersonnel.PERSONNEL_TYPE_3}));
            }

            if (StringUtils.isBlank(Utils.toString(data.get("limit_time_1"))) || StringUtils.isBlank(Utils.toString(data.get("limit_time_2"))) || StringUtils.isBlank(Utils.toString(data.get("limit_time_3")))) {
                return Utils.getSuccessMap(data);
            }

            Date nowDate = new Date();
            String nowTime = Utils.SDF_YYYYMMDD.format(nowDate);
            String limitTime = "";
            if ("1".equals(data.get("state"))) {
                limitTime = Utils.toString(data.get("limit_time_1"));
                Date limitDate = Utils.SDF_YYYYMMDD_HHMMSS.parse(limitTime + " 23:59:59");
                boolean isOver = nowDate.getTime() - limitDate.getTime() >= 0;
                if (isOver) {
                    data.put("is_over", "1");
                    data.put("limit_time", nowDate.getTime() - limitDate.getTime());
                } else {
                    data.put("is_over", "0");
                    if (casesManager.isWorkDate(nowTime)) {
                        String nextTime = nowTime;
                        int i = 0;
                        while (!nextTime.equals(limitTime)) {
                            nextTime = Utils.modifyDate("+", "D", 1, nextTime);
                            if (!casesManager.isWorkDate(nextTime)) {
                                i++;
                            }
                        }
                        data.put("limit_time", limitDate.getTime() - nowDate.getTime() - i * 1000 * 60 * 60 * 24);
                    } else {
                        String nextTime = nowTime;
                        nextTime = Utils.modifyDate("+", "D", 1, nextTime);
                        while (!casesManager.isWorkDate(nowTime)) {
                            nextTime = Utils.modifyDate("+", "D", 1, nextTime);
                        }
                        data.put("limit_time", limitDate.getTime() - Utils.SDF_YYYYMMDD_HHMMSS.parse(nextTime + " 00:00:00").getTime());
                    }
                }
            } else if ("9".equals(data.get("state")) || "11".equals(data.get("state"))) {
                limitTime = Utils.toString(data.get("limit_time_2"));
                Date limitDate = Utils.SDF_YYYYMMDD_HHMMSS.parse(limitTime + " 23:59:59");
                boolean isOver = nowDate.getTime() - limitDate.getTime() >= 0;
                if (isOver) {
                    data.put("is_over", "1");
                    data.put("limit_time", nowDate.getTime() - limitDate.getTime());
                } else {
                    data.put("is_over", "0");
                    data.put("limit_time", limitDate.getTime() - nowDate.getTime());
                }
            } else if ("12".equals(data.get("state"))) {
                limitTime = Utils.toString(data.get("limit_time_3"));
                Date limitDate = Utils.SDF_YYYYMMDD_HHMMSS.parse(limitTime + " 23:59:59");
                boolean isOver = nowDate.getTime() - limitDate.getTime() >= 0;
                if (isOver) {
                    data.put("is_over", "1");
                    data.put("limit_time", nowDate.getTime() - limitDate.getTime());
                } else {
                    data.put("is_over", "0");
                    data.put("limit_time", limitDate.getTime() - nowDate.getTime());
                }
            }
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> saveContent(String type, String id, String content) {
        try {
            int num = casesManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                return Utils.getErrorMap("案件不存在");
            }

            if (StringUtils.isBlank(content)) {
                return Utils.getErrorMap("请填写内容");
            }
            if ("1".equals(type)) {
                casesManager.executeSQL(Cases.UPDATE_CONTENT_1, new Object[]{content, id});
                return Utils.getSuccessMap(null);
            }
            if ("2".equals(type)) {
                casesManager.executeSQL(Cases.UPDATE_CONTENT_2, new Object[]{content, id});
                casesManager.createDoc(id);
                return Utils.getSuccessMap(null);
            }
            if ("3".equals(type)) {
                casesManager.executeSQL(Cases.UPDATE_CONTENT_3, new Object[]{content, id});
                casesManager.createDoc(id);
                return Utils.getSuccessMap(null);
            }
            if ("5".equals(type)) {
                casesManager.executeSQL(Cases.UPDATE_CONTENT_5, new Object[]{content, id});
                return Utils.getSuccessMap(null);
            }
            if ("6".equals(type)) {
                casesManager.executeSQL(Cases.UPDATE_CONTENT_6, new Object[]{content, id});
                return Utils.getSuccessMap(null);
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> saveLabels(String id, String labels) {
        try {
            int num = casesManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{id});
            if (num == 0) {
                return Utils.getErrorMap("案件不存在");
            }
            if (StringUtils.isBlank(labels)) {
                return Utils.getErrorMap("请填写标签");
            }
            List<String> labelList = initLabelList(labels);
            if (labelList == null || labelList.size() == 0) {
                return Utils.getErrorMap("请填写标签");
            }

            saveLabels(id, labelList);
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> getLabelList(String casesId) {
        try {
            int num = casesManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            if (num == 0) {
                return Utils.getErrorMap("案件不存在");
            }
            return Utils.getSuccessMap(casesManager.getList(Cases.SELECT_LABELS, new Object[]{casesId}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> total(HttpServletRequest request) {
        try {
            Map<String, Object> account = casesManager.getMap(Account.SELECT_BY_ID, new Object[]{request.getSession().getAttribute("account")});
            if (account == null) {
                return Utils.getErrorMap(null);
            }

            String sql = "select c.state,c.id,DATE_FORMAT(c.createtime,'%Y-%m-%d') date_time,DATE_FORMAT(c.createtime,'%Y-%m') month," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' ";
            // 今日新案
            int numberNew = casesManager.getListCount("select a.id from (" + sql + ") a where a.department_id = ? and a.date_time = ?",
                    new Object[]{account.get("department_id"), Utils.SDF_YYYYMMDD.format(new Date())});
            // 受理中案件
            int numberProcess1 = casesManager.getListCount("select a.id from (" + sql + ") a where a.department_id = ? and (a.state = ? or a.state = ?)",
                    new Object[]{account.get("department_id"), Cases.STATE_1, Cases.STATE_2});
            // 审理中案件
            int numberProcess2 = casesManager.getListCount("select a.id from (" + sql + ") a where a.department_id = ? and (a.state = ? or a.state = ? or a.state = ?)",
                    new Object[]{account.get("department_id"), Cases.STATE_12, Cases.STATE_13, Cases.STATE_14});
            // 总数案件
            int numberTotal = casesManager.getListCount("select a.id from (" + sql + ") a where a.department_id = ?",
                    new Object[]{account.get("department_id")});

            // 受理结果
            String sql0 = "select count(*) value,a.name from (" +
                    "select c.state name,c.id," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and (c.state = '3' or c.state = '4' or c.state = '5' or c.state = '6' or c.state = '7' or c.state = '8')" +
                    ") a where a.department_id = ? GROUP BY a.name";
            Map<String, Object> pie1 = getChartData(sql0, new Object[]{account.get("department_id")}, true, true);

            // 审理结果
            // 外圈：中止、支持性的7项、驳回、维持、支持性含有赔偿字眼 ，共11项 
            String sql1 = "select count(*) value,a.name from (" +
                    "select c.state,c.id, '批准中止' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '14' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准变更）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '17' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准撤销）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '21' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准确认违法）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '22' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准责令履行）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '23' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准确认无效）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '24' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准调解与决定）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '25' " +
                    "UNION " +
                    "select c.state,c.id, '支持（批准其他决定）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '26' " +
                    "UNION " +
                    "select c.state,c.id, '否定（批准驳回）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '18' " +
                    "UNION " +
                    "select c.state,c.id, '否定（批准维持）' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and c.state = '19' " +
                    "UNION " +
                    "select c.state,c.id, '赔偿' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c,irsa_cases_step s where c.trash = '0' and (c.state = '17' or c.state = '21' or c.state = '22' or c.state = '23' or c.state = '24' or c.state = '25' or c.state = '26') " +
                    "and s.cases_id = c.id and s.state = c.state and s.`mode` = '1' and s.refine like '%赔偿%' " +
                    ") a where a.department_id = ? GROUP BY a.name";
            Map<String, Object> pie2 = getChartData(sql1, new Object[]{account.get("department_id")}, false, true);
            // 内圈：支持性、否定性、终止性
            String sql2 = "select count(*) value,a.name from (" +
                    "select c.state,c.id, '支持性' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and (c.state = '17' or c.state = '21' or c.state = '22' or c.state = '23' or c.state = '24' or c.state = '25' or c.state = '26') " +
                    "UNION " +
                    "select c.state,c.id, '否定性' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and (c.state = '18' or c.state = '19') " +
                    "UNION " +
                    "select c.state,c.id, '终止性' name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c where c.trash = '0' and (c.state = '15' or c.state = '16') " +
                    ") a where a.department_id = ? GROUP BY a.name";
            List<Map<String, Object>> list2 = casesManager.getList(sql2, new Object[]{account.get("department_id")});
            if (pie2 != null) {
                pie2.put("inner", list2);
            }
            // 案由分布
            Map<String, Object> pie3 = getChartData("select a.label_reason name,COUNT(*) value from (select c.id,c.reason_id,(select r.title from irsa_cases_reason r where r.id = c.reason_id) label_reason," +
                            "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                            "from irsa_cases c where c.trash = '0') a where a.department_id = ? GROUP BY a.label_reason order by value",
                    new Object[]{account.get("department_id")}, false, true);

            // 申请人排名
            String sql3 = "select count(*) value,a.name from(" +
                    "select c.id,p.personnel_id,p.name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c,irsa_cases_personnel p where c.trash = '0' and p.cases_id = c.id and p.personnel_type = '1' " +
                    ") a where a.department_id = ? GROUP BY a.name order by value desc limit 10";
            Map<String, Object> bar3 = getChartData4Bar(sql3, new Object[]{account.get("department_id")});

            // 被申请人类型
            String sql4 = "select count(*) value,a.name from(" +
                    "select c.id,p.personnel_id,dt.title name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c,irsa_cases_personnel p,irsa_defendant_type dt where c.trash = '0' and p.cases_id = c.id and p.personnel_type = '2' and p.type = dt.id " +
                    ") a where a.department_id = ? GROUP BY a.name order by value desc limit 10";
            Map<String, Object> pie4 = getChartData4Funnel(sql4, new Object[]{account.get("department_id")});
            // 被申请人排名
            String sql5 = "select count(*) value,a.name from(" +
                    "select c.id,p.personnel_id,p.name," +
                    "(select (select (select d.id from irsa_department d where d.id = r.department_id) from irsa_console_role r where r.id = a.role_id) from irsa_console_account a where a.id = c.entry_account_id) department_id " +
                    "from irsa_cases c,irsa_cases_personnel p where c.trash = '0' and p.cases_id = c.id and p.personnel_type = '2' " +
                    ") a where a.department_id = ? GROUP BY a.name order by value desc,name desc limit 10";
            Map<String, Object> pie5 = getChartData4Funnel(sql5, new Object[]{account.get("department_id")});

            // 案件处理统计，从当前月开始统计6个月
            SimpleDateFormat sdfMonth = new SimpleDateFormat("YYYY-MM");
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            String[] monthArr = new String[6];
            for (int i = 0; i < 6; i++) {
                calendar.setTime(now);
                calendar.add(Calendar.MONTH, -i);
                String month = sdfMonth.format(calendar.getTime());
                monthArr[i] = month;
            }
            // 录入，告知，转办，不予受理，受理中，审理中，已审结 
            List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
            String[] itemArr = new String[]{"录入", "批准告知", "批准受理转办", "批准不予受理", "受理中", "审理中", "已审结"};
            for (String item : itemArr) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("type", "line");
                data.put("name", item);
                if ("录入".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ?", new Object[]{account.get("department_id"), monthArr[i]});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
                if ("批准告知".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ? and a.state = ?", new Object[]{account.get("department_id"), monthArr[i], Cases.STATE_3});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
                if ("批准受理转办".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ? and a.state = ?", new Object[]{account.get("department_id"), monthArr[i], Cases.STATE_4});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
                if ("批准不予受理".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ? and a.state = ?", new Object[]{account.get("department_id"), monthArr[i], Cases.STATE_6});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
                if ("受理中".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ? and (a.state = ? or a.state = ?)",
                                new Object[]{account.get("department_id"), monthArr[i], Cases.STATE_1, Cases.STATE_2});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
                if ("审理中".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ? and (a.state = ? or a.state = ? or a.state = ? or a.state = ?)",
                                new Object[]{account.get("department_id"), monthArr[i], Cases.STATE_12, Cases.STATE_13, Cases.STATE_14, Cases.STATE_20});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
                if ("已审结".equals(item)) {
                    int[] countArr = new int[6];
                    for (int i = 0; i < 6; i++) {
                        countArr[i] = casesManager.getListCount("select * from (" + sql + ") a where a.department_id = ? and a.month = ? and " +
                                        "(a.state = ? or a.state = ? or a.state = ? or a.state = ? or a.state = ? or a.state = ? or a.state = ? " +
                                        "or a.state = ? or a.state = ? or a.state = ? or a.state = ?)",
                                new Object[]{account.get("department_id"), monthArr[i],
                                        Cases.STATE_17, Cases.STATE_21, Cases.STATE_22, Cases.STATE_23, Cases.STATE_24, Cases.STATE_25, Cases.STATE_26,
                                        Cases.STATE_18, Cases.STATE_19, Cases.STATE_15, Cases.STATE_16});
                    }
                    data.put("data", countArr);
                    itemList.add(data);
                    continue;
                }
            }
            Map<String, Object> line = new HashMap<String, Object>();
            line.put("x_data", monthArr);
            line.put("series", itemList);


            Map<String, Object> data = new HashMap<String, Object>();
            data.put("number_new", numberNew);
            data.put("number_process_1", numberProcess1);
            data.put("number_process_2", numberProcess2);
            data.put("number_total", numberTotal);

            data.put("pie_1", pie1);
            data.put("pie_2", pie2);
            data.put("pie_3", pie3);
            data.put("bar_3", bar3);
            data.put("pie_4", pie4);
            data.put("pie_5", pie5);
            data.put("line", line);
            return Utils.getSuccessMap(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    Map<String, Object> getChartData(String sql, Object[] param, boolean isState, boolean isPie) {
        List<Map<String, Object>> list = casesManager.getList(sql, param);
        if (list == null || list.size() == 0) {
            return null;
        }
        String[] legend = new String[list.size()];
        int[] data = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String name = "";
            if (isState) {
                name = Cases.STATE_LABEL.get(map.get("name"));
            } else {
                name = map.get("name").toString();
            }
            map.put("name", name);
            legend[i] = name;
            data[i] = Integer.parseInt(Utils.toString(map.get("value")));
        }
        Map<String, Object> pie = new HashMap<String, Object>();
        pie.put("legend", legend);
        if (isPie) {
            pie.put("data", list);
        } else {
            pie.put("data", data);
        }
        return pie;
    }

    Map<String, Object> getChartData4Bar(String sql, Object[] param) {
        List<Map<String, Object>> list = casesManager.getList(sql, param);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        String[] legend = new String[list.size()];
        for (int i = list.size(); i > 0; i--) {
            Map<String, Object> map = list.get(i - 1);
            legend[list.size() - i] = map.get("name").toString();
            dataList.add(map);
        }
        Map<String, Object> pie = new HashMap<String, Object>();
        pie.put("legend", legend);
        pie.put("data", dataList);
        return pie;
    }

    Map<String, Object> getChartData4Funnel(String sql, Object[] param) {
        List<Map<String, Object>> list = casesManager.getList(sql, param);
        if (list == null || list.size() == 0) {
            return null;
        }
        String[] legend = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String name = map.get("name").toString() + "：" + map.get("value").toString();
            map.put("name", name);
            map.put("value", (list.size() - i) * 10);
            legend[i] = name;
        }
        Map<String, Object> pie = new HashMap<String, Object>();
        pie.put("legend", legend);
        pie.put("data", list);
        return pie;
    }

    @Override
    public Map<String, Object> createPDF(String casesId) {
        try {
            int num = casesManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            if (num == 0) {
                return Utils.getErrorMap("案件不存在");
            }

            // 案件详情
            Map<String, Object> cases = casesManager.getMap(Cases.SELECT_BY_ID, new Object[]{casesId});
            // 申请人
            List<Map<String, Object>> personnelList1 = casesManager.getList(CasesPersonnel.SELECT_BY_CASESID_TYPE137 + "order by p.createtime,p.id",
                    new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1});
            // 第三人
            List<Map<String, Object>> personnelList3 = casesManager.getList(CasesPersonnel.SELECT_BY_CASESID_TYPE137 + "order by p.createtime,p.id",
                    new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_3});
            // 被申请人
            Map<String, Object> personnel2 = casesManager.getMap(CasesPersonnel.SELECT_BY_CASESID_TYPE2,
                    new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_2});
            // 委托代理人
//            List<Map<String, Object>> personnelList6 = casesManager.getList(CasesPersonnel.SELECT_BY_CASESID + "where a.personnel_type = ? or a.personnel_type = ? order by a.a.personnel_type,a.createtime", 
//                    new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_4, CasesPersonnel.PERSONNEL_TYPE_5});

            String sql = "select * from irsa_cases_file where res_id = ? and mode = ? and type = '1' order by createtime,real_name";
            // 代理人委托书
            List<Map<String, Object>> casesFilelList4 = casesManager.getList(sql,
                    new Object[]{casesId, CasesFile.MODE_4});
            // 申请人证据及其他相关材料
            List<Map<String, Object>> casesFilelList1 = casesManager.getList(sql,
                    new Object[]{casesId, CasesFile.MODE_1});
            // 第三人证据及其他相关材料
            List<Map<String, Object>> casesFilelList3 = casesManager.getList(sql,
                    new Object[]{casesId, CasesFile.MODE_3});
            // 被申请人答复书照片
            List<Map<String, Object>> casesFilelList7 = casesManager.getList(sql,
                    new Object[]{casesId, CasesFile.MODE_7});
            // 被申请人证据及其他相关材料
            List<Map<String, Object>> casesFilelList2 = casesManager.getList(sql,
                    new Object[]{casesId, CasesFile.MODE_2});
            // 涉案文书
            List<Map<String, Object>> casesFilelList6 = casesManager.getList(sql,
                    new Object[]{casesId, CasesFile.MODE_6});

            Utils4PDF.createPDF(cases, personnelList1, personnelList3, personnel2, null,
                    casesFilelList4, casesFilelList1, casesFilelList3, casesFilelList7, casesFilelList2, casesFilelList6);
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public static void main(String[] args) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat("YYYY-MM");
        List<String> monthList = new ArrayList<String>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 6; i++) {
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, 0 - i);
            String month = sdfMonth.format(calendar.getTime());
            System.out.println(month);
            monthList.add(month);
        }
    }

    public CasesManager getCasesManager() {
        return casesManager;
    }

    public void setCasesManager(CasesManager casesManager) {
        this.casesManager = casesManager;
    }

    public CasesFileManager getCasesFileManager() {
        return casesFileManager;
    }

    public void setCasesFileManager(CasesFileManager casesFileManager) {
        this.casesFileManager = casesFileManager;
    }

    public CasesPersonnelManager getCasesPersonnelManager() {
        return casesPersonnelManager;
    }

    public void setCasesPersonnelManager(CasesPersonnelManager casesPersonnelManager) {
        this.casesPersonnelManager = casesPersonnelManager;
    }
}
