/*      */ package com.birosoft.liquid;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Locale;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.AncestorEvent;
/*      */ import javax.swing.event.AncestorListener;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ 
/*      */ public class LiquidOptionPaneUI extends javax.swing.plaf.OptionPaneUI
/*      */ {
/*      */   public static final int MinimumWidth = 262;
/*      */   public static final int MinimumHeight = 90;
/*      */   private static String newline;
/*      */   private int[] mnemonics;
/*      */   protected JOptionPane optionPane;
/*      */   protected Dimension minimumSize;
/*      */   protected JComponent inputComponent;
/*      */   protected Component initialFocusComponent;
/*      */   protected boolean hasCustomComponents;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   
/*      */   static
/*      */   {
/*   64 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/*   66 */         LiquidOptionPaneUI.access$002(System.getProperty("line.separator"));
/*      */         
/*   68 */         if (LiquidOptionPaneUI.newline == null) {
/*   69 */           LiquidOptionPaneUI.access$002("\n");
/*      */         }
/*      */         
/*   72 */         return null;
/*      */       }
/*      */     });
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
/*      */   public static ComponentUI createUI(JComponent x)
/*      */   {
/*  105 */     return new LiquidOptionPaneUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void installUI(JComponent c)
/*      */   {
/*  113 */     this.optionPane = ((JOptionPane)c);
/*  114 */     installDefaults();
/*  115 */     this.optionPane.setLayout(createLayoutManager());
/*  116 */     installComponents();
/*  117 */     installListeners();
/*  118 */     installKeyboardActions();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void uninstallUI(JComponent c)
/*      */   {
/*  126 */     uninstallComponents();
/*  127 */     this.optionPane.setLayout(null);
/*  128 */     uninstallKeyboardActions();
/*  129 */     uninstallListeners();
/*  130 */     uninstallDefaults();
/*  131 */     this.optionPane = null;
/*      */   }
/*      */   
/*      */   protected void installDefaults() {
/*  135 */     LookAndFeel.installColorsAndFont(this.optionPane, "OptionPane.background", "OptionPane.foreground", "OptionPane.font");
/*      */     
/*  137 */     LookAndFeel.installBorder(this.optionPane, "OptionPane.border");
/*  138 */     this.minimumSize = UIManager.getDimension("OptionPane.minimumSize");
/*      */     
/*  140 */     if (LiquidLookAndFeel.areStipplesUsed()) {
/*  141 */       this.optionPane.setOpaque(false);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void uninstallDefaults() {
/*  146 */     LookAndFeel.uninstallBorder(this.optionPane);
/*      */   }
/*      */   
/*      */   protected void installComponents() {
/*  150 */     this.optionPane.add(createMessageArea());
/*      */     
/*  152 */     Container separator = createSeparator();
/*      */     
/*  154 */     if (separator != null) {
/*  155 */       this.optionPane.add(separator);
/*      */     }
/*      */     
/*  158 */     this.optionPane.add(createButtonArea());
/*  159 */     this.optionPane.applyComponentOrientation(this.optionPane.getComponentOrientation());
/*      */   }
/*      */   
/*      */   protected void uninstallComponents() {
/*  163 */     this.hasCustomComponents = false;
/*  164 */     this.inputComponent = null;
/*  165 */     this.initialFocusComponent = null;
/*  166 */     this.optionPane.removeAll();
/*      */   }
/*      */   
/*      */   protected LayoutManager createLayoutManager() {
/*  170 */     return new BoxLayout(this.optionPane, 1);
/*      */   }
/*      */   
/*      */   protected void installListeners() {
/*  174 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null) {
/*  175 */       this.optionPane.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void uninstallListeners() {
/*  180 */     if (this.propertyChangeListener != null) {
/*  181 */       this.optionPane.removePropertyChangeListener(this.propertyChangeListener);
/*  182 */       this.propertyChangeListener = null;
/*      */     }
/*      */   }
/*      */   
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/*  187 */     return new PropertyChangeHandler();
/*      */   }
/*      */   
/*      */   protected void installKeyboardActions() {
/*  191 */     InputMap map = getInputMap(2);
/*      */     
/*  193 */     SwingUtilities.replaceUIInputMap(this.optionPane, 2, map);
/*      */     
/*      */ 
/*  196 */     ActionMap actionMap = getActionMap();
/*      */     
/*  198 */     SwingUtilities.replaceUIActionMap(this.optionPane, actionMap);
/*      */   }
/*      */   
/*      */   protected void uninstallKeyboardActions() {
/*  202 */     SwingUtilities.replaceUIInputMap(this.optionPane, 2, null);
/*      */     
/*  204 */     SwingUtilities.replaceUIActionMap(this.optionPane, null);
/*      */   }
/*      */   
/*      */   InputMap getInputMap(int condition) {
/*  208 */     if (condition == 2) {
/*  209 */       Object[] bindings = (Object[])UIManager.get("OptionPane.windowBindings");
/*      */       
/*      */ 
/*  212 */       if (bindings != null) {
/*  213 */         return LookAndFeel.makeComponentInputMap(this.optionPane, bindings);
/*      */       }
/*      */     }
/*      */     
/*  217 */     return null;
/*      */   }
/*      */   
/*      */   ActionMap getActionMap() {
/*  221 */     ActionMap map = (ActionMap)UIManager.get("OptionPane.actionMap");
/*      */     
/*  223 */     if (map == null) {
/*  224 */       map = createActionMap();
/*      */       
/*  226 */       if (map != null) {
/*  227 */         UIManager.getLookAndFeelDefaults().put("OptionPane.actionMap", map);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  232 */     return map;
/*      */   }
/*      */   
/*      */   ActionMap createActionMap() {
/*  236 */     ActionMap map = new ActionMapUIResource();
/*  237 */     map.put("close", new CloseAction(null));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  243 */     return map;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getMinimumOptionPaneSize()
/*      */   {
/*  251 */     if (this.minimumSize == null)
/*      */     {
/*      */ 
/*  254 */       return new Dimension(262, 90);
/*      */     }
/*      */     
/*  257 */     return new Dimension(this.minimumSize.width, this.minimumSize.height);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent c)
/*      */   {
/*  268 */     if ((JOptionPane)c == this.optionPane) {
/*  269 */       Dimension ourMin = getMinimumOptionPaneSize();
/*  270 */       LayoutManager lm = c.getLayout();
/*      */       
/*  272 */       if (lm != null) {
/*  273 */         Dimension lmSize = lm.preferredLayoutSize(c);
/*      */         
/*  275 */         if (ourMin != null) {
/*  276 */           return new Dimension(Math.max(lmSize.width, ourMin.width), Math.max(lmSize.height, ourMin.height));
/*      */         }
/*      */         
/*      */ 
/*  280 */         return lmSize;
/*      */       }
/*      */       
/*  283 */       return ourMin;
/*      */     }
/*      */     
/*  286 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent c)
/*      */   {
/*  293 */     return getPreferredSize(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent c)
/*      */   {
/*  300 */     return getPreferredSize(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Container createMessageArea()
/*      */   {
/*  309 */     JPanel top = new JPanel();
/*  310 */     top.setBorder(UIManager.getBorder("OptionPane.messageAreaBorder"));
/*  311 */     top.setLayout(new BorderLayout());
/*      */     
/*  313 */     if (LiquidLookAndFeel.areStipplesUsed()) {
/*  314 */       top.setOpaque(false);
/*      */     }
/*      */     
/*      */ 
/*  318 */     Container body = new JPanel() {};
/*  321 */     Container realBody = new JPanel() {};
/*  324 */     realBody.setLayout(new BorderLayout());
/*      */     
/*  326 */     if (LiquidLookAndFeel.areStipplesUsed()) {
/*  327 */       ((JPanel)realBody).setOpaque(false);
/*  328 */       ((JPanel)body).setOpaque(false);
/*      */     }
/*      */     
/*  331 */     if (getIcon() != null) {
/*  332 */       Container sep = new JPanel() {
/*      */         public Dimension getPreferredSize() {
/*  334 */           return new Dimension(15, 1);
/*      */         }
/*      */       };
/*      */       
/*  338 */       if (LiquidLookAndFeel.areStipplesUsed()) {
/*  339 */         ((JPanel)sep).setOpaque(false);
/*      */       }
/*      */       
/*  342 */       realBody.add(sep, "Before");
/*      */     }
/*      */     
/*  345 */     realBody.add(body, "Center");
/*      */     
/*  347 */     body.setLayout(new GridBagLayout());
/*      */     
/*  349 */     GridBagConstraints cons = new GridBagConstraints();
/*  350 */     cons.gridx = (cons.gridy = 0);
/*  351 */     cons.gridwidth = 0;
/*  352 */     cons.gridheight = 1;
/*  353 */     cons.anchor = 21;
/*  354 */     cons.insets = new Insets(0, 0, 3, 0);
/*      */     
/*  356 */     addMessageComponents(body, cons, getMessage(), getMaxCharactersPerLineCount(), false);
/*      */     
/*  358 */     top.add(realBody, "Center");
/*      */     
/*  360 */     addIcon(top);
/*      */     
/*  362 */     return top;
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
/*      */   protected void addMessageComponents(Container container, GridBagConstraints cons, Object msg, int maxll, boolean internallyCreated)
/*      */   {
/*  379 */     if (msg == null) {
/*  380 */       return;
/*      */     }
/*      */     
/*  383 */     if ((msg instanceof Component))
/*      */     {
/*      */ 
/*      */ 
/*  387 */       if (((msg instanceof JScrollPane)) || ((msg instanceof JPanel))) {
/*  388 */         cons.fill = 1;
/*  389 */         cons.weighty = 1.0D;
/*      */       } else {
/*  391 */         cons.fill = 2;
/*      */       }
/*      */       
/*  394 */       cons.weightx = 1.0D;
/*      */       
/*  396 */       container.add((Component)msg, cons);
/*  397 */       cons.weightx = 0.0D;
/*  398 */       cons.weighty = 0.0D;
/*  399 */       cons.fill = 0;
/*  400 */       cons.gridy += 1;
/*      */       
/*  402 */       if (!internallyCreated) {
/*  403 */         this.hasCustomComponents = true;
/*      */       }
/*  405 */     } else if ((msg instanceof Object[])) {
/*  406 */       Object[] msgs = (Object[])msg;
/*      */       
/*  408 */       for (int i = 0; i < msgs.length; i++) {
/*  409 */         addMessageComponents(container, cons, msgs[i], maxll, false);
/*      */       }
/*  411 */     } else if ((msg instanceof Icon)) {
/*  412 */       JLabel label = new JLabel((Icon)msg, 0);
/*  413 */       configureMessageLabel(label);
/*  414 */       addMessageComponents(container, cons, label, maxll, true);
/*      */     } else {
/*  416 */       String s = msg.toString();
/*  417 */       int len = s.length();
/*      */       
/*  419 */       if (len <= 0) {
/*  420 */         return;
/*      */       }
/*      */       
/*  423 */       int nl = -1;
/*  424 */       int nll = 0;
/*      */       
/*  426 */       if ((nl = s.indexOf(newline)) >= 0) {
/*  427 */         nll = newline.length();
/*  428 */       } else if ((nl = s.indexOf("\r\n")) >= 0) {
/*  429 */         nll = 2;
/*  430 */       } else if ((nl = s.indexOf('\n')) >= 0) {
/*  431 */         nll = 1;
/*      */       }
/*      */       
/*  434 */       if (nl >= 0)
/*      */       {
/*  436 */         if (nl == 0) {
/*  437 */           addMessageComponents(container, cons, new Component()
/*      */           {
/*      */             public Dimension getPreferredSize() {
/*  440 */               Font f = getFont();
/*      */               
/*  442 */               if (f != null) {
/*  443 */                 return new Dimension(1, f.getSize() + 2);
/*      */               }
/*      */               
/*  446 */               return new Dimension(0, 0); } }, maxll, true);
/*      */         }
/*      */         else
/*      */         {
/*  450 */           addMessageComponents(container, cons, s.substring(0, nl), maxll, false);
/*      */         }
/*      */         
/*      */ 
/*  454 */         addMessageComponents(container, cons, s.substring(nl + nll), maxll, false);
/*      */       }
/*  456 */       else if (len > maxll) {
/*  457 */         Container c = javax.swing.Box.createVerticalBox();
/*  458 */         burstStringInto(c, s, maxll);
/*  459 */         addMessageComponents(container, cons, c, maxll, true);
/*      */       }
/*      */       else {
/*  462 */         JLabel label = new JLabel(s, 10);
/*  463 */         configureMessageLabel(label);
/*  464 */         addMessageComponents(container, cons, label, maxll, true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object getMessage()
/*      */   {
/*  474 */     this.inputComponent = null;
/*      */     
/*  476 */     if (this.optionPane != null) {
/*  477 */       if (this.optionPane.getWantsInput())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  482 */         Object message = this.optionPane.getMessage();
/*  483 */         Object[] sValues = this.optionPane.getSelectionValues();
/*  484 */         Object inputValue = this.optionPane.getInitialSelectionValue();
/*      */         
/*      */         JComponent toAdd;
/*  487 */         if (sValues != null) { JComponent toAdd;
/*  488 */           if (sValues.length < 20) {
/*  489 */             JComboBox cBox = new JComboBox();
/*      */             
/*  491 */             int counter = 0;int maxCounter = sValues.length;
/*  492 */             for (; counter < maxCounter; counter++) {
/*  493 */               cBox.addItem(sValues[counter]);
/*      */             }
/*      */             
/*  496 */             if (inputValue != null) {
/*  497 */               cBox.setSelectedItem(inputValue);
/*      */             }
/*      */             
/*  500 */             this.inputComponent = cBox;
/*  501 */             toAdd = cBox;
/*      */           } else {
/*  503 */             JList list = new JList(sValues);
/*  504 */             JScrollPane sp = new JScrollPane(list);
/*      */             
/*  506 */             list.setVisibleRowCount(10);
/*  507 */             list.setSelectionMode(0);
/*      */             
/*  509 */             if (inputValue != null) {
/*  510 */               list.setSelectedValue(inputValue, true);
/*      */             }
/*      */             
/*  513 */             list.addMouseListener(new ListSelectionListener(null));
/*  514 */             JComponent toAdd = sp;
/*  515 */             this.inputComponent = list;
/*      */           }
/*      */         } else {
/*  518 */           MultiplexingTextField tf = new MultiplexingTextField(20);
/*      */           
/*  520 */           tf.setKeyStrokes(new KeyStroke[] { KeyStroke.getKeyStroke("ENTER") });
/*      */           
/*      */ 
/*      */ 
/*  524 */           if (inputValue != null) {
/*  525 */             String inputString = inputValue.toString();
/*  526 */             tf.setText(inputString);
/*  527 */             tf.setSelectionStart(0);
/*  528 */             tf.setSelectionEnd(inputString.length());
/*      */           }
/*      */           
/*  531 */           tf.addActionListener(new TextFieldActionListener(null));
/*  532 */           toAdd = this.inputComponent = tf;
/*      */         }
/*      */         
/*      */         Object[] newMessage;
/*      */         
/*  537 */         if (message == null) {
/*  538 */           Object[] newMessage = new Object[1];
/*  539 */           newMessage[0] = toAdd;
/*      */         } else {
/*  541 */           newMessage = new Object[2];
/*  542 */           newMessage[0] = message;
/*  543 */           newMessage[1] = toAdd;
/*      */         }
/*      */         
/*  546 */         return newMessage;
/*      */       }
/*      */       
/*  549 */       return this.optionPane.getMessage();
/*      */     }
/*      */     
/*  552 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addIcon(Container top)
/*      */   {
/*  562 */     Icon sideIcon = getIcon();
/*      */     
/*  564 */     if (sideIcon != null) {
/*  565 */       JLabel iconLabel = new JLabel(sideIcon);
/*      */       
/*  567 */       iconLabel.setVerticalAlignment(1);
/*  568 */       top.add(iconLabel, "Before");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Icon getIcon()
/*      */   {
/*  578 */     Icon mIcon = this.optionPane == null ? null : this.optionPane.getIcon();
/*      */     
/*  580 */     if ((mIcon == null) && (this.optionPane != null)) {
/*  581 */       mIcon = getIconForType(this.optionPane.getMessageType());
/*      */     }
/*      */     
/*  584 */     return mIcon;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Icon getIconForType(int messageType)
/*      */   {
/*  591 */     if ((messageType < 0) || (messageType > 3)) {
/*  592 */       return null;
/*      */     }
/*      */     
/*  595 */     switch (messageType) {
/*      */     case 0: 
/*  597 */       return UIManager.getIcon("OptionPane.errorIcon");
/*      */     
/*      */     case 1: 
/*  600 */       return UIManager.getIcon("OptionPane.informationIcon");
/*      */     
/*      */     case 2: 
/*  603 */       return UIManager.getIcon("OptionPane.warningIcon");
/*      */     
/*      */     case 3: 
/*  606 */       return UIManager.getIcon("OptionPane.questionIcon");
/*      */     }
/*      */     
/*  609 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int getMaxCharactersPerLineCount()
/*      */   {
/*  616 */     return this.optionPane.getMaxCharactersPerLineCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void burstStringInto(Container c, String d, int maxll)
/*      */   {
/*  625 */     int len = d.length();
/*      */     
/*  627 */     if (len <= 0) {
/*  628 */       return;
/*      */     }
/*      */     
/*  631 */     if (len > maxll) {
/*  632 */       int p = d.lastIndexOf(' ', maxll);
/*      */       
/*  634 */       if (p <= 0) {
/*  635 */         p = d.indexOf(' ', maxll);
/*      */       }
/*      */       
/*  638 */       if ((p > 0) && (p < len)) {
/*  639 */         burstStringInto(c, d.substring(0, p), maxll);
/*  640 */         burstStringInto(c, d.substring(p + 1), maxll);
/*      */         
/*  642 */         return;
/*      */       }
/*      */     }
/*      */     
/*  646 */     JLabel label = new JLabel(d, 2);
/*  647 */     configureMessageLabel(label);
/*  648 */     c.add(label);
/*      */   }
/*      */   
/*      */   protected Container createSeparator() {
/*  652 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Container createButtonArea()
/*      */   {
/*  660 */     JPanel bottom = new JPanel();
/*      */     
/*  662 */     if (LiquidLookAndFeel.areStipplesUsed()) {
/*  663 */       bottom.setOpaque(false);
/*      */     }
/*      */     
/*  666 */     bottom.setBorder(UIManager.getBorder("OptionPane.buttonAreaBorder"));
/*  667 */     bottom.setLayout(new ButtonAreaLayout(true, 6));
/*  668 */     addButtonComponents(bottom, getButtons(), getInitialValueIndex());
/*  669 */     this.mnemonics = null;
/*      */     
/*  671 */     return bottom;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addButtonComponents(Container container, Object[] buttons, int initialIndex)
/*      */   {
/*  683 */     if ((buttons != null) && (buttons.length > 0)) {
/*  684 */       boolean sizeButtonsToSame = getSizeButtonsToSameWidth();
/*  685 */       boolean createdAll = true;
/*  686 */       int numButtons = buttons.length;
/*  687 */       JButton[] createdButtons = null;
/*  688 */       int maxWidth = 0;
/*  689 */       int[] mnemonics = this.mnemonics;
/*      */       
/*  691 */       if ((mnemonics != null) && (mnemonics.length != buttons.length)) {
/*  692 */         mnemonics = null;
/*      */       }
/*      */       
/*  695 */       if (sizeButtonsToSame) {
/*  696 */         createdButtons = new JButton[numButtons];
/*      */       }
/*      */       
/*  699 */       for (int counter = 0; counter < numButtons; counter++) {
/*  700 */         Object button = buttons[counter];
/*      */         
/*      */         Component newComponent;
/*  703 */         if ((button instanceof Component)) {
/*  704 */           createdAll = false;
/*  705 */           Component newComponent = (Component)button;
/*  706 */           container.add(newComponent);
/*  707 */           this.hasCustomComponents = true;
/*      */         } else {
/*      */           JButton aButton;
/*      */           JButton aButton;
/*  711 */           if ((button instanceof Icon)) {
/*  712 */             aButton = new JButton((Icon)button);
/*      */           } else {
/*  714 */             aButton = new JButton(button.toString());
/*      */           }
/*      */           
/*  717 */           aButton.setMultiClickThreshhold(UIManager.getInt("OptionPane.buttonClickThreshhold"));
/*      */           
/*  719 */           configureButton(aButton);
/*      */           
/*  721 */           container.add(aButton);
/*      */           
/*  723 */           ActionListener buttonListener = createButtonActionListener(counter);
/*      */           
/*  725 */           if (buttonListener != null) {
/*  726 */             aButton.addActionListener(buttonListener);
/*      */           }
/*      */           
/*  729 */           newComponent = aButton;
/*      */           
/*  731 */           if (mnemonics != null) {
/*  732 */             aButton.setMnemonic(mnemonics[counter]);
/*      */           }
/*      */         }
/*      */         
/*  736 */         if ((sizeButtonsToSame) && (createdAll) && ((newComponent instanceof JButton)))
/*      */         {
/*  738 */           createdButtons[counter] = ((JButton)newComponent);
/*  739 */           maxWidth = Math.max(maxWidth, newComponent.getMinimumSize().width);
/*      */         }
/*      */         
/*      */ 
/*  743 */         if (counter == initialIndex) {
/*  744 */           this.initialFocusComponent = newComponent;
/*      */           
/*  746 */           if ((this.initialFocusComponent instanceof JButton)) {
/*  747 */             JButton defaultB = (JButton)this.initialFocusComponent;
/*  748 */             defaultB.addAncestorListener(new AncestorListener() {
/*      */               public void ancestorAdded(AncestorEvent e) {
/*  750 */                 JButton defaultButton = (JButton)e.getComponent();
/*  751 */                 JRootPane root = SwingUtilities.getRootPane(defaultButton);
/*      */                 
/*  753 */                 if (root != null) {
/*  754 */                   root.setDefaultButton(defaultButton);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */               public void ancestorRemoved(AncestorEvent event) {}
/*      */               
/*      */ 
/*      */               public void ancestorMoved(AncestorEvent event) {}
/*      */             });
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  768 */       ((ButtonAreaLayout)container.getLayout()).setSyncAllWidths((sizeButtonsToSame) && (createdAll));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  774 */       if ((sizeButtonsToSame) && (createdAll))
/*      */       {
/*      */ 
/*      */ 
/*  778 */         int padSize = numButtons <= 2 ? 8 : 4;
/*      */         
/*  780 */         for (int counter = 0; counter < numButtons; counter++) {
/*  781 */           JButton aButton = createdButtons[counter];
/*  782 */           aButton.setMargin(new Insets(2, padSize, 2, padSize));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected ActionListener createButtonActionListener(int buttonIndex) {
/*  789 */     return new ButtonActionListener(buttonIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object[] getButtons()
/*      */   {
/*  801 */     if (this.optionPane != null) {
/*  802 */       Object[] suppliedOptions = this.optionPane.getOptions();
/*      */       
/*  804 */       if (suppliedOptions == null)
/*      */       {
/*  806 */         int type = this.optionPane.getOptionType();
/*      */         
/*  808 */         Locale l = this.optionPane.getLocale();
/*      */         Object[] defaultOptions;
/*  810 */         if (type == 0) {
/*  811 */           Object[] defaultOptions = new String[2];
/*  812 */           defaultOptions[0] = UIManager.get("OptionPane.yesButtonText", l);
/*      */           
/*  814 */           defaultOptions[1] = UIManager.get("OptionPane.noButtonText", l);
/*      */           
/*  816 */           this.mnemonics = new int[2];
/*  817 */           this.mnemonics[0] = getMnemonic("OptionPane.yesButtonMnemonic", l);
/*  818 */           this.mnemonics[1] = getMnemonic("OptionPane.noButtonMnemonic", l);
/*  819 */         } else if (type == 1) {
/*  820 */           Object[] defaultOptions = new String[3];
/*  821 */           defaultOptions[0] = UIManager.get("OptionPane.yesButtonText", l);
/*      */           
/*  823 */           defaultOptions[1] = UIManager.get("OptionPane.noButtonText", l);
/*      */           
/*  825 */           defaultOptions[2] = UIManager.get("OptionPane.cancelButtonText", l);
/*      */           
/*  827 */           this.mnemonics = new int[3];
/*  828 */           this.mnemonics[0] = getMnemonic("OptionPane.yesButtonMnemonic", l);
/*  829 */           this.mnemonics[1] = getMnemonic("OptionPane.noButtonMnemonic", l);
/*  830 */           this.mnemonics[2] = getMnemonic("OptionPane.cancelButtonMnemonic", l);
/*      */         }
/*  832 */         else if (type == 2) {
/*  833 */           Object[] defaultOptions = new String[2];
/*  834 */           defaultOptions[0] = UIManager.get("OptionPane.okButtonText", l);
/*      */           
/*  836 */           defaultOptions[1] = UIManager.get("OptionPane.cancelButtonText", l);
/*      */           
/*  838 */           this.mnemonics = new int[2];
/*  839 */           this.mnemonics[0] = getMnemonic("OptionPane.okButtonMnemonic", l);
/*  840 */           this.mnemonics[1] = getMnemonic("OptionPane.cancelButtonMnemonic", l);
/*      */         }
/*      */         else {
/*  843 */           defaultOptions = new String[1];
/*  844 */           defaultOptions[0] = UIManager.get("OptionPane.okButtonText", l);
/*      */           
/*  846 */           this.mnemonics = new int[1];
/*  847 */           this.mnemonics[0] = getMnemonic("OptionPane.okButtonMnemonic", l);
/*      */         }
/*      */         
/*  850 */         return defaultOptions;
/*      */       }
/*      */       
/*  853 */       return suppliedOptions;
/*      */     }
/*      */     
/*  856 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int getMnemonic(String key, Locale l)
/*      */   {
/*  863 */     String value = (String)UIManager.get(key, l);
/*      */     
/*  865 */     if (value == null) {
/*  866 */       return 0;
/*      */     }
/*      */     try
/*      */     {
/*  870 */       return Integer.parseInt(value);
/*      */     }
/*      */     catch (NumberFormatException nfe) {}
/*      */     
/*  874 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean getSizeButtonsToSameWidth()
/*      */   {
/*  882 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getInitialValueIndex()
/*      */   {
/*  891 */     if (this.optionPane != null) {
/*  892 */       Object iv = this.optionPane.getInitialValue();
/*  893 */       Object[] options = this.optionPane.getOptions();
/*      */       
/*  895 */       if (options == null)
/*  896 */         return 0;
/*  897 */       if (iv != null) {
/*  898 */         for (int counter = options.length - 1; counter >= 0; 
/*  899 */             counter--) {
/*  900 */           if (options[counter].equals(iv)) {
/*  901 */             return counter;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  907 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void resetInputValue()
/*      */   {
/*  915 */     if ((this.inputComponent != null) && ((this.inputComponent instanceof JTextField))) {
/*  916 */       this.optionPane.setInputValue(((JTextField)this.inputComponent).getText());
/*  917 */     } else if ((this.inputComponent != null) && ((this.inputComponent instanceof JComboBox)))
/*      */     {
/*  919 */       this.optionPane.setInputValue(((JComboBox)this.inputComponent).getSelectedItem());
/*  920 */     } else if (this.inputComponent != null) {
/*  921 */       this.optionPane.setInputValue(((JList)this.inputComponent).getSelectedValue());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void selectInitialValue(JOptionPane op)
/*      */   {
/*  930 */     if (this.inputComponent != null) {
/*  931 */       this.inputComponent.requestFocus();
/*      */     } else {
/*  933 */       if (this.initialFocusComponent != null) {
/*  934 */         this.initialFocusComponent.requestFocus();
/*      */       }
/*      */       
/*  937 */       if ((this.initialFocusComponent instanceof JButton)) {
/*  938 */         JRootPane root = SwingUtilities.getRootPane(this.initialFocusComponent);
/*      */         
/*  940 */         if (root != null) {
/*  941 */           root.setDefaultButton((JButton)this.initialFocusComponent);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean containsCustomComponents(JOptionPane op)
/*      */   {
/*  952 */     return this.hasCustomComponents;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void fireAudioAction(String actionName)
/*      */   {
/*  962 */     ActionMap map = this.optionPane.getActionMap();
/*      */     
/*  964 */     if (map != null) {
/*  965 */       Action audioAction = map.get(actionName);
/*      */       
/*  967 */       if (audioAction == null) {}
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
/*      */   private void configureMessageLabel(JLabel label)
/*      */   {
/*  981 */     label.setForeground(UIManager.getColor("OptionPane.messageForeground"));
/*      */     
/*  983 */     Font messageFont = UIManager.getFont("OptionPane.messageFont");
/*      */     
/*  985 */     if (messageFont != null) {
/*  986 */       label.setFont(messageFont);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void configureButton(JButton button)
/*      */   {
/*  995 */     Font buttonFont = UIManager.getFont("OptionPane.buttonFont");
/*      */     
/*  997 */     if (buttonFont != null) {
/*  998 */       button.setFont(buttonFont);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class ButtonAreaLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     protected boolean syncAllWidths;
/*      */     
/*      */ 
/*      */     protected int padding;
/*      */     
/*      */ 
/*      */     protected boolean centersChildren;
/*      */     
/*      */ 
/*      */ 
/*      */     public ButtonAreaLayout(boolean syncAllWidths, int padding)
/*      */     {
/* 1020 */       this.syncAllWidths = syncAllWidths;
/* 1021 */       this.padding = padding;
/* 1022 */       this.centersChildren = true;
/*      */     }
/*      */     
/*      */     public void setSyncAllWidths(boolean newValue) {
/* 1026 */       this.syncAllWidths = newValue;
/*      */     }
/*      */     
/*      */     public boolean getSyncAllWidths() {
/* 1030 */       return this.syncAllWidths;
/*      */     }
/*      */     
/*      */     public void setPadding(int newPadding) {
/* 1034 */       this.padding = newPadding;
/*      */     }
/*      */     
/*      */     public int getPadding() {
/* 1038 */       return this.padding;
/*      */     }
/*      */     
/*      */     public void setCentersChildren(boolean newValue) {
/* 1042 */       this.centersChildren = newValue;
/*      */     }
/*      */     
/*      */     public boolean getCentersChildren() {
/* 1046 */       return this.centersChildren;
/*      */     }
/*      */     
/*      */     public void addLayoutComponent(String string, Component comp) {}
/*      */     
/*      */     public void layoutContainer(Container container)
/*      */     {
/* 1053 */       Component[] children = container.getComponents();
/*      */       
/* 1055 */       if ((children != null) && (children.length > 0)) {
/* 1056 */         int numChildren = children.length;
/* 1057 */         Dimension[] sizes = new Dimension[numChildren];
/* 1058 */         Insets insets = container.getInsets();
/*      */         
/* 1060 */         int yLocation = insets.top;
/* 1061 */         boolean ltr = container.getComponentOrientation().isLeftToRight();
/*      */         
/* 1063 */         if (this.syncAllWidths) {
/* 1064 */           int maxWidth = 0;
/*      */           
/* 1066 */           for (int counter = 0; counter < numChildren; counter++) {
/* 1067 */             sizes[counter] = children[counter].getPreferredSize();
/* 1068 */             maxWidth = Math.max(maxWidth, sizes[counter].width);
/*      */           }
/*      */           
/*      */           int xOffset;
/*      */           int xLocation;
/*      */           int xOffset;
/* 1074 */           if (getCentersChildren()) {
/* 1075 */             int xLocation = (container.getSize().width - insets.left - insets.right - (maxWidth * numChildren + (numChildren - 1) * this.padding)) / 2;
/*      */             
/*      */ 
/*      */ 
/* 1079 */             xOffset = this.padding + maxWidth;
/*      */           } else { int xOffset;
/* 1081 */             if (numChildren > 1) {
/* 1082 */               int xLocation = insets.left;
/* 1083 */               xOffset = (container.getSize().width - insets.left - insets.right - maxWidth * numChildren) / (numChildren - 1) + maxWidth;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1088 */               xLocation = insets.left + (container.getSize().width - insets.left - insets.right - maxWidth) / 2;
/*      */               
/*      */ 
/* 1091 */               xOffset = 0;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1098 */           if (!ltr) {
/* 1099 */             xLocation = container.getSize().width - insets.right - (xLocation - insets.left) - maxWidth;
/*      */             
/* 1101 */             xOffset = -xOffset;
/*      */           }
/*      */           
/* 1104 */           for (counter = 0; counter < numChildren; counter++) {
/* 1105 */             children[counter].setBounds(xLocation, yLocation, maxWidth, sizes[counter].height);
/*      */             
/* 1107 */             xLocation += xOffset;
/*      */           }
/*      */         } else {
/* 1110 */           int totalWidth = 0;
/*      */           
/* 1112 */           for (int counter = 0; counter < numChildren; counter++) {
/* 1113 */             sizes[counter] = children[counter].getPreferredSize();
/* 1114 */             totalWidth += sizes[counter].width;
/*      */           }
/*      */           
/* 1117 */           totalWidth += (numChildren - 1) * this.padding;
/*      */           
/* 1119 */           boolean cc = getCentersChildren();
/*      */           
/*      */           int xOffset;
/*      */           int xOffset;
/* 1123 */           if (cc) {
/* 1124 */             int xLocation = insets.left + (container.getSize().width - insets.left - insets.right - totalWidth) / 2;
/*      */             
/*      */ 
/* 1127 */             xOffset = this.padding;
/*      */           } else { int xLocation;
/* 1129 */             if (numChildren > 1) {
/* 1130 */               int xOffset = (container.getSize().width - insets.left - insets.right - totalWidth) / (numChildren - 1);
/*      */               
/* 1132 */               xLocation = insets.left;
/*      */             } else {
/* 1134 */               xLocation = insets.left + (container.getSize().width - insets.left - insets.right - totalWidth) / 2;
/*      */               
/*      */ 
/* 1137 */               xOffset = 0;
/*      */             }
/*      */           }
/*      */           
/* 1141 */           if (ltr) {
/* 1142 */             for (counter = 0; counter < numChildren; counter++) {
/* 1143 */               children[counter].setBounds(xLocation, yLocation, sizes[counter].width, sizes[counter].height);
/*      */               
/* 1145 */               xLocation += xOffset + sizes[counter].width;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1150 */           int xLocation = container.getSize().width - insets.right - (xLocation - insets.left);
/*      */           
/*      */ 
/* 1153 */           for (counter = 0; counter < numChildren; counter++) {
/* 1154 */             xLocation -= xOffset + sizes[counter].width;
/* 1155 */             children[counter].setBounds(xLocation, yLocation, sizes[counter].width, sizes[counter].height);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container c)
/*      */     {
/* 1164 */       if (c != null) {
/* 1165 */         Component[] children = c.getComponents();
/*      */         
/* 1167 */         if ((children != null) && (children.length > 0))
/*      */         {
/* 1169 */           int numChildren = children.length;
/* 1170 */           int height = 0;
/* 1171 */           Insets cInsets = c.getInsets();
/* 1172 */           int extraHeight = cInsets.top + cInsets.bottom;
/* 1173 */           int extraWidth = cInsets.left + cInsets.right;
/*      */           
/* 1175 */           if (this.syncAllWidths) {
/* 1176 */             int maxWidth = 0;
/*      */             
/* 1178 */             for (int counter = 0; counter < numChildren; 
/* 1179 */                 counter++) {
/* 1180 */               Dimension aSize = children[counter].getPreferredSize();
/* 1181 */               height = Math.max(height, aSize.height);
/* 1182 */               maxWidth = Math.max(maxWidth, aSize.width);
/*      */             }
/*      */             
/* 1185 */             return new Dimension(extraWidth + maxWidth * numChildren + (numChildren - 1) * this.padding, extraHeight + height);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1190 */           int totalWidth = 0;
/*      */           
/* 1192 */           for (int counter = 0; counter < numChildren; 
/* 1193 */               counter++) {
/* 1194 */             Dimension aSize = children[counter].getPreferredSize();
/* 1195 */             height = Math.max(height, aSize.height);
/* 1196 */             totalWidth += aSize.width;
/*      */           }
/*      */           
/* 1199 */           totalWidth += (numChildren - 1) * this.padding;
/*      */           
/* 1201 */           return new Dimension(extraWidth + totalWidth, extraHeight + height);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1207 */       return new Dimension(0, 0);
/*      */     }
/*      */     
/*      */     public Dimension preferredLayoutSize(Container c) {
/* 1211 */       return minimumLayoutSize(c);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void removeLayoutComponent(Component c) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public PropertyChangeHandler() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent e)
/*      */     {
/* 1231 */       if (e.getSource() == LiquidOptionPaneUI.this.optionPane)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1236 */         if ("ancestor" == e.getPropertyName()) {
/* 1237 */           JOptionPane op = (JOptionPane)e.getSource();
/*      */           
/*      */           boolean isComingUp;
/*      */           
/*      */           boolean isComingUp;
/* 1242 */           if (e.getOldValue() == null) {
/* 1243 */             isComingUp = true;
/*      */           } else {
/* 1245 */             isComingUp = false;
/*      */           }
/*      */           
/*      */ 
/* 1249 */           switch (op.getMessageType())
/*      */           {
/*      */           case -1: 
/* 1252 */             if (isComingUp)
/* 1253 */               LiquidOptionPaneUI.this.fireAudioAction("OptionPane.informationSound");
/* 1254 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */           case 3: 
/* 1260 */             if (isComingUp)
/* 1261 */               LiquidOptionPaneUI.this.fireAudioAction("OptionPane.questionSound");
/* 1262 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */           case 1: 
/* 1268 */             if (isComingUp)
/* 1269 */               LiquidOptionPaneUI.this.fireAudioAction("OptionPane.informationSound");
/* 1270 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */           case 2: 
/* 1276 */             if (isComingUp)
/* 1277 */               LiquidOptionPaneUI.this.fireAudioAction("OptionPane.warningSound");
/* 1278 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */           case 0: 
/* 1284 */             if (isComingUp)
/* 1285 */               LiquidOptionPaneUI.this.fireAudioAction("OptionPane.errorSound");
/* 1286 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */           default: 
/* 1291 */             System.err.println("Undefined JOptionPane type: " + op.getMessageType());
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1299 */         String changeName = e.getPropertyName();
/*      */         
/* 1301 */         if ((changeName.equals("options")) || (changeName.equals("initialValue")) || (changeName.equals("icon")) || (changeName.equals("messageType")) || (changeName.equals("optionType")) || (changeName.equals("message")) || (changeName.equals("selectionValues")) || (changeName.equals("initialSelectionValue")) || (changeName.equals("wantsInput")))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1311 */           LiquidOptionPaneUI.this.uninstallComponents();
/* 1312 */           LiquidOptionPaneUI.this.installComponents();
/* 1313 */           LiquidOptionPaneUI.this.optionPane.validate();
/* 1314 */         } else if (changeName.equals("componentOrientation")) {
/* 1315 */           ComponentOrientation o = (ComponentOrientation)e.getNewValue();
/* 1316 */           JOptionPane op = (JOptionPane)e.getSource();
/*      */           
/* 1318 */           if (o != (ComponentOrientation)e.getOldValue()) {
/* 1319 */             op.applyComponentOrientation(o);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public class ButtonActionListener
/*      */     implements ActionListener
/*      */   {
/*      */     protected int buttonIndex;
/*      */     
/*      */ 
/*      */     public ButtonActionListener(int buttonIndex)
/*      */     {
/* 1335 */       this.buttonIndex = buttonIndex;
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1339 */       if (LiquidOptionPaneUI.this.optionPane != null) {
/* 1340 */         int optionType = LiquidOptionPaneUI.this.optionPane.getOptionType();
/* 1341 */         Object[] options = LiquidOptionPaneUI.this.optionPane.getOptions();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1348 */         if ((LiquidOptionPaneUI.this.inputComponent != null) && (
/* 1349 */           (options != null) || (optionType == -1) || (((optionType == 0) || (optionType == 1) || (optionType == 2)) && (this.buttonIndex == 0))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1355 */           LiquidOptionPaneUI.this.resetInputValue();
/*      */         }
/*      */         
/*      */ 
/* 1359 */         if (options == null) {
/* 1360 */           if ((optionType == 2) && (this.buttonIndex == 1))
/*      */           {
/* 1362 */             LiquidOptionPaneUI.this.optionPane.setValue(new Integer(2));
/*      */           } else {
/* 1364 */             LiquidOptionPaneUI.this.optionPane.setValue(new Integer(this.buttonIndex));
/*      */           }
/*      */         } else {
/* 1367 */           LiquidOptionPaneUI.this.optionPane.setValue(options[this.buttonIndex]);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class ListSelectionListener
/*      */     extends java.awt.event.MouseAdapter
/*      */   {
/* 1380 */     ListSelectionListener(LiquidOptionPaneUI.1 x1) { this(); }
/*      */     
/* 1382 */     public void mousePressed(MouseEvent e) { if (e.getClickCount() == 2) {
/* 1383 */         JList list = (JList)e.getSource();
/* 1384 */         int index = list.locationToIndex(e.getPoint());
/*      */         
/* 1386 */         LiquidOptionPaneUI.this.optionPane.setInputValue(list.getModel().getElementAt(index));
/*      */       }
/*      */     }
/*      */     
/*      */     private ListSelectionListener() {}
/*      */   }
/*      */   
/*      */   private class TextFieldActionListener implements ActionListener {
/* 1394 */     TextFieldActionListener(LiquidOptionPaneUI.1 x1) { this(); }
/*      */     
/* 1396 */     public void actionPerformed(ActionEvent e) { LiquidOptionPaneUI.this.optionPane.setInputValue(((JTextField)e.getSource()).getText()); }
/*      */     
/*      */ 
/*      */ 
/*      */     private TextFieldActionListener() {}
/*      */   }
/*      */   
/*      */ 
/*      */   private static class MultiplexingTextField
/*      */     extends JTextField
/*      */   {
/*      */     private KeyStroke[] strokes;
/*      */     
/*      */ 
/*      */     MultiplexingTextField(int cols)
/*      */     {
/* 1412 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     void setKeyStrokes(KeyStroke[] strokes)
/*      */     {
/* 1420 */       this.strokes = strokes;
/*      */     }
/*      */     
/*      */     protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed)
/*      */     {
/* 1425 */       boolean processed = super.processKeyBinding(ks, e, condition, pressed);
/*      */       
/*      */ 
/* 1428 */       if ((processed) && (condition != 2)) {
/* 1429 */         for (int counter = this.strokes.length - 1; counter >= 0; 
/* 1430 */             counter--) {
/* 1431 */           if (this.strokes[counter].equals(ks))
/*      */           {
/*      */ 
/*      */ 
/* 1435 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1440 */       return processed;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CloseAction
/*      */     extends javax.swing.AbstractAction
/*      */   {
/* 1452 */     CloseAction(LiquidOptionPaneUI.1 x0) { this(); }
/*      */     
/* 1454 */     public void actionPerformed(ActionEvent e) { JOptionPane optionPane = (JOptionPane)e.getSource();
/*      */       
/* 1456 */       optionPane.setValue(new Integer(-1));
/*      */     }
/*      */     
/*      */     private CloseAction() {}
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidOptionPaneUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */