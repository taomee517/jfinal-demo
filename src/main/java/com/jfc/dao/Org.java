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

    public static final String ORGID = "orgid";
    public static final String NAME = "name";
    public static final String LEVELCODE = "levelcode";
    public static final String MEMO = "memo";
    public static final String CREATOR = "creator";
    public static final String CREATE_TIME = "create_time";

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

    public String getMemo() {
        return (null==get(MEMO)||null==getStr(MEMO))?"":getStr(MEMO);
    }

    public Org setMemo(String value) {
        return set(MEMO, value);
    }

    public int getCreator() {
        return null==get(CREATOR)?0:getNumber(CREATOR).intValue();
    }

    public Org setCreator(int value) {
        return set(CREATOR, value);
    }

    public java.util.Date getCreate_time() {
        return getTimestamp(CREATE_TIME);
    }

    public Org setCreate_time(java.util.Date value) {
        return set(CREATE_TIME, value == null ? value : new java.sql.Timestamp(value.getTime()));
    }
}


