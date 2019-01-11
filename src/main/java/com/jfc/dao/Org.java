package com.jfc.dao;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 10:48
 */

import com.jfinal.plugin.activerecord.Model;

public class Org extends Model<Org> {
    private static final long serialVersionUID = 1L;

    public static final Org dao = new Org();

    public static final String ORGID = "id";
    public static final String NAME = "org_name";
    public static final String LEVELCODE = "levelcode";
    public static final String MANAGER = "manager_user";
    public static final String CREATOR = "crt_uid";
    public static final String CREATETIME = "crt_time";

    public int getOrgid() {
        return null==get(ORGID)?0:getNumber(ORGID).intValue();
    }

    public Org setOrgid(int value) {
        return set(ORGID, value);
    }

    public String getName() {
        return (null==get(NAME)||null==getStr(NAME))?"":getStr(NAME);
    }

    public Org setName(String value) {
        return set(NAME, value);
    }

    public String getLevelcode() {
        return (null==get(LEVELCODE)||null==getStr(LEVELCODE))?"":getStr(LEVELCODE);
    }

    public Org setLevelcode(String value) {
        return set(LEVELCODE, value);
    }

    public String getManager() {
        return (null==get(MANAGER)||null==getStr(MANAGER))?"":getStr(MANAGER);
    }

    public Org setManager(String value) {
        return set(MANAGER, value);
    }

    public int getCreator() {
        return null==get(CREATOR)?0:getNumber(CREATOR).intValue();
    }

    public Org setCreator(int value) {
        return set(CREATOR, value);
    }

    public java.util.Date getCreate_time() {
        return getTimestamp(CREATETIME);
    }

    public Org setCreate_time(java.util.Date value) {
        return set(CREATETIME, value == null ? value : new java.sql.Timestamp(value.getTime()));
    }
}


