package com.foc.business.workflow.map;

import com.foc.Globals;
import com.foc.desc.FocDesc;
import com.foc.gui.FListPanel;
import com.foc.gui.FPanel;
import com.foc.gui.FValidationPanel;
import com.foc.gui.table.FTableView;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class WFMapGuiBrowsePanel extends FListPanel {
	
	public static final int VIEW_SELECTION = 1; 
	
	public WFMapGuiBrowsePanel(FocList focList, int viewID){
		super("Workflow Map List", FPanel.FILL_VERTICAL);
		FocDesc focDesc = WFMapDesc.getInstance();
		if(focDesc != null){
			if(focList == null){
				focList = WFMapDesc.getList(FocList.FORCE_RELOAD);
			}
			if(focList != null){
				try {
					setFocList(focList);
				} catch (Exception e) {
					Globals.logException(e);
				}
				FTableView tableView = getTableView();

				if(viewID == VIEW_SELECTION){
					tableView.addSelectionColumn();
				}
				tableView.addColumn(focDesc, WFMapDesc.FLD_NAME, false);
				tableView.addColumn(focDesc, WFMapDesc.FLD_DESCRIPTION, false);
				/*
				tableView.addColumn(focDesc, WFMapDesc.FLD_CREATION_TITLE, false);
				tableView.addColumn(focDesc, WFMapDesc.FLD_MODIFICATION_TITLE, false);
				tableView.addColumn(focDesc, WFMapDesc.FLD_CANCELATION_TITLE, false);
				*/
				
				construct();
				tableView.setColumnResizingMode(FTableView.COLUMN_AUTO_RESIZE_MODE);
				
				FValidationPanel validPanel = showValidationPanel(true);
				if(validPanel != null){
					if(viewID == VIEW_SELECTION){
						validPanel.setValidationButtonLabel("Ok");
					}else{
						validPanel.addSubject(focList);
					}
				}
				
				showAddButton(viewID != VIEW_SELECTION);
				showRemoveButton(viewID != VIEW_SELECTION);
				showEditButton(viewID != VIEW_SELECTION);
			}
		}
	}
}
