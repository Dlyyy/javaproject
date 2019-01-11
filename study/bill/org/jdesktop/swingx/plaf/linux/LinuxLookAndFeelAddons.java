/*    */ package org.jdesktop.swingx.plaf.linux;
/*    */ 
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.BorderUIResource;
/*    */ import javax.swing.plaf.UIResource;
/*    */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*    */ import org.jdesktop.swingx.plaf.basic.BasicLookAndFeelAddons;
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
/*    */ public class LinuxLookAndFeelAddons
/*    */   extends BasicLookAndFeelAddons
/*    */ {
/*    */   public void initialize()
/*    */   {
/* 38 */     super.initialize();
/*    */     
/*    */ 
/* 41 */     Border b = UIManagerExt.getSafeBorder("Table.focusSelectedCellHighlightBorder", BorderFactory.createEmptyBorder());
/*    */     
/* 43 */     if ((b instanceof UIResource)) {
/* 44 */       UIManager.put("Table.focusSelectedCellHighlightBorder", new BorderUIResource(b));
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\linux\LinuxLookAndFeelAddons.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */