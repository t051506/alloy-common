package com.alloy.cloud.common.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @Author: tn_kec
 * @Date: 2019/11/28 15:27
 */
public class ConverterUtil {

    /**
     * 集合COPY
     * @param source 源
     * @param target 目标
     * @param sourceList 源集合
     * @param <T> 源类
     * @param <F> 目标类
     * @return 目标集合
     */
    public static <T, F> List<F> copyProperties(Class<T> source, Class<F> target, List<T> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        } else {
            List<F> targetList = new ArrayList<>();
            sourceList.forEach((t) -> {
                F f = null;

                try {
                    f = target.newInstance();
                } catch (InstantiationException var5) {
                    var5.printStackTrace();
                } catch (IllegalAccessException var6){
                    var6.printStackTrace();
                }

                BeanUtils.copyProperties(t, f);
                targetList.add(f);
            });
            return targetList;
        }
    }
}
