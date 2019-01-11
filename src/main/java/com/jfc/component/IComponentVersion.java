package com.jfc.component;

import com.jfc.enums.ComponentEnum;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 10:21
 */
public interface IComponentVersion {
    String getVersion();

    ComponentEnum getConfig();

    String getDesc();
}
