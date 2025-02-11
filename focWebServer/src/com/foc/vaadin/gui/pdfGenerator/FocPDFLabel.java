package com.foc.vaadin.gui.pdfGenerator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.foc.Globals;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLAttributes;

public class FocPDFLabel extends FocPDFComponent {

	private String value = null;
	
	public FocPDFLabel(FocPDFLayout pdfParent, FocXMLAttributes xmlAttribute) {
		super(pdfParent, xmlAttribute);
		value = xmlAttribute != null ? xmlAttribute.getValue(FXML.ATT_VALUE) : "";
	}
	
	public void dispose(){
		super.dispose();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void write(){
		try{
			PDPageContentStream contentStream = getPDPageContentStream();

			if(value != null){
//				contentStream.moveTo(0, 0);
				contentStream.beginText();
				PDType1Font font = PDType1Font.HELVETICA;
				int fontSize = 10;				
				contentStream.setFont(font, fontSize);
//				contentStream.moveTextPositionByAmount(0, getHeight());
				contentStream.moveTextPositionByAmount(getAbsoluteLeft(), getAbsoluteTop()  - getHeight());
				contentStream.drawString(value);
		    contentStream.endText();
			}
			
		}catch(Exception ex){
			Globals.logException(ex);
		}
	}

	@Override
	public void layout(float left, float top, float maxWidth, float maxHeight) {
		setLeft(left);
		setTop(top);
		float height = PDFUtil.getStringHeight(PDType1Font.HELVETICA, 10, getValue());
		float width = PDFUtil.getStringWidth(PDType1Font.HELVETICA, 10, getValue());
		setHeight(height);
		setWidth(width);
	}

	public float getLabelWidthFromAttributesIfExists(){
		float width = 0;
		String widthStrg = getXmlAttribute() != null ? getXmlAttribute().getValue(FXML.ATT_WIDTH) : null;
		if(widthStrg != null && !widthStrg.isEmpty()){
			width = Float.valueOf(widthStrg);
		}else{
			width = getWidth();			
		}
		return width;
	}

	public void debug(int indentation){
		Globals.logString(getDebugIndentation(indentation)+"Label = "+value+" - ("+getDebugString()+")");
	}
	
}
