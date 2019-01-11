/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.text.DateFormat;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.Format;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.JXMonthView;
/*     */ import org.jdesktop.swingx.decorator.AbstractHighlighter;
/*     */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*     */ import org.jdesktop.swingx.decorator.CompoundHighlighter;
/*     */ import org.jdesktop.swingx.decorator.HighlightPredicate;
/*     */ import org.jdesktop.swingx.decorator.Highlighter;
/*     */ import org.jdesktop.swingx.decorator.PainterHighlighter;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.renderer.CellContext;
/*     */ import org.jdesktop.swingx.renderer.ComponentProvider;
/*     */ import org.jdesktop.swingx.renderer.FormatStringValue;
/*     */ import org.jdesktop.swingx.renderer.LabelProvider;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
/*     */ import org.jdesktop.swingx.renderer.StringValues;
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
/*     */ class BasicCalendarRenderingHandler
/*     */   implements CalendarRenderingHandler
/*     */ {
/*     */   private CalendarCellContext cellContext;
/*     */   private Map<CalendarState, ComponentProvider<?>> providers;
/*     */   private TextCrossingPainter<?> textCross;
/*     */   private Color unselectableDayForeground;
/*     */   private CalendarAdapter calendarAdapter;
/*     */   private CompoundHighlighter highlighter;
/*     */   
/*     */   public BasicCalendarRenderingHandler()
/*     */   {
/*  57 */     install();
/*     */   }
/*     */   
/*     */   private void install() {
/*  61 */     this.unselectableDayForeground = UIManagerExt.getColor("JXMonthView.unselectableDayForeground");
/*  62 */     this.textCross = new TextCrossingPainter();
/*  63 */     this.cellContext = new CalendarCellContext();
/*  64 */     installProviders();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void installProviders()
/*     */   {
/*  71 */     this.providers = new HashMap();
/*     */     
/*  73 */     StringValue sv = createDayStringValue(null);
/*  74 */     ComponentProvider<?> provider = new LabelProvider(sv, 4);
/*  75 */     this.providers.put(CalendarState.IN_MONTH, provider);
/*  76 */     this.providers.put(CalendarState.TODAY, provider);
/*  77 */     this.providers.put(CalendarState.TRAILING, provider);
/*  78 */     this.providers.put(CalendarState.LEADING, provider);
/*     */     
/*  80 */     StringValue wsv = createWeekOfYearStringValue(null);
/*  81 */     ComponentProvider<?> weekOfYearProvider = new LabelProvider(wsv, 4);
/*     */     
/*  83 */     this.providers.put(CalendarState.WEEK_OF_YEAR, weekOfYearProvider);
/*     */     
/*  85 */     ComponentProvider<?> dayOfWeekProvider = new LabelProvider(0)
/*     */     {
/*     */       protected String getValueAsString(CellContext context)
/*     */       {
/*  89 */         Object value = context.getValue();
/*     */         
/*     */ 
/*  92 */         if ((value instanceof Calendar)) {
/*  93 */           int day = ((Calendar)value).get(7);
/*  94 */           return ((JXMonthView)context.getComponent()).getDayOfTheWeek(day);
/*     */         }
/*  96 */         return super.getValueAsString(context);
/*     */       }
/*     */       
/*  99 */     };
/* 100 */     this.providers.put(CalendarState.DAY_OF_WEEK, dayOfWeekProvider);
/*     */     
/* 102 */     StringValue tsv = createMonthHeaderStringValue(null);
/* 103 */     ComponentProvider<?> titleProvider = new LabelProvider(tsv, 0);
/*     */     
/* 105 */     this.providers.put(CalendarState.TITLE, titleProvider);
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
/*     */   protected StringValue createMonthHeaderStringValue(Locale locale)
/*     */   {
/* 118 */     if (locale == null) {
/* 119 */       locale = Locale.getDefault();
/*     */     }
/* 121 */     final String[] monthNames = DateFormatSymbols.getInstance(locale).getMonths();
/* 122 */     StringValue tsv = new StringValue()
/*     */     {
/*     */       public String getString(Object value) {
/* 125 */         if ((value instanceof Calendar)) {
/* 126 */           String month = monthNames[((Calendar)value).get(2)];
/*     */           
/* 128 */           return month + " " + ((Calendar)value).get(1);
/*     */         }
/*     */         
/* 131 */         return StringValues.TO_STRING.getString(value);
/*     */       }
/*     */       
/* 134 */     };
/* 135 */     return tsv;
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
/*     */   protected StringValue createWeekOfYearStringValue(Locale locale)
/*     */   {
/* 148 */     StringValue wsv = new StringValue()
/*     */     {
/*     */       public String getString(Object value) {
/* 151 */         if ((value instanceof Calendar)) {
/* 152 */           value = Integer.valueOf(((Calendar)value).get(3));
/*     */         }
/* 154 */         return StringValues.TO_STRING.getString(value);
/*     */       }
/*     */       
/* 157 */     };
/* 158 */     return wsv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringValue createDayStringValue(Locale locale)
/*     */   {
/* 170 */     if (locale == null) {
/* 171 */       locale = Locale.getDefault();
/*     */     }
/* 173 */     FormatStringValue sv = new FormatStringValue(new SimpleDateFormat("d", locale))
/*     */     {
/*     */       public String getString(Object value)
/*     */       {
/* 177 */         if ((value instanceof Calendar)) {
/* 178 */           ((DateFormat)getFormat()).setTimeZone(((Calendar)value).getTimeZone());
/* 179 */           value = ((Calendar)value).getTime();
/*     */         }
/* 181 */         return super.getString(value);
/*     */       }
/*     */       
/* 184 */     };
/* 185 */     return sv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 195 */     StringValue dayValue = createDayStringValue(locale);
/* 196 */     ((ComponentProvider)this.providers.get(CalendarState.IN_MONTH)).setStringValue(dayValue);
/* 197 */     ((ComponentProvider)this.providers.get(CalendarState.TODAY)).setStringValue(dayValue);
/* 198 */     ((ComponentProvider)this.providers.get(CalendarState.TRAILING)).setStringValue(dayValue);
/* 199 */     ((ComponentProvider)this.providers.get(CalendarState.LEADING)).setStringValue(dayValue);
/*     */     
/* 201 */     ((ComponentProvider)this.providers.get(CalendarState.WEEK_OF_YEAR)).setStringValue(createWeekOfYearStringValue(locale));
/* 202 */     ((ComponentProvider)this.providers.get(CalendarState.TITLE)).setStringValue(createMonthHeaderStringValue(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JComponent prepareRenderingComponent(JXMonthView monthView, Calendar calendar, CalendarState dayState)
/*     */   {
/* 214 */     this.cellContext.installContext(monthView, calendar, isSelected(monthView, calendar, dayState), isFocused(monthView, calendar, dayState), dayState);
/*     */     
/*     */ 
/*     */ 
/* 218 */     JComponent comp = ((ComponentProvider)this.providers.get(dayState)).getRendererComponent(this.cellContext);
/* 219 */     return highlight(comp, monthView, calendar, dayState);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private JComponent highlight(JComponent comp, JXMonthView monthView, Calendar calendar, CalendarState dayState)
/*     */   {
/* 238 */     CalendarAdapter adapter = getCalendarAdapter(monthView, calendar, dayState);
/* 239 */     return (JComponent)getHighlighter().highlight(comp, adapter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Highlighter getHighlighter()
/*     */   {
/* 246 */     if (this.highlighter == null) {
/* 247 */       this.highlighter = new CompoundHighlighter(new Highlighter[0]);
/* 248 */       installHighlighters();
/*     */     }
/* 250 */     return this.highlighter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void installHighlighters()
/*     */   {
/* 257 */     HighlightPredicate boldPredicate = new HighlightPredicate()
/*     */     {
/*     */       public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */       {
/* 261 */         if (!(adapter instanceof CalendarAdapter))
/* 262 */           return false;
/* 263 */         CalendarAdapter ca = (CalendarAdapter)adapter;
/* 264 */         return (CalendarState.DAY_OF_WEEK == ca.getCalendarState()) || (CalendarState.TITLE == ca.getCalendarState());
/*     */       }
/*     */       
/*     */ 
/* 268 */     };
/* 269 */     Highlighter font = new AbstractHighlighter(boldPredicate)
/*     */     {
/*     */ 
/*     */       protected Component doHighlight(Component component, ComponentAdapter adapter)
/*     */       {
/* 274 */         component.setFont(BasicCalendarRenderingHandler.this.getDerivedFont(component.getFont()));
/* 275 */         return component;
/*     */       }
/*     */       
/* 278 */     };
/* 279 */     this.highlighter.addHighlighter(font);
/*     */     
/* 281 */     HighlightPredicate unselectable = new HighlightPredicate()
/*     */     {
/*     */       public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */       {
/* 285 */         if (!(adapter instanceof CalendarAdapter))
/* 286 */           return false;
/* 287 */         return ((CalendarAdapter)adapter).isUnselectable();
/*     */       }
/*     */       
/* 290 */     };
/* 291 */     this.textCross.setForeground(this.unselectableDayForeground);
/* 292 */     Highlighter painterHL = new PainterHighlighter(unselectable, this.textCross);
/* 293 */     this.highlighter.addHighlighter(painterHL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CalendarAdapter getCalendarAdapter(JXMonthView monthView, Calendar calendar, CalendarState dayState)
/*     */   {
/* 305 */     if (this.calendarAdapter == null) {
/* 306 */       this.calendarAdapter = new CalendarAdapter(monthView);
/*     */     }
/* 308 */     return this.calendarAdapter.install(calendar, dayState);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Font getDerivedFont(Font font)
/*     */   {
/* 319 */     return font.deriveFont(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isFocused(JXMonthView monthView, Calendar calendar, CalendarState dayState)
/*     */   {
/* 330 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSelected(JXMonthView monthView, Calendar calendar, CalendarState dayState)
/*     */   {
/* 341 */     if (!isSelectable(dayState)) return false;
/* 342 */     return monthView.isSelected(calendar.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isSelectable(CalendarState dayState)
/*     */   {
/* 351 */     return (CalendarState.IN_MONTH == dayState) || (CalendarState.TODAY == dayState);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicCalendarRenderingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */