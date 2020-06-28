package com.huchx.utils;

import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页对象处理工具
 */
public class PageUtil {
    public static Map<String,Object> parseToModel(PageInfo page){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("content",page.getList());
        resultMap.put("total",page.getTotal());
        resultMap.put("totalPage",page.getPages());
        resultMap.put("pageNo",page.getPageNum());
        resultMap.put("pageSize",page.getPageSize());
        resultMap.put("count",page.getSize());
        return  resultMap;
    }
}
