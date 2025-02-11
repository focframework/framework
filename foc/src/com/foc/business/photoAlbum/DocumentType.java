package com.foc.business.photoAlbum;

import com.foc.Globals;
import com.foc.admin.DocRightsGroup;
import com.foc.admin.DocRightsGroupUsers;
import com.foc.admin.DocRightsGroupUsersDesc;
import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class DocumentType extends FocObject {

  public DocumentType(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public String getCaption(){
  	return getPropertyString(DocumentTypeDesc.FLD_CAPTION);
  }
  
  public void setCaption(String caption){
  	setPropertyString(DocumentTypeDesc.FLD_CAPTION, caption);
  }
  
  public FocList getDocumentTypeAccessList(){
  	return getPropertyList(DocumentTypeDesc.FLD_DOCUMENT_TYPE_ACCESS_LIST);
  }
  
  public int hasAccess(){
		FocUser currentUser = Globals.getApp().getUser_ForThisSession();
		return hasAccess(currentUser);
	}
	
	public int hasAccess(FocUser currentUser){
		int include = DocTypeAccessDesc.READ_ONLY;
		if(currentUser != null && getDocumentTypeAccessList() != null){
			FocList documentTypeAccessList = getDocumentTypeAccessList();
			documentTypeAccessList.loadIfNotLoadedFromDB();
			
			if(documentTypeAccessList.size() == 0){
				include = DocTypeAccessDesc.READ_WRITE;
			}else{
			  for(int i=0; i<documentTypeAccessList.size() && (include == DocTypeAccessDesc.READ_ONLY); i++){
			  	DocTypeAccess docTypeAccess = (DocTypeAccess) documentTypeAccessList.getFocObject(i);
			  	DocRightsGroup docRightsGroup = docTypeAccess.getDocRightsGroup();
			  	if(docRightsGroup != null){
			  		FocList userList = docRightsGroup.getDocRightsGroupUsersList();
			  		DocRightsGroupUsers rightUser = (DocRightsGroupUsers) userList.searchByPropertyObjectReference(DocRightsGroupUsersDesc.FLD_USER, currentUser.getReferenceInt());
			  		include = rightUser != null ? DocTypeAccessDesc.READ_WRITE : DocTypeAccessDesc.READ_ONLY;
			  	}
			  }
			}
		}
		return include;
	}
  
//  public boolean hasAccess(){
//		FocUser currentUser = Globals.getApp().getUser_ForThisSession();
//		return hasAccess(currentUser);
//	}
//	
//	public boolean hasAccess(FocUser currentUser){
//		boolean include = false;
//		if(currentUser != null && getDocumentTypeAccessList() != null){
//			FocList documentTypeAccessList = getDocumentTypeAccessList();
//			documentTypeAccessList.reloadFromDB();
//			
//			if(documentTypeAccessList.size() == 0){
//				include = true;
//			}else{
//			  for(int i=0; i<documentTypeAccessList.size() && !include; i++){
//			  	DocTypeAccess docTypeAccess = (DocTypeAccess) documentTypeAccessList.getFocObject(i);
//			  	DocRightsGroup docRightsGroup = docTypeAccess.getDocRightsGroup();
//			  	if(docRightsGroup != null){
//			  		FocList userList = docRightsGroup.getDocRightsGroupUsersList();
//			  		DocRightsGroupUsers rightUser = (DocRightsGroupUsers) userList.searchByPropertyObjectReference(DocRightsGroupUsersDesc.FLD_USER, currentUser.getReferenceInt());
//			  		include = rightUser != null;
//			  	}
//			  }
//			}
//		}
//		return include;
//	}
}
