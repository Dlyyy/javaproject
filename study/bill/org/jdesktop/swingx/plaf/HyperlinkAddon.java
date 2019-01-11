/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import javax.swing.plaf.ColorUIResource;
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
/*    */ public class HyperlinkAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public HyperlinkAddon()
/*    */   {
/* 33 */     super("JXHyperlink");
/*    */   }
/*    */   
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 38 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 40 */     defaults.add("HyperlinkUI", "org.jdesktop.swingx.plaf.basic.BasicHyperlinkUI");
/*    */     
/* 42 */     defaults.add("Hyperlink.linkColor", new ColorUIResource(0, 51, 255));
/* 43 */     defaults.add("Hyperlink.visitedColor", new ColorUIResource(153, 0, 153));
/* 44 */     defaults.add("Hyperlink.hoverColor", new ColorUIResource(255, 51, 0));
/* 45 */     defaults.add("Hyperlink.activeColor", new ColorUIResource(255, 51, 0));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\HyperlinkAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */