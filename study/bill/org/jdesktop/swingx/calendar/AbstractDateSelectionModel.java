/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import org.jdesktop.swingx.event.DateSelectionEvent;
/*     */ import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
/*     */ import org.jdesktop.swingx.event.DateSelectionListener;
/*     */ import org.jdesktop.swingx.event.EventListenerMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDateSelectionModel
/*     */   implements DateSelectionModel
/*     */ {
/*  44 */   public static final SortedSet<Date> EMPTY_DATES = Collections.unmodifiableSortedSet(new TreeSet());
/*     */   
/*     */ 
/*     */   protected EventListenerMap listenerMap;
/*     */   
/*     */   protected boolean adjusting;
/*     */   
/*     */   protected Calendar calendar;
/*     */   
/*     */   protected Date upperBound;
/*     */   
/*     */   protected Date lowerBound;
/*     */   
/*     */   protected Locale locale;
/*     */   
/*     */ 
/*     */   public AbstractDateSelectionModel()
/*     */   {
/*  62 */     this(null);
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
/*     */   public AbstractDateSelectionModel(Locale locale)
/*     */   {
/*  76 */     this.listenerMap = new EventListenerMap();
/*  77 */     setLocale(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Calendar getCalendar()
/*     */   {
/*  84 */     return (Calendar)this.calendar.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getFirstDayOfWeek()
/*     */   {
/*  91 */     return this.calendar.getFirstDayOfWeek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFirstDayOfWeek(int firstDayOfWeek)
/*     */   {
/*  98 */     if (firstDayOfWeek == getFirstDayOfWeek()) return;
/*  99 */     this.calendar.setFirstDayOfWeek(firstDayOfWeek);
/* 100 */     fireValueChanged(DateSelectionEvent.EventType.CALENDAR_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMinimalDaysInFirstWeek()
/*     */   {
/* 107 */     return this.calendar.getMinimalDaysInFirstWeek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMinimalDaysInFirstWeek(int minimalDays)
/*     */   {
/* 114 */     if (minimalDays == getMinimalDaysInFirstWeek()) return;
/* 115 */     this.calendar.setMinimalDaysInFirstWeek(minimalDays);
/* 116 */     fireValueChanged(DateSelectionEvent.EventType.CALENDAR_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZone getTimeZone()
/*     */   {
/* 124 */     return this.calendar.getTimeZone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTimeZone(TimeZone timeZone)
/*     */   {
/* 131 */     if (getTimeZone().equals(timeZone)) return;
/* 132 */     TimeZone oldTimeZone = getTimeZone();
/* 133 */     this.calendar.setTimeZone(timeZone);
/* 134 */     adjustDatesToTimeZone(oldTimeZone);
/* 135 */     fireValueChanged(DateSelectionEvent.EventType.CALENDAR_CHANGED);
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
/*     */   protected void adjustDatesToTimeZone(TimeZone oldTimeZone)
/*     */   {
/* 149 */     clearSelection();
/* 150 */     setLowerBound(null);
/* 151 */     setUpperBound(null);
/* 152 */     setUnselectableDates(EMPTY_DATES);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 159 */     return this.locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 166 */     if (locale == null) {
/* 167 */       locale = Locale.getDefault();
/*     */     }
/* 169 */     if (locale.equals(getLocale())) return;
/* 170 */     this.locale = locale;
/* 171 */     if (this.calendar != null) {
/* 172 */       this.calendar = Calendar.getInstance(this.calendar.getTimeZone(), locale);
/*     */     } else {
/* 174 */       this.calendar = Calendar.getInstance(locale);
/*     */     }
/* 176 */     fireValueChanged(DateSelectionEvent.EventType.CALENDAR_CHANGED);
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
/*     */   protected Date startOfDay(Date date)
/*     */   {
/* 189 */     return CalendarUtils.startOfDay(this.calendar, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Date endOfDay(Date date)
/*     */   {
/* 200 */     return CalendarUtils.endOfDay(this.calendar, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isSameDay(Date selected, Date compare)
/*     */   {
/* 212 */     return startOfDay(selected).equals(startOfDay(compare));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getUpperBound()
/*     */   {
/* 221 */     return this.upperBound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUpperBound(Date upperBound)
/*     */   {
/* 228 */     if (upperBound != null) {
/* 229 */       upperBound = getNormalizedDate(upperBound);
/*     */     }
/* 231 */     if (CalendarUtils.areEqual(upperBound, getUpperBound()))
/* 232 */       return;
/* 233 */     this.upperBound = upperBound;
/* 234 */     if ((this.upperBound != null) && (!isSelectionEmpty())) {
/* 235 */       long justAboveUpperBoundMs = this.upperBound.getTime() + 1L;
/* 236 */       removeSelectionInterval(new Date(justAboveUpperBoundMs), getLastSelectionDate());
/*     */     }
/*     */     
/* 239 */     fireValueChanged(DateSelectionEvent.EventType.UPPER_BOUND_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getLowerBound()
/*     */   {
/* 246 */     return this.lowerBound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLowerBound(Date lowerBound)
/*     */   {
/* 253 */     if (lowerBound != null) {
/* 254 */       lowerBound = getNormalizedDate(lowerBound);
/*     */     }
/* 256 */     if (CalendarUtils.areEqual(lowerBound, getLowerBound()))
/* 257 */       return;
/* 258 */     this.lowerBound = lowerBound;
/* 259 */     if ((this.lowerBound != null) && (!isSelectionEmpty()))
/*     */     {
/* 261 */       long justBelowLowerBoundMs = this.lowerBound.getTime() - 1L;
/* 262 */       removeSelectionInterval(getFirstSelectionDate(), new Date(justBelowLowerBoundMs));
/*     */     }
/*     */     
/* 265 */     fireValueChanged(DateSelectionEvent.EventType.LOWER_BOUND_CHANGED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAdjusting()
/*     */   {
/* 273 */     return this.adjusting;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAdjusting(boolean adjusting)
/*     */   {
/* 280 */     if (adjusting == isAdjusting()) return;
/* 281 */     this.adjusting = adjusting;
/* 282 */     fireValueChanged(adjusting ? DateSelectionEvent.EventType.ADJUSTING_STARTED : DateSelectionEvent.EventType.ADJUSTING_STOPPED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDateSelectionListener(DateSelectionListener l)
/*     */   {
/* 291 */     this.listenerMap.add(DateSelectionListener.class, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeDateSelectionListener(DateSelectionListener l)
/*     */   {
/* 298 */     this.listenerMap.remove(DateSelectionListener.class, l);
/*     */   }
/*     */   
/*     */   public List<DateSelectionListener> getDateSelectionListeners() {
/* 302 */     return this.listenerMap.getListeners(DateSelectionListener.class);
/*     */   }
/*     */   
/*     */   protected void fireValueChanged(DateSelectionEvent.EventType eventType) {
/* 306 */     List<DateSelectionListener> listeners = getDateSelectionListeners();
/* 307 */     DateSelectionEvent e = null;
/*     */     
/* 309 */     for (DateSelectionListener listener : listeners) {
/* 310 */       if (e == null) {
/* 311 */         e = new DateSelectionEvent(this, eventType, isAdjusting());
/*     */       }
/* 313 */       listener.valueChanged(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\AbstractDateSelectionModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */