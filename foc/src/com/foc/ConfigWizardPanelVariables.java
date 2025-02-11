package com.foc;

public class ConfigWizardPanelVariables {

  private boolean existingEnv = true;
  private boolean configFieldsEmpty = true;
  private boolean directoryPathEmpty = true;
  private String directoryPath = null;
  private String newEnvironment = null;
  private String existingEnvironment = null;
  private String localhost = null;
  private String port = null;
  private String schema = null;
  private String username = null;
  private String password = null;
  
  public String getLocalhost() {
    return localhost;
  }

  public void setLocalhost(String localhost) {
    this.localhost = localhost;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isExistingEnv() {
    return existingEnv;
  }

  public void setExistingEnv(boolean existingEnv) {
    this.existingEnv = existingEnv;
  }

  public String getDirectoryPath() {
    return directoryPath == null ? Globals.getApp().getDefaultAppDirectory() : directoryPath;
  }

  public void setDirectoryPath(String directoryPath) {
    this.directoryPath = directoryPath;
  }

  public String getNewEnvironment() {
    return newEnvironment;
  }

  public void setNewEnvironment(String newEnvironment) {
    this.newEnvironment = GuiConfigInfo.ENVIRONMENT_PREFIX + newEnvironment;
  }

  public String getExistingEnvironment() {
    return existingEnvironment;
  }

  public void setExistingEnvironment(String existingEnvironment) {
    this.existingEnvironment = GuiConfigInfo.ENVIRONMENT_PREFIX + existingEnvironment;
  }

  public boolean isConfigFieldsEmpty() {
    return configFieldsEmpty;
  }

  public void setConfigFieldsEmpty(boolean emptyConfigField) {
    this.configFieldsEmpty = emptyConfigField;
  }

  public boolean isDirectoryPathEmpty() {
    return directoryPathEmpty;
  }

  public void setDirectoryPathEmpty(boolean directoryPathEmpty) {
    this.directoryPathEmpty = directoryPathEmpty;
  }
  
  
}
