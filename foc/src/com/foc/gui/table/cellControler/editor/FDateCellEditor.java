package com.foc.gui.table.cellControler.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import com.foc.gui.table.FAbstractTableModel;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;

/**
 * @author 01Barmaja
 */

@SuppressWarnings("serial")
public class FDateCellEditor extends DefaultCellEditor implements CellEditorListener {
  
  private FProperty specialNodeEditorProperty = null;
  private FPropertyListener nodeEditorPropertyListener = null;
  
  public FDateCellEditor() {
    super(new JTextField());
    addCellEditorListener(this);
  }
  
  public void dispose() {
    unPlugPropertyListener(true);
  }
  
  private void unPlugPropertyListener(boolean withDispose){
    if( specialNodeEditorProperty != null ){
      specialNodeEditorProperty.removeListener(nodeEditorPropertyListener);
    }
    
    if( withDispose ){
      specialNodeEditorProperty = null;
      nodeEditorPropertyListener = null;  
    }
  }
  
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
    Component component = super.getTableCellEditorComponent(table, value, isSelected, r, c);
    FAbstractTableModel model = (FAbstractTableModel) table.getModel();
    int modelCol = model.getTableView().getVisibleColumnIndex(c);
    specialNodeEditorProperty = model.getFProperty(r, modelCol);
    if( specialNodeEditorProperty != null ){
      specialNodeEditorProperty.addListener(getNodeEditorPropertyListener());
      component = specialNodeEditorProperty.getGuiComponent();
    }
    return component;
  }
  
  private FPropertyListener getNodeEditorPropertyListener(){
    if( nodeEditorPropertyListener == null ){
      nodeEditorPropertyListener = new FPropertyListener(){
        public void dispose() {
        }

        public void propertyModified(FProperty property) {
          delegate.setValue(property.getString());
          stopCellEditing();         
        }
      };
    }
    return nodeEditorPropertyListener;
  }

  public void editingCanceled(ChangeEvent e) {
    unPlugPropertyListener(false);
  }

  public void editingStopped(ChangeEvent e) {
    unPlugPropertyListener(false);
  }
}
