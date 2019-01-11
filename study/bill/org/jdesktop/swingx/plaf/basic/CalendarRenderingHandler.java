package org.jdesktop.swingx.plaf.basic;

import java.util.Calendar;
import java.util.Locale;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXMonthView;

public abstract interface CalendarRenderingHandler
{
  public abstract JComponent prepareRenderingComponent(JXMonthView paramJXMonthView, Calendar paramCalendar, CalendarState paramCalendarState);
  
  public abstract void setLocale(Locale paramLocale);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\CalendarRenderingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */