/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Font;
/*      */ import java.awt.GradientPaint;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.lang.reflect.Array;
/*      */ import java.text.DateFormat;
/*      */ import java.text.Format;
/*      */ import java.text.MessageFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.EventListener;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFormattedTextField;
/*      */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*      */ import javax.swing.JFormattedTextField.AbstractFormatterFactory;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.text.DefaultFormatterFactory;
/*      */ import org.jdesktop.swingx.calendar.DatePickerFormatter;
/*      */ import org.jdesktop.swingx.event.EventListenerMap;
/*      */ import org.jdesktop.swingx.painter.MattePainter;
/*      */ import org.jdesktop.swingx.plaf.DatePickerAddon;
/*      */ import org.jdesktop.swingx.plaf.DatePickerUI;
/*      */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*      */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JXDatePicker
/*      */   extends JComponent
/*      */ {
/*  169 */   private static final Logger LOG = Logger.getLogger(JXDatePicker.class.getName());
/*      */   public static final String uiClassID = "DatePickerUI";
/*      */   
/*  172 */   static { LookAndFeelAddons.contribute(new DatePickerAddon()); }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String EDITOR = "editor";
/*      */   
/*      */ 
/*      */   public static final String MONTH_VIEW = "monthView";
/*      */   
/*      */ 
/*      */   public static final String LINK_PANEL = "linkPanel";
/*      */   
/*      */ 
/*      */   public static final String COMMIT_KEY = "datePickerCommit";
/*      */   
/*      */ 
/*      */   public static final String CANCEL_KEY = "datePickerCancel";
/*      */   
/*      */   public static final String HOME_NAVIGATE_KEY = "navigateHome";
/*      */   
/*      */   public static final String HOME_COMMIT_KEY = "commitHome";
/*      */   
/*  194 */   private static final DateFormat[] EMPTY_DATE_FORMATS = new DateFormat[0];
/*      */   
/*      */ 
/*      */   private JFormattedTextField _dateField;
/*      */   
/*      */ 
/*      */   private JPanel _linkPanel;
/*      */   
/*      */ 
/*      */   private MessageFormat _linkFormat;
/*      */   
/*      */ 
/*      */   private Date linkDate;
/*      */   
/*      */   private JXMonthView _monthView;
/*      */   
/*  210 */   private boolean editable = true;
/*      */   
/*      */   private EventListenerMap listenerMap;
/*  213 */   protected boolean lightWeightPopupEnabled = JPopupMenu.getDefaultLightWeightPopupEnabled();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Date date;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private PropertyChangeListener monthViewListener;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXDatePicker()
/*      */   {
/*  230 */     this(null, null);
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
/*      */   public JXDatePicker(Date selected)
/*      */   {
/*  247 */     this(selected, null);
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
/*      */   public JXDatePicker(Locale locale)
/*      */   {
/*  262 */     this(null, locale);
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
/*      */   public JXDatePicker(Date selection, Locale locale)
/*      */   {
/*  279 */     init();
/*  280 */     if (locale != null) {
/*  281 */       setLocale(locale);
/*      */     }
/*      */     
/*  284 */     updateUI();
/*  285 */     setDate(selection);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDate(Date date)
/*      */   {
/*      */     try
/*      */     {
/*  326 */       date = getUI().getSelectableDate(date);
/*      */     } catch (PropertyVetoException e) {
/*  328 */       return;
/*      */     }
/*  330 */     Date old = getDate();
/*  331 */     this.date = date;
/*  332 */     firePropertyChange("date", old, getDate());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getDate()
/*      */   {
/*  343 */     return this.date;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void init()
/*      */   {
/*  350 */     this.listenerMap = new EventListenerMap();
/*  351 */     initMonthView();
/*      */     
/*  353 */     updateLinkFormat();
/*  354 */     this.linkDate = this._monthView.getToday();
/*  355 */     this._linkPanel = new TodayPanel();
/*      */   }
/*      */   
/*      */   private void initMonthView() {
/*  359 */     this._monthView = new JXMonthView();
/*      */     
/*  361 */     this._monthView.setTraversable(true);
/*  362 */     this._monthView.addPropertyChangeListener(getMonthViewListener());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private PropertyChangeListener getMonthViewListener()
/*      */   {
/*  372 */     if (this.monthViewListener == null) {
/*  373 */       this.monthViewListener = new PropertyChangeListener()
/*      */       {
/*      */         public void propertyChange(PropertyChangeEvent evt) {
/*  376 */           if ("timeZone".equals(evt.getPropertyName())) {
/*  377 */             JXDatePicker.this.updateTimeZone((TimeZone)evt.getOldValue(), (TimeZone)evt.getNewValue());
/*      */           }
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*  384 */     return this.monthViewListener;
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
/*      */   protected void updateTimeZone(TimeZone oldValue, TimeZone newValue)
/*      */   {
/*  399 */     firePropertyChange("timeZone", oldValue, newValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DatePickerUI getUI()
/*      */   {
/*  408 */     return (DatePickerUI)this.ui;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUI(DatePickerUI ui)
/*      */   {
/*  417 */     super.setUI(ui);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  427 */     setUI((DatePickerUI)LookAndFeelAddons.getUI(this, DatePickerUI.class));
/*      */     
/*      */ 
/*  430 */     SwingUtilities.updateComponentTreeUI(getMonthView());
/*  431 */     invalidate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  439 */     return "DatePickerUI";
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
/*      */   public void setFormats(String... formats)
/*      */   {
/*  457 */     DateFormat[] dateFormats = null;
/*  458 */     if (formats != null) {
/*  459 */       Contract.asNotNull(formats, "the array of format strings must not must not contain null elements");
/*      */       
/*      */ 
/*  462 */       dateFormats = new DateFormat[formats.length];
/*  463 */       for (int counter = formats.length - 1; counter >= 0; counter--) {
/*  464 */         dateFormats[counter] = new SimpleDateFormat(formats[counter], getLocale());
/*      */       }
/*      */     }
/*  467 */     setFormats(dateFormats);
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
/*      */   public void setFormats(DateFormat... formats)
/*      */   {
/*  483 */     if (formats != null) {
/*  484 */       Contract.asNotNull(formats, "the array of formats must not contain null elements");
/*      */     }
/*      */     
/*  487 */     DateFormat[] old = getFormats();
/*  488 */     this._dateField.setFormatterFactory(new DefaultFormatterFactory(new DatePickerFormatter(formats, getLocale())));
/*      */     
/*  490 */     firePropertyChange("formats", old, getFormats());
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
/*      */   public DateFormat[] getFormats()
/*      */   {
/*  504 */     JFormattedTextField.AbstractFormatterFactory factory = this._dateField.getFormatterFactory();
/*  505 */     if (factory != null) {
/*  506 */       JFormattedTextField.AbstractFormatter formatter = factory.getFormatter(this._dateField);
/*  507 */       if ((formatter instanceof DatePickerFormatter)) {
/*  508 */         return ((DatePickerFormatter)formatter).getFormats();
/*      */       }
/*      */     }
/*  511 */     return EMPTY_DATE_FORMATS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXMonthView getMonthView()
/*      */   {
/*  521 */     return this._monthView;
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
/*      */   public void setMonthView(JXMonthView monthView)
/*      */   {
/*  536 */     Contract.asNotNull(monthView, "monthView must not be null");
/*  537 */     JXMonthView oldMonthView = getMonthView();
/*  538 */     TimeZone oldTZ = getTimeZone();
/*  539 */     oldMonthView.removePropertyChangeListener(getMonthViewListener());
/*  540 */     this._monthView = monthView;
/*  541 */     getMonthView().addPropertyChangeListener(getMonthViewListener());
/*  542 */     firePropertyChange("monthView", oldMonthView, getMonthView());
/*  543 */     firePropertyChange("timeZone", oldTZ, getTimeZone());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  553 */     return this._monthView.getTimeZone();
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
/*      */   public void setTimeZone(TimeZone tz)
/*      */   {
/*  569 */     this._monthView.setTimeZone(tz);
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
/*      */   public Date getLinkDay()
/*      */   {
/*  582 */     return this.linkDate;
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
/*      */   public void setLinkDay(Date linkDay, String linkFormatString)
/*      */   {
/*  598 */     setLinkFormat(new MessageFormat(linkFormatString));
/*  599 */     setLinkDay(linkDay);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLinkDay(Date linkDay)
/*      */   {
/*  611 */     this.linkDate = linkDay;
/*  612 */     Format[] formats = getLinkFormat().getFormatsByArgumentIndex();
/*  613 */     for (Format format : formats) {
/*  614 */       if ((format instanceof DateFormat)) {
/*  615 */         ((DateFormat)format).setTimeZone(getTimeZone());
/*      */       }
/*      */     }
/*  618 */     setLinkPanel(new TodayPanel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setLinkFormat(MessageFormat _linkFormat)
/*      */   {
/*  626 */     this._linkFormat = _linkFormat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected MessageFormat getLinkFormat()
/*      */   {
/*  633 */     return this._linkFormat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateLinkFormat()
/*      */   {
/*  642 */     String linkFormat = UIManagerExt.getString("JXDatePicker.linkFormat", getLocale());
/*      */     
/*      */ 
/*  645 */     if (linkFormat != null) {
/*  646 */       setLinkFormat(new MessageFormat(linkFormat));
/*      */     } else {
/*  648 */       setLinkFormat(new MessageFormat("{0,date, dd MMMM yyyy}"));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JPanel getLinkPanel()
/*      */   {
/*  659 */     return this._linkPanel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLinkPanel(JPanel linkPanel)
/*      */   {
/*  669 */     JPanel oldLinkPanel = this._linkPanel;
/*  670 */     this._linkPanel = linkPanel;
/*  671 */     firePropertyChange("linkPanel", oldLinkPanel, this._linkPanel);
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
/*      */   public JFormattedTextField getEditor()
/*      */   {
/*  684 */     return this._dateField;
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
/*      */   public void setEditor(JFormattedTextField editor)
/*      */   {
/*  703 */     Contract.asNotNull(editor, "editor must not be null");
/*  704 */     JFormattedTextField oldEditor = this._dateField;
/*  705 */     this._dateField = editor;
/*  706 */     firePropertyChange("editor", oldEditor, this._dateField);
/*      */   }
/*      */   
/*      */   public void setComponentOrientation(ComponentOrientation orientation)
/*      */   {
/*  711 */     super.setComponentOrientation(orientation);
/*  712 */     this._monthView.setComponentOrientation(orientation);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEditValid()
/*      */   {
/*  721 */     return this._dateField.isEditValid();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void commitEdit()
/*      */     throws ParseException
/*      */   {
/*      */     try
/*      */     {
/*  736 */       this._dateField.commitEdit();
/*  737 */       fireActionPerformed("datePickerCommit");
/*      */     }
/*      */     catch (ParseException e) {
/*  740 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void cancelEdit()
/*      */   {
/*  750 */     this._dateField.setValue(this._dateField.getValue());
/*  751 */     fireActionPerformed("datePickerCancel");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEditable(boolean value)
/*      */   {
/*  763 */     boolean oldEditable = isEditable();
/*  764 */     this.editable = value;
/*  765 */     firePropertyChange("editable", oldEditable, this.editable);
/*  766 */     if (this.editable != oldEditable) {
/*  767 */       repaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEditable()
/*      */   {
/*  777 */     return this.editable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  785 */     return getEditor().getFont();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFont(Font font)
/*      */   {
/*  793 */     getEditor().setFont(font);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLightWeightPopupEnabled(boolean aFlag)
/*      */   {
/*  825 */     boolean oldFlag = this.lightWeightPopupEnabled;
/*  826 */     this.lightWeightPopupEnabled = aFlag;
/*  827 */     firePropertyChange("lightWeightPopupEnabled", oldFlag, this.lightWeightPopupEnabled);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLightWeightPopupEnabled()
/*      */   {
/*  839 */     return this.lightWeightPopupEnabled;
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
/*      */   public int getBaseline(int width, int height)
/*      */   {
/*  853 */     return ((DatePickerUI)this.ui).getBaseline(width, height);
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
/*      */   public void addActionListener(ActionListener l)
/*      */   {
/*  866 */     this.listenerMap.add(ActionListener.class, l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeActionListener(ActionListener l)
/*      */   {
/*  875 */     this.listenerMap.remove(ActionListener.class, l);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> listenerType)
/*      */   {
/*  881 */     List<T> listeners = this.listenerMap.getListeners(listenerType);
/*      */     T[] result;
/*  883 */     if (!listeners.isEmpty())
/*      */     {
/*  885 */       T[] result = (EventListener[])Array.newInstance(listenerType, listeners.size());
/*  886 */       result = (EventListener[])listeners.toArray(result);
/*      */     } else {
/*  888 */       result = super.getListeners(listenerType);
/*      */     }
/*  890 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void fireActionPerformed(String actionCommand)
/*      */   {
/*  899 */     ActionListener[] listeners = (ActionListener[])getListeners(ActionListener.class);
/*  900 */     ActionEvent e = null;
/*      */     
/*  902 */     for (ActionListener listener : listeners) {
/*  903 */       if (e == null) {
/*  904 */         e = new ActionEvent(this, 1001, actionCommand);
/*      */       }
/*      */       
/*      */ 
/*  908 */       listener.actionPerformed(e);
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
/*      */   public void addPopupMenuListener(PopupMenuListener l)
/*      */   {
/*  921 */     this.listenerMap.add(PopupMenuListener.class, l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removePopupMenuListener(PopupMenuListener l)
/*      */   {
/*  930 */     this.listenerMap.remove(PopupMenuListener.class, l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PopupMenuListener[] getPopupMenuListeners()
/*      */   {
/*  941 */     return (PopupMenuListener[])getListeners(PopupMenuListener.class);
/*      */   }
/*      */   
/*      */   private final class TodayPanel
/*      */     extends JXPanel
/*      */   {
/*      */     private TodayAction todayAction;
/*      */     private JXHyperlink todayLink;
/*      */     
/*      */     TodayPanel()
/*      */     {
/*  952 */       super();
/*  953 */       setBackgroundPainter(new MattePainter(new GradientPaint(0.0F, 0.0F, new Color(238, 238, 238), 0.0F, 1.0F, Color.WHITE)));
/*  954 */       this.todayAction = new TodayAction();
/*  955 */       this.todayLink = new JXHyperlink(this.todayAction);
/*  956 */       this.todayLink.addMouseListener(createDoubleClickListener());
/*  957 */       Color textColor = new Color(16, 66, 104);
/*  958 */       this.todayLink.setUnclickedColor(textColor);
/*  959 */       this.todayLink.setClickedColor(textColor);
/*  960 */       add(this.todayLink);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private MouseListener createDoubleClickListener()
/*      */     {
/*  967 */       MouseAdapter adapter = new MouseAdapter()
/*      */       {
/*      */         public void mousePressed(MouseEvent e)
/*      */         {
/*  971 */           if (e.getClickCount() != 2) return;
/*  972 */           JXDatePicker.TodayPanel.this.todayAction.select = true;
/*      */         }
/*      */         
/*  975 */       };
/*  976 */       return adapter;
/*      */     }
/*      */     
/*      */     protected void paintComponent(Graphics g)
/*      */     {
/*  981 */       super.paintComponent(g);
/*  982 */       g.setColor(new Color(187, 187, 187));
/*  983 */       g.drawLine(0, 0, getWidth(), 0);
/*  984 */       g.setColor(new Color(221, 221, 221));
/*  985 */       g.drawLine(0, 1, getWidth(), 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setLocale(Locale l)
/*      */     {
/*  994 */       super.setLocale(l);
/*  995 */       JXDatePicker.this.updateLinkFormat();
/*  996 */       this.todayLink.setText(JXDatePicker.this.getLinkFormat().format(new Object[] { JXDatePicker.this.getLinkDay() }));
/*      */     }
/*      */     
/*      */     private final class TodayAction extends AbstractAction {
/*      */       boolean select;
/*      */       
/* 1002 */       TodayAction() { super();
/* 1003 */         Calendar cal = JXDatePicker.this._monthView.getCalendar();
/* 1004 */         cal.setTime(JXDatePicker.this.getLinkDay());
/* 1005 */         putValue("Name", JXDatePicker.this.getLinkFormat().format(new Object[] { cal.getTime() }));
/*      */       }
/*      */       
/*      */       public void actionPerformed(ActionEvent ae) {
/* 1009 */         String key = this.select ? "commitHome" : "navigateHome";
/* 1010 */         this.select = false;
/* 1011 */         Action delegate = JXDatePicker.TodayPanel.this.getActionMap().get(key);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1016 */         if ((delegate != null) && (delegate.isEnabled())) {
/* 1017 */           delegate.actionPerformed(null);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXDatePicker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */