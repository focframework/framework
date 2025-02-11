package com.foc.vaadin;

import java.util.ArrayList;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.business.workflow.implementation.ILoggable;
import com.foc.business.workflow.implementation.Loggable;
import com.foc.dataWrapper.FocListWrapper;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.log.FocLogEvent;
import com.foc.shared.dataStore.IFocData;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FocCentralPanel extends FVVerticalLayout implements INavigationWindow {

	public static final int PREVIOUS_REMOVE     = 0;
	public static final int PREVIOUS_KEEP       = 1;
	public static final int PREVIOUS_REMOVE_ALL = 2;

	private Panel                    centralPanel             = null;
//	private FVVerticalLayout         centralPanel             = null;
	private ArrayList<ICentralPanel> arrayOfCentralComponents = null;
  private FVMenuTree               menuTree                 = null;

  private Window wrapperWindow = null; 
	private Window utilityWindow = null;
	
	private String preferredWidth = null;
	public static final int PAGE_WIDTH               = 700;
//	public static final int MARGIN_FOR_CENTRAL_PANEL = 18;
//  public static final int MARGIN_FOR_CENTRAL_PANEL = 0;
  private int marginOfCentralPanel = 18;

	public FocCentralPanel(){
		super(null);
	}
	
	public void dispose(){
		try {
			goBackTillFirstPage();
			ICentralPanel centralPanel = getCentralPanel();
			super.dispose();
			dispose_MenuTree();
			arrayOfCentralComponents = null;
			if(centralPanel != null) {
				centralPanel.dispose();
			}
		}catch(Exception e) {
			Globals.logExceptionWithoutPopup(e);
		}
	}
	
	public void dispose_MenuTree(){
		if(menuTree != null){
			menuTree.dispose();
			menuTree = null;
		}
	}
	
	public boolean isRootWindow(){
		return getParent() == null;
	}
	
	public Window getWindow_Parent(){
		Window w = (Window) this.getParent();
		return w;
	}
	
	public ArrayList<ICentralPanel> getCentralPanelsArrayList(){
		if(arrayOfCentralComponents == null){
			arrayOfCentralComponents = new ArrayList<ICentralPanel>();	
		}
		return arrayOfCentralComponents;
	}
	
	public Component getCentralPanelWrapper(){
		if(centralPanel == null){
			centralPanel = new Panel();
//			centralPanel = new FVVerticalLayout();
			centralPanel.setSizeFull();
//			centralPanel.setHeight("-1px");
//			centralPanel.setWidth("-1px");
			centralPanel.setStyleName("focCentralPanel");
			if(Globals.isValo()) centralPanel.removeStyleName("focNoCaptionMargin");
		}
		return centralPanel; 
	}

	/*
	public FVVerticalLayout getCentralPanelWrapper_VerticalLayout(){
		if(centralPanel == null){
//			centralPanel = new Panel();
			centralPanel = new FVVerticalLayout();
			centralPanel.setSizeFull();
//			centralPanel.setHeight("-1px");
//			centralPanel.setWidth("-1px");
			centralPanel.setStyleName("focCentralPanel");
			if(Globals.isValo()) centralPanel.removeStyleName("focNoCaptionMargin");
		}
		return centralPanel; 
	}
	*/
	
	public void centralPanel_setPadding(boolean padding){
		if(centralPanel != null){
			if(padding){
				centralPanel.removeStyleName("focNoPadding");
				centralPanel.removeStyleName("focNoCaptionMargin");
				setMarginOfCentralPanel(18);
			}else{
				centralPanel.addStyleName("focNoPadding");
				centralPanel.addStyleName("focNoCaptionMargin");
				setMarginOfCentralPanel(0);
			}
		}
	}

	public Window getWrapperWindow(){
		return wrapperWindow;
	}
	
	public Window newWrapperWindow(){
		wrapperWindow = new Window();
		wrapperWindow.setClosable(false);
		wrapperWindow.setResizable(true);
		/*
		window.addCloseListener(new CloseListener() {
      @Override
      public void windowClose(CloseEvent e) {
        if(getCentralPanel() instanceof FocXMLLayout){
          if(((FocXMLLayout)getCentralPanel()).getValidationLayout() != null){
          	((FocXMLLayout)getCentralPanel()).getValidationLayout().cancel();
          }
          else{
          	goBack();
          }
        }
      }
    });
    */
	  
		wrapperWindow.setWidth(getPreferredWidth());
		wrapperWindow.addStyleName("focCentralPanel");
		wrapperWindow.center();
		
		//ATTENTION-MAN
//		Panel panel = new Panel("Scroll");
//		panel.setWidth(width);
//		panel.setHeight(height);
//		panel.setContent(this);
//		if(ConfigInfo.isGuiRTL()) {
//			centralPanel.addStyleName("foc-floatNone");
//		}
//		wrapperWindow.setContent(panel);
		
		wrapperWindow.setContent(this);
		//ATTENTION-MAN

		return wrapperWindow;
	}

	public void fill(){
    addComponent(getCentralPanelWrapper());
	}

	public ICentralPanel changeCentralPanelContent_ToTableForFocListWrapper(FocListWrapper focListWrapper){
		ICentralPanel centralPanel = null;
		if(focListWrapper != null && focListWrapper.getFocList() != null && focListWrapper.getFocList().getFocDesc() != null){
		  XMLViewKey xmlViewKey = new XMLViewKey(focListWrapper.getFocList().getFocDesc().getStorageName(), XMLViewKey.TYPE_TABLE);
		  centralPanel = XMLViewDictionary.getInstance().newCentralPanel(this, xmlViewKey, focListWrapper);
		  changeCentralPanelContent(centralPanel, true);
		}
	  return centralPanel;
	}                           

	public ICentralPanel changeCentralPanelContent_ToTableForFocList(FocList focList){
	  XMLViewKey xmlViewKey = new XMLViewKey(focList.getFocDesc().getStorageName(), XMLViewKey.TYPE_TABLE);
	  ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(this, xmlViewKey, focList);
	  changeCentralPanelContent(centralPanel, true);
	  return centralPanel;
	}                           
	
  @Override
  public ICentralPanel changeCentralPanelContent_ToFormForFocObject(FocObject focObject) {
    XMLViewKey xmlViewKey = new XMLViewKey(focObject.getThisFocDesc().getStorageName(), XMLViewKey.TYPE_FORM, focObject.getThisFocDesc().focDesc_getGuiContext(), XMLViewKey.VIEW_DEFAULT);
    ICentralPanel centralPanel = XMLViewDictionary.getInstance().newCentralPanel(this, xmlViewKey, focObject);
    changeCentralPanelContent(centralPanel, true);
    return centralPanel;
  }

	public ICentralPanel getCentralPanel(){
		ICentralPanel currentCentralPanelContent = null;
		if(arrayOfCentralComponents != null && arrayOfCentralComponents.size() > 0) currentCentralPanelContent = arrayOfCentralComponents.get(arrayOfCentralComponents.size()-1);
		return currentCentralPanelContent;
	}
	
	protected void afterChangeCentralPanelContent(ICentralPanel iCentralPanel){
		Component central = (Component) iCentralPanel;
		if(central != null && central instanceof FocXMLLayout){
			FocXMLLayout xmlLayout = (FocXMLLayout)central;
			if(xmlLayout != null && xmlLayout.getLayouts() != null && xmlLayout.getLayouts().size() > 0){
				Component rootComp = (Component) xmlLayout.getLayouts().get(0);
				if(rootComp != null){
					if(centralPanel != null){
						centralPanel.setWidth(rootComp.getWidth(), rootComp.getWidthUnits());
//						centralPanel.setHeight("-1px");
//						centralPanel.setHeight("100%");//NO_PANEL
						//When the main layout is 100% this means that there will be no vertical srcoll
						//When the main layout is -1px this means that there will be a  vertical srcoll
						//    And then we can use expandRatio to the table or any other component inside
						if(this instanceof FocWebVaadinWindow) {
							centralPanel.setHeight(rootComp.getHeight(), rootComp.getHeightUnits());
							setHeight("100%");//NO_PANEL
						} else {
							centralPanel.setHeight(rootComp.getHeight(), rootComp.getHeightUnits());
							setHeight(rootComp.getHeight(), rootComp.getHeightUnits());
							//This means we are in a Popup 
//							centralPanel.setHeight("100%");
//							setHeight("100%");//NO_PANEL
						}
					}
//					setWidth(rootComp.getWidth(), rootComp.getWidthUnits());
					//setHeight(rootComp.getHeight(), rootComp.getHeightUnits());
					
//					setHeight("-1px");
//					setHeight("100%");//NO_PANEL					
					markAsDirty();
//					requestRepaint();
				}
			}
		}
	}
	
	@Override
	public void changeCentralPanelContent(ICentralPanel newCentralPanel, boolean keepPrevious) {
		changeCentralPanelContent(newCentralPanel, keepPrevious ? PREVIOUS_KEEP : PREVIOUS_REMOVE);
	}

	@Override
	public void changeCentralPanelContent(ICentralPanel newCentralPanel, int previousMode){
		changeCentralPanelContent(null, newCentralPanel, previousMode);
	}
	
	public void changeCentralPanelContent(ICentralPanel iCentralPanelToRemove, ICentralPanel newCentralPanel, boolean keepPrevious){
		changeCentralPanelContent(iCentralPanelToRemove, newCentralPanel, keepPrevious ? PREVIOUS_KEEP : PREVIOUS_REMOVE);
	}
	
	public void changeCentralPanelContent(ICentralPanel iCentralPanelToRemove, ICentralPanel newCentralPanel, int previousMode){
		if(getCentralPanelWrapper() != null){
			ICentralPanel currentCentralPanelContent = getCentralPanel();
			boolean goingBack = newCentralPanel == null;
			boolean goingBack_RemovingAMiddlePanel = goingBack && iCentralPanelToRemove != null && currentCentralPanelContent != iCentralPanelToRemove;
			if(goingBack_RemovingAMiddlePanel){
				iCentralPanelToRemove.setGoBackRequested(true);
			}else if(goingBack){
				removeGuiCentralComponent(currentCentralPanelContent);
				
				removeFromCacheAndDispose(currentCentralPanelContent);
				
				int i=getCentralPanelsArrayList().size()-1; 
				while(i>=0 && newCentralPanel == null){
					ICentralPanel tempICentralPanel = getCentralPanelsArrayList().get(i);
					if(tempICentralPanel != null && !tempICentralPanel.isGoBackRequested()){
						newCentralPanel = tempICentralPanel;
					}else{
						removeFromCacheAndDispose(tempICentralPanel);		
						i--;						
					}
				}

				if(newCentralPanel != null){
					addGuiCentralComponent(newCentralPanel, false);
					newCentralPanel.refresh();
					if(newCentralPanel.getRightPanel(false) != null){
						newCentralPanel.getRightPanel(false).refresh();
					}
					replaceValidationLayout(newCentralPanel);						
				}
				afterGoBack();
			}else{
				if(newCentralPanel != null && newCentralPanel.getXMLView() != null){
					logMemory("Entry to", newCentralPanel.getXMLView().getXmlViewKey());
				}

				removeGuiCentralComponent(currentCentralPanelContent);
				addGuiCentralComponent(newCentralPanel, true);
				replaceValidationLayout(newCentralPanel);
				
				if(previousMode == PREVIOUS_REMOVE_ALL){
					removeFromCacheAndDispose_AllLayouts(false);
				}else if(previousMode == PREVIOUS_REMOVE){
					int index = getCentralPanelsArrayList().size()-1;
					if(index >= 0){
						ICentralPanel iCentralPanel = getCentralPanelsArrayList().get(index);
						if(iCentralPanel != null){
							removeFromCacheAndDispose(iCentralPanel);
						}
					}
				}
				getCentralPanelsArrayList().add(newCentralPanel);
			}
		}	
	}
	
	public static void logFormOpened(IFocData focData) {
		if(focData != null) {
			FocObject focObjToBeViewed = FocXMLLayout.getFocObject(focData);
			if(focObjToBeViewed != null && focObjToBeViewed.getThisFocDesc() != null && focObjToBeViewed.getThisFocDesc().workflow_IsLoggable() && !focObjToBeViewed.isCreated()) {
				Loggable loggable = ((ILoggable)focObjToBeViewed).iWorkflow_getWorkflow();
				if(loggable != null) {
					loggable.insertLogLine(FocLogEvent.EVENT_OPENED);
				}
			}
		}
	}
	
	public void removeFromCacheAndDispose_AllLayouts(boolean keepLast){
		for(int i=getCentralPanelsArrayList().size()-1;i>=1; i--){
			ICentralPanel iCentralPanel = getCentralPanelsArrayList().get(i);
			removeFromCacheAndDispose(iCentralPanel);
		}
	}
	
	private void replaceValidationLayout(ICentralPanel centralPanel){
		if(Globals.isValo()){
			//Here we need to check if we are in a popup window or in the MainWindow UI
			if(getWrapperWindow() == null){
				FocWebApplication webApp = findAncestor(FocWebApplication.class);
				if(webApp != null){
					webApp.replaceFooterLayout(centralPanel.getValidationLayout());
				}
			}
		}
	}
	
	public void removeFromCacheAndDispose(ICentralPanel currentCentralPanelContent){
		if(currentCentralPanelContent != null && getCentralPanelsArrayList() != null){
			getCentralPanelsArrayList().remove(currentCentralPanelContent);
			currentCentralPanelContent.dispose();
		}
	}
	
	public void removeGuiCentralComponent(ICentralPanel currentCentralPanelContent){
		if(currentCentralPanelContent != null){
			currentCentralPanelContent.removedFromNavigator();
			//Panel-Vertical
//			centralPanel.removeAllComponents();
			centralPanel.setContent(null);
			//----
		}
	}
	
	public void addGuiCentralComponent(ICentralPanel newCentralPanel, boolean showValidationLayout){
		//Panel-Vertical
//		centralPanel.addComponent((Component) newCentralPanel);
		centralPanel.setContent((Component) newCentralPanel);
		//----
		
		centralPanel.markAsDirty();
//				centralPanel.addComponent((Component) newCentralPanel);
		String preferedWidth = newCentralPanel.getPreferredPageWidth();
		
		if (preferedWidth != null && preferedWidth.endsWith("px")) {
		  setPreferredWidth(preferedWidth);
		}else{
			setPreferredWidth(null);
		}
		if(showValidationLayout) {
		  newCentralPanel.showValidationLayout(getCentralPanelsArrayList().size()>0);
		}
		
		newCentralPanel.addedToNavigator();
		afterChangeCentralPanelContent(newCentralPanel);		
	}

//	@Override
//	public void changeCentralPanelContent(ICentralPanel newCentralPanel, int previousMode){
//		if(getCentralPanelWrapper() != null){
//			boolean goingBack = newCentralPanel == null;
//			if(goingBack){
//				newCentralPanel = arrayOfCentralComponents.size() >= 2 ? arrayOfCentralComponents.get(arrayOfCentralComponents.size()-2) : null;
//				keepPrevious    = false;
//			}
//			if(arrayOfCentralComponents.size() > 0){
//				ICentralPanel currentCentralPanelContent = getCentralPanel();
//				
//				//NO_PANEL
////				centralPanel.setContent(null);
//				currentCentralPanelContent.removedFromNavigator();				
//				centralPanel.removeAllComponents();
//				
////				centralPanel.removeComponent((Component) currentCentralPanelContent);
//				if (currentCentralPanelContent.getRightPanel(false) != null) {
//				  removeUtilityPanel(currentCentralPanelContent.getRightPanel(false));
//				}
//				
//				if(!keepPrevious){
//					arrayOfCentralComponents.remove(currentCentralPanelContent);
//					currentCentralPanelContent.dispose();
//				}
//			}
//			if(newCentralPanel != null){
//				if(goingBack){
//					//NO_PANEL
////					ICentralPanel previousCentralPanel = (ICentralPanel) centralPanel.getContent();
//					ICentralPanel previousCentralPanel = centralPanel.getComponentCount() > 0 ? (ICentralPanel) centralPanel.getComponent(0) : null;
//					
//					if(previousCentralPanel != null){
//						previousCentralPanel.dispose();
//						previousCentralPanel = null;
//					}
//				}
//
//				//NO_PANEL
////				centralPanel.setContent((Component) newCentralPanel);
//				centralPanel.addComponent((Component) newCentralPanel);
//				
//				centralPanel.markAsDirty();
////				centralPanel.addComponent((Component) newCentralPanel);
//				String preferedWidth = newCentralPanel.getPreferredPageWidth();
//				
//				if (preferedWidth != null && preferedWidth.endsWith("px")) {
//				  setPreferredWidth(preferedWidth);
//				}else{
//					setPreferredWidth(null);
//				}
//				if(!goingBack) {
//				  newCentralPanel.showValidationLayout(arrayOfCentralComponents.size()>0);
//				}
//				
//				newCentralPanel.addedToNavigator();				
//			}
//			if(!goingBack){
//				arrayOfCentralComponents.add(newCentralPanel);
//			}else{
//				if(newCentralPanel != null){
//					newCentralPanel.refresh();
//					if(newCentralPanel.getRightPanel(false) != null){
//						newCentralPanel.getRightPanel(false).refresh();
//					}
//				}
//				afterGoBack();
//			}
//			afterChangeCentralPanelContent();
//		}
//	}

	public void afterGoBack(){
		if(getCentralPanel() == null){
			Window window = findAncestor(Window.class);
			if(window != null && getUI() != null){
				INavigationWindow navWindow = null;
				if(getUI().getContent() instanceof INavigationWindow){
					navWindow = (INavigationWindow) (getUI().getContent());
				}
				getUI().removeWindow(window);
				if(navWindow != null){
					navWindow.refreshCentralPanelAndRightPanel();
				}
			}
		}
	}
	
	private void logMemory(String exitEntry, XMLViewKey key){
		if(key != null){
			String userForThisSession = null;
			if(Globals.getApp() != null && Globals.getApp().getUser_ForThisSession() != null){
				userForThisSession = Globals.getApp().getUser_ForThisSession().getName();
			}
			String message = exitEntry+" View " + key.getStringKey() + " User: " + userForThisSession;
			Globals.logString(message);//Here we write who entered a exited what form
			Globals.logMemoryNewThread("");//Here we write every 10min minimum the memory log
		}
	}
	
	@Override
	public void goBack(ICentralPanel iCentralPanelToRemove){
		XMLViewKey exitingKey = null;
		if(iCentralPanelToRemove != null && iCentralPanelToRemove.getXMLView() != null){
			exitingKey = iCentralPanelToRemove.getXMLView().getXmlViewKey();
		}
		changeCentralPanelContent(iCentralPanelToRemove, null, false);
				
		afterGoBack();
		
		if(exitingKey != null){
			logMemory("Exit from ", exitingKey);
		}
	}

	public void goBackTillFirstPage(){
		while(getCentralPanelsArrayList().size() > 1){
			goBack(null);
		}
	}

	
	public void init(){
	}

	public void refresh(){
		
	}
	
	public void refreshCentralPanelAndRightPanel(){
		ICentralPanel newCentralPanel = getCentralPanel();
		if(newCentralPanel != null){
			newCentralPanel.refresh();
			if(newCentralPanel.getRightPanel(false) != null){
				newCentralPanel.getRightPanel(false).refresh();
			}
		}
	}
	
	public FocWebApplication getFocWebApplication(){
	  return (FocWebApplication) getUI();
	}

	@Override
	public void addUtilityPanel(IRightPanel utilityPanel) {
		if(utilityPanel != null){
			try{
				Component utilityPanelAsComponent = (Component) utilityPanel;
//				Window window = getParent(Window.class);
				
				utilityWindow = new Window("Dictionary");
				utilityWindow.setSizeFull();
				utilityWindow.setWidth(utilityPanelAsComponent.getWidth(), utilityPanelAsComponent.getWidthUnits());
				utilityWindow.setHeight(utilityPanelAsComponent.getHeight(), utilityPanelAsComponent.getHeightUnits());
				utilityWindow.setContent(utilityPanelAsComponent);
				
				getUI().addWindow(utilityWindow);
			}catch(Exception e){
				Globals.logException(e);
			}
		}
	}

	@Override
	public void removeUtilityPanel(IRightPanel utilityPanel) {
		if(utilityPanel != null){
			try{
				FocWebApplication.getInstanceForThread().removeWindow(utilityWindow);
			}catch(Exception e){
				Globals.logException(e);
			}
		}
	}
	
	public void setCaption(String caption){
		super.setCaption(caption);
	}

	public String getPreferredWidth(){
	  int marginFromPanel = getMarginOfCentralPanel();
		String returnedPreferredPageWidth = null;
		if(preferredWidth != null && preferredWidth.endsWith("px")){
		  String str = preferredWidth.substring(0, preferredWidth.length()-2);
		  int width = Integer.valueOf(str)+marginFromPanel;
			returnedPreferredPageWidth = width+"px";
		}else{
			FocUser user = FocWebApplication.getFocUser();
			int pageWidthInt = user != null && user.getPageWidth() > 0 ? user.getPageWidth() : PAGE_WIDTH;
			pageWidthInt += marginFromPanel;
			returnedPreferredPageWidth = pageWidthInt+"px";
		}
		return returnedPreferredPageWidth;
	}
	
	public void setPreferredWidth(String width) {
    preferredWidth = width;
  }
	
	public void removeFocAllWindows(){
		if(arrayOfCentralComponents != null){
			for(int i=arrayOfCentralComponents.size()-1; i>=0; i--){
				goBack(null);
//				ICentralPanel iCentralPanel = arrayOfCentralComponents.get(i);
//				arrayOfCentralComponents.remove(iCentralPanel);
//				iCentralPanel.dispose();
			}
		}
	}

	public int getMarginOfCentralPanel() {
		return marginOfCentralPanel;
	}

	public void setMarginOfCentralPanel(int marginOfCentralPanel) {
		this.marginOfCentralPanel = marginOfCentralPanel;
	}

	@Override
	public void fillHomepageShortcutMenu(FocXMLLayout centralPanel) {
	}
	
	@Override
	public void showValidationLayout(Component validationLayout) {
		
	}

	@Override
	public FVMenuTree getMenuTree(boolean createIfNeeded){
		if(menuTree == null && createIfNeeded){
			menuTree = new FVMenuTree();
			menuTree.setTreeType(FVMenuTree.TYPE_NORMAL);
	  	menuTree.fill();
		}
		return menuTree;
	}
}