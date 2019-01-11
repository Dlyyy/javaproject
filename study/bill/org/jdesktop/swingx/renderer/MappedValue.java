/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import javax.swing.Icon;
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
/*    */ public class MappedValue
/*    */   implements StringValue, IconValue, BooleanValue
/*    */ {
/*    */   private StringValue stringDelegate;
/*    */   private IconValue iconDelegate;
/*    */   private BooleanValue booleanDelegate;
/*    */   
/*    */   public MappedValue(StringValue stringDelegate, IconValue iconDelegate)
/*    */   {
/* 43 */     this(stringDelegate, iconDelegate, null);
/*    */   }
/*    */   
/*    */   public MappedValue(StringValue stringDelegate, IconValue iconDelegate, BooleanValue booleanDelegate)
/*    */   {
/* 48 */     this.stringDelegate = stringDelegate;
/* 49 */     this.iconDelegate = iconDelegate;
/* 50 */     this.booleanDelegate = booleanDelegate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getString(Object value)
/*    */   {
/* 61 */     if (this.stringDelegate != null) {
/* 62 */       return this.stringDelegate.getString(value);
/*    */     }
/* 64 */     return "";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Icon getIcon(Object value)
/*    */   {
/* 75 */     if (this.iconDelegate != null) {
/* 76 */       return this.iconDelegate.getIcon(value);
/*    */     }
/* 78 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean getBoolean(Object value)
/*    */   {
/* 89 */     if (this.booleanDelegate != null) {
/* 90 */       return this.booleanDelegate.getBoolean(value);
/*    */     }
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\MappedValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */