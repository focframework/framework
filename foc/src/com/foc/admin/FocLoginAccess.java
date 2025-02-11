package com.foc.admin;

import com.foc.Application;
import com.foc.Globals;
import com.foc.util.Encryptor;

public class FocLoginAccess {
	private FocUser user        = null;
	private int     loginStatus = Application.LOGIN_WRONG;
	
	public FocLoginAccess(){
	}
	
	public void dispose(){
		user = null;
	}
	
	public int checkUserPassword(String userName, String encryptedPassword){
		return checkUserPassword(userName, encryptedPassword, true);
	}
	
	public int checkUserPassword(String userName, String encryptedPassword, boolean setUserAsRootUser){
  	loginStatus   = Application.LOGIN_WRONG;
    user          = FocUser.findUser(userName);
    String  typedPassword = encryptedPassword;
    
    //We have a case for unset Passwords
    if (user != null){
    	boolean credentialsCorrect = false;
    	if(user.getPassword().isEmpty()){
    		String emptyEncriptionPassword = Encryptor.encrypt_MD5("");    		
    		credentialsCorrect = typedPassword.startsWith(emptyEncriptionPassword);
    	}else if(typedPassword.startsWith(user.getPassword())){
    		credentialsCorrect = true;
    	}
    	if(credentialsCorrect && !user.isSuspended()){
        if(setUserAsRootUser) Globals.getApp().setUser(user);
        if (user.isAdmin()) {
        	loginStatus = Application.LOGIN_ADMIN;
        } else {
        	loginStatus = Application.LOGIN_VALID;
        }
    	}
    }
    return loginStatus;
	}

	public FocUser getUser() {
		return user;
	}

	public void setUser(FocUser user) {
		this.user = user;
	}
}
