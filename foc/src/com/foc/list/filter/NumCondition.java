//IMPLEMENTED

/*
 * Created on Sep 9, 2005
 */
package com.foc.list.filter;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.foc.Globals;
import com.foc.db.DBManager;
import com.foc.desc.*;
import com.foc.desc.field.FFieldPath;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.desc.field.FNumField;
import com.foc.gui.FGComboBox;
import com.foc.gui.FGLabel;
import com.foc.gui.FPanel;
import com.foc.property.FDouble;
import com.foc.property.FInt;
import com.foc.property.FMultipleChoice;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;

/**
 * @author 01Barmaja
 */
public class NumCondition extends FilterCondition{
  private static final int FLD_FIRST_VALUE = 1;
  private static final int FLD_LAST_VALUE = 2;
  private static final int FLD_OPERATOR = 3;

  //Make the constansts public and add operator_equals
  /*private static final int OPERATOR_BETWEEN = 0;
  private static final int OPERATOR_GREATER_THAN = 1;
  private static final int OPERATOR_LESS_THAN = 2;*/
  public static final int OPERATOR_INDIFERENT= 0;
  public static final int OPERATOR_GREATER_THAN = 1;
  public static final int OPERATOR_LESS_THAN = 2;
  public static final int OPERATOR_EQUALS = 3;
  public static final int OPERATOR_BETWEEN = 4;
  
  public NumCondition(FFieldPath numFieldPath, String fieldPrefix){
    super(numFieldPath, fieldPrefix);
  }

  public NumCondition(int fieldID){
  	super(fieldID);
  }

  public double getFirstValue(FocListFilter filter){
    return filter.getPropertyDouble(getFirstFieldID() + FLD_FIRST_VALUE);
  }

  public double getLastValue(FocListFilter filter){
    return filter.getPropertyDouble(getFirstFieldID() + FLD_LAST_VALUE);
  }

  public int getOperator(FocListFilter filter){
    FMultipleChoice prop = (FMultipleChoice) filter.getFocProperty(getFirstFieldID() + FLD_OPERATOR);
    return prop.getInteger();
  }
  
  public void setOperator(FocListFilter filter , int op){
    FInt operator = (FInt)filter.getFocProperty(getFirstFieldID() + FLD_OPERATOR);
    if (operator != null){
      operator.setInteger(op);
    }
  }
  
  public void setFirstValue(FocListFilter filter ,double d){
    filter.setPropertyDouble(getFirstFieldID() + FLD_FIRST_VALUE, d);
  }
  
  public void setLastValue(FocListFilter filter ,double d){
    filter.setPropertyDouble(getFirstFieldID() + FLD_LAST_VALUE, d);
  }
  
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  // IMPLEMENTED
  // oooooooooooooooooooooooooooooooooo
  // oooooooooooooooooooooooooooooooooo
  
  public void fillProperties(FocObject focFatherObject){
    new FMultipleChoice(focFatherObject, getFirstFieldID() + FLD_OPERATOR, OPERATOR_INDIFERENT);
    new FDouble(focFatherObject, getFirstFieldID() + FLD_FIRST_VALUE, 0);
    new FDouble(focFatherObject, getFirstFieldID() + FLD_LAST_VALUE, 0);
  }
  
	@Override
	public void copyCondition(FocObject tarObject, FocObject srcObject, FilterCondition srcCondition) {
	  copyProperty(tarObject, getFirstFieldID() + FLD_OPERATOR  , srcObject, srcCondition.getFirstFieldID() + FLD_OPERATOR);
	  copyProperty(tarObject, getFirstFieldID() + FLD_FIRST_VALUE, srcObject, srcCondition.getFirstFieldID() + FLD_FIRST_VALUE);
	  copyProperty(tarObject, getFirstFieldID() + FLD_LAST_VALUE , srcObject, srcCondition.getFirstFieldID() + FLD_LAST_VALUE);
	}

