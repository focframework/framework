package com.foc.desc.parsers.predefinedFields;

import java.util.HashMap;

public class FocPredefinedFieldFactory {
	
	private HashMap<String, IFocPredefinedFieldType> map = null;
	
	private FocPredefinedFieldFactory(){
		map = new HashMap<String, IFocPredefinedFieldType>();
		
		put(new FTypeCODE());
		put(new FTypeNAME());
		put(new FTypeEXTERNAL_CODE());
		put(new FTypeDESCRIPTION());
		put(new FTypeCOMPANY());
		put(new FTypeSITE());
		put(new FTypeDATE());
		put(new FTypeORDER());
		put(new FTypeNOT_COMPLETED_YET());
		put(new FTypeDEPRECATED());
		put(new FTypeIS_SYSTEM());
	}

	public void put(IFocPredefinedFieldType type) {
		map.put(type.getTypeName(), type);
	}
	
	public IFocPredefinedFieldType get(String typeName) {
		return map != null ? map.get(typeName) : null;
	}

	private static FocPredefinedFieldFactory instance = null;
	public static FocPredefinedFieldFactory getInstance(){
		if(instance == null){
			instance = new FocPredefinedFieldFactory();
		}
		return instance;
	};
	
}
