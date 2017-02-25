package com.foc.business.printing;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class PrnContextDesc extends FocDesc {
	public static final int FLD_NAME             = FField.FLD_NAME;
	public static final int FLD_DESCRIPTION      = FField.FLD_DESCRIPTION;
	public static final int FLD_DB_NAME          = 1;	
	public static final int FLD_LAYOUT_LIST      = 2;
	
	public static final String DB_TABLE_NAME = "PRN_CONTEXT";
	
	public static final int LEN_CONTEXT_DB_NAME = 30;
  
	public PrnContextDesc(){
		super(PrnContext.class, FocDesc.NOT_DB_RESIDENT, DB_TABLE_NAME, true);
		setGuiBrowsePanelClass(PrnContextGuiBrowsePanel.class);
		setGuiDetailsPanelClass(PrnContextGuiDetailsPanel.class);
		addReferenceField();
		
		addNameField();
		addDescriptionField();
		
		FStringField cFld = new FStringField("DB_NAME", "Database Name", FLD_DB_NAME, false, LEN_CONTEXT_DB_NAME);
		addField(cFld);
  }
	
	public static FObjectField newReportField(int fieldID, boolean key){
		FObjectField objFld = new FObjectField("PRN_REPORT", "Report", fieldID, PrnContextDesc.getInstance());
	  objFld.setComboBoxCellEditor(PrnContextDesc.FLD_NAME);
	  objFld.setSelectionList(PrnContextDesc.getList(FocList.NONE));
	  return objFld;
	}
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE LIST
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
	
	public static FocList getList(int mode){
    return getInstance().getFocList(mode);
  }
  
  @Override
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(false);
    list.setDirectImpactOnDatabase(true);
    if(list.getListOrder() == null){
      FocListOrder order = new FocListOrder(FLD_NAME);
      list.setListOrder(order);
    }
    return list;
  }
	
	public static PrnContext getReport_ForDBName(String name){
		FocList   list = getList(FocList.LOAD_IF_NEEDED);
		PrnContext rep  = null;
		if(list != null){
			rep = (PrnContext) list.searchByPropertyStringValue(PrnContextDesc.FLD_DB_NAME, name);
		}
		return rep;
	}
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

	public static FocDesc getInstance() {
    return getInstance(DB_TABLE_NAME, PrnContextDesc.class);    
  }
}
