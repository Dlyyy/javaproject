/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.text.DateFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Locale;
/*     */ import javax.swing.filechooser.FileSystemView;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.util.Contract;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringValues
/*     */ {
/*  45 */   public static final StringValue EMPTY = new StringValue() {
/*     */     public String getString(Object value) {
/*  47 */       return "";
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   public static final StringValue TO_STRING = new StringValue() {
/*     */     public String getString(Object value) {
/*  59 */       return value != null ? value.toString() : StringValues.EMPTY.getString(value);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   public static final StringValue FILE_NAME = new StringValue() {
/*     */     public String getString(Object value) {
/*  71 */       if ((value instanceof File)) {
/*  72 */         FileSystemView fsv = FileSystemView.getFileSystemView();
/*     */         
/*  74 */         return fsv.getSystemDisplayName((File)value);
/*     */       }
/*     */       
/*  77 */       return StringValues.TO_STRING.getString(value);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   public static final StringValue FILE_TYPE = new StringValue() {
/*     */     public String getString(Object value) {
/*  89 */       if ((value instanceof File)) {
/*  90 */         FileSystemView fsv = FileSystemView.getFileSystemView();
/*     */         
/*  92 */         return fsv.getSystemTypeDescription((File)value);
/*     */       }
/*     */       
/*  95 */       return StringValues.TO_STRING.getString(value);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Locale defaultLocale;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean localeChanged()
/*     */   {
/* 110 */     boolean changed = !Locale.getDefault().equals(defaultLocale);
/* 111 */     if (changed) {
/* 112 */       defaultLocale = Locale.getDefault();
/*     */     }
/* 114 */     return changed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   public static final FormatStringValue DATE_TO_STRING = new FormatStringValue()
/*     */   {
/*     */ 
/*     */ 
/*     */     public String getString(Object value)
/*     */     {
/*     */ 
/* 129 */       if ((this.format == null) || (StringValues.access$000())) {
/* 130 */         this.format = DateFormat.getDateInstance();
/*     */       }
/* 132 */       return super.getString(value);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   public static final FormatStringValue NUMBER_TO_STRING = new FormatStringValue()
/*     */   {
/*     */ 
/*     */ 
/*     */     public String getString(Object value)
/*     */     {
/*     */ 
/* 149 */       if ((this.format == null) || (StringValues.access$000())) {
/* 150 */         this.format = NumberFormat.getNumberInstance();
/*     */       }
/* 152 */       return super.getString(value);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 159 */   public static final StringValue TO_STRING_UI = new StringValueUIResource(TO_STRING);
/* 160 */   public static final StringValue EMPTY_UI = new StringValueUIResource(EMPTY);
/*     */   
/*     */ 
/*     */ 
/*     */   public static class StringValueUIResource
/*     */     implements StringValue, UIResource
/*     */   {
/*     */     private StringValue delegate;
/*     */     
/*     */ 
/*     */     public StringValueUIResource(StringValue toString)
/*     */     {
/* 172 */       Contract.asNotNull(toString, "delegate StringValue must not be null");
/* 173 */       this.delegate = toString;
/*     */     }
/*     */     
/*     */     public String getString(Object value)
/*     */     {
/* 178 */       return this.delegate.getString(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\StringValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */