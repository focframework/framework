package com.foc.vaadin.gui.layouts;


import com.foc.Globals;
import com.foc.vaadin.gui.FVIconFactory;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.details.AbsoluteLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

public class FVDeleteDropHandler extends AbstractDefaultLayoutDropHandler {
  @Override
  protected void handleComponentReordering(DragAndDropEvent event) {
      AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event
              .getTargetDetails();
      DDAbsoluteLayout layout = (DDAbsoluteLayout) details.getTarget();
      LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
              .getTransferable();
      Component component = transferable.getComponent();

      // Get top-left pixel position
      int leftPixelPosition = details.getRelativeLeft();
      int topPixelPosition = details.getRelativeTop();

      ComponentPosition position = layout.getPosition(component);

      position.setLeft((float) leftPixelPosition, com.vaadin.server.Sizeable.Unit.PIXELS);
      position.setTop((float) topPixelPosition, com.vaadin.server.Sizeable.Unit.PIXELS);
  }

  /**
   * Handle a drop from another layout
   * 
   * @param event
   *            The drag and drop event
   */
  @Override
  protected void handleDropFromLayout(DragAndDropEvent event) {
      AbsoluteLayoutTargetDetails details = (AbsoluteLayoutTargetDetails) event
              .getTargetDetails();
      LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
              .getTransferable();
      Component component = transferable.getComponent();
      Component source = event.getTransferable().getSourceComponent();
      DDAbsoluteLayout layout = (DDAbsoluteLayout) details.getTarget();
      int leftPixelPosition = details.getRelativeLeft();
      int topPixelPosition = details.getRelativeTop();

      // Check that we are not dragging an outer layout into an
      // inner
      // layout
      Component parent = source.getParent();
      while (parent != null) {
          parent = parent.getParent();
      }

      // remove component from source
      if (source instanceof ComponentContainer) {
          ComponentContainer sourceLayout = (ComponentContainer) source;
          sourceLayout.removeComponent(component);
      }

      // Add component to absolute layout
      layout.addComponent(component, "left:" + leftPixelPosition + "px;top:"
              + topPixelPosition + "px");
      
      layout.removeAllComponents();
      layout.addComponent(FVIconFactory.getInstance().getFVIcon(FVIconFactory.ICON_TRASH, false));
  }
  
	@Override
	public Class<? extends HasComponents> getTargetLayoutType() {
		return null;
	}
}
