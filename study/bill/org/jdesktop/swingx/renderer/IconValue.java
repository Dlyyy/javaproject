/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.swing.Icon;
/*    */ import org.jdesktop.swingx.icon.EmptyIcon;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface IconValue
/*    */   extends Serializable
/*    */ {
/*    */   public abstract Icon getIcon(Object paramObject);
/*    */   
/*    */   public static enum IconType
/*    */   {
/* 56 */     LEAF, 
/*    */     
/* 58 */     OPEN_FOLDER, 
/*    */     
/* 60 */     CLOSED_FOLDER;
/*    */     
/*    */ 
/*    */ 
/*    */     private IconType() {}
/*    */   }
/*    */   
/*    */ 
/* 68 */   public static final Icon NULL_ICON = new EmptyIcon();
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\IconValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */