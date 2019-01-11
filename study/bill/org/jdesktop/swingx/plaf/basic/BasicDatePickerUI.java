/*      */ package org.jdesktop.swingx.plaf.basic;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFormattedTextField;
/*      */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*      */ import javax.swing.JFormattedTextField.AbstractFormatterFactory;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.DefaultFormatterFactory;
/*      */ import javax.swing.text.View;
/*      */ import org.jdesktop.swingx.JXDatePicker;
/*      */ import org.jdesktop.swingx.JXMonthView;
/*      */ import org.jdesktop.swingx.SwingXUtilities;
/*      */ import org.jdesktop.swingx.calendar.CalendarUtils;
/*      */ import org.jdesktop.swingx.calendar.DatePickerFormatter;
/*      */ import org.jdesktop.swingx.calendar.DatePickerFormatter.DatePickerFormatterUIResource;
/*      */ import org.jdesktop.swingx.calendar.DateSelectionModel;
/*      */ import org.jdesktop.swingx.event.DateSelectionEvent;
/*      */ import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
/*      */ import org.jdesktop.swingx.event.DateSelectionListener;
/*      */ import org.jdesktop.swingx.plaf.DatePickerUI;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BasicDatePickerUI
/*      */   extends DatePickerUI
/*      */ {
/*   95 */   private static final Logger LOG = Logger.getLogger(BasicDatePickerUI.class.getName());
/*      */   
/*      */ 
/*      */   protected JXDatePicker datePicker;
/*      */   
/*      */ 
/*      */   private JButton popupButton;
/*      */   
/*      */ 
/*      */   private BasicDatePickerPopup popup;
/*      */   
/*      */   private Handler handler;
/*      */   
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   
/*      */   private FocusListener focusListener;
/*      */   
/*      */   protected MouseListener mouseListener;
/*      */   
/*      */   protected MouseMotionListener mouseMotionListener;
/*      */   
/*      */   private ActionListener editorActionListener;
/*      */   
/*      */   private EditorCancelAction editorCancelAction;
/*      */   
/*      */   private PropertyChangeListener editorPropertyListener;
/*      */   
/*      */   private DateSelectionListener monthViewSelectionListener;
/*      */   
/*      */   private ActionListener monthViewActionListener;
/*      */   
/*      */   private PropertyChangeListener monthViewPropertyListener;
/*      */   
/*      */   private PopupRemover popupRemover;
/*      */   
/*      */   private PopupMenuListener popupMenuListener;
/*      */   
/*      */ 
/*      */   public static ComponentUI createUI(JComponent c)
/*      */   {
/*  135 */     return new BasicDatePickerUI();
/*      */   }
/*      */   
/*      */   public void installUI(JComponent c)
/*      */   {
/*  140 */     this.datePicker = ((JXDatePicker)c);
/*  141 */     this.datePicker.setLayout(createLayoutManager());
/*  142 */     installComponents();
/*  143 */     installDefaults();
/*  144 */     installKeyboardActions();
/*  145 */     installListeners();
/*      */   }
/*      */   
/*      */   public void uninstallUI(JComponent c)
/*      */   {
/*  150 */     uninstallListeners();
/*  151 */     uninstallKeyboardActions();
/*  152 */     uninstallDefaults();
/*  153 */     uninstallComponents();
/*  154 */     this.datePicker.setLayout(null);
/*  155 */     this.datePicker = null;
/*      */   }
/*      */   
/*      */   protected void installComponents()
/*      */   {
/*  160 */     JFormattedTextField editor = this.datePicker.getEditor();
/*  161 */     if (SwingXUtilities.isUIInstallable(editor)) {
/*  162 */       DateFormat[] formats = getCustomFormats(editor);
/*      */       
/*  164 */       this.datePicker.setEditor(createEditor());
/*  165 */       if (formats != null) {
/*  166 */         this.datePicker.setFormats(formats);
/*      */       }
/*      */     }
/*  169 */     updateFromEditorChanged(null, false);
/*      */     
/*  171 */     this.popupButton = createPopupButton();
/*  172 */     if (this.popupButton != null)
/*      */     {
/*      */ 
/*  175 */       JComboBox box = new JComboBox();
/*  176 */       Object preventHide = box.getClientProperty("doNotCancelPopup");
/*  177 */       this.popupButton.putClientProperty("doNotCancelPopup", preventHide);
/*  178 */       this.datePicker.add(this.popupButton);
/*  179 */       this.popupButton.setEnabled(this.datePicker.isEnabled());
/*      */     }
/*  181 */     updateChildLocale(this.datePicker.getLocale());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private DateFormat[] getCustomFormats(JFormattedTextField editor)
/*      */   {
/*  193 */     DateFormat[] formats = null;
/*  194 */     if (editor != null) {
/*  195 */       JFormattedTextField.AbstractFormatterFactory factory = editor.getFormatterFactory();
/*  196 */       if (factory != null) {
/*  197 */         JFormattedTextField.AbstractFormatter formatter = factory.getFormatter(editor);
/*      */         
/*      */ 
/*  200 */         if (((formatter instanceof DatePickerFormatter)) && (!(formatter instanceof UIResource)))
/*      */         {
/*  202 */           formats = ((DatePickerFormatter)formatter).getFormats();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  207 */     return formats;
/*      */   }
/*      */   
/*      */   protected void uninstallComponents() {
/*  211 */     JFormattedTextField editor = this.datePicker.getEditor();
/*  212 */     if (editor != null) {
/*  213 */       this.datePicker.remove(editor);
/*      */     }
/*      */     
/*  216 */     if (this.popupButton != null) {
/*  217 */       this.datePicker.remove(this.popupButton);
/*  218 */       this.popupButton = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  227 */     boolean zoomable = Boolean.TRUE.equals(UIManager.get("JXDatePicker.forceZoomable"));
/*  228 */     if (zoomable) {
/*  229 */       this.datePicker.getMonthView().setZoomable(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void uninstallDefaults() {}
/*      */   
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  239 */     ActionMap pickerMap = this.datePicker.getActionMap();
/*  240 */     pickerMap.put("datePickerCancel", createCancelAction());
/*  241 */     pickerMap.put("datePickerCommit", createCommitAction());
/*  242 */     pickerMap.put("navigateHome", createHomeAction(false));
/*  243 */     pickerMap.put("commitHome", createHomeAction(true));
/*  244 */     TogglePopupAction popupAction = createTogglePopupAction();
/*  245 */     pickerMap.put("TOGGLE_POPUP", popupAction);
/*      */     
/*  247 */     InputMap pickerInputMap = this.datePicker.getInputMap(1);
/*  248 */     pickerInputMap.put(KeyStroke.getKeyStroke("ENTER"), "datePickerCommit");
/*  249 */     pickerInputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "datePickerCancel");
/*      */     
/*  251 */     pickerInputMap.put(KeyStroke.getKeyStroke("F5"), "commitHome");
/*  252 */     pickerInputMap.put(KeyStroke.getKeyStroke("shift F5"), "navigateHome");
/*  253 */     pickerInputMap.put(KeyStroke.getKeyStroke("alt DOWN"), "TOGGLE_POPUP");
/*      */     
/*  255 */     installLinkPanelKeyboardActions();
/*      */   }
/*      */   
/*      */   protected void uninstallKeyboardActions() {
/*  259 */     uninstallLinkPanelKeyboardActions(this.datePicker.getLinkPanel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void installLinkPanelKeyboardActions()
/*      */   {
/*  270 */     if (this.datePicker.getLinkPanel() == null)
/*  271 */       return;
/*  272 */     ActionMap map = this.datePicker.getLinkPanel().getActionMap();
/*  273 */     map.put("commitHome", this.datePicker.getActionMap().get("commitHome"));
/*      */     
/*  275 */     map.put("navigateHome", this.datePicker.getActionMap().get("navigateHome"));
/*      */     
/*  277 */     InputMap inputMap = this.datePicker.getLinkPanel().getInputMap(2);
/*      */     
/*      */ 
/*  280 */     inputMap.put(KeyStroke.getKeyStroke("F5"), "commitHome");
/*      */     
/*  282 */     inputMap.put(KeyStroke.getKeyStroke("shift F5"), "navigateHome");
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
/*      */   protected void uninstallLinkPanelKeyboardActions(JComponent panel)
/*      */   {
/*  295 */     if (panel == null) return;
/*  296 */     ActionMap map = panel.getActionMap();
/*  297 */     map.remove("commitHome");
/*  298 */     map.remove("navigateHome");
/*  299 */     InputMap inputMap = panel.getInputMap(2);
/*      */     
/*  301 */     inputMap.remove(KeyStroke.getKeyStroke("F5"));
/*  302 */     inputMap.remove(KeyStroke.getKeyStroke("shift F5"));
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
/*      */   protected void installListeners()
/*      */   {
/*  315 */     this.propertyChangeListener = createPropertyChangeListener();
/*      */     
/*      */ 
/*  318 */     this.mouseListener = createMouseListener();
/*  319 */     this.mouseMotionListener = createMouseMotionListener();
/*      */     
/*      */ 
/*  322 */     this.focusListener = createFocusListener();
/*      */     
/*      */ 
/*  325 */     this.editorActionListener = createEditorActionListener();
/*  326 */     this.editorPropertyListener = createEditorPropertyListener();
/*      */     
/*      */ 
/*  329 */     this.monthViewSelectionListener = createMonthViewSelectionListener();
/*  330 */     this.monthViewActionListener = createMonthViewActionListener();
/*  331 */     this.monthViewPropertyListener = createMonthViewPropertyListener();
/*      */     
/*  333 */     this.popupRemover = new PopupRemover();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  338 */     this.datePicker.addPropertyChangeListener(this.propertyChangeListener);
/*  339 */     this.datePicker.addFocusListener(this.focusListener);
/*      */     
/*  341 */     if (this.popupButton != null)
/*      */     {
/*  343 */       this.popupButton.addPropertyChangeListener(this.propertyChangeListener);
/*  344 */       this.popupButton.addMouseListener(this.mouseListener);
/*  345 */       this.popupButton.addMouseMotionListener(this.mouseMotionListener);
/*      */     }
/*      */     
/*  348 */     updateEditorListeners(null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  355 */     updateFromMonthViewChanged(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  364 */     this.datePicker.removePropertyChangeListener(this.propertyChangeListener);
/*  365 */     this.datePicker.removeFocusListener(this.focusListener);
/*      */     
/*      */ 
/*  368 */     this.datePicker.getMonthView().getSelectionModel().removeDateSelectionListener(this.monthViewSelectionListener);
/*  369 */     this.datePicker.getMonthView().removeActionListener(this.monthViewActionListener);
/*  370 */     this.datePicker.getMonthView().removePropertyChangeListener(this.propertyChangeListener);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  375 */     if (this.datePicker.getEditor() != null) {
/*  376 */       uninstallEditorListeners(this.datePicker.getEditor());
/*      */     }
/*  378 */     if (this.popupButton != null) {
/*  379 */       this.popupButton.removePropertyChangeListener(this.propertyChangeListener);
/*  380 */       this.popupButton.removeMouseListener(this.mouseListener);
/*  381 */       this.popupButton.removeMouseMotionListener(this.mouseMotionListener);
/*      */     }
/*      */     
/*  384 */     this.popupRemover.unload();
/*      */     
/*  386 */     this.popupRemover = null;
/*  387 */     this.propertyChangeListener = null;
/*  388 */     this.mouseListener = null;
/*  389 */     this.mouseMotionListener = null;
/*      */     
/*  391 */     this.editorActionListener = null;
/*  392 */     this.editorPropertyListener = null;
/*      */     
/*  394 */     this.monthViewSelectionListener = null;
/*  395 */     this.monthViewActionListener = null;
/*  396 */     this.monthViewPropertyListener = null;
/*      */     
/*  398 */     this.handler = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateMonthViewListeners(JXMonthView oldMonthView)
/*      */   {
/*  410 */     DateSelectionModel oldModel = null;
/*  411 */     if (oldMonthView != null) {
/*  412 */       oldMonthView.removePropertyChangeListener(this.monthViewPropertyListener);
/*  413 */       oldMonthView.removeActionListener(this.monthViewActionListener);
/*  414 */       oldModel = oldMonthView.getSelectionModel();
/*      */     }
/*  416 */     this.datePicker.getMonthView().addPropertyChangeListener(this.monthViewPropertyListener);
/*  417 */     this.datePicker.getMonthView().addActionListener(this.monthViewActionListener);
/*  418 */     updateSelectionModelListeners(oldModel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateEditorListeners(JFormattedTextField oldEditor)
/*      */   {
/*  430 */     if (oldEditor != null) {
/*  431 */       uninstallEditorListeners(oldEditor);
/*      */     }
/*  433 */     this.datePicker.getEditor().addPropertyChangeListener(this.editorPropertyListener);
/*  434 */     this.datePicker.getEditor().addActionListener(this.editorActionListener);
/*  435 */     this.datePicker.getEditor().addFocusListener(this.focusListener);
/*  436 */     this.editorCancelAction = new EditorCancelAction(this.datePicker.getEditor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void uninstallEditorListeners(JFormattedTextField oldEditor)
/*      */   {
/*  446 */     oldEditor.removePropertyChangeListener(this.editorPropertyListener);
/*  447 */     oldEditor.removeActionListener(this.editorActionListener);
/*  448 */     oldEditor.removeFocusListener(this.focusListener);
/*  449 */     if (this.editorCancelAction != null) {
/*  450 */       this.editorCancelAction.uninstall();
/*  451 */       this.editorCancelAction = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateSelectionModelListeners(DateSelectionModel oldModel)
/*      */   {
/*  462 */     if (oldModel != null) {
/*  463 */       oldModel.removeDateSelectionListener(this.monthViewSelectionListener);
/*      */     }
/*  465 */     this.datePicker.getMonthView().getSelectionModel().addDateSelectionListener(this.monthViewSelectionListener);
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
/*      */   protected JFormattedTextField createEditor()
/*      */   {
/*  479 */     JFormattedTextField f = new DefaultEditor(new DatePickerFormatter.DatePickerFormatterUIResource(this.datePicker.getLocale()));
/*      */     
/*  481 */     f.setName("dateField");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  489 */     Border border = UIManager.getBorder("JXDatePicker.border");
/*  490 */     if (border != null) {
/*  491 */       f.setBorder(border);
/*      */     }
/*  493 */     return f;
/*      */   }
/*      */   
/*      */   protected JButton createPopupButton() {
/*  497 */     JButton b = new JButton();
/*  498 */     b.setName("popupButton");
/*  499 */     b.setRolloverEnabled(false);
/*  500 */     b.setMargin(new Insets(0, 3, 0, 3));
/*      */     
/*  502 */     Icon icon = UIManager.getIcon("JXDatePicker.arrowIcon");
/*  503 */     if (icon == null) {
/*  504 */       icon = (Icon)UIManager.get("Tree.expandedIcon");
/*      */     }
/*  506 */     b.setIcon(icon);
/*  507 */     b.setFocusable(false);
/*  508 */     return b;
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
/*      */   private class DefaultEditor
/*      */     extends JFormattedTextField
/*      */     implements UIResource
/*      */   {
/*      */     public DefaultEditor(JFormattedTextField.AbstractFormatter formatter)
/*      */     {
/*  527 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension getPreferredSize()
/*      */     {
/*  537 */       Dimension preferredSize = super.getPreferredSize();
/*  538 */       if (getColumns() <= 0) {
/*  539 */         Dimension compare = getCompareMinimumSize();
/*  540 */         if (preferredSize.width < compare.width) {
/*  541 */           return compare;
/*      */         }
/*      */       }
/*  544 */       return preferredSize;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension getMinimumSize()
/*      */     {
/*  554 */       return getPreferredSize();
/*      */     }
/*      */     
/*      */     private Dimension getCompareMinimumSize() {
/*  558 */       JFormattedTextField field = new JFormattedTextField(getFormatter());
/*  559 */       field.setMargin(getMargin());
/*  560 */       field.setBorder(getBorder());
/*  561 */       field.setFont(getFont());
/*  562 */       field.setValue(new Date());
/*  563 */       Dimension min = field.getPreferredSize();
/*  564 */       field.setValue(null);
/*  565 */       min.width += Math.max(field.getPreferredSize().width, 4);
/*  566 */       return min;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent c)
/*      */   {
/*  578 */     return getPreferredSize(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent c)
/*      */   {
/*  586 */     Dimension dim = this.datePicker.getEditor().getPreferredSize();
/*  587 */     if (this.popupButton != null) {
/*  588 */       dim.width += this.popupButton.getPreferredSize().width;
/*      */     }
/*  590 */     Insets insets = this.datePicker.getInsets();
/*  591 */     dim.width += insets.left + insets.right;
/*  592 */     dim.height += insets.top + insets.bottom;
/*  593 */     return (Dimension)dim.clone();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getBaseline(int width, int height)
/*      */   {
/*  599 */     JFormattedTextField editor = this.datePicker.getEditor();
/*  600 */     View rootView = editor.getUI().getRootView(editor);
/*  601 */     if (rootView.getViewCount() > 0) {
/*  602 */       Insets insets = editor.getInsets();
/*  603 */       Insets insetsOut = this.datePicker.getInsets();
/*  604 */       int nh = height - insets.top - insets.bottom - insetsOut.top - insetsOut.bottom;
/*      */       
/*  606 */       int y = insets.top + insetsOut.top;
/*  607 */       View fieldView = rootView.getView(0);
/*  608 */       int vspan = (int)fieldView.getPreferredSpan(1);
/*  609 */       if (nh != vspan) {
/*  610 */         int slop = nh - vspan;
/*  611 */         y += slop / 2;
/*      */       }
/*  613 */       FontMetrics fm = editor.getFontMetrics(editor.getFont());
/*  614 */       y += fm.getAscent();
/*  615 */       return y;
/*      */     }
/*  617 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date getSelectableDate(Date date)
/*      */     throws PropertyVetoException
/*      */   {
/*  628 */     Date cleaned = date == null ? null : this.datePicker.getMonthView().getSelectionModel().getNormalizedDate(date);
/*      */     
/*  630 */     if (CalendarUtils.areEqual(cleaned, this.datePicker.getDate()))
/*      */     {
/*  632 */       throw new PropertyVetoException("date not selectable", null);
/*      */     }
/*  634 */     if (cleaned == null) return cleaned;
/*  635 */     if (this.datePicker.getMonthView().isUnselectableDate(cleaned)) {
/*  636 */       throw new PropertyVetoException("date not selectable", null);
/*      */     }
/*  638 */     return cleaned;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateFromDateChanged()
/*      */   {
/*  646 */     Date visibleHook = this.datePicker.getDate() != null ? this.datePicker.getDate() : this.datePicker.getLinkDay();
/*      */     
/*  648 */     this.datePicker.getMonthView().ensureDateVisible(visibleHook);
/*  649 */     this.datePicker.getEditor().setValue(this.datePicker.getDate());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateFromValueChanged(Date oldDate, Date newDate)
/*      */   {
/*  661 */     if ((newDate != null) && (this.datePicker.getMonthView().isUnselectableDate(newDate))) {
/*  662 */       revertValue(oldDate);
/*  663 */       return;
/*      */     }
/*      */     
/*  666 */     if (!CalendarUtils.areEqual(newDate, this.datePicker.getMonthView().getSelectionDate())) {
/*  667 */       this.datePicker.getMonthView().setSelectionDate(newDate);
/*      */     }
/*  669 */     this.datePicker.setDate(newDate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void revertValue(Date oldDate)
/*      */   {
/*  679 */     this.datePicker.getEditor().setValue(oldDate);
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
/*      */   protected void updateFromSelectionChanged(DateSelectionEvent.EventType eventType, boolean adjusting)
/*      */   {
/*  695 */     if (adjusting) return;
/*  696 */     updateEditorValue();
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
/*      */   protected void updateFromMonthViewChanged(JXMonthView oldMonthView)
/*      */   {
/*  709 */     uninstallPopup();
/*  710 */     updateMonthViewListeners(oldMonthView);
/*  711 */     TimeZone oldTimeZone = null;
/*  712 */     if (oldMonthView != null) {
/*  713 */       oldMonthView.setComponentInputMapEnabled(false);
/*  714 */       oldTimeZone = oldMonthView.getTimeZone();
/*      */     }
/*  716 */     this.datePicker.getMonthView().setComponentInputMapEnabled(true);
/*  717 */     updateTimeZone(oldTimeZone);
/*  718 */     updateEditorValue();
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
/*      */   protected void updateFromEditorChanged(JFormattedTextField oldEditor, boolean updateListeners)
/*      */   {
/*  740 */     if (oldEditor != null) {
/*  741 */       this.datePicker.remove(oldEditor);
/*  742 */       oldEditor.putClientProperty("doNotCancelPopup", null);
/*      */     }
/*  744 */     this.datePicker.add(this.datePicker.getEditor());
/*      */     
/*      */ 
/*  747 */     JComboBox box = new JComboBox();
/*  748 */     Object preventHide = box.getClientProperty("doNotCancelPopup");
/*  749 */     this.datePicker.getEditor().putClientProperty("doNotCancelPopup", preventHide);
/*      */     
/*  751 */     updateEditorValue();
/*  752 */     updateEditorProperties();
/*  753 */     if (updateListeners) {
/*  754 */       updateEditorListeners(oldEditor);
/*  755 */       this.datePicker.revalidate();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateEditorProperties()
/*      */   {
/*  765 */     this.datePicker.getEditor().setEnabled(this.datePicker.isEnabled());
/*  766 */     this.datePicker.getEditor().setEditable(this.datePicker.isEditable());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateFromSelectionModelChanged(DateSelectionModel oldModel)
/*      */   {
/*  775 */     updateSelectionModelListeners(oldModel);
/*  776 */     updateEditorValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateEditorValue()
/*      */   {
/*  783 */     this.datePicker.getEditor().setValue(this.datePicker.getMonthView().getSelectionDate());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateFromEditableChanged()
/*      */   {
/*  794 */     boolean isEditable = this.datePicker.isEditable();
/*      */     
/*  796 */     this.datePicker.getMonthView().setEnabled(isEditable);
/*  797 */     this.datePicker.getEditor().setEditable(isEditable);
/*      */     
/*      */ 
/*      */ 
/*  801 */     setActionEnabled("commitHome", isEditable);
/*      */     
/*  803 */     setActionEnabled("navigateHome", isEditable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void updateFromEnabledChanged()
/*      */   {
/*  810 */     boolean isEnabled = this.datePicker.isEnabled();
/*  811 */     this.popupButton.setEnabled(isEnabled);
/*  812 */     this.datePicker.getEditor().setEnabled(isEnabled);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setActionEnabled(String key, boolean enabled)
/*      */   {
/*  822 */     Action action = this.datePicker.getActionMap().get(key);
/*  823 */     if (action != null) {
/*  824 */       action.setEnabled(enabled);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateFormatsFromTimeZone(TimeZone zone)
/*      */   {
/*  833 */     for (DateFormat format : this.datePicker.getFormats()) {
/*  834 */       format.setTimeZone(zone);
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
/*      */   protected void updateTimeZone(TimeZone old)
/*      */   {
/*  847 */     updateFormatsFromTimeZone(this.datePicker.getTimeZone());
/*  848 */     updateLinkDate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void updateLinkDate()
/*      */   {
/*  855 */     this.datePicker.setLinkDay(this.datePicker.getMonthView().getToday());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateLocale()
/*      */   {
/*  865 */     Locale locale = this.datePicker.getLocale();
/*  866 */     updateFormatLocale(locale);
/*  867 */     updateChildLocale(locale);
/*      */   }
/*      */   
/*      */   private void updateFormatLocale(Locale locale) {
/*  871 */     if (locale != null)
/*      */     {
/*  873 */       if (getCustomFormats(this.datePicker.getEditor()) == null) {
/*  874 */         this.datePicker.getEditor().setFormatterFactory(new DefaultFormatterFactory(new DatePickerFormatter.DatePickerFormatterUIResource(locale)));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void updateChildLocale(Locale locale)
/*      */   {
/*  882 */     if (locale != null) {
/*  883 */       this.datePicker.getEditor().setLocale(locale);
/*  884 */       this.datePicker.getLinkPanel().setLocale(locale);
/*  885 */       this.datePicker.getMonthView().setLocale(locale);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateLinkPanel(JComponent oldLinkPanel)
/*      */   {
/*  894 */     if (oldLinkPanel != null) {
/*  895 */       uninstallLinkPanelKeyboardActions(oldLinkPanel);
/*      */     }
/*  897 */     installLinkPanelKeyboardActions();
/*  898 */     if (this.popup != null) {
/*  899 */       this.popup.updateLinkPanel(oldLinkPanel);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void commit()
/*      */   {
/*  910 */     hidePopup();
/*      */     try {
/*  912 */       this.datePicker.commitEdit();
/*      */     }
/*      */     catch (ParseException ex) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void cancel()
/*      */   {
/*  922 */     if (isPopupVisible()) {
/*  923 */       this.popup.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
/*      */     }
/*  925 */     hidePopup();
/*  926 */     this.datePicker.cancelEdit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void hidePopup()
/*      */   {
/*  934 */     if (this.popup != null) this.popup.setVisible(false);
/*      */   }
/*      */   
/*      */   public boolean isPopupVisible() {
/*  938 */     if (this.popup != null) {
/*  939 */       return this.popup.isVisible();
/*      */     }
/*  941 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void home(boolean commit)
/*      */   {
/*  952 */     if (commit) {
/*  953 */       Calendar cal = this.datePicker.getMonthView().getCalendar();
/*  954 */       cal.setTime(this.datePicker.getLinkDay());
/*  955 */       this.datePicker.getMonthView().setSelectionDate(cal.getTime());
/*  956 */       this.datePicker.getMonthView().commitSelection();
/*      */     } else {
/*  958 */       this.datePicker.getMonthView().ensureDateVisible(this.datePicker.getLinkDay());
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
/*      */   private Action createCommitAction()
/*      */   {
/*  971 */     Action action = new AbstractAction()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/*  974 */         BasicDatePickerUI.this.commit();
/*      */       }
/*      */       
/*  977 */     };
/*  978 */     return action;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Action createCancelAction()
/*      */   {
/*  988 */     Action action = new AbstractAction()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/*  991 */         BasicDatePickerUI.this.cancel();
/*      */       }
/*      */       
/*  994 */     };
/*  995 */     return action;
/*      */   }
/*      */   
/*      */   private Action createHomeAction(final boolean commit) {
/*  999 */     Action action = new AbstractAction()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/* 1002 */         BasicDatePickerUI.this.home(commit);
/*      */       }
/*      */       
/*      */ 
/* 1006 */     };
/* 1007 */     return action;
/*      */   }
/*      */   
/*      */ 
/*      */   public class EditorCancelAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private JFormattedTextField editor;
/*      */     
/*      */     private Action cancelAction;
/*      */     public static final String TEXT_CANCEL_KEY = "reset-field-edit";
/*      */     
/*      */     public EditorCancelAction(JFormattedTextField field)
/*      */     {
/* 1021 */       install(field);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void uninstall()
/*      */     {
/* 1032 */       this.editor.getActionMap().remove("reset-field-edit");
/* 1033 */       this.cancelAction = null;
/* 1034 */       this.editor = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void install(JFormattedTextField editor)
/*      */     {
/* 1041 */       this.editor = editor;
/* 1042 */       this.cancelAction = editor.getActionMap().get("reset-field-edit");
/* 1043 */       editor.getActionMap().put("reset-field-edit", this);
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1047 */       this.cancelAction.actionPerformed(null);
/* 1048 */       BasicDatePickerUI.this.cancel();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TogglePopupAction createTogglePopupAction()
/*      */   {
/* 1059 */     return new TogglePopupAction();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void toggleShowPopup()
/*      */   {
/* 1069 */     if (this.popup == null) {
/* 1070 */       installPopup();
/*      */     }
/* 1072 */     if (this.popup.isVisible()) {
/* 1073 */       this.popup.setVisible(false);
/*      */     }
/*      */     else
/*      */     {
/* 1077 */       this.datePicker.getEditor().requestFocusInWindow();
/*      */       
/* 1079 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/* 1081 */           BasicDatePickerUI.this.popup.show(BasicDatePickerUI.this.datePicker, 0, BasicDatePickerUI.this.datePicker.getHeight());
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void installPopup()
/*      */   {
/* 1094 */     this.popup = createMonthViewPopup();
/* 1095 */     this.popup.addPopupMenuListener(getPopupMenuListener());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void uninstallPopup()
/*      */   {
/* 1105 */     if (this.popup != null) {
/* 1106 */       this.popup.removePopupMenuListener(getPopupMenuListener());
/*      */     }
/* 1108 */     this.popup = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PopupMenuListener getPopupMenuListener()
/*      */   {
/* 1117 */     if (this.popupMenuListener == null) {
/* 1118 */       this.popupMenuListener = createPopupMenuListener();
/*      */     }
/* 1120 */     return this.popupMenuListener;
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
/*      */   protected PopupMenuListener createPopupMenuListener()
/*      */   {
/* 1133 */     PopupMenuListener l = new PopupMenuListener()
/*      */     {
/*      */       public void popupMenuCanceled(PopupMenuEvent e)
/*      */       {
/* 1137 */         PopupMenuListener[] ls = BasicDatePickerUI.this.datePicker.getPopupMenuListeners();
/* 1138 */         PopupMenuEvent retargeted = null;
/* 1139 */         for (PopupMenuListener listener : ls) {
/* 1140 */           if (retargeted == null) {
/* 1141 */             retargeted = new PopupMenuEvent(BasicDatePickerUI.this.datePicker);
/*      */           }
/* 1143 */           listener.popupMenuCanceled(retargeted);
/*      */         }
/*      */       }
/*      */       
/*      */       public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
/*      */       {
/* 1149 */         PopupMenuListener[] ls = BasicDatePickerUI.this.datePicker.getPopupMenuListeners();
/* 1150 */         PopupMenuEvent retargeted = null;
/* 1151 */         for (PopupMenuListener listener : ls) {
/* 1152 */           if (retargeted == null) {
/* 1153 */             retargeted = new PopupMenuEvent(BasicDatePickerUI.this.datePicker);
/*      */           }
/* 1155 */           listener.popupMenuWillBecomeInvisible(retargeted);
/*      */         }
/*      */       }
/*      */       
/*      */       public void popupMenuWillBecomeVisible(PopupMenuEvent e)
/*      */       {
/* 1161 */         PopupMenuListener[] ls = BasicDatePickerUI.this.datePicker.getPopupMenuListeners();
/* 1162 */         PopupMenuEvent retargeted = null;
/* 1163 */         for (PopupMenuListener listener : ls) {
/* 1164 */           if (retargeted == null) {
/* 1165 */             retargeted = new PopupMenuEvent(BasicDatePickerUI.this.datePicker);
/*      */           }
/* 1167 */           listener.popupMenuWillBecomeVisible(retargeted);
/*      */         }
/*      */         
/*      */       }
/* 1171 */     };
/* 1172 */     return l;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private BasicDatePickerPopup createMonthViewPopup()
/*      */   {
/* 1180 */     BasicDatePickerPopup popup = new BasicDatePickerPopup();
/* 1181 */     popup.setLightWeightPopupEnabled(this.datePicker.isLightWeightPopupEnabled());
/* 1182 */     return popup;
/*      */   }
/*      */   
/*      */   private class TogglePopupAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public TogglePopupAction()
/*      */     {
/* 1190 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent ev) {
/* 1194 */       BasicDatePickerUI.this.toggleShowPopup();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class BasicDatePickerPopup
/*      */     extends JPopupMenu
/*      */   {
/*      */     public BasicDatePickerPopup()
/*      */     {
/* 1209 */       setLayout(new BorderLayout());
/* 1210 */       add(BasicDatePickerUI.this.datePicker.getMonthView(), "Center");
/* 1211 */       updateLinkPanel(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void updateLinkPanel(JComponent oldLinkPanel)
/*      */     {
/* 1218 */       if (oldLinkPanel != null) {
/* 1219 */         remove(oldLinkPanel);
/*      */       }
/* 1221 */       if (BasicDatePickerUI.this.datePicker.getLinkPanel() != null) {
/* 1222 */         add(BasicDatePickerUI.this.datePicker.getLinkPanel(), "South");
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
/*      */   private class Handler
/*      */     implements LayoutManager, MouseListener, MouseMotionListener, PropertyChangeListener, DateSelectionListener, ActionListener, FocusListener
/*      */   {
/* 1238 */     private boolean _forwardReleaseEvent = false;
/*      */     
/*      */     private Handler() {}
/*      */     
/*      */     public void mouseClicked(MouseEvent ev) {}
/*      */     
/* 1244 */     public void mousePressed(MouseEvent ev) { if (!BasicDatePickerUI.this.datePicker.isEnabled()) {
/* 1245 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1253 */       BasicDatePickerUI.this.toggleShowPopup();
/*      */     }
/*      */     
/*      */     public void mouseReleased(MouseEvent ev) {
/* 1257 */       if ((!BasicDatePickerUI.this.datePicker.isEnabled()) || (!BasicDatePickerUI.this.datePicker.isEditable())) {
/* 1258 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1262 */       if (this._forwardReleaseEvent) {
/* 1263 */         JXMonthView monthView = BasicDatePickerUI.this.datePicker.getMonthView();
/* 1264 */         ev = SwingUtilities.convertMouseEvent(BasicDatePickerUI.this.popupButton, ev, monthView);
/*      */         
/* 1266 */         monthView.dispatchEvent(ev);
/* 1267 */         this._forwardReleaseEvent = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void mouseEntered(MouseEvent ev) {}
/*      */     
/*      */     public void mouseExited(MouseEvent ev) {}
/*      */     
/*      */     public void mouseDragged(MouseEvent ev)
/*      */     {
/* 1278 */       if ((!BasicDatePickerUI.this.datePicker.isEnabled()) || (!BasicDatePickerUI.this.datePicker.isEditable())) {
/* 1279 */         return;
/*      */       }
/*      */       
/* 1282 */       this._forwardReleaseEvent = true;
/*      */       
/* 1284 */       if (!BasicDatePickerUI.this.popup.isShowing()) {
/* 1285 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1289 */       JXMonthView monthView = BasicDatePickerUI.this.datePicker.getMonthView();
/* 1290 */       ev = SwingUtilities.convertMouseEvent(BasicDatePickerUI.this.popupButton, ev, monthView);
/* 1291 */       monthView.dispatchEvent(ev);
/*      */     }
/*      */     
/*      */ 
/*      */     public void mouseMoved(MouseEvent ev) {}
/*      */     
/*      */     public void valueChanged(DateSelectionEvent ev)
/*      */     {
/* 1299 */       BasicDatePickerUI.this.updateFromSelectionChanged(ev.getEventType(), ev.isAdjusting());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent e)
/*      */     {
/* 1307 */       if (e.getSource() == BasicDatePickerUI.this.datePicker) {
/* 1308 */         datePickerPropertyChange(e);
/*      */       }
/* 1310 */       else if (e.getSource() == BasicDatePickerUI.this.datePicker.getEditor()) {
/* 1311 */         editorPropertyChange(e);
/*      */       }
/* 1313 */       else if (e.getSource() == BasicDatePickerUI.this.datePicker.getMonthView()) {
/* 1314 */         monthViewPropertyChange(e);
/*      */       }
/* 1316 */       else if (e.getSource() == BasicDatePickerUI.this.popupButton) {
/* 1317 */         buttonPropertyChange(e);
/*      */ 
/*      */       }
/* 1320 */       else if ("value".equals(e.getPropertyName())) {
/* 1321 */         throw new IllegalStateException("editor listening is moved to dedicated propertyChangeLisener");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void editorPropertyChange(PropertyChangeEvent evt)
/*      */     {
/* 1333 */       if ("value".equals(evt.getPropertyName())) {
/* 1334 */         BasicDatePickerUI.this.updateFromValueChanged((Date)evt.getOldValue(), (Date)evt.getNewValue());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void datePickerPropertyChange(PropertyChangeEvent e)
/*      */     {
/* 1346 */       String property = e.getPropertyName();
/* 1347 */       if ("date".equals(property)) {
/* 1348 */         BasicDatePickerUI.this.updateFromDateChanged();
/* 1349 */       } else if ("enabled".equals(property)) {
/* 1350 */         BasicDatePickerUI.this.updateFromEnabledChanged();
/* 1351 */       } else if ("editable".equals(property)) {
/* 1352 */         BasicDatePickerUI.this.updateFromEditableChanged();
/* 1353 */       } else if ("ToolTipText".equals(property)) {
/* 1354 */         String tip = BasicDatePickerUI.this.datePicker.getToolTipText();
/* 1355 */         BasicDatePickerUI.this.datePicker.getEditor().setToolTipText(tip);
/* 1356 */         BasicDatePickerUI.this.popupButton.setToolTipText(tip);
/* 1357 */       } else if ("monthView".equals(property)) {
/* 1358 */         BasicDatePickerUI.this.updateFromMonthViewChanged((JXMonthView)e.getOldValue());
/* 1359 */       } else if ("linkPanel".equals(property)) {
/* 1360 */         BasicDatePickerUI.this.updateLinkPanel((JComponent)e.getOldValue());
/* 1361 */       } else if ("editor".equals(property)) {
/* 1362 */         BasicDatePickerUI.this.updateFromEditorChanged((JFormattedTextField)e.getOldValue(), true);
/* 1363 */       } else if ("componentOrientation".equals(property)) {
/* 1364 */         BasicDatePickerUI.this.datePicker.revalidate();
/* 1365 */       } else if ("lightWeightPopupEnabled".equals(property))
/*      */       {
/* 1367 */         if (BasicDatePickerUI.this.popup != null) {
/* 1368 */           BasicDatePickerUI.this.popup.setVisible(false);
/*      */         }
/* 1370 */         BasicDatePickerUI.this.uninstallPopup();
/* 1371 */       } else if ("formats".equals(property)) {
/* 1372 */         BasicDatePickerUI.this.updateFormatsFromTimeZone(BasicDatePickerUI.this.datePicker.getTimeZone());
/*      */       }
/* 1374 */       else if ("locale".equals(property)) {
/* 1375 */         BasicDatePickerUI.this.updateLocale();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void monthViewPropertyChange(PropertyChangeEvent e)
/*      */     {
/* 1386 */       if ("selectionModel".equals(e.getPropertyName())) {
/* 1387 */         BasicDatePickerUI.this.updateFromSelectionModelChanged((DateSelectionModel)e.getOldValue());
/* 1388 */       } else if ("timeZone".equals(e.getPropertyName())) {
/* 1389 */         BasicDatePickerUI.this.updateTimeZone((TimeZone)e.getOldValue());
/* 1390 */       } else if ("today".equals(e.getPropertyName())) {
/* 1391 */         BasicDatePickerUI.this.updateLinkDate();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void buttonPropertyChange(PropertyChangeEvent e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void addLayoutComponent(String name, Component comp) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void removeLayoutComponent(Component comp) {}
/*      */     
/*      */ 
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container parent)
/*      */     {
/* 1414 */       return parent.getPreferredSize();
/*      */     }
/*      */     
/*      */     public Dimension minimumLayoutSize(Container parent) {
/* 1418 */       return parent.getMinimumSize();
/*      */     }
/*      */     
/*      */     public void layoutContainer(Container parent) {
/* 1422 */       Insets insets = BasicDatePickerUI.this.datePicker.getInsets();
/* 1423 */       int width = BasicDatePickerUI.this.datePicker.getWidth() - insets.left - insets.right;
/* 1424 */       int height = BasicDatePickerUI.this.datePicker.getHeight() - insets.top - insets.bottom;
/*      */       
/* 1426 */       int popupButtonWidth = BasicDatePickerUI.this.popupButton != null ? BasicDatePickerUI.this.popupButton.getPreferredSize().width : 0;
/*      */       
/* 1428 */       boolean ltr = BasicDatePickerUI.this.datePicker.getComponentOrientation().isLeftToRight();
/*      */       
/* 1430 */       BasicDatePickerUI.this.datePicker.getEditor().setBounds(ltr ? insets.left : insets.left + popupButtonWidth, insets.top, width - popupButtonWidth, height);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1435 */       if (BasicDatePickerUI.this.popupButton != null) {
/* 1436 */         BasicDatePickerUI.this.popupButton.setBounds(ltr ? width - popupButtonWidth + insets.left : insets.left, insets.top, popupButtonWidth, height);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void actionPerformed(ActionEvent e)
/*      */     {
/* 1446 */       if (e == null) return;
/* 1447 */       if (e.getSource() == BasicDatePickerUI.this.datePicker.getMonthView()) {
/* 1448 */         monthViewActionPerformed(e);
/* 1449 */       } else if (e.getSource() == BasicDatePickerUI.this.datePicker.getEditor()) {
/* 1450 */         editorActionPerformed(e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void editorActionPerformed(ActionEvent e)
/*      */     {
/* 1461 */       BasicDatePickerUI.this.commit();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void monthViewActionPerformed(ActionEvent e)
/*      */     {
/* 1470 */       if ("monthViewCancel".equals(e.getActionCommand())) {
/* 1471 */         BasicDatePickerUI.this.cancel();
/* 1472 */       } else if ("monthViewCommit".equals(e.getActionCommand())) {
/* 1473 */         BasicDatePickerUI.this.commit();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void focusGained(FocusEvent e)
/*      */     {
/* 1486 */       if (e.isTemporary()) return;
/* 1487 */       BasicDatePickerUI.this.popupRemover.load();
/* 1488 */       if (e.getSource() == BasicDatePickerUI.this.datePicker) {
/* 1489 */         BasicDatePickerUI.this.datePicker.getEditor().requestFocusInWindow();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void focusLost(FocusEvent e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class PopupRemover
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     private KeyboardFocusManager manager;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean loaded;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public PopupRemover() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void load()
/*      */     {
/* 1526 */       if (this.manager != KeyboardFocusManager.getCurrentKeyboardFocusManager()) {
/* 1527 */         unload();
/* 1528 */         this.manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */       }
/* 1530 */       if (!this.loaded) {
/* 1531 */         this.manager.addPropertyChangeListener("permanentFocusOwner", this);
/* 1532 */         this.loaded = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void unload(boolean nullManager)
/*      */     {
/* 1540 */       if (this.manager != null) {
/* 1541 */         this.manager.removePropertyChangeListener("permanentFocusOwner", this);
/* 1542 */         if (nullManager) {
/* 1543 */           this.manager = null;
/*      */         }
/*      */       }
/* 1546 */       this.loaded = false;
/*      */     }
/*      */     
/*      */     public void unload() {
/* 1550 */       unload(true);
/*      */     }
/*      */     
/*      */     public void propertyChange(PropertyChangeEvent evt) {
/* 1554 */       if (!BasicDatePickerUI.this.isPopupVisible()) {
/* 1555 */         unload(false);
/* 1556 */         return;
/*      */       }
/* 1558 */       Component comp = this.manager.getPermanentFocusOwner();
/* 1559 */       if ((comp != null) && (!SwingXUtilities.isDescendingFrom(comp, BasicDatePickerUI.this.datePicker))) {
/* 1560 */         unload(false);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1565 */         BasicDatePickerUI.this.hidePopup();
/* 1566 */         comp.requestFocusInWindow();
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
/*      */   protected PropertyChangeListener createMonthViewPropertyListener()
/*      */   {
/* 1584 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected FocusListener createFocusListener()
/*      */   {
/* 1592 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ActionListener createEditorActionListener()
/*      */   {
/* 1601 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ActionListener createMonthViewActionListener()
/*      */   {
/* 1610 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateSelectionListener createMonthViewSelectionListener()
/*      */   {
/* 1619 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PropertyChangeListener createEditorPropertyListener()
/*      */   {
/* 1627 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/* 1636 */     if (this.handler == null) {
/* 1637 */       this.handler = new Handler(null);
/*      */     }
/* 1639 */     return this.handler;
/*      */   }
/*      */   
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/* 1643 */     return getHandler();
/*      */   }
/*      */   
/*      */   protected LayoutManager createLayoutManager() {
/* 1647 */     return getHandler();
/*      */   }
/*      */   
/*      */   protected MouseListener createMouseListener() {
/* 1651 */     return getHandler();
/*      */   }
/*      */   
/*      */   protected MouseMotionListener createMouseMotionListener() {
/* 1655 */     return getHandler();
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicDatePickerUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */