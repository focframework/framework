package com.foc.admin;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.desc.field.FField;
import com.foc.desc.field.FFieldPath;
import com.foc.gui.FGButton;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableColumn;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class FocUserGuiBrowsePanel extends FListPanel{

  public static final int COL_NAME      = 1;  
  public static final int COL_PASSWORD  = 2;  
  public static final int COL_GROUP     = 3;
  public static final int COL_LANGUAGE  = 4;
  public static final int COL_FULL_NAME = 5;
  public static final int COL_SUSPENDED = 6;
  public static final int COL_CONTACT   = 7;

  public FocUserGuiBrowsePanel(FocList list, int viewID){
  	super("User", FILL_BOTH);
    FocDesc desc = FocUser.getFocDesc();
    if (desc != null) {
      if(list == null){
        list = FocUserDesc.getList();
      }
      desc.setFieldSelectionListNotLoaded();
      if (list != null) {
        try {
          setFocList(list);
        } catch (Exception e) {
          Globals.logException(e);
        }
        fillTableView(desc);
        construct();
        getTableView().setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        setDirectlyEditable(true);
  
        //ChangePassword Button
        //---------------------
        FPanel totalsPanel = getTotalsPanel();
        
        FGButton changePass = new FGButton("Set password");
        changePass.addActionListener(new SetPassListener(this));
        totalsPanel.add(changePass, 0, 0);

        FValidationPanel savePanel = showValidationPanel(true);
        if (savePanel != null) {
          list.setFatherSubject(null);
          savePanel.addSubject(list);
        }
  
        requestFocusOnCurrentItem();
        showEditButton(false);
      }
    }
  }
  
  public void fillTableView(FocDesc desc){
    FField currField = null;
    FTableColumn col = null;
    FTableView tableView = getTableView();

    tableView.addColumn(desc, FocUserDesc.FLD_SUSPENDED, COL_SUSPENDED, true);

    currField = desc.getFieldByID(FocUserDesc.FLD_NAME);
    col = new FTableColumn(desc, FFieldPath.newFieldPath(currField.getID()), COL_NAME, "Name", currField.getSize(), true);
    tableView.addColumn(col);

    currField = desc.getFieldByID(FocUserDesc.FLD_FULL_NAME);
    col = new FTableColumn(desc, FFieldPath.newFieldPath(currField.getID()), COL_FULL_NAME, "Full name", currField.getSize(), true);
    tableView.addColumn(col);

    currField = desc.getFieldByID(FocUserDesc.FLD_GROUP);
    col = new FTableColumn(desc, FFieldPath.newFieldPath(currField.getID()), COL_GROUP, "Group", 20, true);
    tableView.addColumn(col);

    currField = desc.getFieldByID(FocUserDesc.FLD_LANGUAGE);
    if(currField != null){
      col = new FTableColumn(desc, FFieldPath.newFieldPath(currField.getID()), COL_LANGUAGE, "Language", 20, true);
      tableView.addColumn(col);
    }
    
    currField = desc.getFieldByID(FocUserDesc.FLD_CONTACT);
    col = new FTableColumn(desc, FFieldPath.newFieldPath(currField.getID()), COL_CONTACT, "Contact", 20, false);
    tableView.addColumn(col);
  }
}
