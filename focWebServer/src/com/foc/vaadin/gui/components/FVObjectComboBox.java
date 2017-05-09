package com.foc.vaadin.gui.components;

import org.xml.sax.Attributes;

import com.fab.gui.xmlView.IAddClickSpecialHandler;
import com.foc.Globals;
import com.foc.IFocEnvironment;
import com.foc.access.FocDataMap;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FField;
import com.foc.desc.field.FObjectField;
import com.foc.list.FocList;
import com.foc.property.FObject;
import com.foc.property.FProperty;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.ICentralPanel;
import com.foc.vaadin.gui.FVIconFactory;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.objectSelectorPopupView.IObjectSelectWindowListener;
import com.foc.vaadin.gui.layouts.validationLayout.FVValidationLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.vaadin.gui.xmlForm.IValidationListener;
import com.foc.web.dataModel.FocDataItem_ForComboBoxActions;
import com.foc.web.dataModel.FocListWrapper_ForObjectSelection;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class FVObjectComboBox extends ComboBox implements FocXMLGuiComponent {//Needs to implement FocXMLGuiComponent because this is the component returned when event.
  
  private IFocData                    focData  = null;

  private FocXMLGuiComponentDelegate  delegate      = null;//The delegate is the one coming from the selector
  private boolean                     delegateOwner = false;
	private Attributes                  attributes    = null;
	private FocListWrapper              selectionWrapperList = null; 
	private ICentralPanel               openedCentralPanel = null;
	private IObjectSelectWindowListener iObjectSelectWindowListener = null;
	
  public FVObjectComboBox(IFocData objProperty) {
    this(objProperty, (String)null);
    setImmediate(true);
    init();
  }
  
  public FVObjectComboBox(IFocData objProperty, String captionFieldName) {
  	this(objProperty, captionFieldName, null);
  }
  
  public FVObjectComboBox(IFocData objProperty, String captionFieldName, Attributes attributes) {
  	setAttributes(attributes);
    setFocProperty(objProperty, captionFieldName);
    setFilteringMode(FilteringMode.CONTAINS);
    addStyleName("component-margin");
    setImmediate(true);
    init();
  }

  //This is used when no Gear
  public FVObjectComboBox(IFocData objProperty, Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
  	delegateOwner = true;
  	setAttributes(attributes);
  	setFocData(objProperty);
  	setFilteringMode(FilteringMode.CONTAINS);
  	init();
  }
  
  public int getFocObjectRef_ForTheADDIcon(){
  	int ref = 0;
  	if(getSelectionWrapperList() != null && getSelectionWrapperList() instanceof FocListWrapper_ForObjectSelection){
  		ref = ((FocListWrapper_ForObjectSelection)getSelectionWrapperList()).getFocObjectRef_ForTheADDIcon();	
  	}
  	return ref;
  }
  
  public int getFocObjectRef_ForTheREFRESHIcon(){
  	int ref = 0;
  	if(getSelectionWrapperList() != null && getSelectionWrapperList() instanceof FocListWrapper_ForObjectSelection){
  		ref = ((FocListWrapper_ForObjectSelection)getSelectionWrapperList()).getFocObjectRef_ForTheREFRESHIcon();	
  	}
  	return ref;
  }
  
  private void init(){
  	if(Globals.isValo()){
	  	addValueChangeListener(new ValueChangeListener() {
	
				@Override
				public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
					if(event != null && event.getProperty() != null && event.getProperty().getValue() instanceof Integer){
						int valueInteger = Integer.valueOf(event.getProperty().getValue()+"");
						
//						if(valueString == FocListWrapper_ForObjectSelection.REF_ADD_NEW_ITEM && getMainWindow() != null){
						if(isSelected(getFocObjectRef_ForTheADDIcon()) && getMainWindow() != null){
//							addNewObject();
							//We need it like this when we add a new unit then we do cancel, the combo has the 'Add new item' selected.   
							copyMemoryToGui();
							Object backupValue = getValue();
							addNewObject();
							Object newValue = getValue();
							if(newValue instanceof Integer){
								int newSeletedValue = (Integer) newValue;
								if(newSeletedValue == FocDataItem_ForComboBoxActions.REF_ADD){
									setValue(backupValue);
								}
							}
						}else if(isSelected(getFocObjectRef_ForTheREFRESHIcon()) && getMainWindow() != null){
							copyMemoryToGui();
							Object previousValue = getValue();
							reloadList();
							setValue(previousValue);
//							copyMemoryToGui();
//							if(getDelegate() != null && getDelegate().getFocXMLLayout() != null){
//								getDelegate().getFocXMLLayout().refresh();
//							}
							if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue() instanceof Integer){
								Integer propertyReference = (Integer) event.getProperty().getValue();
								if(propertyReference == FocDataItem_ForComboBoxActions.REF_REFRESH){
									unselect(propertyReference);
								}
							}
						}
					}
				}
			});
  	}
  }
  
	public void dispose(){
    focData = null;
    if(delegate != null){
    	if(delegateOwner) delegate.dispose();
    	delegate = null;
    }
    openedCentralPanel = null;
    attributes = null;
    dispose_selectionWrapperList();
    iObjectSelectWindowListener = null;
  }
  
	public void dispose_selectionWrapperList(){
		if(selectionWrapperList != null){
			selectionWrapperList.dispose();
			selectionWrapperList = null;
		}
	}
	
	public void reloadList(){
		FocList focList = getSelectionFocList();
		if(focList != null){
			focList.reloadFromDB();
			refreshGuiForContainerChanges();
		}
	}

	public void refreshGuiForContainerChanges(){
		if(getListWrapper() != null){
			getListWrapper().refreshGuiForContainerChanges();
		}
	}
	
	public FocObject getFocObject_Master(){
		FocObject masterFocObject = null;
		if(getFocData() != null && getFocData() instanceof FObject){
	    FObject objProperty = (FObject) getFocData();
	    if(objProperty != null){
	    	masterFocObject = objProperty.getFocObject();
	    }
		}
    return masterFocObject;
	}
	
  public FocList getSelectionFocList(){
		FocList focList = null;
		IFocData focData = getFocData(); 
		if(focData != null){
			if(focData instanceof FObject){
				focList = ((FObject)focData).getPropertySourceList();
			}else if(getFocData() instanceof FObjectField){
				focList = ((FObjectField)focData).getSelectionList();
			}
		}
		
		return focList;
  }
  
	private FocListWrapper newSelectionFocListWrapper(){
  	dispose_selectionWrapperList();
  	if(getFocData() instanceof FObject){
  		selectionWrapperList = new FocListWrapper_ForObjectSelection(this, (FObject) getFocData());
  	}else if(getFocData() instanceof FObjectField){
  		selectionWrapperList = new FocListWrapper_ForObjectSelection(this, (FObjectField) getFocData());
  	}
    return selectionWrapperList;
  }
	
  public void setFocProperty(IFocData objProperty, String captionFieldName){
  	focData = objProperty;
    if(objProperty != null){
      FObjectField objFld = null;
      if(objProperty instanceof FObjectField){
      	objFld = (FObjectField) objProperty;
      }else if(objProperty instanceof FObject){
      	objFld = (FObjectField) ((FObject)objProperty).getFocField();
      }
      
      if(objFld != null){          
      	if(objFld.getNullValueDisplayString() != null && !objFld.getNullValueDisplayString().isEmpty()){
      		setInputPrompt(objFld.getNullValueDisplayString());
      	}
	      FocListWrapper selectionWrapperList = newSelectionFocListWrapper();
	      if(selectionWrapperList != null){
	        if (captionFieldName == null) {
            int captionFieldID = objFld.getDisplayField();
            FField captionField = objFld.getFocDesc() != null ? objFld.getFocDesc().getFieldByID(captionFieldID) : null;
            captionFieldName = captionField != null ? captionField.getName() : null;
	        }
	        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	        this.setItemCaptionPropertyId(captionFieldName);
        	this.setContainerDataSource(selectionWrapperList);
	      }
      }
    }
		setItemIcon(getFocObjectRef_ForTheADDIcon(), FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_ADD));
		setItemIcon(getFocObjectRef_ForTheREFRESHIcon(), FVIconFactory.getInstance().getFVIcon_Small(FVIconFactory.ICON_REFRESH));
  }
  
  public FocListWrapper getListWrapper(){
  	return (FocListWrapper) getContainerDataSource();
  }
  	
  public IFocData getFocData() {
    return focData;
  }
	
	public FocObject getFocObject_FromGuiCombo(){
		FocObject obj = null;
		try{
			FocListWrapper listWrapper = (FocListWrapper) getContainerDataSource();
			obj = (FocObject) listWrapper.getItem(getValue());//searchByRealReferenceOnly(intReference);
		}catch(Exception e){
			Globals.logException(e);
		}
		return obj;
	}

  public boolean copyGuiToMemory() {
    if(focData instanceof FProperty){
    	boolean copy = true;
    	if(getValue() instanceof Integer){
    		int intValue = (Integer)getValue();
    		copy = intValue != FocDataItem_ForComboBoxActions.REF_REFRESH && intValue != FocDataItem_ForComboBoxActions.REF_ADD; 
    	}
    	if(copy){
    		((FProperty)focData).setValue(getValue());
    	}
    }
    return false;
  }
  
  public void copyMemoryToGui() {
    if(focData instanceof FProperty){
    	setValue(((FProperty)focData).getValue());
    }
  }
  
	public void applyAttributes(){
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
	}

  public void setFocData(IFocData focData) {
    this.focData = focData;
   	setFocProperty(focData, getCaptionProperty());
  }

	@Override
	public String getXMLType() {
		return null;
	}

	@Override
	public Field getFormField() {
		return this;
	}

	@Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    applyAttributes();
  }
	
	@Override
  public Attributes getAttributes() {
    return attributes;
  }

	@Override
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate; 
	}

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}
	
	@Override
  public String getValueString() {
  	String value = null;
  	try{
  		if(getValue() != null){
		  	int ref = (Integer) getValue();
		    FocList list = getSelectionFocList();
		    if(list != null){
		    	FocObject obj = list.searchByReference(ref);
		    	FProperty prop = obj != null ? obj.getFocPropertyByName(getItemCaptionPropertyId().toString()) : null;
		    	value = prop != null ? prop.getString() : null;
		    }
  		}
  	}catch(Exception e){
  		value = null;
  		Globals.logExceptionWithoutPopup(e);
  	}
    return value;
  }

  @Override
  public void setValueString(String value) {
  	FocListWrapper listWrapper = getListWrapper();
    if(listWrapper != null){
    	FocObject obj = listWrapper.searchByPropertyValue(getItemCaptionPropertyId().toString(), value);
      if(obj == null){
    		select(0);
      }else{
    		select(obj.getReference().getInteger());
      }
    }
  }
  
	private String getCaptionProperty(){
		String captionProp = null;
		Attributes att = getAttributes();
		if(att != null){
			captionProp = att.getValue(FXML.ATT_CAPTION_PROPERTY);
		}
		return captionProp;
	}

	@Override
	public void setDescription(String description) {
		super.setDescription(description);
	}
	
	@Override
	public void refreshEditable() {
		setEnabled(getDelegate() != null ? getDelegate().isEditable() : true);
	}
	
	public FocObject addNewObject(){
		FocObject focObjToOpen = null;
		FocDesc desc = null;
		FocList list = getSelectionFocList();
		if(list != null){
			desc = list.getFocDesc();
			IAddClickSpecialHandler handler = XMLViewDictionary.getInstance().getAddClickSpecialHandler(desc.getStorageName());
			if(handler != null){
				handler.addClicked(getMainWindow(), this);
			}else{
				focObjToOpen = createNewItemAndOpenForm(true);
			}
		}
		return focObjToOpen;
	}
	
	public FocObject createNewItemAndOpenForm(boolean adaptViewKeyWhenObjectCreated){
		FocObject focObjToOpen = null;
		FocList list = getSelectionFocList();
		if(list != null){
			focObjToOpen = list.newEmptyItem();
			if(getSelectionWrapperList() != null){
				getSelectionWrapperList().adjustPropertiesForNewItemAccordingTofilter(focObjToOpen);
			}
				
			if(getiObjectSelectWindowListener() != null){
				getiObjectSelectWindowListener().beforeOpenForm(focObjToOpen);
			}
			openObjectDetailsPanel(focObjToOpen, adaptViewKeyWhenObjectCreated);
		}
		return focObjToOpen;
	}
	
	public void openObjectDetailsPanel(FocObject focObjToOpen, boolean adaptViewKeyWhenObjectCreated){
		XMLViewKey key = new XMLViewKey(focObjToOpen.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
		
		try{
			openedCentralPanel = XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, focObjToOpen, true, true, adaptViewKeyWhenObjectCreated);
		}catch(Exception e){
			Globals.showNotification("View Open Error", "Could not open view", IFocEnvironment.TYPE_HUMANIZED_MESSAGE);
			Globals.logString("FVObjectComboBox.openObjectDetailsPanel");
			Globals.logExceptionWithoutPopup(e);//We already gave a more meaningful message to the user.
		}
		
		if(openedCentralPanel != null){
			getMainWindow().changeCentralPanelContent(openedCentralPanel, true);
			copyMemoryToGui();
			if(openedCentralPanel.getValidationLayout() != null){
				((FocXMLLayout) openedCentralPanel).getValidationLayout().addValidationListener(new IValidationListener() {
					
					@Override
					public void validationDiscard(FVValidationLayout validationLayout) {
					}
					
					@Override
					public boolean validationCommit(FVValidationLayout validationLayout) {
						reloadList();		
						return false;
					}
		
					@Override
					public void validationAfter(FVValidationLayout validationLayout, boolean commited) {
						FocObject newFocObject = null;
						
						IFocData focData = openedCentralPanel != null ? openedCentralPanel.getFocData() : null;
						if(focData != null){
							if(focData instanceof FocDataMap){
								focData = ((FocDataMap)focData).getMainFocData();
							}
							
							if(focData != null && focData instanceof FocObject){
								newFocObject = (FocObject) focData;
								if(newFocObject != null){
									int ref = newFocObject.getReference().getInteger();
									select(ref);
								}
							}
						}
					}
				});
			}
		}
	}
	
	public INavigationWindow getMainWindow(){
		return getWindow() != null ? getWindow() : findAncestor(FocCentralPanel.class);
	}

	public INavigationWindow getWindow(){
		return getDelegate() != null && getDelegate().getFocXMLLayout() != null ? getDelegate().getFocXMLLayout().getMainWindow() : null;
	}

	public FocListWrapper getSelectionWrapperList() {
		return selectionWrapperList;
	}
	
	@Override
	public void setValue(Object newValue) throws com.vaadin.data.Property.ReadOnlyException {
		super.setValue(newValue);
	}

	public IObjectSelectWindowListener getiObjectSelectWindowListener() {
		return iObjectSelectWindowListener;
	}

	public void setiObjectSelectWindowListener(IObjectSelectWindowListener iObjectSelectWindowListener) {
		this.iObjectSelectWindowListener = iObjectSelectWindowListener;
	}
}
