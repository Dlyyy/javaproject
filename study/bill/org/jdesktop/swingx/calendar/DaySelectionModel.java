/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
/*     */ import org.jdesktop.swingx.event.EventListenerMap;
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
/*     */ public class DaySelectionModel
/*     */   extends AbstractDateSelectionModel
/*     */ {
/*     */   private DateSelectionModel.SelectionMode selectionMode;
/*     */   private SortedSet<Date> selectedDates;
/*     */   private SortedSet<Date> unselectableDates;
/*     */   
/*     */   public DaySelectionModel()
/*     */   {
/*  52 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DaySelectionModel(Locale locale)
/*     */   {
/*  59 */     super(locale);
/*  60 */     this.listenerMap = new EventListenerMap();
/*  61 */     this.selectionMode = DateSelectionModel.SelectionMode.SINGLE_SELECTION;
/*  62 */     this.selectedDates = new TreeSet();
/*  63 */     this.unselectableDates = new TreeSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateSelectionModel.SelectionMode getSelectionMode()
/*     */   {
/*  70 */     return this.selectionMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSelectionMode(DateSelectionModel.SelectionMode selectionMode)
/*     */   {
/*  77 */     this.selectionMode = selectionMode;
/*  78 */     clearSelection();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSelectionInterval(Date startDate, Date endDate)
/*     */   {
/*  86 */     if (startDate.after(endDate)) {
/*  87 */       return;
/*     */     }
/*  89 */     startDate = startOfDay(startDate);
/*  90 */     endDate = startOfDay(endDate);
/*  91 */     boolean added = false;
/*  92 */     switch (this.selectionMode) {
/*     */     case SINGLE_SELECTION: 
/*  94 */       if (isSelected(startDate)) return;
/*  95 */       clearSelectionImpl();
/*  96 */       added = addSelectionImpl(startDate, startDate);
/*  97 */       break;
/*     */     case SINGLE_INTERVAL_SELECTION: 
/*  99 */       if (isIntervalSelected(startDate, endDate)) return;
/* 100 */       clearSelectionImpl();
/* 101 */       added = addSelectionImpl(startDate, endDate);
/* 102 */       break;
/*     */     case MULTIPLE_INTERVAL_SELECTION: 
/* 104 */       if (isIntervalSelected(startDate, endDate)) return;
/* 105 */       added = addSelectionImpl(startDate, endDate);
/* 106 */       break;
/*     */     }
/*     */     
/*     */     
/* 110 */     if (added) {
/* 111 */       fireValueChanged(DateSelectionEvent.EventType.DATES_ADDED);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 119 */     startDate = startOfDay(startDate);
/* 120 */     endDate = startOfDay(endDate);
/* 121 */     if (DateSelectionModel.SelectionMode.SINGLE_SELECTION.equals(this.selectionMode)) {
/* 122 */       if (isSelected(startDate)) return;
/* 123 */       endDate = startDate;
/*     */     }
/* 125 */     else if (isIntervalSelected(startDate, endDate)) { return;
/*     */     }
/* 127 */     clearSelectionImpl();
/* 128 */     if (addSelectionImpl(startDate, endDate)) {
/* 129 */       fireValueChanged(DateSelectionEvent.EventType.DATES_SET);
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
/* 142 */     if (isSelectionEmpty()) return false;
/* 143 */     startDate = startOfDay(startDate);
/* 144 */     endDate = startOfDay(endDate);
/* 145 */     return (((Date)this.selectedDates.first()).equals(startDate)) && (((Date)this.selectedDates.last()).equals(endDate));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeSelectionInterval(Date startDate, Date endDate)
/*     */   {
/* 153 */     if (startDate.after(endDate)) {
/* 154 */       return;
/*     */     }
/*     */     
/* 157 */     startDate = startOfDay(startDate);
/* 158 */     endDate = startOfDay(endDate);
/* 159 */     long startDateMs = startDate.getTime();
/* 160 */     long endDateMs = endDate.getTime();
/* 161 */     ArrayList<Date> datesToRemove = new ArrayList();
/* 162 */     for (Date selectedDate : this.selectedDates) {
/* 163 */       long selectedDateMs = selectedDate.getTime();
/* 164 */       if ((selectedDateMs >= startDateMs) && (selectedDateMs <= endDateMs)) {
/* 165 */         datesToRemove.add(selectedDate);
/*     */       }
/*     */     }
/*     */     
/* 169 */     if (!datesToRemove.isEmpty()) {
/* 170 */       this.selectedDates.removeAll(datesToRemove);
/* 171 */       fireValueChanged(DateSelectionEvent.EventType.DATES_REMOVED);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearSelection()
/*     */   {
/* 179 */     if (isSelectionEmpty()) return;
/* 180 */     clearSelectionImpl();
/* 181 */     fireValueChanged(DateSelectionEvent.EventType.SELECTION_CLEARED);
/*     */   }
/*     */   
/*     */   private void clearSelectionImpl() {
/* 185 */     this.selectedDates.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SortedSet<Date> getSelection()
/*     */   {
/* 192 */     return new TreeSet(this.selectedDates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getFirstSelectionDate()
/*     */   {
/* 199 */     return isSelectionEmpty() ? null : (Date)this.selectedDates.first();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getLastSelectionDate()
/*     */   {
/* 206 */     return isSelectionEmpty() ? null : (Date)this.selectedDates.last();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSelected(Date date)
/*     */   {
/* 214 */     return this.selectedDates.contains(startOfDay(date));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSelectionEmpty()
/*     */   {
/* 221 */     return this.selectedDates.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SortedSet<Date> getUnselectableDates()
/*     */   {
/* 228 */     return new TreeSet(this.unselectableDates);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUnselectableDates(SortedSet<Date> unselectables)
/*     */   {
/* 235 */     this.unselectableDates.clear();
/* 236 */     for (Date date : unselectables) {
/* 237 */       this.unselectableDates.add(startOfDay(date));
/*     */     }
/* 239 */     for (Date unselectableDate : this.unselectableDates) {
/* 240 */       removeSelectionInterval(unselectableDate, unselectableDate);
/*     */     }
/* 242 */     fireValueChanged(DateSelectionEvent.EventType.UNSELECTED_DATES_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUnselectableDate(Date date)
/*     */   {
/* 249 */     date = startOfDay(date);
/* 250 */     return ((this.upperBound != null) && (this.upperBound.getTime() < date.getTime())) || ((this.lowerBound != null) && (this.lowerBound.getTime() > date.getTime())) || ((this.unselectableDates != null) && (this.unselectableDates.contains(date)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean addSelectionImpl(Date startDate, Date endDate)
/*     */   {
/* 257 */     boolean hasAdded = false;
/* 258 */     this.calendar.setTime(startDate);
/* 259 */     Date date = this.calendar.getTime();
/* 260 */     while ((date.before(endDate)) || (date.equals(endDate))) {
/* 261 */       if (!isUnselectableDate(date)) {
/* 262 */         hasAdded = true;
/* 263 */         this.selectedDates.add(date);
/*     */       }
/* 265 */       this.calendar.add(5, 1);
/* 266 */       date = this.calendar.getTime();
/*     */     }
/* 268 */     return hasAdded;
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
/*     */   public Date getNormalizedDate(Date date)
/*     */   {
/* 282 */     Contract.asNotNull(date, "date must not be null");
/* 283 */     return startOfDay(date);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\DaySelectionModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */