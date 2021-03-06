package com.jfc.controller;

import com.jfc.dao.Org;
import com.jfinal.core.Controller;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:33
 */
public class OrgCtrl extends Controller {

    public void getOrgInfo(){
        System.out.println("进入getOrgInfo方法");
        int id = getParaToInt("id");
        Org org = new Org();
        org.setOrgid(id);
        org =  org.findFirst("select * from sys_org where id=" + id);
        renderJson(org);
    }

    public void queryOrg(){
        int id = getParaToInt("id");
        String name = getPara("name");
        Org org = Org.dao.findById(id);
        renderJson(org);
    }
}
