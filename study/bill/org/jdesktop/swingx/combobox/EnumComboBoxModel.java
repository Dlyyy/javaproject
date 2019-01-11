/*     */ package org.jdesktop.swingx.combobox;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class EnumComboBoxModel<E extends Enum<E>>
/*     */   extends ListComboBoxModel<E>
/*     */ {
/*     */   private static final long serialVersionUID = 2176566393195371004L;
/*     */   private final Map<String, E> valueMap;
/*     */   private final Class<E> enumClass;
/*     */   
/*     */   public EnumComboBoxModel(Class<E> en)
/*     */   {
/* 113 */     super(new ArrayList(EnumSet.allOf(en)));
/*     */     
/*     */ 
/* 116 */     this.valueMap = new HashMap();
/* 117 */     this.enumClass = en;
/*     */     
/* 119 */     Iterator<E> iter = this.data.iterator();
/*     */     
/* 121 */     while (iter.hasNext()) {
/* 122 */       E element = (Enum)iter.next();
/* 123 */       String s = element.toString();
/*     */       
/* 125 */       if (this.valueMap.containsKey(s)) {
/* 126 */         throw new IllegalArgumentException("multiple constants map to one string value");
/*     */       }
/*     */       
/*     */ 
/* 130 */       this.valueMap.put(s, element);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectedItem(Object anItem)
/*     */   {
/* 140 */     E input = null;
/*     */     
/* 142 */     if (this.enumClass.isInstance(anItem)) {
/* 143 */       input = (Enum)anItem;
/*     */     } else {
/* 145 */       input = (Enum)this.valueMap.get(anItem);
/*     */     }
/*     */     
/* 148 */     if ((input != null) || (anItem == null)) {
/* 149 */       this.selected = input;
/*     */     }
/*     */     
/* 152 */     fireContentsChanged(this, 0, getSize());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\combobox\EnumComboBoxModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */