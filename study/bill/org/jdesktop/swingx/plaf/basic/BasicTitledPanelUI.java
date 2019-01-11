/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.JXPanel;
/*     */ import org.jdesktop.swingx.JXTitledPanel;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ import org.jdesktop.swingx.plaf.TitledPanelUI;
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
/*     */ public class BasicTitledPanelUI
/*     */   extends TitledPanelUI
/*     */ {
/*  68 */   private static final Logger LOG = Logger.getLogger(BasicTitledPanelUI.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JLabel caption;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JXPanel topPanel;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener titleChangeListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JComponent left;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JComponent right;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/* 101 */     return new BasicTitledPanelUI();
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
/*     */   public void installUI(JComponent c)
/*     */   {
/* 130 */     assert ((c instanceof JXTitledPanel));
/* 131 */     JXTitledPanel titledPanel = (JXTitledPanel)c;
/* 132 */     installDefaults(titledPanel);
/*     */     
/* 134 */     this.caption = createAndConfigureCaption(titledPanel);
/* 135 */     this.topPanel = createAndConfigureTopPanel(titledPanel);
/*     */     
/* 137 */     installComponents(titledPanel);
/* 138 */     installListeners(titledPanel);
/*     */   }
/*     */   
/*     */   protected void installDefaults(JXTitledPanel titledPanel) {
/* 142 */     installProperty(titledPanel, "titlePainter", UIManager.get("JXTitledPanel.titlePainter"));
/* 143 */     installProperty(titledPanel, "titleForeground", UIManager.getColor("JXTitledPanel.titleForeground"));
/* 144 */     installProperty(titledPanel, "titleFont", UIManager.getFont("JXTitledPanel.titleFont"));
/* 145 */     LookAndFeel.installProperty(titledPanel, "opaque", Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void uninstallDefaults(JXTitledPanel titledPanel) {}
/*     */   
/*     */   protected void installComponents(JXTitledPanel titledPanel)
/*     */   {
/* 152 */     this.topPanel.add(this.caption, new GridBagConstraints(1, 0, 1, 1, 1.0D, 1.0D, 18, 2, getCaptionInsets(), 0, 0));
/*     */     
/* 154 */     if ((titledPanel.getClientProperty("JXTitledPanel.rightDecoration") instanceof JComponent)) {
/* 155 */       setRightDecoration((JComponent)titledPanel.getClientProperty("JXTitledPanel.rightDecoration"));
/*     */     }
/* 157 */     if ((titledPanel.getClientProperty("JXTitledPanel.leftDecoration") instanceof JComponent)) {
/* 158 */       setLeftDecoration((JComponent)titledPanel.getClientProperty("JXTitledPanel.leftDecoration"));
/*     */     }
/*     */     
/* 161 */     if (!(titledPanel.getLayout() instanceof BorderLayout)) {
/* 162 */       titledPanel.setLayout(new BorderLayout());
/*     */     }
/* 164 */     titledPanel.add(this.topPanel, "North");
/*     */     
/* 166 */     if (SwingXUtilities.isUIInstallable(titledPanel.getBorder()))
/*     */     {
/*     */ 
/* 169 */       titledPanel.setBorder(BorderUIResource.getRaisedBevelBorderUIResource());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void uninstallComponents(JXTitledPanel titledPanel) {
/* 174 */     titledPanel.remove(this.topPanel);
/*     */   }
/*     */   
/*     */   protected Insets getCaptionInsets() {
/* 178 */     return UIManager.getInsets("JXTitledPanel.captionInsets");
/*     */   }
/*     */   
/*     */   protected JXPanel createAndConfigureTopPanel(JXTitledPanel titledPanel) {
/* 182 */     JXPanel topPanel = new JXPanel();
/* 183 */     topPanel.setBackgroundPainter(titledPanel.getTitlePainter());
/* 184 */     topPanel.setBorder(BorderFactory.createEmptyBorder());
/* 185 */     topPanel.setLayout(new GridBagLayout());
/* 186 */     topPanel.setOpaque(false);
/* 187 */     return topPanel;
/*     */   }
/*     */   
/*     */   protected JLabel createAndConfigureCaption(final JXTitledPanel titledPanel) {
/* 191 */     JLabel caption = new JLabel(titledPanel.getTitle())
/*     */     {
/*     */       public void updateUI()
/*     */       {
/* 195 */         super.updateUI();
/* 196 */         setForeground(titledPanel.getTitleForeground());
/* 197 */         setFont(titledPanel.getTitleFont());
/*     */       }
/* 199 */     };
/* 200 */     caption.setFont(titledPanel.getTitleFont());
/* 201 */     caption.setForeground(titledPanel.getTitleForeground());
/* 202 */     return caption;
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
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 232 */     assert ((c instanceof JXTitledPanel));
/* 233 */     JXTitledPanel titledPanel = (JXTitledPanel)c;
/* 234 */     uninstallListeners(titledPanel);
/*     */     
/*     */ 
/* 237 */     this.topPanel.removeAll();
/* 238 */     titledPanel.remove(this.topPanel);
/* 239 */     titledPanel.putClientProperty("JXTitledPanel.leftDecoration", this.left);
/* 240 */     titledPanel.putClientProperty("JXTitledPanel.rightDecoration", this.right);
/* 241 */     this.caption = null;
/* 242 */     this.topPanel = null;
/* 243 */     titledPanel = null;
/* 244 */     this.left = null;
/* 245 */     this.right = null;
/*     */   }
/*     */   
/*     */   protected void installListeners(final JXTitledPanel titledPanel) {
/* 249 */     this.titleChangeListener = new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 251 */         if (evt.getPropertyName().equals("title")) {
/* 252 */           BasicTitledPanelUI.this.caption.setText((String)evt.getNewValue());
/* 253 */         } else if (evt.getPropertyName().equals("titleForeground")) {
/* 254 */           BasicTitledPanelUI.this.caption.setForeground((Color)evt.getNewValue());
/* 255 */         } else if (evt.getPropertyName().equals("titleFont")) {
/* 256 */           BasicTitledPanelUI.this.caption.setFont((Font)evt.getNewValue());
/* 257 */         } else if ("titlePainter".equals(evt.getPropertyName())) {
/* 258 */           BasicTitledPanelUI.this.topPanel.setBackgroundPainter(titledPanel.getTitlePainter());
/* 259 */           BasicTitledPanelUI.this.topPanel.repaint();
/*     */         }
/*     */       }
/* 262 */     };
/* 263 */     titledPanel.addPropertyChangeListener(this.titleChangeListener);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners(JXTitledPanel titledPanel) {
/* 267 */     titledPanel.removePropertyChangeListener(this.titleChangeListener);
/*     */   }
/*     */   
/*     */   protected void installProperty(JComponent c, String propName, Object value) {
/*     */     try {
/* 272 */       BeanInfo bi = Introspector.getBeanInfo(c.getClass());
/* 273 */       for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
/* 274 */         if (pd.getName().equals(propName)) {
/* 275 */           Method m = pd.getReadMethod();
/* 276 */           Object oldVal = m.invoke(c, new Object[0]);
/* 277 */           if ((oldVal == null) || ((oldVal instanceof UIResource))) {
/* 278 */             m = pd.getWriteMethod();
/* 279 */             m.invoke(c, new Object[] { value });
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 284 */       LOG.log(Level.FINE, "Failed to install property " + propName, e);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 307 */     super.paint(g, c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRightDecoration(JComponent decoration)
/*     */   {
/* 316 */     if (this.right != null) this.topPanel.remove(this.right);
/* 317 */     this.right = decoration;
/* 318 */     if (this.right != null) {
/* 319 */       this.topPanel.add(decoration, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 13, 0, UIManager.getInsets("JXTitledPanel.rightDecorationInsets"), 0, 0));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public JComponent getRightDecoration()
/*     */   {
/* 326 */     return this.right;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLeftDecoration(JComponent decoration)
/*     */   {
/* 335 */     if (this.left != null) this.topPanel.remove(this.left);
/* 336 */     this.left = decoration;
/* 337 */     if (this.left != null) {
/* 338 */       this.topPanel.add(this.left, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, UIManager.getInsets("JXTitledPanel.leftDecorationInsets"), 0, 0));
/*     */     }
/*     */   }
/*     */   
/*     */   public JComponent getLeftDecoration()
/*     */   {
/* 344 */     return this.left;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Container getTitleBar()
/*     */   {
/* 352 */     return this.topPanel;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicTitledPanelUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */