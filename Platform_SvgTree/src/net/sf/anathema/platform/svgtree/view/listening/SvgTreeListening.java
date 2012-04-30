package net.sf.anathema.platform.svgtree.view.listening;

import net.sf.anathema.platform.svgtree.presenter.view.CharmInteractionListener;
import net.sf.anathema.platform.svgtree.presenter.view.IAnathemaCanvas;
import net.sf.anathema.platform.svgtree.presenter.view.ISvgTreeViewProperties;
import net.sf.anathema.platform.svgtree.view.batik.AnathemaCanvas;
import net.sf.anathema.platform.svgtree.view.batik.IBoundsCalculator;
import org.apache.batik.swing.gvt.Interactor;
import org.apache.batik.util.SVGConstants;
import org.jmock.example.announcer.Announcer;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGGElement;

import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SvgTreeListening {

  private final ISvgTreeViewProperties properties;
  private final IAnathemaCanvas canvas;
  private final IBoundsCalculator boundsCalculator;
  private final Announcer<CharmInteractionListener> control = Announcer.to(CharmInteractionListener.class);
  private final LeftClickPanInteractor leftClickPanner;
  private String selectionId;

  private final EventListener nodeExitListener = new EventListener() {
    @Override
    public void handleEvent(Event event) {
      canvas.setToolTipText(null);
      String nodeId = ((SVGGElement) event.getCurrentTarget()).getId();
      if (((MouseEvent) event).getButton() == 0) {
        if (selectionId == null) {
          selectionId=nodeId;
        }
        canvas.setCursorInternal(properties.getForbiddenCursor());
        leftClickPanner.toggleCursorControls();
      }
      else if (selectionId == null || selectionId.equals(nodeId)) {
        leftClickPanner.togglePanning();
        selectionId = null;
        resetCursor();
      }
    }
  };
  
  private final EventListener nodeEditedListener = new EventListener() {
    @Override
    public void handleEvent(Event event) {
      MouseEvent mouseEvent = (MouseEvent) event;
      if (!mouseEvent.getCtrlKey()) {
        return;
      }
      String nodeId = ((SVGGElement) event.getCurrentTarget()).getId();
      boolean primaryButton = mouseEvent.getButton() == 0;
      if (primaryButton && (selectionId == null || selectionId.equals(nodeId))) {
        fireNodeEditedEvent(nodeId);
      }
    }
  };

  private final EventListener cursorTooltipInitListener = new EventListener() {
    @Override
    public void handleEvent(Event event) {
      leftClickPanner.disable();
      String nodeId = ((SVGGElement) event.getCurrentTarget()).getId();
      if (selectionId == null || selectionId.equals(nodeId)) {
        selectionId = nodeId;
        setCursor(nodeId);
        setCanvasTooltip(nodeId);
      }
    }
  };

  private final EventListener selectionInvokingListener = new EventListener() {
    @Override
    public void handleEvent(Event event) {
      MouseEvent mouseEvent = (MouseEvent) event;
      if (mouseEvent.getCtrlKey()) {
        return;
      }
      String nodeId = ((SVGGElement) event.getCurrentTarget()).getId();
      boolean primaryButton = mouseEvent.getButton() == 0;
      if (primaryButton && (selectionId == null || selectionId.equals(nodeId))) {
        fireNodeSelectionEvent(nodeId);
      }
      selectionId = null;
      setCursor(nodeId);
      setCanvasTooltip(nodeId);
      event.stopPropagation();
    }
  };

  private final EventListener controlListener = new EventListener() {
    @Override
    public void handleEvent(Event evt) {
      leftClickPanner.disable();
      canvas.setCursorInternal(properties.getControlCursor());
    }
  };
  private EventListener cursorListener = new EventListener() {
    @Override
    public void handleEvent(Event event) {
      selectionId = null;
      resetCursor();
    }
  };

  @SuppressWarnings("unchecked")
public SvgTreeListening(
      final AnathemaCanvas canvas,
      IBoundsCalculator calculator,
      ISvgTreeViewProperties viewProperties) {
    this.canvas = canvas;
    this.boundsCalculator = calculator;
    this.properties = viewProperties;
    canvas.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
          boundsCalculator.reset();
        }
      }
    });
		MagnifyInteractor magnify = new MagnifyInteractor(boundsCalculator, canvas, this, properties.getZoomCursor());
    List<Interactor> interactors = canvas.getInteractors();
    interactors.add(magnify);
    interactors.add(new DoubleRightClickResetTransformInteractor(boundsCalculator));
    this.leftClickPanner = new LeftClickPanInteractor(boundsCalculator, canvas, properties);
    interactors.add(leftClickPanner);
    canvas.addMouseListener(leftClickPanner);
    canvas.addMouseListener(new RightClickPanAdapter(boundsCalculator));
    canvas.addMouseWheelListener(magnify);
    canvas.setCursorInternal(properties.getDefaultCursor());
  }

  public void addNodeSelectionListener(final CharmInteractionListener listener) {
    control.addListener(listener);
  }

  public void destructDocumentListening(final SVGDocument document) {
    if (document == null) {
      return;
    }
    document.getRootElement().removeEventListener(SVGConstants.SVG_MOUSEUP_EVENT_TYPE, cursorListener, false);
    for (SVGGElement groupElement : canvas.getNodeElements()) {
      groupElement.removeEventListener(SVGConstants.SVG_MOUSEUP_EVENT_TYPE, selectionInvokingListener, false);
      groupElement.removeEventListener(SVGConstants.SVG_MOUSEUP_EVENT_TYPE, nodeEditedListener, false);
      groupElement.removeEventListener(SVGConstants.SVG_MOUSEOVER_EVENT_TYPE, cursorTooltipInitListener, false);
      groupElement.removeEventListener(SVGConstants.SVG_MOUSEOUT_EVENT_TYPE, nodeExitListener, false);
    }
    for (final SVGGElement groupElement : canvas.getControlElements()) {
      groupElement.removeEventListener(SVGConstants.SVG_MOUSEMOVE_EVENT_TYPE, controlListener, false);
      groupElement.removeEventListener(SVGConstants.SVG_MOUSEOUT_EVENT_TYPE, nodeExitListener, false);
    }
  }

  private void fireNodeSelectionEvent(String nodeId) {
    control.announce().nodeSelected(nodeId);
  }
 
  private void fireNodeEditedEvent(String nodeId) {
    control.announce().nodeDetailsDemanded(nodeId);
   }
 
  public void initDocumentListening(final SVGDocument document) {
    if (document == null) {
      return;
    }
    document.getRootElement().addEventListener(SVGConstants.SVG_MOUSEUP_EVENT_TYPE, cursorListener, false);
    for (SVGGElement groupElement : canvas.getNodeElements()) {
      groupElement.addEventListener(SVGConstants.SVG_MOUSEUP_EVENT_TYPE, selectionInvokingListener, false);
      groupElement.addEventListener(SVGConstants.SVG_MOUSEUP_EVENT_TYPE, nodeEditedListener, false);
      groupElement.addEventListener(SVGConstants.SVG_MOUSEOVER_EVENT_TYPE, cursorTooltipInitListener, false);
      groupElement.addEventListener(SVGConstants.SVG_MOUSEOUT_EVENT_TYPE, nodeExitListener, false);
    }
    for (SVGGElement groupElement : canvas.getControlElements()) {
      groupElement.addEventListener(SVGConstants.SVG_MOUSEMOVE_EVENT_TYPE, controlListener, false);
      groupElement.addEventListener(SVGConstants.SVG_MOUSEOUT_EVENT_TYPE, nodeExitListener, false);
    }
  }

  private void setCanvasTooltip(final String node) {
    canvas.setToolTipText(properties.getToolTip(node));
  }

  private void setCursor(final String nodeId) {
    Cursor cursor = properties.getCursor(nodeId);
    canvas.setCursorInternal(cursor);
  }

  public void resetCursor() {
    canvas.setCursorInternal(properties.getDefaultCursor());
  }
}
