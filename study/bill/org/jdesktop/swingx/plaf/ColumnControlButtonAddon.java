/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import javax.swing.plaf.InsetsUIResource;
/*    */ import org.jdesktop.swingx.icon.ColumnControlIcon;
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
/*    */ public class ColumnControlButtonAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public ColumnControlButtonAddon()
/*    */   {
/* 38 */     super("ColumnControlButton");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 44 */     super.addBasicDefaults(addon, defaults);
/* 45 */     defaults.add("ColumnControlButton.actionIcon", new ColumnControlIcon());
/* 46 */     defaults.add("ColumnControlButton.margin", new InsetsUIResource(1, 2, 2, 1));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\ColumnControlButtonAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */