package com.foc.excel;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FDescFieldStringBased;
import com.foc.desc.field.FField;
import com.foc.list.FocList;

public class FocExcelDesc extends FocDesc {

  public static final int    FLD_TABLE_NAME          = 1;
  public static final int    FLD_EXCEL_FILE_CONTENTS = 2;
  
  public static final String DB_TABLE_NAME = "EXCEL";
  
  public FocExcelDesc(){
    super(FocExcel.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, true);
    
    addReferenceField();
    
    FStringField cfield = new FStringField("FILE_CONTENTS", "File Contents", FLD_EXCEL_FILE_CONTENTS, false, 20000);
    addField(cfield);
    
    FField descField     = new FDescFieldStringBased("TABLE_NAME", "Table", FLD_TABLE_NAME, false);
    descField.setLockValueAfterCreation(true);
    addField(descField);
  }
  
  @Override
  protected void afterConstruction() {
    super.afterConstruction();
    
    FDescFieldStringBased descField = (FDescFieldStringBased) getFieldByID(FLD_TABLE_NAME);
    descField.fillWithAllDeclaredFocDesc();
  }
  
  @Override
  public FocList getFocList(){
    return getFocList(FocList.LOAD_IF_NEEDED);
  }
    
  public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, FocExcelDesc.class);
  }
}
