package com.irsa.cases.console.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.common.utils.Utils4File;
import com.irsa.cases.manager.AgentManager;
import com.irsa.cases.manager.CasesManager;
import com.irsa.cases.manager.CasesPersonnelManager;
import com.irsa.cases.manager.DefendantManager;
import com.irsa.cases.manager.PartyManager;
import com.irsa.cases.model.Cases;
import com.irsa.cases.model.CasesPersonnel;
import com.jdbc.utils.Utils4JDBC;

@Service
public class CasesPersonnelConsoleServiceImpl implements CasesPersonnelConsoleService {
    Logger log = LoggerFactory.getLogger(CasesPersonnelConsoleServiceImpl.class);
    @Autowired
    AgentManager agentManager;
    @Autowired
    CasesManager casesManager;
    @Autowired
    CasesPersonnelManager casesPersonnelManager;
    
    @Override
    public Map<String, Object> getList(String casesId, String personnelType) {
        try {
            StringBuffer sql = null;
            Object[] param = null;
            if ("0".equals(personnelType) || CasesPersonnel.PERSONNEL_TYPE_1.equals(personnelType) || 
                    CasesPersonnel.PERSONNEL_TYPE_3.equals(personnelType) || CasesPersonnel.PERSONNEL_TYPE_7.equals(personnelType)) {
                sql = new StringBuffer(CasesPersonnel.SELECT_BY_CASESID_TYPE137);
                if ("0".equals(personnelType)) {
                    sql.append("and p.representative = ? order by p.createtime,p.id");
                    param = Utils4JDBC.mergeObjectArray(param, new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1, CasesPersonnel.REPRESENTATIVE_1});
                } else {
                    sql.append("order by p.createtime,p.id");
                    param = Utils4JDBC.mergeObjectArray(param, new Object[]{casesId, personnelType});
                }
                List<Map<String, Object>> list = casesPersonnelManager.getList(sql.toString(), param);
                if (list != null && list.size() > 0) {
                    for (Map<String, Object> data : list) {
                        PartyManager.getFileUrl(data);
                        List<Map<String, Object>> agentList = casesPersonnelManager.getList(CasesPersonnel.SELECT_BY_CASESID_CLIENTID, new Object[]{casesId, data.get("id")});
                        if (agentList != null && agentList.size() > 0) {
                            for (Map<String, Object> agent : agentList) {
                                agent.put("agent_file", Utils4File.getFileUrl(Utils4File.PATH_CASES, agent.get("agent_file").toString()));
                                agentManager.getIdFiles(agent);
                            }
                        }
                        data.put("agent_list", agentList);
                        
                        List<Map<String, Object>> casesList = new ArrayList<Map<String,Object>>();
                        StringBuffer casesSql = new StringBuffer(Cases.SELECT);
                        casesSql.append("where c.id != ? and c.id in (select cc.id from irsa_cases cc,irsa_cases_personnel cp " +
                                "where cc.id = cp.cases_id and cp.personnel_type = '1' and cp.id_no = ?) " +
                                "order by c.createtime desc limit 6");
                        List<Map<String, Object>> cases = casesPersonnelManager.getList(casesSql.toString(), new Object[]{casesId, data.get("id_no")});
                        if (cases != null && cases.size() > 0) {
                            for (Map<String, Object> map : cases) {
                                Map<String, Object> c = new HashMap<String, Object>();
                                c.put("id", map.get("id"));
                                c.put("title", map.get("title"));
                                casesList.add(c);
                            }
                        }
                        data.put("cases_list", casesList);
                    }
                }
                return Utils.getSuccessMap(list);
            } 
            
            sql = new StringBuffer(CasesPersonnel.SELECT_BY_CASESID_TYPE2);
            param = Utils4JDBC.mergeObjectArray(param, new Object[]{casesId});
            List<Map<String, Object>> list = casesPersonnelManager.getList(sql.toString(), param);
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    DefendantManager.getFileUrl(data);
                    List<Map<String, Object>> agentList = casesPersonnelManager.getList(CasesPersonnel.SELECT_BY_CASESID_CLIENTID, new Object[]{casesId, data.get("id")});
                    if (agentList != null && agentList.size() > 0) {
                        for (Map<String, Object> agent : agentList) {
                            agent.put("agent_file", Utils4File.getFileUrl(Utils4File.PATH_CASES, agent.get("agent_file").toString()));
                            agentManager.getIdFiles(agent);
                        }
                    }
                    data.put("agent_list", agentList);
                    
                    Map<String, Object> totalMap = new HashMap<String, Object>(); 
                    BigDecimal totalCases = Utils.changeToBigDecimal(data.get("defendant_cases_number"));
                    if (totalCases.compareTo(new BigDecimal(0)) == 1) {
                        StringBuffer totalSql = new StringBuffer("select c.state,count(*) number from irsa_cases c,irsa_cases_personnel cp where c.id = cp.cases_id and cp.personnel_type = '2' and cp.name = ? group by c.state order by number;");
                        List<Map<String, Object>> totals = casesPersonnelManager.getList(totalSql.toString(), new Object[]{data.get("name")});
                        if (totals != null && totals.size() > 0) {
                            List<String> dataList = new ArrayList<String>();
                            List<String> colorList = new ArrayList<String>();
                            List<String> labelList = new ArrayList<String>();
                            for (Map<String, Object> map : totals) {
                                BigDecimal rate = Utils.changeToBigDecimal(map.get("number")).multiply(new BigDecimal(100)).divide(totalCases, 2, BigDecimal.ROUND_HALF_UP); 
                                dataList.add(map.get("number").toString());
                                colorList.add(Cases.STATE_COLOR.get(map.get("state")));
                                labelList.add(Cases.STATE_LABEL.get(map.get("state")) + "-" + rate + "%");
                            }
                            totalMap.put("data_list", dataList);
                            totalMap.put("color_list", colorList);
                            totalMap.put("label_list", labelList);
                        }
                    }
                    data.put("total", totalMap);
                }
            }
            return Utils.getSuccessMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> getPersonnelInfoList(String casesId, String personnelType) {
        try {
            List<Map<String, Object>> casesApply = casesPersonnelManager.getList(CasesPersonnel.SELECT_APPLY_BY_CASESID, new Object[]{personnelType, casesId});
            return Utils.getSuccessMap(casesApply);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> getList4Representative(String casesId) {
        try {
            StringBuffer sql = new StringBuffer(CasesPersonnel.SELECT_BY_CASESID_TYPE137).append("and p.representative = ? order by p.createtime,p.id");;
            Object[] param = new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_1, CasesPersonnel.REPRESENTATIVE_1};
            List<Map<String, Object>> list = casesPersonnelManager.getList(sql.toString(), param);
            if (list != null && list.size() > 0) {
                for (Map<String, Object> data : list) {
                    PartyManager.getFileUrl(data);
                }
            }
            return Utils.getSuccessMap(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList4Client(String casesId, String type) {
        try {
            List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
            List<Map<String, Object>> list = casesPersonnelManager.getList(CasesPersonnel.SELECT_EXIST_BY_CASESID_PERSONNEL_TYPE, new Object[]{casesId, type}); 
            if (list.size() > 0) {
                for (Map<String, Object> map : list) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("id", map.get("id"));
                    data.put("text", map.get("name"));
                    dataList.add(data);
                }
            }
            return Utils.getSuccessMap(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList4ApplyThirdParty(String casesId) {
        try {
            List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", "-1");
            data.put("text", "请选择");
            dataList.add(data);
            List<Map<String, Object>> list = casesPersonnelManager.getList(CasesPersonnel.SELECT_BY_CASESID_TYPE137 + " order by p.createtime,p.id", new Object[]{casesId, CasesPersonnel.PERSONNEL_TYPE_7}); 
            if (list != null && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    map.put("text", map.get("name"));
                    map.put("id", map.get("personnel_id"));
                    dataList.add(map);
                }
            }
            return Utils.getSuccessMap(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> update13(String id, String name, String legalPerson, String idType, String idNo, 
            String gender, String birthday, String phone, String contact,
            String zipCode, String provinceId, String cityId, String countyId, String address,
            String unitIdType, String unitIdNo, String unitContact, String domicile) {
        try {
            Map<String, Object> personnel = casesPersonnelManager.getMap(CasesPersonnel.SELECT_EXIST_BY_ID, new Object[]{id});
            if (personnel == null) {
                return Utils.getErrorMap("当事人不存在");
            }
            if ("1".equals(personnel.get("type"))) {
                if (StringUtils.isBlank(name)) {
                    return Utils.getErrorMap("请填写姓名");
                } 
            } else {
                if (StringUtils.isBlank(name)) {
                    return Utils.getErrorMap("请填写法人组织名称");
                } 
                if (StringUtils.isBlank(legalPerson)) {
                    return Utils.getErrorMap("请填写法定代表人（负责人）");
                }
                if (StringUtils.isBlank(unitIdType) || "-1".equals(unitIdType)) {
                    return Utils.getErrorMap("请选择单位证件类型");
                }
            }
            if (StringUtils.isBlank(idType) || "-1".equals(idType)) {
                return Utils.getErrorMap("请选择证件类型");
            }
            if (StringUtils.isBlank(idNo)) {
                return Utils.getErrorMap("请填写证件号码");
            }
            if (StringUtils.isBlank(birthday)) {
                birthday = null;
            }
            if (StringUtils.isBlank(phone)) {
                return Utils.getErrorMap("请填写手机号码");
            }
            if (StringUtils.isBlank(provinceId) || "-1".equals(provinceId)) {
                return Utils.getErrorMap("请选择省");
            }
            if (StringUtils.isBlank(cityId) || "-1".equals(cityId)) {
                return Utils.getErrorMap("请选择市");
            }
            if (StringUtils.isBlank(countyId) || "-1".equals(countyId)) {
                return Utils.getErrorMap("请选择区/县");
            }
            if (StringUtils.isBlank(address)) {
                return Utils.getErrorMap("请填写详细地址");
            }
            
            casesPersonnelManager.executeSQL(CasesPersonnel.UPDATE13, new Object[]{name, legalPerson, idType, idNo, 
                    gender, birthday, phone, contact, zipCode, countyId, address, 
                    unitIdType, unitIdNo, unitContact, domicile, id});
            casesManager.createDoc(Utils.toString(personnel.get("cases_id")));
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> update2(String id, String type, String name, String address, String legalPerson) {
        try {
            Map<String, Object> personnel = casesPersonnelManager.getMap(CasesPersonnel.SELECT_EXIST_BY_ID, new Object[]{id});
            if (personnel == null) {
                return Utils.getErrorMap("当事人不存在");
            }
            if (StringUtils.isBlank(type) || "-1".equals(type)) {
                return Utils.getErrorMap("请选择被申请人类型");
            }
            if (StringUtils.isBlank(name)) {
                return Utils.getErrorMap("请填写被申请人名称");
            } 
            if (StringUtils.isBlank(address)) {
                return Utils.getErrorMap("请填写被申请人住所");
            }
            if (StringUtils.isBlank(legalPerson)) {
                return Utils.getErrorMap("请填写法定代表人");
            }
            
            casesPersonnelManager.executeSQL(CasesPersonnel.UPDATE2, new Object[]{type, name, address, legalPerson, id});
            casesManager.createDoc(Utils.toString(personnel.get("cases_id")));
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> update6(String id, String name, String unit_name, String idType, String idNo, String gender, String birthday, String phone, String contact,
            String zipCode, String provinceId, String cityId, String countyId, String address) {
        try {
            Map<String, Object> personnel = casesPersonnelManager.getMap(CasesPersonnel.SELECT_EXIST_BY_ID, new Object[]{id});
            if (personnel == null) {
                return Utils.getErrorMap("当事人不存在");
            }
            if (StringUtils.isBlank(name)) {
                return Utils.getErrorMap("请填写姓名");
            } 
            if (StringUtils.isBlank(idType) || "-1".equals(idType)) {
                return Utils.getErrorMap("请选择证件类型");
            }
            if (StringUtils.isBlank(idNo)) {
                return Utils.getErrorMap("请填写证件号码");
            }
            if (StringUtils.isBlank(birthday)) {
                birthday = null;
            }
            if (StringUtils.isBlank(phone)) {
                return Utils.getErrorMap("请填写手机号码");
            }
            if (StringUtils.isBlank(provinceId) || "-1".equals(provinceId)) {
                return Utils.getErrorMap("请选择省");
            }
            if (StringUtils.isBlank(cityId) || "-1".equals(cityId)) {
                return Utils.getErrorMap("请选择市");
            }
            if (StringUtils.isBlank(countyId) || "-1".equals(countyId)) {
                return Utils.getErrorMap("请选择区/县");
            }
            if (StringUtils.isBlank(address)) {
                return Utils.getErrorMap("请填写详细地址");
            }
            
            casesPersonnelManager.executeSQL(CasesPersonnel.UPDATE6, new Object[]{name, unit_name, idType, idNo, gender, birthday, phone, contact, zipCode, countyId, address, id});
            casesManager.createDoc(Utils.toString(personnel.get("cases_id")));
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public AgentManager getAgentManager() {
        return agentManager;
    }
    public void setAgentManager(AgentManager agentManager) {
        this.agentManager = agentManager;
    }
    public CasesManager getCasesManager() {
        return casesManager;
    }
    public void setCasesManager(CasesManager casesManager) {
        this.casesManager = casesManager;
    }
    public CasesPersonnelManager getCasesPersonnelManager() {
        return casesPersonnelManager;
    }
    public void setCasesPersonnelManager(CasesPersonnelManager casesPersonnelManager) {
        this.casesPersonnelManager = casesPersonnelManager;
    }
}
