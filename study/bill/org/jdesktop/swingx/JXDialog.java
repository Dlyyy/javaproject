/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.LayoutManager;
/*     */ import java.util.Locale;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;
/*     */ import org.jdesktop.swingx.action.BoundAction;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXDialog
/*     */   extends JDialog
/*     */ {
/*     */   public static final String EXECUTE_ACTION_COMMAND = "execute";
/*     */   public static final String CLOSE_ACTION_COMMAND = "close";
/*     */   public static final String UIPREFIX = "XDialog.";
/*     */   protected JComponent content;
/*     */   
/*     */   static
/*     */   {
/*  70 */     LookAndFeelAddons.getAddon();
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
/*     */   public JXDialog(JComponent content)
/*     */   {
/*  88 */     setContent(content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXDialog(Frame frame, JComponent content)
/*     */   {
/* 100 */     super(frame);
/* 101 */     setContent(content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXDialog(Dialog dialog, JComponent content)
/*     */   {
/* 112 */     super(dialog);
/* 113 */     setContent(content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JXRootPane createRootPane()
/*     */   {
/* 121 */     return new JXRootPane();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXRootPane getRootPane()
/*     */   {
/* 129 */     return (JXRootPane)super.getRootPane();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatusBar(JXStatusBar statusBar)
/*     */   {
/* 141 */     getRootPane().setStatusBar(statusBar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXStatusBar getStatusBar()
/*     */   {
/* 153 */     return getRootPane().getStatusBar();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToolBar(JToolBar toolBar)
/*     */   {
/* 165 */     getRootPane().setToolBar(toolBar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JToolBar getToolBar()
/*     */   {
/* 177 */     return getRootPane().getToolBar();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setContent(JComponent content)
/*     */   {
/* 187 */     if (this.content != null) {
/* 188 */       throw new IllegalStateException("content must not be set more than once");
/*     */     }
/* 190 */     initActions();
/* 191 */     Action contentCloseAction = content.getActionMap().get("close");
/* 192 */     if (contentCloseAction != null) {
/* 193 */       putAction("close", contentCloseAction);
/*     */     }
/* 195 */     Action contentExecuteAction = content.getActionMap().get("execute");
/* 196 */     if (contentExecuteAction != null) {
/* 197 */       putAction("execute", contentExecuteAction);
/*     */     }
/* 199 */     this.content = content;
/* 200 */     build();
/* 201 */     setTitleFromContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setTitleFromContent()
/*     */   {
/* 211 */     if (this.content == null) return;
/* 212 */     setTitle(this.content.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void build()
/*     */   {
/* 220 */     JComponent contentBox = new Box(3);
/* 221 */     contentBox.add(this.content);
/* 222 */     JComponent buttonPanel = createButtonPanel();
/* 223 */     contentBox.add(buttonPanel);
/* 224 */     contentBox.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 229 */     add(contentBox);
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
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 242 */     if (this.content == null) { throw new IllegalStateException("content must be built before showing the dialog");
/*     */     }
/* 244 */     super.setVisible(visible);
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
/*     */   public void setLocale(Locale l)
/*     */   {
/* 265 */     if (this.content != null) {
/* 266 */       this.content.setLocale(l);
/* 267 */       updateLocaleState(l);
/*     */     }
/* 269 */     super.setLocale(l);
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
/*     */   protected void updateLocaleState(Locale locale)
/*     */   {
/* 282 */     setTitleFromContent();
/* 283 */     for (Object key : getRootPane().getActionMap().allKeys()) {
/* 284 */       if ((key instanceof String)) {
/* 285 */         Action contentAction = this.content.getActionMap().get(key);
/* 286 */         Action rootPaneAction = getAction(key);
/* 287 */         if (!rootPaneAction.equals(contentAction)) {
/* 288 */           String keyString = getUIString((String)key, locale);
/* 289 */           if (!key.equals(keyString)) {
/* 290 */             rootPaneAction.putValue("Name", keyString);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doClose()
/*     */   {
/* 303 */     dispose();
/*     */   }
/*     */   
/*     */   private void initActions() {
/* 307 */     Action defaultAction = createCloseAction();
/* 308 */     putAction("close", defaultAction);
/* 309 */     putAction("execute", defaultAction);
/*     */   }
/*     */   
/*     */   private Action createCloseAction() {
/* 313 */     String actionName = getUIString("close");
/* 314 */     BoundAction action = new BoundAction(actionName, "close");
/*     */     
/* 316 */     action.registerCallback(this, "doClose");
/* 317 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JComponent createButtonPanel()
/*     */   {
/* 328 */     JPanel panel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 6))
/*     */     {
/*     */       public Dimension getMaximumSize()
/*     */       {
/* 332 */         return getPreferredSize();
/*     */       }
/*     */       
/* 335 */     };
/* 336 */     panel.setBorder(BorderFactory.createEmptyBorder(9, 0, 0, 0));
/* 337 */     Action executeAction = getAction("execute");
/* 338 */     Action closeAction = getAction("close");
/*     */     
/* 340 */     JButton defaultButton = new JButton(executeAction);
/* 341 */     panel.add(defaultButton);
/* 342 */     getRootPane().setDefaultButton(defaultButton);
/*     */     
/* 344 */     if (executeAction != closeAction) {
/* 345 */       JButton b = new JButton(closeAction);
/* 346 */       panel.add(b);
/* 347 */       getRootPane().setCancelButton(b);
/*     */     }
/*     */     
/* 350 */     return panel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void putAction(Object key, Action action)
/*     */   {
/* 359 */     getRootPane().getActionMap().put(key, action);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Action getAction(Object key)
/*     */   {
/* 369 */     return getRootPane().getActionMap().get(key);
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
/*     */   protected String getUIString(String key)
/*     */   {
/* 382 */     return getUIString(key, getLocale());
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
/*     */   protected String getUIString(String key, Locale locale)
/*     */   {
/* 397 */     String text = UIManagerExt.getString("XDialog." + key, locale);
/* 398 */     return text != null ? text : key;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */