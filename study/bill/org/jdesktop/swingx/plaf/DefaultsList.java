/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
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
/*     */ public final class DefaultsList
/*     */ {
/*     */   private List<Object> delegate;
/*     */   
/*     */   public DefaultsList()
/*     */   {
/*  54 */     this.delegate = new ArrayList();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(Object key, Object value)
/*     */   {
/*  75 */     add(key, value, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(Object key, Object value, boolean enableChecking)
/*     */   {
/* 100 */     if (enableChecking) {
/* 101 */       asUIResource(value, value + " must be a UIResource");
/*     */     }
/*     */     
/* 104 */     if ((value == null) && (this.delegate.contains(key))) {
/* 105 */       int i = this.delegate.indexOf(key);
/*     */       
/* 107 */       this.delegate.remove(i + 1);
/* 108 */       this.delegate.remove(i);
/* 109 */     } else if (value != null) {
/* 110 */       this.delegate.add(Contract.asNotNull(key, "key cannot be null"));
/* 111 */       this.delegate.add(value);
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> T asUIResource(T value, String message)
/*     */   {
/* 117 */     if (!(value instanceof UIResource)) {
/* 118 */       boolean shouldThrow = false;
/*     */       
/* 120 */       shouldThrow |= value instanceof ActionMap;
/* 121 */       shouldThrow |= value instanceof Border;
/* 122 */       shouldThrow |= value instanceof Color;
/* 123 */       shouldThrow |= value instanceof Dimension;
/* 124 */       shouldThrow |= value instanceof Font;
/* 125 */       shouldThrow |= value instanceof Icon;
/* 126 */       shouldThrow |= value instanceof InputMap;
/* 127 */       shouldThrow |= value instanceof Insets;
/* 128 */       shouldThrow |= value instanceof Painter;
/* 129 */       shouldThrow |= value instanceof StringValue;
/*     */       
/* 131 */       if (shouldThrow) {
/* 132 */         throw new IllegalArgumentException(message);
/*     */       }
/*     */     }
/*     */     
/* 136 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 145 */     return this.delegate.toArray();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\DefaultsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */