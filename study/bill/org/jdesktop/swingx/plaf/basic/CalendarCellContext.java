/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Calendar;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import org.jdesktop.swingx.JXMonthView;
/*     */ import org.jdesktop.swingx.border.IconBorder;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.renderer.CellContext;
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
/*     */ class CalendarCellContext
/*     */   extends CellContext
/*     */ {
/*  53 */   private int arrowPaddingX = 3;
/*  54 */   private int arrowPaddingY = 3;
/*     */   
/*     */   private CalendarState dayState;
/*     */   
/*     */   public void installContext(JXMonthView component, Calendar value, boolean selected, boolean focused, CalendarState dayState)
/*     */   {
/*  60 */     this.component = component;
/*  61 */     this.dayState = dayState;
/*  62 */     installState(value, -1, -1, selected, focused, true, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public JXMonthView getComponent()
/*     */   {
/*  68 */     return (JXMonthView)super.getComponent();
/*     */   }
/*     */   
/*     */   public CalendarState getCalendarState()
/*     */   {
/*  73 */     return this.dayState;
/*     */   }
/*     */   
/*     */   public Calendar getCalendar()
/*     */   {
/*  78 */     return (getValue() instanceof Calendar) ? (Calendar)getValue() : null;
/*     */   }
/*     */   
/*     */   protected Color getForeground()
/*     */   {
/*  83 */     if (CalendarState.LEADING == this.dayState) {
/*  84 */       return getUIColor("leadingDayForeground");
/*     */     }
/*  86 */     if (CalendarState.TRAILING == this.dayState) {
/*  87 */       return getUIColor("trailingDayForeground");
/*     */     }
/*  89 */     if ((CalendarState.TITLE == this.dayState) && (getComponent() != null)) {
/*  90 */       return getComponent().getMonthStringForeground();
/*     */     }
/*  92 */     if (CalendarState.WEEK_OF_YEAR == this.dayState) {
/*  93 */       Color weekOfTheYearForeground = getUIColor("weekOfTheYearForeground");
/*  94 */       if (weekOfTheYearForeground != null) {
/*  95 */         return weekOfTheYearForeground;
/*     */       }
/*     */     }
/*  98 */     if (CalendarState.DAY_OF_WEEK == this.dayState) {
/*  99 */       Color daysOfTheWeekForeground = getComponent() != null ? getComponent().getDaysOfTheWeekForeground() : null;
/*     */       
/* 101 */       if (daysOfTheWeekForeground != null) {
/* 102 */         return daysOfTheWeekForeground;
/*     */       }
/*     */     }
/*     */     
/* 106 */     Color flaggedOrPerDayForeground = getFlaggedOrPerDayForeground();
/* 107 */     return flaggedOrPerDayForeground != null ? flaggedOrPerDayForeground : super.getForeground();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color getUIColor(String key)
/*     */   {
/* 115 */     return UIManagerExt.getColor(getUIPrefix() + key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getFlaggedOrPerDayForeground()
/*     */   {
/* 126 */     if ((getComponent() != null) && (getCalendar() != null)) {
/* 127 */       if (getComponent().isFlaggedDate(getCalendar().getTime())) {
/* 128 */         return getComponent().getFlaggedDayForeground();
/*     */       }
/* 130 */       Color perDay = getComponent().getPerDayOfWeekForeground(getCalendar().get(7));
/* 131 */       if (perDay != null) {
/* 132 */         return perDay;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   protected Color getBackground()
/*     */   {
/* 142 */     if ((CalendarState.TITLE == this.dayState) && (getComponent() != null)) {
/* 143 */       return getComponent().getMonthStringBackground();
/*     */     }
/* 145 */     return super.getBackground();
/*     */   }
/*     */   
/*     */   protected Color getSelectionBackground()
/*     */   {
/* 150 */     if ((CalendarState.LEADING == this.dayState) || (CalendarState.TRAILING == this.dayState)) return getBackground();
/* 151 */     return getComponent() != null ? getComponent().getSelectionBackground() : null;
/*     */   }
/*     */   
/*     */   protected Color getSelectionForeground()
/*     */   {
/* 156 */     if ((CalendarState.LEADING == this.dayState) || (CalendarState.TRAILING == this.dayState)) return getForeground();
/* 157 */     Color flaggedOrPerDayForeground = getFlaggedOrPerDayForeground();
/* 158 */     if (flaggedOrPerDayForeground != null) {
/* 159 */       return flaggedOrPerDayForeground;
/*     */     }
/* 161 */     return getComponent() != null ? getComponent().getSelectionForeground() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Border getBorder()
/*     */   {
/* 168 */     if (getComponent() == null) {
/* 169 */       return super.getBorder();
/*     */     }
/* 171 */     if (CalendarState.TITLE == this.dayState) {
/* 172 */       return getTitleBorder();
/*     */     }
/* 174 */     if (isToday()) {
/* 175 */       int x = getComponent().getBoxPaddingX();
/* 176 */       int y = getComponent().getBoxPaddingY();
/* 177 */       Border todayBorder = BorderFactory.createLineBorder(getComponent().getTodayBackground());
/* 178 */       Border empty = BorderFactory.createEmptyBorder(y - 1, x - 1, y - 1, x - 1);
/* 179 */       return BorderFactory.createCompoundBorder(todayBorder, empty);
/*     */     }
/* 181 */     return BorderFactory.createEmptyBorder(getComponent().getBoxPaddingY(), getComponent().getBoxPaddingX(), getComponent().getBoxPaddingY(), getComponent().getBoxPaddingX());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Border getTitleBorder()
/*     */   {
/* 188 */     if (getComponent().isTraversable()) {
/* 189 */       Icon downIcon = UIManager.getIcon("JXMonthView.monthDownFileName");
/* 190 */       Icon upIcon = UIManager.getIcon("JXMonthView.monthUpFileName");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 195 */       IconBorder up = new IconBorder(upIcon, 3, this.arrowPaddingX);
/* 196 */       IconBorder down = new IconBorder(downIcon, 7, this.arrowPaddingX);
/* 197 */       Border compound = BorderFactory.createCompoundBorder(up, down);
/* 198 */       Border empty = BorderFactory.createEmptyBorder(2 * this.arrowPaddingY, 0, 2 * this.arrowPaddingY, 0);
/* 199 */       return BorderFactory.createCompoundBorder(compound, empty);
/*     */     }
/*     */     
/* 202 */     return BorderFactory.createEmptyBorder(getComponent().getBoxPaddingY(), getComponent().getBoxPaddingX(), getComponent().getBoxPaddingY(), getComponent().getBoxPaddingX());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isToday()
/*     */   {
/* 209 */     return CalendarState.TODAY == this.dayState;
/*     */   }
/*     */   
/*     */   protected String getUIPrefix()
/*     */   {
/* 214 */     return "JXMonthView.";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\CalendarCellContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */