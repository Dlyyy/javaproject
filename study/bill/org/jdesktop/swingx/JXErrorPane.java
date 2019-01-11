/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.jdesktop.swingx.error.ErrorInfo;
/*     */ import org.jdesktop.swingx.error.ErrorReporter;
/*     */ import org.jdesktop.swingx.plaf.ErrorPaneAddon;
/*     */ import org.jdesktop.swingx.plaf.ErrorPaneUI;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXErrorPane
/*     */   extends JComponent
/*     */ {
/*     */   public static final String REPORT_ACTION_KEY = "report-action";
/*     */   public static final String FATAL_ACTION_KEY = "fatal-action";
/*     */   public static final String uiClassID = "ErrorPaneUI";
/*     */   
/*     */   static
/*     */   {
/* 189 */     LookAndFeelAddons.contribute(new ErrorPaneAddon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */   private ErrorInfo errorInfo = new ErrorInfo("Error", "Normally this place contains problem description.\n You see this text because one of the following reasons:\n * Either it is a test\n * Developer have not provided error details\n * This error message was invoked unexpectedly and there are no more details available", null, null, null, null, null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Icon icon;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ErrorReporter reporter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXErrorPane()
/*     */   {
/* 217 */     updateUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ErrorPaneUI getUI()
/*     */   {
/* 226 */     return (ErrorPaneUI)this.ui;
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
/*     */   public void setUI(ErrorPaneUI ui)
/*     */   {
/* 240 */     super.setUI(ui);
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
/* 252 */     return "ErrorPaneUI";
/*     */   }
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
/* 264 */     setUI((ErrorPaneUI)LookAndFeelAddons.getUI(this, ErrorPaneUI.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorInfo(ErrorInfo info)
/*     */   {
/* 276 */     if (info == null) {
/* 277 */       throw new NullPointerException("ErrorInfo can't be null. Provide valid ErrorInfo object.");
/*     */     }
/* 279 */     ErrorInfo old = this.errorInfo;
/* 280 */     this.errorInfo = info;
/* 281 */     firePropertyChange("errorInfo", old, this.errorInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ErrorInfo getErrorInfo()
/*     */   {
/* 290 */     return this.errorInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIcon(Icon icon)
/*     */   {
/* 299 */     Icon old = this.icon;
/* 300 */     this.icon = icon;
/* 301 */     firePropertyChange("icon", old, this.icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getIcon()
/*     */   {
/* 310 */     return this.icon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorReporter(ErrorReporter reporter)
/*     */   {
/* 321 */     ErrorReporter old = getErrorReporter();
/* 322 */     this.reporter = reporter;
/* 323 */     firePropertyChange("errorReporter", old, getErrorReporter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ErrorReporter getErrorReporter()
/*     */   {
/* 332 */     return this.reporter;
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
/*     */   public static void showDialog(Throwable e)
/*     */   {
/* 350 */     ErrorInfo ii = new ErrorInfo(null, null, null, null, e, null, null);
/* 351 */     showDialog(null, ii);
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
/*     */   public static void showDialog(Component owner, ErrorInfo info)
/*     */   {
/* 369 */     JXErrorPane pane = new JXErrorPane();
/* 370 */     pane.setErrorInfo(info);
/* 371 */     showDialog(owner, pane);
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
/*     */   public static void showDialog(Component owner, final JXErrorPane pane)
/*     */   {
/* 390 */     Runnable r = new Runnable() {
/*     */       public void run() {
/* 392 */         JDialog dlg = JXErrorPane.createDialog(this.val$owner, pane);
/* 393 */         dlg.setVisible(true);
/*     */       }
/*     */     };
/*     */     
/* 397 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       try {
/* 399 */         SwingUtilities.invokeAndWait(r);
/*     */       } catch (InvocationTargetException ex) {
/* 401 */         ex.printStackTrace();
/*     */       } catch (InterruptedException ex) {
/* 403 */         ex.printStackTrace();
/*     */       }
/*     */     } else {
/* 406 */       r.run();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JDialog createDialog(Component owner, JXErrorPane pane)
/*     */   {
/* 428 */     JDialog window = pane.getUI().getErrorDialog(owner);
/*     */     
/*     */ 
/* 431 */     if (owner != null) {
/* 432 */       pane.applyComponentOrientation(owner.getComponentOrientation());
/*     */     } else {
/* 434 */       pane.applyComponentOrientation(window.getComponentOrientation());
/*     */     }
/* 436 */     window.setDefaultCloseOperation(2);
/* 437 */     window.pack();
/* 438 */     window.setLocationRelativeTo(owner);
/* 439 */     return window;
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
/*     */   public static void showFrame(Throwable e)
/*     */   {
/* 455 */     ErrorInfo ii = new ErrorInfo(null, null, null, null, e, null, null);
/* 456 */     showFrame(null, ii);
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
/*     */   public static void showFrame(Component owner, ErrorInfo info)
/*     */   {
/* 474 */     JXErrorPane pane = new JXErrorPane();
/* 475 */     pane.setErrorInfo(info);
/* 476 */     showFrame(owner, pane);
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
/*     */   public static void showFrame(Component owner, final JXErrorPane pane)
/*     */   {
/* 495 */     Runnable r = new Runnable() {
/*     */       public void run() {
/* 497 */         JFrame window = JXErrorPane.createFrame(this.val$owner, pane);
/* 498 */         window.setVisible(true);
/*     */       }
/*     */     };
/*     */     
/* 502 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       try {
/* 504 */         SwingUtilities.invokeAndWait(r);
/*     */       } catch (InvocationTargetException ex) {
/* 506 */         ex.printStackTrace();
/*     */       } catch (InterruptedException ex) {
/* 508 */         ex.printStackTrace();
/*     */       }
/*     */     } else {
/* 511 */       r.run();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JFrame createFrame(Component owner, JXErrorPane pane)
/*     */   {
/* 533 */     JFrame window = pane.getUI().getErrorFrame(owner);
/*     */     
/*     */ 
/* 536 */     if (owner != null) {
/* 537 */       pane.applyComponentOrientation(owner.getComponentOrientation());
/*     */     } else {
/* 539 */       pane.applyComponentOrientation(window.getComponentOrientation());
/*     */     }
/* 541 */     window.setDefaultCloseOperation(2);
/* 542 */     window.pack();
/*     */     
/* 544 */     return window;
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
/*     */   public static void showInternalFrame(Throwable e)
/*     */   {
/* 560 */     ErrorInfo ii = new ErrorInfo(null, null, null, null, e, null, null);
/* 561 */     showInternalFrame(null, ii);
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
/*     */   public static void showInternalFrame(Component owner, ErrorInfo info)
/*     */   {
/* 579 */     JXErrorPane pane = new JXErrorPane();
/* 580 */     pane.setErrorInfo(info);
/* 581 */     showInternalFrame(owner, pane);
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
/*     */   public static void showInternalFrame(Component owner, final JXErrorPane pane)
/*     */   {
/* 600 */     Runnable r = new Runnable() {
/*     */       public void run() {
/* 602 */         JInternalFrame window = JXErrorPane.createInternalFrame(this.val$owner, pane);
/* 603 */         window.setVisible(true);
/*     */       }
/*     */     };
/*     */     
/* 607 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       try {
/* 609 */         SwingUtilities.invokeAndWait(r);
/*     */       } catch (InvocationTargetException ex) {
/* 611 */         ex.printStackTrace();
/*     */       } catch (InterruptedException ex) {
/* 613 */         ex.printStackTrace();
/*     */       }
/*     */     } else {
/* 616 */       r.run();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JInternalFrame createInternalFrame(Component owner, JXErrorPane pane)
/*     */   {
/* 638 */     JInternalFrame window = pane.getUI().getErrorInternalFrame(owner);
/*     */     
/*     */ 
/* 641 */     if (owner != null) {
/* 642 */       pane.applyComponentOrientation(owner.getComponentOrientation());
/*     */     } else {
/* 644 */       pane.applyComponentOrientation(window.getComponentOrientation());
/*     */     }
/* 646 */     window.setDefaultCloseOperation(2);
/* 647 */     window.pack();
/*     */     
/*     */ 
/* 650 */     return window;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXErrorPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */