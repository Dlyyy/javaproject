/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.logging.Logger;
/*    */ import javax.swing.UIDefaults;
/*    */ import javax.swing.UIManager;
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
/*    */ public class TableAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/* 37 */   private static final Logger LOG = Logger.getLogger(TableAddon.class.getName());
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public TableAddon()
/*    */   {
/* 44 */     super("JXTable");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 50 */     super.addNimbusDefaults(addon, defaults);
/*    */     
/*    */ 
/*    */ 
/* 54 */     if (Boolean.TRUE.equals(UIManager.get("Nimbus.keepAlternateRowColor"))) return;
/* 55 */     Object value = UIManager.getLookAndFeelDefaults().remove("Table.alternateRowColor");
/* 56 */     if ((value instanceof Color)) {
/* 57 */       defaults.add("UIColorHighlighter.stripingBackground", value, false);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TableAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */