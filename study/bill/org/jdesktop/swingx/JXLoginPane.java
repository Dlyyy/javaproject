/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.CardLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Font;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.GridLayout;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyEventDispatcher;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Robot;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowFocusListener;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.AbstractListModel;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.ComboBoxEditor;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.DefaultComboBoxModel;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPasswordField;
/*      */ import javax.swing.JProgressBar;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.text.View;
/*      */ import org.jdesktop.swingx.action.AbstractActionExt;
/*      */ import org.jdesktop.swingx.auth.DefaultUserNameStore;
/*      */ import org.jdesktop.swingx.auth.LoginAdapter;
/*      */ import org.jdesktop.swingx.auth.LoginEvent;
/*      */ import org.jdesktop.swingx.auth.LoginListener;
/*      */ import org.jdesktop.swingx.auth.LoginService;
/*      */ import org.jdesktop.swingx.auth.PasswordStore;
/*      */ import org.jdesktop.swingx.auth.UserNameStore;
/*      */ import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
/*      */ import org.jdesktop.swingx.painter.MattePainter;
/*      */ import org.jdesktop.swingx.plaf.LoginPaneAddon;
/*      */ import org.jdesktop.swingx.plaf.LoginPaneUI;
/*      */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
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
/*      */ public class JXLoginPane
/*      */   extends JXPanel
/*      */ {
/*  146 */   private static final Logger LOG = Logger.getLogger(JXLoginPane.class.getName());
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long serialVersionUID = 3544949969896288564L;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String uiClassID = "LoginPaneUI";
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String LOGIN_ACTION_COMMAND = "login";
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String CANCEL_LOGIN_ACTION_COMMAND = "cancel-login";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static enum SaveMode
/*      */   {
/*  170 */     NONE,  USER_NAME,  PASSWORD,  BOTH;
/*      */     
/*      */     private SaveMode() {} }
/*      */   
/*  174 */   public static enum Status { NOT_STARTED,  IN_PROGRESS,  FAILED,  CANCELLED,  SUCCEEDED;
/*      */     
/*      */     private Status() {} }
/*      */   
/*  178 */   private static String CLASS_NAME = JXLoginPane.class.getSimpleName();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  183 */   private Status status = Status.NOT_STARTED;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXImagePanel banner;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String bannerText;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JLabel messageLabel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXLabel errorMessageLabel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXPanel loginPanel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXPanel contentPanel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private NameComponent namePanel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JPasswordField passwordField;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JComboBox serverCombo;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JCheckBox saveCB;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JLabel capsOn;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXPanel progressPanel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JLabel progressMessageLabel;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private LoginService loginService;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private PasswordStore passwordStore;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private UserNameStore userNameStore;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<String> servers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SaveMode saveMode;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Cursor oldCursor;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  287 */   private boolean namePanelEnabled = true;
/*      */   
/*      */ 
/*      */   private LoginListener defaultLoginListener;
/*      */   
/*      */   private final CapsOnTest capsOnTest;
/*      */   
/*      */   private boolean caps;
/*      */   
/*      */   private boolean isTestingCaps;
/*      */   
/*      */   private final KeyEventDispatcher capsOnListener;
/*      */   
/*  300 */   private boolean capsLockSupport = true;
/*      */   
/*      */ 
/*      */ 
/*      */   private JXBtnPanel buttonPanel;
/*      */   
/*      */ 
/*      */ 
/*      */   private final CapsOnWinListener capsOnWinListener;
/*      */   
/*      */ 
/*      */ 
/*      */   private JPanel contentCardPane;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean isErrorMessageSet;
/*      */   
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*  322 */     LookAndFeelAddons.contribute(new LoginPaneAddon());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void reinitLocales(Locale l)
/*      */   {
/*  332 */     setBannerText(UIManagerExt.getString(CLASS_NAME + ".bannerString", getLocale()));
/*  333 */     this.banner.setImage(createLoginBanner());
/*  334 */     if (!this.isErrorMessageSet) {
/*  335 */       this.errorMessageLabel.setText(UIManager.getString(CLASS_NAME + ".errorMessage", getLocale()));
/*      */     }
/*  337 */     this.progressMessageLabel.setText(UIManagerExt.getString(CLASS_NAME + ".pleaseWait", getLocale()));
/*  338 */     recreateLoginPanel();
/*  339 */     Window w = SwingUtilities.getWindowAncestor(this);
/*  340 */     if ((w instanceof JXLoginFrame)) {
/*  341 */       JXLoginFrame f = (JXLoginFrame)w;
/*  342 */       f.setTitle(UIManagerExt.getString(CLASS_NAME + ".titleString", getLocale()));
/*  343 */       if (this.buttonPanel != null) {
/*  344 */         this.buttonPanel.getOk().setText(UIManagerExt.getString(CLASS_NAME + ".loginString", getLocale()));
/*  345 */         this.buttonPanel.getCancel().setText(UIManagerExt.getString(CLASS_NAME + ".cancelString", getLocale()));
/*      */       }
/*      */     }
/*  348 */     JLabel lbl = (JLabel)this.passwordField.getClientProperty("labeledBy");
/*  349 */     if (lbl != null) {
/*  350 */       lbl.setText(UIManagerExt.getString(CLASS_NAME + ".passwordString", getLocale()));
/*      */     }
/*  352 */     lbl = (JLabel)this.namePanel.getComponent().getClientProperty("labeledBy");
/*  353 */     if (lbl != null) {
/*  354 */       lbl.setText(UIManagerExt.getString(CLASS_NAME + ".nameString", getLocale()));
/*      */     }
/*  356 */     if (this.serverCombo != null) {
/*  357 */       lbl = (JLabel)this.serverCombo.getClientProperty("labeledBy");
/*  358 */       if (lbl != null) {
/*  359 */         lbl.setText(UIManagerExt.getString(CLASS_NAME + ".serverString", getLocale()));
/*      */       }
/*      */     }
/*  362 */     this.saveCB.setText(UIManagerExt.getString(CLASS_NAME + ".rememberPasswordString", getLocale()));
/*      */     
/*      */ 
/*  365 */     this.capsOn.setText(isCapsLockOn() ? UIManagerExt.getString(CLASS_NAME + ".capsOnWarning", getLocale()) : " ");
/*      */     
/*  367 */     getActionMap().get("login").putValue("Name", UIManagerExt.getString(CLASS_NAME + ".loginString", getLocale()));
/*  368 */     getActionMap().get("cancel-login").putValue("Name", UIManagerExt.getString(CLASS_NAME + ".cancelString", getLocale()));
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
/*      */   public JXLoginPane()
/*      */   {
/*  382 */     this(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXLoginPane(LoginService service)
/*      */   {
/*  393 */     this(service, null, null);
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
/*      */   public JXLoginPane(LoginService service, PasswordStore passwordStore, UserNameStore userStore)
/*      */   {
/*  414 */     this(service, passwordStore, userStore, null);
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
/*      */   public JXLoginPane(LoginService service, PasswordStore passwordStore, UserNameStore userStore, List<String> servers)
/*      */   {
/*  440 */     if (Boolean.parseBoolean(System.getProperty("swingx.enableCapslockTesting"))) {
/*  441 */       this.capsOnTest = new CapsOnTest(null);
/*  442 */       this.capsOnListener = new KeyEventDispatcher() {
/*      */         public boolean dispatchKeyEvent(KeyEvent e) {
/*  444 */           if (e.getID() != 401) {
/*  445 */             return false;
/*      */           }
/*  447 */           if (e.getKeyCode() == 20) {
/*  448 */             JXLoginPane.this.setCapsLock(!JXLoginPane.this.isCapsLockOn());
/*      */           }
/*  450 */           return false;
/*  451 */         } };
/*  452 */       this.capsOnWinListener = new CapsOnWinListener(this.capsOnTest);
/*      */     } else {
/*  454 */       this.capsOnTest = null;
/*  455 */       this.capsOnListener = null;
/*  456 */       this.capsOnWinListener = null;
/*  457 */       this.capsLockSupport = false;
/*      */     }
/*  459 */     setLoginService(service);
/*  460 */     setPasswordStore(passwordStore);
/*  461 */     setUserNameStore(userStore);
/*  462 */     setServers(servers);
/*      */     
/*      */ 
/*      */ 
/*  466 */     getActionMap().put("login", createLoginAction());
/*  467 */     getActionMap().put("cancel-login", createCancelAction());
/*      */     
/*      */ 
/*  470 */     if ((passwordStore != null) && (userStore != null)) {
/*  471 */       this.saveMode = SaveMode.BOTH;
/*  472 */     } else if (passwordStore != null) {
/*  473 */       this.saveMode = SaveMode.PASSWORD;
/*  474 */     } else if (userStore != null) {
/*  475 */       this.saveMode = SaveMode.USER_NAME;
/*      */     } else {
/*  477 */       this.saveMode = SaveMode.NONE;
/*      */     }
/*      */     
/*      */ 
/*  481 */     setOpaque(false);
/*  482 */     initComponents();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setCapsLock(boolean b)
/*      */   {
/*  490 */     this.caps = b;
/*  491 */     this.capsOn.setText(this.caps ? UIManagerExt.getString(CLASS_NAME + ".capsOnWarning", getLocale()) : " ");
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
/*      */   public boolean isCapsLockOn()
/*      */   {
/*  504 */     return this.caps;
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
/*      */   public boolean isCapsLockDetectionSupported()
/*      */   {
/*  518 */     return this.capsLockSupport;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LoginPaneUI getUI()
/*      */   {
/*  528 */     return (LoginPaneUI)super.getUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUI(LoginPaneUI ui)
/*      */   {
/*  539 */     if (this.banner == null) {
/*  540 */       this.banner = new JXImagePanel();
/*      */     }
/*  542 */     if (this.errorMessageLabel == null) {
/*  543 */       this.errorMessageLabel = new JXLabel(UIManagerExt.getString(CLASS_NAME + ".errorMessage", getLocale()));
/*      */     }
/*  545 */     super.setUI(ui);
/*  546 */     this.banner.setImage(createLoginBanner());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  558 */     setUI((LoginPaneUI)LookAndFeelAddons.getUI(this, LoginPaneUI.class));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  570 */     return "LoginPaneUI";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void recreateLoginPanel()
/*      */   {
/*  577 */     JXPanel old = this.loginPanel;
/*  578 */     this.loginPanel = createLoginPanel();
/*  579 */     this.loginPanel.setBorder(BorderFactory.createEmptyBorder(0, 36, 7, 11));
/*  580 */     this.contentPanel.remove(old);
/*  581 */     this.contentPanel.add(this.loginPanel, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXPanel createLoginPanel()
/*      */   {
/*  592 */     JXPanel loginPanel = new JXPanel();
/*      */     
/*  594 */     JPasswordField oldPwd = this.passwordField;
/*      */     
/*  596 */     this.passwordField = new JPasswordField("", 15);
/*  597 */     JLabel passwordLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".passwordString", getLocale()));
/*  598 */     passwordLabel.setLabelFor(this.passwordField);
/*  599 */     if (oldPwd != null) {
/*  600 */       this.passwordField.setText(new String(oldPwd.getPassword()));
/*      */     }
/*      */     
/*  603 */     NameComponent oldPanel = this.namePanel;
/*      */     
/*  605 */     if (this.saveMode == SaveMode.NONE) {
/*  606 */       this.namePanel = new SimpleNamePanel();
/*      */     } else {
/*  608 */       this.namePanel = new ComboNamePanel();
/*      */     }
/*  610 */     if (oldPanel != null)
/*      */     {
/*  612 */       this.namePanel.setUserName(oldPanel.getUserName());
/*  613 */       this.namePanel.setEnabled(oldPanel.isEnabled());
/*  614 */       this.namePanel.setEditable(oldPanel.isEditable());
/*      */     } else {
/*  616 */       this.namePanel.setEnabled(this.namePanelEnabled);
/*  617 */       this.namePanel.setEditable(this.namePanelEnabled);
/*      */     }
/*  619 */     JLabel nameLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".nameString", getLocale()));
/*  620 */     nameLabel.setLabelFor(this.namePanel.getComponent());
/*      */     
/*      */ 
/*  623 */     JLabel serverLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".serverString", getLocale()));
/*  624 */     if (this.servers.size() > 1) {
/*  625 */       this.serverCombo = new JComboBox(this.servers.toArray());
/*  626 */       serverLabel.setLabelFor(this.serverCombo);
/*      */     } else {
/*  628 */       this.serverCombo = null;
/*      */     }
/*      */     
/*      */ 
/*  632 */     this.saveCB = new JCheckBox(UIManagerExt.getString(CLASS_NAME + ".rememberPasswordString", getLocale()));
/*  633 */     this.saveCB.setIconTextGap(10);
/*      */     
/*  635 */     this.saveCB.setSelected(false);
/*      */     
/*  637 */     this.saveCB.setVisible((this.saveMode == SaveMode.PASSWORD) || (this.saveMode == SaveMode.BOTH));
/*  638 */     this.saveCB.setOpaque(false);
/*      */     
/*  640 */     this.capsOn = new JLabel(" ");
/*      */     
/*      */ 
/*  643 */     int lShift = 3;
/*  644 */     GridLayout grid = new GridLayout(2, 1);
/*  645 */     grid.setVgap(5);
/*  646 */     JPanel fields = new JPanel(grid);
/*  647 */     fields.setOpaque(false);
/*  648 */     fields.add(this.namePanel.getComponent());
/*  649 */     fields.add(this.passwordField);
/*      */     
/*  651 */     loginPanel.setLayout(new GridBagLayout());
/*  652 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/*  653 */     gridBagConstraints.gridx = 0;
/*  654 */     gridBagConstraints.gridy = 0;
/*  655 */     gridBagConstraints.anchor = 21;
/*  656 */     gridBagConstraints.insets = new Insets(4, lShift, 5, 11);
/*  657 */     loginPanel.add(nameLabel, gridBagConstraints);
/*      */     
/*  659 */     gridBagConstraints = new GridBagConstraints();
/*  660 */     gridBagConstraints.gridx = 1;
/*  661 */     gridBagConstraints.gridy = 0;
/*  662 */     gridBagConstraints.gridwidth = 1;
/*  663 */     gridBagConstraints.gridheight = 2;
/*  664 */     gridBagConstraints.anchor = 21;
/*  665 */     gridBagConstraints.fill = 1;
/*  666 */     gridBagConstraints.weightx = 1.0D;
/*  667 */     gridBagConstraints.insets = new Insets(0, 0, 5, 0);
/*  668 */     loginPanel.add(fields, gridBagConstraints);
/*      */     
/*  670 */     gridBagConstraints = new GridBagConstraints();
/*  671 */     gridBagConstraints.gridx = 0;
/*  672 */     gridBagConstraints.gridy = 1;
/*  673 */     gridBagConstraints.anchor = 21;
/*  674 */     gridBagConstraints.insets = new Insets(5, lShift, 5, 11);
/*  675 */     loginPanel.add(passwordLabel, gridBagConstraints);
/*      */     
/*  677 */     if (this.serverCombo != null) {
/*  678 */       gridBagConstraints = new GridBagConstraints();
/*  679 */       gridBagConstraints.gridx = 0;
/*  680 */       gridBagConstraints.gridy = 2;
/*  681 */       gridBagConstraints.anchor = 21;
/*  682 */       gridBagConstraints.insets = new Insets(0, lShift, 5, 11);
/*  683 */       loginPanel.add(serverLabel, gridBagConstraints);
/*      */       
/*  685 */       gridBagConstraints = new GridBagConstraints();
/*  686 */       gridBagConstraints.gridx = 1;
/*  687 */       gridBagConstraints.gridy = 2;
/*  688 */       gridBagConstraints.gridwidth = 1;
/*  689 */       gridBagConstraints.anchor = 21;
/*  690 */       gridBagConstraints.fill = 2;
/*  691 */       gridBagConstraints.weightx = 1.0D;
/*  692 */       gridBagConstraints.insets = new Insets(0, 0, 5, 0);
/*  693 */       loginPanel.add(this.serverCombo, gridBagConstraints);
/*      */       
/*  695 */       gridBagConstraints = new GridBagConstraints();
/*  696 */       gridBagConstraints.gridx = 0;
/*  697 */       gridBagConstraints.gridy = 3;
/*  698 */       gridBagConstraints.gridwidth = 2;
/*  699 */       gridBagConstraints.fill = 2;
/*  700 */       gridBagConstraints.anchor = 21;
/*  701 */       gridBagConstraints.weightx = 1.0D;
/*  702 */       gridBagConstraints.insets = new Insets(0, 0, 4, 0);
/*  703 */       loginPanel.add(this.saveCB, gridBagConstraints);
/*      */       
/*  705 */       gridBagConstraints = new GridBagConstraints();
/*  706 */       gridBagConstraints.gridx = 0;
/*  707 */       gridBagConstraints.gridy = 4;
/*  708 */       gridBagConstraints.gridwidth = 2;
/*  709 */       gridBagConstraints.fill = 2;
/*  710 */       gridBagConstraints.anchor = 21;
/*  711 */       gridBagConstraints.weightx = 1.0D;
/*  712 */       gridBagConstraints.insets = new Insets(0, lShift, 0, 11);
/*  713 */       loginPanel.add(this.capsOn, gridBagConstraints);
/*      */     } else {
/*  715 */       gridBagConstraints = new GridBagConstraints();
/*  716 */       gridBagConstraints.gridx = 0;
/*  717 */       gridBagConstraints.gridy = 2;
/*  718 */       gridBagConstraints.gridwidth = 2;
/*  719 */       gridBagConstraints.fill = 2;
/*  720 */       gridBagConstraints.anchor = 21;
/*  721 */       gridBagConstraints.weightx = 1.0D;
/*  722 */       gridBagConstraints.insets = new Insets(0, 0, 4, 0);
/*  723 */       loginPanel.add(this.saveCB, gridBagConstraints);
/*      */       
/*  725 */       gridBagConstraints = new GridBagConstraints();
/*  726 */       gridBagConstraints.gridx = 0;
/*  727 */       gridBagConstraints.gridy = 3;
/*  728 */       gridBagConstraints.gridwidth = 2;
/*  729 */       gridBagConstraints.fill = 2;
/*  730 */       gridBagConstraints.anchor = 21;
/*  731 */       gridBagConstraints.weightx = 1.0D;
/*  732 */       gridBagConstraints.insets = new Insets(0, lShift, 0, 11);
/*  733 */       loginPanel.add(this.capsOn, gridBagConstraints);
/*      */     }
/*  735 */     loginPanel.setOpaque(false);
/*  736 */     return loginPanel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setComponentOrientation(ComponentOrientation orient)
/*      */   {
/*  746 */     if (orient != super.getComponentOrientation()) {
/*  747 */       super.setComponentOrientation(orient);
/*  748 */       this.banner.setImage(createLoginBanner());
/*  749 */       this.progressPanel.applyComponentOrientation(orient);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initComponents()
/*      */   {
/*  758 */     this.banner.setImage(createLoginBanner());
/*      */     
/*      */ 
/*  761 */     this.messageLabel = new JLabel(" ");
/*  762 */     this.messageLabel.setOpaque(false);
/*  763 */     this.messageLabel.setFont(this.messageLabel.getFont().deriveFont(1));
/*      */     
/*      */ 
/*  766 */     this.loginPanel = createLoginPanel();
/*      */     
/*      */ 
/*  769 */     this.errorMessageLabel.setIcon(UIManager.getIcon(CLASS_NAME + ".errorIcon", getLocale()));
/*  770 */     this.errorMessageLabel.setVerticalTextPosition(1);
/*  771 */     this.errorMessageLabel.setLineWrap(true);
/*  772 */     this.errorMessageLabel.setPaintBorderInsets(false);
/*  773 */     this.errorMessageLabel.setBackgroundPainter(new MattePainter(UIManager.getColor(CLASS_NAME + ".errorBackground", getLocale()), true));
/*  774 */     this.errorMessageLabel.setMaxLineSpan(320);
/*  775 */     this.errorMessageLabel.setVisible(false);
/*      */     
/*      */ 
/*      */ 
/*  779 */     this.contentPanel = new JXPanel(new LoginPaneLayout(null));
/*  780 */     this.contentPanel.setOpaque(false);
/*  781 */     this.messageLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 7, 11));
/*  782 */     this.contentPanel.add(this.messageLabel);
/*  783 */     this.loginPanel.setBorder(BorderFactory.createEmptyBorder(0, 36, 7, 11));
/*  784 */     this.contentPanel.add(this.loginPanel);
/*  785 */     this.errorMessageLabel.setBorder(UIManager.getBorder(CLASS_NAME + ".errorBorder", getLocale()));
/*  786 */     this.contentPanel.add(this.errorMessageLabel);
/*      */     
/*      */ 
/*  789 */     this.progressPanel = new JXPanel(new GridBagLayout());
/*  790 */     this.progressPanel.setOpaque(false);
/*  791 */     this.progressMessageLabel = new JLabel(UIManagerExt.getString(CLASS_NAME + ".pleaseWait", getLocale()));
/*  792 */     this.progressMessageLabel.setFont(UIManager.getFont(CLASS_NAME + ".pleaseWaitFont", getLocale()));
/*  793 */     JProgressBar pb = new JProgressBar();
/*  794 */     pb.setIndeterminate(true);
/*  795 */     JButton cancelButton = new JButton(getActionMap().get("cancel-login"));
/*  796 */     this.progressPanel.add(this.progressMessageLabel, new GridBagConstraints(0, 0, 2, 1, 1.0D, 0.0D, 21, 2, new Insets(12, 12, 11, 11), 0, 0));
/*  797 */     this.progressPanel.add(pb, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 24, 11, 7), 0, 0));
/*  798 */     this.progressPanel.add(cancelButton, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 11, 11), 0, 0));
/*      */     
/*      */ 
/*  801 */     setLayout(new BorderLayout());
/*  802 */     add(this.banner, "North");
/*  803 */     this.contentCardPane = new JPanel(new CardLayout());
/*  804 */     this.contentCardPane.setOpaque(false);
/*  805 */     this.contentCardPane.add(this.contentPanel, "0");
/*  806 */     this.contentCardPane.add(this.progressPanel, "1");
/*  807 */     add(this.contentCardPane, "Center");
/*      */   }
/*      */   
/*      */   private final class LoginPaneLayout extends VerticalLayout implements LayoutManager {
/*      */     private LoginPaneLayout() {}
/*      */     
/*      */     public Dimension preferredLayoutSize(Container parent) {
/*  814 */       Insets insets = parent.getInsets();
/*  815 */       Dimension pref = new Dimension(0, 0);
/*  816 */       int gap = getGap();
/*  817 */       int i = 0; for (int c = parent.getComponentCount(); i < c; i++) {
/*  818 */         Component m = parent.getComponent(i);
/*  819 */         if (m.isVisible()) {
/*  820 */           Dimension componentPreferredSize = m.getPreferredSize();
/*      */           
/*  822 */           if ((m instanceof JLabel)) {
/*  823 */             View view = (View)((JLabel)m).getClientProperty("html");
/*  824 */             if (view != null) {
/*  825 */               view.setSize(pref.width, m.getHeight());
/*      */               
/*  827 */               componentPreferredSize = m.getPreferredSize();
/*      */             }
/*      */           } else {
/*  830 */             pref.width = Math.max(pref.width, componentPreferredSize.width);
/*      */           }
/*  832 */           pref.height += componentPreferredSize.height + gap;
/*      */         }
/*      */       }
/*      */       
/*  836 */       pref.width += insets.left + insets.right;
/*  837 */       pref.height += insets.top + insets.bottom;
/*      */       
/*  839 */       return pref;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Image createLoginBanner()
/*      */   {
/*  848 */     return getUI() == null ? null : getUI().getBanner();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Action createLoginAction()
/*      */   {
/*  855 */     return new LoginAction(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Action createCancelAction()
/*      */   {
/*  862 */     return new CancelAction(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SaveMode getSaveMode()
/*      */   {
/*  871 */     return this.saveMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSaveMode(SaveMode saveMode)
/*      */   {
/*  881 */     if (this.saveMode != saveMode) {
/*  882 */       SaveMode oldMode = getSaveMode();
/*  883 */       this.saveMode = saveMode;
/*  884 */       recreateLoginPanel();
/*  885 */       firePropertyChange("saveMode", oldMode, getSaveMode());
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isRememberPassword() {
/*  890 */     return (this.saveCB.isVisible()) && (this.saveCB.isSelected());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getServers()
/*      */   {
/*  897 */     return Collections.unmodifiableList(this.servers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServers(List<String> servers)
/*      */   {
/*  905 */     if (this.servers == null) {
/*  906 */       this.servers = (servers == null ? new ArrayList() : servers);
/*  907 */     } else if (this.servers != servers) {
/*  908 */       List<String> old = getServers();
/*  909 */       this.servers = (servers == null ? new ArrayList() : servers);
/*  910 */       recreateLoginPanel();
/*  911 */       firePropertyChange("servers", old, getServers());
/*      */     }
/*      */   }
/*      */   
/*      */   private LoginListener getDefaultLoginListener() {
/*  916 */     if (this.defaultLoginListener == null) {
/*  917 */       this.defaultLoginListener = new LoginListenerImpl();
/*      */     }
/*      */     
/*  920 */     return this.defaultLoginListener;
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
/*      */   public void setLoginService(LoginService service)
/*      */   {
/*  933 */     LoginService oldService = getLoginService();
/*  934 */     LoginService newService = service == null ? new NullLoginService(null) : service;
/*      */     
/*      */ 
/*  937 */     if (!newService.equals(oldService)) {
/*  938 */       if (oldService != null) {
/*  939 */         oldService.removeLoginListener(getDefaultLoginListener());
/*      */       }
/*      */       
/*  942 */       this.loginService = newService;
/*  943 */       this.loginService.addLoginListener(getDefaultLoginListener());
/*      */       
/*  945 */       firePropertyChange("loginService", oldService, getLoginService());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LoginService getLoginService()
/*      */   {
/*  955 */     return this.loginService;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPasswordStore(PasswordStore store)
/*      */   {
/*  964 */     PasswordStore oldStore = getPasswordStore();
/*  965 */     PasswordStore newStore = store == null ? new NullPasswordStore(null) : store;
/*      */     
/*      */ 
/*  968 */     if (!newStore.equals(oldStore)) {
/*  969 */       this.passwordStore = newStore;
/*      */       
/*  971 */       firePropertyChange("passwordStore", oldStore, getPasswordStore());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UserNameStore getUserNameStore()
/*      */   {
/*  981 */     return this.userNameStore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserNameStore(UserNameStore store)
/*      */   {
/*  989 */     UserNameStore oldStore = getUserNameStore();
/*  990 */     UserNameStore newStore = store == null ? new DefaultUserNameStore() : store;
/*      */     
/*      */ 
/*  993 */     if (!newStore.equals(oldStore)) {
/*  994 */       this.userNameStore = newStore;
/*      */       
/*  996 */       firePropertyChange("userNameStore", oldStore, getUserNameStore());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PasswordStore getPasswordStore()
/*      */   {
/* 1006 */     return this.passwordStore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserName(String username)
/*      */   {
/* 1015 */     if (this.namePanel != null) {
/* 1016 */       String old = getUserName();
/* 1017 */       this.namePanel.setUserName(username);
/* 1018 */       firePropertyChange("userName", old, getUserName());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserNameEnabled(boolean enabled)
/*      */   {
/* 1028 */     boolean old = isUserNameEnabled();
/* 1029 */     this.namePanelEnabled = enabled;
/* 1030 */     if (this.namePanel != null) {
/* 1031 */       this.namePanel.setEnabled(enabled);
/* 1032 */       this.namePanel.setEditable(enabled);
/*      */     }
/* 1034 */     firePropertyChange("userNameEnabled", old, isUserNameEnabled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUserNameEnabled()
/*      */   {
/* 1042 */     return this.namePanelEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserName()
/*      */   {
/* 1050 */     return this.namePanel == null ? null : this.namePanel.getUserName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPassword(char[] password)
/*      */   {
/* 1059 */     this.passwordField.setText(new String(password));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char[] getPassword()
/*      */   {
/* 1068 */     return this.passwordField.getPassword();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Image getBanner()
/*      */   {
/* 1075 */     return this.banner.getImage();
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
/*      */   public void setBanner(Image img)
/*      */   {
/* 1088 */     Image oldImage = getBanner();
/*      */     
/* 1090 */     if (oldImage != img) {
/* 1091 */       this.banner.setImage(img);
/* 1092 */       firePropertyChange("banner", oldImage, getBanner());
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
/*      */   public void setBannerText(String text)
/*      */   {
/* 1105 */     if (text == null) {
/* 1106 */       text = "";
/*      */     }
/*      */     
/* 1109 */     if (!text.equals(this.bannerText)) {
/* 1110 */       String oldText = this.bannerText;
/* 1111 */       this.bannerText = text;
/*      */       
/* 1113 */       this.banner.setImage(createLoginBanner());
/* 1114 */       firePropertyChange("bannerText", oldText, text);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getBannerText()
/*      */   {
/* 1122 */     return this.bannerText;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getMessage()
/*      */   {
/* 1129 */     return this.messageLabel.getText();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setMessage(String message)
/*      */   {
/* 1136 */     String old = this.messageLabel.getText();
/* 1137 */     this.messageLabel.setText(message);
/* 1138 */     firePropertyChange("message", old, this.messageLabel.getText());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getErrorMessage()
/*      */   {
/* 1145 */     return this.errorMessageLabel.getText();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setErrorMessage(String errorMessage)
/*      */   {
/* 1152 */     this.isErrorMessageSet = true;
/* 1153 */     String old = this.errorMessageLabel.getText();
/* 1154 */     this.errorMessageLabel.setText(errorMessage);
/* 1155 */     firePropertyChange("errorMessage", old, this.errorMessageLabel.getText());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Status getStatus()
/*      */   {
/* 1162 */     return this.status;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void setStatus(Status newStatus)
/*      */   {
/* 1169 */     if (this.status != newStatus) {
/* 1170 */       Status oldStatus = this.status;
/* 1171 */       this.status = newStatus;
/* 1172 */       firePropertyChange("status", oldStatus, newStatus);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setLocale(Locale l)
/*      */   {
/* 1178 */     super.setLocale(l);
/* 1179 */     reinitLocales(l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void startLogin()
/*      */   {
/* 1191 */     this.oldCursor = getCursor();
/*      */     try {
/* 1193 */       setCursor(Cursor.getPredefinedCursor(3));
/* 1194 */       this.progressMessageLabel.setText(UIManagerExt.getString(CLASS_NAME + ".pleaseWait", getLocale()));
/* 1195 */       String name = getUserName();
/* 1196 */       char[] password = getPassword();
/* 1197 */       String server = this.serverCombo == null ? null : this.servers.size() == 1 ? (String)this.servers.get(0) : (String)this.serverCombo.getSelectedItem();
/*      */       
/* 1199 */       this.loginService.startAuthentication(name, password, server);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/* 1203 */       LOG.log(Level.WARNING, "Authentication exception while logging in", ex);
/*      */     } finally {
/* 1205 */       setCursor(this.oldCursor);
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
/*      */   protected void cancelLogin()
/*      */   {
/* 1218 */     this.progressMessageLabel.setText(UIManagerExt.getString(CLASS_NAME + ".cancelWait", getLocale()));
/* 1219 */     getActionMap().get("cancel-login").setEnabled(false);
/* 1220 */     this.loginService.cancelAuthentication();
/* 1221 */     setCursor(this.oldCursor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void savePassword()
/*      */   {
/* 1229 */     if ((this.saveCB.isSelected()) && ((this.saveMode == SaveMode.BOTH) || (this.saveMode == SaveMode.PASSWORD)) && (this.passwordStore != null))
/*      */     {
/*      */ 
/* 1232 */       this.passwordStore.set(getUserName(), getLoginService().getServer(), getPassword());
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeNotify()
/*      */   {
/*      */     try
/*      */     {
/* 1240 */       if (this.capsLockSupport)
/* 1241 */         KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.capsOnListener);
/* 1242 */       Container c = getTLA();
/* 1243 */       if ((c instanceof Window)) {
/* 1244 */         Window w = (Window)c;
/* 1245 */         w.removeWindowFocusListener(this.capsOnWinListener);
/* 1246 */         w.removeWindowListener(this.capsOnWinListener);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/* 1251 */     super.removeNotify();
/*      */   }
/*      */   
/*      */   private Window getTLA() {
/* 1255 */     Container c = this;
/*      */     
/* 1257 */     while ((c.getParent() != null) && (!(c instanceof Window))) {
/* 1258 */       c = c.getParent();
/*      */     }
/* 1260 */     return (Window)c;
/*      */   }
/*      */   
/*      */   public void addNotify()
/*      */   {
/*      */     try {
/* 1266 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.capsOnListener);
/*      */       
/* 1268 */       Container c = getTLA();
/* 1269 */       if ((c instanceof Window)) {
/* 1270 */         Window w = (Window)c;
/* 1271 */         w.addWindowFocusListener(this.capsOnWinListener);
/* 1272 */         w.addWindowListener(this.capsOnWinListener);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/* 1276 */       this.capsLockSupport = false;
/*      */     }
/* 1278 */     super.addNotify();
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
/*      */   protected class LoginListenerImpl
/*      */     extends LoginAdapter
/*      */   {
/*      */     protected LoginListenerImpl() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void loginSucceeded(LoginEvent source)
/*      */     {
/* 1322 */       String userName = JXLoginPane.this.namePanel.getUserName();
/* 1323 */       if (((JXLoginPane.this.getSaveMode() == JXLoginPane.SaveMode.USER_NAME) || (JXLoginPane.this.getSaveMode() == JXLoginPane.SaveMode.BOTH)) && (userName != null) && (!userName.trim().equals("")))
/*      */       {
/* 1325 */         JXLoginPane.this.userNameStore.addUserName(userName);
/* 1326 */         JXLoginPane.this.userNameStore.saveUserNames();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1332 */       if (JXLoginPane.this.saveCB.isSelected()) {
/* 1333 */         JXLoginPane.this.savePassword();
/*      */ 
/*      */       }
/* 1336 */       else if (JXLoginPane.this.passwordStore != null) {
/* 1337 */         JXLoginPane.this.passwordStore.removeUserPassword(userName);
/*      */       }
/*      */       
/*      */ 
/* 1341 */       JXLoginPane.this.setStatus(JXLoginPane.Status.SUCCEEDED);
/*      */     }
/*      */     
/*      */     public void loginStarted(LoginEvent source)
/*      */     {
/* 1346 */       assert (EventQueue.isDispatchThread());
/* 1347 */       JXLoginPane.this.getActionMap().get("login").setEnabled(false);
/* 1348 */       JXLoginPane.this.getActionMap().get("cancel-login").setEnabled(true);
/*      */       
/*      */ 
/* 1351 */       ((CardLayout)JXLoginPane.this.contentCardPane.getLayout()).last(JXLoginPane.this.contentCardPane);
/* 1352 */       JXLoginPane.this.revalidate();
/* 1353 */       JXLoginPane.this.repaint();
/* 1354 */       JXLoginPane.this.setStatus(JXLoginPane.Status.IN_PROGRESS);
/*      */     }
/*      */     
/*      */     public void loginFailed(LoginEvent source)
/*      */     {
/* 1359 */       assert (EventQueue.isDispatchThread());
/*      */       
/*      */ 
/* 1362 */       ((CardLayout)JXLoginPane.this.contentCardPane.getLayout()).first(JXLoginPane.this.contentCardPane);
/* 1363 */       JXLoginPane.this.getActionMap().get("login").setEnabled(true);
/* 1364 */       JXLoginPane.this.errorMessageLabel.setVisible(true);
/* 1365 */       JXLoginPane.this.revalidate();
/* 1366 */       JXLoginPane.this.repaint();
/* 1367 */       JXLoginPane.this.setStatus(JXLoginPane.Status.FAILED);
/*      */     }
/*      */     
/*      */     public void loginCanceled(LoginEvent source)
/*      */     {
/* 1372 */       assert (EventQueue.isDispatchThread());
/*      */       
/*      */ 
/* 1375 */       ((CardLayout)JXLoginPane.this.contentCardPane.getLayout()).first(JXLoginPane.this.contentCardPane);
/* 1376 */       JXLoginPane.this.getActionMap().get("login").setEnabled(true);
/* 1377 */       JXLoginPane.this.errorMessageLabel.setVisible(false);
/* 1378 */       JXLoginPane.this.revalidate();
/* 1379 */       JXLoginPane.this.repaint();
/* 1380 */       JXLoginPane.this.setStatus(JXLoginPane.Status.CANCELLED);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class LoginAction
/*      */     extends AbstractActionExt
/*      */   {
/*      */     private static final long serialVersionUID = 7256761187925982485L;
/*      */     private JXLoginPane panel;
/*      */     
/*      */     public LoginAction(JXLoginPane p)
/*      */     {
/* 1392 */       super("login");
/* 1393 */       this.panel = p;
/*      */     }
/*      */     
/* 1396 */     public void actionPerformed(ActionEvent e) { this.panel.startLogin(); }
/*      */     
/*      */     public void itemStateChanged(ItemEvent e) {}
/*      */   }
/*      */   
/*      */   private static final class CancelAction
/*      */     extends AbstractActionExt
/*      */   {
/*      */     private static final long serialVersionUID = 4040029973355439229L;
/*      */     private JXLoginPane panel;
/*      */     
/*      */     public CancelAction(JXLoginPane p)
/*      */     {
/* 1409 */       super("cancel-login");
/* 1410 */       this.panel = p;
/* 1411 */       setEnabled(false);
/*      */     }
/*      */     
/* 1414 */     public void actionPerformed(ActionEvent e) { this.panel.cancelLogin(); }
/*      */     
/*      */ 
/*      */     public void itemStateChanged(ItemEvent e) {}
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class NullLoginService
/*      */     extends LoginService
/*      */   {
/*      */     public boolean authenticate(String name, char[] password, String server)
/*      */       throws Exception
/*      */     {
/* 1427 */       return true;
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/* 1432 */       return obj instanceof NullLoginService;
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/* 1437 */       return 7;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class NullPasswordStore
/*      */     extends PasswordStore
/*      */   {
/*      */     public boolean set(String username, String server, char[] password)
/*      */     {
/* 1448 */       return false;
/*      */     }
/*      */     
/*      */     public char[] get(String username, String server)
/*      */     {
/* 1453 */       return new char[0];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void removeUserPassword(String username) {}
/*      */     
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/* 1463 */       return obj instanceof NullPasswordStore;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1468 */     public int hashCode() { return 7; } }
/*      */   
/*      */   private static abstract interface NameComponent { public abstract String getUserName();
/*      */     
/*      */     public abstract boolean isEnabled();
/*      */     
/*      */     public abstract boolean isEditable();
/*      */     
/*      */     public abstract void setEditable(boolean paramBoolean);
/*      */     
/*      */     public abstract void setEnabled(boolean paramBoolean);
/*      */     
/*      */     public abstract void setUserName(String paramString);
/*      */     
/*      */     public abstract JComponent getComponent(); }
/*      */   
/* 1484 */   private void updatePassword(String username) { String password = "";
/* 1485 */     if (username != null) {
/* 1486 */       char[] pw = this.passwordStore.get(username, null);
/* 1487 */       password = pw == null ? "" : new String(pw);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1492 */       this.saveCB.setSelected(this.userNameStore.containsUserName(username));
/*      */     }
/*      */     
/* 1495 */     this.passwordField.setText(password);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final class SimpleNamePanel
/*      */     extends JTextField
/*      */     implements JXLoginPane.NameComponent
/*      */   {
/*      */     public SimpleNamePanel()
/*      */     {
/* 1506 */       super(15);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1513 */       if ((JXLoginPane.this.passwordStore != null) && (JXLoginPane.this.passwordField != null)) {
/* 1514 */         addKeyListener(new KeyAdapter()
/*      */         {
/*      */           public void keyReleased(KeyEvent e) {
/* 1517 */             JXLoginPane.this.updatePassword(JXLoginPane.SimpleNamePanel.this.getText());
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */     
/*      */     public String getUserName() {
/* 1524 */       return getText();
/*      */     }
/*      */     
/* 1527 */     public void setUserName(String userName) { setText(userName); }
/*      */     
/*      */     public JComponent getComponent() {
/* 1530 */       return this;
/*      */     }
/*      */     
/*      */     private static final long serialVersionUID = 6513437813612641002L;
/*      */   }
/*      */   
/*      */   private final class ComboNamePanel
/*      */     extends JComboBox implements JXLoginPane.NameComponent
/*      */   {
/*      */     private static final long serialVersionUID = 2511649075486103959L;
/*      */     
/*      */     public ComboNamePanel()
/*      */     {
/* 1543 */       setModel(new NameComboBoxModel(null));
/* 1544 */       setEditable(true);
/*      */       
/*      */ 
/* 1547 */       AutoCompleteDecorator.decorate(this);
/*      */       
/*      */       final JTextField textfield;
/*      */       
/* 1551 */       if ((JXLoginPane.this.passwordStore != null) && (JXLoginPane.this.passwordField != null)) {
/* 1552 */         textfield = (JTextField)getEditor().getEditorComponent();
/* 1553 */         textfield.addKeyListener(new KeyAdapter()
/*      */         {
/*      */           public void keyReleased(KeyEvent e) {
/* 1556 */             JXLoginPane.this.updatePassword(textfield.getText());
/*      */           }
/*      */           
/* 1559 */         });
/* 1560 */         super.addItemListener(new ItemListener() {
/*      */           public void itemStateChanged(ItemEvent e) {
/* 1562 */             JXLoginPane.this.updatePassword((String)JXLoginPane.ComboNamePanel.this.getSelectedItem());
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */     
/*      */     public String getUserName() {
/* 1569 */       Object item = getModel().getSelectedItem();
/* 1570 */       return item == null ? null : item.toString();
/*      */     }
/*      */     
/* 1573 */     public void setUserName(String userName) { getModel().setSelectedItem(userName); }
/*      */     
/*      */     public void setUserNames(String[] names) {
/* 1576 */       setModel(new DefaultComboBoxModel(names));
/*      */     }
/*      */     
/* 1579 */     public JComponent getComponent() { return this; }
/*      */     
/*      */     private final class NameComboBoxModel extends AbstractListModel implements ComboBoxModel { private static final long serialVersionUID = 7097674687536018633L;
/*      */       private Object selectedItem;
/*      */       
/*      */       private NameComboBoxModel() {}
/*      */       
/* 1586 */       public void setSelectedItem(Object anItem) { this.selectedItem = anItem;
/* 1587 */         fireContentsChanged(this, -1, -1);
/*      */       }
/*      */       
/* 1590 */       public Object getSelectedItem() { return this.selectedItem; }
/*      */       
/*      */       public Object getElementAt(int index) {
/* 1593 */         return JXLoginPane.this.userNameStore.getUserNames()[index];
/*      */       }
/*      */       
/* 1596 */       public int getSize() { return JXLoginPane.this.userNameStore.getUserNames().length; }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Status showLoginDialog(Component parent, LoginService svc)
/*      */   {
/* 1607 */     return showLoginDialog(parent, svc, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Status showLoginDialog(Component parent, LoginService svc, PasswordStore ps, UserNameStore us)
/*      */   {
/* 1615 */     return showLoginDialog(parent, svc, ps, us, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Status showLoginDialog(Component parent, LoginService svc, PasswordStore ps, UserNameStore us, List<String> servers)
/*      */   {
/* 1623 */     JXLoginPane panel = new JXLoginPane(svc, ps, us, servers);
/* 1624 */     return showLoginDialog(parent, panel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Status showLoginDialog(Component parent, JXLoginPane panel)
/*      */   {
/* 1632 */     Window w = WindowUtils.findWindow(parent);
/* 1633 */     JXLoginDialog dlg = null;
/* 1634 */     if (w == null) {
/* 1635 */       dlg = new JXLoginDialog((Frame)null, panel);
/* 1636 */     } else if ((w instanceof Dialog)) {
/* 1637 */       dlg = new JXLoginDialog((Dialog)w, panel);
/* 1638 */     } else if ((w instanceof Frame)) {
/* 1639 */       dlg = new JXLoginDialog((Frame)w, panel);
/*      */     } else {
/* 1641 */       throw new AssertionError("Shouldn't be able to happen");
/*      */     }
/* 1643 */     dlg.setVisible(true);
/* 1644 */     return dlg.getStatus();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static JXLoginFrame showLoginFrame(LoginService svc)
/*      */   {
/* 1651 */     return showLoginFrame(svc, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */   public static JXLoginFrame showLoginFrame(LoginService svc, PasswordStore ps, UserNameStore us)
/*      */   {
/* 1657 */     return showLoginFrame(svc, ps, us, null);
/*      */   }
/*      */   
/*      */ 
/*      */   public static JXLoginFrame showLoginFrame(LoginService svc, PasswordStore ps, UserNameStore us, List<String> servers)
/*      */   {
/* 1663 */     JXLoginPane panel = new JXLoginPane(svc, ps, us, servers);
/* 1664 */     return showLoginFrame(panel);
/*      */   }
/*      */   
/*      */ 
/*      */   public static JXLoginFrame showLoginFrame(JXLoginPane panel)
/*      */   {
/* 1670 */     return new JXLoginFrame(panel);
/*      */   }
/*      */   
/*      */   public static final class JXLoginDialog extends JDialog {
/*      */     private static final long serialVersionUID = -3185639594267828103L;
/*      */     private JXLoginPane panel;
/*      */     
/*      */     public JXLoginDialog(Frame parent, JXLoginPane p) {
/* 1678 */       super(true);
/* 1679 */       init(p);
/*      */     }
/*      */     
/*      */     public JXLoginDialog(Dialog parent, JXLoginPane p) {
/* 1683 */       super(true);
/* 1684 */       init(p);
/*      */     }
/*      */     
/*      */     protected void init(JXLoginPane p) {
/* 1688 */       setTitle(UIManagerExt.getString(JXLoginPane.CLASS_NAME + ".titleString", getLocale()));
/* 1689 */       this.panel = p;
/* 1690 */       JXLoginPane.initWindow(this, this.panel);
/*      */     }
/*      */     
/*      */     public JXLoginPane.Status getStatus() {
/* 1694 */       return this.panel.getStatus();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class JXLoginFrame extends JXFrame {
/*      */     private static final long serialVersionUID = -9016407314342050807L;
/*      */     private JXLoginPane panel;
/*      */     
/*      */     public JXLoginFrame(JXLoginPane p) {
/* 1703 */       super();
/* 1704 */       JXPanel cp = new JXPanel();
/* 1705 */       cp.setOpaque(true);
/* 1706 */       setContentPane(cp);
/* 1707 */       this.panel = p;
/* 1708 */       JXLoginPane.initWindow(this, this.panel);
/*      */     }
/*      */     
/*      */     public JXPanel getContentPane()
/*      */     {
/* 1713 */       return (JXPanel)super.getContentPane();
/*      */     }
/*      */     
/*      */     public JXLoginPane.Status getStatus() {
/* 1717 */       return this.panel.getStatus();
/*      */     }
/*      */     
/*      */     public JXLoginPane getPanel() {
/* 1721 */       return this.panel;
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
/*      */   static void initWindow(final Window w, final JXLoginPane panel)
/*      */   {
/* 1735 */     w.setLayout(new BorderLayout());
/* 1736 */     w.add(panel, "Center");
/* 1737 */     JButton okButton = new JButton(panel.getActionMap().get("login"));
/* 1738 */     JButton cancelButton = new JButton(UIManagerExt.getString(CLASS_NAME + ".cancelString", panel.getLocale()));
/*      */     
/* 1740 */     cancelButton.addActionListener(new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/* 1743 */         this.val$panel.status = JXLoginPane.Status.CANCELLED;
/* 1744 */         w.setVisible(false);
/* 1745 */         w.dispose();
/*      */       }
/* 1747 */     });
/* 1748 */     panel.addPropertyChangeListener("status", new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent evt) {
/* 1750 */         JXLoginPane.Status status = (JXLoginPane.Status)evt.getNewValue();
/* 1751 */         switch (JXLoginPane.7.$SwitchMap$org$jdesktop$swingx$JXLoginPane$Status[status.ordinal()]) {
/*      */         case 1: 
/*      */           break;
/*      */         case 2: 
/* 1755 */           this.val$cancelButton.setEnabled(false);
/* 1756 */           break;
/*      */         case 3: 
/* 1758 */           this.val$cancelButton.setEnabled(true);
/* 1759 */           w.pack();
/* 1760 */           break;
/*      */         case 4: 
/* 1762 */           this.val$cancelButton.setEnabled(true);
/* 1763 */           panel.passwordField.requestFocusInWindow();
/* 1764 */           w.pack();
/* 1765 */           break;
/*      */         case 5: 
/* 1767 */           w.setVisible(false);
/* 1768 */           w.dispose();
/*      */         }
/* 1770 */         for (PropertyChangeListener l : w.getPropertyChangeListeners("status")) {
/* 1771 */           PropertyChangeEvent pce = new PropertyChangeEvent(w, "status", evt.getOldValue(), evt.getNewValue());
/* 1772 */           l.propertyChange(pce);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/* 1778 */     });
/* 1779 */     JXBtnPanel buttonPanel = new JXBtnPanel(okButton, cancelButton);
/* 1780 */     buttonPanel.setOpaque(false);
/* 1781 */     panel.setButtonPanel(buttonPanel);
/* 1782 */     JXPanel controls = new JXPanel(new FlowLayout(2));
/* 1783 */     controls.setOpaque(false);
/* 1784 */     new BoxLayout(controls, 0);
/* 1785 */     controls.add(Box.createHorizontalGlue());
/* 1786 */     controls.add(buttonPanel);
/* 1787 */     w.add(controls, "South");
/* 1788 */     w.addWindowListener(new WindowAdapter()
/*      */     {
/*      */       public void windowClosing(WindowEvent e) {
/* 1791 */         this.val$panel.cancelLogin();
/*      */       }
/*      */     });
/*      */     
/* 1795 */     if ((w instanceof JFrame)) {
/* 1796 */       JFrame f = (JFrame)w;
/* 1797 */       f.getRootPane().setDefaultButton(okButton);
/* 1798 */       f.setResizable(false);
/* 1799 */       f.setDefaultCloseOperation(2);
/* 1800 */       KeyStroke ks = KeyStroke.getKeyStroke(27, 0);
/* 1801 */       ActionListener closeAction = new ActionListener() {
/*      */         public void actionPerformed(ActionEvent e) {
/* 1803 */           this.val$f.setVisible(false);
/* 1804 */           this.val$f.dispose();
/*      */         }
/* 1806 */       };
/* 1807 */       f.getRootPane().registerKeyboardAction(closeAction, ks, 2);
/* 1808 */     } else if ((w instanceof JDialog)) {
/* 1809 */       JDialog d = (JDialog)w;
/* 1810 */       d.getRootPane().setDefaultButton(okButton);
/* 1811 */       d.setResizable(false);
/* 1812 */       KeyStroke ks = KeyStroke.getKeyStroke(27, 0);
/* 1813 */       ActionListener closeAction = new ActionListener() {
/*      */         public void actionPerformed(ActionEvent e) {
/* 1815 */           this.val$d.setVisible(false);
/*      */         }
/* 1817 */       };
/* 1818 */       d.getRootPane().registerKeyboardAction(closeAction, ks, 2);
/*      */     }
/* 1820 */     w.pack();
/* 1821 */     w.setLocation(WindowUtils.getPointForCentering(w));
/*      */   }
/*      */   
/*      */   private void setButtonPanel(JXBtnPanel buttonPanel) {
/* 1825 */     this.buttonPanel = buttonPanel;
/*      */   }
/*      */   
/*      */   private static class JXBtnPanel extends JXPanel {
/*      */     private static final long serialVersionUID = 4136611099721189372L;
/*      */     private JButton cancel;
/*      */     private JButton ok;
/*      */     
/*      */     public JXBtnPanel(JButton okButton, JButton cancelButton) {
/* 1834 */       GridLayout layout = new GridLayout(1, 2);
/* 1835 */       layout.setHgap(5);
/* 1836 */       setLayout(layout);
/* 1837 */       this.ok = okButton;
/* 1838 */       this.cancel = cancelButton;
/* 1839 */       add(okButton);
/* 1840 */       add(cancelButton);
/* 1841 */       setBorder(new EmptyBorder(0, 0, 7, 11));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public JButton getCancel()
/*      */     {
/* 1848 */       return this.cancel;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public JButton getOk()
/*      */     {
/* 1855 */       return this.ok;
/*      */     }
/*      */   }
/*      */   
/*      */   private final class CapsOnTest {
/*      */     JXLoginPane.RemovableKeyEventDispatcher ked;
/*      */     
/*      */     private CapsOnTest() {}
/*      */     
/*      */     public void runTest() {
/* 1865 */       boolean success = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1874 */       if (!success) {
/*      */         try {
/* 1876 */           KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */           
/*      */ 
/* 1879 */           if (this.ked != null) {
/* 1880 */             kfm.removeKeyEventDispatcher(this.ked);
/*      */           }
/*      */           
/*      */ 
/* 1884 */           this.ked = new JXLoginPane.RemovableKeyEventDispatcher(JXLoginPane.this, this);
/* 1885 */           kfm.addKeyEventDispatcher(this.ked);
/* 1886 */           Robot r = new Robot();
/* 1887 */           JXLoginPane.this.isTestingCaps = true;
/* 1888 */           r.keyPress(65);
/* 1889 */           r.keyRelease(65);
/* 1890 */           r.keyPress(8);
/* 1891 */           r.keyRelease(8);
/*      */ 
/*      */         }
/*      */         catch (Exception e1)
/*      */         {
/*      */ 
/* 1897 */           this.ked.uninstall();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void clean() {
/* 1903 */       if (this.ked != null) {
/* 1904 */         this.ked.cleanOnBogusFocus();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private final class CapsOnWinListener
/*      */     extends WindowAdapter
/*      */     implements WindowFocusListener, WindowListener
/*      */   {
/*      */     private JXLoginPane.CapsOnTest cot;
/*      */     
/*      */     private long stamp;
/*      */     
/*      */     public CapsOnWinListener(JXLoginPane.CapsOnTest cot)
/*      */     {
/* 1920 */       this.cot = cot;
/*      */     }
/*      */     
/*      */     public void windowActivated(WindowEvent e)
/*      */     {
/* 1925 */       this.cot.runTest();
/* 1926 */       this.stamp = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void windowGainedFocus(WindowEvent e)
/*      */     {
/* 1933 */       if (this.stamp + 20L < System.currentTimeMillis()) {
/* 1934 */         this.cot.runTest();
/*      */       }
/*      */     }
/*      */     
/*      */     public void windowOpened(WindowEvent arg0)
/*      */     {
/* 1940 */       this.cot.clean();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class RemovableKeyEventDispatcher
/*      */     implements KeyEventDispatcher
/*      */   {
/*      */     private JXLoginPane.CapsOnTest cot;
/* 1949 */     private boolean tested = false;
/*      */     
/* 1951 */     private int retry = 0;
/*      */     
/*      */     public RemovableKeyEventDispatcher(JXLoginPane.CapsOnTest capsOnTest) {
/* 1954 */       this.cot = capsOnTest;
/*      */     }
/*      */     
/*      */     public boolean dispatchKeyEvent(KeyEvent e) {
/* 1958 */       this.tested = true;
/* 1959 */       if (e.getID() != 401) {
/* 1960 */         return true;
/*      */       }
/* 1962 */       if ((JXLoginPane.this.isTestingCaps) && (e.getKeyCode() > 64) && (e.getKeyCode() < 91)) {
/* 1963 */         JXLoginPane.this.setCapsLock((!e.isShiftDown()) && (Character.isUpperCase(e.getKeyChar())));
/*      */       }
/*      */       
/* 1966 */       if ((JXLoginPane.this.isTestingCaps) && (e.getKeyCode() == 8))
/*      */       {
/* 1968 */         uninstall();
/* 1969 */         this.retry = 0;
/*      */       }
/* 1971 */       return true;
/*      */     }
/*      */     
/*      */     void uninstall() {
/* 1975 */       JXLoginPane.this.isTestingCaps = false;
/* 1976 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
/*      */       
/* 1978 */       if (this.cot.ked == this) {
/* 1979 */         this.cot.ked = null;
/*      */       }
/*      */     }
/*      */     
/*      */     void cleanOnBogusFocus()
/*      */     {
/* 1985 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/* 1987 */           if (!JXLoginPane.RemovableKeyEventDispatcher.this.tested) {
/* 1988 */             JXLoginPane.RemovableKeyEventDispatcher.this.uninstall();
/* 1989 */             if (JXLoginPane.RemovableKeyEventDispatcher.this.retry < 3)
/*      */             {
/* 1991 */               Window w = JXLoginPane.this.getTLA();
/* 1992 */               if (w != null) {
/* 1993 */                 w.toFront();
/*      */               }
/* 1995 */               JXLoginPane.RemovableKeyEventDispatcher.this.cot.runTest();
/* 1996 */               JXLoginPane.RemovableKeyEventDispatcher.access$1808(JXLoginPane.RemovableKeyEventDispatcher.this);
/*      */             }
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXLoginPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */