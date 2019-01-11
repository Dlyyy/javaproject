/*      */ package org.jdesktop.swingx.plaf.basic;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.SortedSet;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.CellRendererPane;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import org.jdesktop.swingx.JXMonthView;
/*      */ import org.jdesktop.swingx.SwingXUtilities;
/*      */ import org.jdesktop.swingx.action.AbstractActionExt;
/*      */ import org.jdesktop.swingx.calendar.CalendarUtils;
/*      */ import org.jdesktop.swingx.calendar.DateSelectionModel;
/*      */ import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
/*      */ import org.jdesktop.swingx.event.DateSelectionEvent;
/*      */ import org.jdesktop.swingx.event.DateSelectionListener;
/*      */ import org.jdesktop.swingx.plaf.MonthViewUI;
/*      */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*      */ public class BasicMonthViewUI
/*      */   extends MonthViewUI
/*      */ {
/*  121 */   private static final Logger LOG = Logger.getLogger(BasicMonthViewUI.class.getName());
/*      */   
/*      */   private static final int CALENDAR_SPACING = 10;
/*      */   
/*      */   public static final int MONTH_DOWN = 1;
/*      */   
/*      */   public static final int MONTH_UP = 2;
/*      */   
/*      */   protected static final int WEEK_HEADER_COLUMN = 0;
/*      */   
/*      */   protected static final int DAYS_IN_WEEK = 7;
/*      */   protected static final int FIRST_DAY_COLUMN = 1;
/*      */   protected static final int LAST_DAY_COLUMN = 7;
/*      */   protected static final int DAY_HEADER_ROW = 0;
/*      */   protected static final int WEEKS_IN_MONTH = 6;
/*      */   protected static final int FIRST_WEEK_ROW = 1;
/*      */   protected static final int LAST_WEEK_ROW = 6;
/*      */   @Deprecated
/*      */   protected String[] monthsOfTheYear;
/*      */   protected JXMonthView monthView;
/*      */   private PropertyChangeListener propertyChangeListener;
/*      */   private MouseListener mouseListener;
/*      */   private MouseMotionListener mouseMotionListener;
/*      */   private Handler handler;
/*      */   private Date lastDisplayedDate;
/*      */   private boolean usingKeyboard;
/*      */   private Date pivotDate;
/*      */   private SortedSet<Date> originalDateSpan;
/*      */   protected boolean isLeftToRight;
/*      */   protected Icon monthUpImage;
/*      */   protected Icon monthDownImage;
/*      */   private int arrowPaddingX;
/*      */   private int arrowPaddingY;
/*      */   private int fullMonthBoxHeight;
/*      */   private int fullBoxWidth;
/*      */   private int fullBoxHeight;
/*      */   private int calendarWidth;
/*      */   private int calendarHeight;
/*      */   private int fullCalendarHeight;
/*      */   private int fullCalendarWidth;
/*      */   private int calendarRowCount;
/*      */   private int calendarColumnCount;
/*      */   protected Rectangle calendarGrid;
/*      */   private String[] daysOfTheWeek;
/*      */   private CalendarRenderingHandler renderingHandler;
/*      */   private CellRendererPane rendererPane;
/*      */   private CalendarHeaderHandler calendarHeaderHandler;
/*      */   
/*      */   public BasicMonthViewUI()
/*      */   {
/*  171 */     this.usingKeyboard = false;
/*      */     
/*  173 */     this.pivotDate = null;
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
/*  190 */     this.arrowPaddingX = 3;
/*  191 */     this.arrowPaddingY = 3;
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
/*  215 */     this.calendarRowCount = 1;
/*      */     
/*  217 */     this.calendarColumnCount = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  222 */     this.calendarGrid = new Rectangle();
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
/*      */   public static ComponentUI createUI(JComponent c)
/*      */   {
/*  248 */     return new BasicMonthViewUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void installUI(JComponent c)
/*      */   {
/*  258 */     this.monthView = ((JXMonthView)c);
/*  259 */     this.monthView.setLayout(createLayoutManager());
/*      */     
/*      */ 
/*  262 */     installRenderingHandler();
/*      */     
/*  264 */     installDefaults();
/*  265 */     installDelegate();
/*  266 */     installKeyboardActions();
/*  267 */     installComponents();
/*  268 */     updateLocale(false);
/*  269 */     updateZoomable();
/*  270 */     installListeners();
/*      */   }
/*      */   
/*      */ 
/*      */   public void uninstallUI(JComponent c)
/*      */   {
/*  276 */     uninstallRenderingHandler();
/*  277 */     uninstallListeners();
/*  278 */     uninstallKeyboardActions();
/*  279 */     uninstallDefaults();
/*  280 */     uninstallComponents();
/*  281 */     this.monthView.setLayout(null);
/*  282 */     this.monthView = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void installComponents()
/*      */   {
/*  289 */     setCalendarHeaderHandler(createCalendarHeaderHandler());
/*  290 */     getCalendarHeaderHandler().install(this.monthView);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void uninstallComponents()
/*      */   {
/*  297 */     getCalendarHeaderHandler().uninstall(this.monthView);
/*  298 */     setCalendarHeaderHandler(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  309 */     LookAndFeel.installProperty(this.monthView, "opaque", Boolean.TRUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  316 */     if (SwingXUtilities.isUIInstallable(this.monthView.getBackground())) {
/*  317 */       this.monthView.setBackground(UIManagerExt.getColor("JXMonthView.background"));
/*      */     }
/*  319 */     if (SwingXUtilities.isUIInstallable(this.monthView.getForeground())) {
/*  320 */       this.monthView.setForeground(UIManagerExt.getColor("JXMonthView.foreground"));
/*      */     }
/*  322 */     if (SwingXUtilities.isUIInstallable(this.monthView.getFont()))
/*      */     {
/*  324 */       this.monthView.setFont(UIManager.getFont("JXMonthView.font"));
/*      */     }
/*  326 */     if (SwingXUtilities.isUIInstallable(this.monthView.getMonthStringBackground())) {
/*  327 */       this.monthView.setMonthStringBackground(UIManagerExt.getColor("JXMonthView.monthStringBackground"));
/*      */     }
/*  329 */     if (SwingXUtilities.isUIInstallable(this.monthView.getMonthStringForeground())) {
/*  330 */       this.monthView.setMonthStringForeground(UIManagerExt.getColor("JXMonthView.monthStringForeground"));
/*      */     }
/*  332 */     if (SwingXUtilities.isUIInstallable(this.monthView.getDaysOfTheWeekForeground())) {
/*  333 */       this.monthView.setDaysOfTheWeekForeground(UIManagerExt.getColor("JXMonthView.daysOfTheWeekForeground"));
/*      */     }
/*  335 */     if (SwingXUtilities.isUIInstallable(this.monthView.getSelectionBackground())) {
/*  336 */       this.monthView.setSelectionBackground(UIManagerExt.getColor("JXMonthView.selectedBackground"));
/*      */     }
/*  338 */     if (SwingXUtilities.isUIInstallable(this.monthView.getSelectionForeground())) {
/*  339 */       this.monthView.setSelectionForeground(UIManagerExt.getColor("JXMonthView.selectedForeground"));
/*      */     }
/*  341 */     if (SwingXUtilities.isUIInstallable(this.monthView.getFlaggedDayForeground())) {
/*  342 */       this.monthView.setFlaggedDayForeground(UIManagerExt.getColor("JXMonthView.flaggedDayForeground"));
/*      */     }
/*      */     
/*  345 */     this.monthView.setBoxPaddingX(UIManagerExt.getInt("JXMonthView.boxPaddingX"));
/*  346 */     this.monthView.setBoxPaddingY(UIManagerExt.getInt("JXMonthView.boxPaddingY"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void installDelegate()
/*      */   {
/*  353 */     this.isLeftToRight = this.monthView.getComponentOrientation().isLeftToRight();
/*      */     
/*      */ 
/*  356 */     this.monthDownImage = UIManager.getIcon("JXMonthView.monthDownFileName");
/*  357 */     this.monthUpImage = UIManager.getIcon("JXMonthView.monthUpFileName");
/*      */     
/*  359 */     setFirstDisplayedDay(this.monthView.getFirstDisplayedDay());
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
/*      */   @Deprecated
/*      */   protected boolean isUIInstallable(Object property)
/*      */   {
/*  374 */     return (property == null) || ((property instanceof UIResource));
/*      */   }
/*      */   
/*      */ 
/*      */   protected void uninstallDefaults() {}
/*      */   
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  383 */     installKeyBindings(1);
/*      */     
/*      */ 
/*  386 */     ActionMap actionMap = this.monthView.getActionMap();
/*  387 */     KeyboardAction acceptAction = new KeyboardAction(0);
/*  388 */     actionMap.put("acceptSelection", acceptAction);
/*  389 */     KeyboardAction cancelAction = new KeyboardAction(1);
/*  390 */     actionMap.put("cancelSelection", cancelAction);
/*      */     
/*  392 */     actionMap.put("selectPreviousDay", new KeyboardAction(2));
/*  393 */     actionMap.put("selectNextDay", new KeyboardAction(3));
/*  394 */     actionMap.put("selectDayInPreviousWeek", new KeyboardAction(4));
/*  395 */     actionMap.put("selectDayInNextWeek", new KeyboardAction(5));
/*      */     
/*  397 */     actionMap.put("adjustSelectionPreviousDay", new KeyboardAction(6));
/*  398 */     actionMap.put("adjustSelectionNextDay", new KeyboardAction(7));
/*  399 */     actionMap.put("adjustSelectionPreviousWeek", new KeyboardAction(8));
/*  400 */     actionMap.put("adjustSelectionNextWeek", new KeyboardAction(9));
/*      */     
/*      */ 
/*  403 */     actionMap.put("monthViewCommit", acceptAction);
/*  404 */     actionMap.put("monthViewCancel", cancelAction);
/*      */     
/*      */ 
/*      */ 
/*  408 */     AbstractActionExt prev = new AbstractActionExt()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/*  411 */         BasicMonthViewUI.this.previousMonth();
/*      */       }
/*      */       
/*  414 */     };
/*  415 */     this.monthView.getActionMap().put("scrollToPreviousMonth", prev);
/*  416 */     AbstractActionExt next = new AbstractActionExt()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/*  419 */         BasicMonthViewUI.this.nextMonth();
/*      */       }
/*      */       
/*  422 */     };
/*  423 */     this.monthView.getActionMap().put("scrollToNextMonth", next);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void installKeyBindings(int type)
/*      */   {
/*  432 */     InputMap inputMap = this.monthView.getInputMap(type);
/*  433 */     inputMap.put(KeyStroke.getKeyStroke(10, 0, false), "acceptSelection");
/*  434 */     inputMap.put(KeyStroke.getKeyStroke(27, 0, false), "cancelSelection");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  440 */     inputMap.put(KeyStroke.getKeyStroke(37, 0, false), "selectPreviousDay");
/*  441 */     inputMap.put(KeyStroke.getKeyStroke(39, 0, false), "selectNextDay");
/*  442 */     inputMap.put(KeyStroke.getKeyStroke(38, 0, false), "selectDayInPreviousWeek");
/*  443 */     inputMap.put(KeyStroke.getKeyStroke(40, 0, false), "selectDayInNextWeek");
/*      */     
/*  445 */     inputMap.put(KeyStroke.getKeyStroke(37, 1, false), "adjustSelectionPreviousDay");
/*  446 */     inputMap.put(KeyStroke.getKeyStroke(39, 1, false), "adjustSelectionNextDay");
/*  447 */     inputMap.put(KeyStroke.getKeyStroke(38, 1, false), "adjustSelectionPreviousWeek");
/*  448 */     inputMap.put(KeyStroke.getKeyStroke(40, 1, false), "adjustSelectionNextWeek");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void uninstallKeyBindings(int type)
/*      */   {
/*  455 */     InputMap inputMap = this.monthView.getInputMap(type);
/*  456 */     inputMap.clear();
/*      */   }
/*      */   
/*      */   protected void uninstallKeyboardActions() {}
/*      */   
/*      */   protected void installListeners() {
/*  462 */     this.propertyChangeListener = createPropertyChangeListener();
/*  463 */     this.mouseListener = createMouseListener();
/*  464 */     this.mouseMotionListener = createMouseMotionListener();
/*      */     
/*  466 */     this.monthView.addPropertyChangeListener(this.propertyChangeListener);
/*  467 */     this.monthView.addMouseListener(this.mouseListener);
/*  468 */     this.monthView.addMouseMotionListener(this.mouseMotionListener);
/*      */     
/*  470 */     this.monthView.getSelectionModel().addDateSelectionListener(getHandler());
/*      */   }
/*      */   
/*      */   protected void uninstallListeners() {
/*  474 */     this.monthView.getSelectionModel().removeDateSelectionListener(getHandler());
/*  475 */     this.monthView.removeMouseMotionListener(this.mouseMotionListener);
/*  476 */     this.monthView.removeMouseListener(this.mouseListener);
/*  477 */     this.monthView.removePropertyChangeListener(this.propertyChangeListener);
/*      */     
/*  479 */     this.mouseMotionListener = null;
/*  480 */     this.mouseListener = null;
/*  481 */     this.propertyChangeListener = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void installRenderingHandler()
/*      */   {
/*  488 */     setRenderingHandler(createRenderingHandler());
/*  489 */     if (getRenderingHandler() != null) {
/*  490 */       this.rendererPane = new CellRendererPane();
/*  491 */       this.monthView.add(this.rendererPane);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void uninstallRenderingHandler()
/*      */   {
/*  499 */     if (getRenderingHandler() == null) return;
/*  500 */     this.monthView.remove(this.rendererPane);
/*  501 */     this.rendererPane = null;
/*  502 */     setRenderingHandler(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CalendarRenderingHandler createRenderingHandler()
/*      */   {
/*  514 */     return new RenderingHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void setRenderingHandler(CalendarRenderingHandler renderingHandler)
/*      */   {
/*  521 */     this.renderingHandler = renderingHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected CalendarRenderingHandler getRenderingHandler()
/*      */   {
/*  528 */     return this.renderingHandler;
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
/*      */   protected void updateComponentInputMap()
/*      */   {
/*  549 */     if (this.monthView.isComponentInputMapEnabled()) {
/*  550 */       installKeyBindings(2);
/*      */     } else {
/*  552 */       uninstallKeyBindings(2);
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
/*      */   protected void updateLocale(boolean revalidate)
/*      */   {
/*  566 */     Locale locale = this.monthView.getLocale();
/*  567 */     if (getRenderingHandler() != null) {
/*  568 */       getRenderingHandler().setLocale(locale);
/*      */     }
/*  570 */     this.monthsOfTheYear = DateFormatSymbols.getInstance(locale).getMonths();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  575 */     this.daysOfTheWeek = ((String[])UIManager.get("JXMonthView.daysOfTheWeek"));
/*      */     
/*  577 */     if (this.daysOfTheWeek == null) {
/*  578 */       this.daysOfTheWeek = new String[7];
/*  579 */       String[] dateFormatSymbols = DateFormatSymbols.getInstance(locale).getShortWeekdays();
/*      */       
/*  581 */       this.daysOfTheWeek = new String[7];
/*  582 */       for (int i = 1; i <= 7; i++) {
/*  583 */         this.daysOfTheWeek[(i - 1)] = dateFormatSymbols[i];
/*      */       }
/*      */     }
/*  586 */     if (revalidate) {
/*  587 */       this.monthView.invalidate();
/*  588 */       this.monthView.validate();
/*      */     }
/*      */   }
/*      */   
/*      */   public String[] getDaysOfTheWeek()
/*      */   {
/*  594 */     String[] days = new String[this.daysOfTheWeek.length];
/*  595 */     System.arraycopy(this.daysOfTheWeek, 0, days, 0, days.length);
/*  596 */     return days;
/*      */   }
/*      */   
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  602 */     return getHandler();
/*      */   }
/*      */   
/*      */   protected LayoutManager createLayoutManager() {
/*  606 */     return getHandler();
/*      */   }
/*      */   
/*      */   protected MouseListener createMouseListener() {
/*  610 */     return getHandler();
/*      */   }
/*      */   
/*      */   protected MouseMotionListener createMouseMotionListener() {
/*  614 */     return getHandler();
/*      */   }
/*      */   
/*      */   private Handler getHandler() {
/*  618 */     if (this.handler == null) {
/*  619 */       this.handler = new Handler(null);
/*      */     }
/*      */     
/*  622 */     return this.handler;
/*      */   }
/*      */   
/*      */   public boolean isUsingKeyboard() {
/*  626 */     return this.usingKeyboard;
/*      */   }
/*      */   
/*      */   public void setUsingKeyboard(boolean val) {
/*  630 */     this.usingKeyboard = val;
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
/*      */   protected Rectangle getDayBoundsAtLocation(int x, int y)
/*      */   {
/*  652 */     Rectangle monthDetails = getMonthDetailsBoundsAtLocation(x, y);
/*  653 */     if ((monthDetails == null) || (!monthDetails.contains(x, y))) {
/*  654 */       return null;
/*      */     }
/*  656 */     int row = (y - monthDetails.y) / this.fullBoxHeight;
/*  657 */     int column = (x - monthDetails.x) / this.fullBoxWidth;
/*  658 */     return new Rectangle(monthDetails.x + column * this.fullBoxWidth, monthDetails.y + row * this.fullBoxHeight, this.fullBoxWidth, this.fullBoxHeight);
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
/*      */   protected Rectangle getDayBoundsInMonth(Date month, int row, int column)
/*      */   {
/*  676 */     checkValidRow(row, column);
/*  677 */     if ((0 == column) && (!this.monthView.isShowingWeekNumber())) return null;
/*  678 */     Rectangle monthBounds = getMonthBounds(month);
/*  679 */     if (monthBounds == null) { return null;
/*      */     }
/*  681 */     monthBounds.y += getMonthHeaderHeight() + (row - 0) * this.fullBoxHeight;
/*      */     
/*  683 */     int absoluteColumn = column - 1;
/*  684 */     if (this.monthView.isShowingWeekNumber()) {
/*  685 */       absoluteColumn++;
/*      */     }
/*  687 */     if (this.isLeftToRight) {
/*  688 */       monthBounds.x += absoluteColumn * this.fullBoxWidth;
/*      */     } else {
/*  690 */       int leading = monthBounds.x + monthBounds.width - this.fullBoxWidth;
/*  691 */       monthBounds.x = (leading - absoluteColumn * this.fullBoxWidth);
/*      */     }
/*  693 */     monthBounds.width = this.fullBoxWidth;
/*  694 */     monthBounds.height = this.fullBoxHeight;
/*  695 */     return monthBounds;
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
/*      */   protected Point getDayGridPositionAtLocation(int x, int y)
/*      */   {
/*  720 */     Rectangle monthDetailsBounds = getMonthDetailsBoundsAtLocation(x, y);
/*  721 */     if ((monthDetailsBounds == null) || (!monthDetailsBounds.contains(x, y))) return null;
/*  722 */     int calendarRow = (y - monthDetailsBounds.y) / this.fullBoxHeight + 0;
/*  723 */     int absoluteColumn = (x - monthDetailsBounds.x) / this.fullBoxWidth;
/*  724 */     int calendarColumn = absoluteColumn + 1;
/*  725 */     if (!this.isLeftToRight) {
/*  726 */       int leading = monthDetailsBounds.x + monthDetailsBounds.width;
/*  727 */       calendarColumn = (leading - x) / this.fullBoxWidth + 1;
/*      */     }
/*  729 */     if (this.monthView.isShowingWeekNumber()) {
/*  730 */       calendarColumn--;
/*      */     }
/*  732 */     return new Point(calendarColumn, calendarRow);
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
/*      */   protected Date getDayInMonth(Date month, int row, int column)
/*      */   {
/*  756 */     if ((row == 0) || (column == 0)) return null;
/*  757 */     Calendar calendar = getCalendar(month);
/*  758 */     int monthField = calendar.get(2);
/*  759 */     if (!CalendarUtils.isStartOfMonth(calendar))
/*  760 */       throw new IllegalStateException("calendar must be start of month but was: " + month.getTime());
/*  761 */     CalendarUtils.startOfWeek(calendar);
/*      */     
/*  763 */     calendar.add(5, (row - 1) * 7 + (column - 1));
/*      */     
/*  765 */     if (calendar.get(2) == monthField) {
/*  766 */       return calendar.getTime();
/*      */     }
/*  768 */     return null;
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
/*      */   protected Point getDayGridPosition(Date date)
/*      */   {
/*  782 */     if (!isVisible(date)) return null;
/*  783 */     Calendar calendar = getCalendar(date);
/*  784 */     Date startOfDay = CalendarUtils.startOfDay(calendar, date);
/*      */     
/*      */ 
/*  787 */     CalendarUtils.startOfWeek(calendar);
/*  788 */     int column = 1;
/*  789 */     while (calendar.getTime().before(startOfDay)) {
/*  790 */       column++;
/*  791 */       calendar.add(5, 1);
/*      */     }
/*      */     
/*  794 */     Date startOfWeek = CalendarUtils.startOfWeek(calendar, date);
/*  795 */     calendar.setTime(date);
/*  796 */     CalendarUtils.startOfMonth(calendar);
/*  797 */     int row = 1;
/*  798 */     while (calendar.getTime().before(startOfWeek)) {
/*  799 */       row++;
/*  800 */       calendar.add(3, 1);
/*      */     }
/*  802 */     return new Point(column, row);
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
/*      */   public Date getDayAtLocation(int x, int y)
/*      */   {
/*  822 */     Point dayInGrid = getDayGridPositionAtLocation(x, y);
/*  823 */     if ((dayInGrid == null) || (dayInGrid.x == 0) || (dayInGrid.y == 0))
/*  824 */       return null;
/*  825 */     Date month = getMonthAtLocation(x, y);
/*  826 */     return getDayInMonth(month, dayInGrid.y, dayInGrid.x);
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
/*      */   protected Rectangle getDayBounds(Date date)
/*      */   {
/*  842 */     if (!isVisible(date)) return null;
/*  843 */     Point position = getDayGridPosition(date);
/*  844 */     Rectangle monthBounds = getMonthBounds(date);
/*  845 */     monthBounds.y += getMonthHeaderHeight() + (position.y - 0) * this.fullBoxHeight;
/*  846 */     if (this.monthView.isShowingWeekNumber()) {
/*  847 */       position.x += 1;
/*      */     }
/*  849 */     position.x -= 1;
/*  850 */     if (this.isLeftToRight) {
/*  851 */       monthBounds.x += position.x * this.fullBoxWidth;
/*      */     } else {
/*  853 */       int start = monthBounds.x + monthBounds.width - this.fullBoxWidth;
/*  854 */       monthBounds.x = (start - position.x * this.fullBoxWidth);
/*      */     }
/*  856 */     monthBounds.width = this.fullBoxWidth;
/*  857 */     monthBounds.height = this.fullBoxHeight;
/*  858 */     return monthBounds;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void checkValidRow(int row, int column)
/*      */   {
/*  865 */     if ((column < 0) || (column > 7))
/*  866 */       throw new IllegalArgumentException("illegal column in day grid " + column);
/*  867 */     if ((row < 0) || (row > 6)) {
/*  868 */       throw new IllegalArgumentException("illegal row in day grid" + row);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isVisible(Date date)
/*      */   {
/*  879 */     if ((getFirstDisplayedDay().after(date)) || (getLastDisplayedDay().before(date))) return false;
/*  880 */     return true;
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
/*      */   protected int getTraversableGridPositionAtLocation(int x, int y)
/*      */   {
/*  899 */     Rectangle headerBounds = getMonthHeaderBoundsAtLocation(x, y);
/*  900 */     if (headerBounds == null) return -1;
/*  901 */     if (y < headerBounds.y + this.arrowPaddingY) return -1;
/*  902 */     if (y > headerBounds.y + headerBounds.height - this.arrowPaddingY) return -1;
/*  903 */     headerBounds.setBounds(headerBounds.x + this.arrowPaddingX, y, headerBounds.width - 2 * this.arrowPaddingX, headerBounds.height);
/*      */     
/*  905 */     if (!headerBounds.contains(x, y)) return -1;
/*  906 */     Rectangle hitArea = new Rectangle(headerBounds.x, headerBounds.y, this.monthUpImage.getIconWidth(), this.monthUpImage.getIconHeight());
/*  907 */     if (hitArea.contains(x, y)) {
/*  908 */       return this.isLeftToRight ? 1 : 2;
/*      */     }
/*  910 */     hitArea.translate(headerBounds.width - this.monthUpImage.getIconWidth(), 0);
/*  911 */     if (hitArea.contains(x, y)) {
/*  912 */       return this.isLeftToRight ? 2 : 1;
/*      */     }
/*  914 */     return -1;
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
/*      */   protected Rectangle getMonthHeaderBoundsAtLocation(int x, int y)
/*      */   {
/*  929 */     Rectangle header = getMonthBoundsAtLocation(x, y);
/*  930 */     if (header == null) return null;
/*  931 */     header.height = getMonthHeaderHeight();
/*  932 */     return header;
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
/*      */   protected Rectangle getMonthDetailsBoundsAtLocation(int x, int y)
/*      */   {
/*  945 */     Rectangle month = getMonthBoundsAtLocation(x, y);
/*  946 */     if (month == null) return null;
/*  947 */     int startOfDaysY = month.y + getMonthHeaderHeight();
/*  948 */     if (y < startOfDaysY) return null;
/*  949 */     month.y = startOfDaysY;
/*  950 */     month.height -= getMonthHeaderHeight();
/*  951 */     return month;
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
/*      */   protected Rectangle getMonthBoundsAtLocation(int x, int y)
/*      */   {
/*  971 */     if (!this.calendarGrid.contains(x, y)) return null;
/*  972 */     int calendarRow = (y - this.calendarGrid.y) / this.fullCalendarHeight;
/*  973 */     int calendarColumn = (x - this.calendarGrid.x) / this.fullCalendarWidth;
/*  974 */     return new Rectangle(this.calendarGrid.x + calendarColumn * this.fullCalendarWidth, this.calendarGrid.y + calendarRow * this.fullCalendarHeight, this.calendarWidth, this.calendarHeight);
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
/*      */   protected Point getMonthGridPositionAtLocation(int x, int y)
/*      */   {
/*  996 */     if (!this.calendarGrid.contains(x, y)) return null;
/*  997 */     int calendarRow = (y - this.calendarGrid.y) / this.fullCalendarHeight;
/*  998 */     int calendarColumn = (x - this.calendarGrid.x) / this.fullCalendarWidth;
/*  999 */     if (!this.isLeftToRight) {
/* 1000 */       int start = this.calendarGrid.x + this.calendarGrid.width;
/* 1001 */       calendarColumn = (start - x) / this.fullCalendarWidth;
/*      */     }
/*      */     
/* 1004 */     return new Point(calendarColumn, calendarRow);
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
/*      */   protected Date getMonthAtLocation(int x, int y)
/*      */   {
/* 1019 */     Point month = getMonthGridPositionAtLocation(x, y);
/* 1020 */     if (month == null) return null;
/* 1021 */     return getMonth(month.y, month.x);
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
/*      */   protected Date getMonth(int row, int column)
/*      */   {
/* 1038 */     Calendar calendar = getCalendar();
/* 1039 */     calendar.add(2, row * this.calendarColumnCount + column);
/*      */     
/* 1041 */     return calendar.getTime();
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
/*      */   protected Point getMonthGridPosition(Date date)
/*      */   {
/* 1059 */     if (!isVisible(date)) { return null;
/*      */     }
/* 1061 */     Calendar calendar = getCalendar();
/* 1062 */     int firstMonth = calendar.get(2);
/* 1063 */     int firstYear = calendar.get(1);
/*      */     
/*      */ 
/* 1066 */     calendar.setTime(date);
/* 1067 */     int month = calendar.get(2);
/* 1068 */     int year = calendar.get(1);
/*      */     
/* 1070 */     int diffMonths = month - firstMonth + (year - firstYear) * 12;
/*      */     
/*      */ 
/* 1073 */     int row = diffMonths / this.calendarColumnCount;
/* 1074 */     int column = diffMonths % this.calendarColumnCount;
/*      */     
/* 1076 */     return new Point(column, row);
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
/*      */   protected Rectangle getMonthBounds(int row, int column)
/*      */   {
/* 1093 */     int startY = this.calendarGrid.y + row * this.fullCalendarHeight;
/* 1094 */     int startX = this.calendarGrid.x + column * this.fullCalendarWidth;
/* 1095 */     if (!this.isLeftToRight) {
/* 1096 */       startX = this.calendarGrid.x + (this.calendarColumnCount - 1 - column) * this.fullCalendarWidth;
/*      */     }
/* 1098 */     return new Rectangle(startX, startY, this.calendarWidth, this.calendarHeight);
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
/*      */   protected Rectangle getMonthBounds(Date date)
/*      */   {
/* 1113 */     Point position = getMonthGridPosition(date);
/* 1114 */     return position != null ? getMonthBounds(position.y, position.x) : null;
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
/*      */   protected Rectangle getMonthHeaderBounds(Date date, boolean includeInsets)
/*      */   {
/* 1129 */     Point position = getMonthGridPosition(date);
/* 1130 */     if (position == null) return null;
/* 1131 */     Rectangle bounds = getMonthBounds(position.y, position.x);
/* 1132 */     bounds.height = getMonthHeaderHeight();
/* 1133 */     if (!includeInsets) {}
/*      */     
/*      */ 
/* 1136 */     return bounds;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Dimension getMonthSize()
/*      */   {
/* 1147 */     return new Dimension(this.calendarWidth, this.calendarHeight);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Dimension getDaySize()
/*      */   {
/* 1155 */     return new Dimension(this.fullBoxWidth, this.fullBoxHeight);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getMonthHeaderHeight()
/*      */   {
/* 1163 */     return this.fullMonthBoxHeight;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void calculateMonthGridLayoutProperties()
/*      */   {
/* 1175 */     calculateMonthGridRowColumnCount();
/* 1176 */     calculateMonthGridBounds();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void calculateMonthGridBounds()
/*      */   {
/* 1186 */     this.calendarGrid.setBounds(calculateCalendarGridX(), calculateCalendarGridY(), calculateCalendarGridWidth(), calculateCalendarGridHeight());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int calculateCalendarGridY()
/*      */   {
/* 1194 */     return (this.monthView.getHeight() - calculateCalendarGridHeight()) / 2;
/*      */   }
/*      */   
/*      */   private int calculateCalendarGridX() {
/* 1198 */     return (this.monthView.getWidth() - calculateCalendarGridWidth()) / 2;
/*      */   }
/*      */   
/*      */   private int calculateCalendarGridHeight() {
/* 1202 */     return this.calendarHeight * this.calendarRowCount + 10 * (this.calendarRowCount - 1);
/*      */   }
/*      */   
/*      */   private int calculateCalendarGridWidth()
/*      */   {
/* 1207 */     return this.calendarWidth * this.calendarColumnCount + 10 * (this.calendarColumnCount - 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void calculateMonthGridRowColumnCount()
/*      */   {
/* 1218 */     int oldNumCalCols = this.calendarColumnCount;
/* 1219 */     int oldNumCalRows = this.calendarRowCount;
/*      */     
/* 1221 */     this.calendarRowCount = 1;
/* 1222 */     this.calendarColumnCount = 1;
/* 1223 */     if (!isZoomable())
/*      */     {
/* 1225 */       int addColumns = (this.monthView.getWidth() - this.calendarWidth) / (this.calendarWidth + 10);
/*      */       
/*      */ 
/* 1228 */       if (addColumns > 0) {
/* 1229 */         this.calendarColumnCount += addColumns;
/*      */       }
/*      */       
/*      */ 
/* 1233 */       int addRows = (this.monthView.getHeight() - this.calendarHeight) / (this.calendarHeight + 10);
/*      */       
/* 1235 */       if (addRows > 0) {
/* 1236 */         this.calendarRowCount += addRows;
/*      */       }
/*      */     }
/* 1239 */     if ((oldNumCalCols != this.calendarColumnCount) || (oldNumCalRows != this.calendarRowCount))
/*      */     {
/* 1241 */       updateLastDisplayedDay(getFirstDisplayedDay());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected boolean isZoomable()
/*      */   {
/* 1249 */     return this.monthView.isZoomable();
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
/*      */   public void update(Graphics g, JComponent c)
/*      */   {
/* 1264 */     paintBackground(g);
/* 1265 */     paint(g, c);
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
/*      */   protected void paintBackground(Graphics g)
/*      */   {
/* 1278 */     if (this.monthView.isOpaque()) {
/* 1279 */       g.setColor(this.monthView.getBackground());
/* 1280 */       g.fillRect(0, 0, this.monthView.getWidth(), this.monthView.getHeight());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void paint(Graphics g, JComponent c)
/*      */   {
/* 1289 */     Rectangle clip = g.getClipBounds();
/*      */     
/* 1291 */     Calendar cal = getCalendar();
/*      */     
/* 1293 */     for (int row = 0; row < this.calendarRowCount; row++) {
/* 1294 */       for (int column = 0; column < this.calendarColumnCount; column++)
/*      */       {
/* 1296 */         Rectangle bounds = getMonthBounds(row, column);
/*      */         
/* 1298 */         if (bounds.intersects(clip)) {
/* 1299 */           paintMonth(g, cal);
/*      */         }
/* 1301 */         cal.add(2, 1);
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
/*      */   protected void paintMonth(Graphics g, Calendar month)
/*      */   {
/* 1316 */     paintMonthHeader(g, month);
/* 1317 */     paintDayHeader(g, month);
/* 1318 */     paintWeekHeader(g, month);
/* 1319 */     paintDays(g, month);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void paintMonthHeader(Graphics g, Calendar month)
/*      */   {
/* 1331 */     Rectangle page = getMonthHeaderBounds(month.getTime(), false);
/* 1332 */     paintDayOfMonth(g, page, month, CalendarState.TITLE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void paintDayHeader(Graphics g, Calendar month)
/*      */   {
/* 1344 */     paintDaysOfWeekSeparator(g, month);
/* 1345 */     Calendar cal = (Calendar)month.clone();
/* 1346 */     CalendarUtils.startOfWeek(cal);
/* 1347 */     for (int i = 1; i <= 7; i++) {
/* 1348 */       Rectangle dayBox = getDayBoundsInMonth(month.getTime(), 0, i);
/* 1349 */       paintDayOfMonth(g, dayBox, cal, CalendarState.DAY_OF_WEEK);
/* 1350 */       cal.add(5, 1);
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
/*      */   protected void paintWeekHeader(Graphics g, Calendar month)
/*      */   {
/* 1363 */     if (!this.monthView.isShowingWeekNumber())
/* 1364 */       return;
/* 1365 */     paintWeekOfYearSeparator(g, month);
/*      */     
/* 1367 */     int weeks = getWeeks(month);
/*      */     
/* 1369 */     Calendar weekCalendar = (Calendar)month.clone();
/*      */     
/* 1371 */     for (int week = 1; week < 1 + weeks; week++)
/*      */     {
/* 1373 */       Rectangle dayBox = getDayBoundsInMonth(month.getTime(), week, 0);
/*      */       
/*      */ 
/* 1376 */       paintDayOfMonth(g, dayBox, weekCalendar, CalendarState.WEEK_OF_YEAR);
/* 1377 */       weekCalendar.add(3, 1);
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
/*      */   protected void paintDays(Graphics g, Calendar month)
/*      */   {
/* 1390 */     Calendar clonedCal = (Calendar)month.clone();
/* 1391 */     CalendarUtils.startOfMonth(clonedCal);
/* 1392 */     Date startOfMonth = clonedCal.getTime();
/* 1393 */     CalendarUtils.endOfMonth(clonedCal);
/* 1394 */     Date endOfMonth = clonedCal.getTime();
/*      */     
/* 1396 */     clonedCal.setTime(month.getTime());
/*      */     
/* 1398 */     clonedCal.setTime(month.getTime());
/* 1399 */     CalendarUtils.startOfWeek(clonedCal);
/* 1400 */     for (int week = 1; week <= 6; week++) {
/* 1401 */       for (int day = 1; day <= 7; day++) {
/* 1402 */         CalendarState state = null;
/* 1403 */         if (clonedCal.getTime().before(startOfMonth)) {
/* 1404 */           if (this.monthView.isShowingLeadingDays()) {
/* 1405 */             state = CalendarState.LEADING;
/*      */           }
/* 1407 */         } else if (clonedCal.getTime().after(endOfMonth)) {
/* 1408 */           if (this.monthView.isShowingTrailingDays()) {
/* 1409 */             state = CalendarState.TRAILING;
/*      */           }
/*      */         }
/*      */         else {
/* 1413 */           state = isToday(clonedCal.getTime()) ? CalendarState.TODAY : CalendarState.IN_MONTH;
/*      */         }
/* 1415 */         if (state != null) {
/* 1416 */           Rectangle bounds = getDayBoundsInMonth(startOfMonth, week, day);
/* 1417 */           paintDayOfMonth(g, bounds, clonedCal, state);
/*      */         }
/* 1419 */         clonedCal.add(5, 1);
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
/*      */ 
/*      */ 
/*      */   protected void paintDayOfMonth(Graphics g, Rectangle bounds, Calendar calendar, CalendarState state)
/*      */   {
/* 1440 */     JComponent comp = getRenderingHandler().prepareRenderingComponent(this.monthView, calendar, state);
/*      */     
/* 1442 */     this.rendererPane.paintComponent(g, comp, this.monthView, bounds.x, bounds.y, bounds.width, bounds.height, true);
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
/*      */   protected void paintWeekOfYearSeparator(Graphics g, Calendar month)
/*      */   {
/* 1455 */     Rectangle r = getSeparatorBounds(month, 1, 0);
/* 1456 */     if (r == null) return;
/* 1457 */     g.setColor(this.monthView.getForeground());
/* 1458 */     g.drawLine(r.x, r.y, r.x, r.y + r.height);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void paintDaysOfWeekSeparator(Graphics g, Calendar month)
/*      */   {
/* 1470 */     Rectangle r = getSeparatorBounds(month, 0, 1);
/* 1471 */     if (r == null) return;
/* 1472 */     g.setColor(this.monthView.getForeground());
/* 1473 */     g.drawLine(r.x, r.y, r.x + r.width, r.y);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Rectangle getSeparatorBounds(Calendar month, int row, int column)
/*      */   {
/* 1483 */     Rectangle separator = getDayBoundsInMonth(month.getTime(), row, column);
/* 1484 */     if (separator == null) return null;
/* 1485 */     if (column == 0) {
/* 1486 */       separator.height *= 6;
/* 1487 */       if (this.isLeftToRight) {
/* 1488 */         separator.x += separator.width - 1;
/*      */       }
/* 1490 */       separator.width = 1;
/* 1491 */     } else if (row == 0) {
/* 1492 */       int oldWidth = separator.width;
/* 1493 */       separator.width *= 7;
/* 1494 */       if (!this.isLeftToRight) {
/* 1495 */         separator.x -= separator.width - oldWidth;
/*      */       }
/* 1497 */       separator.y += separator.height - 1;
/* 1498 */       separator.height = 1;
/*      */     }
/* 1500 */     return separator;
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
/*      */   protected int getWeeks(Calendar month)
/*      */   {
/* 1515 */     Calendar cloned = (Calendar)month.clone();
/*      */     
/* 1517 */     CalendarUtils.endOfMonth(cloned);
/*      */     
/* 1519 */     Date last = cloned.getTime();
/*      */     
/* 1521 */     cloned.setTime(month.getTime());
/* 1522 */     CalendarUtils.startOfWeek(cloned);
/* 1523 */     int weeks = 0;
/* 1524 */     while (last.after(cloned.getTime())) {
/* 1525 */       weeks++;
/* 1526 */       cloned.add(4, 1);
/*      */     }
/* 1528 */     return weeks;
/*      */   }
/*      */   
/*      */ 
/*      */   private void traverseMonth(int arrowType)
/*      */   {
/* 1534 */     if (arrowType == 1) {
/* 1535 */       previousMonth();
/* 1536 */     } else if (arrowType == 2) {
/* 1537 */       nextMonth();
/*      */     }
/*      */   }
/*      */   
/*      */   private void nextMonth() {
/* 1542 */     Date upperBound = this.monthView.getUpperBound();
/* 1543 */     if ((upperBound == null) || (upperBound.after(getLastDisplayedDay())))
/*      */     {
/* 1545 */       Calendar cal = getCalendar();
/* 1546 */       cal.add(2, 1);
/* 1547 */       this.monthView.setFirstDisplayedDay(cal.getTime());
/*      */     }
/*      */   }
/*      */   
/*      */   private void previousMonth() {
/* 1552 */     Date lowerBound = this.monthView.getLowerBound();
/* 1553 */     if ((lowerBound == null) || (lowerBound.before(getFirstDisplayedDay())))
/*      */     {
/* 1555 */       Calendar cal = getCalendar();
/* 1556 */       cal.add(2, -1);
/* 1557 */       this.monthView.setFirstDisplayedDay(cal.getTime());
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
/*      */   protected Calendar getCalendar()
/*      */   {
/* 1573 */     return getCalendar(getFirstDisplayedDay());
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
/*      */   protected Calendar getCalendar(Date date)
/*      */   {
/* 1586 */     Calendar calendar = this.monthView.getCalendar();
/* 1587 */     calendar.setTime(date);
/* 1588 */     return calendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateLastDisplayedDay(Date first)
/*      */   {
/* 1600 */     Calendar cal = getCalendar(first);
/* 1601 */     cal.add(2, this.calendarColumnCount * this.calendarRowCount - 1);
/* 1602 */     CalendarUtils.endOfMonth(cal);
/* 1603 */     this.lastDisplayedDate = cal.getTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getLastDisplayedDay()
/*      */   {
/* 1612 */     return this.lastDisplayedDate;
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
/*      */   protected void setFirstDisplayedDay(Date firstDisplayedDay)
/*      */   {
/* 1629 */     updateLastDisplayedDay(firstDisplayedDay);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date getFirstDisplayedDay()
/*      */   {
/* 1638 */     return this.monthView.getFirstDisplayedDay();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int getFirstDisplayedMonth()
/*      */   {
/* 1645 */     return getCalendar().get(2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getFirstDisplayedYear()
/*      */   {
/* 1653 */     return getCalendar().get(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SortedSet<Date> getSelection()
/*      */   {
/* 1661 */     return this.monthView.getSelection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date getToday()
/*      */   {
/* 1669 */     return this.monthView.getToday();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isToday(Date date)
/*      */   {
/* 1681 */     return date.equals(getToday());
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
/* 1696 */   private boolean canSelectByMode() { return true; }
/*      */   
/*      */   protected static class RenderingHandler extends BasicCalendarRenderingHandler
/*      */   {}
/*      */   
/*      */   private class Handler implements MouseListener, MouseMotionListener, LayoutManager, PropertyChangeListener, DateSelectionListener {
/*      */     private boolean armed;
/*      */     private Date startDate;
/*      */     private Date endDate;
/*      */     
/*      */     private Handler() {}
/*      */     
/*      */     public void mouseClicked(MouseEvent e) {}
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 1711 */       BasicMonthViewUI.this.setUsingKeyboard(false);
/*      */       
/* 1713 */       if (!BasicMonthViewUI.this.monthView.isEnabled()) {
/* 1714 */         return;
/*      */       }
/*      */       
/* 1717 */       if ((!BasicMonthViewUI.this.monthView.hasFocus()) && (BasicMonthViewUI.this.monthView.isFocusable())) {
/* 1718 */         BasicMonthViewUI.this.monthView.requestFocusInWindow();
/*      */       }
/*      */       
/*      */ 
/* 1722 */       if (BasicMonthViewUI.this.monthView.isTraversable()) {
/* 1723 */         int arrowType = BasicMonthViewUI.this.getTraversableGridPositionAtLocation(e.getX(), e.getY());
/* 1724 */         if (arrowType != -1) {
/* 1725 */           BasicMonthViewUI.this.traverseMonth(arrowType);
/* 1726 */           return;
/*      */         }
/*      */       }
/*      */       
/* 1730 */       if (!BasicMonthViewUI.this.canSelectByMode()) {
/* 1731 */         return;
/*      */       }
/*      */       
/* 1734 */       Date cal = BasicMonthViewUI.this.getDayAtLocation(e.getX(), e.getY());
/* 1735 */       if (cal == null) {
/* 1736 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1740 */       this.startDate = cal;
/* 1741 */       this.endDate = cal;
/*      */       
/* 1743 */       if ((BasicMonthViewUI.this.monthView.getSelectionMode() == DateSelectionModel.SelectionMode.SINGLE_INTERVAL_SELECTION) || (BasicMonthViewUI.this.monthView.getSelectionMode() == DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION))
/*      */       {
/*      */ 
/* 1746 */         BasicMonthViewUI.this.pivotDate = this.startDate;
/*      */       }
/*      */       
/* 1749 */       BasicMonthViewUI.this.monthView.getSelectionModel().setAdjusting(true);
/*      */       
/* 1751 */       if ((BasicMonthViewUI.this.monthView.getSelectionMode() == DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION) && (e.isControlDown())) {
/* 1752 */         BasicMonthViewUI.this.monthView.addSelectionInterval(this.startDate, this.endDate);
/*      */       } else {
/* 1754 */         BasicMonthViewUI.this.monthView.setSelectionInterval(this.startDate, this.endDate);
/*      */       }
/*      */       
/*      */ 
/* 1758 */       this.armed = true;
/*      */     }
/*      */     
/*      */ 
/*      */     public void mouseReleased(MouseEvent e)
/*      */     {
/* 1764 */       BasicMonthViewUI.this.setUsingKeyboard(false);
/*      */       
/* 1766 */       if (!BasicMonthViewUI.this.monthView.isEnabled()) {
/* 1767 */         return;
/*      */       }
/*      */       
/* 1770 */       if ((!BasicMonthViewUI.this.monthView.hasFocus()) && (BasicMonthViewUI.this.monthView.isFocusable())) {
/* 1771 */         BasicMonthViewUI.this.monthView.requestFocusInWindow();
/*      */       }
/*      */       
/* 1774 */       if (this.armed) {
/* 1775 */         BasicMonthViewUI.this.monthView.commitSelection();
/*      */       }
/* 1777 */       this.armed = false;
/*      */     }
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {}
/*      */     
/*      */     public void mouseExited(MouseEvent e) {}
/*      */     
/*      */     public void mouseDragged(MouseEvent e)
/*      */     {
/* 1786 */       BasicMonthViewUI.this.setUsingKeyboard(false);
/* 1787 */       if ((!BasicMonthViewUI.this.monthView.isEnabled()) || (!BasicMonthViewUI.this.canSelectByMode())) {
/* 1788 */         return;
/*      */       }
/* 1790 */       Date cal = BasicMonthViewUI.this.getDayAtLocation(e.getX(), e.getY());
/* 1791 */       if (cal == null) {
/* 1792 */         return;
/*      */       }
/*      */       
/* 1795 */       Date selected = cal;
/* 1796 */       Date oldStart = this.startDate;
/* 1797 */       Date oldEnd = this.endDate;
/*      */       
/* 1799 */       if (BasicMonthViewUI.this.monthView.getSelectionMode() == DateSelectionModel.SelectionMode.SINGLE_SELECTION) {
/* 1800 */         if (selected.equals(oldStart)) {
/* 1801 */           return;
/*      */         }
/* 1803 */         this.startDate = selected;
/* 1804 */         this.endDate = selected;
/* 1805 */       } else if (BasicMonthViewUI.this.pivotDate != null) {
/* 1806 */         if (selected.before(BasicMonthViewUI.this.pivotDate)) {
/* 1807 */           this.startDate = selected;
/* 1808 */           this.endDate = BasicMonthViewUI.this.pivotDate;
/* 1809 */         } else if (selected.after(BasicMonthViewUI.this.pivotDate)) {
/* 1810 */           this.startDate = BasicMonthViewUI.this.pivotDate;
/* 1811 */           this.endDate = selected;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1816 */         this.startDate = selected;
/* 1817 */         this.endDate = selected;
/* 1818 */         BasicMonthViewUI.this.pivotDate = selected;
/*      */       }
/*      */       
/* 1821 */       if ((this.startDate.equals(oldStart)) && (this.endDate.equals(oldEnd))) {
/* 1822 */         return;
/*      */       }
/*      */       
/* 1825 */       if ((BasicMonthViewUI.this.monthView.getSelectionMode() == DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION) && (e.isControlDown())) {
/* 1826 */         BasicMonthViewUI.this.monthView.addSelectionInterval(this.startDate, this.endDate);
/*      */       } else {
/* 1828 */         BasicMonthViewUI.this.monthView.setSelectionInterval(this.startDate, this.endDate);
/*      */       }
/*      */       
/*      */ 
/* 1832 */       this.armed = true;
/*      */     }
/*      */     
/*      */ 
/*      */     public void mouseMoved(MouseEvent e) {}
/*      */     
/*      */     public void addLayoutComponent(String name, Component comp) {}
/*      */     
/* 1840 */     private Dimension preferredSize = new Dimension();
/*      */     
/*      */ 
/*      */     public void removeLayoutComponent(Component comp) {}
/*      */     
/*      */     public Dimension preferredLayoutSize(Container parent)
/*      */     {
/* 1847 */       layoutContainer(parent);
/* 1848 */       return new Dimension(this.preferredSize);
/*      */     }
/*      */     
/*      */     public Dimension minimumLayoutSize(Container parent) {
/* 1852 */       return preferredLayoutSize(parent);
/*      */     }
/*      */     
/*      */     public void layoutContainer(Container parent)
/*      */     {
/* 1857 */       int maxMonthWidth = 0;
/* 1858 */       int maxMonthHeight = 0;
/* 1859 */       Calendar calendar = BasicMonthViewUI.this.getCalendar();
/* 1860 */       for (int i = calendar.getMinimum(2); i <= calendar.getMaximum(2); i++) {
/* 1861 */         calendar.set(2, i);
/* 1862 */         CalendarUtils.startOfMonth(calendar);
/* 1863 */         JComponent comp = BasicMonthViewUI.this.getRenderingHandler().prepareRenderingComponent(BasicMonthViewUI.this.monthView, calendar, CalendarState.TITLE);
/* 1864 */         Dimension pref = comp.getPreferredSize();
/* 1865 */         maxMonthWidth = Math.max(maxMonthWidth, pref.width);
/* 1866 */         maxMonthHeight = Math.max(maxMonthHeight, pref.height);
/*      */       }
/*      */       
/* 1869 */       int maxBoxWidth = 0;
/* 1870 */       int maxBoxHeight = 0;
/* 1871 */       calendar = BasicMonthViewUI.this.getCalendar();
/* 1872 */       CalendarUtils.startOfWeek(calendar);
/* 1873 */       for (int i = 0; i < 7; i++) {
/* 1874 */         JComponent comp = BasicMonthViewUI.this.getRenderingHandler().prepareRenderingComponent(BasicMonthViewUI.this.monthView, calendar, CalendarState.DAY_OF_WEEK);
/* 1875 */         Dimension pref = comp.getPreferredSize();
/* 1876 */         maxBoxWidth = Math.max(maxBoxWidth, pref.width);
/* 1877 */         maxBoxHeight = Math.max(maxBoxHeight, pref.height);
/* 1878 */         calendar.add(5, 1);
/*      */       }
/*      */       
/* 1881 */       calendar = BasicMonthViewUI.this.getCalendar();
/* 1882 */       for (int i = 0; i < calendar.getMaximum(5); i++) {
/* 1883 */         JComponent comp = BasicMonthViewUI.this.getRenderingHandler().prepareRenderingComponent(BasicMonthViewUI.this.monthView, calendar, CalendarState.IN_MONTH);
/* 1884 */         Dimension pref = comp.getPreferredSize();
/* 1885 */         maxBoxWidth = Math.max(maxBoxWidth, pref.width);
/* 1886 */         maxBoxHeight = Math.max(maxBoxHeight, pref.height);
/* 1887 */         calendar.add(5, 1);
/*      */       }
/*      */       
/* 1890 */       int dayColumns = 7;
/* 1891 */       if (BasicMonthViewUI.this.monthView.isShowingWeekNumber()) {
/* 1892 */         dayColumns++;
/*      */       }
/*      */       
/* 1895 */       if (maxMonthWidth > maxBoxWidth * dayColumns)
/*      */       {
/*      */ 
/* 1898 */         double diff = maxMonthWidth - maxBoxWidth * dayColumns;
/* 1899 */         maxBoxWidth = (int)(maxBoxWidth + Math.ceil(diff / dayColumns));
/*      */       }
/*      */       
/*      */ 
/* 1903 */       BasicMonthViewUI.this.fullBoxWidth = maxBoxWidth;
/* 1904 */       BasicMonthViewUI.this.fullBoxHeight = maxBoxHeight;
/*      */       
/* 1906 */       int boxHeight = maxBoxHeight - 2 * BasicMonthViewUI.this.monthView.getBoxPaddingY();
/* 1907 */       BasicMonthViewUI.this.fullMonthBoxHeight = Math.max(boxHeight, maxMonthHeight);
/*      */       
/*      */ 
/* 1910 */       BasicMonthViewUI.this.calendarWidth = (BasicMonthViewUI.this.fullBoxWidth * 7);
/* 1911 */       if (BasicMonthViewUI.this.monthView.isShowingWeekNumber()) {
/* 1912 */         BasicMonthViewUI.access$912(BasicMonthViewUI.this, BasicMonthViewUI.this.fullBoxWidth);
/*      */       }
/* 1914 */       BasicMonthViewUI.this.fullCalendarWidth = (BasicMonthViewUI.this.calendarWidth + 10);
/*      */       
/* 1916 */       BasicMonthViewUI.this.calendarHeight = (BasicMonthViewUI.this.fullBoxHeight * 7 + BasicMonthViewUI.this.fullMonthBoxHeight);
/* 1917 */       BasicMonthViewUI.this.fullCalendarHeight = (BasicMonthViewUI.this.calendarHeight + 10);
/*      */       
/* 1919 */       int prefRows = getPreferredRows();
/* 1920 */       this.preferredSize.height = (BasicMonthViewUI.this.calendarHeight * prefRows + 10 * (prefRows - 1));
/*      */       
/*      */ 
/* 1923 */       int prefCols = getPreferredColumns();
/* 1924 */       this.preferredSize.width = (BasicMonthViewUI.this.calendarWidth * prefCols + 10 * (prefCols - 1));
/*      */       
/*      */ 
/*      */ 
/* 1928 */       Insets insets = BasicMonthViewUI.this.monthView.getInsets();
/* 1929 */       this.preferredSize.width += insets.left + insets.right;
/* 1930 */       this.preferredSize.height += insets.top + insets.bottom;
/*      */       
/* 1932 */       BasicMonthViewUI.this.calculateMonthGridLayoutProperties();
/*      */       
/* 1934 */       if (BasicMonthViewUI.this.isZoomable()) {
/* 1935 */         BasicMonthViewUI.this.getCalendarHeaderHandler().getHeaderComponent().setBounds(BasicMonthViewUI.this.getMonthHeaderBounds(BasicMonthViewUI.this.monthView.getFirstDisplayedDay(), false));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private int getPreferredColumns()
/*      */     {
/* 1943 */       return BasicMonthViewUI.this.isZoomable() ? 1 : BasicMonthViewUI.this.monthView.getPreferredColumnCount();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private int getPreferredRows()
/*      */     {
/* 1950 */       return BasicMonthViewUI.this.isZoomable() ? 1 : BasicMonthViewUI.this.monthView.getPreferredRowCount();
/*      */     }
/*      */     
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent evt)
/*      */     {
/* 1956 */       String property = evt.getPropertyName();
/*      */       
/* 1958 */       if ("componentOrientation".equals(property)) {
/* 1959 */         BasicMonthViewUI.this.isLeftToRight = BasicMonthViewUI.this.monthView.getComponentOrientation().isLeftToRight();
/* 1960 */         BasicMonthViewUI.this.monthView.revalidate();
/* 1961 */         BasicMonthViewUI.this.monthView.repaint();
/* 1962 */       } else if ("selectionModel".equals(property)) {
/* 1963 */         DateSelectionModel selectionModel = (DateSelectionModel)evt.getOldValue();
/* 1964 */         selectionModel.removeDateSelectionListener(BasicMonthViewUI.this.getHandler());
/* 1965 */         selectionModel = (DateSelectionModel)evt.getNewValue();
/* 1966 */         selectionModel.addDateSelectionListener(BasicMonthViewUI.this.getHandler());
/* 1967 */       } else if ("firstDisplayedDay".equals(property)) {
/* 1968 */         BasicMonthViewUI.this.setFirstDisplayedDay((Date)evt.getNewValue());
/* 1969 */         BasicMonthViewUI.this.monthView.repaint();
/* 1970 */       } else if (("boxPaddingX".equals(property)) || ("boxPaddingY".equals(property)) || ("traversable".equals(property)) || ("daysOfTheWeek".equals(property)) || ("border".equals(property)) || ("showingWeekNumber".equals(property)) || ("traversable".equals(property)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1979 */         BasicMonthViewUI.this.monthView.revalidate();
/* 1980 */         BasicMonthViewUI.this.monthView.repaint();
/* 1981 */       } else if ("zoomable".equals(property)) {
/* 1982 */         BasicMonthViewUI.this.updateZoomable();
/*      */ 
/*      */ 
/*      */       }
/* 1986 */       else if ("componentInputMapEnabled".equals(property)) {
/* 1987 */         BasicMonthViewUI.this.updateComponentInputMap();
/* 1988 */       } else if ("locale".equals(property)) {
/* 1989 */         BasicMonthViewUI.this.updateLocale(true);
/*      */       } else {
/* 1991 */         BasicMonthViewUI.this.monthView.repaint();
/*      */       }
/*      */     }
/*      */     
/*      */     public void valueChanged(DateSelectionEvent ev)
/*      */     {
/* 1997 */       BasicMonthViewUI.this.monthView.repaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class KeyboardAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public static final int ACCEPT_SELECTION = 0;
/*      */     
/*      */     public static final int CANCEL_SELECTION = 1;
/*      */     
/*      */     public static final int SELECT_PREVIOUS_DAY = 2;
/*      */     public static final int SELECT_NEXT_DAY = 3;
/*      */     public static final int SELECT_DAY_PREVIOUS_WEEK = 4;
/*      */     public static final int SELECT_DAY_NEXT_WEEK = 5;
/*      */     public static final int ADJUST_SELECTION_PREVIOUS_DAY = 6;
/*      */     public static final int ADJUST_SELECTION_NEXT_DAY = 7;
/*      */     public static final int ADJUST_SELECTION_PREVIOUS_WEEK = 8;
/*      */     public static final int ADJUST_SELECTION_NEXT_WEEK = 9;
/*      */     private int action;
/*      */     
/*      */     public KeyboardAction(int action)
/*      */     {
/* 2021 */       this.action = action;
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent ev) {
/* 2025 */       if (!BasicMonthViewUI.this.canSelectByMode())
/* 2026 */         return;
/* 2027 */       if (!BasicMonthViewUI.this.isUsingKeyboard()) {
/* 2028 */         BasicMonthViewUI.this.originalDateSpan = BasicMonthViewUI.this.getSelection();
/*      */       }
/*      */       
/*      */ 
/* 2032 */       if ((this.action >= 0) && (this.action <= 1))
/*      */       {
/* 2034 */         if (this.action == 1)
/*      */         {
/* 2036 */           if ((BasicMonthViewUI.this.originalDateSpan != null) && (!BasicMonthViewUI.this.originalDateSpan.isEmpty()))
/*      */           {
/* 2038 */             BasicMonthViewUI.this.monthView.setSelectionInterval((Date)BasicMonthViewUI.this.originalDateSpan.first(), (Date)BasicMonthViewUI.this.originalDateSpan.last());
/*      */           }
/*      */           else
/*      */           {
/* 2042 */             BasicMonthViewUI.this.monthView.clearSelection();
/*      */           }
/* 2044 */           BasicMonthViewUI.this.monthView.cancelSelection();
/*      */         }
/*      */         else {
/* 2047 */           BasicMonthViewUI.this.monthView.commitSelection();
/*      */         }
/* 2049 */         BasicMonthViewUI.this.setUsingKeyboard(false);
/* 2050 */       } else if ((this.action >= 2) && (this.action <= 5))
/*      */       {
/* 2052 */         BasicMonthViewUI.this.setUsingKeyboard(true);
/* 2053 */         BasicMonthViewUI.this.monthView.getSelectionModel().setAdjusting(true);
/* 2054 */         BasicMonthViewUI.this.pivotDate = null;
/* 2055 */         traverse(this.action);
/* 2056 */       } else if ((isIntervalMode()) && (this.action >= 6) && (this.action <= 9))
/*      */       {
/*      */ 
/* 2059 */         BasicMonthViewUI.this.setUsingKeyboard(true);
/* 2060 */         BasicMonthViewUI.this.monthView.getSelectionModel().setAdjusting(true);
/* 2061 */         addToSelection(this.action);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean isIntervalMode()
/*      */     {
/* 2070 */       return BasicMonthViewUI.this.monthView.getSelectionMode() != DateSelectionModel.SelectionMode.SINGLE_SELECTION;
/*      */     }
/*      */     
/*      */     private void traverse(int action) {
/* 2074 */       Date oldStart = BasicMonthViewUI.this.monthView.isSelectionEmpty() ? BasicMonthViewUI.this.monthView.getToday() : BasicMonthViewUI.this.monthView.getFirstSelectionDate();
/*      */       
/* 2076 */       Calendar cal = BasicMonthViewUI.this.getCalendar(oldStart);
/* 2077 */       switch (action) {
/*      */       case 2: 
/* 2079 */         cal.add(5, -1);
/* 2080 */         break;
/*      */       case 3: 
/* 2082 */         cal.add(5, 1);
/* 2083 */         break;
/*      */       case 4: 
/* 2085 */         cal.add(5, -7);
/* 2086 */         break;
/*      */       case 5: 
/* 2088 */         cal.add(5, 7);
/*      */       }
/*      */       
/*      */       
/* 2092 */       Date newStartDate = cal.getTime();
/* 2093 */       if (!newStartDate.equals(oldStart)) {
/* 2094 */         BasicMonthViewUI.this.monthView.setSelectionInterval(newStartDate, newStartDate);
/* 2095 */         BasicMonthViewUI.this.monthView.ensureDateVisible(newStartDate);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void addToSelection(int action)
/*      */     {
/*      */       Date newEndDate;
/*      */       
/*      */       Date selectionStart;
/*      */       
/*      */       Date newStartDate;
/*      */       
/*      */       Date selectionEnd;
/*      */       
/*      */       Date newEndDate;
/*      */       
/* 2113 */       if (!BasicMonthViewUI.this.monthView.isSelectionEmpty()) { Date selectionStart;
/* 2114 */         Date newStartDate = selectionStart = BasicMonthViewUI.this.monthView.getFirstSelectionDate();
/* 2115 */         Date selectionEnd; newEndDate = selectionEnd = BasicMonthViewUI.this.monthView.getLastSelectionDate();
/*      */       } else {
/* 2117 */         newStartDate = selectionStart = BasicMonthViewUI.this.monthView.getToday();
/* 2118 */         newEndDate = selectionEnd = newStartDate;
/*      */       }
/*      */       
/* 2121 */       if (BasicMonthViewUI.this.pivotDate == null) {
/* 2122 */         BasicMonthViewUI.this.pivotDate = newStartDate;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2127 */       Calendar cal = BasicMonthViewUI.this.getCalendar();
/*      */       boolean isStartMoved;
/* 2129 */       boolean isStartMoved; boolean isStartMoved; boolean isStartMoved; switch (action) {
/*      */       case 6:  boolean isStartMoved;
/* 2131 */         if (newEndDate.after(BasicMonthViewUI.this.pivotDate)) {
/* 2132 */           newEndDate = previousDay(cal, newEndDate);
/* 2133 */           isStartMoved = false;
/*      */         } else {
/* 2135 */           newStartDate = previousDay(cal, newStartDate);
/* 2136 */           newEndDate = BasicMonthViewUI.this.pivotDate;
/* 2137 */           isStartMoved = true;
/*      */         }
/* 2139 */         break;
/*      */       case 7: 
/* 2141 */         if (newStartDate.before(BasicMonthViewUI.this.pivotDate)) {
/* 2142 */           newStartDate = nextDay(cal, newStartDate);
/* 2143 */           isStartMoved = true;
/*      */         } else {
/* 2145 */           newEndDate = nextDay(cal, newEndDate);
/* 2146 */           isStartMoved = false;
/* 2147 */           newStartDate = BasicMonthViewUI.this.pivotDate;
/*      */         }
/* 2149 */         break;
/*      */       case 8:  boolean isStartMoved;
/* 2151 */         if (newEndDate.after(BasicMonthViewUI.this.pivotDate)) {
/* 2152 */           Date newTime = previousWeek(cal, newEndDate);
/* 2153 */           if (newTime.after(BasicMonthViewUI.this.pivotDate)) {
/* 2154 */             newEndDate = newTime;
/* 2155 */             isStartMoved = false;
/*      */           } else {
/* 2157 */             newStartDate = newTime;
/* 2158 */             newEndDate = BasicMonthViewUI.this.pivotDate;
/* 2159 */             isStartMoved = true;
/*      */           }
/*      */         } else {
/* 2162 */           newStartDate = previousWeek(cal, newStartDate);
/* 2163 */           isStartMoved = true;
/*      */         }
/* 2165 */         break;
/*      */       case 9:  boolean isStartMoved;
/* 2167 */         if (newStartDate.before(BasicMonthViewUI.this.pivotDate)) {
/* 2168 */           Date newTime = nextWeek(cal, newStartDate);
/* 2169 */           if (newTime.before(BasicMonthViewUI.this.pivotDate)) {
/* 2170 */             newStartDate = newTime;
/* 2171 */             isStartMoved = true;
/*      */           } else {
/* 2173 */             newStartDate = BasicMonthViewUI.this.pivotDate;
/* 2174 */             newEndDate = newTime;
/* 2175 */             isStartMoved = false;
/*      */           }
/*      */         } else {
/* 2178 */           newEndDate = nextWeek(cal, newEndDate);
/* 2179 */           isStartMoved = false;
/*      */         }
/* 2181 */         break;
/* 2182 */       default:  throw new IllegalArgumentException("invalid adjustment action: " + action);
/*      */       }
/*      */       
/* 2185 */       if ((!newStartDate.equals(selectionStart)) || (!newEndDate.equals(selectionEnd))) {
/* 2186 */         BasicMonthViewUI.this.monthView.setSelectionInterval(newStartDate, newEndDate);
/* 2187 */         BasicMonthViewUI.this.monthView.ensureDateVisible(isStartMoved ? newStartDate : newEndDate);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Date nextWeek(Calendar cal, Date date)
/*      */     {
/* 2198 */       cal.setTime(date);
/* 2199 */       cal.add(5, 7);
/* 2200 */       return cal.getTime();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Date previousWeek(Calendar cal, Date date)
/*      */     {
/* 2209 */       cal.setTime(date);
/* 2210 */       cal.add(5, -7);
/* 2211 */       return cal.getTime();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Date nextDay(Calendar cal, Date date)
/*      */     {
/* 2220 */       cal.setTime(date);
/* 2221 */       cal.add(5, 1);
/* 2222 */       return cal.getTime();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Date previousDay(Calendar cal, Date date)
/*      */     {
/* 2231 */       cal.setTime(date);
/* 2232 */       cal.add(5, -1);
/* 2233 */       return cal.getTime();
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
/*      */   protected void updateZoomable()
/*      */   {
/* 2247 */     if (this.monthView.isZoomable()) {
/* 2248 */       this.monthView.add(getCalendarHeaderHandler().getHeaderComponent());
/*      */     } else {
/* 2250 */       this.monthView.remove(getCalendarHeaderHandler().getHeaderComponent());
/*      */     }
/* 2252 */     this.monthView.revalidate();
/* 2253 */     this.monthView.repaint();
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
/*      */   protected CalendarHeaderHandler createCalendarHeaderHandler()
/*      */   {
/* 2272 */     CalendarHeaderHandler handler = getHeaderFromUIManager();
/* 2273 */     return handler != null ? handler : new BasicCalendarHeaderHandler();
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
/*      */   protected CalendarHeaderHandler getHeaderFromUIManager()
/*      */   {
/* 2287 */     Object handlerClass = UIManager.get("CalendarHeaderHandler");
/* 2288 */     if ((handlerClass instanceof String)) {
/* 2289 */       return instantiateClass((String)handlerClass);
/*      */     }
/* 2291 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private CalendarHeaderHandler instantiateClass(String handlerClassName)
/*      */   {
/* 2299 */     Class<?> handler = null;
/*      */     try {
/* 2301 */       handler = Class.forName(handlerClassName);
/* 2302 */       return instantiateClass(handler);
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/* 2305 */       e.printStackTrace();
/*      */     }
/* 2307 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private CalendarHeaderHandler instantiateClass(Class<?> handlerClass)
/*      */   {
/* 2315 */     Constructor<?> constructor = null;
/*      */     try {
/* 2317 */       constructor = handlerClass.getConstructor(new Class[0]);
/*      */     } catch (SecurityException e) {
/* 2319 */       LOG.finer("cant instantiate CalendarHeaderHandler (security) " + handlerClass);
/*      */     } catch (NoSuchMethodException e) {
/* 2321 */       LOG.finer("cant instantiate CalendarHeaderHandler (missing parameterless constructo?)" + handlerClass);
/*      */     }
/* 2323 */     if (constructor != null) {
/*      */       try {
/* 2325 */         return (CalendarHeaderHandler)constructor.newInstance(new Object[0]);
/*      */       } catch (IllegalArgumentException e) {
/* 2327 */         LOG.finer("cant instantiate CalendarHeaderHandler (missing parameterless constructo?)" + handlerClass);
/*      */       } catch (InstantiationException e) {
/* 2329 */         LOG.finer("cant instantiate CalendarHeaderHandler (not instantiable) " + handlerClass);
/*      */       } catch (IllegalAccessException e) {
/* 2331 */         LOG.finer("cant instantiate CalendarHeaderHandler (constructor not public) " + handlerClass);
/*      */       } catch (InvocationTargetException e) {
/* 2333 */         LOG.finer("cant instantiate CalendarHeaderHandler (Invocation target)" + handlerClass);
/*      */       }
/*      */     }
/* 2336 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void setCalendarHeaderHandler(CalendarHeaderHandler calendarHeaderHandler)
/*      */   {
/* 2343 */     this.calendarHeaderHandler = calendarHeaderHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected CalendarHeaderHandler getCalendarHeaderHandler()
/*      */   {
/* 2350 */     return this.calendarHeaderHandler;
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
/*      */   @Deprecated
/*      */   protected Font createDerivedFont()
/*      */   {
/* 2367 */     return this.monthView.getFont().deriveFont(1);
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicMonthViewUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */