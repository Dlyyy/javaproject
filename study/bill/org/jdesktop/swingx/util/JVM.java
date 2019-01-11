/*     */ package org.jdesktop.swingx.util;
/*     */ 
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.UIManager.LookAndFeelInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JVM
/*     */ {
/*     */   public static final int JDK1_0 = 10;
/*     */   public static final int JDK1_1 = 11;
/*     */   public static final int JDK1_2 = 12;
/*     */   public static final int JDK1_3 = 13;
/*     */   public static final int JDK1_4 = 14;
/*     */   public static final int JDK1_5 = 15;
/*     */   public static final int JDK1_6 = 16;
/*     */   public static final int JDK1_6N = 1610;
/*     */   public static final int JDK1_7 = 17;
/*  43 */   private static JVM current = new JVM();
/*     */   
/*     */   private int jdkVersion;
/*     */   
/*     */ 
/*     */   public static JVM current()
/*     */   {
/*  50 */     return current;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JVM()
/*     */   {
/*  61 */     this(System.getProperty("java.version"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JVM(String p_JavaVersion)
/*     */   {
/*  68 */     if (p_JavaVersion.startsWith("1.7.")) {
/*  69 */       this.jdkVersion = 17;
/*  70 */     } else if (p_JavaVersion.startsWith("1.6.")) {
/*  71 */       for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/*  72 */         if ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel".equals(info.getClassName())) {
/*  73 */           this.jdkVersion = 1610;
/*  74 */           break;
/*     */         }
/*     */       }
/*     */       
/*  78 */       this.jdkVersion = (this.jdkVersion == 0 ? 16 : this.jdkVersion);
/*  79 */     } else if (p_JavaVersion.startsWith("1.5.")) {
/*  80 */       this.jdkVersion = 15;
/*  81 */     } else if (p_JavaVersion.startsWith("1.4.")) {
/*  82 */       this.jdkVersion = 14;
/*  83 */     } else if (p_JavaVersion.startsWith("1.3.")) {
/*  84 */       this.jdkVersion = 13;
/*  85 */     } else if (p_JavaVersion.startsWith("1.2.")) {
/*  86 */       this.jdkVersion = 12;
/*  87 */     } else if (p_JavaVersion.startsWith("1.1.")) {
/*  88 */       this.jdkVersion = 11;
/*  89 */     } else if (p_JavaVersion.startsWith("1.0.")) {
/*  90 */       this.jdkVersion = 10;
/*     */     }
/*     */     else {
/*  93 */       this.jdkVersion = 13;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOrLater(int p_Version) {
/*  98 */     return this.jdkVersion >= p_Version;
/*     */   }
/*     */   
/*     */   public boolean isOneDotOne() {
/* 102 */     return this.jdkVersion == 11;
/*     */   }
/*     */   
/*     */   public boolean isOneDotTwo() {
/* 106 */     return this.jdkVersion == 12;
/*     */   }
/*     */   
/*     */   public boolean isOneDotThree() {
/* 110 */     return this.jdkVersion == 13;
/*     */   }
/*     */   
/*     */   public boolean isOneDotFour() {
/* 114 */     return this.jdkVersion == 14;
/*     */   }
/*     */   
/*     */   public boolean isOneDotFive() {
/* 118 */     return this.jdkVersion == 15;
/*     */   }
/*     */   
/*     */   public boolean isOneDotSix() {
/* 122 */     return this.jdkVersion == 16;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOneDotSixUpdateN()
/*     */   {
/* 132 */     return this.jdkVersion == 1610;
/*     */   }
/*     */   
/*     */   public boolean isOneDotSeven() {
/* 136 */     return this.jdkVersion == 17;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\JVM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */