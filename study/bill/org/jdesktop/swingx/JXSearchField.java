/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.SearchFieldAddon;
/*     */ import org.jdesktop.swingx.plaf.TextUIWrapper;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.prompt.BuddyButton;
/*     */ import org.jdesktop.swingx.search.NativeSearchFieldSupport;
/*     */ import org.jdesktop.swingx.search.RecentSearches;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXSearchField
/*     */   extends JXTextField
/*     */ {
/*     */   private static final int DEFAULT_INSTANT_SEARCH_DELAY = 180;
/*  64 */   private static final KeyStroke CANCEL_KEY = KeyStroke.getKeyStroke(27, 0);
/*     */   
/*     */ 
/*     */   private JButton findButton;
/*     */   
/*     */ 
/*     */   private JButton cancelButton;
/*     */   
/*     */ 
/*     */   private JButton popupButton;
/*     */   
/*     */ 
/*     */   private LayoutStyle layoutStyle;
/*     */   
/*     */ 
/*     */   public static enum LayoutStyle
/*     */   {
/*  81 */     VISTA, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */     MAC;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private LayoutStyle() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum SearchMode
/*     */   {
/* 116 */     REGULAR, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */     INSTANT;
/*     */     
/*     */     private SearchMode() {}
/*     */   }
/*     */   
/* 131 */   static { LookAndFeelAddons.contribute(new SearchFieldAddon()); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   private SearchMode searchMode = SearchMode.INSTANT;
/*     */   
/*     */   private boolean useSeperatePopupButton;
/*     */   
/*     */   private boolean useSeperatePopupButtonSet;
/*     */   
/*     */   private boolean layoutStyleSet;
/*     */   
/* 150 */   private int instantSearchDelay = 180;
/*     */   
/*     */ 
/*     */   private boolean promptFontStyleSet;
/*     */   
/*     */   private Timer instantSearchTimer;
/*     */   
/*     */   private String recentSearchesSaveKey;
/*     */   
/*     */   private RecentSearches recentSearches;
/*     */   
/*     */ 
/*     */   public JXSearchField()
/*     */   {
/* 164 */     this(UIManagerExt.getString("SearchField.prompt"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXSearchField(String prompt)
/*     */   {
/* 174 */     super(prompt);
/*     */     
/* 176 */     setUseNativeSearchFieldIfPossible(true);
/*     */     
/* 178 */     setCancelAction(new ClearAction());
/* 179 */     setFindAction(new FindAction());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 185 */     addKeyListener(new KeyAdapter() {
/*     */       public void keyPressed(KeyEvent e) {
/* 187 */         if (JXSearchField.CANCEL_KEY.equals(KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()))) {
/* 188 */           JXSearchField.this.getCancelAction().actionPerformed(new ActionEvent(JXSearchField.this, e.getID(), KeyEvent.getKeyText(e.getKeyCode())));
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/* 194 */     });
/* 195 */     addPropertyChangeListener("JTextField.Search.FindPopup", new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 197 */         JPopupMenu oldPopup = (JPopupMenu)evt.getOldValue();
/* 198 */         JXSearchField.this.firePropertyChange("findPopupMenu", oldPopup, evt.getNewValue());
/*     */       }
/* 200 */     });
/* 201 */     addPropertyChangeListener("JTextField.Search.CancelAction", new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 203 */         ActionListener oldAction = (ActionListener)evt.getOldValue();
/* 204 */         JXSearchField.this.firePropertyChange("cancelAction", oldAction, evt.getNewValue());
/*     */       }
/* 206 */     });
/* 207 */     addPropertyChangeListener("JTextField.Search.FindAction", new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 209 */         ActionListener oldAction = (ActionListener)evt.getOldValue();
/* 210 */         JXSearchField.this.firePropertyChange("findAction", oldAction, evt.getNewValue());
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SearchMode getSearchMode()
/*     */   {
/* 221 */     return this.searchMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInstantSearchMode()
/*     */   {
/* 232 */     return SearchMode.INSTANT.equals(getSearchMode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegularSearchMode()
/*     */   {
/* 243 */     return SearchMode.REGULAR.equals(getSearchMode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSearchMode(SearchMode searchMode)
/*     */   {
/* 254 */     firePropertyChange("searchMode", this.searchMode, this.searchMode = searchMode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInstantSearchDelay()
/*     */   {
/* 265 */     return this.instantSearchDelay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInstantSearchDelay(int instantSearchDelay)
/*     */   {
/* 287 */     firePropertyChange("instantSearchDelay", this.instantSearchDelay, this.instantSearchDelay = instantSearchDelay);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LayoutStyle getLayoutStyle()
/*     */   {
/* 296 */     return this.layoutStyle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVistaLayoutStyle()
/*     */   {
/* 306 */     return LayoutStyle.VISTA.equals(getLayoutStyle());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMacLayoutStyle()
/*     */   {
/* 316 */     return LayoutStyle.MAC.equals(getLayoutStyle());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayoutStyle(LayoutStyle layoutStyle)
/*     */   {
/* 327 */     this.layoutStyleSet = true;
/* 328 */     firePropertyChange("layoutStyle", this.layoutStyle, this.layoutStyle = layoutStyle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMargin(Insets m)
/*     */   {
/* 337 */     super.setMargin(m);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ActionListener getCancelAction()
/*     */   {
/* 347 */     ActionListener a = NativeSearchFieldSupport.getCancelAction(this);
/* 348 */     if (a == null) {
/* 349 */       a = new ClearAction();
/*     */     }
/* 351 */     return a;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setCancelAction(ActionListener cancelAction)
/*     */   {
/* 361 */     NativeSearchFieldSupport.setCancelAction(this, cancelAction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JButton getCancelButton()
/*     */   {
/* 375 */     if (this.cancelButton == null) {
/* 376 */       this.cancelButton = createCancelButton();
/* 377 */       this.cancelButton.addActionListener(new ActionListener() {
/*     */         public void actionPerformed(ActionEvent e) {
/* 379 */           JXSearchField.this.getCancelAction().actionPerformed(e);
/*     */         }
/*     */       });
/*     */     }
/* 383 */     return this.cancelButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createCancelButton()
/*     */   {
/* 395 */     BuddyButton btn = new BuddyButton();
/*     */     
/* 397 */     return btn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ActionListener getFindAction()
/*     */   {
/* 408 */     ActionListener a = NativeSearchFieldSupport.getFindAction(this);
/* 409 */     if (a == null) {
/* 410 */       a = new FindAction();
/*     */     }
/* 412 */     return a;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setFindAction(ActionListener findAction)
/*     */   {
/* 422 */     NativeSearchFieldSupport.setFindAction(this, findAction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JButton getFindButton()
/*     */   {
/* 435 */     if (this.findButton == null) {
/* 436 */       this.findButton = createFindButton();
/* 437 */       this.findButton.addActionListener(new ActionListener() {
/*     */         public void actionPerformed(ActionEvent e) {
/* 439 */           JXSearchField.this.getFindAction().actionPerformed(e);
/*     */         }
/*     */       });
/*     */     }
/* 443 */     return this.findButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createFindButton()
/*     */   {
/* 456 */     BuddyButton btn = new BuddyButton();
/*     */     
/* 458 */     return btn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JButton getPopupButton()
/*     */   {
/* 472 */     if (this.popupButton == null) {
/* 473 */       this.popupButton = createPopupButton();
/*     */     }
/* 475 */     return this.popupButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createPopupButton()
/*     */   {
/* 486 */     return new BuddyButton();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseSeperatePopupButton()
/*     */   {
/* 497 */     return this.useSeperatePopupButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseSeperatePopupButton(boolean useSeperatePopupButton)
/*     */   {
/* 507 */     this.useSeperatePopupButtonSet = true;
/* 508 */     firePropertyChange("useSeperatePopupButton", this.useSeperatePopupButton, this.useSeperatePopupButton = useSeperatePopupButton);
/*     */   }
/*     */   
/*     */   public boolean isUseNativeSearchFieldIfPossible()
/*     */   {
/* 513 */     return NativeSearchFieldSupport.isSearchField(this);
/*     */   }
/*     */   
/*     */   public void setUseNativeSearchFieldIfPossible(boolean useNativeSearchFieldIfPossible) {
/* 517 */     TextUIWrapper.getDefaultWrapper().uninstall(this);
/* 518 */     NativeSearchFieldSupport.setSearchField(this, useNativeSearchFieldIfPossible);
/* 519 */     TextUIWrapper.getDefaultWrapper().install(this, true);
/* 520 */     updateUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEditable(boolean b)
/*     */   {
/* 531 */     super.setEditable(b);
/* 532 */     updateButtonState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/* 543 */     super.setEnabled(enabled);
/* 544 */     updateButtonState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateButtonState()
/*     */   {
/* 553 */     getCancelButton().setEnabled(isEditable() & isEnabled());
/* 554 */     getFindButton().setEnabled(isEnabled());
/* 555 */     getPopupButton().setEnabled(isEnabled());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFindPopupMenu(JPopupMenu findPopupMenu)
/*     */   {
/* 582 */     if (isManagingRecentSearches()) {
/* 583 */       return;
/*     */     }
/*     */     
/* 586 */     NativeSearchFieldSupport.setFindPopupMenu(this, findPopupMenu);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JPopupMenu getFindPopupMenu()
/*     */   {
/* 596 */     return NativeSearchFieldSupport.getFindPopupMenu(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isManagingRecentSearches()
/*     */   {
/* 605 */     return this.recentSearches != null;
/*     */   }
/*     */   
/*     */   private boolean isValidRecentSearchesKey(String key) {
/* 609 */     return (key != null) && (key.length() > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecentSearchesSaveKey()
/*     */   {
/* 619 */     return this.recentSearchesSaveKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRecentSearchesSaveKey(String recentSearchesSaveKey)
/*     */   {
/* 635 */     String oldName = getRecentSearchesSaveKey();
/* 636 */     this.recentSearchesSaveKey = recentSearchesSaveKey;
/*     */     
/* 638 */     if (this.recentSearches != null)
/*     */     {
/*     */ 
/* 641 */       RecentSearches rs = this.recentSearches;
/* 642 */       this.recentSearches = null;
/* 643 */       rs.uninstall(this);
/*     */     }
/*     */     
/* 646 */     if (isValidRecentSearchesKey(recentSearchesSaveKey)) {
/* 647 */       this.recentSearches = new RecentSearches(recentSearchesSaveKey);
/* 648 */       this.recentSearches.install(this);
/*     */     }
/*     */     
/* 651 */     firePropertyChange("recentSearchesSaveKey", oldName, this.recentSearchesSaveKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RecentSearches getRecentSearches()
/*     */   {
/* 660 */     return this.recentSearches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Timer getInstantSearchTimer()
/*     */   {
/* 672 */     if (this.instantSearchTimer == null) {
/* 673 */       this.instantSearchTimer = new Timer(0, new ActionListener() {
/*     */         public void actionPerformed(ActionEvent e) {
/* 675 */           JXSearchField.this.postActionEvent();
/*     */         }
/* 677 */       });
/* 678 */       this.instantSearchTimer.setRepeats(false);
/*     */     }
/* 680 */     return this.instantSearchTimer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasFocus()
/*     */   {
/* 694 */     if ((getFindPopupMenu() != null) && (getFindPopupMenu().isVisible())) {
/* 695 */       return true;
/*     */     }
/* 697 */     return super.hasFocus();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 704 */     super.updateUI();
/* 705 */     if (getFindPopupMenu() != null) {
/* 706 */       SwingUtilities.updateComponentTreeUI(getFindPopupMenu());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPromptFontStyle(Integer fontStyle)
/*     */   {
/* 715 */     super.setPromptFontStyle(fontStyle);
/* 716 */     this.promptFontStyleSet = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void customSetUIProperty(String propertyName, Object value)
/*     */   {
/* 729 */     customSetUIProperty(propertyName, value, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void customSetUIProperty(String propertyName, Object value, boolean override)
/*     */   {
/* 744 */     if (propertyName == "useSeperatePopupButton") {
/* 745 */       if ((!this.useSeperatePopupButtonSet) || (override)) {
/* 746 */         setUseSeperatePopupButton(((Boolean)value).booleanValue());
/* 747 */         this.useSeperatePopupButtonSet = false;
/*     */       }
/* 749 */     } else if (propertyName == "layoutStyle") {
/* 750 */       if ((!this.layoutStyleSet) || (override)) {
/* 751 */         setLayoutStyle(LayoutStyle.valueOf(value.toString()));
/* 752 */         this.layoutStyleSet = false;
/*     */       }
/* 754 */     } else if (propertyName == "promptFontStyle") {
/* 755 */       if ((!this.promptFontStyleSet) || (override)) {
/* 756 */         setPromptFontStyle((Integer)value);
/* 757 */         this.promptFontStyleSet = false;
/*     */       }
/*     */     } else {
/* 760 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void postActionEvent()
/*     */   {
/* 776 */     getInstantSearchTimer().stop();
/* 777 */     super.postActionEvent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   class ClearAction
/*     */     extends AbstractAction
/*     */   {
/*     */     public ClearAction()
/*     */     {
/* 787 */       putValue("ShortDescription", "Clear Search Text");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 794 */       clear();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void clear()
/*     */     {
/* 802 */       JXSearchField.this.setText(null);
/* 803 */       JXSearchField.this.requestFocusInWindow();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class FindAction
/*     */     extends AbstractAction
/*     */   {
/*     */     public FindAction() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 822 */       if ((JXSearchField.this.isFocusOwner()) && (JXSearchField.this.isRegularSearchMode())) {
/* 823 */         JXSearchField.this.postActionEvent();
/*     */       }
/* 825 */       JXSearchField.this.requestFocusInWindow();
/* 826 */       JXSearchField.this.selectAll();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXSearchField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */