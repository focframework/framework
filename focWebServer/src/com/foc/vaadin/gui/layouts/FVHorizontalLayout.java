package com.foc.vaadin.gui.layouts;

import org.xml.sax.Attributes;

import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.windows.EditFieldsWindow;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import fi.jasoft.dragdroplayouts.DDHorizontalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;

@SuppressWarnings("serial")
public class FVHorizontalLayout extends DDHorizontalLayout implements FVLayout {
  private String name;
  private String type;
  private boolean isDragDropEnabled;
  private Attributes attributes = null;
  
  private FocXMLGuiComponentDelegate delegate = null;
  
  public FVHorizontalLayout(Attributes attributes) {
  	delegate = new FocXMLGuiComponentDelegate(this);
  	setSpacing(true);
  	
    setAttributes(attributes);
    
    addLayoutClickListener(listener);

    addStyleName(FVLayout.DEFAULT_STYLE);
  }
  
  @Override
  public void setWYSIWYGDropHandler() {
  	setDropHandler(new FVHorizontal_WYSIWYG_DropHandler(this));
  }
  
  private LayoutClickListener listener = new LayoutClickListener() {
    
    @Override
    public void layoutClick(LayoutClickEvent event) {
      Component child = event.getChildComponent();
      
      if (event.isDoubleClick()) {
        if (child != null && isDragDropEnabled) {
          EditFieldsWindow editFieldsWindow = EditFieldsWindow.getInstanceForThread();
          editFieldsWindow.addTab(null, child, FVHorizontalLayout.this, name, ((FocXMLGuiComponent) child).getAttributes(), false);
        }
      }
    }
  };
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
	public FocXMLLayout getFocXMLLayout() {
		return findAncestor(FocXMLLayout.class);
	}
  
  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public void addComponent(Component comp, Attributes attributes) {
    String idx = attributes != null ? attributes.getValue(FXML.ATT_IDX) : null;
    
    int idxInt = -1;
    try{
      idxInt = idx != null ? Integer.parseInt(idx)+1 : -1;
    }catch(Exception e){
    }
    
    if(idxInt >= getComponentCount()) idxInt = -1; 
    
    if (idxInt < 0) {
    	if(getFocXMLLayout() != null && getFocXMLLayout().isRTL()){
    		addComponentAsFirst(comp);
    	}else{
    		addComponent(comp);
    	}
    } else {
      addComponent(comp, idxInt);
    }
    
    if(attributes != null && attributes.getValue(FXML.ATT_ALIGNMENT) != null) {
      FocXMLGuiComponentStatic.applyAlignment(this, comp, attributes.getValue(FXML.ATT_ALIGNMENT));
    }
  }

  @Override
  public void setDragDrop(boolean state) {
    if (state) {
      setDragMode(LayoutDragMode.CLONE);
      isDragDropEnabled = true;
    }
    else {
      setDragMode(LayoutDragMode.NONE);
      isDragDropEnabled = false;
    }
  }

  @Override
  public boolean isDragDrop() {
    return isDragDropEnabled;
  }

  @Override
  public ComponentPosition getPosition(Component comp) {
    return null;
  }
  
  @Override
  public void setAttributes(Attributes attributes) {
    this.attributes = attributes;
    FocXMLGuiComponentStatic.applyAttributes(this, attributes);
    
    if(getCaption() == null || getCaption().isEmpty()){
    	setCaption("");
    	addStyleName("margination");
    }
    
    if(attributes != null && attributes.getValue(FXML.ATT_IMAGE_DIR) != null){
    	FocXMLGuiComponentStatic.applyLayoutBackgroundImageAttributes(this, attributes);
    }
  }
  
  @Override
  public Attributes getAttributes() {
    return attributes;
  }
  
  @Override
  public String getXMLType() {
    return FXML.TAG_HORIZONTAL_LAYOUT;
  }

  @Override
  public boolean isXMLLeaf() {
    return false;
  }

  @Override
  public void fillXMLNodeContent(XMLBuilder builder) {
  }

	@Override
	public IFocData getFocData() {
		return null;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
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
  public void dispose(){
    name       = null;
    type       = null;
    attributes = null;
		if(delegate != null){
			delegate.dispose();
			delegate = null;
		}    
		removeAllComponents();
  }

  @Override
  public void setFocData(IFocData focData) {
  }
  
  @Override
  public String getValueString() {
    return null;
  }

  @Override
  public void setValueString(String value) {
  }

	@Override
	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}
	
	@Override
	public void refreshEditable() {
	}
}