  public boolean includeObject(FocListFilter filter, FocObject object){
    boolean include = true;
    
    FProperty dateProp   = (FProperty) getFieldPath().getPropertyFromObject(object);
    double  dVal       = dateProp.getDouble(); 
    double  firstValue = getFirstValue(filter);
    double  lastValue  = getLastValue(filter);
    
    int op = getOperator(filter);
    
    if(op != OPERATOR_INDIFERENT){
      if(op == OPERATOR_BETWEEN || op == OPERATOR_GREATER_THAN){
        include = dVal >= firstValue;
      }
  
      if(include && op == OPERATOR_BETWEEN || op == OPERATOR_LESS_THAN){
        include = dVal <= lastValue;
      }

      if(include && op == OPERATOR_EQUALS){
        include = dVal == firstValue;
      }
    }
    
    return include;
  }
  
  @Override
  public void forceFocObjectToConditionValueIfNeeded(FocListFilter filter, FocObject focObject) {
    if(getOperator(filter) == OPERATOR_EQUALS){
      FProperty fDate = getFieldPath().getPropertyFromObject(focObject);
      if(fDate != null){
        fDate.setDouble(getFirstValue(filter));
      }
    }
  }
  
  public StringBuffer buildSQLWhere(FocListFilter filter, String fieldName) {
    //b01.foc.Globals.logString("Condition sql build not implemented yet");
    StringBuffer buffer = null;
    double firstValue = getFirstValue(filter);
    double lastValue  = getLastValue(filter);
    int op = getOperator(filter);
    buffer = new StringBuffer();
        
    if(getProvider() == DBManager.PROVIDER_ORACLE){
  		fieldName = "\"" + fieldName + "\"";
  	}
    
    if (op != OPERATOR_INDIFERENT){
      if (op == OPERATOR_GREATER_THAN){
        buffer.append(fieldName + ">=" + firstValue);
      }else if (op == OPERATOR_LESS_THAN) {
        buffer.append(fieldName + "<=" + lastValue);
      }else if (op == OPERATOR_BETWEEN){
        buffer.append(fieldName + ">=" + firstValue +" AND "+ fieldName + "<=" + lastValue);
      }else if (op == OPERATOR_EQUALS){
//        buffer.append(fieldName + " = \"" + firstValue + "\"");
      	buffer.append(fieldName + " = " + firstValue);
      }
    }
    return buffer;
  }
  
  public void setToOperationWithLock(FocListFilter filter, int operation, boolean lock){
  	FProperty prop = filter.getFocProperty(getFirstFieldID() + FLD_OPERATOR);
    prop.setInteger(operation);
    prop.setValueLocked(lock);
  }
  
  public void setToValue(FocListFilter filter, int operation, double firstDate, double lastDate){
  	setToValue(filter, operation, firstDate, lastDate, false);
  }
  
  public void forceToValue(FocListFilter filter, int operation, double firstDate, double lastDate){
  	setToValue(filter, operation, firstDate, lastDate, true);
  }
  
  private void setToValue(FocListFilter filter, int operation, double firstDate, double lastDate, boolean lockConditionAlso){
  	FProperty operatorprop = filter.getFocProperty(getFirstFieldID() + FLD_OPERATOR);
  	operatorprop.setInteger(operation);
  	if(lockConditionAlso){
  		operatorprop.setValueLocked(true);
  	}
  	
  	FDouble dateCond = null;
  	
  	dateCond = (FDouble) filter.getFocProperty(getFirstFieldID() + FLD_FIRST_VALUE);
  	dateCond.setDouble(firstDate);
  	if(lockConditionAlso){
  		dateCond.setValueLocked(true);
  	}

  	dateCond = (FDouble) filter.getFocProperty(getFirstFieldID() + FLD_LAST_VALUE);
  	dateCond.setDouble(lastDate);
  	if(lockConditionAlso){
  		dateCond.setValueLocked(true);
  	}
  }
  
