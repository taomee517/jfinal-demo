package com.jfc.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 10:22
 */
public enum ComponentEnum {
    /**Jfinal demo
     * jfinal demo项目
     */
    JFINAL_DEMO(1, "zjfinal", "jfinal demo项目", "com.jfc.StartServer");


    private int id;
    private String appName;
    private String nickName;
    private String startClazz;

    private ComponentEnum(int id, String appName, String nickName, String startClazz) {
        this.id = id;
        this.appName = appName;
        this.nickName = nickName;
        this.startClazz = startClazz;
    }

}
