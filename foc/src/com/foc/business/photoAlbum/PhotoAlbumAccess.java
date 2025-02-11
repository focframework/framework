package com.foc.business.photoAlbum;

import com.foc.admin.DocRightsGroup;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@SuppressWarnings("serial")
public class PhotoAlbumAccess extends FocObject{
  
  public PhotoAlbumAccess(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  public PhotoAlbum getPhotoAlbum(){
  	return (PhotoAlbum) getPropertyObject(PhotoAlbumAccessDesc.FLD_PHOTO_ALBUM);
  }
	
	public void setPhotoAlbum(PhotoAlbum photoAlbum){
  	setPropertyObject(PhotoAlbumAccessDesc.FLD_PHOTO_ALBUM, photoAlbum);
  }
	
	public DocRightsGroup getDocRightsGroup(){
  	return (DocRightsGroup) getPropertyObject(PhotoAlbumAccessDesc.FLD_DOC_RIGHTS_GROUP);
  }
	
	public void setDocRightsGroup(DocRightsGroup docRightsGroup){
  	setPropertyObject(PhotoAlbumAccessDesc.FLD_DOC_RIGHTS_GROUP, docRightsGroup);
  }
	
	public int getAccessRight(){
		return getPropertyMultiChoice(PhotoAlbumAccessDesc.FLD_ACCESS_RIGHT);
	}	

	public void setAccessRightType(int accessRight){
		setPropertyMultiChoice(PhotoAlbumAccessDesc.FLD_ACCESS_RIGHT, accessRight);
	}
}
