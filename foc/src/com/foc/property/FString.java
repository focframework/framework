/*
 * Created on 15 fevr. 2004
 */
package com.foc.property;

import java.text.Format;

import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;

/**
 * @author Standard
 */
public class FString extends FProperty implements Cloneable{
  private String str;
  private String backupStr = null;
  
  public FString(FocObject focObj, int fieldID, String str) {
    super(focObj, fieldID);
    this.str = str;
    backupStr = null;
  }

  protected Object clone() throws CloneNotSupportedException {
    FString zClone = (FString)super.clone();
    zClone.setString(str);
    return zClone;
  }
  
  public boolean isEmpty(){
    return getString() == null || getString().trim().compareTo("") == 0;
  }
  
  public String getString() {
    return str;
  }
  
  public void setString_UserEditingEvent(String str) {
  	setString_Internal(str, true);
  }

  public void setString(String str) {
  	setString_Internal(str, false);
  }
  
  public void setString_Internal(String str, boolean userEditingEvent) {
    if(doSetString(str)){
      this.str = str;
      
      if(getFocField() != null && this.str.length() > getFocField().getSize() && getFocField().getSize() > 0){

    		if(			getFocField().isDBResident()
    				&& 	getFocObject() != null 
    				&& 	getFocObject().getThisFocDesc() != null
    				&& 	getFocObject().getThisFocDesc().isDbResident()){
	      	String storageName = getFocObject() != null && getFocObject().getThisFocDesc() != null ? getFocObject().getThisFocDesc().getStorageName() : "";
	      	String message = "Data Will be truncated : "+storageName+"."+getFocField().getName()+" Size = "+this.str.length()+" > Max = "+getFocField().getSize() + "Value="+this.str;
	      	
	      	Globals.showNotification("Text Truncated", message, IFocEnvironment.TYPE_WARNING_MESSAGE);
	      	
	      	Globals.logString("!!! "+message);
    		}
      	
      	this.str = this.str.substring(0, getFocField().getSize()); 
      }
      
      notifyListeners(userEditingEvent);
      //setInherited(false);
    }
  }

  public void setString_WithoutListeners(String str) {
    this.str = str;
  }

  public boolean doSetString(String str){
  	boolean allow = 		(this.str == null && str != null) 
  									|| 	(this.str != null && str != null && this.str.compareTo(str) != 0) /*|| (this.str != null && str == null)*/ ;
  	if(allow){
  		allow = getFocField() == null || getFocField().isPropertyModificationAllowed(this, str, true);
  	}
    return allow; 
  }
  
  @Override
  protected void setSqlStringInternal(String str) {
  	str = str != null ? str.replace("''", "\"") : null;
    setString(str);
  }

  public String getSqlString() {
  	String sqlStr = new String(getString() != null ? getString() : "");
  	if(getProvider() == DBManager.PROVIDER_MSSQL){
//  		try{
//				sqlStr = new String(sqlStr.getBytes(), "UTF-8");
//			}catch (UnsupportedEncodingException e){
//				Globals.logException(e);
//			}
  		sqlStr = sqlStr.replace("'", "''");
  		sqlStr = "N\'" + sqlStr + "\'";
  	}else if(getProvider() == DBManager.PROVIDER_ORACLE){
  		sqlStr = sqlStr.replaceAll("'", "''");
  		sqlStr = sqlStr.replaceAll("\"", "''");
  		sqlStr = "\'" + sqlStr + "\'";
  	}else if(getProvider() == DBManager.PROVIDER_H2){
  		sqlStr = sqlStr.replaceAll("'", "''");
//  		sqlStr = sqlStr.replaceAll("\"", "''");
  		sqlStr = "\'" + sqlStr + "\'";
  	}else{
  		sqlStr = sqlStr.replaceAll("\"", "''");
  		sqlStr = "\"" + sqlStr + "\"";
  	}
    return sqlStr;
  }

  public void setInteger(int iVal) {
    setString(String.valueOf(iVal));
  }

  public int getInteger() {
    return Integer.parseInt(str);
  }

  public void setDouble(double dVal) {
    setString(String.valueOf(dVal));
  }

  public double getDouble() {
  	String String = "0";
  	if(!str.isEmpty()){
  		String = str;
  	}
    return Double.parseDouble(String);
  }

  public void setObject(Object obj) {
    setString((String) obj);
  }

  public Object getObject() {
    return (Object) getString();
  }
  
  public Object getTableDisplayObject(Format format) {
  
    Object displayObj = null;
    String realValue = getString();
    
    displayObj = realValue;
    
    return displayObj;
  }

  public void setTableDisplayObject(Object obj, Format format) {
  	setString_UserEditingEvent((String) obj);
  }

  public void backup() {
    backupStr = str;    
  }
  
  public void restore() {
    str = backupStr != null ? backupStr : "" ;
  }
  
  public void setEmptyValue(){
  	setString("");
  }
}
