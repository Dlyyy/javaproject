/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.AWTEventListener;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.Timer;
/*     */ import org.jdesktop.swingx.util.WindowUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXFrame
/*     */   extends JFrame
/*     */ {
/*     */   public static enum StartPosition
/*     */   {
/*  77 */     CenterInScreen,  CenterInParent,  Manual;
/*     */     private StartPosition() {} }
/*  79 */   private Component waitPane = null;
/*  80 */   private Component glassPane = null;
/*  81 */   private boolean waitPaneVisible = false;
/*  82 */   private Cursor realCursor = null;
/*  83 */   private boolean waitCursorVisible = false;
/*  84 */   private boolean waiting = false;
/*     */   private StartPosition startPosition;
/*  86 */   private boolean hasBeenVisible = false;
/*     */   private AWTEventListener keyEventListener;
/*  88 */   private boolean keyPreview = false;
/*     */   private AWTEventListener idleListener;
/*     */   private Timer idleTimer;
/*  91 */   private long idleThreshold = 0L;
/*     */   
/*     */   private boolean idle;
/*     */   
/*     */ 
/*     */   public JXFrame()
/*     */   {
/*  98 */     this(null, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXFrame(String title)
/*     */   {
/* 109 */     this(title, false);
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
/*     */   public JXFrame(String title, boolean exitOnClose)
/*     */   {
/* 123 */     super(title);
/* 124 */     if (exitOnClose) {
/* 125 */       setDefaultCloseOperation(3);
/*     */     }
/*     */     
/*     */ 
/* 129 */     this.keyEventListener = new AWTEventListener() {
/*     */       public void eventDispatched(AWTEvent aWTEvent) {
/* 131 */         if ((aWTEvent instanceof KeyEvent)) {
/* 132 */           KeyEvent evt = (KeyEvent)aWTEvent;
/* 133 */           for (KeyListener kl : JXFrame.this.getKeyListeners()) {
/* 134 */             int id = aWTEvent.getID();
/* 135 */             switch (id) {
/*     */             case 401: 
/* 137 */               kl.keyPressed(evt);
/* 138 */               break;
/*     */             case 402: 
/* 140 */               kl.keyReleased(evt);
/* 141 */               break;
/*     */             case 400: 
/* 143 */               kl.keyTyped(evt);
/* 144 */               break;
/*     */             default: 
/* 146 */               System.err.println("Unhandled Key ID: " + id);
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/* 152 */     };
/* 153 */     this.idleTimer = new Timer(100, new ActionListener() {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/* 155 */         JXFrame.this.setIdle(true);
/*     */       }
/*     */       
/*     */ 
/* 159 */     });
/* 160 */     this.idleListener = new AWTEventListener()
/*     */     {
/*     */       public void eventDispatched(AWTEvent aWTEvent) {
/* 163 */         JXFrame.this.idleTimer.stop();
/*     */         
/* 165 */         if (JXFrame.this.isIdle()) {
/* 166 */           JXFrame.this.setIdle(false);
/*     */         }
/*     */         
/* 169 */         JXFrame.this.idleTimer.restart();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCancelButton(JButton button)
/*     */   {
/* 183 */     getRootPaneExt().setCancelButton(button);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JButton getCancelButton()
/*     */   {
/* 195 */     return getRootPaneExt().getCancelButton();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultButton(JButton button)
/*     */   {
/* 207 */     JButton old = getDefaultButton();
/* 208 */     getRootPane().setDefaultButton(button);
/* 209 */     firePropertyChange("defaultButton", old, getDefaultButton());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JButton getDefaultButton()
/*     */   {
/* 221 */     return getRootPane().getDefaultButton();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKeyPreview(boolean flag)
/*     */   {
/* 233 */     Toolkit.getDefaultToolkit().removeAWTEventListener(this.keyEventListener);
/* 234 */     if (flag) {
/* 235 */       Toolkit.getDefaultToolkit().addAWTEventListener(this.keyEventListener, 8L);
/*     */     }
/* 237 */     boolean old = this.keyPreview;
/* 238 */     this.keyPreview = flag;
/* 239 */     firePropertyChange("keyPreview", old, this.keyPreview);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean getKeyPreview()
/*     */   {
/* 249 */     return this.keyPreview;
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
/*     */   public void setStartPosition(StartPosition position)
/*     */   {
/* 262 */     StartPosition old = getStartPosition();
/* 263 */     this.startPosition = position;
/* 264 */     firePropertyChange("startPosition", old, getStartPosition());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StartPosition getStartPosition()
/*     */   {
/* 274 */     return this.startPosition == null ? StartPosition.Manual : this.startPosition;
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
/*     */   public void setWaitCursorVisible(boolean flag)
/*     */   {
/* 287 */     boolean old = isWaitCursorVisible();
/* 288 */     if (flag != old) {
/* 289 */       this.waitCursorVisible = flag;
/* 290 */       if (isWaitCursorVisible()) {
/* 291 */         this.realCursor = getCursor();
/* 292 */         super.setCursor(Cursor.getPredefinedCursor(3));
/*     */       } else {
/* 294 */         super.setCursor(this.realCursor);
/*     */       }
/* 296 */       firePropertyChange("waitCursorVisible", old, isWaitCursorVisible());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWaitCursorVisible()
/*     */   {
/* 307 */     return this.waitCursorVisible;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCursor(Cursor c)
/*     */   {
/* 315 */     if (!isWaitCursorVisible()) {
/* 316 */       super.setCursor(c);
/*     */     } else {
/* 318 */       this.realCursor = c;
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
/*     */   public void setWaitPane(Component c)
/*     */   {
/* 332 */     Component old = getWaitPane();
/* 333 */     this.waitPane = c;
/* 334 */     firePropertyChange("waitPane", old, getWaitPane());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Component getWaitPane()
/*     */   {
/* 345 */     return this.waitPane;
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
/*     */   public void setWaitPaneVisible(boolean flag)
/*     */   {
/* 360 */     boolean old = isWaitPaneVisible();
/* 361 */     if (flag != old) {
/* 362 */       this.waitPaneVisible = flag;
/* 363 */       Component wp = getWaitPane();
/* 364 */       if (isWaitPaneVisible()) {
/* 365 */         this.glassPane = getRootPane().getGlassPane();
/* 366 */         if (wp != null) {
/* 367 */           getRootPane().setGlassPane(wp);
/* 368 */           wp.setVisible(true);
/*     */         }
/*     */       } else {
/* 371 */         if (wp != null) {
/* 372 */           wp.setVisible(false);
/*     */         }
/* 374 */         getRootPane().setGlassPane(this.glassPane);
/*     */       }
/* 376 */       firePropertyChange("waitPaneVisible", old, isWaitPaneVisible());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWaitPaneVisible()
/*     */   {
/* 387 */     return this.waitPaneVisible;
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
/*     */   public void setWaiting(boolean waiting)
/*     */   {
/* 401 */     boolean old = isWaiting();
/* 402 */     this.waiting = waiting;
/* 403 */     firePropertyChange("waiting", old, isWaiting());
/* 404 */     setWaitPaneVisible(waiting);
/* 405 */     setWaitCursorVisible(waiting);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWaiting()
/*     */   {
/* 416 */     return this.waiting;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 424 */     if ((!this.hasBeenVisible) && (visible))
/*     */     {
/* 426 */       StartPosition pos = getStartPosition();
/* 427 */       switch (pos) {
/*     */       case CenterInParent: 
/* 429 */         setLocationRelativeTo(getParent());
/* 430 */         break;
/*     */       case CenterInScreen: 
/* 432 */         setLocation(WindowUtils.getPointForCentering(this));
/* 433 */         break;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 439 */     super.setVisible(visible);
/*     */   }
/*     */   
/*     */   public boolean isIdle() {
/* 443 */     return this.idle;
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
/*     */   public void setIdle(boolean idle)
/*     */   {
/* 456 */     boolean old = isIdle();
/* 457 */     this.idle = idle;
/* 458 */     firePropertyChange("idle", old, isIdle());
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
/*     */   public void setIdleThreshold(long threshold)
/*     */   {
/* 472 */     long old = getIdleThreshold();
/* 473 */     this.idleThreshold = threshold;
/* 474 */     firePropertyChange("idleThreshold", old, getIdleThreshold());
/*     */     
/* 476 */     threshold = getIdleThreshold();
/*     */     
/* 478 */     Toolkit.getDefaultToolkit().removeAWTEventListener(this.idleListener);
/* 479 */     if (threshold > 0L) {
/* 480 */       Toolkit.getDefaultToolkit().addAWTEventListener(this.idleListener, 131128L);
/*     */     }
/* 482 */     this.idleTimer.stop();
/* 483 */     this.idleTimer.setInitialDelay((int)threshold);
/* 484 */     this.idleTimer.restart();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getIdleThreshold()
/*     */   {
/* 494 */     return this.idleThreshold;
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
/* 506 */     getRootPaneExt().setStatusBar(statusBar);
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
/* 518 */     return getRootPaneExt().getStatusBar();
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
/* 530 */     getRootPaneExt().setToolBar(toolBar);
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
/* 542 */     return getRootPaneExt().getToolBar();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JRootPane createRootPane()
/*     */   {
/* 551 */     return new JXRootPane();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRootPane(JRootPane root)
/*     */   {
/* 559 */     super.setRootPane(root);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXRootPane getRootPaneExt()
/*     */   {
/* 570 */     if ((this.rootPane instanceof JXRootPane)) {
/* 571 */       return (JXRootPane)this.rootPane;
/*     */     }
/* 573 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */