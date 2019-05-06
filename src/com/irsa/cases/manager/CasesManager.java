package com.irsa.cases.manager;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.common.utils.Utils4Template;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.CasesFile;
import com.irsa.cases.model.CasesPersonnel;
import com.irsa.cases.model.HolidayDate;
import com.jdbc.manager.BaseManager;

@Service
public class CasesManager extends BaseManager {
    
    public boolean isWorkDate(String dateTime) throws ParseException {
        String[] dateTimeArr = dateTime.split("-");
        List<Map<String, Object>> list1 = getList(HolidayDate.SELECT_BY_YEAR_TYPE, new Object[]{dateTimeArr[0], "1"});
        System.out.println(list1);
        List<Map<String, Object>> list2 = getList(HolidayDate.SELECT_BY_YEAR_TYPE, new Object[]{dateTimeArr[0], "2"});
        System.out.println(list2);
        if (list1 != null && list1.size() > 0) {
            List<String> restList = new ArrayList<String>();
            for (Map<String, Object> data : list1) {
                restList.add(Utils.toString(data.get("date_time")));
            }
            if (restList.contains(dateTime)) {
                System.out.println("is");
                return false;
            }
        }
        if (list2 != null && list2.size() > 0) {
            List<String> workList = new ArrayList<String>();
            for (Map<String, Object> data : list2) {
                workList.add(Utils.toString(data.get("date_time")));
            }
            if (workList.contains(dateTime)) {
                return true;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateTime);
        int week = Utils.getDayOfWeek(date);
        if (week == 6 || week ==7) {
            return false;
        }
        return true;
    }
    
    public String toHtml4Party(String casesId, String personnelType) {
    	String type = "申请人";
    	if (CasesPersonnel.PERSONNEL_TYPE_3.equals(personnelType)) {
    		type = "第三人";
    	}
    	StringBuffer partySB = new StringBuffer();
        List<Map<String, Object>> applyList = getList(CasesPersonnel.SELECT_BY_CASESID_TYPE137 + "order by p.createtime,p.id", new Object[]{casesId, personnelType});
        for (Map<String, Object> apply :applyList) {
            String name = "";
            if ("1".equals(apply.get("type"))) {
                name = Utils.toString(apply.get("name"));
                if(apply.get("legal_person") != null && !"".equals(Utils.toString(apply.get("legal_person"))) && !"无".equals(Utils.toString(apply.get("legal_person")))) {
                    name += "，英文名/其他语言名/化名/曾用名，" + apply.get("legal_person");
                }
            } else {
                name = apply.get("name") + "，法定代表人（负责人），" + apply.get("legal_person");
            }
            String contact = "";
            if(apply.get("contact") != null && !"".equals(Utils.toString(apply.get("contact"))) && !"无".equals(Utils.toString(apply.get("contact")))) {
                contact += "，其他联系方式，" + apply.get("contact");
            }
            String zipCode = "";
            if(apply.get("zip_code") != null && !"".equals(Utils.toString(apply.get("zip_code"))) && !"无".equals(Utils.toString(apply.get("zip_code")))) {
                contact += "，邮编，" + apply.get("zip_code");
            }
            StringBuffer sb = new StringBuffer();
            sb.append("<div style=\"text-indent: 2em;\">").append(type).append("：").append(name).append("，证件号码，").append(apply.get("id_no"))
            .append("，手机号码，").append(apply.get("phone")).append(contact).append(zipCode)
            .append("，通讯地址，").append(apply.get("province")).append(apply.get("city")).append(apply.get("county")).append(apply.get("address"))
            .append("</div>");
            partySB.append(sb.toString());
        }
        return partySB.toString();
    }
    
    public String toHtml4Defendant(String casesId) {
    	StringBuffer defendantSB = new StringBuffer();
        Map<String, Object> defendant = getMap(CasesPersonnel.SELECT_BY_CASESID_TYPE2, new Object[]{casesId});
        defendantSB.append("<div style=\"text-indent: 2em;\">被申请人：").append(defendant.get("name"))
        .append("，法定代表人，").append(defendant.get("legal_person"))
        .append("，住所，").append(defendant.get("address"))
        .append("</div>");
        return defendantSB.toString();
    }
    
    public void createDoc(String casesId) {
        Map<String, Object> cases = getMap(Cases.SELECT_4_DOC, new Object[]{casesId});
        if (cases == null) {
            return;
        }
        // 申请人
        String apply = toHtml4Party(casesId, CasesPersonnel.PERSONNEL_TYPE_1);
        // 委托代理人
        StringBuffer agentSB = new StringBuffer();
//        List<Map<String, Object>> agentList = getList(sql.toString(), new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_4});
//        if (agentList.size() > 0) {
//            for (Map<String, Object> agent :agentList) {
//                String contact = "";
//                if(agent.get("contact") != null && !"".equals(Utils.toString(agent.get("contact"))) && !"无".equals(Utils.toString(agent.get("contact")))) {
//                    contact += "，其他联系方式，" + agent.get("contact");
//                }
//                String zipCode = "";
//                if(agent.get("zip_code") != null && !"".equals(Utils.toString(agent.get("zip_code"))) && !"无".equals(Utils.toString(agent.get("zip_code")))) {
//                    contact += "，邮编，" + agent.get("zip_code");
//                }
//                StringBuffer sb = new StringBuffer("<div>委托代理人：");
//                sb.append(agent.get("name")).append("，证件号码，").append(agent.get("id_no"))
//                .append("，手机号码，").append(agent.get("phone")).append(contact).append(zipCode)
//                .append("，联系地址，").append(agent.get("province")).append(agent.get("city")).append(agent.get("county"))
//                .append("，详细地址，").append(agent.get("address"))
//                .append("，工作单位，").append(agent.get("unit_name"))
//                .append("</div>");
//                agentSB.append(sb.toString());
//            }
//        }
        // 被申请人
        String defendant = toHtml4Defendant(casesId);

        String html = MessageFormat.format(Utils4Template.TEMPLATE_HTML_STATE_0, 
        		apply, agentSB.toString(), defendant, 
                cases.get("apply_request"), cases.get("facts_reasons"),
                cases.get("create_department"), cases.get("create_time"));
//        System.out.println(html);
        Map<String, Object> file = getMap(CasesFile.SELECT_BY_CASESID_MODE, new Object[]{casesId, CasesFile.MODE_6});
        String fileName = Utils.getId() + ".doc";
        Utils4File.createDoc(fileName, html);
        executeSQL(CasesFile.UPDATE_DOC, new Object[]{fileName, html, file.get("id")});
    } 
    
    public static void main(String[] args) throws ParseException {
        CasesManager m = new CasesManager();
        String limitTime= "2018-09-21";
        for (int i=0;i<4;i++) {
            limitTime = Utils.modifyDate("+", "D", 1, limitTime);
            while(!m.isWorkDate(limitTime)) {
                limitTime = Utils.modifyDate("+", "D", 1, limitTime);
            }
            System.out.println(i+"@"+limitTime);
        }
    }
}
