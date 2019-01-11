/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.text.DateFormat;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.AbstractSpinnerModel;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JSpinner.DefaultEditor;
/*     */ import javax.swing.JSpinner.NumberEditor;
/*     */ import javax.swing.SpinnerModel;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.JXHyperlink;
/*     */ import org.jdesktop.swingx.JXMonthView;
/*     */ import org.jdesktop.swingx.JXPanel;
/*     */ import org.jdesktop.swingx.renderer.FormatStringValue;
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
/*     */ public class SpinningCalendarHeaderHandler
/*     */   extends CalendarHeaderHandler
/*     */ {
/*     */   public static final String ARROWS_SURROUND_MONTH = "SpinningCalendarHeader.arrowsSurroundMonth";
/*     */   public static final String FOCUSABLE_SPINNER_TEXT = "SpinningCalendarHeader.focusableSpinnerText";
/*  78 */   private static final Logger LOG = Logger.getLogger(SpinningCalendarHeaderHandler.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private SpinnerModel yearSpinnerModel;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeListener monthPropertyListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private FormatStringValue monthStringValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void install(JXMonthView monthView)
/*     */   {
/* 101 */     super.install(monthView);
/* 102 */     getHeaderComponent().setActions(monthView.getActionMap().get("previousMonth"), monthView.getActionMap().get("nextMonth"), getYearSpinnerModel());
/*     */     
/*     */ 
/*     */ 
/* 106 */     componentOrientationChanged();
/* 107 */     monthStringBackgroundChanged();
/* 108 */     fontChanged();
/* 109 */     localeChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstall(JXMonthView monthView)
/*     */   {
/* 120 */     getHeaderComponent().setActions(null, null, null);
/* 121 */     getHeaderComponent().setMonthText("");
/* 122 */     super.uninstall(monthView);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpinningCalendarHeader getHeaderComponent()
/*     */   {
/* 133 */     return (SpinningCalendarHeader)super.getHeaderComponent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SpinningCalendarHeader createCalendarHeader()
/*     */   {
/* 144 */     SpinningCalendarHeader header = new SpinningCalendarHeader();
/* 145 */     if (Boolean.TRUE.equals(Boolean.valueOf(UIManager.getBoolean("SpinningCalendarHeader.focusableSpinnerText")))) {
/* 146 */       header.setSpinnerFocusable(true);
/*     */     }
/* 148 */     if (Boolean.TRUE.equals(Boolean.valueOf(UIManager.getBoolean("SpinningCalendarHeader.arrowsSurroundMonth")))) {
/* 149 */       header.setArrowsSurroundMonth(true);
/*     */     }
/* 151 */     return header;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 160 */     super.installListeners();
/* 161 */     this.monthView.addPropertyChangeListener(getPropertyChangeListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 170 */     this.monthView.removePropertyChangeListener(getPropertyChangeListener());
/* 171 */     super.uninstallListeners();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateFormatters()
/*     */   {
/* 180 */     SimpleDateFormat monthNameFormat = (SimpleDateFormat)DateFormat.getDateInstance(3, this.monthView.getLocale());
/*     */     
/* 182 */     monthNameFormat.applyPattern("MMMM");
/* 183 */     this.monthStringValue = new FormatStringValue(monthNameFormat);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void firstDisplayedDayChanged()
/*     */   {
/* 190 */     ((YearSpinnerModel)getYearSpinnerModel()).fireStateChanged();
/* 191 */     getHeaderComponent().setMonthText(this.monthStringValue.getString(this.monthView.getFirstDisplayedDay()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void localeChanged()
/*     */   {
/* 199 */     updateFormatters();
/* 200 */     firstDisplayedDayChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeListener getPropertyChangeListener()
/*     */   {
/* 212 */     if (this.monthPropertyListener == null) {
/* 213 */       this.monthPropertyListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/* 216 */           if ("firstDisplayedDay".equals(evt.getPropertyName())) {
/* 217 */             SpinningCalendarHeaderHandler.this.firstDisplayedDayChanged();
/* 218 */           } else if ("locale".equals(evt.getPropertyName())) {
/* 219 */             SpinningCalendarHeaderHandler.this.localeChanged();
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/* 226 */     return this.monthPropertyListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getYear()
/*     */   {
/* 237 */     Calendar cal = this.monthView.getCalendar();
/* 238 */     return cal.get(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getPreviousYear()
/*     */   {
/* 250 */     Calendar cal = this.monthView.getCalendar();
/* 251 */     cal.add(1, -1);
/* 252 */     return cal.get(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getNextYear()
/*     */   {
/* 264 */     Calendar cal = this.monthView.getCalendar();
/* 265 */     cal.add(1, 1);
/* 266 */     return cal.get(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean setYear(Object value)
/*     */   {
/* 277 */     int year = ((Integer)value).intValue();
/* 278 */     Calendar cal = this.monthView.getCalendar();
/* 279 */     if (cal.get(1) == year)
/* 280 */       return false;
/* 281 */     cal.set(1, year);
/* 282 */     this.monthView.setFirstDisplayedDay(cal.getTime());
/* 283 */     return true;
/*     */   }
/*     */   
/*     */   private class YearSpinnerModel
/*     */     extends AbstractSpinnerModel
/*     */   {
/*     */     private YearSpinnerModel() {}
/*     */     
/*     */     public Object getNextValue()
/*     */     {
/* 293 */       return Integer.valueOf(SpinningCalendarHeaderHandler.this.getNextYear());
/*     */     }
/*     */     
/*     */     public Object getPreviousValue() {
/* 297 */       return Integer.valueOf(SpinningCalendarHeaderHandler.this.getPreviousYear());
/*     */     }
/*     */     
/*     */     public Object getValue() {
/* 301 */       return Integer.valueOf(SpinningCalendarHeaderHandler.this.getYear());
/*     */     }
/*     */     
/*     */     public void setValue(Object value) {
/* 305 */       if (SpinningCalendarHeaderHandler.this.setYear(value)) {
/* 306 */         fireStateChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     public void fireStateChanged()
/*     */     {
/* 312 */       super.fireStateChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   private SpinnerModel getYearSpinnerModel()
/*     */   {
/* 318 */     if (this.yearSpinnerModel == null) {
/* 319 */       this.yearSpinnerModel = new YearSpinnerModel(null);
/*     */     }
/* 321 */     return this.yearSpinnerModel;
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class SpinningCalendarHeader
/*     */     extends JXPanel
/*     */   {
/*     */     private AbstractButton prevButton;
/*     */     
/*     */     private AbstractButton nextButton;
/*     */     
/*     */     private JLabel monthText;
/*     */     
/*     */     private JSpinner yearSpinner;
/*     */     
/*     */     private boolean surroundMonth;
/*     */     
/*     */     public SpinningCalendarHeader()
/*     */     {
/* 340 */       initComponents();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setActions(Action prev, Action next, SpinnerModel model)
/*     */     {
/* 351 */       this.prevButton.setAction(prev);
/* 352 */       this.nextButton.setAction(next);
/* 353 */       uninstallZoomAction();
/* 354 */       installZoomAction(model);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setSpinnerFocusable(boolean focusable)
/*     */     {
/* 365 */       ((JSpinner.DefaultEditor)this.yearSpinner.getEditor()).getTextField().setFocusable(focusable);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setArrowsSurroundMonth(boolean surroundMonth)
/*     */     {
/* 375 */       if (this.surroundMonth == surroundMonth)
/* 376 */         return;
/* 377 */       this.surroundMonth = surroundMonth;
/* 378 */       removeAll();
/* 379 */       addComponents();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setMonthText(String text)
/*     */     {
/* 388 */       this.monthText.setText(text);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setFont(Font font)
/*     */     {
/* 399 */       super.setFont(font);
/* 400 */       if (this.monthText != null) {
/* 401 */         this.monthText.setFont(font);
/* 402 */         this.yearSpinner.setFont(font);
/* 403 */         this.yearSpinner.getEditor().setFont(font);
/* 404 */         ((JSpinner.DefaultEditor)this.yearSpinner.getEditor()).getTextField().setFont(font);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setBackground(Color bg)
/*     */     {
/* 417 */       super.setBackground(bg);
/* 418 */       for (int i = 0; i < getComponentCount(); i++) {
/* 419 */         getComponent(i).setBackground(bg);
/*     */       }
/* 421 */       if (this.yearSpinner != null) {
/* 422 */         this.yearSpinner.setBackground(bg);
/* 423 */         this.yearSpinner.getEditor().setBackground(bg);
/* 424 */         ((JSpinner.DefaultEditor)this.yearSpinner.getEditor()).getTextField().setBackground(bg);
/*     */       }
/*     */     }
/*     */     
/*     */     private void installZoomAction(SpinnerModel model)
/*     */     {
/* 430 */       if (model == null)
/* 431 */         return;
/* 432 */       this.yearSpinner.setModel(model);
/*     */     }
/*     */     
/*     */     private void uninstallZoomAction() {}
/*     */     
/*     */     private void initComponents()
/*     */     {
/* 439 */       createComponents();
/* 440 */       setLayout(new BoxLayout(this, 2));
/* 441 */       setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
/* 442 */       addComponents();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void addComponents()
/*     */     {
/* 449 */       if (this.surroundMonth) {
/* 450 */         add(this.prevButton);
/* 451 */         add(this.monthText);
/* 452 */         add(this.nextButton);
/* 453 */         add(Box.createHorizontalStrut(5));
/* 454 */         add(this.yearSpinner);
/*     */       } else {
/* 456 */         add(this.prevButton);
/* 457 */         add(Box.createHorizontalGlue());
/* 458 */         add(this.monthText);
/* 459 */         add(Box.createHorizontalStrut(5));
/* 460 */         add(this.yearSpinner);
/* 461 */         add(Box.createHorizontalGlue());
/* 462 */         add(this.nextButton);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void createComponents()
/*     */     {
/* 470 */       this.prevButton = createNavigationButton();
/* 471 */       this.nextButton = createNavigationButton();
/* 472 */       this.monthText = createMonthText();
/* 473 */       this.yearSpinner = createSpinner();
/*     */     }
/*     */     
/*     */     private JLabel createMonthText() {
/* 477 */       JLabel comp = new JLabel()
/*     */       {
/*     */         public Dimension getMaximumSize()
/*     */         {
/* 481 */           Dimension dim = super.getMaximumSize();
/* 482 */           dim.width = Integer.MAX_VALUE;
/* 483 */           dim.height = Integer.MAX_VALUE;
/* 484 */           return dim;
/*     */         }
/*     */         
/* 487 */       };
/* 488 */       comp.setHorizontalAlignment(0);
/* 489 */       return comp;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private JSpinner createSpinner()
/*     */     {
/* 498 */       JSpinner spinner = new JSpinner();
/* 499 */       spinner.setFocusable(false);
/* 500 */       spinner.setBorder(BorderFactory.createEmptyBorder());
/* 501 */       JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner);
/* 502 */       editor.getFormat().setGroupingUsed(false);
/* 503 */       editor.getTextField().setFocusable(false);
/* 504 */       spinner.setEditor(editor);
/* 505 */       return spinner;
/*     */     }
/*     */     
/*     */     private AbstractButton createNavigationButton() {
/* 509 */       JXHyperlink b = new JXHyperlink();
/* 510 */       b.setContentAreaFilled(false);
/* 511 */       b.setBorder(BorderFactory.createEmptyBorder());
/* 512 */       b.setRolloverEnabled(true);
/* 513 */       b.setFocusable(false);
/* 514 */       return b;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\SpinningCalendarHeaderHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */