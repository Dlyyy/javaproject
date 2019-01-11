/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Insets;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.EventListener;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeSet;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.Timer;
/*      */ import org.jdesktop.swingx.calendar.CalendarUtils;
/*      */ import org.jdesktop.swingx.calendar.DateSelectionModel;
/*      */ import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
/*      */ import org.jdesktop.swingx.calendar.DaySelectionModel;
/*      */ import org.jdesktop.swingx.event.DateSelectionEvent;
/*      */ import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
/*      */ import org.jdesktop.swingx.event.DateSelectionListener;
/*      */ import org.jdesktop.swingx.event.EventListenerMap;
/*      */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*      */ import org.jdesktop.swingx.plaf.MonthViewAddon;
/*      */ import org.jdesktop.swingx.plaf.MonthViewUI;
/*      */ import org.jdesktop.swingx.util.Contract;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JXMonthView
/*      */   extends JComponent
/*      */ {
/*  131 */   private static final Logger LOG = Logger.getLogger(JXMonthView.class.getName());
/*      */   
/*      */   public static final String COMMIT_KEY = "monthViewCommit";
/*      */   public static final String CANCEL_KEY = "monthViewCancel";
/*      */   public static final String BOX_PADDING_X = "boxPaddingX";
/*      */   public static final String BOX_PADDING_Y = "boxPaddingY";
/*      */   public static final String DAYS_OF_THE_WEEK = "daysOfTheWeek";
/*      */   public static final String SELECTION_MODEL = "selectionModel";
/*      */   public static final String TRAVERSABLE = "traversable";
/*      */   public static final String FLAGGED_DATES = "flaggedDates";
/*      */   public static final String uiClassID = "MonthViewUI";
/*      */   public static final int DAYS_IN_WEEK = 7;
/*      */   public static final int MONTHS_IN_YEAR = 12;
/*      */   private Date firstDisplayedDay;
/*      */   private Calendar cal;
/*      */   private Calendar anchor;
/*      */   
/*      */   static
/*      */   {
/*  150 */     LookAndFeelAddons.contribute(new MonthViewAddon());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Date today;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Timer todayTimer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int firstDayOfWeek;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private DateSelectionModel model;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private DateSelectionListener modelListener;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private DaySelectionModel flaggedDates;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private EventListenerMap listenerMap;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean traversable;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean leadingDays;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean trailingDays;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean showWeekNumber;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean componentInputMapEnabled;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date modifiedStartDate;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date modifiedEndDate;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String[] _daysOfTheWeek;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  233 */   protected Insets _monthStringInsets = new Insets(0, 0, 0, 0);
/*      */   private int boxPaddingX;
/*      */   private int boxPaddingY;
/*  236 */   private int minCalCols = 1;
/*  237 */   private int minCalRows = 1;
/*      */   private Color todayBackgroundColor;
/*      */   private Color monthStringBackground;
/*      */   private Color monthStringForeground;
/*      */   private Color daysOfTheWeekForeground;
/*      */   private Color selectedBackground;
/*  243 */   private Hashtable<Integer, Color> dayToColorTable = new Hashtable();
/*      */   
/*      */ 
/*      */   private Color flaggedDayForeground;
/*      */   
/*      */   private Color selectedForeground;
/*      */   
/*      */   private boolean zoomable;
/*      */   
/*      */ 
/*      */   public JXMonthView()
/*      */   {
/*  255 */     this(null, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXMonthView(Locale locale)
/*      */   {
/*  266 */     this(null, null, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXMonthView(Date firstDisplayedDay)
/*      */   {
/*  278 */     this(firstDisplayedDay, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXMonthView(Date firstDisplayedDay, DateSelectionModel model)
/*      */   {
/*  292 */     this(firstDisplayedDay, model, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXMonthView(Date firstDisplayedDay, DateSelectionModel model, Locale locale)
/*      */   {
/*  309 */     this.listenerMap = new EventListenerMap();
/*      */     
/*  311 */     initModel(model, locale);
/*  312 */     superSetLocale(locale);
/*  313 */     setFirstDisplayedDay(firstDisplayedDay != null ? firstDisplayedDay : getCurrentDate());
/*      */     
/*  315 */     updateTodayFromCurrentTime();
/*      */     
/*      */ 
/*  318 */     updateUI();
/*      */     
/*  320 */     setFocusable(true);
/*  321 */     this.todayBackgroundColor = getForeground();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */   {
/*  347 */     this.model.setLocale(locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void superSetLocale(Locale locale)
/*      */   {
/*  358 */     if (locale != null) {
/*  359 */       super.setLocale(locale);
/*  360 */       repaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar getCalendar()
/*      */   {
/*  376 */     if (this.cal == null) { throw new IllegalStateException("must not be called before instantiation is complete");
/*      */     }
/*  378 */     Calendar calendar = (Calendar)this.cal.clone();
/*  379 */     calendar.setTime(this.firstDisplayedDay);
/*  380 */     return calendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  390 */     return this.cal.getTimeZone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimeZone(TimeZone tz)
/*      */   {
/*  401 */     this.model.setTimeZone(tz);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFirstDayOfWeek()
/*      */   {
/*  412 */     return this.firstDayOfWeek;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFirstDayOfWeek(int firstDayOfWeek)
/*      */   {
/*  424 */     getSelectionModel().setFirstDayOfWeek(firstDayOfWeek);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initModel(DateSelectionModel model, Locale locale)
/*      */   {
/*  444 */     if (locale == null) {
/*  445 */       locale = JComponent.getDefaultLocale();
/*      */     }
/*  447 */     if (model == null) {
/*  448 */       model = new DaySelectionModel(locale);
/*      */     }
/*  450 */     this.model = model;
/*      */     
/*      */ 
/*      */ 
/*  454 */     this.flaggedDates = new DaySelectionModel(locale);
/*  455 */     this.flaggedDates.setSelectionMode(DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
/*      */     
/*  457 */     installCalendar();
/*  458 */     model.addDateSelectionListener(getDateSelectionListener());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private DateSelectionListener getDateSelectionListener()
/*      */   {
/*  468 */     if (this.modelListener == null) {
/*  469 */       this.modelListener = new DateSelectionListener()
/*      */       {
/*      */         public void valueChanged(DateSelectionEvent ev) {
/*  472 */           if (DateSelectionEvent.EventType.CALENDAR_CHANGED.equals(ev.getEventType())) {
/*  473 */             JXMonthView.this.updateCalendar();
/*      */           }
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*  480 */     return this.modelListener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void installCalendar()
/*      */   {
/*  491 */     this.cal = this.model.getCalendar();
/*  492 */     this.firstDayOfWeek = this.cal.getFirstDayOfWeek();
/*  493 */     Date anchorDate = getAnchorDate();
/*  494 */     this.anchor = ((Calendar)this.cal.clone());
/*  495 */     if (anchorDate != null) {
/*  496 */       setFirstDisplayedDay(anchorDate);
/*      */     }
/*  498 */     updateTodayFromCurrentTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date getAnchorDate()
/*      */   {
/*  511 */     return this.anchor != null ? this.anchor.getTime() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateCalendar()
/*      */   {
/*  518 */     if (!getLocale().equals(this.model.getLocale())) {
/*  519 */       installCalendar();
/*  520 */       superSetLocale(this.model.getLocale());
/*      */     } else {
/*  522 */       if (!this.model.getTimeZone().equals(getTimeZone())) {
/*  523 */         updateTimeZone();
/*      */       }
/*  525 */       if (this.cal.getMinimalDaysInFirstWeek() != this.model.getMinimalDaysInFirstWeek()) {
/*  526 */         updateMinimalDaysOfFirstWeek();
/*      */       }
/*  528 */       if (this.cal.getFirstDayOfWeek() != this.model.getFirstDayOfWeek()) {
/*  529 */         updateFirstDayOfWeek();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateTimeZone()
/*      */   {
/*  539 */     TimeZone old = getTimeZone();
/*  540 */     TimeZone tz = this.model.getTimeZone();
/*  541 */     this.cal.setTimeZone(tz);
/*  542 */     this.anchor.setTimeZone(tz);
/*  543 */     setFirstDisplayedDay(this.anchor.getTime());
/*  544 */     updateTodayFromCurrentTime();
/*  545 */     updateDatesAfterTimeZoneChange(old);
/*  546 */     firePropertyChange("timeZone", old, getTimeZone());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateDatesAfterTimeZoneChange(TimeZone oldTimeZone)
/*      */   {
/*  558 */     SortedSet<Date> flagged = getFlaggedDates();
/*  559 */     this.flaggedDates.setTimeZone(getTimeZone());
/*  560 */     firePropertyChange("flaggedDates", flagged, getFlaggedDates());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateFirstDayOfWeek()
/*      */   {
/*  567 */     int oldFirstDayOfWeek = this.firstDayOfWeek;
/*      */     
/*  569 */     this.firstDayOfWeek = getSelectionModel().getFirstDayOfWeek();
/*  570 */     this.cal.setFirstDayOfWeek(this.firstDayOfWeek);
/*  571 */     this.anchor.setFirstDayOfWeek(this.firstDayOfWeek);
/*  572 */     firePropertyChange("firstDayOfWeek", oldFirstDayOfWeek, this.firstDayOfWeek);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateMinimalDaysOfFirstWeek()
/*      */   {
/*  582 */     this.cal.setMinimalDaysInFirstWeek(this.model.getMinimalDaysInFirstWeek());
/*  583 */     this.anchor.setMinimalDaysInFirstWeek(this.model.getMinimalDaysInFirstWeek());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getLastDisplayedDay()
/*      */   {
/*  596 */     return getUI().getLastDisplayedDay();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getFirstDisplayedDay()
/*      */   {
/*  606 */     return this.firstDisplayedDay;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFirstDisplayedDay(Date date)
/*      */   {
/*  619 */     this.anchor.setTime(date);
/*  620 */     Date oldDate = getFirstDisplayedDay();
/*      */     
/*  622 */     this.cal.setTime(this.anchor.getTime());
/*  623 */     CalendarUtils.startOfMonth(this.cal);
/*  624 */     this.firstDisplayedDay = this.cal.getTime();
/*  625 */     firePropertyChange("firstDisplayedDay", oldDate, getFirstDisplayedDay());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void ensureDateVisible(Date date)
/*      */   {
/*  642 */     if (date.before(this.firstDisplayedDay)) {
/*  643 */       setFirstDisplayedDay(date);
/*      */     } else {
/*  645 */       Date lastDisplayedDate = getLastDisplayedDay();
/*  646 */       if (date.after(lastDisplayedDate))
/*      */       {
/*  648 */         this.cal.setTime(date);
/*  649 */         int month = this.cal.get(2);
/*  650 */         int year = this.cal.get(1);
/*      */         
/*  652 */         this.cal.setTime(lastDisplayedDate);
/*  653 */         int lastMonth = this.cal.get(2);
/*  654 */         int lastYear = this.cal.get(1);
/*      */         
/*  656 */         int diffMonths = month - lastMonth + (year - lastYear) * 12;
/*      */         
/*      */ 
/*  659 */         this.cal.setTime(this.firstDisplayedDay);
/*  660 */         this.cal.add(2, diffMonths);
/*  661 */         setFirstDisplayedDay(this.cal.getTime());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getDayAtLocation(int x, int y)
/*      */   {
/*  680 */     return getUI().getDayAtLocation(x, y);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Date getCurrentDate()
/*      */   {
/*  694 */     return new Date(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateTodayFromCurrentTime()
/*      */   {
/*  704 */     setToday(getCurrentDate());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void incrementToday()
/*      */   {
/*  714 */     this.cal.setTime(getToday());
/*  715 */     this.cal.add(5, 1);
/*  716 */     setToday(this.cal.getTime());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setToday(Date date)
/*      */   {
/*  729 */     Date oldToday = getToday();
/*      */     
/*  731 */     this.today = startOfDay(date);
/*  732 */     firePropertyChange("today", oldToday, getToday());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getToday()
/*      */   {
/*  742 */     return this.today != null ? (Date)this.today.clone() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Date startOfDay(Date date)
/*      */   {
/*  759 */     return CalendarUtils.startOfDay(this.cal, date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MonthViewUI getUI()
/*      */   {
/*  767 */     return (MonthViewUI)this.ui;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUI(MonthViewUI ui)
/*      */   {
/*  776 */     super.setUI(ui);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  786 */     setUI((MonthViewUI)LookAndFeelAddons.getUI(this, MonthViewUI.class));
/*  787 */     invalidate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  795 */     return "MonthViewUI";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateSelectionModel getSelectionModel()
/*      */   {
/*  808 */     return this.model;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionModel(DateSelectionModel model)
/*      */   {
/*  818 */     Contract.asNotNull(model, "date selection model must not be null");
/*  819 */     DateSelectionModel oldModel = getSelectionModel();
/*  820 */     model.removeDateSelectionListener(getDateSelectionListener());
/*  821 */     this.model = model;
/*  822 */     installCalendar();
/*  823 */     if (!model.getLocale().equals(getLocale())) {
/*  824 */       super.setLocale(model.getLocale());
/*      */     }
/*  826 */     model.addDateSelectionListener(getDateSelectionListener());
/*  827 */     firePropertyChange("selectionModel", oldModel, getSelectionModel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearSelection()
/*      */   {
/*  836 */     getSelectionModel().clearSelection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSelectionEmpty()
/*      */   {
/*  845 */     return getSelectionModel().isSelectionEmpty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SortedSet<Date> getSelection()
/*      */   {
/*  854 */     return getSelectionModel().getSelection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addSelectionInterval(Date startDate, Date endDate)
/*      */   {
/*  864 */     getSelectionModel().addSelectionInterval(startDate, endDate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionInterval(Date startDate, Date endDate)
/*      */   {
/*  874 */     getSelectionModel().setSelectionInterval(startDate, endDate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeSelectionInterval(Date startDate, Date endDate)
/*      */   {
/*  884 */     getSelectionModel().removeSelectionInterval(startDate, endDate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateSelectionModel.SelectionMode getSelectionMode()
/*      */   {
/*  893 */     return getSelectionModel().getSelectionMode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionMode(DateSelectionModel.SelectionMode selectionMode)
/*      */   {
/*  902 */     getSelectionModel().setSelectionMode(selectionMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getFirstSelectionDate()
/*      */   {
/*  912 */     return getSelectionModel().getFirstSelectionDate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getLastSelectionDate()
/*      */   {
/*  922 */     return getSelectionModel().getLastSelectionDate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getSelectionDate()
/*      */   {
/*  934 */     return getFirstSelectionDate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionDate(Date newDate)
/*      */   {
/*  944 */     if (newDate == null) {
/*  945 */       clearSelection();
/*      */     } else {
/*  947 */       setSelectionInterval(newDate, newDate);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSelected(Date date)
/*      */   {
/*  959 */     return getSelectionModel().isSelected(date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLowerBound(Date lowerBound)
/*      */   {
/*  970 */     getSelectionModel().setLowerBound(lowerBound);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUpperBound(Date upperBound)
/*      */   {
/*  979 */     getSelectionModel().setUpperBound(upperBound);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getLowerBound()
/*      */   {
/*  990 */     return getSelectionModel().getLowerBound();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getUpperBound()
/*      */   {
/* 1000 */     return getSelectionModel().getUpperBound();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUnselectableDate(Date date)
/*      */   {
/* 1011 */     return getSelectionModel().isUnselectableDate(date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUnselectableDates(Date... unselectableDates)
/*      */   {
/* 1028 */     Contract.asNotNull(unselectableDates, "unselectable dates must not be null");
/*      */     
/* 1030 */     SortedSet<Date> unselectableSet = new TreeSet();
/* 1031 */     for (Date unselectableDate : unselectableDates) {
/* 1032 */       unselectableSet.add(unselectableDate);
/*      */     }
/* 1034 */     getSelectionModel().setUnselectableDates(unselectableSet);
/*      */     
/* 1036 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFlaggedDate(Date date)
/*      */   {
/* 1047 */     if (date == null)
/* 1048 */       return false;
/* 1049 */     return this.flaggedDates.isSelected(date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFlaggedDates(Date... flagged)
/*      */   {
/* 1064 */     SortedSet<Date> oldFlagged = getFlaggedDates();
/* 1065 */     this.flaggedDates.clearSelection();
/* 1066 */     if (flagged != null) {
/* 1067 */       for (Date date : flagged) {
/* 1068 */         this.flaggedDates.addSelectionInterval(date, date);
/*      */       }
/*      */     }
/* 1071 */     firePropertyChange("flaggedDates", oldFlagged, getFlaggedDates());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addFlaggedDates(Date... flagged)
/*      */   {
/* 1084 */     SortedSet<Date> oldFlagged = this.flaggedDates.getSelection();
/* 1085 */     if (flagged != null) {
/* 1086 */       for (Date date : flagged) {
/* 1087 */         this.flaggedDates.addSelectionInterval(date, date);
/*      */       }
/*      */     }
/* 1090 */     firePropertyChange("flaggedDates", oldFlagged, this.flaggedDates.getSelection());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeFlaggedDates(Date... flagged)
/*      */   {
/* 1103 */     SortedSet<Date> oldFlagged = this.flaggedDates.getSelection();
/* 1104 */     if (flagged != null) {
/* 1105 */       for (Date date : flagged) {
/* 1106 */         this.flaggedDates.removeSelectionInterval(date, date);
/*      */       }
/*      */     }
/* 1109 */     firePropertyChange("flaggedDates", oldFlagged, this.flaggedDates.getSelection());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void clearFlaggedDates()
/*      */   {
/* 1116 */     SortedSet<Date> oldFlagged = this.flaggedDates.getSelection();
/* 1117 */     this.flaggedDates.clearSelection();
/* 1118 */     firePropertyChange("flaggedDates", oldFlagged, this.flaggedDates.getSelection());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SortedSet<Date> getFlaggedDates()
/*      */   {
/* 1128 */     return this.flaggedDates.getSelection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasFlaggedDates()
/*      */   {
/* 1137 */     return !this.flaggedDates.isSelectionEmpty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setShowingLeadingDays(boolean value)
/*      */   {
/* 1151 */     boolean old = isShowingLeadingDays();
/* 1152 */     this.leadingDays = value;
/* 1153 */     firePropertyChange("showingLeadingDays", old, isShowingLeadingDays());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isShowingLeadingDays()
/*      */   {
/* 1162 */     return this.leadingDays;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setShowingTrailingDays(boolean value)
/*      */   {
/* 1174 */     boolean old = isShowingTrailingDays();
/* 1175 */     this.trailingDays = value;
/* 1176 */     firePropertyChange("showingTrailingDays", old, isShowingTrailingDays());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isShowingTrailingDays()
/*      */   {
/* 1185 */     return this.trailingDays;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTraversable()
/*      */   {
/* 1197 */     if (isZoomable()) return true;
/* 1198 */     return this.traversable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTraversable(boolean traversable)
/*      */   {
/* 1215 */     boolean old = isTraversable();
/* 1216 */     this.traversable = traversable;
/* 1217 */     firePropertyChange("traversable", old, isTraversable());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isZoomable()
/*      */   {
/* 1227 */     return this.zoomable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setZoomable(boolean zoomable)
/*      */   {
/* 1243 */     boolean old = isZoomable();
/* 1244 */     this.zoomable = zoomable;
/* 1245 */     firePropertyChange("zoomable", old, isZoomable());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isShowingWeekNumber()
/*      */   {
/* 1255 */     return this.showWeekNumber;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setShowingWeekNumber(boolean showWeekNumber)
/*      */   {
/* 1266 */     boolean old = isShowingWeekNumber();
/* 1267 */     this.showWeekNumber = showWeekNumber;
/* 1268 */     firePropertyChange("showingWeekNumber", old, isShowingWeekNumber());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDaysOfTheWeek(String[] days)
/*      */   {
/* 1286 */     if ((days != null) && (days.length != 7)) {
/* 1287 */       throw new IllegalArgumentException("Array of days is not of length 7 as expected.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1292 */     String[] oldValue = getDaysOfTheWeek();
/* 1293 */     this._daysOfTheWeek = days;
/* 1294 */     firePropertyChange("daysOfTheWeek", oldValue, days);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getDaysOfTheWeek()
/*      */   {
/* 1308 */     if (this._daysOfTheWeek != null) {
/* 1309 */       String[] days = new String[7];
/* 1310 */       System.arraycopy(this._daysOfTheWeek, 0, days, 0, 7);
/* 1311 */       return days;
/*      */     }
/* 1313 */     return getUI().getDaysOfTheWeek();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDayOfTheWeek(int dayOfWeek)
/*      */   {
/* 1322 */     return getDaysOfTheWeek()[(dayOfWeek - 1)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBoxPaddingX()
/*      */   {
/* 1332 */     return this.boxPaddingX;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBoxPaddingX(int boxPaddingX)
/*      */   {
/* 1344 */     int oldBoxPadding = getBoxPaddingX();
/* 1345 */     this.boxPaddingX = boxPaddingX;
/* 1346 */     firePropertyChange("boxPaddingX", oldBoxPadding, getBoxPaddingX());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBoxPaddingY()
/*      */   {
/* 1355 */     return this.boxPaddingY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBoxPaddingY(int boxPaddingY)
/*      */   {
/* 1367 */     int oldBoxPadding = getBoxPaddingY();
/* 1368 */     this.boxPaddingY = boxPaddingY;
/* 1369 */     firePropertyChange("boxPaddingY", oldBoxPadding, getBoxPaddingY());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSelectionBackground()
/*      */   {
/* 1379 */     return this.selectedBackground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionBackground(Color c)
/*      */   {
/* 1389 */     Color old = getSelectionBackground();
/* 1390 */     this.selectedBackground = c;
/* 1391 */     firePropertyChange("selectionBackground", old, getSelectionBackground());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSelectionForeground()
/*      */   {
/* 1400 */     return this.selectedForeground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionForeground(Color c)
/*      */   {
/* 1410 */     Color old = getSelectionForeground();
/* 1411 */     this.selectedForeground = c;
/* 1412 */     firePropertyChange("selectionForeground", old, getSelectionForeground());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getTodayBackground()
/*      */   {
/* 1422 */     return this.todayBackgroundColor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTodayBackground(Color c)
/*      */   {
/* 1432 */     Color oldValue = getTodayBackground();
/* 1433 */     this.todayBackgroundColor = c;
/* 1434 */     firePropertyChange("todayBackground", oldValue, getTodayBackground());
/*      */     
/* 1436 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getMonthStringBackground()
/*      */   {
/* 1445 */     return this.monthStringBackground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonthStringBackground(Color c)
/*      */   {
/* 1455 */     Color old = getMonthStringBackground();
/* 1456 */     this.monthStringBackground = c;
/* 1457 */     firePropertyChange("monthStringBackground", old, getMonthStringBackground());
/*      */     
/* 1459 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getMonthStringForeground()
/*      */   {
/* 1468 */     return this.monthStringForeground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonthStringForeground(Color c)
/*      */   {
/* 1478 */     Color old = getMonthStringForeground();
/* 1479 */     this.monthStringForeground = c;
/* 1480 */     firePropertyChange("monthStringForeground", old, getMonthStringForeground());
/*      */     
/* 1482 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDaysOfTheWeekForeground(Color c)
/*      */   {
/* 1492 */     Color old = getDaysOfTheWeekForeground();
/* 1493 */     this.daysOfTheWeekForeground = c;
/* 1494 */     firePropertyChange("daysOfTheWeekForeground", old, getDaysOfTheWeekForeground());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Color getDaysOfTheWeekForeground()
/*      */   {
/* 1501 */     return this.daysOfTheWeekForeground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDayForeground(int dayOfWeek, Color c)
/*      */   {
/* 1516 */     if ((dayOfWeek < 1) || (dayOfWeek > 7)) {
/* 1517 */       throw new IllegalArgumentException("dayOfWeek must be in [Calendar.SUNDAY ... Calendar.SATURDAY] but was " + dayOfWeek);
/*      */     }
/*      */     
/* 1520 */     this.dayToColorTable.put(Integer.valueOf(dayOfWeek), c);
/* 1521 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getDayForeground(int dayOfWeek)
/*      */   {
/* 1534 */     Color c = (Color)this.dayToColorTable.get(Integer.valueOf(dayOfWeek));
/* 1535 */     if (c == null) {
/* 1536 */       c = getForeground();
/*      */     }
/* 1538 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getPerDayOfWeekForeground(int dayOfWeek)
/*      */   {
/* 1549 */     return (Color)this.dayToColorTable.get(Integer.valueOf(dayOfWeek));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFlaggedDayForeground(Color c)
/*      */   {
/* 1557 */     Color old = getFlaggedDayForeground();
/* 1558 */     this.flaggedDayForeground = c;
/* 1559 */     firePropertyChange("flaggedDayForeground", old, getFlaggedDayForeground());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getFlaggedDayForeground()
/*      */   {
/* 1568 */     return this.flaggedDayForeground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Insets getMonthStringInsets()
/*      */   {
/* 1577 */     return (Insets)this._monthStringInsets.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonthStringInsets(Insets insets)
/*      */   {
/* 1587 */     Insets old = getMonthStringInsets();
/* 1588 */     if (insets == null) {
/* 1589 */       this._monthStringInsets.top = 0;
/* 1590 */       this._monthStringInsets.left = 0;
/* 1591 */       this._monthStringInsets.bottom = 0;
/* 1592 */       this._monthStringInsets.right = 0;
/*      */     } else {
/* 1594 */       this._monthStringInsets.top = insets.top;
/* 1595 */       this._monthStringInsets.left = insets.left;
/* 1596 */       this._monthStringInsets.bottom = insets.bottom;
/* 1597 */       this._monthStringInsets.right = insets.right;
/*      */     }
/* 1599 */     firePropertyChange("monthStringInsets", old, getMonthStringInsets());
/*      */     
/* 1601 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPreferredColumnCount()
/*      */   {
/* 1612 */     return this.minCalCols;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreferredColumnCount(int cols)
/*      */   {
/* 1624 */     if (cols <= 0) {
/* 1625 */       return;
/*      */     }
/* 1627 */     int old = getPreferredColumnCount();
/* 1628 */     this.minCalCols = cols;
/* 1629 */     firePropertyChange("preferredColumnCount", old, getPreferredColumnCount());
/*      */     
/* 1631 */     revalidate();
/* 1632 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPreferredRowCount()
/*      */   {
/* 1644 */     return this.minCalRows;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreferredRowCount(int rows)
/*      */   {
/* 1657 */     if (rows <= 0) {
/* 1658 */       return;
/*      */     }
/* 1660 */     int old = getPreferredRowCount();
/* 1661 */     this.minCalRows = rows;
/* 1662 */     firePropertyChange("preferredRowCount", old, getPreferredRowCount());
/*      */     
/* 1664 */     revalidate();
/* 1665 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/* 1674 */     if (this.todayTimer != null) {
/* 1675 */       this.todayTimer.stop();
/*      */     }
/* 1677 */     super.removeNotify();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addNotify()
/*      */   {
/* 1685 */     super.addNotify();
/*      */     
/*      */ 
/* 1688 */     updateTodayFromCurrentTime();
/*      */     
/* 1690 */     int secondsTillTomorrow = 86400;
/*      */     
/* 1692 */     if (this.todayTimer == null) {
/* 1693 */       this.todayTimer = new Timer(secondsTillTomorrow * 1000, new ActionListener()
/*      */       {
/*      */         public void actionPerformed(ActionEvent e) {
/* 1696 */           JXMonthView.this.incrementToday();
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1703 */     this.cal.setTime(getCurrentDate());
/* 1704 */     secondsTillTomorrow = secondsTillTomorrow - this.cal.get(11) * 3600 - this.cal.get(12) * 60 - this.cal.get(13);
/*      */     
/*      */ 
/*      */ 
/* 1708 */     this.todayTimer.setInitialDelay(secondsTillTomorrow * 1000);
/* 1709 */     this.todayTimer.start();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void commitSelection()
/*      */   {
/* 1727 */     getSelectionModel().setAdjusting(false);
/* 1728 */     fireActionPerformed("monthViewCommit");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void cancelSelection()
/*      */   {
/* 1741 */     getSelectionModel().setAdjusting(false);
/* 1742 */     fireActionPerformed("monthViewCancel");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setComponentInputMapEnabled(boolean enabled)
/*      */   {
/* 1760 */     boolean old = isComponentInputMapEnabled();
/* 1761 */     this.componentInputMapEnabled = enabled;
/* 1762 */     firePropertyChange("componentInputMapEnabled", old, isComponentInputMapEnabled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isComponentInputMapEnabled()
/*      */   {
/* 1774 */     return this.componentInputMapEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addActionListener(ActionListener l)
/*      */   {
/* 1798 */     this.listenerMap.add(ActionListener.class, l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeActionListener(ActionListener l)
/*      */   {
/* 1807 */     this.listenerMap.remove(ActionListener.class, l);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> listenerType)
/*      */   {
/* 1813 */     List<T> listeners = this.listenerMap.getListeners(listenerType);
/*      */     T[] result;
/* 1815 */     if (!listeners.isEmpty())
/*      */     {
/* 1817 */       T[] result = (EventListener[])Array.newInstance(listenerType, listeners.size());
/* 1818 */       result = (EventListener[])listeners.toArray(result);
/*      */     } else {
/* 1820 */       result = super.getListeners(listenerType);
/*      */     }
/* 1822 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void fireActionPerformed(String actionCommand)
/*      */   {
/* 1832 */     ActionListener[] listeners = (ActionListener[])getListeners(ActionListener.class);
/* 1833 */     ActionEvent e = null;
/*      */     
/* 1835 */     for (ActionListener listener : listeners) {
/* 1836 */       if (e == null) {
/* 1837 */         e = new ActionEvent(this, 1001, actionCommand);
/*      */       }
/*      */       
/*      */ 
/* 1841 */       listener.actionPerformed(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected void cleanupWeekSelectionDates(Date startDate, Date endDate)
/*      */   {
/* 1855 */     int count = 1;
/* 1856 */     this.cal.setTime(startDate);
/* 1857 */     while (this.cal.getTimeInMillis() < endDate.getTime()) {
/* 1858 */       this.cal.add(5, 1);
/* 1859 */       count++;
/*      */     }
/*      */     
/* 1862 */     if (count > 7)
/*      */     {
/* 1864 */       this.cal.setTime(startDate);
/* 1865 */       int dayOfWeek = this.cal.get(7);
/* 1866 */       int firstDayOfWeek = getFirstDayOfWeek();
/* 1867 */       int daysFromStart = dayOfWeek - firstDayOfWeek;
/* 1868 */       if (daysFromStart < 0) {
/* 1869 */         daysFromStart += 7;
/*      */       }
/* 1871 */       this.cal.add(5, -daysFromStart);
/*      */       
/* 1873 */       this.modifiedStartDate = this.cal.getTime();
/*      */       
/*      */ 
/* 1876 */       this.cal.setTime(endDate);
/* 1877 */       dayOfWeek = this.cal.get(7);
/* 1878 */       int lastDayOfWeek = firstDayOfWeek - 1;
/* 1879 */       if (lastDayOfWeek == 0) {
/* 1880 */         lastDayOfWeek = 7;
/*      */       }
/* 1882 */       int daysTillEnd = lastDayOfWeek - dayOfWeek;
/* 1883 */       if (daysTillEnd < 0) {
/* 1884 */         daysTillEnd += 7;
/*      */       }
/* 1886 */       this.cal.add(5, daysTillEnd);
/* 1887 */       this.modifiedEndDate = this.cal.getTime();
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXMonthView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */