/*    */ package org.jdesktop.swingx.calendar;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.SortedSet;
/*    */ import org.jdesktop.swingx.event.DateSelectionListener;
/*    */ 
/*    */ public abstract interface DateSelectionModel
/*    */ {
/*    */   public abstract SelectionMode getSelectionMode();
/*    */   
/*    */   public abstract void setSelectionMode(SelectionMode paramSelectionMode);
/*    */   
/*    */   public abstract java.util.Calendar getCalendar();
/*    */   
/*    */   public abstract int getFirstDayOfWeek();
/*    */   
/*    */   public abstract void setFirstDayOfWeek(int paramInt);
/*    */   
/*    */   public abstract int getMinimalDaysInFirstWeek();
/*    */   
/*    */   public abstract void setMinimalDaysInFirstWeek(int paramInt);
/*    */   
/*    */   public abstract java.util.TimeZone getTimeZone();
/*    */   
/*    */   public abstract void setTimeZone(java.util.TimeZone paramTimeZone);
/*    */   
/*    */   public abstract java.util.Locale getLocale();
/*    */   
/*    */   public abstract void setLocale(java.util.Locale paramLocale);
/*    */   
/*    */   public abstract void addSelectionInterval(Date paramDate1, Date paramDate2);
/*    */   
/*    */   public abstract void setSelectionInterval(Date paramDate1, Date paramDate2);
/*    */   
/*    */   public abstract void removeSelectionInterval(Date paramDate1, Date paramDate2);
/*    */   
/*    */   public abstract void clearSelection();
/*    */   
/*    */   public abstract SortedSet<Date> getSelection();
/*    */   
/*    */   public static enum SelectionMode
/*    */   {
/* 43 */     SINGLE_SELECTION, 
/*    */     
/*    */ 
/*    */ 
/* 47 */     SINGLE_INTERVAL_SELECTION, 
/*    */     
/*    */ 
/*    */ 
/* 51 */     MULTIPLE_INTERVAL_SELECTION;
/*    */     
/*    */     private SelectionMode() {}
/*    */   }
/*    */   
/*    */   public abstract Date getFirstSelectionDate();
/*    */   
/*    */   public abstract Date getLastSelectionDate();
/*    */   
/*    */   public abstract boolean isSelected(Date paramDate);
/*    */   
/*    */   public abstract Date getNormalizedDate(Date paramDate);
/*    */   
/*    */   public abstract boolean isSelectionEmpty();
/*    */   
/*    */   public abstract SortedSet<Date> getUnselectableDates();
/*    */   
/*    */   public abstract void setUnselectableDates(SortedSet<Date> paramSortedSet);
/*    */   
/*    */   public abstract boolean isUnselectableDate(Date paramDate);
/*    */   
/*    */   public abstract Date getUpperBound();
/*    */   
/*    */   public abstract void setUpperBound(Date paramDate);
/*    */   
/*    */   public abstract Date getLowerBound();
/*    */   
/*    */   public abstract void setLowerBound(Date paramDate);
/*    */   
/*    */   public abstract void setAdjusting(boolean paramBoolean);
/*    */   
/*    */   public abstract boolean isAdjusting();
/*    */   
/*    */   public abstract void addDateSelectionListener(DateSelectionListener paramDateSelectionListener);
/*    */   
/*    */   public abstract void removeDateSelectionListener(DateSelectionListener paramDateSelectionListener);
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\DateSelectionModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */