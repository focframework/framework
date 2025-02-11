package com.foc.desc.field;

import java.awt.Component;
import java.sql.Types;

import com.fab.model.table.FieldDefinition;
import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.FocObject;
import com.foc.gui.FGTextArea;
import com.foc.gui.FGTextAreaPanel;
import com.foc.gui.table.cellControler.AbstractCellControler;
import com.foc.gui.table.cellControler.BlobStringCellControler;
import com.foc.property.FBlobStringProperty;
import com.foc.property.FProperty;

public class FBlobStringField extends FStringField {

  private int rows    = 0;
  private int columns = 0;
  
  public FBlobStringField(String name, String title, int id, boolean key, int rows, int columns){
    super(name, title, id, key, 0);
    this.rows = rows;
    this.columns = columns;
    setIncludeInDBRequests(false);
    setShowInDictionary(false);
  }
  
  public FProperty newProperty(FocObject masterObj, Object defaultValue){
    return new FBlobStringProperty(masterObj, getID(), (String)defaultValue);
  }
  
  public static int SqlType() {
    return Types.BLOB;
  }

  public int getSqlType() {
    return SqlType();
  }
  
  public int getFabType() {
    return FieldDefinition.SQL_TYPE_ID_BLOB_STRING;
  }

  public String getCreationString(String name) {
  	if (getProvider()== DBManager.PROVIDER_MSSQL){
//  		return " " + name + " varbinary(MAX)";
  		return " " + name + " nvarchar(MAX)";
  	}else if(getProvider()== DBManager.PROVIDER_ORACLE){
  		return " \"" + name + "\" BLOB";
  	}else if(getProvider()== DBManager.PROVIDER_H2){
  		return " " + name + " VARCHAR";
  	}else{
  		return " " + name + " BLOB";
  	}
  }
  
  private FGTextArea newTextArea(){
    FGTextArea textArea = new FGTextArea();
    textArea.setColumns(columns);
    textArea.setRows(rows);
    textArea.setColumnsLimit(this.getSize());
    textArea.setCapital(isCapital());
    return textArea;
  }
  
  public Component getGuiComponent(FProperty prop){
    FGTextArea textArea = newTextArea();
    if(prop != null) textArea.setProperty(prop);
    FGTextAreaPanel textAreaPanel = new FGTextAreaPanel(textArea, getTitle());    
    return textAreaPanel;
  }
  
  public AbstractCellControler getTableCellEditor_ToImplement(FProperty prop){
    FGTextArea textAreaEditor   = newTextArea();
    FGTextArea textAreaRenderer = newTextArea();
    return new BlobStringCellControler(textAreaEditor, textAreaRenderer); 
  	/*
  	FGTextAreaPanel textAreaPanel = (FGTextAreaPanel) getGuiComponent(prop);
  	JTextArea 			textArea 			= textAreaPanel.getTextArea();
    String 					textAreaText 	= textArea.getText();
    textAreaText = textAreaText.replaceAll("\n", " ");
    return new TextCellControler(new JTextField(textAreaText));
    */
  }
  
  public int getRows(){
  	return rows;
  }
  
  public int getCols(){
  	return columns;
  }  
}
