package com.foc.db.sync.model;

import java.util.ArrayList;

import com.foc.desc.FocDesc;
import com.foc.desc.FocFieldEnum;
import com.foc.desc.field.FField;
import com.foc.list.FocLinkSimple;
import com.foc.list.FocList;

public class SyncTable {

	private FocDesc           focDesc    = null;
	private ArrayList<FField> fieldArray = null;
	private FocList           focList    = null;

	//Used when uploading from Remote
	public SyncTable(FocDesc focDesc){
		this.focDesc = focDesc;
	}
	
	public void dispose(){
		focDesc = null;
		if(focList != null){
			focList.dispose();
			focList = null;
		}
		if(fieldArray != null){
			fieldArray.clear();
			fieldArray = null;
		}
	}
	
	public void fillFromDatabase(){
		focList = new FocList(new FocLinkSimple(focDesc));
		focList.getFilter().setAdditionalWhere(new StringBuffer(FField.FNAME_SYNC_IS_NEW_OBJECT+"=1"));
		focList.loadIfNotLoadedFromDB();
		
		fieldArray = new ArrayList<FField>();
		
		FocFieldEnum fldEnum = focDesc.newFocFieldEnum(FocFieldEnum.CAT_ALL_DB, FocFieldEnum.LEVEL_DB);
		while(fldEnum != null && fldEnum.hasNext()){
			FField fld = fldEnum.nextField();
			if(fld.getID() != FField.FLD_SYNC_IS_NEW_OBJECT){
				fieldArray.add(fld);
			}
		}
	}
}