  public int fillDesc(FocDesc focDesc, int firstID){
    setFirstFieldID(firstID);
    
    if(focDesc != null){
    	FPropertyListener colorListener = new ColorPropertyListener(this, firstID + FLD_OPERATOR);
      FMultipleChoiceField multipleChoice = new FMultipleChoiceField(getFieldPrefix()+"_OP", "Operation", firstID + FLD_OPERATOR, false, 1);
      multipleChoice.addChoice(OPERATOR_INDIFERENT, "None ");
      multipleChoice.addChoice(OPERATOR_BETWEEN, "Between");
      multipleChoice.addChoice(OPERATOR_GREATER_THAN, " >= ");
      multipleChoice.addChoice(OPERATOR_LESS_THAN, " <= ");
      multipleChoice.addChoice(OPERATOR_EQUALS, " = ");
      multipleChoice.setSortItems(false);
      focDesc.addField(multipleChoice);
      multipleChoice.addListener(colorListener);
      
      FNumField dateField = new FNumField(getFieldPrefix()+"_FVAL", "First value", firstID + FLD_FIRST_VALUE, false, 10, 2);
      focDesc.addField(dateField);
      dateField.addListener(colorListener);
      
      dateField = new FNumField(getFieldPrefix()+"_LVAL", "Last value", firstID + FLD_LAST_VALUE, false, 10, 2);
      focDesc.addField(dateField);
      dateField.addListener(colorListener);
    }
    
    return firstID + FLD_OPERATOR + 1;
  }

  FGComboBox combo         = null;
  Component  firstDateComp = null;
  Component  lastDateComp  = null;
  FGLabel    flecheComp    = null;
  
  /* (non-Javadoc)
   * @see b01.foc.list.filter.FilterCondition#putInPanel(b01.foc.gui.FPanel, int, int)
   */
  public GuiSpace putInPanel(FocListFilter filter, FPanel panel, int x, int y) {
    GuiSpace space = new GuiSpace();
    
    FProperty operatorProp = filter.getFocProperty(getFirstFieldID() + FLD_OPERATOR);
    FProperty firstDateProp = filter.getFocProperty(getFirstFieldID() + FLD_FIRST_VALUE);
    FProperty lastDateProp = filter.getFocProperty(getFirstFieldID() + FLD_LAST_VALUE);
   
    FPanel datesPanel = new FPanel();
    
    combo = (FGComboBox) operatorProp.getGuiComponent();
    panel.add(getFieldLabel(), combo, x, y);
    
    firstDateComp = firstDateProp.getGuiComponent();
    datesPanel.add(firstDateComp, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);

    flecheComp = new FGLabel("->");
    datesPanel.add(flecheComp, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
    
    lastDateComp = lastDateProp.getGuiComponent();
    datesPanel.add(lastDateComp, 2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
    
    panel.add(datesPanel, x+2, y, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
    
    space.setLocation(2, 2);
    
    ItemListener itemListener = new ItemListener(){
      public void itemStateChanged(ItemEvent e) {
        if(e == null || e.getStateChange() == ItemEvent.SELECTED){
          firstDateComp.setVisible(true);
          lastDateComp.setVisible(true);
          flecheComp.setVisible(true);
          firstDateComp.setVisible(false);
          lastDateComp.setVisible(false);
          flecheComp.setVisible(false);
          
          switch(combo.getSelectedIndex()){
          case OPERATOR_BETWEEN:
            firstDateComp.setVisible(true);
            lastDateComp.setVisible(true);
            flecheComp.setVisible(true);
            break;
          case OPERATOR_GREATER_THAN:           
            firstDateComp.setVisible(true);
            
            break;
          case OPERATOR_LESS_THAN:            
            lastDateComp.setVisible(true);            
            break;
          case OPERATOR_EQUALS:
            firstDateComp.setVisible(true);
            break;
          }
        }
      }
    };
    
    combo.addItemListener(itemListener);
    itemListener.itemStateChanged(null);
    //lastDateComp.setVisible(true);
    return space;
  }
  
  public boolean isValueLocked(FocListFilter filter){
  	boolean locked = false;
  	FProperty operatorProp = filter.getFocProperty(getFirstFieldID() + FLD_OPERATOR);
  	if(operatorProp != null){
  		locked = operatorProp.isValueLocked();
  	}
  	return locked;
  } 
  
  public void resetToDefaultValue(FocListFilter filter){
  	setToValue(filter, OPERATOR_INDIFERENT, 0, 0);
  }

}
