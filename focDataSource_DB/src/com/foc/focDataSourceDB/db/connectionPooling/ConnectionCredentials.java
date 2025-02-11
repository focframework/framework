package com.foc.focDataSourceDB.db.connectionPooling;

import java.util.StringTokenizer;

import com.foc.ConfigInfo;
import com.foc.Globals;
import com.foc.db.DBManager;

public class ConnectionCredentials {
	private String dbSourceKey = null;
	
  private String drivers   = null;
  private String url       = null;
  private String username  = null;
  private String password  = null;
  private String xpassword = null;      
  
  private int    provider  = DBManager.PROVIDER_MYSQL;

  public ConnectionCredentials(){
    drivers   = null;
    url       = null;
    username  = null;
    password  = null;
    xpassword = null;  	
  }
  
  public void dispose(){
    drivers   = null;
    url       = null;
    username  = null;
    password  = null;
    xpassword = null;
  }
  
  private void adjustProvider(){
  	if(url != null){
			if(url.startsWith("jdbc:sqlserver")){
				setProvider(DBManager.PROVIDER_MSSQL);
			}else if(url.startsWith("jdbc:oracle:thin")){
				setProvider(DBManager.PROVIDER_ORACLE);
			}else if(url.startsWith("jdbc:h2")){
				setProvider(DBManager.PROVIDER_H2);
			}else{
				setProvider(DBManager.PROVIDER_MYSQL);
			}
  	}
  }
  
  public void fillCredentialsIfNeeded(){
  	setDrivers(ConfigInfo.getJdbcDrivers());
    setUrl(ConfigInfo.getJdbcURL());
    setUsername(ConfigInfo.getJdbcUserName());
    setPassword(ConfigInfo.getJdbcPassword());
    setXpassword(ConfigInfo.getJdbcXPassword());
    
    StringTokenizer tokenizer = url != null ? new StringTokenizer(url, ":", false) : null;
    for(int count = 0; count < 2 && tokenizer.hasMoreTokens(); count++){
      String token = tokenizer.nextToken();

      if(count == 1){
        if (token.equals("oracle")){
          setProvider(DBManager.PROVIDER_ORACLE);
        }else if (token.equals("mysql")){
          setProvider(DBManager.PROVIDER_MYSQL);
        }
      }
    }

    String dbHost = Globals.getApp().getCommandLineArgument("dbHost");
    if(dbHost != null && !dbHost.isEmpty()){
    	if(getProvider() == DBManager.PROVIDER_MYSQL){
    		String newURL  = url.substring(0, 13);
    		int    indexOf = url.indexOf(':', 14); 
    		newURL += dbHost;
    		newURL += url.substring(indexOf);
    		url = newURL;
    	}else if(getProvider() == DBManager.PROVIDER_ORACLE){
    		String newURL  = url.substring(0, 17);
    		int    indexOf = url.indexOf(':', 18); 
    		newURL += dbHost;
    		newURL += url.substring(indexOf);
    		url = newURL;
    	}
    }

    String dbPort = Globals.getApp().getCommandLineArgument("dbPort");
    if(dbPort != null && !dbPort.isEmpty()){
    	if(getProvider() == DBManager.PROVIDER_MYSQL){
    		int    indexOf2Points = url.indexOf(':', 14); 
    		int    indexOfSlash   = url.indexOf('/', indexOf2Points);
    		String newURL = url.substring(0, indexOf2Points+1);      		
    		newURL += dbPort.trim();
    		newURL += url.substring(indexOfSlash);
    		url = newURL;
    	}else if(getProvider() == DBManager.PROVIDER_ORACLE){
    		int    indexOf2Points = url.indexOf(':', 18);
    		int    indexOfSlash   = url.indexOf('/', indexOf2Points+1);
    		String newURL  = url.substring(0, indexOf2Points);
    		newURL += dbPort.trim();
    		newURL += url.substring(indexOfSlash);
    		url = newURL;
    	}
    }

    String dbSchema = Globals.getApp().getCommandLineArgument("dbSchema");
    if(dbSchema != null && !dbSchema.isEmpty()){
    	if(getProvider() == DBManager.PROVIDER_MYSQL){
    		int    lastSlash = url.lastIndexOf('/');
    		if(lastSlash > 0){
      		String newURL    = url.substring(0, lastSlash+1);
      		newURL += dbSchema;
      		url = newURL;
    		}
    	}else if(getProvider() == DBManager.PROVIDER_ORACLE){
    		int    lastSlash = url.lastIndexOf(':');
    		if(lastSlash > 0){
      		String newURL    = url.substring(0, lastSlash+1);
      		newURL += dbSchema;
      		url = newURL;
    		}
    	}
    }
    
    Globals.logString("Connecting to url : "+url+" user : "+username);
  }

	public int getProvider() {
		return provider;
	}

	public void setProvider(int provider) {
		this.provider = provider;
	}

	public String getDrivers() {
		return drivers;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getXpassword() {
		return xpassword;
	}

	public String getDbSourceKey() {
		return dbSourceKey;
	}

	public void setDbSourceKey(String dbSourceKey) {
		this.dbSourceKey = dbSourceKey;
	}

	public void setDrivers(String drivers) {
		this.drivers = drivers;
	}

	public void setUrl(String url) {
		this.url = url;
		adjustProvider();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setXpassword(String xpassword) {
		this.xpassword = xpassword;
	}
}
