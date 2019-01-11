/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
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
/*     */ public class DefaultDateSelectionModel
/*     */   extends AbstractDateSelectionModel
/*     */ {
/*     */   private DateSelectionModel.SelectionMode selectionMode;
/*     */   private SortedSet<Date> selectedDates;
/*     */   private SortedSet<Date> unselectableDates;
/*     */   
/*     */   public DefaultDateSelectionModel()
/*     */   {
/*  46 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultDateSelectionModel(Locale locale)
/*     */   {
/*  55 */     super(locale);
/*  56 */     this.selectionMode = DateSelectionModel.SelectionMode.SINGLE_SELECTION;
/*  57 */     this.selectedDates = new TreeSet();
/*  58 */     this.unselectableDates = new TreeSet();
/*     */   }
/*     */   
/*     */ 
/*     */   public DateSelectionModel.SelectionMode getSelectionMode()
/*     */   {
/*  64 */     return this.selectionMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSelectionMode(DateSelectionModel.SelectionMode selectionMode)
/*     */   {
/*  71 */     this.selectionMode = selectionMode;
/*  72 */     clearSelection();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSelectionInterval(Date startDate, Date endDate)
/*     */   {
/*  81 */     if (startDate.after(endDate)) {
/*  82 */       return;
/*     */     }
/*  84 */     boolean added = false;
/*  85 */     switch (this.selectionMode) {
/*     */     case SINGLE_SELECTION: 
/*  87 */       if (isSelected(startDate)) return;
/*  88 */       clearSelectionImpl();
/*  89 */       added = addSelectionImpl(startDate, startDate);
/*  90 */       break;
/*     */     case SINGLE_INTERVAL_SELECTION: 
/*  92 */       if (isIntervalSelected(startDate, endDate)) return;
/*  93 */       clearSelectionImpl();
/*  94 */       added = addSelectionImpl(startDate, endDate);
/*  95 */       break;
/*     */     case MULTIPLE_INTERVAL_SELECTION: 
/*  97 */       if (isIntervalSelected(startDate, endDate)) return;
/*  98 */       added = addSelectionImpl(startDate, endDate);
/*  99 */       break;
/*     */     }
/*     */     
/*     */     
/* 103 */     if (added) {
/* 104 */       fireValueChanged(DateSelectionEvent.EventType.DATES_ADDED);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 112 */     if (DateSelectionModel.SelectionMode.SINGLE_SELECTION.equals(this.selectionMode)) {
/* 113 */       if (isSelected(startDate)) return;
/* 114 */       endDate = startDate;
/*     */     }
/* 116 */     else if (isIntervalSelected(startDate, endDate)) { return;
/*     */     }
/* 118 */     clearSelectionImpl();
/* 119 */     if (addSelectionImpl(startDate, endDate)) {
/* 120 */       fireValueChanged(DateSelectionEvent.EventType.DATES_SET);
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
/*     */   private boolean isIntervalSelected(Date startDate, Date endDate)
/*     */   {
/* 133 */     if (isSelectionEmpty()) return false;
/* 134 */     return (((Date)this.selectedDates.first()).equals(startDate)) && (((Date)this.selectedDates.last()).equals(endDate));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 142 */     if (startDate.after(endDate)) {
/* 143 */       return;
/*     */     }
/*     */     
/* 146 */     long startDateMs = startDate.getTime();
/* 147 */     long endDateMs = endDate.getTime();
/* 148 */     ArrayList<Date> datesToRemove = new ArrayList();
/* 149 */     for (Date selectedDate : this.selectedDates) {
/* 150 */       long selectedDateMs = selectedDate.getTime();
/* 151 */       if ((selectedDateMs >= startDateMs) && (selectedDateMs <= endDateMs)) {
/* 152 */         datesToRemove.add(selectedDate);
/*     */       }
/*     */     }
/*     */     
/* 156 */     if (!datesToRemove.isEmpty()) {
/* 157 */       this.selectedDates.removeAll(datesToRemove);
/* 158 */       fireValueChanged(DateSelectionEvent.EventType.DATES_REMOVED);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearSelection()
/*     */   {
/* 166 */     if (isSelectionEmpty()) return;
/* 167 */     clearSelectionImpl();
/* 168 */     fireValueChanged(DateSelectionEvent.EventType.SELECTION_CLEARED);
/*     */   }
/*     */   
/*     */   private void clearSelectionImpl() {
/* 172 */     this.selectedDates.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SortedSet<Date> getSelection()
/*     */   {
/* 179 */     return new TreeSet(this.selectedDates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getFirstSelectionDate()
/*     */   {
/* 186 */     return isSelectionEmpty() ? null : (Date)this.selectedDates.first();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getLastSelectionDate()
/*     */   {
/* 193 */     return isSelectionEmpty() ? null : (Date)this.selectedDates.last();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSelected(Date date)
/*     */   {
/* 200 */     Contract.asNotNull(date, "date must not be null");
/* 201 */     return this.selectedDates.contains(date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getNormalizedDate(Date date)
/*     */   {
/* 208 */     return new Date(date.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSelectionEmpty()
/*     */   {
/* 215 */     return this.selectedDates.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedSet<Date> getUnselectableDates()
/*     */   {
/* 223 */     return new TreeSet(this.unselectableDates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUnselectableDates(SortedSet<Date> unselectableDates)
/*     */   {
/* 230 */     this.unselectableDates = unselectableDates;
/* 231 */     for (Date unselectableDate : this.unselectableDates) {
/* 232 */       removeSelectionInterval(unselectableDate, unselectableDate);
/*     */     }
/* 234 */     fireValueChanged(DateSelectionEvent.EventType.UNSELECTED_DATES_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUnselectableDate(Date date)
/*     */   {
/* 241 */     return ((this.upperBound != null) && (this.upperBound.getTime() < date.getTime())) || ((this.lowerBound != null) && (this.lowerBound.getTime() > date.getTime())) || ((this.unselectableDates != null) && (this.unselectableDates.contains(date)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean addSelectionImpl(Date startDate, Date endDate)
/*     */   {
/* 248 */     boolean hasAdded = false;
/* 249 */     this.calendar.setTime(startDate);
/* 250 */     Date date = this.calendar.getTime();
/* 251 */     while ((date.before(endDate)) || (date.equals(endDate))) {
/* 252 */       if (!isUnselectableDate(date)) {
/* 253 */         hasAdded = true;
/* 254 */         this.selectedDates.add(date);
/*     */       }
/* 256 */       this.calendar.add(5, 1);
/* 257 */       date = this.calendar.getTime();
/*     */     }
/* 259 */     return hasAdded;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\DefaultDateSelectionModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */