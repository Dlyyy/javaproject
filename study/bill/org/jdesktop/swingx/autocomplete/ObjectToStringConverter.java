/*     */ package org.jdesktop.swingx.autocomplete;
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
/*     */ public abstract class ObjectToStringConverter
/*     */ {
/*     */   public String[] getPossibleStringsForItem(Object item)
/*     */   {
/*  82 */     String preferred = getPreferredStringForItem(item);
/*  83 */     return new String[] { preferred == null ? new String[0] : preferred };
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
/*     */ 
/*  97 */   public static final ObjectToStringConverter DEFAULT_IMPLEMENTATION = new DefaultObjectToStringConverter(null);
/*     */   
/*     */   public abstract String getPreferredStringForItem(Object paramObject);
/*     */   
/*     */   private static class DefaultObjectToStringConverter extends ObjectToStringConverter {
/* 102 */     public String getPreferredStringForItem(Object item) { return item == null ? null : item.toString(); }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\ObjectToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */