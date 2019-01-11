/*     */ package org.jdesktop.swingx.rollover;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.KeyStroke;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RolloverController<T extends JComponent>
/*     */   implements PropertyChangeListener
/*     */ {
/*  54 */   private static final Logger LOG = Logger.getLogger(RolloverController.class.getName());
/*     */   
/*     */ 
/*     */   public static final String EXECUTE_BUTTON_ACTIONCOMMAND = "executeButtonAction";
/*     */   
/*     */ 
/*     */   protected T component;
/*     */   
/*     */ 
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent evt)
/*     */   {
/*  66 */     if ((this.component == null) || (this.component != evt.getSource()))
/*  67 */       return;
/*  68 */     if ("swingx.rollover".equals(evt.getPropertyName())) {
/*  69 */       rollover((Point)evt.getOldValue(), (Point)evt.getNewValue());
/*  70 */     } else if ("swingx.clicked".equals(evt.getPropertyName())) {
/*  71 */       click((Point)evt.getNewValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void install(T table)
/*     */   {
/*  81 */     release();
/*  82 */     this.component = table;
/*  83 */     table.addPropertyChangeListener("swingx.clicked", this);
/*  84 */     table.addPropertyChangeListener("swingx.rollover", this);
/*  85 */     registerExecuteButtonAction();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void release()
/*     */   {
/*  93 */     if (this.component == null)
/*  94 */       return;
/*  95 */     this.component.removePropertyChangeListener("swingx.clicked", this);
/*  96 */     this.component.removePropertyChangeListener("swingx.rollover", this);
/*  97 */     unregisterExecuteButtonAction();
/*  98 */     this.component = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void rollover(Point paramPoint1, Point paramPoint2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void click(Point location)
/*     */   {
/* 114 */     if (!isClickable(location))
/* 115 */       return;
/* 116 */     RolloverRenderer rollover = getRolloverRenderer(location, true);
/* 117 */     if (rollover != null) {
/* 118 */       rollover.doClick();
/* 119 */       this.component.repaint();
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
/*     */   protected abstract RolloverRenderer getRolloverRenderer(Point paramPoint, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isClickable(Point location)
/*     */   {
/* 150 */     return hasRollover(location);
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
/*     */   protected boolean hasRollover(Point location)
/*     */   {
/* 168 */     if ((location == null) || (location.x < 0) || (location.y < 0))
/* 169 */       return false;
/* 170 */     return getRolloverRenderer(location, false) != null;
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
/*     */   protected abstract Point getFocusedCell();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void unregisterExecuteButtonAction()
/*     */   {
/* 195 */     this.component.getActionMap().put("executeButtonAction", null);
/* 196 */     KeyStroke space = KeyStroke.getKeyStroke("released SPACE");
/* 197 */     this.component.getInputMap(1).put(space, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerExecuteButtonAction()
/*     */   {
/* 207 */     this.component.getActionMap().put("executeButtonAction", createExecuteButtonAction());
/*     */     
/* 209 */     KeyStroke space = KeyStroke.getKeyStroke("released SPACE");
/* 210 */     this.component.getInputMap(1).put(space, "executeButtonAction");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Action createExecuteButtonAction()
/*     */   {
/* 221 */     new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 223 */         RolloverController.this.click(RolloverController.this.getFocusedCell());
/*     */       }
/*     */       
/*     */       public boolean isEnabled()
/*     */       {
/* 228 */         if ((RolloverController.this.component == null) || (!RolloverController.this.component.isEnabled()) || (!RolloverController.this.component.hasFocus()))
/* 229 */           return false;
/* 230 */         return RolloverController.this.isClickable(RolloverController.this.getFocusedCell());
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\RolloverController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */