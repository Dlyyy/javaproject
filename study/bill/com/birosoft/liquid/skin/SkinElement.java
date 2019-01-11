/*    */ package com.birosoft.liquid.skin;
/*    */ 
/*    */ import java.awt.Image;
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
/*    */ public class SkinElement
/*    */ {
/*    */   private String filename;
/*    */   private Image image;
/*    */   
/*    */   public SkinElement(String filename, boolean useAutomaticBitmap)
/*    */   {
/* 24 */     this.filename = filename;
/*    */     
/* 26 */     if (useAutomaticBitmap) {
/* 27 */       this.image = SkinImageCache.getInstance().getAutomaticImage(filename);
/*    */     } else {
/* 29 */       this.image = SkinImageCache.getInstance().getImage(filename);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getFilename()
/*    */   {
/* 38 */     return this.filename;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setFilename(String filename)
/*    */   {
/* 46 */     this.filename = filename;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Image getImage()
/*    */   {
/* 54 */     return this.image;
/*    */   }
/*    */   
/*    */   protected void setImage(Image image) {
/* 58 */     this.image = image;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\skin\SkinElement.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */