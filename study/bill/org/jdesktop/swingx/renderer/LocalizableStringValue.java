/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*    */ import org.jdesktop.swingx.util.Contract;
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
/*    */ public class LocalizableStringValue
/*    */   implements StringValue
/*    */ {
/*    */   private Map<Object, String> lookup;
/*    */   private Locale locale;
/*    */   
/*    */   public LocalizableStringValue(Map<Object, String> lookup)
/*    */   {
/* 26 */     this(lookup, null);
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
/*    */   public LocalizableStringValue(Map<Object, String> lookup, Locale locale)
/*    */   {
/* 39 */     this.lookup = ((Map)Contract.asNotNull(lookup, "map must not be null"));
/* 40 */     setLocale(locale);
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
/*    */   public String getString(Object value)
/*    */   {
/* 54 */     String key = (String)this.lookup.get(value);
/* 55 */     if (key != null) {
/* 56 */       String text = UIManagerExt.getString(key, getLocale());
/* 57 */       if (text != null)
/* 58 */         return text;
/*    */     }
/* 60 */     return StringValues.TO_STRING_UI.getString(value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void setLocale(Locale locale)
/*    */   {
/* 72 */     this.locale = locale;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Locale getLocale()
/*    */   {
/* 82 */     return this.locale != null ? this.locale : Locale.getDefault();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\LocalizableStringValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */