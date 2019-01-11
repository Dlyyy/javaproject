/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface HighlightPredicate
/*     */ {
/*  80 */   public static final HighlightPredicate ALWAYS = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/*     */ 
/*  88 */       return true;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   public static final HighlightPredicate NEVER = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/*     */ 
/* 104 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   public static final HighlightPredicate ROLLOVER_ROW = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 123 */       if (!adapter.getComponent().isEnabled()) return false;
/* 124 */       Point p = (Point)adapter.getComponent().getClientProperty("swingx.rollover");
/*     */       
/* 126 */       return (p != null) && (p.y == adapter.row);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   public static final HighlightPredicate ROLLOVER_COLUMN = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 145 */       if (!adapter.getComponent().isEnabled()) return false;
/* 146 */       Point p = (Point)adapter.getComponent().getClientProperty("swingx.rollover");
/*     */       
/* 148 */       return (p != null) && (p.x == adapter.column);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 155 */   public static final HighlightPredicate ROLLOVER_CELL = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 166 */       if (!adapter.getComponent().isEnabled()) return false;
/* 167 */       Point p = (Point)adapter.getComponent().getClientProperty("swingx.rollover");
/*     */       
/* 169 */       return (p != null) && (p.y == adapter.row) && (p.x == adapter.column);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */   public static final HighlightPredicate EDITABLE = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/* 184 */       return adapter.isEditable();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 191 */   public static final HighlightPredicate READ_ONLY = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/* 198 */       return !adapter.isEditable();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 205 */   public static final HighlightPredicate IS_LEAF = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/* 212 */       return adapter.isLeaf();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 219 */   public static final HighlightPredicate IS_FOLDER = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/* 226 */       return !adapter.isLeaf();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 233 */   public static final HighlightPredicate IS_SELECTED = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 238 */       return adapter.isSelected();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 248 */   public static final HighlightPredicate IS_TEXT_TRUNCATED = new HighlightPredicate()
/*     */   {
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
/* 251 */       JComponent c = (renderer instanceof JComponent) ? (JComponent)renderer : null;
/* 252 */       String text = adapter.getString();
/* 253 */       Icon icon = null;
/*     */       
/* 255 */       int verticalAlignment = 0;
/* 256 */       int horizontalAlignment = 10;
/* 257 */       int verticalTextPosition = 0;
/* 258 */       int horizontalTextPosition = 11;
/* 259 */       int gap = 0;
/*     */       
/* 261 */       if ((renderer instanceof JLabel)) {
/* 262 */         icon = ((JLabel)renderer).getIcon();
/* 263 */         gap = ((JLabel)renderer).getIconTextGap();
/* 264 */       } else if ((renderer instanceof AbstractButton)) {
/* 265 */         icon = ((AbstractButton)renderer).getIcon();
/* 266 */         gap = ((AbstractButton)renderer).getIconTextGap();
/*     */       }
/*     */       
/* 269 */       String result = SwingUtilities.layoutCompoundLabel(c, renderer.getFontMetrics(renderer.getFont()), text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, adapter.getCellBounds(), new Rectangle(), new Rectangle(), gap);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 274 */       return !text.equals(result);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 281 */   public static final HighlightPredicate HAS_FOCUS = new HighlightPredicate()
/*     */   {
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/*     */ 
/* 288 */       return adapter.hasFocus();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 298 */   public static final HighlightPredicate EVEN = new HighlightPredicate()
/*     */   {
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
/* 301 */       return adapter.row % 2 == 0;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 313 */   public static final HighlightPredicate ODD = new HighlightPredicate()
/*     */   {
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
/* 316 */       return !EVEN.isHighlighted(renderer, adapter);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 324 */   public static final HighlightPredicate BIG_DECIMAL_NEGATIVE = new HighlightPredicate()
/*     */   {
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 328 */       return ((adapter.getValue() instanceof BigDecimal)) && (((BigDecimal)adapter.getValue()).compareTo(BigDecimal.ZERO) < 0);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 337 */   public static final HighlightPredicate INTEGER_NEGATIVE = new HighlightPredicate()
/*     */   {
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 341 */       return ((adapter.getValue() instanceof Number)) && (((Number)adapter.getValue()).intValue() < 0);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 348 */   public static final HighlightPredicate[] EMPTY_PREDICATE_ARRAY = new HighlightPredicate[0];
/* 349 */   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/* 350 */   public static final Integer[] EMPTY_INTEGER_ARRAY = new Integer[0];
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean isHighlighted(Component paramComponent, ComponentAdapter paramComponentAdapter);
/*     */   
/*     */ 
/*     */ 
/*     */   public static class NotHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private HighlightPredicate predicate;
/*     */     
/*     */ 
/*     */ 
/*     */     public NotHighlightPredicate(HighlightPredicate predicate)
/*     */     {
/* 367 */       if (predicate == null)
/* 368 */         throw new NullPointerException("predicate must not be null");
/* 369 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 377 */       return !this.predicate.isHighlighted(renderer, adapter);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public HighlightPredicate getHighlightPredicate()
/*     */     {
/* 384 */       return this.predicate;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class AndHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private List<HighlightPredicate> predicate;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public AndHighlightPredicate(HighlightPredicate... predicate)
/*     */     {
/* 402 */       this.predicate = Arrays.asList((Object[])Contract.asNotNull(predicate, "predicate must not be null"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public AndHighlightPredicate(Collection<HighlightPredicate> list)
/*     */     {
/* 411 */       this.predicate = new ArrayList((Collection)Contract.asNotNull(list, "predicate list must not be null"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 420 */       for (HighlightPredicate hp : this.predicate) {
/* 421 */         if (!hp.isHighlighted(renderer, adapter)) return false;
/*     */       }
/* 423 */       return !this.predicate.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public HighlightPredicate[] getHighlightPredicates()
/*     */     {
/* 430 */       if (this.predicate.isEmpty()) return EMPTY_PREDICATE_ARRAY;
/* 431 */       return (HighlightPredicate[])this.predicate.toArray(new HighlightPredicate[this.predicate.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class OrHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private List<HighlightPredicate> predicate;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public OrHighlightPredicate(HighlightPredicate... predicate)
/*     */     {
/* 450 */       this.predicate = Arrays.asList((Object[])Contract.asNotNull(predicate, "predicate must not be null"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public OrHighlightPredicate(Collection<HighlightPredicate> list)
/*     */     {
/* 459 */       this.predicate = new ArrayList((Collection)Contract.asNotNull(list, "predicate list must not be null"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 468 */       for (HighlightPredicate hp : this.predicate) {
/* 469 */         if (hp.isHighlighted(renderer, adapter)) return true;
/*     */       }
/* 471 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public HighlightPredicate[] getHighlightPredicates()
/*     */     {
/* 477 */       if (this.predicate.isEmpty()) return EMPTY_PREDICATE_ARRAY;
/* 478 */       return (HighlightPredicate[])this.predicate.toArray(new HighlightPredicate[this.predicate.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class RowGroupHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private int linesPerGroup;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public RowGroupHighlightPredicate(int linesPerGroup)
/*     */     {
/* 497 */       if (linesPerGroup < 1)
/* 498 */         throw new IllegalArgumentException("a group contain at least 1 row, was: " + linesPerGroup);
/* 499 */       this.linesPerGroup = linesPerGroup;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 509 */       return adapter.row / this.linesPerGroup % 2 == 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getLinesPerGroup()
/*     */     {
/* 517 */       return this.linesPerGroup;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ColumnHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     List<Integer> columnList;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ColumnHighlightPredicate(int... columns)
/*     */     {
/* 536 */       this.columnList = new ArrayList();
/* 537 */       for (int i = 0; i < columns.length; i++) {
/* 538 */         this.columnList.add(Integer.valueOf(columns[i]));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 550 */       int modelIndex = adapter.convertColumnIndexToModel(adapter.column);
/* 551 */       return this.columnList.contains(Integer.valueOf(modelIndex));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Integer[] getColumns()
/*     */     {
/* 560 */       if (this.columnList.isEmpty()) return EMPTY_INTEGER_ARRAY;
/* 561 */       return (Integer[])this.columnList.toArray(new Integer[this.columnList.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class IdentifierHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     List<Object> columnList;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public IdentifierHighlightPredicate(Object... columns)
/*     */     {
/* 581 */       this.columnList = new ArrayList();
/* 582 */       for (int i = 0; i < columns.length; i++) {
/* 583 */         this.columnList.add(columns[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 595 */       int modelIndex = adapter.convertColumnIndexToModel(adapter.column);
/* 596 */       Object identifier = adapter.getColumnIdentifierAt(modelIndex);
/* 597 */       return identifier != null ? this.columnList.contains(identifier) : false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Object[] getIdentifiers()
/*     */     {
/* 604 */       if (this.columnList.isEmpty()) return EMPTY_OBJECT_ARRAY;
/* 605 */       return this.columnList.toArray(new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class DepthHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private List<Integer> depthList;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public DepthHighlightPredicate(int... depths)
/*     */     {
/* 627 */       this.depthList = new ArrayList();
/* 628 */       for (int i = 0; i < depths.length; i++) {
/* 629 */         this.depthList.add(Integer.valueOf(depths[i]));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 642 */       int depth = adapter.getDepth();
/* 643 */       return this.depthList.contains(Integer.valueOf(depth));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Integer[] getDepths()
/*     */     {
/* 650 */       if (this.depthList.isEmpty()) return EMPTY_INTEGER_ARRAY;
/* 651 */       return (Integer[])this.depthList.toArray(new Integer[this.depthList.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class EqualsHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private Object compareValue;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public EqualsHighlightPredicate()
/*     */     {
/* 671 */       this(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public EqualsHighlightPredicate(Object compareValue)
/*     */     {
/* 680 */       this.compareValue = compareValue;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 690 */       if (this.compareValue == null) return adapter.getValue() == null;
/* 691 */       return this.compareValue.equals(adapter.getValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Object getCompareValue()
/*     */     {
/* 698 */       return this.compareValue;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class TypeHighlightPredicate
/*     */     implements HighlightPredicate
/*     */   {
/*     */     private Class<?> clazz;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public TypeHighlightPredicate()
/*     */     {
/* 717 */       this(Object.class);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public TypeHighlightPredicate(Class<?> compareValue)
/*     */     {
/* 728 */       this.clazz = compareValue;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */     {
/* 738 */       return adapter.getValue() != null ? this.clazz.isAssignableFrom(adapter.getValue().getClass()) : false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Class<?> getType()
/*     */     {
/* 745 */       return this.clazz;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\HighlightPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */