/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import java.awt.Window;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.plaf.ButtonUI;
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
/*    */ public class SpecialUIButton
/*    */   extends JButton
/*    */ {
/*    */   ButtonUI myUI;
/*    */   JInternalFrame frame;
/*    */   Window window;
/*    */   
/*    */   public SpecialUIButton(ButtonUI ui)
/*    */   {
/* 29 */     this.ui = ui;
/* 30 */     this.myUI = ui;
/* 31 */     ui.installUI(this);
/*    */   }
/*    */   
/*    */   public SpecialUIButton(ButtonUI ui, JInternalFrame frame) {
/* 35 */     this.ui = ui;
/* 36 */     this.myUI = ui;
/* 37 */     ui.installUI(this);
/* 38 */     this.frame = frame;
/* 39 */     setOpaque(false);
/* 40 */     setFocusPainted(false);
/* 41 */     setFocusable(false);
/*    */   }
/*    */   
/*    */   public SpecialUIButton(ButtonUI ui, Window frame) {
/* 45 */     this.ui = ui;
/* 46 */     this.myUI = ui;
/* 47 */     ui.installUI(this);
/* 48 */     this.window = frame;
/* 49 */     setOpaque(false);
/* 50 */     setFocusPainted(false);
/* 51 */     setFocusable(false);
/*    */   }
/*    */   
/*    */   public void setUI(ButtonUI ui) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\SpecialUIButton.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */