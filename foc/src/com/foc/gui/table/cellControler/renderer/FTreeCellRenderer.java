package com.foc.gui.table.cellControler.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.foc.Globals;
import com.foc.gui.DisplayManager;
import com.foc.gui.tree.FGTree;
import com.foc.gui.treeTable.FGTreeInTable;
import com.foc.gui.treeTable.FTreeTableModel;
import com.foc.tree.FTree;

public class FTreeCellRenderer implements TableCellRenderer {
	
	/** Last table/tree row asked to renderer. */
	protected int visibleRow;
  private FTreeTableModel treeTableModel = null;
	
	public FTreeCellRenderer(FTreeTableModel treeTableModel){
    setTreeTableModel(treeTableModel);
  }
  
  public void dispose(){
    treeTableModel = null;
  }
    
  private FGTree getFGTree(){
    return (treeTableModel != null) ? treeTableModel.getGuiTree(): null; 
  }
	
  public void setTreeTableModel(FTreeTableModel treeTableModel){
    this.treeTableModel = treeTableModel;
  }
  
	/**
	 * updateUI is overridden to set the colors of the Tree's renderer to match
	 * that of the table.
	 */
	public void updateUI() {
    FGTree gTree = getFGTree();
    if(gTree != null){
      gTree.updateUI();
  		// Make the tree's cell renderer use the table's cell selection
  		// colors.
  		TreeCellRenderer tcr = gTree.getCellRenderer();
  		if (tcr instanceof DefaultTreeCellRenderer) {
  			DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
  			// For 1.1 uncomment this, 1.2 has a bug that will cause an
  			// exception to be thrown if the border selection color is
  			// null.
  			// dtcr.setBorderSelectionColor(null);
  			dtcr.setTextSelectionColor(UIManager
  					.getColor("Table.selectionForeground"));
  			dtcr.setBackgroundSelectionColor(UIManager
  					.getColor("Table.selectionBackground"));
  			if(Globals.getDisplayManager().getLookAndFeel() == DisplayManager.LOOK_AND_FEEL_NIMBUS){
  				dtcr.setBackgroundSelectionColor(Globals.getDisplayManager().getBarmajaOrangeDark());
  				dtcr.setFont(Globals.getDisplayManager().getDefaultFont());
  			}
      }
    }
	}

	/**
	 * Sets the row height of the tree, and forwards the row height to the
	 * table.
	 */
	public void setRowHeight(int rowHeight) {
    FGTree gTree = getFGTree(); 
    if(gTree != null){
  		if (rowHeight > 0) {
        gTree.setRowHeight(rowHeight);
        //BAntoineS - GUI_TREE_SHIFT
  			//if (gTree.getRowHeight() != rowHeight) {
  			//	setRowHeight(gTree.getRowHeight());
  			//}
        //EAntoineS - GUI_TREE_SHIFT
  		}
    }
	}

	/**
	 * This is overridden to set the height to match that of the JTable.
	 */
	public void setBounds(int x, int y, int w, int h) {
    FGTree gTree = getFGTree(); 
    if(gTree != null){
      gTree.setBounds(x, 0, w, /*FGTreeInTable.this.getHeight()*/gTree.getRowHeight());
    }
		//super.setBounds(x, 0, w, 18);
	}

	/**
	 * Sublcassed to translate the graphics such that the last visible row will
	 * be drawn at 0,0.
	 */
	/*public void paint(Graphics g) {
    FGTree gTree = getFGTree();
    if(gTree != null){
      b01.foc.Globals.logString(" GTREE GET ROW HEIGHT = "+gTree.getRowHeight());
      g.translate(0, -visibleRow * gTree.getRowHeight());
      //gTree.paint(g, visibleRow);
    }else{
      b01.foc.Globals.logString(" GTREE GET ROW HEIGHT = NULL");
    }
	}*/
  
	/**
	 * TreeCellRenderer method. Overridden to update the visible row.
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    
    FGTreeInTable gTree = (FGTreeInTable) getFGTree(); 
    if(gTree != null){
  		//FDefaultCellRenderer.setCellColorStatic(this, table, value, isSelected, hasFocus, row, column);
      if (isSelected){
        gTree.setBackground(table.getSelectionBackground());
      }else{
        gTree.setBackground(table.getBackground());
      }
      gTree.setVisibleRow(row);
    }
    
    if(treeTableModel != null){
      FTree tree = treeTableModel.getTree();
      Color color = tree.getColorForNode(treeTableModel.getNodeForRow(0), 0, gTree);
      if(color != null){
        gTree.setBackground(color);
      }  
      
      /*
      Color foreground = tree.getForegroundForNode(treeTableModel.getNodeForRow(0));
      if(foreground != null){
      	gTree.setForeground(foreground);
      }
      */
    }
    
    return gTree;
	}
  
}
