/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.Document;
/*     */ import org.jdesktop.swingx.JXSearchField;
/*     */ import org.jdesktop.swingx.JXSearchField.LayoutStyle;
/*     */ import org.jdesktop.swingx.prompt.BuddySupport;
/*     */ import org.jdesktop.swingx.search.NativeSearchFieldSupport;
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
/*     */ public class SearchFieldUI
/*     */   extends BuddyTextFieldUI
/*     */ {
/*     */   protected JXSearchField searchField;
/*     */   private Handler handler;
/*  48 */   public static final Insets NO_INSETS = new Insets(0, 0, 0, 0);
/*     */   
/*     */   public SearchFieldUI(TextUI delegate) {
/*  51 */     super(delegate);
/*     */   }
/*     */   
/*     */   private Handler getHandler() {
/*  55 */     if (this.handler == null) {
/*  56 */       this.handler = new Handler();
/*     */     }
/*  58 */     return this.handler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/*  69 */     this.searchField = ((JXSearchField)c);
/*     */     
/*  71 */     super.installUI(c);
/*     */     
/*  73 */     installDefaults();
/*  74 */     layoutButtons();
/*     */     
/*  76 */     configureListeners();
/*     */   }
/*     */   
/*     */   private void configureListeners() {
/*  80 */     if (isNativeSearchField()) {
/*  81 */       popupButton().removeActionListener(getHandler());
/*  82 */       this.searchField.removePropertyChangeListener(getHandler());
/*     */     } else {
/*  84 */       popupButton().addActionListener(getHandler());
/*  85 */       this.searchField.addPropertyChangeListener(getHandler());
/*     */     }
/*     */     
/*     */ 
/*  89 */     this.searchField.getDocument().addDocumentListener(getHandler());
/*     */   }
/*     */   
/*     */   private boolean isNativeSearchField() {
/*  93 */     return NativeSearchFieldSupport.isNativeSearchField(this.searchField);
/*     */   }
/*     */   
/*     */   protected BuddyLayoutAndBorder createBuddyLayoutAndBorder()
/*     */   {
/*  98 */     new BuddyLayoutAndBorder()
/*     */     {
/*     */ 
/*     */ 
/*     */       protected void replaceBorderIfNecessary()
/*     */       {
/*     */ 
/* 105 */         if (!SearchFieldUI.this.isNativeSearchField()) {
/* 106 */           super.replaceBorderIfNecessary();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Dimension preferredLayoutSize(Container parent)
/*     */       {
/* 116 */         if (SearchFieldUI.this.isNativeSearchField()) {
/* 117 */           return new Dimension();
/*     */         }
/* 119 */         return super.preferredLayoutSize(parent);
/*     */       }
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
/*     */       public Insets getBorderInsets(Component c)
/*     */       {
/* 133 */         Insets insets = super.getBorderInsets(c);
/* 134 */         if ((SearchFieldUI.this.searchField != null) && (!SearchFieldUI.this.isNativeSearchField())) {
/* 135 */           if (SearchFieldUI.this.isMacLayoutStyle()) {
/* 136 */             if (!SearchFieldUI.this.clearButton().isVisible()) {
/* 137 */               insets.right += SearchFieldUI.this.clearButton().getPreferredSize().width;
/*     */             }
/*     */           } else {
/* 140 */             JButton refButton = SearchFieldUI.this.popupButton();
/* 141 */             if ((SearchFieldUI.this.searchField.getFindPopupMenu() == null ^ SearchFieldUI.this.searchField.isUseSeperatePopupButton()))
/*     */             {
/* 143 */               refButton = SearchFieldUI.this.searchButton();
/*     */             }
/*     */             
/* 146 */             int clearWidth = SearchFieldUI.this.clearButton().getPreferredSize().width;
/* 147 */             int refWidth = refButton.getPreferredSize().width;
/* 148 */             int overSize = SearchFieldUI.this.clearButton().isVisible() ? refWidth - clearWidth : clearWidth - refWidth;
/*     */             
/* 150 */             if (overSize > 0) {
/* 151 */               insets.right += overSize;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 156 */         return insets;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private void layoutButtons() {
/* 162 */     BuddySupport.removeAll(this.searchField);
/*     */     
/* 164 */     if (isNativeSearchField()) {
/* 165 */       return;
/*     */     }
/*     */     
/* 168 */     if (isMacLayoutStyle()) {
/* 169 */       BuddySupport.addLeft(searchButton(), this.searchField);
/*     */     } else {
/* 171 */       BuddySupport.addRight(searchButton(), this.searchField);
/*     */     }
/*     */     
/* 174 */     BuddySupport.addRight(clearButton(), this.searchField);
/*     */     
/* 176 */     if (usingSeperatePopupButton()) {
/* 177 */       BuddySupport.addRight(BuddySupport.createGap(getPopupOffset()), this.searchField);
/*     */     }
/*     */     
/*     */ 
/* 181 */     if ((usingSeperatePopupButton()) || (!isMacLayoutStyle())) {
/* 182 */       BuddySupport.addRight(popupButton(), this.searchField);
/*     */     } else {
/* 184 */       BuddySupport.addLeft(popupButton(), this.searchField);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isMacLayoutStyle() {
/* 189 */     return this.searchField.getLayoutStyle() == JXSearchField.LayoutStyle.MAC;
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
/*     */   protected void installDefaults()
/*     */   {
/* 205 */     if (isNativeSearchField()) {
/* 206 */       return;
/*     */     }
/*     */     
/* 209 */     if (UIManager.getBoolean("SearchField.useSeperatePopupButton")) {
/* 210 */       this.searchField.customSetUIProperty("useSeperatePopupButton", Boolean.TRUE);
/*     */     }
/*     */     else {
/* 213 */       this.searchField.customSetUIProperty("useSeperatePopupButton", Boolean.FALSE);
/*     */     }
/*     */     
/*     */ 
/* 217 */     this.searchField.customSetUIProperty("layoutStyle", UIManager.get("SearchField.layoutStyle"));
/*     */     
/* 219 */     this.searchField.customSetUIProperty("promptFontStyle", UIManager.get("SearchField.promptFontStyle"));
/*     */     
/*     */ 
/* 222 */     if (shouldReplaceResource(this.searchField.getOuterMargin())) {
/* 223 */       this.searchField.setOuterMargin(UIManager.getInsets("SearchField.buttonMargin"));
/*     */     }
/*     */     
/*     */ 
/* 227 */     updateButtons();
/*     */     
/* 229 */     if (shouldReplaceResource(clearButton().getIcon())) {
/* 230 */       clearButton().setIcon(UIManager.getIcon("SearchField.clearIcon"));
/*     */     }
/* 232 */     if (shouldReplaceResource(clearButton().getPressedIcon())) {
/* 233 */       clearButton().setPressedIcon(UIManager.getIcon("SearchField.clearPressedIcon"));
/*     */     }
/*     */     
/* 236 */     if (shouldReplaceResource(clearButton().getRolloverIcon())) {
/* 237 */       clearButton().setRolloverIcon(UIManager.getIcon("SearchField.clearRolloverIcon"));
/*     */     }
/*     */     
/*     */ 
/* 241 */     searchButton().setIcon(getNewIcon(searchButton().getIcon(), "SearchField.icon"));
/*     */     
/*     */ 
/* 244 */     popupButton().setIcon(getNewIcon(popupButton().getIcon(), "SearchField.popupIcon"));
/*     */     
/* 246 */     popupButton().setRolloverIcon(getNewIcon(popupButton().getRolloverIcon(), "SearchField.popupRolloverIcon"));
/*     */     
/*     */ 
/* 249 */     popupButton().setPressedIcon(getNewIcon(popupButton().getPressedIcon(), "SearchField.popupPressedIcon"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 260 */     super.uninstallUI(c);
/*     */     
/* 262 */     this.searchField.removePropertyChangeListener(getHandler());
/* 263 */     this.searchField.getDocument().removeDocumentListener(getHandler());
/* 264 */     popupButton().removeActionListener(getHandler());
/*     */     
/* 266 */     this.searchField.setLayout(null);
/* 267 */     this.searchField.removeAll();
/* 268 */     this.searchField = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldReplaceResource(Object o)
/*     */   {
/* 280 */     return (o == null) || ((o instanceof UIResource));
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
/*     */   protected Icon getNewIcon(Icon icon, String resKey)
/*     */   {
/* 295 */     Icon uiIcon = UIManager.getIcon(resKey);
/* 296 */     if (shouldReplaceResource(icon)) {
/* 297 */       return uiIcon;
/*     */     }
/* 299 */     return icon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JButton clearButton()
/*     */   {
/* 309 */     return this.searchField.getCancelButton();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JButton searchButton()
/*     */   {
/* 319 */     return this.searchField.getFindButton();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JButton popupButton()
/*     */   {
/* 329 */     return this.searchField.getPopupButton();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean usingSeperatePopupButton()
/*     */   {
/* 340 */     return (this.searchField.isUseSeperatePopupButton()) && (this.searchField.getFindPopupMenu() != null);
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
/*     */   protected int getPopupOffset()
/*     */   {
/* 354 */     if (usingSeperatePopupButton()) {
/* 355 */       return UIManager.getInt("SearchField.popupOffset");
/*     */     }
/* 357 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateButtons()
/*     */   {
/* 368 */     clearButton().setVisible(((!this.searchField.isRegularSearchMode()) || (this.searchField.isMacLayoutStyle())) && (hasText()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 373 */     boolean clearNotHere = (this.searchField.isMacLayoutStyle()) || (!clearButton().isVisible());
/*     */     
/*     */ 
/* 376 */     searchButton().setVisible(((this.searchField.getFindPopupMenu() == null) || (usingSeperatePopupButton())) && (clearNotHere));
/*     */     
/*     */ 
/*     */ 
/* 380 */     popupButton().setVisible((this.searchField.getFindPopupMenu() != null) && ((clearNotHere) || (usingSeperatePopupButton())));
/*     */     
/*     */ 
/*     */ 
/* 384 */     if (this.searchField.isRegularSearchMode()) {
/* 385 */       searchButton().setRolloverIcon(getNewIcon(searchButton().getRolloverIcon(), "SearchField.rolloverIcon"));
/*     */       
/*     */ 
/* 388 */       searchButton().setPressedIcon(getNewIcon(searchButton().getPressedIcon(), "SearchField.pressedIcon"));
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 393 */       if (shouldReplaceResource(searchButton().getRolloverIcon())) {
/* 394 */         searchButton().setRolloverIcon(null);
/*     */       }
/* 396 */       if (shouldReplaceResource(searchButton().getPressedIcon())) {
/* 397 */         searchButton().setPressedIcon(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasText() {
/* 403 */     return (this.searchField.getText() != null) && (this.searchField.getText().length() > 0);
/*     */   }
/*     */   
/*     */   class Handler implements PropertyChangeListener, ActionListener, DocumentListener {
/*     */     Handler() {}
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent evt) {
/* 410 */       String prop = evt.getPropertyName();
/* 411 */       Object src = evt.getSource();
/*     */       
/* 413 */       if (src.equals(SearchFieldUI.this.searchField)) {
/* 414 */         if (("findPopupMenu".equals(prop)) || ("searchMode".equals(prop)) || ("useSeperatePopupButton".equals(prop)) || ("searchMode".equals(prop)) || ("layoutStyle".equals(prop)))
/*     */         {
/*     */ 
/*     */ 
/* 418 */           SearchFieldUI.this.layoutButtons();
/* 419 */           SearchFieldUI.this.updateButtons();
/* 420 */         } else if ("document".equals(prop)) {
/* 421 */           Document doc = (Document)evt.getOldValue();
/* 422 */           if (doc != null) {
/* 423 */             doc.removeDocumentListener(this);
/*     */           }
/* 425 */           doc = (Document)evt.getNewValue();
/* 426 */           if (doc != null) {
/* 427 */             doc.addDocumentListener(this);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 437 */       if (SearchFieldUI.this.searchField.getFindPopupMenu() != null) {
/* 438 */         Component src = "searchField".equals(UIManager.getString("SearchField.popupSource")) ? SearchFieldUI.this.searchField : (Component)e.getSource();
/*     */         
/*     */ 
/*     */ 
/* 442 */         Rectangle r = SwingUtilities.getLocalBounds(src);
/* 443 */         int popupWidth = SearchFieldUI.this.searchField.getFindPopupMenu().getPreferredSize().width;
/*     */         
/* 445 */         int x = (SearchFieldUI.this.searchField.isVistaLayoutStyle()) || (SearchFieldUI.this.usingSeperatePopupButton()) ? r.x + r.width - popupWidth : r.x;
/*     */         
/*     */ 
/* 448 */         SearchFieldUI.this.searchField.getFindPopupMenu().show(src, x, r.y + r.height);
/*     */       }
/*     */     }
/*     */     
/*     */     public void changedUpdate(DocumentEvent e) {
/* 453 */       update();
/*     */     }
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 457 */       update();
/*     */     }
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 461 */       update();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void update()
/*     */     {
/* 471 */       if (SearchFieldUI.this.searchField.isInstantSearchMode()) {
/* 472 */         SearchFieldUI.this.searchField.getInstantSearchTimer().stop();
/*     */         
/* 474 */         if (SearchFieldUI.this.searchField.getInstantSearchDelay() > 0) {
/* 475 */           SearchFieldUI.this.searchField.getInstantSearchTimer().setInitialDelay(SearchFieldUI.this.searchField.getInstantSearchDelay());
/*     */           
/* 477 */           SearchFieldUI.this.searchField.getInstantSearchTimer().start();
/*     */         } else {
/* 479 */           SearchFieldUI.this.searchField.postActionEvent();
/*     */         }
/*     */       }
/*     */       
/* 483 */       SearchFieldUI.this.updateButtons();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\SearchFieldUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */