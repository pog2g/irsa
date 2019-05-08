package com.irsa.cases.console.service;

import java.util.Map;

public interface RegionConsoleService {
    Map<String, Object> getList(String page, String grade, String provinceId, String cityId);

    Map<String, Object> save1(String id, String title);

    Map<String, Object> save2(String id, String title, String provinceId);

    Map<String, Object> save3(String id, String title, String provinceId, String cityId);

    Map<String, Object> delete(String ids);

    Map<String, Object> sort(String ids);

    Map<String, Object> getChooseList4Parent();

    Map<String, Object> getChooseListByParent(String id);


}
