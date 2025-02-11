package com.foc.vaadin.gui.manipulators;

import org.xml.sax.Attributes;

import com.foc.Globals;
import com.foc.shared.dataStore.IFocData;
import com.foc.util.Utils;
import com.foc.vaadin.FocWebEnvironment;
import com.foc.vaadin.fields.FocXMLGuiComponentCreator;
import com.foc.vaadin.gui.FocXMLGuiComponent;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.components.chart.FVBarChart;
import com.foc.vaadin.gui.components.chart.FVChart;
import com.foc.vaadin.gui.components.chart.FVColumnChart;
import com.foc.vaadin.gui.components.chart.FVLineChart;
import com.foc.vaadin.gui.components.chart.FVPieChart;
import com.foc.vaadin.gui.layouts.FVChartWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;

public class FVChartCreator implements FocXMLGuiComponentCreator {

	@Override
	public FocXMLGuiComponent newGuiComponent(FocXMLLayout xmlLayout, IFocData focData, Attributes attributes, IFocData rootFocData, String dataPathFromRootFocData) {
		FVChart chart = null;
		String chartType = attributes.getValue(FXML.ATT_CHART_TYPE);
		
		if(!Utils.isStringEmpty(chartType)){
			
			if(chartType.equalsIgnoreCase(FXML.VAL_CHART_PIE)){
				chart = new FVPieChart(attributes);
			}else if(chartType.equalsIgnoreCase(FXML.VAL_CHART_BAR)){
				chart = new FVBarChart(attributes);
			}else if(chartType.equalsIgnoreCase(FXML.VAL_CHART_COLUMN)){
				chart = new FVColumnChart(attributes);
			}else if(chartType.equalsIgnoreCase(FXML.VAL_CHART_LINE)){
				chart = new FVLineChart(attributes);
			}
		}
		
		if(chart != null && focData != null){
			chart.setFocData(focData);
    }else{
      Globals.showNotification("NULL DATA", "For Chart", FocWebEnvironment.TYPE_WARNING_MESSAGE);
    }
		
		FVChartWrapperLayout chartWrapperLayout = new FVChartWrapperLayout();
		FocXMLGuiComponentStatic.setRootFocDataWithDataPath(chartWrapperLayout, rootFocData, dataPathFromRootFocData);
		chartWrapperLayout.setAttributes(attributes);
		chartWrapperLayout.setTableOrTree(xmlLayout, chart);		
		return chartWrapperLayout;
	}

}
