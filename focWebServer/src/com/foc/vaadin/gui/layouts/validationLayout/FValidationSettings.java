package com.foc.vaadin.gui.layouts.validationLayout;

import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.xmlForm.FXML;

public class FValidationSettings {
  public boolean withValidationLayout = true;
  public String  title             = null;
  public boolean withApply         = false;
  public String  discardLink       = null;
  public String  applyLink         = null;
  public String  computeLevel       = null;
  public boolean withSave          = true;
  public boolean withDiscard       = false;
  public boolean withPrint         = false;
  public boolean withLog           = false;
  public boolean withPrintAndExit  = false;
  public boolean withAttach        = true;
  public boolean withEmail         = false;
  public boolean withInternalEmail = true;
  public boolean withViewSelector  = true;
  public boolean withTips          = false;
  public boolean withStatus        = true;
  public boolean withStage         = true;
  public boolean commitData        = true;
  public boolean hasPDFGenerator   = false;
  public boolean hasMSWordGenerator= false;
  public boolean avoidRowBreak = false;
  public boolean reportPrintAsWord = true;
  public boolean reportPrintAsRTF  = true;
  public boolean reportSendEMail   = true;

//  public boolean printAndExit = false;

  public FValidationSettings() {
    withApply = true;
    withDiscard = true;
    withPrint = true;
  }
  
  public void dispose(){
  	
  }
  
  public void copy(FValidationSettings s) {
    withValidationLayout = s.withValidationLayout;
    title             = s.title;
    withApply         = s.withApply;
    discardLink       = s.discardLink;
    applyLink         = s.applyLink;
    computeLevel      = s.computeLevel;
    withSave          = s.withSave;
    withDiscard       = s.withDiscard;
    withPrint         = s.withPrint;
    withPrintAndExit  = s.withPrintAndExit;
    withAttach        = s.withAttach;
    withEmail         = s.withEmail;
    withInternalEmail = s.withInternalEmail;
    withViewSelector  = s.withViewSelector;
    withTips          = s.withTips;
    withStatus        = s.withStatus;
    commitData        = s.commitData;
    hasPDFGenerator   = s.hasPDFGenerator;
    hasMSWordGenerator= s.hasMSWordGenerator;
    avoidRowBreak     = s.avoidRowBreak;
  }
  
//  public boolean isPrintAndExit(){
//  	return printAndExit;
//  }
//  
//  public void setPrintAndExit(boolean printAndExit) {
//    this.printAndExit = printAndExit;
//  }

  public boolean isWithSave() {
    return withSave;
  }

  public void setWithSave(boolean withSave) {
    this.withSave = withSave;
  }
  
  public boolean isWithApply() {
    return withApply;
  }

  public void setWithApply(boolean withApply) {
    this.withApply = withApply;
  }

  public boolean isWithDiscard() {
    return withDiscard;
  }
  
  public void setWithDiscard(boolean withDiscard) {
    this.withDiscard = withDiscard;
  }
  
  public String getDiscardLink() {
    return discardLink != null ? discardLink : "";
  }

  public void setDiscardLink(String discardLink) {
    this.discardLink = discardLink;
  }
  
  public String getApplyLink() {
    return applyLink != null ? applyLink : "";
  }

  public void setApplyLink(String applyLink) {
    this.applyLink = applyLink;
  }
  
  public boolean isWithPrint() {
    return withPrint;
  }
  
  public boolean isWithLog() {
    return withLog;
  }
  
  public boolean hasPDFGenerator(){
  	return hasPDFGenerator;
  }
  public void setWithPDFGenerator(boolean hasPDFGenerator) {
    this.hasPDFGenerator = hasPDFGenerator;
  }
  
  public boolean hasMSWordGenerator(){
  	return hasMSWordGenerator;
  }
  public void setWithMSWordGenerator(boolean hasMSWordGenerator) {
    this.hasMSWordGenerator = hasMSWordGenerator;
  }

  public void setWithPrint(boolean withPrint) {
    this.withPrint = withPrint;
  }
  
  public void setWithLog(boolean withLog) {
    this.withLog = withLog;
  }
  
  public void setAvoidRowBreak(boolean avoidRowBreak) {
    this.avoidRowBreak = avoidRowBreak;
  }
  
  public boolean avoidRowBreak() {
    return avoidRowBreak;
  }
  
  public void setWithPrintAndExit(boolean withPrintAndExit) {
    this.withPrintAndExit = withPrintAndExit;
  }
  
  public boolean isWithPrintAndExit() {
    return withPrintAndExit;
  }
  
  public boolean isWithAttach() {
    return withAttach;
  }

  public void setWithAttach(boolean withAttach) {
    this.withAttach = withAttach;
  }

  public boolean isWithEmail() {
    return withEmail;
  }
  
  public void setWithEmail(boolean withEmail) {
    this.withEmail = withEmail;
  }
  
  public boolean isWithInternalEmail() {
    return withInternalEmail;
  }

  public void setWithInternalEmail(boolean withInternalEmail) {
    this.withInternalEmail = withInternalEmail;
  }
  
  public boolean isWithViewSelector() {
    return withViewSelector;
  }

  public void setWithViewSelector(boolean withViewSelector) {
    this.withViewSelector = withViewSelector;
  }
  
  public boolean isWithTips() {
  	return withTips;
  }
  
  public void setWithTips(boolean withTips){
  	this.withTips = withTips;
  }

  public boolean isWithStatus() {
  	return withStatus;
  }
  
  public void setWithStatus(boolean withStatus){
  	this.withStatus= withStatus;
  }
  
  public boolean isWithStage() {
		return withStage;
	}

	public void setWithStage(boolean withStage) {
		this.withStage = withStage;
	}

	public String getTitle() {
    return title != null ? title : "";
  }

  public void setTitle(String title) {
    this.title = title;
  }

  private String b2s(boolean b) {
    return b ? "true" : "false";
  }

  public void fillXML(XMLBuilder builder) {
    builder.appendSpaces();
    builder.append("<" + FXML.TAG_VALIDATION_SETTINGS + " ");
    builder.append(FXML.ATT_FORM_TITLE + "=\"" + getTitle() + "\" ");
    builder.append(FXML.ATT_WITH_APPLY + "=\"" + b2s(isWithApply()) + "\" ");
    builder.append(FXML.ATT_WITH_DISCARD + "=\"" + b2s(isWithDiscard()) + "\" ");
    builder.append(FXML.ATT_WITH_ATTACH + "=\"" + b2s(isWithAttach()) + "\" ");
    builder.append(FXML.ATT_WITH_PRINT + "=\"" + b2s(isWithPrint()) + "\" ");
    builder.append(FXML.ATT_WITH_EMAIL + "=\"" + b2s(isWithEmail()) + "\" ");
    builder.append(FXML.ATT_WITH_INTERNAL_EMAIL + "=\"" + b2s(isWithInternalEmail()) + "\" ");
    builder.append(FXML.ATT_WITH_TIPS + "=\"" + b2s(isWithTips()) + "\" ");
    builder.append(FXML.ATT_PRINT_AND_EXIT + "=\"" + b2s(isWithPrintAndExit()) + "\" ");
    builder.append("/>\n");
  }

  public boolean isCommitData() {
    return commitData;
  }

  public void setCommitData(boolean commitData) {
    this.commitData = commitData;
  }

	public boolean isReportPrintAsWord() {
		return reportPrintAsWord;
	}

	public void setReportPrintAsWord(boolean reportPrintAsWord) {
		this.reportPrintAsWord = reportPrintAsWord;
	}

	public boolean isReportPrintAsRTF() {
		return reportPrintAsRTF;
	}

	public void setReportPrintAsRTF(boolean reportPrintAsRTF) {
		this.reportPrintAsRTF = reportPrintAsRTF;
	}
	
	public boolean isReportSendEMail() {
		return reportSendEMail;
	}

	public void setReportSendEMail(boolean reportSendEMail) {
		this.reportSendEMail = reportSendEMail;
	}
}
