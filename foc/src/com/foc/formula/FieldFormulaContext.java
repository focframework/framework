package com.foc.formula;

import java.util.HashMap;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;
import com.foc.property.FAttributeLocationProperty;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;

public class FieldFormulaContext extends FocAbstractFormulaContext implements FPropertyListener {

	private FocDesc baseFocDesc = null;
	private FocObject currentFocObject = null;
	
	public FieldFormulaContext(Formula formula, FFieldPath outputFieldPath, FocDesc baseFocDesc) {
		super(formula, outputFieldPath);
		setBaseFocDesc(baseFocDesc);
		plugListeners();
	}

	public void dispose(){
		super.dispose();
		baseFocDesc = null;
		currentFocObject = null;
	}
	
	private void setBaseFocDesc(FocDesc focDesc){
		this.baseFocDesc = focDesc;
	}
	
	private FocDesc getBaseFocDesc(){
		return this.baseFocDesc;
	}
	
	public void setCurrentFocObject(FocObject focObject){
		this.currentFocObject = focObject;
	}
	
	private FocObject getCurrentFocObject(){
		return this.currentFocObject;
	}

	@Override
	public FocObject getOriginFocObject() {
		return getCurrentFocObject();
	}

	@Override
	public void plugListeners(String expression) {
		plugUnplugListeners(expression, true);
	}

	@Override
	public void unplugListeners(String expression) {
		plugUnplugListeners(expression, false);
	}
	
	private void plugUnplugListeners(String expression, boolean plug){
		if(expression != null){
			FFieldPath fieldPath = FAttributeLocationProperty.newFieldPathUpToOneLevel(false, expression, getBaseFocDesc());
			if(isFieldPathValid(fieldPath)){
				FField field = fieldPath.getFieldFromDesc(getBaseFocDesc());
				if(field != null){
					if(plug){
						field.addListener(this);
					}else{
						field.removeListener(this);
					}
				}
			}else{
				//It's not a legal fieldPath
			}
		}
	}

	public void propertyModified(FProperty property) {
		if(property != null){
			FocObject focObject = property.getFocObject();
			if(focObject != null){
				setCurrentFocObject(focObject);
				compute();
			}
		}
	}

	public String getNewExpression(String oldExpression, HashMap<String, String> oldValuesNewValuedMap) {
		return oldExpression;
	}
}
