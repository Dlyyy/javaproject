/*     */ package org.jdesktop.swingx.combobox;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapComboBoxModel<K, V>
/*     */   extends ListComboBoxModel<K>
/*     */ {
/*     */   protected Map<K, V> map_data;
/*     */   
/*     */   public MapComboBoxModel()
/*     */   {
/*  55 */     this(new LinkedHashMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapComboBoxModel(Map<K, V> map)
/*     */   {
/*  65 */     super(buildIndex(map));
/*     */     
/*  67 */     this.map_data = map;
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
/*     */   private static <E> List<E> buildIndex(Map<E, ?> map)
/*     */   {
/*  83 */     return new ArrayList(map.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSize()
/*     */   {
/*  91 */     return this.map_data.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 100 */     if (evt.getActionCommand().equals("update"))
/*     */     {
/* 102 */       Set<K> keys = this.map_data.keySet();
/* 103 */       keys.removeAll(this.data);
/* 104 */       this.data.addAll(keys);
/*     */       
/*     */ 
/* 107 */       List<K> copy = new ArrayList(this.data);
/* 108 */       keys = this.map_data.keySet();
/* 109 */       copy.removeAll(keys);
/* 110 */       this.data.removeAll(copy);
/*     */       
/* 112 */       fireContentsChanged(this, 0, getSize() - 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public V getValue(Object selectedItem)
/*     */   {
/* 124 */     return (V)this.map_data.get(selectedItem);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public V getValue(int selectedItem)
/*     */   {
/* 135 */     return (V)getValue(getElementAt(selectedItem));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\combobox\MapComboBoxModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */