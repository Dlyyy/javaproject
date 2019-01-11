/*      */ package org.jdesktop.swingx.plaf.basic;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Window;
/*      */ import java.awt.datatransfer.StringSelection;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.logging.Level;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.AbstractButton;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JEditorPane;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JInternalFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicHTML;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.StyledEditorKit;
/*      */ import javax.swing.text.html.HTMLEditorKit;
/*      */ import org.jdesktop.swingx.JXEditorPane;
/*      */ import org.jdesktop.swingx.JXErrorPane;
/*      */ import org.jdesktop.swingx.action.AbstractActionExt;
/*      */ import org.jdesktop.swingx.error.ErrorInfo;
/*      */ import org.jdesktop.swingx.error.ErrorLevel;
/*      */ import org.jdesktop.swingx.error.ErrorReporter;
/*      */ import org.jdesktop.swingx.plaf.ErrorPaneUI;
/*      */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*      */ import org.jdesktop.swingx.util.WindowUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BasicErrorPaneUI
/*      */   extends ErrorPaneUI
/*      */ {
/*      */   protected static final String CLASS_NAME = "JXErrorPane";
/*      */   protected JXErrorPane pane;
/*      */   protected JEditorPane errorMessage;
/*      */   protected JScrollPane errorScrollPane;
/*      */   protected JXEditorPane details;
/*      */   protected AbstractButton detailButton;
/*      */   protected JButton closeButton;
/*      */   protected JLabel iconLabel;
/*      */   protected AbstractButton reportButton;
/*      */   protected JPanel detailsPanel;
/*      */   protected JScrollPane detailsScrollPane;
/*      */   protected JButton copyToClipboardButton;
/*      */   protected PropertyChangeListener errorPaneListener;
/*      */   protected ActionListener detailListener;
/*      */   protected ActionListener copyToClipboardListener;
/*      */   private int collapsedHeight;
/*      */   private int expandedHeight;
/*      */   
/*      */   public BasicErrorPaneUI()
/*      */   {
/*  159 */     this.collapsedHeight = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  164 */     this.expandedHeight = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ComponentUI createUI(JComponent c)
/*      */   {
/*  172 */     return new BasicErrorPaneUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void installUI(JComponent c)
/*      */   {
/*  180 */     super.installUI(c);
/*      */     
/*  182 */     this.pane = ((JXErrorPane)c);
/*      */     
/*  184 */     installDefaults();
/*  185 */     installComponents();
/*  186 */     installListeners();
/*      */     
/*      */ 
/*  189 */     Action a = c.getActionMap().get("report-action");
/*  190 */     if (a == null) {
/*  191 */       final JXErrorPane pane = (JXErrorPane)c;
/*  192 */       AbstractActionExt reportAction = new AbstractActionExt() {
/*      */         public void actionPerformed(ActionEvent e) {
/*  194 */           ErrorReporter reporter = pane.getErrorReporter();
/*  195 */           if (reporter != null) {
/*  196 */             reporter.reportError(pane.getErrorInfo());
/*      */           }
/*      */         }
/*  199 */       };
/*  200 */       configureReportAction(reportAction);
/*  201 */       c.getActionMap().put("report-action", reportAction);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void uninstallUI(JComponent c)
/*      */   {
/*  210 */     super.uninstallUI(c);
/*      */     
/*  212 */     uninstallListeners();
/*  213 */     uninstallComponents();
/*  214 */     uninstallDefaults();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void installDefaults() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  228 */     LookAndFeel.uninstallBorder(this.pane);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  239 */     this.errorPaneListener = new ErrorPaneListener(null);
/*  240 */     this.pane.addPropertyChangeListener(this.errorPaneListener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  251 */     this.pane.removePropertyChangeListener(this.errorPaneListener);
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
/*      */   protected void installComponents()
/*      */   {
/*  265 */     this.iconLabel = new JLabel(this.pane.getIcon());
/*      */     
/*  267 */     this.errorMessage = new JEditorPane();
/*  268 */     this.errorMessage.setEditable(false);
/*  269 */     this.errorMessage.setContentType("text/html");
/*  270 */     this.errorMessage.setEditorKitForContentType("text/plain", new StyledEditorKit());
/*  271 */     this.errorMessage.setEditorKitForContentType("text/html", new HTMLEditorKit());
/*      */     
/*  273 */     this.errorMessage.setOpaque(false);
/*  274 */     this.errorMessage.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
/*      */     
/*  276 */     this.closeButton = new JButton(UIManagerExt.getString("JXErrorPane.ok_button_text", this.errorMessage.getLocale()));
/*      */     
/*      */ 
/*  279 */     this.reportButton = new EqualSizeJButton(this.pane.getActionMap().get("report-action"));
/*      */     
/*  281 */     this.detailButton = new EqualSizeJButton(UIManagerExt.getString("JXErrorPane.details_expand_text", this.errorMessage.getLocale()));
/*      */     
/*      */ 
/*  284 */     this.details = new JXEditorPane();
/*  285 */     this.details.setContentType("text/html");
/*  286 */     this.details.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
/*  287 */     this.details.setTransferHandler(createDetailsTransferHandler(this.details));
/*  288 */     this.detailsScrollPane = new JScrollPane(this.details);
/*  289 */     this.detailsScrollPane.setPreferredSize(new Dimension(10, 250));
/*  290 */     this.details.setEditable(false);
/*  291 */     this.detailsPanel = new JPanel();
/*  292 */     this.detailsPanel.setVisible(false);
/*  293 */     this.copyToClipboardButton = new JButton(UIManagerExt.getString("JXErrorPane.copy_to_clipboard_button_text", this.errorMessage.getLocale()));
/*      */     
/*  295 */     this.copyToClipboardListener = new ActionListener() {
/*      */       public void actionPerformed(ActionEvent ae) {
/*  297 */         BasicErrorPaneUI.this.details.copy();
/*      */       }
/*  299 */     };
/*  300 */     this.copyToClipboardButton.addActionListener(this.copyToClipboardListener);
/*      */     
/*  302 */     this.detailsPanel.setLayout(createDetailPanelLayout());
/*  303 */     this.detailsPanel.add(this.detailsScrollPane);
/*  304 */     this.detailsPanel.add(this.copyToClipboardButton);
/*      */     
/*      */ 
/*      */ 
/*  308 */     this.errorScrollPane = new JScrollPane(this.errorMessage);
/*  309 */     this.errorScrollPane.setBorder(new EmptyBorder(0, 0, 5, 0));
/*  310 */     this.errorScrollPane.setOpaque(false);
/*  311 */     this.errorScrollPane.getViewport().setOpaque(false);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  316 */     this.pane.setLayout(createErrorPaneLayout());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  321 */     Insets borderInsets = new Insets(16, 24, 16, 17);
/*  322 */     this.pane.setBorder(BorderFactory.createEmptyBorder(borderInsets.top, borderInsets.left, borderInsets.bottom, borderInsets.right));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  329 */     this.pane.add(this.iconLabel);
/*  330 */     this.pane.add(this.errorScrollPane);
/*  331 */     this.pane.add(this.closeButton);
/*  332 */     this.pane.add(this.reportButton);
/*  333 */     this.reportButton.setVisible(false);
/*  334 */     this.pane.add(this.detailButton);
/*  335 */     this.pane.add(this.detailsPanel);
/*      */     
/*      */ 
/*  338 */     EqualSizeJButton[] buttons = { (EqualSizeJButton)this.detailButton, (EqualSizeJButton)this.reportButton };
/*      */     
/*  340 */     ((EqualSizeJButton)this.reportButton).setGroup(buttons);
/*  341 */     ((EqualSizeJButton)this.detailButton).setGroup(buttons);
/*      */     
/*  343 */     this.reportButton.setMinimumSize(this.reportButton.getPreferredSize());
/*  344 */     this.detailButton.setMinimumSize(this.detailButton.getPreferredSize());
/*      */     
/*      */ 
/*  347 */     this.detailListener = new DetailsClickEvent(null);
/*  348 */     this.detailButton.addActionListener(this.detailListener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void uninstallComponents()
/*      */   {
/*  357 */     this.iconLabel = null;
/*  358 */     this.errorMessage = null;
/*  359 */     this.closeButton = null;
/*  360 */     this.reportButton = null;
/*      */     
/*  362 */     this.detailButton.removeActionListener(this.detailListener);
/*  363 */     this.detailButton = null;
/*      */     
/*  365 */     this.details.setTransferHandler(null);
/*  366 */     this.details = null;
/*      */     
/*  368 */     this.detailsScrollPane.removeAll();
/*  369 */     this.detailsScrollPane = null;
/*      */     
/*  371 */     this.detailsPanel.setLayout(null);
/*  372 */     this.detailsPanel.removeAll();
/*  373 */     this.detailsPanel = null;
/*      */     
/*  375 */     this.copyToClipboardButton.removeActionListener(this.copyToClipboardListener);
/*  376 */     this.copyToClipboardButton = null;
/*      */     
/*  378 */     this.pane.removeAll();
/*  379 */     this.pane.setLayout(null);
/*  380 */     this.pane.setBorder(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JFrame getErrorFrame(Component owner)
/*      */   {
/*  392 */     reinit();
/*  393 */     this.expandedHeight = 0;
/*  394 */     this.collapsedHeight = 0;
/*  395 */     JXErrorFrame frame = new JXErrorFrame(this.pane);
/*  396 */     centerWindow(frame, owner);
/*  397 */     return frame;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JDialog getErrorDialog(Component owner)
/*      */   {
/*  405 */     reinit();
/*  406 */     this.expandedHeight = 0;
/*  407 */     this.collapsedHeight = 0;
/*  408 */     Window w = WindowUtils.findWindow(owner);
/*  409 */     JXErrorDialog dlg = null;
/*  410 */     if ((w instanceof Dialog)) {
/*  411 */       dlg = new JXErrorDialog((Dialog)w, this.pane);
/*  412 */     } else if ((w instanceof Frame)) {
/*  413 */       dlg = new JXErrorDialog((Frame)w, this.pane);
/*      */     }
/*      */     else {
/*  416 */       dlg = new JXErrorDialog(JOptionPane.getRootFrame(), this.pane);
/*      */     }
/*  418 */     centerWindow(dlg, owner);
/*  419 */     return dlg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JInternalFrame getErrorInternalFrame(Component owner)
/*      */   {
/*  427 */     reinit();
/*  428 */     this.expandedHeight = 0;
/*  429 */     this.collapsedHeight = 0;
/*  430 */     JXInternalErrorFrame frame = new JXInternalErrorFrame(this.pane);
/*  431 */     centerWindow(frame, owner);
/*  432 */     return frame;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected LayoutManager createErrorPaneLayout()
/*      */   {
/*  439 */     return new ErrorPaneLayout(null);
/*      */   }
/*      */   
/*      */   protected LayoutManager createDetailPanelLayout() {
/*  443 */     GridBagLayout layout = new GridBagLayout();
/*  444 */     layout.addLayoutComponent(this.detailsScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(6, 0, 0, 0), 0, 0));
/*  445 */     GridBagConstraints gbc = new GridBagConstraints();
/*  446 */     gbc.anchor = 22;
/*  447 */     gbc.fill = 0;
/*  448 */     gbc.gridwidth = 1;
/*  449 */     gbc.gridx = 0;
/*  450 */     gbc.gridy = 1;
/*  451 */     gbc.weighty = 0.0D;
/*  452 */     gbc.weightx = 1.0D;
/*  453 */     gbc.insets = new Insets(6, 0, 6, 0);
/*  454 */     layout.addLayoutComponent(this.copyToClipboardButton, gbc);
/*  455 */     return layout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Dimension calculatePreferredSize()
/*      */   {
/*  462 */     return new Dimension(this.iconLabel.getPreferredSize().width + this.errorMessage.getPreferredSize().width, 206);
/*      */   }
/*      */   
/*      */   protected int getDetailsHeight() {
/*  466 */     return 300;
/*      */   }
/*      */   
/*      */   protected void configureReportAction(AbstractActionExt reportAction) {
/*  470 */     reportAction.setName(UIManagerExt.getString("JXErrorPane.report_button_text", this.pane.getLocale()));
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
/*      */   private TransferHandler createDetailsTransferHandler(JTextComponent detailComponent)
/*      */   {
/*  483 */     return new DetailsTransferHandler(detailComponent, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Icon getDefaultErrorIcon()
/*      */   {
/*      */     try
/*      */     {
/*  491 */       Icon icon = UIManager.getIcon("JXErrorPane.errorIcon");
/*  492 */       return icon == null ? UIManager.getIcon("OptionPane.errorIcon") : icon;
/*      */     } catch (Exception e) {}
/*  494 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Icon getDefaultWarningIcon()
/*      */   {
/*      */     try
/*      */     {
/*  503 */       Icon icon = UIManager.getIcon("JXErrorPane.warningIcon");
/*  504 */       return icon == null ? UIManager.getIcon("OptionPane.warningIcon") : icon;
/*      */     } catch (Exception e) {}
/*  506 */     return null;
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
/*      */   private void setDetails(String details)
/*      */   {
/*  520 */     if ((details == null) || (details.equals(""))) {
/*  521 */       this.detailButton.setVisible(false);
/*      */     } else {
/*  523 */       this.details.setText(details);
/*  524 */       this.detailButton.setVisible(true);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void configureDetailsButton(boolean expanded) {
/*  529 */     if (expanded) {
/*  530 */       this.detailButton.setText(UIManagerExt.getString("JXErrorPane.details_contract_text", this.detailButton.getLocale()));
/*      */     }
/*      */     else {
/*  533 */       this.detailButton.setText(UIManagerExt.getString("JXErrorPane.details_expand_text", this.detailButton.getLocale()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setDetailsVisible(boolean b)
/*      */   {
/*  544 */     if (b) {
/*  545 */       this.collapsedHeight = this.pane.getHeight();
/*  546 */       this.pane.setSize(this.pane.getWidth(), this.expandedHeight == 0 ? this.collapsedHeight + getDetailsHeight() : this.expandedHeight);
/*  547 */       this.detailsPanel.setVisible(true);
/*  548 */       configureDetailsButton(true);
/*  549 */       this.detailsPanel.applyComponentOrientation(this.detailButton.getComponentOrientation());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  557 */       this.details.setText(this.details.getText());
/*  558 */       this.details.setCaretPosition(0);
/*  559 */     } else if (this.collapsedHeight != 0) {
/*  560 */       this.expandedHeight = this.pane.getHeight();
/*  561 */       this.detailsPanel.setVisible(false);
/*  562 */       configureDetailsButton(false);
/*      */       
/*      */ 
/*  565 */       this.errorMessage.setSize(0, 0);
/*  566 */       this.errorMessage.setSize(this.errorMessage.getPreferredSize());
/*  567 */       this.pane.setSize(this.pane.getWidth(), this.collapsedHeight);
/*      */     } else {
/*  569 */       this.detailsPanel.setVisible(false);
/*      */     }
/*      */     
/*  572 */     this.pane.doLayout();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setErrorMessage(String errorMessage)
/*      */   {
/*  580 */     if (BasicHTML.isHTMLString(errorMessage)) {
/*  581 */       this.errorMessage.setContentType("text/html");
/*      */     } else {
/*  583 */       this.errorMessage.setContentType("text/plain");
/*      */     }
/*  585 */     this.errorMessage.setText(errorMessage);
/*  586 */     this.errorMessage.setCaretPosition(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void reinit()
/*      */   {
/*  594 */     setDetailsVisible(false);
/*  595 */     Action reportAction = this.pane.getActionMap().get("report-action");
/*  596 */     this.reportButton.setAction(reportAction);
/*  597 */     this.reportButton.setVisible((reportAction != null) && (reportAction.isEnabled()) && (this.pane.getErrorReporter() != null));
/*  598 */     this.reportButton.setEnabled(this.reportButton.isVisible());
/*  599 */     ErrorInfo errorInfo = this.pane.getErrorInfo();
/*  600 */     if (errorInfo == null) {
/*  601 */       this.iconLabel.setIcon(this.pane.getIcon());
/*  602 */       setErrorMessage("");
/*  603 */       this.closeButton.setText(UIManagerExt.getString("JXErrorPane.ok_button_text", this.closeButton.getLocale()));
/*      */       
/*  605 */       setDetails("");
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  611 */       if (errorInfo.getErrorLevel() == ErrorLevel.FATAL) {
/*  612 */         this.closeButton.setText(UIManagerExt.getString("JXErrorPane.fatal_button_text", this.closeButton.getLocale()));
/*      */       }
/*      */       else {
/*  615 */         this.closeButton.setText(UIManagerExt.getString("JXErrorPane.ok_button_text", this.closeButton.getLocale()));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  621 */       Icon icon = this.pane.getIcon();
/*  622 */       if ((icon == null) || ((icon instanceof UIResource))) {
/*  623 */         if (errorInfo.getErrorLevel().intValue() <= Level.WARNING.intValue()) {
/*  624 */           icon = getDefaultWarningIcon();
/*      */         } else {
/*  626 */           icon = getDefaultErrorIcon();
/*      */         }
/*      */       }
/*  629 */       this.iconLabel.setIcon(icon);
/*  630 */       setErrorMessage(errorInfo.getBasicErrorMessage());
/*  631 */       String details = errorInfo.getDetailedErrorMessage();
/*  632 */       if (details == null) {
/*  633 */         details = getDetailsAsHTML(errorInfo);
/*      */       }
/*  635 */       setDetails(details);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDetailsAsHTML(ErrorInfo errorInfo)
/*      */   {
/*  645 */     if (errorInfo.getErrorException() != null)
/*      */     {
/*  647 */       StringBuffer html = new StringBuffer("<html>");
/*  648 */       html.append("<h2>" + escapeXml(errorInfo.getTitle()) + "</h2>");
/*  649 */       html.append("<HR size='1' noshade>");
/*  650 */       html.append("<div></div>");
/*  651 */       html.append("<b>Message:</b>");
/*  652 */       html.append("<pre>");
/*  653 */       html.append("    " + escapeXml(errorInfo.getErrorException().toString()));
/*  654 */       html.append("</pre>");
/*  655 */       html.append("<b>Level:</b>");
/*  656 */       html.append("<pre>");
/*  657 */       html.append("    " + errorInfo.getErrorLevel());
/*  658 */       html.append("</pre>");
/*  659 */       html.append("<b>Stack Trace:</b>");
/*  660 */       Throwable ex = errorInfo.getErrorException();
/*  661 */       while (ex != null) {
/*  662 */         html.append("<h4>" + ex.getMessage() + "</h4>");
/*  663 */         html.append("<pre>");
/*  664 */         for (StackTraceElement el : ex.getStackTrace()) {
/*  665 */           html.append("    " + el.toString().replace("<init>", "&lt;init&gt;") + "\n");
/*      */         }
/*  667 */         html.append("</pre>");
/*  668 */         ex = ex.getCause();
/*      */       }
/*  670 */       html.append("</html>");
/*  671 */       return html.toString();
/*      */     }
/*  673 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class CloseAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private Window w;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private CloseAction(Window w)
/*      */     {
/*  690 */       if (w == null) {
/*  691 */         throw new NullPointerException("Window cannot be null");
/*      */       }
/*  693 */       this.w = w;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void actionPerformed(ActionEvent e)
/*      */     {
/*  700 */       this.w.setVisible(false);
/*  701 */       this.w.dispose();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final class DetailsClickEvent
/*      */     implements ActionListener
/*      */   {
/*      */     private DetailsClickEvent() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  718 */     public void actionPerformed(ActionEvent e) { BasicErrorPaneUI.this.setDetailsVisible(!BasicErrorPaneUI.this.detailsPanel.isVisible()); }
/*      */   }
/*      */   
/*      */   private final class ResizeWindow implements ActionListener {
/*      */     private Window w;
/*      */     
/*      */     private ResizeWindow(Window w) {
/*  725 */       if (w == null) {
/*  726 */         throw new NullPointerException();
/*      */       }
/*  728 */       this.w = w;
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent ae) {
/*  732 */       Dimension contentSize = null;
/*  733 */       if ((this.w instanceof JDialog)) {
/*  734 */         contentSize = ((JDialog)this.w).getContentPane().getSize();
/*      */       } else {
/*  736 */         contentSize = ((JFrame)this.w).getContentPane().getSize();
/*      */       }
/*      */       
/*  739 */       Dimension dialogSize = this.w.getSize();
/*  740 */       int ydiff = dialogSize.height - contentSize.height;
/*  741 */       Dimension paneSize = BasicErrorPaneUI.this.pane.getSize();
/*  742 */       this.w.setSize(new Dimension(dialogSize.width, paneSize.height + ydiff));
/*  743 */       this.w.validate();
/*  744 */       this.w.repaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class EqualSizeJButton
/*      */     extends JButton
/*      */   {
/*      */     private EqualSizeJButton[] group;
/*      */     
/*      */ 
/*      */     public EqualSizeJButton() {}
/*      */     
/*      */     public EqualSizeJButton(String text)
/*      */     {
/*  759 */       super();
/*      */     }
/*      */     
/*      */     public EqualSizeJButton(Action a) {
/*  763 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setGroup(EqualSizeJButton[] group)
/*      */     {
/*  772 */       this.group = group;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private Dimension getRealPreferredSize()
/*      */     {
/*  779 */       return super.getPreferredSize();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension getPreferredSize()
/*      */     {
/*  795 */       int width = 0;
/*  796 */       int height = 0;
/*  797 */       for (int iter = 0; iter < this.group.length; iter++) {
/*  798 */         Dimension size = this.group[iter].getRealPreferredSize();
/*  799 */         width = Math.max(size.width, width);
/*  800 */         height = Math.max(size.height, height);
/*      */       }
/*      */       
/*  803 */       return new Dimension(width, height);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class DetailsTransferHandler
/*      */     extends TransferHandler
/*      */   {
/*      */     private JTextComponent details;
/*      */     
/*      */     private DetailsTransferHandler(JTextComponent detailComponent)
/*      */     {
/*  815 */       if (detailComponent == null) {
/*  816 */         throw new NullPointerException("detail component cannot be null");
/*      */       }
/*  818 */       this.details = detailComponent;
/*      */     }
/*      */     
/*      */     protected Transferable createTransferable(JComponent c)
/*      */     {
/*  823 */       String text = this.details.getSelectedText();
/*  824 */       if ((text == null) || (text.equals(""))) {
/*  825 */         this.details.selectAll();
/*  826 */         text = this.details.getSelectedText();
/*  827 */         this.details.select(-1, -1);
/*      */       }
/*  829 */       return new StringSelection(text);
/*      */     }
/*      */     
/*      */     public int getSourceActions(JComponent c)
/*      */     {
/*  834 */       return 1;
/*      */     }
/*      */   }
/*      */   
/*      */   private final class JXErrorDialog extends JDialog
/*      */   {
/*      */     public JXErrorDialog(Frame parent, JXErrorPane p) {
/*  841 */       super(true);
/*  842 */       init(p);
/*      */     }
/*      */     
/*      */     public JXErrorDialog(Dialog parent, JXErrorPane p) {
/*  846 */       super(true);
/*  847 */       init(p);
/*      */     }
/*      */     
/*      */     protected void init(JXErrorPane p)
/*      */     {
/*  852 */       setTitle(p.getErrorInfo() == null ? null : p.getErrorInfo().getTitle());
/*  853 */       BasicErrorPaneUI.this.initWindow(this, p);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class JXErrorFrame extends JFrame {
/*      */     public JXErrorFrame(JXErrorPane p) {
/*  859 */       setTitle(p.getErrorInfo().getTitle());
/*  860 */       BasicErrorPaneUI.this.initWindow(this, p);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class JXInternalErrorFrame extends JInternalFrame {
/*      */     public JXInternalErrorFrame(JXErrorPane p) {
/*  866 */       setTitle(p.getErrorInfo().getTitle());
/*      */       
/*  868 */       setLayout(new BorderLayout());
/*  869 */       add(p, "Center");
/*  870 */       final Action closeAction = new AbstractAction() {
/*      */         public void actionPerformed(ActionEvent evt) {
/*  872 */           BasicErrorPaneUI.JXInternalErrorFrame.this.setVisible(false);
/*  873 */           BasicErrorPaneUI.JXInternalErrorFrame.this.dispose();
/*      */         }
/*  875 */       };
/*  876 */       BasicErrorPaneUI.this.closeButton.addActionListener(closeAction);
/*  877 */       addComponentListener(new ComponentAdapter()
/*      */       {
/*      */         public void componentHidden(ComponentEvent e)
/*      */         {
/*  881 */           BasicErrorPaneUI.this.closeButton.removeActionListener(closeAction);
/*  882 */           BasicErrorPaneUI.this.exitIfFatal();
/*      */         }
/*      */         
/*  885 */       });
/*  886 */       getRootPane().setDefaultButton(BasicErrorPaneUI.this.closeButton);
/*  887 */       setResizable(false);
/*  888 */       setDefaultCloseOperation(2);
/*  889 */       KeyStroke ks = KeyStroke.getKeyStroke(27, 0);
/*  890 */       getRootPane().registerKeyboardAction(closeAction, ks, 2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initWindow(Window w, JXErrorPane pane)
/*      */   {
/*  902 */     w.setLayout(new BorderLayout());
/*  903 */     w.add(pane, "Center");
/*  904 */     final Action closeAction = new CloseAction(w, null);
/*  905 */     this.closeButton.addActionListener(closeAction);
/*  906 */     final ResizeWindow resizeListener = new ResizeWindow(w, null);
/*      */     
/*  908 */     ActionListener[] list = this.detailButton.getActionListeners();
/*  909 */     for (ActionListener a : list) {
/*  910 */       this.detailButton.removeActionListener(a);
/*      */     }
/*  912 */     this.detailButton.addActionListener(resizeListener);
/*  913 */     for (ActionListener a : list) {
/*  914 */       this.detailButton.addActionListener(a);
/*      */     }
/*      */     
/*  917 */     if ((w instanceof JFrame)) {
/*  918 */       JFrame f = (JFrame)w;
/*  919 */       f.getRootPane().setDefaultButton(this.closeButton);
/*  920 */       f.setResizable(true);
/*  921 */       f.setDefaultCloseOperation(2);
/*  922 */       KeyStroke ks = KeyStroke.getKeyStroke(27, 0);
/*  923 */       f.getRootPane().registerKeyboardAction(closeAction, ks, 2);
/*  924 */     } else if ((w instanceof JDialog)) {
/*  925 */       JDialog d = (JDialog)w;
/*  926 */       d.getRootPane().setDefaultButton(this.closeButton);
/*  927 */       d.setResizable(true);
/*  928 */       d.setDefaultCloseOperation(2);
/*  929 */       KeyStroke ks = KeyStroke.getKeyStroke(27, 0);
/*  930 */       d.getRootPane().registerKeyboardAction(closeAction, ks, 2);
/*      */     }
/*      */     
/*  933 */     w.addWindowListener(new WindowAdapter()
/*      */     {
/*      */       public void windowClosing(WindowEvent e)
/*      */       {
/*  937 */         BasicErrorPaneUI.this.closeButton.removeActionListener(closeAction);
/*  938 */         BasicErrorPaneUI.this.detailButton.removeActionListener(resizeListener);
/*  939 */         BasicErrorPaneUI.this.exitIfFatal();
/*      */       }
/*  941 */     });
/*  942 */     w.pack();
/*      */   }
/*      */   
/*      */   private void exitIfFatal() {
/*  946 */     ErrorInfo info = this.pane.getErrorInfo();
/*      */     
/*  948 */     if ((info != null) && (info.getErrorLevel() == ErrorLevel.FATAL)) {
/*  949 */       Action fatalAction = this.pane.getActionMap().get("fatal-action");
/*  950 */       if (fatalAction == null) {
/*  951 */         System.exit(1);
/*      */       } else {
/*  953 */         ActionEvent ae = new ActionEvent(this.closeButton, -1, "fatal");
/*  954 */         fatalAction.actionPerformed(ae);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ErrorPaneListener implements PropertyChangeListener { private ErrorPaneListener() {}
/*      */     
/*  961 */     public void propertyChange(PropertyChangeEvent evt) { BasicErrorPaneUI.this.reinit(); }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final class ErrorPaneLayout
/*      */     implements LayoutManager
/*      */   {
/*  969 */     private JEditorPane dummy = new JEditorPane();
/*      */     
/*      */ 
/*      */ 
/*      */     private ErrorPaneLayout() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void addLayoutComponent(String name, Component comp) {}
/*      */     
/*      */ 
/*      */     public void removeLayoutComponent(Component comp) {}
/*      */     
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container parent)
/*      */     {
/*  985 */       int prefWidth = parent.getWidth();
/*  986 */       int prefHeight = parent.getHeight();
/*  987 */       Insets insets = parent.getInsets();
/*  988 */       int pw = BasicErrorPaneUI.this.detailButton.isVisible() ? BasicErrorPaneUI.this.detailButton.getPreferredSize().width : 0;
/*  989 */       pw += (BasicErrorPaneUI.this.detailButton.isVisible() ? BasicErrorPaneUI.this.detailButton.getPreferredSize().width : 0);
/*  990 */       pw += (BasicErrorPaneUI.this.reportButton.isVisible() ? 5 + BasicErrorPaneUI.this.reportButton.getPreferredSize().width : 0);
/*  991 */       pw += (BasicErrorPaneUI.this.closeButton.isVisible() ? 5 + BasicErrorPaneUI.this.closeButton.getPreferredSize().width : 0);
/*  992 */       prefWidth = Math.max(prefWidth, pw) + insets.left + insets.right;
/*  993 */       if (BasicErrorPaneUI.this.errorMessage != null)
/*      */       {
/*      */ 
/*  996 */         this.dummy.setContentType(BasicErrorPaneUI.this.errorMessage.getContentType());
/*  997 */         this.dummy.setEditorKit(BasicErrorPaneUI.this.errorMessage.getEditorKit());
/*  998 */         this.dummy.setText(BasicErrorPaneUI.this.errorMessage.getText());
/*  999 */         this.dummy.setSize(prefWidth, 20);
/* 1000 */         int errorMessagePrefHeight = this.dummy.getPreferredSize().height;
/*      */         
/* 1002 */         prefHeight = Math.max(errorMessagePrefHeight, BasicErrorPaneUI.this.iconLabel.getPreferredSize().height) + 10 + BasicErrorPaneUI.this.closeButton.getPreferredSize().height;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1010 */         if (BasicErrorPaneUI.this.detailsPanel.isVisible()) {
/* 1011 */           prefHeight += BasicErrorPaneUI.this.getDetailsHeight();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1016 */       if ((BasicErrorPaneUI.this.iconLabel != null) && (BasicErrorPaneUI.this.iconLabel.getIcon() != null)) {
/* 1017 */         prefWidth += BasicErrorPaneUI.this.iconLabel.getIcon().getIconWidth();
/* 1018 */         prefHeight += 10;
/*      */       }
/*      */       
/* 1021 */       return new Dimension(prefWidth + insets.left + insets.right, prefHeight + insets.top + insets.bottom);
/*      */     }
/*      */     
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container parent)
/*      */     {
/* 1027 */       return preferredLayoutSize(parent);
/*      */     }
/*      */     
/*      */     public void layoutContainer(Container parent) {
/* 1031 */       Insets insets = parent.getInsets();
/* 1032 */       int x = insets.left;
/* 1033 */       int y = insets.top;
/*      */       
/*      */ 
/* 1036 */       if (BasicErrorPaneUI.this.iconLabel != null) {
/* 1037 */         Dimension dim = BasicErrorPaneUI.this.iconLabel.getPreferredSize();
/* 1038 */         BasicErrorPaneUI.this.iconLabel.setBounds(x, y, dim.width, dim.height);
/* 1039 */         x += dim.width + 17;
/* 1040 */         int leftEdge = x;
/*      */         
/*      */ 
/* 1043 */         this.dummy.setContentType(BasicErrorPaneUI.this.errorMessage.getContentType());
/* 1044 */         this.dummy.setText(BasicErrorPaneUI.this.errorMessage.getText());
/* 1045 */         this.dummy.setSize(parent.getWidth() - leftEdge - insets.right, 20);
/* 1046 */         dim = this.dummy.getPreferredSize();
/* 1047 */         int spx = x;
/* 1048 */         int spy = y;
/* 1049 */         Dimension spDim = new Dimension(parent.getWidth() - leftEdge - insets.right, dim.height);
/* 1050 */         y += dim.height + 10;
/* 1051 */         int rightEdge = parent.getWidth() - insets.right;
/* 1052 */         x = rightEdge;
/* 1053 */         dim = BasicErrorPaneUI.this.detailButton.getPreferredSize();
/* 1054 */         int buttonY = y + 5;
/* 1055 */         if (BasicErrorPaneUI.this.detailButton.isVisible()) {
/* 1056 */           dim = BasicErrorPaneUI.this.detailButton.getPreferredSize();
/* 1057 */           x -= dim.width;
/* 1058 */           BasicErrorPaneUI.this.detailButton.setBounds(x, buttonY, dim.width, dim.height);
/*      */         }
/* 1060 */         if (BasicErrorPaneUI.this.detailButton.isVisible()) {
/* 1061 */           BasicErrorPaneUI.this.detailButton.setBounds(x, buttonY, dim.width, dim.height);
/*      */         }
/* 1063 */         BasicErrorPaneUI.this.errorScrollPane.setBounds(spx, spy, spDim.width, buttonY - spy);
/* 1064 */         if (BasicErrorPaneUI.this.reportButton.isVisible()) {
/* 1065 */           dim = BasicErrorPaneUI.this.reportButton.getPreferredSize();
/* 1066 */           x -= dim.width;
/* 1067 */           x -= 5;
/* 1068 */           BasicErrorPaneUI.this.reportButton.setBounds(x, buttonY, dim.width, dim.height);
/*      */         }
/*      */         
/* 1071 */         dim = BasicErrorPaneUI.this.closeButton.getPreferredSize();
/* 1072 */         x -= dim.width;
/* 1073 */         x -= 5;
/* 1074 */         BasicErrorPaneUI.this.closeButton.setBounds(x, buttonY, dim.width, dim.height);
/*      */         
/*      */ 
/* 1077 */         if (BasicErrorPaneUI.this.detailsPanel.isVisible())
/*      */         {
/* 1079 */           y = buttonY + dim.height + 6;
/* 1080 */           x = leftEdge;
/* 1081 */           int width = rightEdge - x;
/* 1082 */           BasicErrorPaneUI.this.detailsPanel.setBounds(x, y, width, parent.getHeight() - (y + insets.bottom));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void centerWindow(Window w, Component owner)
/*      */   {
/* 1091 */     if (owner != null) {
/* 1092 */       Point p = owner.getLocation();
/* 1093 */       p.x += owner.getWidth() / 2;
/* 1094 */       p.y += owner.getHeight() / 2;
/* 1095 */       SwingUtilities.convertPointToScreen(p, owner);
/* 1096 */       w.setLocation(p);
/*      */     } else {
/* 1098 */       w.setLocation(WindowUtils.getPointForCentering(w));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void centerWindow(JInternalFrame w, Component owner)
/*      */   {
/* 1105 */     if (owner != null) {
/* 1106 */       Point p = owner.getLocation();
/* 1107 */       p.x += owner.getWidth() / 2;
/* 1108 */       p.y += owner.getHeight() / 2;
/* 1109 */       SwingUtilities.convertPointToScreen(p, owner);
/* 1110 */       w.setLocation(p);
/*      */     } else {
/* 1112 */       w.setLocation(WindowUtils.getPointForCentering(w));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String escapeXml(String input)
/*      */   {
/* 1121 */     String s = input == null ? "" : input.replace("&", "&amp;");
/* 1122 */     s = s.replace("<", "&lt;");
/* 1123 */     return s = s.replace(">", "&gt;");
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicErrorPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */