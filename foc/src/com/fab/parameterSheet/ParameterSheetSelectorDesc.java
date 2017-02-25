package com.fab.parameterSheet;

import java.util.ArrayList;

import com.foc.desc.FocDesc;
import com.foc.desc.field.FStringField;
import com.foc.desc.field.FField;
import com.foc.desc.field.FMultipleChoiceStringField;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;

public class ParameterSheetSelectorDesc extends FocDesc {
	//public static final int FLD_PARAM_SET_ID = 1;
	public static final int FLD_PARAM_SET_NAME = 2;
	public static final int FLD_TABLE_NAME = 3;

	public static final String DB_TABLE_NAME = "PARAMETER_SET_SLECTOR";
	
	public ParameterSheetSelectorDesc(){
		super(ParameterSheetSelector.class, FocDesc.DB_RESIDENT, DB_TABLE_NAME, true);
		
		setGuiBrowsePanelClass(ParameterSheetSelectorGuiBrowsePanel.class);
		
		FField focFld = addReferenceField();
		
		//focFld = new FIntField("PARAM_SET_ID", "Param set ID", FLD_PARAM_SET_ID, false, 2);
		//addField(focFld);
		
		focFld = new FStringField("PARAM_SET_NAME", "Param set name", FLD_PARAM_SET_NAME, true, 50);
		addField(focFld);
		
		focFld = new FMultipleChoiceStringField("TABLE_NAME", "Table name", FLD_TABLE_NAME, false, 50);
		addField(focFld);
	}
	
	protected void afterConstruction(){
		FMultipleChoiceStringField descFld = (FMultipleChoiceStringField)getFieldByID(ParameterSheetSelectorDesc.FLD_TABLE_NAME);
		if(descFld != null){
			descFld.fillWithAllDeclaredFocDesc();
		}
		//FilterDefinition.fillFDescFieldChoices(descFld);
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
			FocListOrder order = new FocListOrder(FLD_PARAM_SET_NAME);
			list.setListOrder(order);
		}
		return list;		
	}
	
	public static boolean hasParamSheets(){
		FocList list = getList(FocList.LOAD_IF_NEEDED);
		return list != null && list.size() > 0; 
	}

	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // All DESC Fields for Param Set
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
	
	private static ArrayList<FMultipleChoiceStringField> fieldArray = null;
	public static void addStringBasedField(FMultipleChoiceStringField fieldToAdd){
		if(fieldArray == null){
			fieldArray = new ArrayList<FMultipleChoiceStringField>();
		}
		fieldArray.add(fieldToAdd);
	}
	
	public static void refreshAllParamSetFieldChoices(){
		if(fieldArray != null){
			for(int i=0; i<fieldArray.size(); i++){
				FMultipleChoiceStringField field = fieldArray.get(i);
				if(field != null){
					field.re_fillWithParamSetFocDesc();
				}
			}
		}
	}
	
	//ooooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // SINGLE INSTANCE
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo

  public static FocDesc getInstance() {
  	return getInstance(DB_TABLE_NAME, ParameterSheetSelectorDesc.class);
  } 
}
