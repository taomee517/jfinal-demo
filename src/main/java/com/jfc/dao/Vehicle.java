package com.jfc.dao;

import com.jfinal.plugin.activerecord.Model;
import lombok.Data;

@Data
public class Vehicle extends Model<Vehicle> {
  private static final long serialVersionUID = 1L;
  
  public static final Vehicle dao = new Vehicle();
  
  public static final String VEHICLEID = "vehicleid";
  public static final String VMID = "vmid";
  public static final String VIN = "vin";
  public static final String PLATENUMBER = "platenumber";
  public static final String COLOR = "color";
  public static final String AVAILABLE = "available";
  public static final String LAT = "lat";
  public static final String LNG = "lng";
  public static final String BUY_DATE = "buy_date";
  public static final String ANNUAL_DATE = "annual_date";
  public static final String INSURANCE_DATE = "insurance_date";
  public static final String SERVICE_DATE = "service_date";
  public static final String OTU_IMEI = "otu_imei";
  public static final String CURRENT_MILE = "current_mile";
  public static final String OIL_TYPE = "oil_type";
  public static final String VOLTAGE = "voltage";
  public static final String PUBLISHED = "published";
  public static final String ONLINE = "online";
  public static final String UNDER_MAINTAIN = "under_maintain";
  public static final String CURRENT_ORDER = "current_order";
  public static final String LEVELCODE = "levelcode";
  public static final String CREATE_TIME = "create_time";
  public static final String LEFT_ELEC_PERCENT = "left_elec_percent";
  public static final String LEFT_OIL_PERCENT = "left_oil_percent";
  public static final String STATIONID = "stationid";
  public static final String BLE_MAC = "ble_mac";
  public static final String BLE_ID = "ble_id";
  public static final String BLE_KEY = "ble_key";
  public static final String OTU_ID = "otu_id";
  
}

