package com.foc.gui.table;

import java.awt.Rectangle;

import com.foc.desc.FocObject;
import com.foc.property.FProperty;

public abstract class FocCellPainter implements IFocCellPainter {
	protected FTable    table         = null;
	protected boolean   isSelected    = false;
	protected boolean   hasFocus      = false;
	protected int       row           = 0;
	protected int       column        = 0;
	protected FocObject focObject     = null;
	protected FProperty prop          = null;
	protected Rectangle cellRectangle = null;

	public void init(FTable table, boolean isSelected, boolean hasFocus, int row, int column, FocObject focObject, FProperty prop, Rectangle cellRectangle){
		this.table         = table;
		this.isSelected    = isSelected;
		this.hasFocus      = hasFocus;
		this.row           = row;
		this.column        = column;
		this.focObject     = focObject;
		this.prop          = prop;
		this.cellRectangle = cellRectangle;
	}
	
	public void clear(){
		table         = null;
		focObject     = null;
		prop          = null;
		cellRectangle = null;
	}
}
