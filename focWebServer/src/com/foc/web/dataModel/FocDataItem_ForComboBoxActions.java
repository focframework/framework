package com.foc.web.dataModel;

import java.util.ArrayList;
import java.util.Collection;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.property.FProperty;
import com.foc.property.FString;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class FocDataItem_ForComboBoxActions extends FocObject {

	private String               propertyPath = null; 
	private FString              property     = null;
	private ArrayList<FProperty> coll         = null; 
	
	public static final int    ACTION_TYPE_ADD        = 0;
	public static final int    ACTION_TYPE_REFRESH    = 1;
	public static final String ACTION_CAPTION_ADD     = "Add New...";
	public static final String ACTION_CAPTION_REFRESH = "Refresh";
	
	public static final long   REF_ADD           = -1000000;
	public static final long   REF_REFRESH       = -2000000;
	
	public FocDataItem_ForComboBoxActions(String propertyPath, int actionType){
		super(new FocDataItem_FocDesc_ForComboBoxActions());
		this.propertyPath = propertyPath;
		if(actionType == ACTION_TYPE_ADD){
			property = new FString(null, -1, ACTION_CAPTION_ADD);
			setReference(REF_ADD);
		}else if(actionType == ACTION_TYPE_REFRESH){
			property = new FString(null, -2, ACTION_CAPTION_REFRESH);
			setReference(REF_REFRESH);
		}
	}
	
	public void dispose(){
		if(property != null){
			property.dispose();
			property = null;
		}
		if(coll != null){
			coll.clear();
			coll = null;
		}
	}

	@Override
	public Property<FProperty> getItemProperty(Object id) {
		return (propertyPath != null && id != null && id.equals(propertyPath)) ? property : null;
	}

	@Override
	public Collection getItemPropertyIds() {
		if(coll == null){
			coll = new ArrayList<FProperty>();
			coll.add(property);
		}
		return coll;
	}

	@Override
	public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
		return false;
	}

	@Override
	public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
		return false;
	}
	
	public static class FocDataItem_FocDesc_ForComboBoxActions extends FocDesc {
		public FocDataItem_FocDesc_ForComboBoxActions(){
			super(null);
			addReferenceField();
		}
	}
}
