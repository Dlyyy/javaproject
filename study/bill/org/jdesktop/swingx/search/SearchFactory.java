/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.JXDialog;
/*     */ import org.jdesktop.swingx.JXFindBar;
/*     */ import org.jdesktop.swingx.JXFindPanel;
/*     */ import org.jdesktop.swingx.JXFrame;
/*     */ import org.jdesktop.swingx.JXRootPane;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
/*     */ import org.jdesktop.swingx.util.Utilities;
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
/*     */ public class SearchFactory
/*     */   implements UIDependent
/*     */ {
/*     */   private static SearchFactory searchFactory;
/*     */   protected JXFindPanel findPanel;
/*     */   protected JXFindBar findBar;
/*     */   protected JComponent lastFindBarTarget;
/*     */   private boolean useFindBar;
/*     */   private Point lastFindDialogLocation;
/*     */   private FindRemover findRemover;
/*     */   
/*     */   private static class LaFListener
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private final WeakReference<SearchFactory> ref;
/*     */     
/*     */     public LaFListener(SearchFactory sf)
/*     */     {
/*  87 */       this.ref = new WeakReference(sf);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/*  95 */       SearchFactory sf = (SearchFactory)this.ref.get();
/*     */       
/*  97 */       if (sf == null) {
/*  98 */         UIManager.removePropertyChangeListener(this);
/*  99 */       } else if ("lookAndFeel".equals(evt.getPropertyName())) {
/* 100 */         sf.updateUI();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 109 */     LookAndFeelAddons.getAddon();
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
/*     */   public static SearchFactory getInstance()
/*     */   {
/* 135 */     if (searchFactory == null) {
/* 136 */       searchFactory = new SearchFactory();
/*     */     }
/* 138 */     return searchFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setInstance(SearchFactory factory)
/*     */   {
/* 147 */     searchFactory = factory;
/*     */   }
/*     */   
/*     */   public SearchFactory() {
/* 151 */     UIManager.addPropertyChangeListener(new LaFListener(this));
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
/*     */   public KeyStroke getSearchAccelerator()
/*     */   {
/* 166 */     String findMnemonic = "F";
/* 167 */     KeyStroke findStroke = Utilities.stringToKey("D-" + findMnemonic);
/*     */     
/* 169 */     if (findStroke == null) {
/* 170 */       findStroke = KeyStroke.getKeyStroke("control F");
/*     */     }
/* 172 */     return findStroke;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseFindBar(JComponent target, Searchable searchable)
/*     */   {
/* 184 */     return this.useFindBar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseFindBar(boolean incremental)
/*     */   {
/* 195 */     if (incremental == this.useFindBar) return;
/* 196 */     this.useFindBar = incremental;
/* 197 */     getFindRemover().endSearching();
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
/*     */   public void showFindInput(JComponent target, Searchable searchable)
/*     */   {
/* 213 */     if (isUseFindBar(target, searchable)) {
/* 214 */       showFindBar(target, searchable);
/*     */     } else {
/* 216 */       showFindDialog(target, searchable);
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
/*     */ 
/*     */ 
/*     */   public void showFindBar(JComponent target, Searchable searchable)
/*     */   {
/* 234 */     if (target == null) return;
/* 235 */     if (this.findBar == null) {
/* 236 */       this.findBar = getSharedFindBar();
/*     */     } else {
/* 238 */       releaseFindBar();
/*     */     }
/* 240 */     Window topLevel = SwingUtilities.getWindowAncestor(target);
/* 241 */     if ((topLevel instanceof JXFrame)) {
/* 242 */       JXRootPane rootPane = ((JXFrame)topLevel).getRootPaneExt();
/* 243 */       JToolBar toolBar = rootPane.getToolBar();
/* 244 */       if (toolBar == null) {
/* 245 */         toolBar = new JToolBar();
/* 246 */         rootPane.setToolBar(toolBar);
/*     */       }
/* 248 */       toolBar.add(this.findBar, 0);
/* 249 */       rootPane.revalidate();
/* 250 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(this.findBar);
/*     */     }
/*     */     
/* 253 */     this.lastFindBarTarget = target;
/* 254 */     this.findBar.setLocale(target.getLocale());
/* 255 */     target.putClientProperty("match.highlighter", Boolean.TRUE);
/* 256 */     getSharedFindBar().setSearchable(searchable);
/* 257 */     installFindRemover(target, this.findBar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXFindBar getSharedFindBar()
/*     */   {
/* 267 */     if (this.findBar == null) {
/* 268 */       this.findBar = createFindBar();
/* 269 */       configureSharedFindBar();
/*     */     }
/* 271 */     return this.findBar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXFindBar createFindBar()
/*     */   {
/* 280 */     return new JXFindBar();
/*     */   }
/*     */   
/*     */   protected void installFindRemover(Container target, Container findWidget)
/*     */   {
/* 285 */     if (target != null) {
/* 286 */       getFindRemover().addTarget(target);
/*     */     }
/* 288 */     getFindRemover().addTarget(findWidget);
/*     */   }
/*     */   
/*     */   private FindRemover getFindRemover() {
/* 292 */     if (this.findRemover == null) {
/* 293 */       this.findRemover = new FindRemover();
/*     */     }
/* 295 */     return this.findRemover;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeFromParent(JComponent component)
/*     */   {
/* 303 */     Container oldParent = component.getParent();
/* 304 */     if (oldParent != null) {
/* 305 */       oldParent.remove(component);
/* 306 */       if ((oldParent instanceof JComponent)) {
/* 307 */         ((JComponent)oldParent).revalidate();
/*     */       }
/*     */       else {
/* 310 */         oldParent.invalidate();
/* 311 */         oldParent.validate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void stopSearching() {
/* 317 */     if (this.findPanel != null) {
/* 318 */       this.lastFindDialogLocation = hideSharedFindPanel(false);
/* 319 */       this.findPanel.setSearchable(null);
/*     */     }
/* 321 */     if (this.findBar != null) {
/* 322 */       releaseFindBar();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void releaseFindBar()
/*     */   {
/* 330 */     this.findBar.setSearchable(null);
/* 331 */     if (this.lastFindBarTarget != null) {
/* 332 */       this.lastFindBarTarget.putClientProperty("match.highlighter", Boolean.FALSE);
/* 333 */       this.lastFindBarTarget = null;
/*     */     }
/* 335 */     removeFromParent(this.findBar);
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
/*     */   protected void configureSharedFindBar()
/*     */   {
/* 351 */     Action removeAction = new AbstractAction()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 354 */         SearchFactory.this.removeFromParent(SearchFactory.this.findBar);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 360 */     };
/* 361 */     this.findBar.getActionMap().put("close", removeAction);
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
/*     */   public void showFindDialog(JComponent target, Searchable searchable)
/*     */   {
/* 378 */     Window frame = null;
/* 379 */     if (target != null) {
/* 380 */       target.putClientProperty("match.highlighter", Boolean.FALSE);
/* 381 */       frame = SwingUtilities.getWindowAncestor(target);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 386 */     JXDialog topLevel = getDialogForSharedFindPanel();
/*     */     JXDialog findDialog;
/* 388 */     if ((topLevel != null) && (topLevel.getOwner().equals(frame))) {
/* 389 */       JXDialog findDialog = topLevel;
/*     */       
/*     */ 
/* 392 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(findDialog);
/*     */     } else {
/* 394 */       Point location = hideSharedFindPanel(true);
/* 395 */       JXDialog findDialog; if ((frame instanceof Frame)) {
/* 396 */         findDialog = new JXDialog((Frame)frame, getSharedFindPanel()); } else { JXDialog findDialog;
/* 397 */         if ((frame instanceof Dialog))
/*     */         {
/* 399 */           findDialog = new JXDialog((Dialog)frame, getSharedFindPanel());
/*     */         } else {
/* 401 */           findDialog = new JXDialog(JOptionPane.getRootFrame(), getSharedFindPanel());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 407 */       findDialog.pack();
/* 408 */       if (location == null) {
/* 409 */         findDialog.setLocationRelativeTo(frame);
/*     */       } else {
/* 411 */         findDialog.setLocation(location);
/*     */       }
/*     */     }
/* 414 */     if (target != null) {
/* 415 */       findDialog.setLocale(target.getLocale());
/*     */     }
/* 417 */     getSharedFindPanel().setSearchable(searchable);
/* 418 */     installFindRemover(target, findDialog);
/* 419 */     findDialog.setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXFindPanel getSharedFindPanel()
/*     */   {
/* 430 */     if (this.findPanel == null) {
/* 431 */       this.findPanel = createFindPanel();
/* 432 */       configureSharedFindPanel();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 440 */     return this.findPanel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXFindPanel createFindPanel()
/*     */   {
/* 449 */     return new JXFindPanel();
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
/*     */   private JXDialog getDialogForSharedFindPanel()
/*     */   {
/* 468 */     if (this.findPanel == null) return null;
/* 469 */     Window window = SwingUtilities.getWindowAncestor(this.findPanel);
/* 470 */     return (window instanceof JXDialog) ? (JXDialog)window : null;
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
/*     */   protected Point hideSharedFindPanel(boolean dispose)
/*     */   {
/* 485 */     if (this.findPanel == null) return null;
/* 486 */     Window window = SwingUtilities.getWindowAncestor(this.findPanel);
/* 487 */     Point location = this.lastFindDialogLocation;
/* 488 */     if (window != null)
/*     */     {
/* 490 */       if (window.isVisible()) {
/* 491 */         location = window.getLocationOnScreen();
/* 492 */         window.setVisible(false);
/*     */       }
/* 494 */       if (dispose) {
/* 495 */         this.findPanel.getParent().remove(this.findPanel);
/* 496 */         window.dispose();
/*     */       }
/*     */     }
/* 499 */     return location;
/*     */   }
/*     */   
/*     */   public class FindRemover implements PropertyChangeListener {
/*     */     KeyboardFocusManager focusManager;
/*     */     Set<Container> targets;
/*     */     
/*     */     public FindRemover() {
/* 507 */       updateManager();
/*     */     }
/*     */     
/*     */     public void addTarget(Container target) {
/* 511 */       getTargets().add(target);
/*     */     }
/*     */     
/*     */     public void removeTarget(Container target) {
/* 515 */       getTargets().remove(target);
/*     */     }
/*     */     
/*     */     private Set<Container> getTargets() {
/* 519 */       if (this.targets == null) {
/* 520 */         this.targets = new HashSet();
/*     */       }
/* 522 */       return this.targets;
/*     */     }
/*     */     
/*     */     private void updateManager() {
/* 526 */       if (this.focusManager != null) {
/* 527 */         this.focusManager.removePropertyChangeListener("permanentFocusOwner", this);
/*     */       }
/* 529 */       this.focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 530 */       this.focusManager.addPropertyChangeListener("permanentFocusOwner", this);
/*     */     }
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent ev)
/*     */     {
/* 535 */       Component c = this.focusManager.getPermanentFocusOwner();
/* 536 */       if (c == null) return;
/* 537 */       for (Iterator<Container> iter = getTargets().iterator(); iter.hasNext();) {
/* 538 */         Container element = (Container)iter.next();
/* 539 */         if ((element == c) || (SwingUtilities.isDescendingFrom(c, element))) {
/* 540 */           return;
/*     */         }
/*     */       }
/* 543 */       endSearching();
/*     */     }
/*     */     
/*     */     public void endSearching() {
/* 547 */       getTargets().clear();
/* 548 */       SearchFactory.this.stopSearching();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 557 */     if (this.findBar != null) {
/* 558 */       SwingUtilities.updateComponentTreeUI(this.findBar);
/*     */     }
/*     */     
/* 561 */     if (this.findPanel != null) {
/* 562 */       SwingUtilities.updateComponentTreeUI(this.findPanel);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void configureSharedFindPanel() {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\SearchFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */