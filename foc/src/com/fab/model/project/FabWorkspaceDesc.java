package com.fab.model.project;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class FabWorkspaceDesc extends FocDesc {
	
	public static final int FLD_NAME           = FField.FLD_NAME;
	public static final int FLD_WORKSPACE_PATH = 2;
	
	public static final String DB_TABLE_NAME = "FAB_WORKSPACE";
	
	public FabWorkspaceDesc(){
		super(FabWorkspace.class,FocDesc.DB_RESIDENT, DB_TABLE_NAME, false);
		setGuiBrowsePanelClass(FabWorkspaceGuiBrowsePanel.class);
		addReferenceField();
		
		FField nameFld = addNameField();
		nameFld.setMandatory(true);

		FStringField charFld = new FStringField("WORKSPACE_PATH", "Workspace file root", FLD_WORKSPACE_PATH, false, 250);
		addField(charFld);
		charFld.setMandatory(true);
	}

	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE LIST
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
	
	public static FocList getList(int mode){
    return getInstance().getFocList(mode);
  }
  
  public FocList newFocList(){
    FocList list = super.newFocList();
    list.setDirectlyEditable(true);
    list.setDirectImpactOnDatabase(false);
    if(list.getListOrder() == null){
      FocListOrder order = new FocListOrder(FLD_NAME);
      list.setListOrder(order);
    }
    return list;
  }
	
	private static FabWorkspace fabWorkspace = null; 
	public static FabWorkspace getCurrentWorkspace(){
		if(fabWorkspace == null){
			FocList list = getList(FocList.LOAD_IF_NEEDED);
			if(list != null && list.size() > 0){
				fabWorkspace = (FabWorkspace) list.getFocObject(0);
			}
		}
		return fabWorkspace;
	}
	
  //ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  public static FabWorkspaceDesc getInstance() {
    return (FabWorkspaceDesc) getInstance(DB_TABLE_NAME, FabWorkspaceDesc .class);
  }
}
