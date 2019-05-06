package com.irsa.cases.console.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.utils.Utils;
import com.irsa.cases.manager.RegionManager;
import com.irsa.cases.model.Region;
import com.jdbc.utils.Utils4JDBC;

@Service
public class RegionConsoleServiceImpl implements RegionConsoleService {
    Logger log = LoggerFactory.getLogger(RegionConsoleServiceImpl.class);
    @Autowired
    RegionManager regionManager;
    
    @Override
    public Map<String, Object> getList(String page, String grade, String provinceId, String cityId) {
        try {
            StringBuffer sql = new StringBuffer(Region.SELECT);
            Object[] param = null;
            if (Region.GRADE_1.equals(grade) || Region.GRADE_2.equals(grade) || Region.GRADE_3.equals(grade)) {
                 sql.append("and r.grade = ? ");
                 param = Utils4JDBC.mergeObjectArray(param, new Object[]{grade});
            } else {
                if (StringUtils.isNotBlank(cityId) && !"-1".equals(cityId)) {
                    sql.append("and r.grade = ? and r.parent_id = ? ");
                    param = Utils4JDBC.mergeObjectArray(param, new Object[]{Region.GRADE_3, cityId});
                } else {
                    if (StringUtils.isNotBlank(provinceId) && !"-1".equals(provinceId)) {
                        sql.append("and r.grade = ? and r.parent_id = ? ");
                        param = Utils4JDBC.mergeObjectArray(param, new Object[]{Region.GRADE_2, provinceId});
                    }
                }
            }
            sql.append("order by province_seq,city_seq,r.grade,r.sequence ");
            return Utils.getSuccessMap(regionManager.getPageList(sql.toString(), param, Integer.parseInt(page), 10, "data_list"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> save1(String id, String title) {
        try {
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填省名称");
            }
            int num = regionManager.getListCount(Region.SELECT_EXIST_TITLE_4_PARENT, new Object[]{id, title});
            if (num != 0) {
                return Utils.getErrorMap("省名称已存在");
            }
            
            num = regionManager.getListCount(Region.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
                id = Utils.getId();
                num = regionManager.getListCount(Region.SELECT_EXIST, null);
                regionManager.executeSQL(Region.INSERT_1, new Object[]{id, Utils.getCreateTime(), num + 1, title, id});
            } else {
                regionManager.executeSQL(Region.UPDATE_1, new Object[]{title, id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> save2(String id, String title, String provinceId) {
        try {
            int num = regionManager.getListCount(Region.SELECT_EXIST_ID, new Object[]{provinceId});
            if (num == 0) {
                return Utils.getErrorMap("省不存在");
            }
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填市名称");
            }
            
            num = regionManager.getListCount(Region.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
            	String[] nameArr = title.split("、");
                for (String name : nameArr) {
                	num = regionManager.getListCount(Region.SELECT_EXIST_TITLE_4_CHILD, new Object[]{id, Utils.toString(name), provinceId});
                    if (num != 0) {
                    	return Utils.getErrorMap("市名称已存在");
                    }
                	id = Utils.getId();
                	num = regionManager.getListCount(Region.SELECT_EXIST, null);
                	regionManager.executeSQL(Region.INSERT_23, new Object[]{id, Utils.getCreateTime(), num, Region.GRADE_2, provinceId, Utils.toString(name), provinceId, id});
                }
                return Utils.getSuccessMap(null);
            }
            
            num = regionManager.getListCount(Region.SELECT_EXIST_TITLE_4_CHILD, new Object[]{id, Utils.toString(title), provinceId});
            if (num != 0) {
            	return Utils.getErrorMap("市名称已存在");
            }
            regionManager.executeSQL(Region.UPDATE_2, new Object[]{provinceId, Utils.toString(title), provinceId, id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> save3(String id, String title, String provinceId, String cityId) {
        try {
            int num = regionManager.getListCount(Region.SELECT_EXIST_ID, new Object[]{provinceId});
            if (num == 0) {
                return Utils.getErrorMap("省不存在");
            }
            num = regionManager.getListCount(Region.SELECT_EXIST_ID, new Object[]{cityId});
            if (num == 0) {
                return Utils.getErrorMap("市不存在");
            }
            if (StringUtils.isBlank(title)) {
                return Utils.getErrorMap("请填区名称");
            }
            num = regionManager.getListCount(Region.SELECT_EXIST_ID, new Object[]{id});
            if (num == 0) {
            	String[] nameArr = title.split("、");
                for (String name : nameArr) {
                	num = regionManager.getListCount(Region.SELECT_EXIST_TITLE_4_CHILD, new Object[]{id, Utils.toString(name), cityId});
                    System.out.println(num);
                	if (num != 0) {
                    	return Utils.getErrorMap("区名称已存在");
                    }
                    
                    id = Utils.getId();
                    num = regionManager.getListCount(Region.SELECT_EXIST, null);
                    regionManager.executeSQL(Region.INSERT_23, new Object[]{id, Utils.getCreateTime(), num, Region.GRADE_3, cityId, Utils.toString(name), provinceId, cityId});
                }
                return Utils.getSuccessMap(null);
            }
            
            num = regionManager.getListCount(Region.SELECT_EXIST_TITLE_4_CHILD, new Object[]{id, Utils.toString(title), cityId});
            if (num != 0) {
            	return Utils.getErrorMap("区名称已存在");
            }
            regionManager.executeSQL(Region.UPDATE_3, new Object[]{cityId, Utils.toString(title), provinceId, cityId, id});
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    @Override
    public Map<String, Object> delete(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择数据");
            }
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                regionManager.executeSQL(Region.DELETE, new Object[]{id});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> sort(String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Utils.getErrorMap("请选择数据");
            }
            String[] idArr = ids.split(",");
            for (int i = 0; i < idArr.length; i++) {
                regionManager.executeSQL(Region.UPDATE_SEQUENCE, new Object[]{i + 1, idArr[i]});
            }
            return Utils.getSuccessMap(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseList4Parent() {
        try {
            return Utils.getSuccessMap(Utils.changeToSelectList(regionManager.getList(Region.SELECT_4_PARENT, null)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }
    
    @Override
    public Map<String, Object> getChooseListByParent(String id) {
        try {
            return Utils.getSuccessMap(Utils.changeToSelectList(regionManager.getList(Region.SELECT_BY_PARENTID, new Object[]{id})));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Utils.getErrorMap(null);
        }
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
    public void setRegionManager(RegionManager regionManager) {
        this.regionManager = regionManager;
    }
}
