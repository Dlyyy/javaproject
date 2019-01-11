/*     */ package org.jdesktop.swingx.util;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OS
/*     */ {
/*     */   private static final boolean osIsMacOsX;
/*     */   private static final boolean osIsWindows;
/*     */   private static final boolean osIsWindowsXP;
/*     */   private static final boolean osIsWindows2003;
/*     */   private static final boolean osIsWindowsVista;
/*     */   private static final boolean osIsLinux;
/*     */   
/*     */   static
/*     */   {
/*  40 */     String os = System.getProperty("os.name");
/*  41 */     if (os != null) {
/*  42 */       os = os.toLowerCase();
/*     */     }
/*  44 */     osIsMacOsX = "mac os x".equals(os);
/*  45 */     osIsWindows = (os != null) && (os.indexOf("windows") != -1);
/*  46 */     osIsWindowsXP = "windows xp".equals(os);
/*  47 */     osIsWindows2003 = "windows 2003".equals(os);
/*  48 */     osIsWindowsVista = "windows vista".equals(os);
/*  49 */     osIsLinux = (os != null) && (os.indexOf("linux") != -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isMacOSX()
/*     */   {
/*  56 */     return osIsMacOsX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWindows()
/*     */   {
/*  63 */     return osIsWindows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWindowsXP()
/*     */   {
/*  70 */     return osIsWindowsXP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWindows2003()
/*     */   {
/*  77 */     return osIsWindows2003;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWindowsVista()
/*     */   {
/*  84 */     return osIsWindowsVista;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isLinux()
/*     */   {
/*  91 */     return osIsLinux;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isUsingWindowsVisualStyles()
/*     */   {
/*  99 */     if (!isWindows()) {
/* 100 */       return false;
/*     */     }
/*     */     
/* 103 */     boolean xpthemeActive = Boolean.TRUE.equals(Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.themeActive"));
/*     */     
/* 105 */     if (!xpthemeActive) {
/* 106 */       return false;
/*     */     }
/*     */     try {
/* 109 */       return System.getProperty("swing.noxp") == null;
/*     */     } catch (RuntimeException e) {}
/* 111 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getWindowsVisualStyle()
/*     */   {
/* 126 */     String style = UIManager.getString("win.xpstyle.name");
/* 127 */     if (style == null)
/*     */     {
/*     */ 
/*     */ 
/* 131 */       style = (String)Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.colorName");
/*     */     }
/*     */     
/* 134 */     return style;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\OS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */