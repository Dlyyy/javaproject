/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.UIResource;
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
/*    */ public class LookAndFeelUtils
/*    */ {
/*    */   public static Object getUIOfType(ComponentUI ui, Class<?> klass)
/*    */   {
/* 42 */     if (klass.isInstance(ui)) {
/* 43 */       return ui;
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Font getAsNotUIResource(Font font)
/*    */   {
/* 55 */     if (!(font instanceof UIResource)) { return font;
/*    */     }
/* 57 */     return font.deriveFont(font.getAttributes());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Color getAsNotUIResource(Color color)
/*    */   {
/* 67 */     if (!(color instanceof UIResource)) { return color;
/*    */     }
/* 69 */     float[] rgb = color.getRGBComponents(null);
/* 70 */     return new Color(rgb[0], rgb[1], rgb[2], rgb[3]);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\LookAndFeelUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */