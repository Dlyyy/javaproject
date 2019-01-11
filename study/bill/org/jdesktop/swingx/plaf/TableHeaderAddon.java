/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.UIDefaults;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;
/*    */ import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;
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
/*    */ public class TableHeaderAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public TableHeaderAddon()
/*    */   {
/* 43 */     super("JXTableHeader");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 49 */     super.addMetalDefaults(addon, defaults);
/* 50 */     String key = "TableHeader.cellBorder";
/* 51 */     Border border = UIManager.getBorder(key);
/* 52 */     if ((border instanceof MetalBorders.TableHeaderBorder)) {
/* 53 */       border = new BorderUIResource.CompoundBorderUIResource(border, BorderFactory.createEmptyBorder());
/*    */       
/*    */ 
/* 56 */       UIManager.getLookAndFeelDefaults().put(key, border);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TableHeaderAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */