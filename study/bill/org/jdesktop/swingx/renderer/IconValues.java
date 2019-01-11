/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import java.io.File;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.filechooser.FileSystemView;
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
/*    */ public final class IconValues
/*    */ {
/* 40 */   public static final IconValue NONE = new IconValue()
/*    */   {
/*    */     public Icon getIcon(Object value) {
/* 43 */       return IconValue.NULL_ICON;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 52 */   public static final IconValue ICON = new IconValue()
/*    */   {
/*    */     public Icon getIcon(Object value) {
/* 55 */       if ((value instanceof Icon)) {
/* 56 */         return (Icon)value;
/*    */       }
/* 58 */       return null;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 68 */   public static final IconValue FILE_ICON = new IconValue() {
/*    */     public Icon getIcon(Object value) {
/* 70 */       if ((value instanceof File)) {
/* 71 */         FileSystemView fsv = FileSystemView.getFileSystemView();
/*    */         
/* 73 */         return fsv.getSystemIcon((File)value);
/*    */       }
/*    */       
/* 76 */       return IconValues.NONE.getIcon(value);
/*    */     }
/*    */   };
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\IconValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */