/*    */ package com.birosoft.liquid.util;
/*    */ 
/*    */ import java.awt.FontMetrics;
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
/*    */ public class LiquidUtilities
/*    */ {
/*    */   public static final String CLIP_STRING = "...";
/*    */   
/*    */   public static int stringWidth(FontMetrics fm, String string)
/*    */   {
/* 34 */     return fm.stringWidth(string);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String clipStringIfNecessary(FontMetrics fm, String string, int availWidth)
/*    */   {
/* 46 */     if ((string == null) || (string.length() == 0)) {
/* 47 */       return "";
/*    */     }
/*    */     
/* 50 */     int textWidth = stringWidth(fm, string);
/*    */     
/* 52 */     if (textWidth > availWidth) {
/* 53 */       return clipString(fm, string, availWidth);
/*    */     }
/* 55 */     return string;
/*    */   }
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
/*    */   public static String clipString(FontMetrics fm, String string, int availWidth)
/*    */   {
/* 70 */     int clipWidth = stringWidth(fm, "...");
/*    */     
/* 72 */     int chars = 0;
/* 73 */     for (int maxLength = string.length(); chars < maxLength; chars++) {
/* 74 */       clipWidth += fm.charWidth(string.charAt(chars));
/*    */       
/* 76 */       if (clipWidth > availWidth) {
/*    */         break;
/*    */       }
/*    */     }
/* 80 */     string = string.substring(0, chars) + "...";
/*    */     
/* 82 */     return string;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\util\LiquidUtilities.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */