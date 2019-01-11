/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import org.jdesktop.swingx.JXMonthView;
/*     */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CalendarAdapter
/*     */   extends ComponentAdapter
/*     */ {
/*     */   Calendar calendar;
/*     */   CalendarState dayState;
/*     */   
/*     */   public CalendarAdapter(JXMonthView component)
/*     */   {
/*  49 */     super(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CalendarAdapter install(Calendar calendar, CalendarState dayState)
/*     */   {
/*  58 */     this.calendar = calendar;
/*  59 */     this.dayState = dayState;
/*  60 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public JXMonthView getComponent()
/*     */   {
/*  66 */     return (JXMonthView)super.getComponent();
/*     */   }
/*     */   
/*     */   public CalendarState getCalendarState() {
/*  70 */     return this.dayState;
/*     */   }
/*     */   
/*     */   public boolean isFlagged() {
/*  74 */     if ((getComponent() == null) || (this.calendar == null)) {
/*  75 */       return false;
/*     */     }
/*  77 */     return getComponent().isFlaggedDate(this.calendar.getTime());
/*     */   }
/*     */   
/*     */   public boolean isUnselectable() {
/*  81 */     if ((getComponent() == null) || (this.calendar == null) || (!isSelectable())) {
/*  82 */       return false;
/*     */     }
/*  84 */     return getComponent().isUnselectableDate(this.calendar.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSelectable()
/*     */   {
/*  92 */     return (CalendarState.IN_MONTH == getCalendarState()) || (CalendarState.TODAY == getCalendarState());
/*     */   }
/*     */   
/*     */   public boolean isSelected()
/*     */   {
/*  97 */     if ((getComponent() == null) || (this.calendar == null)) {
/*  98 */       return false;
/*     */     }
/* 100 */     return getComponent().isSelected(this.calendar.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getFilteredValueAt(int row, int column)
/*     */   {
/* 106 */     return getValueAt(row, column);
/*     */   }
/*     */   
/*     */   public Object getValueAt(int row, int column)
/*     */   {
/* 111 */     return this.calendar;
/*     */   }
/*     */   
/*     */   public boolean hasFocus()
/*     */   {
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCellEditable(int row, int column)
/*     */   {
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEditable()
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\CalendarAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */