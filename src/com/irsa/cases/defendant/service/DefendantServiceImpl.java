package com.irsa.cases.defendant.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.CasesFileManager;
import com.irsa.cases.manager.DefendantManager;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.CasesFile;
import com.irsa.cases.model.CasesStep;
import com.irsa.cases.model.Defendant;
import com.irsa.cases.model.DefendantFile;
import com.irsa.cases.model.DefendantType;
import com.irsa.cases.model.TempFile;

@Service
public class DefendantServiceImpl implements DefendantService {
    Logger log = LoggerFactory.getLogger(DefendantServiceImpl.class);
    @Autowired
    CasesFileManager casesFileManager;
    @Autowired
    DefendantManager defendantManager;
    
    @Override
    public Map<String,Object> register(String id, String name, String address, String legalPerson, String account, String pwd, String pwdConfirm) {
        try {
            if (StringUtils.isBlank(name)) {
                return Utils.getErrorMap("请填写单位名称");
            }
            if (StringUtils.isBlank(address)) {
                return Utils.getErrorMap("请填写单位地址");
            }
            if (StringUtils.isBlank(legalPerson)) {
                return Utils.getErrorMap("请填写法定代表人");
            }
            if (StringUtils.isBlank(account)) {
                return Utils.getErrorMap("请填写用户名");
            }
            if (account.length() < 6 || account.length() > 18) {
                return Utils.getErrorMap("用户名格式错误");
            }
            if (StringUtils.isBlank(pwd)) {
                return Utils.getErrorMap("请填写密码");
            }
            if (!Utils.checkPwd(pwd)) {
                return Utils.getErrorMap("密码格式错误");
            }
            if (StringUtils.isBlank(pwdConfirm)) {
                return Utils.getErrorMap("请填写密码确认");
            }
            if (!pwd.equals(pwdConfirm)) {
                return Utils.getErrorMap("两次输入的密码不一致");   
            }
            
            int num = defendantManager.getListCount(Defendant.SELECT_EXIST_BY_ACCOUNT, new Object[]{account});
            if (num != 0) {
                return Utils.getErrorMap("用户名已存在");
            }
            Map<String, Object> data = defendantManager.getMap(Defendant.SELECT_EXIST_BY_NAME, new Object[]{name});
            if (data != null) { 
                if (StringUtils.isNotBlank(Utils.toString(data.get("account")))) {
                    return Utils.getErrorMap("该单位已注册");
                }
                defendantManager.executeSQL(Defendant.UPDATE_3, new Object[]{name, address, legalPerson, account, Utils.MD5(pwd), data.get("id")});
            } else {
                Map<String, Object> type = defendantManager.getMap(DefendantType.SELECT_ALL, null);
                defendantManager.executeSQL(Defendant.INSERT, new Object[]{id, Utils.getCreateTime(), type == null ? "" : type.get("id"), name, address, legalPerson, account, Utils.MD5(pwd)});
            }
            
            Map<String, Object> tempFile1 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{id, DefendantFile.MODE_1});
            Map<String, Object> tempFile2 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{id, DefendantFile.MODE_2});
            Map<String, Object> tempFile3 = defendantManager.getMap(TempFile.SELECT_BY_RESID_MODE, new Object[]{id, DefendantFile.MODE_3});
            if (tempFile1 != null) {
                defendantManager.saveFile(id, DefendantFile.MODE_1, tempFile1);
            }
            if (tempFile2 != null) {
                defendantManager.saveFile(id, DefendantFile.MODE_2, tempFile2);
            }
            if (tempFile3 != null) {
                defendantManager.saveFile(id, DefendantFile.MODE_3, tempFile3);
            }
            return Utils.getSuccessMap(null);
        } catch(Exception e) {
            log.error(e.getMessage(),e); 
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> login(HttpServletRequest request, String account, String pwd) {
        try {
            if (StringUtils.isBlank(account)) {
                return Utils.getErrorMap("请填写用户名");
            }
            if (StringUtils.isBlank(pwd)) {
                return Utils.getErrorMap("请填写密码");
            }
            
            int num = defendantManager.getListCount(Defendant.SELECT_EXIST_BY_ACCOUNT, new Object[]{account});
            if (num == 0) {
                return Utils.getErrorMap("用户名不存在");
            }
            Map<String, Object> data = defendantManager.getMap(Defendant.SELECT_EXIST_BY_ACCOUNT_PWD, new Object[]{account, Utils.MD5(pwd)});
            if (data == null) {
                return Utils.getErrorMap("密码错误");
            } 
            request.getSession().setAttribute("defendant", data.get("id"));
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getMap(HttpServletRequest request) {
        try {
            Object id = request.getSession().getAttribute("defendant");
            if (id == null || StringUtils.isBlank(id.toString())) {
                return Utils.getErrorMap(null);
            }
            Map<String, Object> account = defendantManager.getMap(Defendant.SELECT_ACCOUNT_BY_ID, new Object[]{id});
            if (account == null) {
                return Utils.getErrorMap(null);
            }
            return Utils.getSuccessMap(account);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getCasesList(HttpServletRequest request) {
        try {
            Object id = request.getSession().getAttribute("defendant");
            if (id == null || StringUtils.isBlank(id.toString())) {
                return Utils.getErrorMap(null);
            }
            Map<String, Object> account = defendantManager.getMap(Defendant.SELECT_EXIST_BY_ID, new Object[]{id});
            if (account == null) {
                return Utils.getErrorMap(null);
            }
            
            StringBuffer sql = new StringBuffer(Cases.SELECT);
            sql.append("where c.state in ('9', '10', '11', '12', '13', '14','32') ");
            sql.append("and c.id in (select cp.cases_id from irsa_cases_personnel cp where cp.personnel_type = '2' and cp.personnel_id = ?) ");
            sql.append("order by c.createtime desc");
            List<Map<String, Object>> list = defendantManager.getList(sql.toString(), new Object[]{account.get("id")});
            return Utils.getSuccessMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> reply(String bookId, String fileId, String casesId, String reply, String remark) {
        try {
            int num = defendantManager.getListCount(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            if (num == 0) {
                return Utils.getErrorMap("案件已不存在");
            }
            if (StringUtils.isBlank(reply)) {
                return Utils.getErrorMap("请填写答复书");
            }
//            if (StringUtils.isBlank(remark)) {
//                return Utils.getErrorMap("请填写备注");
//            }
            // 答复书照片
            List<Map<String, Object>> tempBookList = defendantManager.getList(TempFile.SELECT_BY_RESID_MODE, new Object[]{bookId, CasesFile.MODE_7});
            if (tempBookList == null || tempBookList.size() == 0) {
                return Utils.getErrorMap("请添加答复书照片");
            }
            // 证据信息
            List<Map<String, Object>> tempFileList = defendantManager.getList(TempFile.SELECT_BY_RESID_MODE, new Object[]{fileId, CasesFile.MODE_2});
            if (tempFileList == null || tempFileList.size() == 0) {
                return Utils.getErrorMap("请添加证据和其他相关材料");
            }
            for (Map<String, Object> tempBook : tempBookList) {
                casesFileManager.saveFile(casesId, CasesFile.MODE_7, tempBook);
            }
            for (Map<String, Object> tempFile : tempFileList) {
                casesFileManager.saveFile(casesId, CasesFile.MODE_2, tempFile);
            }
            Map<String, Object> casesMap = defendantManager.getMap(Cases.SELECT_EXIST_BY_ID, new Object[]{casesId});
            int defendant_reply_num=0;
            if(casesMap.get("defendant_reply_num")!=null) {
            	defendant_reply_num=Integer.parseInt(casesMap.get("defendant_reply_num").toString());
            }
            
            if(defendant_reply_num>0) {//表示多个被申请人
            	String reply_state=Cases.STATE_11;
            	if(defendant_reply_num>1) {
            		reply_state=Cases.STATE_32;//部分被申请人答复
            	}
            	defendantManager.executeSQL(Cases.UPDATE_MORE_REPLY, new Object[]{reply, remark, reply_state, casesId});
                defendantManager.executeSQL(CasesStep.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), null, casesId, CasesStep.MODE_1, Cases.STATE_11,
                        null, reply, remark, null, null, null, null, null, null, null, null, null});
            }else {
            	defendantManager.executeSQL(Cases.UPDATE_REPLY, new Object[]{reply, remark, Cases.STATE_11, casesId});
                defendantManager.executeSQL(CasesStep.INSERT, new Object[]{Utils.getId(), Utils.getCreateTime(), null, casesId, CasesStep.MODE_1, Cases.STATE_11,
                        null, reply, remark, null, null, null, null, null, null, null, null, null});
            }
            
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
    public DefendantManager getDefendantManager() {
        return defendantManager;
    }
    public void setDefendantManager(DefendantManager defendantManager) {
        this.defendantManager = defendantManager;
    }
}
