package net.sf.anathema.lib.gui.dialog.wizard;

import net.sf.anathema.lib.gui.dialog.core.IGenericDialogConfiguration;

/**
 * Interface for a wizard. A wizard maintains a list of wizard pages, stacked on top of each other
 * in card layout fashion.
 * 
 * The class Wizard provides an abstract implementation of this interface. However, clients are
 * also free to implement this interface if Wizard does not suit their needs. 
 * 
 * @see AbstractWizardConfiguration */
public interface IWizardConfiguration extends IGenericDialogConfiguration {

  /**
   * Returns the first page to be shown in this wizard.
   * 
   * @return the first wizard page
   */
  IWizardPage getStartingPage();

  /**
   * Returns the container of this wizard.
   * 
   * @return the wizard container, or <code>null</code> if this wizard has yet to be added to a
   *         container
   */
  IWizardContainer getContainer();

  /**
   * Sets or clears the container of this wizard.
   * 
   * @param wizardContainer the wizard container, or <code>null</code>
   */
  void setContainer(IWizardContainer wizardContainer);

  /**
   * Returns whether this wizard could be finished without further user interaction. The result of
   * this method is typically used by the wizard container to enable or disable the Finish button.
   * 
   * @return <code>true</code> if the wizard could be finished, and <code>false</code> otherwise
   */
  boolean canFinish();

  /**
   * Returns whether this wizard could be canceled without further user interaction. The result of
   * this method is typically used by the wizard container to enable or disable the Cancel button.
   * 
   * @return <code>true</code> if the wizard could be canceled, and <code>false</code> otherwise
   */
  boolean canCancel();

  /** Adds any last-minute pages to this wizard.
   * This method is called just before the wizard becomes visible, to give the wizard the
   * opportunity to add any lazily created pages.*/
  void addPages();

  /** Returns the successor of the given page.
   * This method is typically called by a wizard page
   * @param page the page
   * @return the next page, or null if none */
  IWizardPage getNextPage(IWizardPage page);

  /** Returns the predecessor of the given page.
   * This method is typically called by a wizard page
   * @param page the page
   * @return the previous page, or null if none */

  IWizardPage getPreviousPage(IWizardPage page);
}