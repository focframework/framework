package com.foc.business.dateShifter;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.desc.field.FIntField;
import com.foc.desc.field.FMultipleChoiceField;
import com.foc.property.FProperty;
import com.foc.property.FPropertyListener;

public class DateShifterDesc {

	private FocDesc focDesc     = null;
	private int     shift       = 0;
	private String  fieldSuffix = "";
	private int     dateFieldID = 0;
	
	public static final int FLD_YEAR             = 1;
	public static final int FLD_MONTH            = 2;
	public static final int FLD_MONTH_SHIFT      = 3;
	public static final int FLD_DAY              = 4;
	public static final int FLD_DAY_SHIFT        = 5;
	public static final int FLD_IS_SPECIFIC_DATE = 6;

	public static final int YEAR_KEY_CURRENT_YEAR_MINUS_4 = -4;
	public static final int YEAR_KEY_CURRENT_YEAR_MINUS_3 = -3;
	public static final int YEAR_KEY_CURRENT_YEAR_MINUS_2 = -2;
	public static final int YEAR_KEY_PREVIOUS_YEAR        = -1;
	public static final int YEAR_KEY_CURRENT_YEAR         = 0;
	public static final int YEAR_KEY_NEXT_YEAR            = 1;
	public static final int YEAR_KEY_CURRENT_YEAR_PLUS_2  = 2;
	public static final int YEAR_KEY_CURRENT_YEAR_PLUS_3  = 3;
	public static final int YEAR_KEY_CURRENT_YEAR_PLUS_4  = 4;
	
	public static final int MONTH_KEY_CURRENT_MONTH  = 13;
	
	public static final int DAY_KEY_CURRENT_DAY_OF_MONTH = 0;
	public static final int DAY_KEY_END_OF_MONTH         = 32;
	
	public static final int IS_SPECIFIC_DATE_FALSE = 0;
	public static final int IS_SPECIFIC_DATE_TRUE  = 1;
	
	public DateShifterDesc(FocDesc focDesc, int shift, String suffix, int dateFieldID) {
		this.focDesc     = focDesc;
		this.shift       = shift;
		this.fieldSuffix = suffix;
		this.dateFieldID = dateFieldID;
	}
	
	public void dispose(){
		focDesc = null;
	}

	private String adjustFieldName(String fieldName){
		if(fieldSuffix != null && !fieldSuffix.isEmpty()){
			fieldName = fieldName.concat("_" + fieldSuffix);
		}
		return fieldName;
	}
	
	public void addFields(){
		if(getFocDesc() != null){
			int fieldsShift = getFieldsShift();
			FPropertyListener listener = new FPropertyListener(){

				@Override
				public void propertyModified(FProperty property) {
					FocObject focObj = property != null ? property.getFocObject() : null;
					if(focObj != null){
						IDateShifterHolder iDateShifterHolder = (IDateShifterHolder) focObj;
						DateShifter        dateShifter        = iDateShifterHolder.getDateShifter(getFieldsShift());
						dateShifter.adjustDate();
					}
				}

				@Override
				public void dispose() {
				}
			};
			
			FMultipleChoiceField choiceField = new FMultipleChoiceField(adjustFieldName("YEAR"), "Year", fieldsShift + FLD_YEAR, false, 5);
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR_MINUS_4, "Current Year - 4");
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR_MINUS_3, "Current Year - 3");
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR_MINUS_2, "Current Year - 2");
			choiceField.addChoice(YEAR_KEY_PREVIOUS_YEAR       , "Previous Year");
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR        , "Current Year");
			choiceField.addChoice(YEAR_KEY_NEXT_YEAR           , "Next Year");
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR_PLUS_2 , "Current Year + 2");
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR_PLUS_3 , "Current Year + 3");
			choiceField.addChoice(YEAR_KEY_CURRENT_YEAR_PLUS_4 , "Current Year + 4");
			for(int i=2010; i<2025; i++){
				choiceField.addChoice( i, ""+i);
			}
			choiceField.setSortItems(false);
			getFocDesc().addField(choiceField);
			choiceField.addListener(listener);

			choiceField = new FMultipleChoiceField(adjustFieldName("MONTH"), "Month", fieldsShift + FLD_MONTH, false, 2);
			choiceField.setSortItems(false);
			Map<String, Integer> monthsMap   = Calendar.getInstance().getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.US);
			if(monthsMap != null){
				String[] monthsNameArray = monthsMap.keySet().toArray(new String[monthsMap.keySet().size()]);
				for(int i=0; i<monthsNameArray.length; i++){
					String monthName  = monthsNameArray[i];
					int    monthIndex = monthsMap.get(monthName);
					choiceField.addChoice(monthIndex, monthName);
				}
				choiceField.addChoice(MONTH_KEY_CURRENT_MONTH, "Current Month");
			}
			getFocDesc().addField(choiceField);
			choiceField.addListener(listener);
			
			FIntField numField = new FIntField(adjustFieldName("MONTH_SHIFT"), "Month Shift", fieldsShift + FLD_MONTH_SHIFT, false, 2);
			getFocDesc().addField(numField);
			numField.addListener(listener);
			
			choiceField = new FMultipleChoiceField(adjustFieldName("DAY"), "Day", fieldsShift + FLD_DAY, false, 2);
			choiceField.setSortItems(false);
			choiceField.addChoice(DAY_KEY_CURRENT_DAY_OF_MONTH, "Current Day of Month");
			for(int i=1; i<=31; i++){
				choiceField.addChoice(i, String.valueOf(i));
			}
			choiceField.addChoice(DAY_KEY_END_OF_MONTH, "End of Month");
			getFocDesc().addField(choiceField);
			choiceField.addListener(listener);
			
			FMultipleChoiceField bFld = new FMultipleChoiceField(adjustFieldName("IS_SPECIFIC_DATE"), "Specific Date", fieldsShift + FLD_IS_SPECIFIC_DATE, false, 2);
			bFld.addChoice(IS_SPECIFIC_DATE_FALSE, "Use Date Shifter");
			bFld.addChoice(IS_SPECIFIC_DATE_TRUE, "Specific Date");
			getFocDesc().addField(bFld);
		
//			numField = new FNumField(adjustFieldName("DAY_SHIFT"), "Day Shift", fieldsShift + FLD_DAY_SHIFT, false, 2, 0);
//			getFocDesc().addField(numField);
		}
	}
	
	private FocDesc getFocDesc(){
		return focDesc;
	}
	
	public int getFieldsShift(){
		return shift;
	}

	public int getDateFieldID() {
		return dateFieldID;
	}
}
