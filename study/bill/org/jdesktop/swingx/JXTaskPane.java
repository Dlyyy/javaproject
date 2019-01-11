/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.LayoutManager;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.TaskPaneAddon;
/*     */ import org.jdesktop.swingx.plaf.TaskPaneUI;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXTaskPane
/*     */   extends JPanel
/*     */   implements JXCollapsiblePane.CollapsiblePaneContainer
/*     */ {
/*     */   public static final String uiClassID = "swingx/TaskPaneUI";
/*     */   public static final String SCROLL_ON_EXPAND_CHANGED_KEY = "scrollOnExpand";
/*     */   public static final String TITLE_CHANGED_KEY = "title";
/*     */   public static final String ICON_CHANGED_KEY = "icon";
/*     */   public static final String SPECIAL_CHANGED_KEY = "special";
/*     */   public static final String ANIMATED_CHANGED_KEY = "animated";
/*     */   private String title;
/*     */   private Icon icon;
/*     */   private boolean special;
/*     */   private boolean collapsed;
/*     */   private boolean scrollOnExpand;
/*     */   private int mnemonic;
/*     */   
/*     */   static
/*     */   {
/* 150 */     LookAndFeelAddons.contribute(new TaskPaneAddon());
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
/* 185 */   private int mnemonicIndex = -1;
/*     */   
/*     */ 
/*     */   private JXCollapsiblePane collapsePane;
/*     */   
/*     */ 
/*     */   public JXTaskPane()
/*     */   {
/* 193 */     this((String)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTaskPane(String title)
/*     */   {
/* 203 */     this(title, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTaskPane(Icon icon)
/*     */   {
/* 213 */     this(null, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTaskPane(String title, Icon icon)
/*     */   {
/* 225 */     this.collapsePane = new JXCollapsiblePane();
/* 226 */     super.setLayout(new BorderLayout(0, 0));
/* 227 */     super.addImpl(this.collapsePane, "Center", -1);
/*     */     
/* 229 */     setTitle(title);
/* 230 */     setIcon(icon);
/*     */     
/* 232 */     updateUI();
/* 233 */     setFocusable(true);
/*     */     
/*     */ 
/* 236 */     setAnimated(!Boolean.FALSE.equals(UIManager.get("TaskPane.animate")));
/*     */     
/*     */ 
/* 239 */     this.collapsePane.addPropertyChangeListener("animationState", new PropertyChangeListener()
/*     */     {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 242 */         JXTaskPane.this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Container getContentPane()
/*     */   {
/* 253 */     return this.collapsePane.getContentPane();
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
/*     */   public void updateUI()
/*     */   {
/* 266 */     if (this.collapsePane == null) {
/* 267 */       return;
/*     */     }
/* 269 */     setUI((TaskPaneUI)LookAndFeelAddons.getUI(this, TaskPaneUI.class));
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
/*     */   public void setUI(TaskPaneUI ui)
/*     */   {
/* 282 */     super.setUI(ui);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 294 */     return "swingx/TaskPaneUI";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 303 */     return this.title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 315 */     String old = this.title;
/* 316 */     this.title = title;
/* 317 */     firePropertyChange("title", old, title);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getIcon()
/*     */   {
/* 326 */     return this.icon;
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
/*     */   public void setIcon(Icon icon)
/*     */   {
/* 340 */     Icon old = this.icon;
/* 341 */     this.icon = icon;
/* 342 */     firePropertyChange("icon", old, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSpecial()
/*     */   {
/* 352 */     return this.special;
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
/*     */   public void setSpecial(boolean special)
/*     */   {
/* 371 */     boolean oldValue = isSpecial();
/* 372 */     this.special = special;
/* 373 */     firePropertyChange("special", oldValue, isSpecial());
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
/*     */   public void setScrollOnExpand(boolean scrollOnExpand)
/*     */   {
/* 389 */     boolean oldValue = isScrollOnExpand();
/* 390 */     this.scrollOnExpand = scrollOnExpand;
/* 391 */     firePropertyChange("scrollOnExpand", oldValue, isScrollOnExpand());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isScrollOnExpand()
/*     */   {
/* 403 */     return this.scrollOnExpand;
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
/*     */   public void setCollapsed(boolean collapsed)
/*     */   {
/* 416 */     boolean oldValue = isCollapsed();
/* 417 */     this.collapsed = collapsed;
/* 418 */     this.collapsePane.setCollapsed(collapsed);
/* 419 */     firePropertyChange("collapsed", oldValue, isCollapsed());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCollapsed()
/*     */   {
/* 429 */     return this.collapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAnimated(boolean animated)
/*     */   {
/* 441 */     boolean oldValue = isAnimated();
/* 442 */     this.collapsePane.setAnimated(animated);
/* 443 */     firePropertyChange("animated", oldValue, isAnimated());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAnimated()
/*     */   {
/* 454 */     return this.collapsePane.isAnimated();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMnemonic()
/*     */   {
/* 463 */     return this.mnemonic;
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
/*     */   public void setMnemonic(int mnemonic)
/*     */   {
/* 490 */     int oldValue = getMnemonic();
/* 491 */     this.mnemonic = mnemonic;
/*     */     
/* 493 */     firePropertyChange("mnemonic", oldValue, getMnemonic());
/*     */     
/* 495 */     updateDisplayedMnemonicIndex(getTitle(), mnemonic);
/* 496 */     revalidate();
/* 497 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateDisplayedMnemonicIndex(String text, int mnemonic)
/*     */   {
/* 507 */     if ((text == null) || (mnemonic == 0)) {
/* 508 */       this.mnemonicIndex = -1;
/*     */       
/* 510 */       return;
/*     */     }
/*     */     
/* 513 */     char uc = Character.toUpperCase((char)mnemonic);
/* 514 */     char lc = Character.toLowerCase((char)mnemonic);
/*     */     
/* 516 */     int uci = text.indexOf(uc);
/* 517 */     int lci = text.indexOf(lc);
/*     */     
/* 519 */     if (uci == -1) {
/* 520 */       this.mnemonicIndex = lci;
/* 521 */     } else if (lci == -1) {
/* 522 */       this.mnemonicIndex = uci;
/*     */     } else {
/* 524 */       this.mnemonicIndex = (lci < uci ? lci : uci);
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
/*     */   public int getDisplayedMnemonicIndex()
/*     */   {
/* 537 */     return this.mnemonicIndex;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDisplayedMnemonicIndex(int index)
/*     */     throws IllegalArgumentException
/*     */   {
/* 570 */     int oldValue = this.mnemonicIndex;
/* 571 */     if (index == -1) {
/* 572 */       this.mnemonicIndex = -1;
/*     */     } else {
/* 574 */       String text = getTitle();
/* 575 */       int textLength = text == null ? 0 : text.length();
/* 576 */       if ((index < -1) || (index >= textLength)) {
/* 577 */         throw new IllegalArgumentException("index == " + index);
/*     */       }
/*     */     }
/* 580 */     this.mnemonicIndex = index;
/* 581 */     firePropertyChange("displayedMnemonicIndex", oldValue, index);
/* 582 */     if (index != oldValue) {
/* 583 */       revalidate();
/* 584 */       repaint();
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
/*     */   public Component add(Action action)
/*     */   {
/* 597 */     Component c = ((TaskPaneUI)this.ui).createAction(action);
/* 598 */     add(c);
/* 599 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Container getValidatingContainer()
/*     */   {
/* 606 */     return getParent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addImpl(Component comp, Object constraints, int index)
/*     */   {
/* 614 */     getContentPane().add(comp, constraints, index);
/*     */     
/* 616 */     revalidate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayout(LayoutManager mgr)
/*     */   {
/* 624 */     if (this.collapsePane != null) {
/* 625 */       getContentPane().setLayout(mgr);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(Component comp)
/*     */   {
/* 634 */     getContentPane().remove(comp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(int index)
/*     */   {
/* 642 */     getContentPane().remove(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 650 */     getContentPane().removeAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 658 */     return super.paramString() + ",title=" + getTitle() + ",icon=" + getIcon() + ",collapsed=" + String.valueOf(isCollapsed()) + ",special=" + String.valueOf(isSpecial()) + ",scrollOnExpand=" + String.valueOf(isScrollOnExpand()) + ",ui=" + getUI();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTaskPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */