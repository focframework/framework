package com.foc.admin;

import com.foc.Globals;
import com.foc.desc.FocObject;
import com.foc.desc.field.FFieldPath;
import com.foc.list.DisplayList;
import com.foc.list.FocList;
import com.foc.list.FocListOrder;
import com.foc.menu.FMenu;
import com.foc.menu.FMenuList;

public class MenuAccessRightDisplayList extends DisplayList{

  private FocList  realList    = null;
  private FocList  displayList = null;
  private FocGroup group       = null;
  
  public MenuAccessRightDisplayList(FocList realList, FocGroup group) {
    super(realList);
    this.group = group;
    setDoNotRemoveRealItems(false);
    construct();
  }
  
  @Override
  public void dispose(){
  	super.dispose();
    realList    = null;
    displayList = null;
    group       = null;
  }

  @Override
  public void completeTheDisplayList(FocList realList, FocList displayList) {
    this.realList    = realList;
    this.displayList = displayList;
    displayList.setDbResident(false);
    //displayList.setLoaded(true);
    FocListOrder focListOrder = new FocListOrder();
    focListOrder.addField(FFieldPath.newFieldPath(MenuRightsDesc.FLD_MENU_CODE));
    FMenuList mainAppMenuList = (FMenuList) Globals.getApp().getMainAppMenu();
    scanApplicationMenuListAndCompleteDisplayList(mainAppMenuList, null);
    //All Unvisited menus should be removed because maybe the application changes them
    for(int i=realList.size()-1; i>=0; i--){
    	MenuRights menuRights = (MenuRights) realList.getFocObject(i);
    	if(menuRights != null && !menuRights.isVisited()){
    		realList.remove(menuRights);
    		menuRights.setDeleted(true);
    		
    		MenuRights displeyMenuRights = (MenuRights) displayList.searchByPropertyStringValue(MenuRightsDesc.FLD_MENU_CODE, menuRights.getCode());
    		displayList.remove(displeyMenuRights);
    	}
    }
  }
  
  private void scanApplicationMenuListAndCompleteDisplayList(FMenuList menuList, MenuRights fatherMenu){
    for(int i=0; i<menuList.menuCount(); i++){
      FMenu  menu     = menuList.getMenu(i);
      String menuCode = menu.getCode();
      
      if(menuCode != null){
        MenuRights existedMenuRights = (MenuRights) realList.searchByPropertyStringValue(MenuRightsDesc.FLD_MENU_CODE, menuCode);
        if(existedMenuRights == null){
          existedMenuRights = (MenuRights) displayList.newEmptyItem();
          
          existedMenuRights.setCode(menuCode);
          existedMenuRights.setTitle(menu.getTitle());
          existedMenuRights.setFatherMenu(fatherMenu);
          existedMenuRights.setGroup(group);
          if(fatherMenu != null && fatherMenu.getRight() == MenuRightsDesc.ALLOW_HIDE){
            existedMenuRights.setRight(MenuRightsDesc.ALLOW_HIDE);
            existedMenuRights.getFocProperty(MenuRightsDesc.FLD_RIGHT).setValueLocked(true);
          }
          displayList.add(existedMenuRights);
        }else{
          MenuRights menuRightsInDisplay = (MenuRights) displayList.searchByPropertyStringValue(MenuRightsDesc.FLD_MENU_CODE, existedMenuRights.getCode()); 
          if(menuRightsInDisplay != null){
          	menuRightsInDisplay.setTitle(menu.getTitle());          	
            menuRightsInDisplay.setFatherMenu(fatherMenu);
          }
        }
        existedMenuRights.setVisited(true);
        if(menu.isList()){
          scanApplicationMenuListAndCompleteDisplayList((FMenuList)menu, existedMenuRights);
        }
      }
    }
    
  }
  
  @Override
  public void copyFromObjectToObject(FocObject target, FocObject source) {
    MenuRights tar = (MenuRights) target;
    MenuRights src = (MenuRights) source;
    
    tar.copy(src);
  }

  @Override
  public FocObject findObjectInList(FocList focList, FocObject object) {
  	MenuRights menuRights = (MenuRights) object;
    String menuCode = menuRights.getCode();
    return menuCode != null ? focList.searchByPropertyStringValue(MenuRightsDesc.FLD_MENU_CODE, menuCode) : null;
  }

  @Override
  public boolean isDisplayItemToBeSaved(FocObject object) {
    MenuRights menuRights = (MenuRights)object;
    boolean yes = false;
    if(menuRights.getRight() == MenuRightsDesc.ALLOW_HIDE && !menuRights.getFocProperty(MenuRightsDesc.FLD_RIGHT).isValueLocked()){
      yes = true;
    }else if(!menuRights.getCustomTitle().isEmpty()){
    	yes = true;
    }
    
    if(yes) menuRights.setFatherMenu(null);
    return yes;
  }
}
