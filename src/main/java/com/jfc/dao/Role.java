package com.jfc.dao;

import com.jfinal.plugin.activerecord.Model;
import lombok.Data;

@Data
public class Role extends Model<Role> {
  private static final long serialVersionUID = 1L;
  
  public static final Role dao = new Role();
  
  public static final String ROLEID = "roleid";
  public static final String LEVELCODE = "levelcode";
  public static final String NAME = "name";
  public static final String MEMO = "memo";
  public static final String CREATOR = "creator";
  public static final String CREAT_TIME = "creat_time";
}

