package com.foc.business.config;

import com.foc.Globals;
import com.foc.admin.UserSession;
import com.foc.business.company.Company;
import com.foc.business.notifier.FocNotificationEmailTemplate;
import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;
import com.foc.list.FocList;

@SuppressWarnings("serial")
public class BusinessConfig extends FocObject {

	public BusinessConfig(FocConstructor constr) {
    super(constr);
    newFocProperties();
  }

	public String getPartyPrefix(){
		String codePrefix = getPropertyString(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_PREFIX);
		if(codePrefix != null){
  		codePrefix = adjustCodePrefix(codePrefix);
  	}
  	return codePrefix;
  }

	public String getPartyCodePrefix(){
  	return getPropertyString(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_PREFIX);
  }

	public void setPartyCodePrefix(String prefix){
  	setPropertyString(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_PREFIX, prefix);
  }
	
	public FocNotificationEmailTemplate getGeneralEmailTemplate(){
  	return (FocNotificationEmailTemplate) getPropertyObject(BusinessConfigDesc.FLD_GENERAL_EMAIL_TEMPLATE);
  }
  
  public void setGeneralEmailTemplate(FocNotificationEmailTemplate emailTemplate){
  	setPropertyObject(BusinessConfigDesc.FLD_GENERAL_EMAIL_TEMPLATE, emailTemplate);
  }

	public String getPartySeparator(){
  	return getPropertyString(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_SEPERATOR);
  }

	public int getPartyNbrDigits(){
  	return getPropertyInteger(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_NBR_DIGITS);
  }
	
	public void setPartyNbrDigits(int nbrDigits){
  	setPropertyInteger(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_NBR_DIGITS, nbrDigits);
  }

	public boolean getPartyResetCode(){
  	return getPropertyBoolean(BusinessConfigDesc.FLD_ADR_BOOK_PARTY_RESET_NUMBERING_WHEN_PREFIX_CHANGE);
  }
	
	public boolean isAddressBookParty121(){
  	return getPropertyBoolean(BusinessConfigDesc.FLD_ADR_BOOK_TO_PARTY_ONE_2_ONE);
  }
	
	public void setAddressBookParty121(boolean isAddressBookParty121){
  	setPropertyBoolean(BusinessConfigDesc.FLD_ADR_BOOK_TO_PARTY_ONE_2_ONE, isAddressBookParty121);
  }
	
	public boolean isContactInPartyMandatory(){
  	return getPropertyBoolean(BusinessConfigDesc.FLD_CONTACT_IN_PARTY_MANDATORY);
  }
	
	public void setContactInPartyMandatory(boolean isContactInPartyMandatory){
  	setPropertyBoolean(BusinessConfigDesc.FLD_CONTACT_IN_PARTY_MANDATORY, isContactInPartyMandatory);
  }

  public boolean isAllowModif_UndDesc_EvenIfFilledInUnderlying(){
  	return getPropertyBoolean(BusinessConfigDesc.FLD_ALLOW_MODIF_OF_UND_DESC_EVEN_IF_FILLED_IN_UND);
  }

  public String getProjectIdPrefix(){
  	return getPropertyString(BusinessConfigDesc.FLD_PROJECT_ID_PREFIX);
  }

	public int getProjectIdNbrDigits(){
  	return getPropertyInteger(BusinessConfigDesc.FLD_PROJECT_ID_NBR_DIGITS);
  }

	public static BusinessConfig getOrCreateForCompany(Company company){
		BusinessConfig foundConfig = null; 
				
    FocList basicsConfigList = BusinessConfigDesc.getList(FocList.LOAD_IF_NEEDED);
    for(int i=0; i<basicsConfigList.size() && foundConfig == null; i++){
    	BusinessConfig cfg = (BusinessConfig) basicsConfigList.getFocObject(i);
    	if(cfg != null && cfg.isForCompany(company)){
    		foundConfig = cfg;
    	}
    }
    if(foundConfig == null){
    	foundConfig = (BusinessConfig) basicsConfigList.newEmptyItem();
    	foundConfig.setCompany(company);
    	basicsConfigList.add(foundConfig);
    }
    
    return foundConfig;
	}
	
//  private static BusinessConfig generalConfig = null;
  public synchronized static BusinessConfig getInstance(){
  	BusinessConfig generalConfig = (BusinessConfig) UserSession.getParameter("BUSINESS_CONFIG");
  	if(generalConfig == null || !generalConfig.isForCurrentCompany()){
  		generalConfig = getOrCreateForCompany(Globals.getApp().getCurrentCompany());
  		if(generalConfig != null){
  			UserSession.putParameter("BUSINESS_CONFIG", generalConfig);
  		}
  	}
  	return generalConfig;
  }
}