/*     */ package org.jdesktop.swingx.sort;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
/*     */ import org.jdesktop.swingx.renderer.StringValues;
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
/*     */ public final class StringValueRegistry
/*     */   implements StringValueProvider
/*     */ {
/*  44 */   private static final Logger LOG = Logger.getLogger(StringValueRegistry.class.getName());
/*     */   
/*     */ 
/*     */   private Map<Class<?>, StringValue> perClass;
/*     */   
/*     */   private HashMap<Integer, StringValue> perColumn;
/*     */   
/*     */   private HashMap<Integer, Class<?>> classPerColumn;
/*     */   
/*     */ 
/*     */   public StringValue getStringValue(int row, int column)
/*     */   {
/*  56 */     StringValue sv = (StringValue)getPerColumnMap().get(Integer.valueOf(column));
/*  57 */     if (sv == null) {
/*  58 */       sv = getStringValueByClass(getClass(row, column));
/*     */     }
/*  60 */     if (sv == null) {
/*  61 */       sv = getStringValueByClass(Object.class);
/*     */     }
/*  63 */     return sv != null ? sv : StringValues.TO_STRING;
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
/*     */   public void setStringValue(StringValue sv, int column)
/*     */   {
/*  77 */     getPerColumnMap().put(Integer.valueOf(column), sv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearColumnStringValues()
/*     */   {
/*  85 */     getPerColumnMap().clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStringValue(StringValue sv, Class<?> clazz)
/*     */   {
/*  97 */     getPerClassMap().put(clazz, sv);
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
/*     */   public StringValue getStringValue(Class<?> clazz)
/*     */   {
/* 111 */     return (StringValue)getPerClassMap().get(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setColumnClass(Class<?> clazz, int column)
/*     */   {
/* 120 */     getColumnClassMap().put(Integer.valueOf(column), clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setColumnClasses(Map<Integer, Class<?>> classPerColumn)
/*     */   {
/* 127 */     this.classPerColumn = (classPerColumn != null ? new HashMap(classPerColumn) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StringValue getStringValueByClass(Class<?> clazz)
/*     */   {
/* 137 */     if (clazz == null) return null;
/* 138 */     StringValue sv = (StringValue)getPerClassMap().get(clazz);
/* 139 */     if (sv != null) return sv;
/* 140 */     return getStringValueByClass(clazz.getSuperclass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<?> getClass(int row, int column)
/*     */   {
/* 151 */     Class<?> clazz = (Class)getColumnClassMap().get(Integer.valueOf(column));
/* 152 */     return clazz != null ? clazz : Object.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<Integer, Class<?>> getColumnClassMap()
/*     */   {
/* 162 */     if (this.classPerColumn == null) {
/* 163 */       this.classPerColumn = new HashMap();
/*     */     }
/* 165 */     return this.classPerColumn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<Class<?>, StringValue> getPerClassMap()
/*     */   {
/* 175 */     if (this.perClass == null) {
/* 176 */       this.perClass = new HashMap();
/*     */     }
/* 178 */     return this.perClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<Integer, StringValue> getPerColumnMap()
/*     */   {
/* 188 */     if (this.perColumn == null) {
/* 189 */       this.perColumn = new HashMap();
/*     */     }
/* 191 */     return this.perColumn;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\StringValueRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */