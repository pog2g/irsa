package com.irsa.cases.console.service;

import java.util.Map;

public interface RequestCategoryService {

    Map<String, Object> getList();

    Map<String, Object> save(String id, String title);

    Map<String, Object> delete(String ids);

    Map<String, Object> sort(String ids);

    Map<String, Object> getChooseList();
}
