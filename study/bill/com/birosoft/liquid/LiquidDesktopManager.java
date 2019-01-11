/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import javax.swing.DefaultDesktopManager;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LiquidDesktopManager
/*    */   extends DefaultDesktopManager
/*    */ {
/*    */   public void beginDraggingFrame(JComponent f) {}
/*    */   
/*    */   public void dragFrame(JComponent f, int newX, int newY)
/*    */   {
/* 31 */     setBoundsForFrame(f, newX, newY, f.getWidth(), f.getHeight());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void endDraggingFrame(JComponent f) {}
/*    */   
/*    */ 
/*    */   public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight)
/*    */   {
/* 41 */     setBoundsForFrame(f, newX, newY, newWidth, newHeight);
/*    */   }
/*    */   
/*    */   public void endResizingFrame(JComponent f) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidDesktopManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */