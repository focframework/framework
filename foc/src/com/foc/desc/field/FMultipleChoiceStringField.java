package com.foc.desc.field;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.fab.FabStatic;
import com.fab.model.table.FieldDefinition;
import com.fab.parameterSheet.ParameterSheetSelector;
import com.fab.parameterSheet.ParameterSheetSelectorDesc;
import com.foc.Globals;
import com.foc.IFocDescDeclaration;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.gui.FGComboBox;
import com.foc.gui.table.cellControler.AbstractCellControler;
import com.foc.gui.table.cellControler.ComboBoxCellControler;
import com.foc.gui.table.cellControler.editor.FDefaultComboBoxCellEditor;
import com.foc.list.FocList;
import com.foc.property.FMultipleChoiceString;
import com.foc.property.FProperty;

public class FMultipleChoiceStringField extends FStringField {
	
	private HashMap<String, FMultipleChoiceItem> choicesArray = null;
	private boolean      sortItem                         = true;
	private boolean      allowOutofListSelection          = false;
	private FocDesc      choicesSelection_FocDesc         = null;
	private int          choicesSelection_FieldID         = FField.NO_FIELD_ID; 
	private StringBuffer filterExpressionForChoicesSelect = null;     
	private ArrayList    choicesArrayList                 = null;
	private boolean      needRefresh                      = false;
	private boolean      useSameColumnValues              = false;
	
	public FMultipleChoiceStringField(String name, String title, int id, boolean key, int size) {
		super(name, title, id, key, size);
	}
	
	public void dispose(){
		super.dispose();
		if(choicesArray != null){
			choicesArray.clear();
			choicesArray = null;
		}
		needRefresh = false;
		dispose_ChoicesArrayList();
		filterExpressionForChoicesSelect = null;
	}
	
	public void dispose_ChoicesArrayList(){
		if(choicesArrayList != null){
			choicesArrayList.clear();
			choicesArrayList = null;
		}
	}
	
  @Override
  public int getFabType() {
    return FieldDefinition.SQL_TYPE_ID_MULTIPLE_CHOICE_STRING_BASED;
  }

	private int getNextChoiceId(){
		return choicesArray == null ? 1 : choicesArray.size() + 1;
	}
	
	public void refreshChoicesArray(){
		ArrayList arrayList = getChoicesArrayList();
		if(isChoicesAreFromSameColumn()){
			arrayList = arrayList == null || isNeedRefresh() ? Globals.getApp().getDataSource().command_Select(getChoicesSelection_FocDesc(), getChoicesSelection_FieldID(), true, getFilterExpressionForChoicesSelect()) : arrayList;
//			arrayList = Globals.getApp().getDataSource().command_Select(focDescForChoicesSelect, this.getID(), true, getFilterExpressionForChoicesSelect());
			setChoicesArrayList(arrayList);
			if(isNeedRefresh()){
				setNeedRefresh(false);
			}
			try{
				Collections.sort(arrayList);
			}catch(Exception e){
				Globals.logException(e);
			}
			for(int i=0; i<arrayList.size(); i++){
				addChoice((String)arrayList.get(i));
			}
		}
	}
	
	public Iterator<FMultipleChoiceItem> getChoicesIterator(){
		return choicesArray != null ? choicesArray.values().iterator() : null;
	}
	
	public FProperty newProperty(FocObject masterObj, Object defaultValue){
   return new FMultipleChoiceString(masterObj, getID(), defaultValue != null ? (String)defaultValue : null);
  }

  public FProperty newProperty(FocObject masterObj){
    return newProperty(masterObj, null); 
  }
  
  private HashMap<String, FMultipleChoiceItem> getChoicesArray(){
  	if(this.choicesArray == null){
  		this.choicesArray = new HashMap<String, FMultipleChoiceItem>();
  		addChoice("");
  	}
  	return this.choicesArray;
  }
  
  public void addChoice(String choice){
  	if(getChoicesArray().get(choice) == null){
	  	FMultipleChoiceItem multipleChoiceItem = new FMultipleChoiceItem(getNextChoiceId(), choice);
	  	getChoicesArray().put(choice, multipleChoiceItem);
	  }
  }
  
  public void removeChoice(String choice){
  	if(choice != null){
	  	FMultipleChoiceItem multipleChoice = null;
	  	boolean found = false;
	  	Iterator<FMultipleChoiceItem> iter = getChoicesIterator();
	  	while(iter != null && iter.hasNext() && !found){
	  		multipleChoice = iter.next();
	  		if(multipleChoice != null){
	  			String title = multipleChoice.getTitle();
	  			if(choice.equals(title)){
	  				found = true;
	  			}
	  		}
	  	}
	  	if(multipleChoice != null){
	  		getChoicesArray().remove(multipleChoice);
	  	}
  	}
  }
  
  public void removeAllChoices(){
  	getChoicesArray().clear();
  }
  
  public Component getGuiComponent(FProperty prop){
  	//Iterator<FMultipleChoiceItem> choices = this.getChoicesIterator();
  	refreshChoicesArray();
  	Iterator<FMultipleChoiceItem> choices = null;
  	if(prop != null){
  		choices = ((FMultipleChoiceString) prop).getChoiceIterator();
  	}else{
  		choices = this.getChoicesIterator();
  	}
    FGComboBox comboBox = new FGComboBox(choices, isSortItems());
    comboBox.setAllowOutOfListSelection(isAllowOutofListSelection());
    if(prop != null) comboBox.setProperty(prop);
    return comboBox;
  }
  
  public AbstractCellControler getTableCellEditor_ToImplement(FProperty prop){
		//Iterator<FMultipleChoiceItem> choices = this.getChoicesIterator();
  	refreshChoicesArray();
  	Iterator<FMultipleChoiceItem> choices = null;
  	if(prop != null){
  		choices = ((FMultipleChoiceString) prop).getChoiceIterator();
  	}else{
  		choices = this.getChoicesIterator();
  	}
  	ComboBoxCellControler comboBoxCellControler = new ComboBoxCellControler(choices, isSortItems());
  	FDefaultComboBoxCellEditor editor = (FDefaultComboBoxCellEditor) comboBoxCellControler.getEditor();
  	editor.getComboBox().setAllowOutOfListSelection(isAllowOutofListSelection());
    return comboBoxCellControler;
  }
  
  public void setSortItems(boolean sort){
  	this.sortItem = sort;
  }
  
  public boolean isSortItems(){
  	return this.sortItem;
  }

	public boolean isAllowOutofListSelection() {
		return allowOutofListSelection;
	}

	public void setAllowOutofListSelection(boolean allowOutofListSelection) {
		this.allowOutofListSelection = allowOutofListSelection;
	}

	public boolean isChoicesAreFromSameColumn() {
		return choicesSelection_FocDesc != null || isUseSameColumnValues();
	}

	public void setChoicesAreFromSameColumn(FocDesc focDescForChoicesSelect) {
		this.choicesSelection_FocDesc = focDescForChoicesSelect;
		if(this.choicesSelection_FieldID == FField.NO_FIELD_ID) this.choicesSelection_FieldID = getID();
		if(focDescForChoicesSelect != null){
			setAllowOutofListSelection(true);
		}
	}

	public StringBuffer getFilterExpressionForChoicesSelect() {
		return filterExpressionForChoicesSelect;
	}

	public void setFilterExpressionForChoicesSelect(StringBuffer whereConditionForChoicesSelect) {
		this.filterExpressionForChoicesSelect = whereConditionForChoicesSelect;
	}

	private ArrayList getChoicesArrayList() {
		return choicesArrayList;
	}

	private void setChoicesArrayList(ArrayList choicesArrayList) {
		this.choicesArrayList = choicesArrayList;
	}
	
	public boolean isNeedRefresh() {
		return needRefresh;
	}

	public void setNeedRefresh(boolean needRefresh) {
		this.needRefresh = needRefresh;
	}

	public int getChoicesSelection_FieldID() {
		if(choicesSelection_FieldID == FField.NO_FIELD_ID && isUseSameColumnValues()){
			choicesSelection_FieldID = getID();
		}
		return choicesSelection_FieldID;
	}

	public void setChoicesSelection_FieldID(int choicesSelection_FieldID) {
		this.choicesSelection_FieldID = choicesSelection_FieldID;
	}

	public FocDesc getChoicesSelection_FocDesc() {
		if(choicesSelection_FocDesc == null && isUseSameColumnValues()){
			choicesSelection_FocDesc = getFocDescParent();
		}
		return choicesSelection_FocDesc;
	}

	public void setChoicesSelection_FocDesc(FocDesc choicesSelection_FocDesc) {
		this.choicesSelection_FocDesc = choicesSelection_FocDesc;
	}

	public boolean isUseSameColumnValues() {
		return useSameColumnValues;
	}

	public void setUseSameColumnValues(boolean useSameColumnValues) {
		this.useSameColumnValues = useSameColumnValues;
	}
}
