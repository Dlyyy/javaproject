/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.skin.Skin;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.border.AbstractBorder;
/*    */ import javax.swing.plaf.UIResource;
/*    */ import javax.swing.plaf.basic.BasicComboBoxEditor;
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
/*    */ public class LiquidComboBoxEditor
/*    */   extends BasicComboBoxEditor
/*    */ {
/*    */   static Skin skin;
/*    */   
/*    */   public LiquidComboBoxEditor()
/*    */   {
/* 33 */     this.editor.setBorder(new AbstractBorder()
/*    */     {
/*    */ 
/*    */ 
/*    */       public Insets getBorderInsets(Component c)
/*    */       {
/*    */ 
/* 40 */         return new Insets(0, 3, 0, 3);
/*    */       }
/*    */       
/*    */       public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {}
/*    */     });
/*    */   }
/*    */   
/*    */   public static class UIResource
/*    */     extends LiquidComboBoxEditor
/*    */     implements UIResource
/*    */   {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidComboBoxEditor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */