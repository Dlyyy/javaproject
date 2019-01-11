/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Locale;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import org.jdesktop.swingx.JXTipOfTheDay;
/*     */ import org.jdesktop.swingx.JXTipOfTheDay.ShowOnStartupChoice;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ import org.jdesktop.swingx.plaf.TipOfTheDayUI;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.tips.TipOfTheDayModel;
/*     */ import org.jdesktop.swingx.tips.TipOfTheDayModel.Tip;
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
/*     */ public class BasicTipOfTheDayUI
/*     */   extends TipOfTheDayUI
/*     */ {
/*     */   protected JXTipOfTheDay tipPane;
/*     */   protected JPanel tipArea;
/*     */   protected Component currentTipComponent;
/*     */   protected Font tipFont;
/*     */   protected PropertyChangeListener changeListener;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  78 */     return new BasicTipOfTheDayUI((JXTipOfTheDay)c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicTipOfTheDayUI(JXTipOfTheDay tipPane)
/*     */   {
/*  89 */     this.tipPane = tipPane;
/*     */   }
/*     */   
/*     */ 
/*     */   public JDialog createDialog(Component parentComponent, JXTipOfTheDay.ShowOnStartupChoice choice)
/*     */   {
/*  95 */     return createDialog(parentComponent, choice, true);
/*     */   }
/*     */   
/*     */ 
/*     */   protected JDialog createDialog(Component parentComponent, final JXTipOfTheDay.ShowOnStartupChoice choice, boolean showPreviousButton)
/*     */   {
/* 101 */     Locale locale = parentComponent == null ? null : parentComponent.getLocale();
/* 102 */     String title = UIManagerExt.getString("TipOfTheDay.dialogTitle", locale);
/*     */     
/*     */     Window window;
/*     */     
/*     */     Window window;
/* 107 */     if (parentComponent == null) {
/* 108 */       window = JOptionPane.getRootFrame();
/*     */     } else {
/* 110 */       window = (parentComponent instanceof Window) ? (Window)parentComponent : SwingUtilities.getWindowAncestor(parentComponent);
/*     */     }
/*     */     JDialog dialog;
/*     */     final JDialog dialog;
/* 114 */     if ((window instanceof Frame)) {
/* 115 */       dialog = new JDialog((Frame)window, title, true);
/*     */     } else {
/* 117 */       dialog = new JDialog((Dialog)window, title, true);
/*     */     }
/*     */     
/* 120 */     dialog.getContentPane().setLayout(new BorderLayout(10, 10));
/* 121 */     dialog.getContentPane().add(this.tipPane, "Center");
/* 122 */     ((JComponent)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */     JPanel controls = new JPanel(new BorderLayout());
/* 129 */     dialog.add("South", controls);
/*     */     final JCheckBox showOnStartupBox;
/* 131 */     if (choice != null) {
/* 132 */       JCheckBox showOnStartupBox = new JCheckBox(UIManagerExt.getString("TipOfTheDay.showOnStartupText", locale), choice.isShowingOnStartup());
/*     */       
/*     */ 
/* 135 */       controls.add(showOnStartupBox, "Center");
/*     */     } else {
/* 137 */       showOnStartupBox = null;
/*     */     }
/*     */     
/* 140 */     JPanel buttons = new JPanel(new GridLayout(1, showPreviousButton ? 3 : 2, 9, 0));
/*     */     
/* 142 */     controls.add(buttons, "After");
/*     */     
/* 144 */     if (showPreviousButton) {
/* 145 */       JButton previousTipButton = new JButton(UIManagerExt.getString("TipOfTheDay.previousTipText", locale));
/*     */       
/* 147 */       buttons.add(previousTipButton);
/* 148 */       previousTipButton.addActionListener(getActionMap().get("previousTip"));
/*     */     }
/*     */     
/* 151 */     JButton nextTipButton = new JButton(UIManagerExt.getString("TipOfTheDay.nextTipText", locale));
/*     */     
/* 153 */     buttons.add(nextTipButton);
/* 154 */     nextTipButton.addActionListener(getActionMap().get("nextTip"));
/*     */     
/* 156 */     JButton closeButton = new JButton(UIManagerExt.getString("TipOfTheDay.closeText", locale));
/*     */     
/* 158 */     buttons.add(closeButton);
/*     */     
/* 160 */     final ActionListener saveChoice = new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 162 */         if (choice != null) {
/* 163 */           choice.setShowingOnStartup(showOnStartupBox.isSelected());
/*     */         }
/* 165 */         dialog.setVisible(false);
/*     */       }
/*     */       
/* 168 */     };
/* 169 */     closeButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 171 */         dialog.setVisible(false);
/* 172 */         saveChoice.actionPerformed(null);
/*     */       }
/* 174 */     });
/* 175 */     dialog.getRootPane().setDefaultButton(closeButton);
/*     */     
/* 177 */     dialog.addWindowListener(new WindowAdapter()
/*     */     {
/*     */       public void windowClosing(WindowEvent e) {
/* 180 */         saveChoice.actionPerformed(null);
/*     */       }
/*     */       
/* 183 */     });
/* 184 */     ((JComponent)dialog.getContentPane()).registerKeyboardAction(saveChoice, KeyStroke.getKeyStroke(27, 0), 2);
/*     */     
/*     */ 
/*     */ 
/* 188 */     dialog.pack();
/* 189 */     dialog.setLocationRelativeTo(parentComponent);
/*     */     
/* 191 */     return dialog;
/*     */   }
/*     */   
/*     */   public void installUI(JComponent c)
/*     */   {
/* 196 */     super.installUI(c);
/* 197 */     installDefaults();
/* 198 */     installKeyboardActions();
/* 199 */     installComponents();
/* 200 */     installListeners();
/*     */     
/* 202 */     showCurrentTip();
/*     */   }
/*     */   
/*     */   protected void installKeyboardActions() {
/* 206 */     ActionMap map = getActionMap();
/* 207 */     if (map != null) {
/* 208 */       SwingUtilities.replaceUIActionMap(this.tipPane, map);
/*     */     }
/*     */   }
/*     */   
/*     */   ActionMap getActionMap() {
/* 213 */     ActionMap map = new ActionMapUIResource();
/* 214 */     map.put("previousTip", new PreviousTipAction());
/* 215 */     map.put("nextTip", new NextTipAction());
/* 216 */     return map;
/*     */   }
/*     */   
/*     */   protected void installListeners() {
/* 220 */     this.changeListener = createChangeListener();
/* 221 */     this.tipPane.addPropertyChangeListener(this.changeListener);
/*     */   }
/*     */   
/*     */   protected PropertyChangeListener createChangeListener() {
/* 225 */     return new ChangeListener();
/*     */   }
/*     */   
/*     */   protected void installDefaults() {
/* 229 */     LookAndFeel.installColorsAndFont(this.tipPane, "TipOfTheDay.background", "TipOfTheDay.foreground", "TipOfTheDay.font");
/*     */     
/* 231 */     LookAndFeel.installBorder(this.tipPane, "TipOfTheDay.border");
/* 232 */     LookAndFeel.installProperty(this.tipPane, "opaque", Boolean.TRUE);
/* 233 */     this.tipFont = UIManager.getFont("TipOfTheDay.tipFont");
/*     */   }
/*     */   
/*     */   protected void installComponents() {
/* 237 */     this.tipPane.setLayout(new BorderLayout());
/*     */     
/*     */ 
/* 240 */     JLabel tipIcon = new JLabel(UIManagerExt.getString("TipOfTheDay.didYouKnowText", this.tipPane.getLocale()));
/*     */     
/* 242 */     tipIcon.setIcon(UIManager.getIcon("TipOfTheDay.icon"));
/* 243 */     tipIcon.setBorder(BorderFactory.createEmptyBorder(22, 15, 22, 15));
/* 244 */     this.tipPane.add("North", tipIcon);
/*     */     
/*     */ 
/* 247 */     this.tipArea = new JPanel(new BorderLayout(2, 2));
/* 248 */     this.tipArea.setOpaque(false);
/* 249 */     this.tipArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
/* 250 */     this.tipPane.add("Center", this.tipArea);
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 255 */     return new Dimension(420, 175);
/*     */   }
/*     */   
/*     */   protected void showCurrentTip() {
/* 259 */     if (this.currentTipComponent != null) {
/* 260 */       this.tipArea.remove(this.currentTipComponent);
/*     */     }
/*     */     
/* 263 */     int currentTip = this.tipPane.getCurrentTip();
/* 264 */     if (currentTip == -1) {
/* 265 */       JLabel label = new JLabel();
/* 266 */       label.setOpaque(true);
/* 267 */       label.setBackground(UIManager.getColor("TextArea.background"));
/* 268 */       this.currentTipComponent = label;
/* 269 */       this.tipArea.add("Center", this.currentTipComponent);
/* 270 */       return;
/*     */     }
/*     */     
/*     */ 
/* 274 */     if ((this.tipPane.getModel() == null) || (this.tipPane.getModel().getTipCount() == 0) || ((currentTip < 0) && (currentTip >= this.tipPane.getModel().getTipCount())))
/*     */     {
/* 276 */       this.currentTipComponent = new JLabel();
/*     */     } else {
/* 278 */       TipOfTheDayModel.Tip tip = this.tipPane.getModel().getTipAt(currentTip);
/*     */       
/* 280 */       Object tipObject = tip.getTip();
/* 281 */       if ((tipObject instanceof Component)) {
/* 282 */         this.currentTipComponent = ((Component)tipObject);
/* 283 */       } else if ((tipObject instanceof Icon)) {
/* 284 */         this.currentTipComponent = new JLabel((Icon)tipObject);
/*     */       } else {
/* 286 */         JScrollPane tipScroll = new JScrollPane();
/* 287 */         tipScroll.setBorder(null);
/* 288 */         tipScroll.setOpaque(false);
/* 289 */         tipScroll.getViewport().setOpaque(false);
/* 290 */         tipScroll.setBorder(null);
/*     */         
/* 292 */         String text = tipObject == null ? "" : tipObject.toString();
/*     */         
/* 294 */         if (BasicHTML.isHTMLString(text)) {
/* 295 */           JEditorPane editor = new JEditorPane("text/html", text);
/* 296 */           editor.setFont(this.tipPane.getFont());
/*     */           
/* 298 */           SwingXUtilities.setHtmlFont((HTMLDocument)editor.getDocument(), this.tipPane.getFont());
/*     */           
/* 300 */           editor.setEditable(false);
/* 301 */           editor.setBorder(null);
/* 302 */           editor.setMargin(null);
/* 303 */           editor.setOpaque(false);
/* 304 */           tipScroll.getViewport().setView(editor);
/*     */         } else {
/* 306 */           JTextArea area = new JTextArea(text);
/* 307 */           area.setFont(this.tipPane.getFont());
/* 308 */           area.setEditable(false);
/* 309 */           area.setLineWrap(true);
/* 310 */           area.setWrapStyleWord(true);
/* 311 */           area.setBorder(null);
/* 312 */           area.setMargin(null);
/* 313 */           area.setOpaque(false);
/* 314 */           tipScroll.getViewport().setView(area);
/*     */         }
/*     */         
/* 317 */         this.currentTipComponent = tipScroll;
/*     */       }
/*     */     }
/*     */     
/* 321 */     this.tipArea.add("Center", this.currentTipComponent);
/* 322 */     this.tipArea.revalidate();
/* 323 */     this.tipArea.repaint();
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 328 */     uninstallListeners();
/* 329 */     uninstallComponents();
/* 330 */     uninstallDefaults();
/* 331 */     super.uninstallUI(c);
/*     */   }
/*     */   
/*     */ 
/* 335 */   protected void uninstallListeners() { this.tipPane.removePropertyChangeListener(this.changeListener); }
/*     */   
/*     */   protected void uninstallComponents() {}
/*     */   
/*     */   protected void uninstallDefaults() {}
/*     */   
/*     */   class ChangeListener implements PropertyChangeListener {
/*     */     ChangeListener() {}
/*     */     
/* 344 */     public void propertyChange(PropertyChangeEvent evt) { if ("currentTip".equals(evt.getPropertyName())) {
/* 345 */         BasicTipOfTheDayUI.this.showCurrentTip();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class PreviousTipAction extends AbstractAction {
/*     */     public PreviousTipAction() {
/* 352 */       super();
/*     */     }
/*     */     
/* 355 */     public void actionPerformed(ActionEvent e) { BasicTipOfTheDayUI.this.tipPane.previousTip(); }
/*     */     
/*     */     public boolean isEnabled()
/*     */     {
/* 359 */       return BasicTipOfTheDayUI.this.tipPane.isEnabled();
/*     */     }
/*     */   }
/*     */   
/*     */   class NextTipAction extends AbstractAction {
/*     */     public NextTipAction() {
/* 365 */       super();
/*     */     }
/*     */     
/* 368 */     public void actionPerformed(ActionEvent e) { BasicTipOfTheDayUI.this.tipPane.nextTip(); }
/*     */     
/*     */     public boolean isEnabled()
/*     */     {
/* 372 */       return BasicTipOfTheDayUI.this.tipPane.isEnabled();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicTipOfTheDayUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */