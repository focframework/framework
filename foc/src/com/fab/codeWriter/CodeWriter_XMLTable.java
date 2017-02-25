package com.fab.codeWriter;

import com.fab.model.table.FieldDefinition;
import com.foc.list.FocList;

public class CodeWriter_XMLTable extends CodeWriter {

	public CodeWriter_XMLTable(CodeWriterSet set){
		super(set);
	}

	@Override
	public boolean hasInternalFile() {
		return true;
	}

	@Override
	public boolean hasExternalFile() {
		return true;
	}

	@Override
	public String getFileSuffix() {
		return XML_VIEW_TABLE_SUFFIX;
	}
	
	@Override
	public boolean isServerSide() {
		return true;
	}
	
	@Override
	public String getFileExtention() {
		return "xml";
	}
	
	@Override
	public String getPackageName(boolean autoGen) {
		return null;
	}
	
	@Override	
	public void generateCode() {
		initFiles();
		
		CodeWriter_OneFile extWriter = getExternalFileWriter();
		if(extWriter != null && getTblDef() != null && getTblDef().getFieldDefinitionList() != null){
			extWriter.printCore("<VerticalLayout>\n");//Open Layout Tag

			extWriter.printCore("\t<ValidationSettings withApply=\"false\" withAttach=\"false\" withPrint=\"false\"/>\n\n");

			//GuiTable Tag Content - B
			extWriter.printCore("\t<GuiTable");//Open GuiTabe Tag
			extWriter.printCore(" name=\"" + getCodeWriterSet().getCodeWriter_Const().getConstant_TableName().toUpperCase() +"_TABLE\"");//Table Name
			extWriter.printCore(" dataPath=\"DATAROOT\"");//DataPath
			extWriter.printCore(" width=\"100%\"");//width
			extWriter.printCore(" height=\"99%\"");//Height
			extWriter.printCore(">\n");
			//GuiTable Tag Content - E
			
			FocList tableFieldDefinitionList = getTblDef().getFieldDefinitionList();
			for(int i=0; i<tableFieldDefinitionList.size(); i++){
				FieldDefinition fieldDefinition = (FieldDefinition) tableFieldDefinitionList.getFocObject(i);
				if(fieldDefinition != null){
					extWriter.printCore("\t\t");
					extWriter.printCore("<TableColumn");
					extWriter.printCore(" name=\"" + fieldDefinition.getName() + "\"");
					extWriter.printCore(" caption=\"" + fieldDefinition.getTitle() + "\"");
					extWriter.printCore(" />\n");
				}
			}
			
			extWriter.printCore("\t</GuiTable>\n");//Close Table Tag
			extWriter.printCore("</VerticalLayout>");//Close Layout Tag
			
			extWriter.compile();
			closeFiles();
		}
	}
}
