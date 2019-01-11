/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
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
/*     */ public class SingleDaySelectionModel
/*     */   extends AbstractDateSelectionModel
/*     */ {
/*     */   private SortedSet<Date> selectedDates;
/*     */   private SortedSet<Date> unselectableDates;
/*     */   
/*     */   public SingleDaySelectionModel()
/*     */   {
/*  57 */     this(null);
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
/*     */   public SingleDaySelectionModel(Locale locale)
/*     */   {
/*  71 */     super(locale);
/*  72 */     this.selectedDates = new TreeSet();
/*  73 */     this.unselectableDates = new TreeSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateSelectionModel.SelectionMode getSelectionMode()
/*     */   {
/*  80 */     return DateSelectionModel.SelectionMode.SINGLE_SELECTION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectionMode(DateSelectionModel.SelectionMode selectionMode) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 101 */     setSelection(startDate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 111 */     setSelection(startDate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 118 */     Contract.asNotNull(startDate, "date must not be null");
/* 119 */     if (isSelectionEmpty()) return;
/* 120 */     if (isSelectionInInterval(startDate, endDate)) {
/* 121 */       this.selectedDates.clear();
/* 122 */       fireValueChanged(DateSelectionEvent.EventType.DATES_REMOVED);
/*     */     }
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
/*     */   protected boolean isSelectionInInterval(Date startDate, Date endDate)
/*     */   {
/* 139 */     if ((((Date)this.selectedDates.first()).before(startOfDay(startDate))) || (((Date)this.selectedDates.first()).after(endOfDay(endDate))))
/* 140 */       return false;
/* 141 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setSelection(Date date)
/*     */   {
/* 153 */     Contract.asNotNull(date, "date must not be null");
/* 154 */     if (isSelectedStrict(date)) return;
/* 155 */     if (isSelectable(date)) {
/* 156 */       this.selectedDates.clear();
/*     */       
/* 158 */       this.selectedDates.add(date);
/* 159 */       fireValueChanged(DateSelectionEvent.EventType.DATES_SET);
/*     */     }
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
/*     */   private boolean isSelectedStrict(Date date)
/*     */   {
/* 174 */     if (!isSelectionEmpty())
/*     */     {
/* 176 */       return ((Date)this.selectedDates.first()).equals(date);
/*     */     }
/* 178 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getFirstSelectionDate()
/*     */   {
/* 185 */     return isSelectionEmpty() ? null : (Date)this.selectedDates.first();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getLastSelectionDate()
/*     */   {
/* 192 */     return isSelectionEmpty() ? null : (Date)this.selectedDates.last();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSelectable(Date date)
/*     */   {
/* 202 */     if (outOfBounds(date)) return false;
/* 203 */     return !inUnselectables(date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean inUnselectables(Date date)
/*     */   {
/* 211 */     for (Date unselectable : this.unselectableDates) {
/* 212 */       if (isSameDay(unselectable, date)) return true;
/*     */     }
/* 214 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean outOfBounds(Date date)
/*     */   {
/* 225 */     if (belowLowerBound(date)) return true;
/* 226 */     if (aboveUpperBound(date)) return true;
/* 227 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean aboveUpperBound(Date date)
/*     */   {
/* 235 */     if (this.upperBound != null) {
/* 236 */       return endOfDay(this.upperBound).before(date);
/*     */     }
/* 238 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean belowLowerBound(Date date)
/*     */   {
/* 246 */     if (this.lowerBound != null) {
/* 247 */       return startOfDay(this.lowerBound).after(date);
/*     */     }
/* 249 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearSelection()
/*     */   {
/* 257 */     if (isSelectionEmpty()) return;
/* 258 */     this.selectedDates.clear();
/* 259 */     fireValueChanged(DateSelectionEvent.EventType.SELECTION_CLEARED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedSet<Date> getSelection()
/*     */   {
/* 267 */     return new TreeSet(this.selectedDates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSelected(Date date)
/*     */   {
/* 274 */     Contract.asNotNull(date, "date must not be null");
/* 275 */     if (isSelectionEmpty()) return false;
/* 276 */     return isSameDay((Date)this.selectedDates.first(), date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getNormalizedDate(Date date)
/*     */   {
/* 287 */     return new Date(date.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSelectionEmpty()
/*     */   {
/* 295 */     return this.selectedDates.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedSet<Date> getUnselectableDates()
/*     */   {
/* 303 */     return new TreeSet(this.unselectableDates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUnselectableDates(SortedSet<Date> unselectables)
/*     */   {
/* 310 */     Contract.asNotNull(unselectables, "unselectable dates must not be null");
/* 311 */     this.unselectableDates.clear();
/* 312 */     for (Date unselectableDate : unselectables) {
/* 313 */       removeSelectionInterval(unselectableDate, unselectableDate);
/* 314 */       this.unselectableDates.add(unselectableDate);
/*     */     }
/* 316 */     fireValueChanged(DateSelectionEvent.EventType.UNSELECTED_DATES_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUnselectableDate(Date date)
/*     */   {
/* 323 */     return !isSelectable(date);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\SingleDaySelectionModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */