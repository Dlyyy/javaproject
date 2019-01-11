/*    */ package org.jdesktop.swingx.plaf.macosx;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import org.jdesktop.swingx.JXStatusBar;
/*    */ import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MacOSXStatusBarUI
/*    */   extends BasicStatusBarUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 47 */     return new MacOSXStatusBarUI();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void installDefaults(JXStatusBar sb)
/*    */   {
/* 55 */     super.installDefaults(sb);
/*    */     
/* 57 */     LookAndFeel.installProperty(this.statusBar, "opaque", Boolean.FALSE);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\macosx\MacOSXStatusBarUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */