package net.sf.anathema.framework.view.item;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.text.StyleContext;

import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabDropDownListVisiblePolicy;
import net.infonode.tabbedpanel.TabStateChangedEvent;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.theme.ShapedGradientTheme;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.util.Direction;
import net.sf.anathema.framework.view.IItemView;
import net.sf.anathema.framework.view.IViewSelectionListener;
import net.sf.anathema.lib.control.GenericControl;
import net.sf.anathema.lib.control.IClosure;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;

public class ItemViewManagement implements IComponentItemViewManagement {

  private final TabbedPanel tabbedPane = new TabbedPanel();
  private final Map<Component, IItemView> itemViewsByComponent = new HashMap<Component, IItemView>();
  private final Map<IItemView, IObjectValueChangedListener<String>> nameListenersByView = new HashMap<IItemView, IObjectValueChangedListener<String>>();
  private final GenericControl<IViewSelectionListener> control = new GenericControl<IViewSelectionListener>();
  private final TitledTabProperties titledTabProperties = new TitledTabProperties();
  private final ShapedGradientTheme theme = new ShapedGradientTheme(0f, 0.5f, new FixedColorProvider(new Color(
      150,
      150,
      150)), null);

  public ItemViewManagement() {
    initPaneProperties();
    titledTabProperties.addSuperObject(theme.getTitledTabProperties());
    titledTabProperties.getNormalProperties().setTitleComponentVisible(false);
    titledTabProperties.getHighlightedProperties().setTitleComponentVisible(true);
    titledTabProperties.setHoverListener(new HoverListener() {
      public void mouseEntered(HoverEvent event) {
        TitledTab tab = (TitledTab) event.getSource();
        tab.getProperties().getNormalProperties().setTitleComponentVisible(true);
      }

      public void mouseExited(HoverEvent event) {
        TitledTab tab = (TitledTab) event.getSource();
        tab.getProperties().getNormalProperties().setTitleComponentVisible(false);
      }
    });
  }

  private void initPaneProperties() {
    TabbedPanelProperties paneProperties = tabbedPane.getProperties();
    paneProperties.removeSuperObject(tabbedPane.getProperties());
    paneProperties.addSuperObject(theme.getTabbedPanelProperties());
    paneProperties.setTabAreaOrientation(Direction.DOWN);
    paneProperties.setTabReorderEnabled(true);
    paneProperties.setTabDeselectable(false);
    paneProperties.setEnsureSelectedTabVisible(true);
    paneProperties.setHighlightPressedTab(true);
    paneProperties.setTabDropDownListVisiblePolicy(TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE);
  }

  private static class FontHolder {
    public static final Font TAB_CAPTION_FONT = createCompositeFont(Font.PLAIN, 11);

    private static Font createCompositeFont(int style, int size) {
      return StyleContext.getDefaultStyleContext().getFont(Font.SANS_SERIF, style, size);
    }
  }

  public void addItemView(final IItemView view, Action closeAction) {
    JComponent component = view.getComponent();
    itemViewsByComponent.put(component, view);
    TitledTab tab = new TitledTab(view.getName(), view.getIcon(), component, createButton(closeAction));
    tab.getProperties().addSuperObject(titledTabProperties);
    tab.getProperties().getNormalProperties().getComponentProperties().setFont(FontHolder.TAB_CAPTION_FONT);
    tab.getProperties().getDisabledProperties().getComponentProperties().setFont(FontHolder.TAB_CAPTION_FONT);
    tab.getProperties().getHighlightedProperties().getComponentProperties().setFont(FontHolder.TAB_CAPTION_FONT);
    tabbedPane.addTab(tab);
    tab.addTabListener(new TabAdapter() {
      @Override
      public void tabSelected(TabStateChangedEvent event) {
        fireItemViewChanged(getSelectedItemView());
      }
    });
    initNameListening(view);
    tabbedPane.setSelectedTab(tab);
  }

  private AbstractButton createButton(Action closeAction) {
    JButton button = new JButton(closeAction);
    button.setMargin(new Insets(1, 1, 1, 1));
    button.setPreferredSize(new Dimension(24, 24));
    button.setBorderPainted(false);
    return button;
  }

  private void disposeNameListening(final IItemView view) {
    IObjectValueChangedListener<String> listener = nameListenersByView.get(view);
    nameListenersByView.remove(view);
    view.removeNameChangedListener(listener);
  }

  private void initNameListening(final IItemView view) {
    IObjectValueChangedListener<String> listener = new IObjectValueChangedListener<String>() {
      public void valueChanged(String newValue) {
        setItemViewName(view, newValue);
      }
    };
    nameListenersByView.put(view, listener);
    view.addNameChangedListener(listener);
  }

  public JComponent getComponent() {
    return tabbedPane;
  }

  private IItemView getSelectedItemView() {
    return itemViewsByComponent.get(tabbedPane.getSelectedTab().getContentComponent());
  }

  public void addViewSelectionListener(IViewSelectionListener listener) {
    control.addListener(listener);
  }

  private void fireItemViewChanged(final IItemView view) {
    control.forAllDo(new IClosure<IViewSelectionListener>() {
      public void execute(IViewSelectionListener input) {
        input.viewSelectionChangedTo(view);
      }
    });
  }

  public void setSelectedItemView(IItemView view) {
    tabbedPane.setSelectedTab(getTab(view));
  }

  private TitledTab getTab(IItemView view) {
    JComponent viewComponent = view.getComponent();
    for (int index = 0; index < tabbedPane.getTabCount(); index++) {
      Tab tab = tabbedPane.getTabAt(index);
      if (tab.getContentComponent() == viewComponent) {
        return (TitledTab) tab;
      }
    }
    return null;
  }

  public void removeItemView(IItemView view) {
    Component component = view.getComponent();
    itemViewsByComponent.remove(component);
    tabbedPane.removeTab(getTab(view));
    disposeNameListening(view);
    view.dispose();
  }

  private void setItemViewName(IItemView view, String name) {
    getTab(view).setText(name);
  }

  public void setTabAreaComponents(JComponent... components) {
    tabbedPane.setTabAreaComponents(components);
  }
}
