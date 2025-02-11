package com.fab.gui.browse;
import com.fab.FocApplicationBuilder;
import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class GuiBrowseColumnGuiBrowsePanel extends FListPanel{
	
	public GuiBrowseColumnGuiBrowsePanel(FocList list, int ViewId){
		super("Browse Column", FPanel.FILL_BOTH);
		FocDesc desc = GuiBrowseColumnDesc.getInstance();
		boolean allowEdit = ViewId != FocApplicationBuilder.VIEW_NO_EDIT;

    if (desc != null) {
      if(list != null){
      	list.loadIfNotLoadedFromDB();
      }
      if (list != null) {
      	try{
      		setFocList(list);
      	}catch(Exception e){
      		Globals.logException(e);
      	}
        FTableView tableView = getTableView();       
        tableView.setDetailPanelViewID(0);
        
        tableView.addLineNumberColumn();
        //tableView.addColumn(desc, BrowseColumnDesc.FLD_BROWSE_VIEW, 20, false);
        tableView.addColumn(desc, GuiBrowseColumnDesc.FLD_FIELD_DEFINITION, 20, allowEdit);
        tableView.addColumn(desc, GuiBrowseColumnDesc.FLD_EDITABLE, allowEdit);
        construct();
        
        tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
        
        requestFocusOnCurrentItem();
        showEditButton(false);
        showDuplicateButton(false);
        showAddButton(allowEdit);
        showRemoveButton(allowEdit);
      }
    }
	}
}
