package com.foc.business.adrBook;

import com.foc.Globals;
import com.foc.admin.FocUser;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.desc.ReferenceChecker;
import com.foc.property.FProperty;
import com.foc.util.Encryptor;

@SuppressWarnings("serial")
public class Contact extends FocObject {
	
  public Contact(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }
  
  @Override
  public FProperty getFocProperty(int fldID){
  	FProperty prop = super.getFocProperty(fldID);
  	return prop;
  }
  
  @Override
  public boolean delete(ReferenceChecker referenceCjeckerToIgnore) {
  	FocUser user = findUser();
  	if(user != null){
  		if(user.isGuest()){
  			user.setSuspended(true);
  		}
  		user.setContact(null);	
  		user.validate(true);
  	}
  	
  	boolean succcess = super.delete(referenceCjeckerToIgnore);
  	return succcess;
  }
  
  public String getTitle(){
  	return getPropertyString(ContactDesc.FLD_TITLE);
  }
  
  public String getFirstName(){
  	return getPropertyString(ContactDesc.FLD_FIRST_NAME);
  }
  
  public void setFirstName(String firstName){
  	setPropertyString(ContactDesc.FLD_FIRST_NAME, firstName);
  }
  
  public String getFamilyName(){
  	return getPropertyString(ContactDesc.FLD_FAMILY_NAME);
  }

  public void setFamilyName(String lastName){
  	setPropertyString(ContactDesc.FLD_FAMILY_NAME, lastName);
  }

  public String getPosition(){
  	return getPropertyString(ContactDesc.FLD_POSITION_STR);
  }

  public String getFullName(){
  	return getPropertyString(ContactDesc.FLD_FULL_NAME);
  }
  
  public void setFullName(String fullName){
  	setPropertyString(ContactDesc.FLD_FULL_NAME, fullName);
  }
  
  public String getCompanyName(){
  	return getPropertyString(ContactDesc.FLD_COMPANY_NAME);
  }

  public void setCompanyName(String companyName){
  	setPropertyString(ContactDesc.FLD_COMPANY_NAME, companyName);
  }

  public AdrBookParty getAdrBookParty(){
  	return (AdrBookParty) getPropertyObject(ContactDesc.FLD_ADR_BOOK_PARTY);
  }

  public void setAdrBookParty(AdrBookParty party){
  	setPropertyObject(ContactDesc.FLD_ADR_BOOK_PARTY, party);
  }

  public String getPhone1(){
  	return getPropertyString(ContactDesc.FLD_PHONE_1);
  }
  
  public String getPhone2(){
  	return getPropertyString(ContactDesc.FLD_PHONE_2);
  }

  public String getMobile(){
  	return getPropertyString(ContactDesc.FLD_MOBILE);
  }

  public String getEMail(){
  	return getPropertyString(ContactDesc.FLD_EMAIL);
  }

  public void setEMail(String email){
  	setPropertyString(ContactDesc.FLD_EMAIL, email);
  }

  public String getEMail2(){
  	return getPropertyString(ContactDesc.FLD_EMAIL_2);
  }

  public void adjustFullName(){
  	String str = getTitle() != null ? getTitle() : "";
  	if(str != null && !str.isEmpty()){
  		str += " ";
  	}
  	if(getFirstName() != null) str += getFirstName();
  	str += " ";
  	if(getFamilyName() != null) str += getFamilyName();
  	setFullName(str);
  }
  
  public FocUser findUser(){
    FocUser user = FocUser.findUser(this);
    return user;
  }
  
  public UserCreationData findUser_CreateIfNeeded(){
  	UserCreationData userCreationData = new UserCreationData();
  	
  	FocUser user = findUser();
  	if(user == null){
  		String password = FocUser.newRandomPassword();
  		if(Globals.getApp().isUnitTest()){
  			password = "ABCDEF";
  		}
  		userCreationData.setPassword(password);
  		String ecryptedPassword = Encryptor.encrypt_MD5(password);
  		user = FocUser.createUserForContact_UserNameIsEmail(this, ecryptedPassword);
  		userCreationData.setCreated(true);
  	}
  	userCreationData.setUser(user);
  	
  	return userCreationData;
  }
  
  public class UserCreationData {
  	private FocUser user     = null;
  	private String  password = null;
  	private boolean created  = false;
  	
		public FocUser getUser() {
			return user;
		}
		public void setUser(FocUser user) {
			this.user = user;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean isCreated() {
			return created;
		}
		public void setCreated(boolean created) {
			this.created = created;
		}
  }
}
