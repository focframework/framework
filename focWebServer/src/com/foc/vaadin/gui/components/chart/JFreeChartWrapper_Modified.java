package com.foc.vaadin.gui.components.chart;

/*
 * Copyright 2009 IT Mill Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.foc.vaadin.FocWebApplication;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
//import org.apache.batik.svggen.SVGGraphics2D;
//import org.apache.batik.svggen.SVGGraphics2DIOException;
//import com.vaadin.terminal.DownloadStream;
//import com.vaadin.terminal.Resource;
//import com.vaadin.terminal.Sizeable;

/**
 * A simple JFreeChart wrapper that renders charts in SVG to browser.
 * 
 * To use this component, you'll need all the common JFreeChart and Batik
 * libraries.
 * 
 * For MSIE it will fall back to PNG rendering (not that nice when printing).
 * 
 * Supported sizes are currently just pixels, inches, centimeters (converted to
 * pixels by 96dpi). Set them to wrapper.
 * 
 * TODO make it support relative sizes (should be possible to do cleanly with
 * SVG)
 * 
 * TODO when browsers develop SVG could be painted to target instead of
 * registering application resource. This would shorten rendering time and
 * lessen memory consumption on server. This already works ok for webkit
 * browsers. Firefox and Opera fail with gradients.
 * 
 * @author mattitahvonen
 */
@SuppressWarnings("serial")
public class JFreeChartWrapper_Modified extends Embedded {

	public enum RenderingMode {
		SVG, PNG, AUTO
	}

	// 809x 500 ~g olden ratio
//	private static final int DEFAULT_WIDTH = 809;
//	private static final int DEFAULT_HEIGHT = 500;
	private static final int DEFAULT_WIDTH = 404;
	private static final int DEFAULT_HEIGHT = 250;

	private final JFreeChart chart;
//	private ApplicationResource res;
	private ConnectorResource res;
	private RenderingMode mode = RenderingMode.AUTO;
	private boolean gzipEnabled = false;
	private int graphWidthInPixels = -1;
	private int graphHeightInPixels = -1;
	private String aspectRatio = "none"; // stretch to fill whole space
	private VerticalLayout chartLayout = null;

	public JFreeChartWrapper_Modified(JFreeChart chartToBeWrapped) {
		chart = chartToBeWrapped;
		setWidth(DEFAULT_WIDTH, Unit.PIXELS);
		setHeight(DEFAULT_HEIGHT, Unit.PIXELS);
	}

	public JFreeChartWrapper_Modified(JFreeChart chartToBeWrapped, RenderingMode renderingMode) {
		this(chartToBeWrapped);
		setRenderingMode(renderingMode);
	}
	
	public JFreeChartWrapper_Modified(JFreeChart chartToBeWrapped, RenderingMode renderingMode, VerticalLayout chartLayout) {
		this(chartToBeWrapped);
		setRenderingMode(renderingMode);
		this.chartLayout = chartLayout;
	}

	/**
	 * Compress SVG charts in wrapper. It makes sense to put this on if the
	 * server does not automatically compress responses.
	 * 
	 * @param compress
	 *            true to enable component level compression, default false
	 */
	public void setGzipCompression(boolean compress) {
		this.gzipEnabled = compress;
		markAsDirty();
		
	}

	private void setRenderingMode(RenderingMode newMode) {
		if (newMode == RenderingMode.PNG) {
			setType(TYPE_IMAGE);
		} else {
			setType(TYPE_OBJECT);
			setMimeType("image/svg+xml");
		}
		mode = newMode;
	}

	@Override
	public void attach() {
//		super.attach();
//		AbstractWebApplicationContext context = (AbstractWebApplicationContext) getUI().getContext();
		ServletContext context = VaadinServlet.getCurrent().getServletContext();
		if (mode == RenderingMode.AUTO) {
			WebBrowser webBrowser = getWebBrowser();
			if(webBrowser.isIE() && webBrowser.getBrowserMajorVersion() < 9){
				setRenderingMode(RenderingMode.PNG);
			}else{
				// all decent browsers support SVG
				setRenderingMode(RenderingMode.SVG);
			}
		};
		Image image = new Image("", getSource());
		
		chartLayout.addComponent(image);
		//VAADIN 7
//		getApplication().addResource((ConnectorResource) getSource());
		
		 setResource("src", getSource());//Antoine See Workarround from https://vaadin.com/forum#!/thread/8366526
		 markAsDirtyRecursive();
	}

	@Override
	public void detach() {
		//VAADIN 7
//		getApplication().removeResource((ConnectorResource) getSource());
		super.detach();
	}
	
	/**
	 * This method may be used to tune rendering of the chart when using
	 * relative sizes. Most commonly you should use just use common methods
	 * inherited from {@link Sizeable} interface.
	 * <p>
	 * Sets the pixel size of the area where the graph is rendered. Most
	 * commonly developer may need to fine tune the value when the
	 * {@link JFreeChartWrapper_Modified} has a relative size.
	 * 
	 * @param width
	 */
	public void setGraphWidth(int width) {
		graphWidthInPixels = width;
	}

	/**
	 * This method may be used to tune rendering of the chart when using
	 * relative sizes. Most commonly you should use just use common methods
	 * inherited from {@link Sizeable} interface.
	 * <p>
	 * Sets the pixel size of the area where the graph is rendered. Most
	 * commonly developer may need to fine tune the value when the
	 * {@link JFreeChartWrapper_Modified} has a relative size.
	 * 
	 * @param height
	 */
	public void setGraphHeight(int height) {
		graphHeightInPixels = height;
	}

	/**
	 * Gets the pixel width into which the graph is rendered. Unless explicitly
	 * set, the value is derived from the components size, except when the
	 * component has relative size.
	 */
	public int getGraphWidth() {
		if (graphWidthInPixels > 0) {
			return graphWidthInPixels;
		}
		int width;
		float w = getWidth();
		if (w < 0) {
			return DEFAULT_WIDTH;
		}
		switch (getWidthUnits()) {
		
		case CM:
			width = (int) (w * 96 / 2.54);
			break;
		case INCH:
			width = (int) (w * 96);
			break;
		case PERCENTAGE:
			width = DEFAULT_WIDTH;
			break;
		default:
			width = (int) w;
			break;
		}
		return width;
	}

	/**
	 * Gets the pixel height into which the graph is rendered. Unless explicitly
	 * set, the value is derived from the components size, except when the
	 * component has relative size.
	 */
	public int getGraphHeight() {
		if (graphHeightInPixels > 0) {
			return graphHeightInPixels;
		}
		int height;
		float w = getHeight();
		if (w < 0) {
			return DEFAULT_HEIGHT;
		}
		switch (getWidthUnits()) {
		case CM:
			height = (int) (w * 96 / 2.54);
			break;
		case INCH:
			height = (int) (w * 96);
			break;
		case PERCENTAGE:
			height = DEFAULT_HEIGHT;
			break;
		default:
			height = (int) w;
			break;
		}
		return height;
	}

	public String getSvgAspectRatio() {
		return aspectRatio;
	}

	/**
	 * See SVG spec from W3 for more information.
	 * 
	 * 
	 * Default is "none" (stretch), another common value is "xMidYMid" (stretch
	 * proportionally, align middle of the area).
	 * 
	 * @param svgAspectRatioSetting
	 */
	public void setSvgAspectRatio(String svgAspectRatioSetting) {
		aspectRatio = svgAspectRatioSetting;
	}

	@Override
	public Resource getSource() {
		if (res == null) {
			res = new ConnectorResource() {
				private ByteArrayInputStream bytestream = null;
				
				ByteArrayInputStream getByteStream() {
					if (chart != null && bytestream == null) {
						int widht = getGraphWidth();
						int height = getGraphHeight();

						if (mode == RenderingMode.SVG) {
							//VAADIN 7
							/*
							DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
									.newInstance();
							DocumentBuilder docBuilder = null;
							try {
								docBuilder = docBuilderFactory
										.newDocumentBuilder();
							} catch (ParserConfigurationException e1) {
								throw new RuntimeException(e1);
							}
							Document document = docBuilder.newDocument();
							Element svgelem = document.createElement("svg");
							document.appendChild(svgelem);

							// Create an instance of the SVG Generator

							SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

							// draw the chart in the SVG generator
							chart.draw(svgGenerator, new Rectangle(widht, height));
							Element el = svgGenerator.getRoot();
							el.setAttributeNS(null, "viewBox", "0 0 " + widht + " " + height + "");
							el.setAttributeNS(null, "style", "width:100%;height:100%;");
							el.setAttributeNS(null, "preserveAspectRatio", getSvgAspectRatio());

							// Write svg to buffer
							ByteArrayOutputStream baoutputStream = new ByteArrayOutputStream();
							Writer out;
							try {
								OutputStream outputStream = gzipEnabled ? new GZIPOutputStream(
										baoutputStream) : baoutputStream;
								out = new OutputStreamWriter(outputStream,
										"UTF-8");

//								 * don't use css, FF3 can'd deal with the result
//								 * perfectly: wrong font sizes
								boolean useCSS = false;
								svgGenerator.stream(el, out, useCSS, false);
								outputStream.flush();
								outputStream.close();
								bytestream = new ByteArrayInputStream(
										baoutputStream.toByteArray());
							} catch (Exception e) {
								e.printStackTrace();
							}
							*/
						} else {
							// Draw png to bytestream
							try {
								byte[] bytes = ChartUtilities.encodeAsPNG(chart.createBufferedImage(widht, height));
								bytestream = new ByteArrayInputStream(bytes);
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					} else {
						bytestream.reset();
					}
					return bytestream;
				}
				
				@Override
				public String getMIMEType() {
					if (mode == RenderingMode.PNG) {
						return "image/png";
					} else {
						return "image/svg+xml";
					}
				}
				
				@Override
				public DownloadStream getStream() {
					DownloadStream downloadStream = new DownloadStream(
							getByteStream(), getMIMEType(), getFilename());
					if (gzipEnabled && mode == RenderingMode.SVG) {
						downloadStream.setParameter("Content-Encoding", "gzip");
					}
					return downloadStream;
				}
				
				@Override
				public String getFilename() {
					if (mode == RenderingMode.PNG) {
						return "graph.png";
					} else {
						return gzipEnabled ? "graph.svgz" : "graph.svg";
					}
				}
			};
		}
		return res;
	}

	private WebBrowser getWebBrowser(){
		return FocWebApplication.getInstanceForThread().getPage().getWebBrowser();
	}
}
