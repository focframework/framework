package com.foc.tree.criteriaTree;

import com.foc.desc.field.FFieldPath;
import com.foc.tree.FNode;

public class FNodeLevel {
  private int        levelDepth         = 0;
  private FFieldPath path               = null;
  private boolean    sortable           = true;
  private int        detailsPanelViewID = FNode.NO_SPECIFIC_VIEW_ID;
  private String     noneStringDisplay  = " < None > ";
  
  public FNodeLevel(FFieldPath path){
  	this(path,true);
  }
  
  public FNodeLevel(FFieldPath path, boolean sortable){
  	this.path = path ;
  	this.sortable = sortable;
  }
  
  public void dispose(){
    if(path != null){
      path.dispose();
      path = null;      
    }
  }

  public int getLevelDepth() {
    return levelDepth;
  }

  public void setLevelDepth(int levelDepth) {
    this.levelDepth = levelDepth;
  }

  public FFieldPath getPath() {
    return path;
  }

  public void setPath(FFieldPath path) {
    this.path = path;
  }
  
  public boolean isSortable(){
  	return this.sortable;
  }
  
  public void setSortable(boolean sortable){
  	this.sortable = sortable;
  }
  
  public void setDetailsPanelViewID(int viewID){
  	this.detailsPanelViewID = viewID;
  }
  
  public int getDetailsPanelViewID(){
  	return this.detailsPanelViewID;
  }

	public String getNoneStringDisplay() {
		return noneStringDisplay;
	}

	public void setNoneStringDisplay(String noneStringDisplay) {
		this.noneStringDisplay = noneStringDisplay;
	}
}
