package com.foc.dataSource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashMap;

import com.fab.model.table.TableDefinition;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.ReferenceChecker;
import com.foc.desc.ReferenceCheckerDelete;
import com.foc.desc.ReferenceCheckerToPutToZero;
import com.foc.list.FocList;

public interface IFocDataSource {
	public IFocDataUtil  getUtility();
	public boolean       isServer();
	public boolean       isEmptyDatabaseJustCreated();
	public void          setEmptyDatabaseJustCreated(boolean emptyDBJustCreated);
	
	public int           getProvider(String dbSouceKey);
	
	public boolean       command_ExecuteRequest(String dbSourceKey, StringBuffer sqlRequest);
	public boolean       command_ExecuteRequest(StringBuffer sqlRequest);
	public boolean       command_AdaptDataModel_SingleTable(FocDesc focDesc);
	public boolean       command_AdaptDataModel_Reindex();
	public boolean       command_AdaptDataModel(boolean forceAlterTables, boolean schemaEmpty);
	public boolean       command_executeRequestForModulesSP(String spFileName);
	public ArrayList     command_Select(FocDesc desc, int fieldID, boolean distinct, StringBuffer filterExpression);
	public ArrayList<String> command_SelectRequest(StringBuffer sqlRequest);
	public ArrayList<String[]> command_SelectRequest(StringBuffer sqlRequest, int nbrColumns);
	public boolean       command_Replace(FocDesc desc, int fieldID, String originalValue, String newValue);
	public boolean       command_CheckTables();
	
  public java.sql.Date command_GetCurrentTimeStamp();
	
	public boolean       focObject_Load(FocObject focObject);
	public boolean       focObject_Load(FocObject focObject, int fieldsArray[]);
	public boolean       focObject_Delete(FocObject focObject, ReferenceChecker refCheck);
	public boolean       focObject_Save(FocObject focObject, int fieldsArray[]);
	public boolean       focObject_Redirect(FocObject initialFocObject, FocObject newFocObject);
	public int           focObject_GetNumberOfReferences(FocObject focObj, StringBuffer message, ReferenceChecker referenceCjeckerToIgnore, ArrayList<ReferenceCheckerToPutToZero> arrayPutToZero, ArrayList<ReferenceCheckerDelete> arrayDelete);
	public int           focObject_GetReference_ForUniqueKey(FocObject focObj);
	public int           focObject_GetReference_ForFilter(FocDesc focDesc, String filterExpression);
	public BufferedImage focObject_LoadImage(FocObject focObject, int fieldID);
	public InputStream   focObject_LoadInputStream(FocObject focObject, int fieldID);
	public boolean       focObject_UpdateImage(FocObject focObject, int fieldID, File file);
	public boolean       focObject_UpdateImage(FocObject focObject, int fieldID);
	public boolean       focObject_addBlobFromFilePath(FocObject obj, int fieldID, String filePath);
	public boolean       focObject_addBlobFromInputStream(FocObject obj, int fieldID, InputStream inputStream);
		
  public boolean       focList_Load(FocList focList);
  public boolean       focList_Join_Load(FocList focList);
  public boolean       focList_Delete(FocList focList);
  
	public void          transaction_SeeIfShouldCommit();
	public void          transaction_setShouldSurroundWithTransactionIfRequest();
	public Object        getDBManagerServer();
	public CallableStatement sp_Call(String name, Object[] params);
	
	public void          executeCustomQuery(StringBuffer sqlRequest, IExecuteResultSet iExecuteResultSet);
	
	public String command_DataModel2Code();
	public StringBuffer getMonitoringText();
}
