/*     */ package org.jdesktop.swingx.plaf.basic.core;
/*     */ 
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DragRecognitionSupport
/*     */ {
/*     */   private int motionThreshold;
/*     */   private MouseEvent dndArmedEvent;
/*     */   private JComponent component;
/*     */   
/*     */   private static DragRecognitionSupport getDragRecognitionSupport()
/*     */   {
/*  85 */     DragRecognitionSupport support = (DragRecognitionSupport)UIManager.get("sharedInstance.dragRecognitionSupport");
/*     */     
/*  87 */     if (support == null) {
/*  88 */       support = new DragRecognitionSupport();
/*  89 */       UIManager.put("sharedInstance.dragRecognitionSupport", support);
/*     */     }
/*  91 */     return support;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean mousePressed(MouseEvent me)
/*     */   {
/*  98 */     return getDragRecognitionSupport().mousePressedImpl(me);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MouseEvent mouseReleased(MouseEvent me)
/*     */   {
/* 107 */     return getDragRecognitionSupport().mouseReleasedImpl(me);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean mouseDragged(MouseEvent me, BeforeDrag bd)
/*     */   {
/* 115 */     return getDragRecognitionSupport().mouseDraggedImpl(me, bd);
/*     */   }
/*     */   
/*     */   private void clearState()
/*     */   {
/* 120 */     this.dndArmedEvent = null;
/* 121 */     this.component = null;
/*     */   }
/*     */   
/*     */ 
/*     */   private int mapDragOperationFromModifiers(MouseEvent me, TransferHandler th)
/*     */   {
/* 127 */     if ((th == null) || (!SwingUtilities.isLeftMouseButton(me))) {
/* 128 */       return 0;
/*     */     }
/*     */     
/* 131 */     return SwingXUtilities.convertModifiersToDropAction(me.getModifiersEx(), th.getSourceActions(this.component));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean mousePressedImpl(MouseEvent me)
/*     */   {
/* 140 */     this.component = ((JComponent)me.getSource());
/*     */     
/* 142 */     if (mapDragOperationFromModifiers(me, this.component.getTransferHandler()) != 0)
/*     */     {
/*     */ 
/* 145 */       this.motionThreshold = DragSource.getDragThreshold();
/* 146 */       this.dndArmedEvent = me;
/* 147 */       return true;
/*     */     }
/*     */     
/* 150 */     clearState();
/* 151 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MouseEvent mouseReleasedImpl(MouseEvent me)
/*     */   {
/* 160 */     if (this.dndArmedEvent == null) {
/* 161 */       return null;
/*     */     }
/*     */     
/* 164 */     MouseEvent retEvent = null;
/*     */     
/* 166 */     if (me.getSource() == this.component) {
/* 167 */       retEvent = this.dndArmedEvent;
/*     */     }
/*     */     
/* 170 */     clearState();
/* 171 */     return retEvent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean mouseDraggedImpl(MouseEvent me, BeforeDrag bd)
/*     */   {
/* 179 */     if (this.dndArmedEvent == null) {
/* 180 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 184 */     if (me.getSource() != this.component) {
/* 185 */       clearState();
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     int dx = Math.abs(me.getX() - this.dndArmedEvent.getX());
/* 190 */     int dy = Math.abs(me.getY() - this.dndArmedEvent.getY());
/* 191 */     if ((dx > this.motionThreshold) || (dy > this.motionThreshold)) {
/* 192 */       TransferHandler th = this.component.getTransferHandler();
/* 193 */       int action = mapDragOperationFromModifiers(me, th);
/* 194 */       if (action != 0)
/*     */       {
/* 196 */         if (bd != null) {
/* 197 */           bd.dragStarting(this.dndArmedEvent);
/*     */         }
/* 199 */         th.exportAsDrag(this.component, this.dndArmedEvent, action);
/* 200 */         clearState();
/*     */       }
/*     */     }
/*     */     
/* 204 */     return true;
/*     */   }
/*     */   
/*     */   public static abstract interface BeforeDrag
/*     */   {
/*     */     public abstract void dragStarting(MouseEvent paramMouseEvent);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\core\DragRecognitionSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */