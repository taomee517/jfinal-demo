package com.jfc.dao;

import com.jfinal.plugin.activerecord.Model;
import lombok.Data;

@Data
public class Role extends Model<Role> {
  private static final long serialVersionUID = 1L;
  
  public static final Role dao = new Role();
  
  public static final String ROLEID = "id";
  public static final String ROLETYPE = "role_type";
  public static final String NAME = "name";
  public static final String REMARK = "remark";
  public static final String CREATOR = "crt_uid";
  public static final String CREATTIME = "creat_time";
}

