/*    */ package com.birosoft.liquid.skin;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Image;
/*    */ import java.awt.Label;
/*    */ import java.awt.MediaTracker;
/*    */ import java.awt.Toolkit;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.InputStream;
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
/*    */ public class SecretLoader
/*    */ {
/* 23 */   static Component component = new Label();
/* 24 */   static byte[] buffer = new byte['က'];
/*    */   
/*    */ 
/*    */   static Image loadImage(String fileName)
/*    */   {
/* 29 */     byte[] byteArray = null;
/*    */     
/*    */     try
/*    */     {
/* 33 */       InputStream fis = SecretLoader.class.getResourceAsStream("/com/birosoft/liquid/icons/" + fileName);
/* 34 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*    */       
/*    */ 
/*    */ 
/* 38 */       int read = fis.read(buffer);
/* 39 */       while (read != -1)
/*    */       {
/* 41 */         bos.write(buffer, 0, read);
/* 42 */         read = fis.read(buffer);
/*    */       }
/*    */       
/* 45 */       byteArray = bos.toByteArray();
/* 46 */       read = fis.read(byteArray);
/*    */       
/* 48 */       Image img = Toolkit.getDefaultToolkit().createImage(byteArray, 0, byteArray.length);
/*    */       
/* 50 */       MediaTracker tracker = new MediaTracker(component);
/* 51 */       tracker.addImage(img, 0);
/*    */       try
/*    */       {
/* 54 */         tracker.waitForID(0);
/*    */       }
/*    */       catch (InterruptedException ignore) {}
/*    */       
/* 58 */       return img;
/*    */     }
/*    */     catch (Throwable t) {
/* 61 */       throw new IllegalArgumentException("File " + fileName + " could not be loaded.");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\skin\SecretLoader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */