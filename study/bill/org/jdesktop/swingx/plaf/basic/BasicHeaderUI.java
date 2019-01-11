/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.HierarchyBoundsAdapter;
/*     */ import java.awt.event.HierarchyBoundsListener;
/*     */ import java.awt.event.HierarchyEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.View;
/*     */ import org.jdesktop.swingx.JXHeader;
/*     */ import org.jdesktop.swingx.JXHeader.IconPosition;
/*     */ import org.jdesktop.swingx.JXLabel;
/*     */ import org.jdesktop.swingx.JXLabel.MultiLineSupport;
/*     */ import org.jdesktop.swingx.painter.MattePainter;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.plaf.HeaderUI;
/*     */ import org.jdesktop.swingx.plaf.PainterUIResource;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*     */ public class BasicHeaderUI
/*     */   extends HeaderUI
/*     */ {
/*  76 */   private static final Logger LOG = Logger.getLogger(BasicHeaderUI.class.getName());
/*     */   protected JLabel titleLabel;
/*     */   protected DescriptionPane descriptionPane;
/*     */   protected JLabel imagePanel;
/*     */   private PropertyChangeListener propListener;
/*     */   private HierarchyBoundsListener boundsListener;
/*     */   private Color gradientLightColor;
/*     */   private Color gradientDarkColor;
/*     */   
/*     */   protected class DescriptionPane
/*     */     extends JXLabel
/*     */   {
/*     */     protected DescriptionPane() {}
/*     */     
/*     */     public void paint(Graphics g)
/*     */     {
/*  92 */       super.paint(g);
/*     */     }
/*     */     
/*     */     public JXLabel.MultiLineSupport getMultiLineSupport()
/*     */     {
/*  97 */       return super.getMultiLineSupport();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/* 124 */     return new BasicHeaderUI();
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
/*     */   public void installUI(JComponent c)
/*     */   {
/* 154 */     super.installUI(c);
/* 155 */     assert ((c instanceof JXHeader));
/* 156 */     JXHeader header = (JXHeader)c;
/*     */     
/* 158 */     installDefaults(header);
/* 159 */     installComponents(header);
/* 160 */     installListeners(header);
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
/* 190 */     assert ((c instanceof JXHeader));
/* 191 */     JXHeader header = (JXHeader)c;
/*     */     
/* 193 */     uninstallListeners(header);
/* 194 */     uninstallComponents(header);
/* 195 */     uninstallDefaults(header);
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
/*     */   protected void installDefaults(JXHeader header)
/*     */   {
/* 208 */     this.gradientLightColor = UIManagerExt.getColor("JXHeader.startBackground");
/* 209 */     if (this.gradientLightColor == null)
/*     */     {
/* 211 */       this.gradientLightColor = Color.WHITE;
/*     */     }
/* 213 */     this.gradientDarkColor = UIManagerExt.getColor("JXHeader.background");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 218 */     if (this.gradientDarkColor == null) {
/* 219 */       this.gradientDarkColor = UIManagerExt.getColor("control");
/*     */     }
/*     */     
/* 222 */     if (isUIInstallable(header.getBackgroundPainter())) {
/* 223 */       header.setBackgroundPainter(createBackgroundPainter());
/*     */     }
/*     */     
/*     */ 
/* 227 */     if (isUIInstallable(header.getTitleFont())) {
/* 228 */       Font titleFont = UIManager.getFont("JXHeader.titleFont");
/*     */       
/* 230 */       header.setTitleFont(titleFont != null ? titleFont : UIManager.getFont("Label.font"));
/*     */     }
/*     */     
/* 233 */     if (isUIInstallable(header.getTitleForeground())) {
/* 234 */       Color titleForeground = UIManagerExt.getColor("JXHeader.titleForeground");
/*     */       
/*     */ 
/* 237 */       header.setTitleForeground(titleForeground != null ? titleForeground : UIManagerExt.getColor("Label.foreground"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 242 */     if (isUIInstallable(header.getDescriptionFont())) {
/* 243 */       Font descFont = UIManager.getFont("JXHeader.descriptionFont");
/*     */       
/* 245 */       header.setDescriptionFont(descFont != null ? descFont : UIManager.getFont("Label.font"));
/*     */     }
/*     */     
/* 248 */     if (isUIInstallable(header.getDescriptionForeground())) {
/* 249 */       Color descForeground = UIManagerExt.getColor("JXHeader.descriptionForeground");
/*     */       
/*     */ 
/* 252 */       header.setDescriptionForeground(descForeground != null ? descForeground : UIManagerExt.getColor("Label.foreground"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 257 */     if (isUIInstallable(header.getIcon())) {
/* 258 */       header.setIcon(UIManager.getIcon("Header.defaultIcon"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallDefaults(JXHeader h) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installComponents(JXHeader header)
/*     */   {
/* 278 */     this.titleLabel = new JLabel();
/* 279 */     this.descriptionPane = new DescriptionPane();
/* 280 */     this.imagePanel = new JLabel();
/* 281 */     installComponentDefaults(header);
/* 282 */     header.setLayout(new GridBagLayout());
/* 283 */     resetLayout(header);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallComponents(JXHeader header)
/*     */   {
/* 292 */     uninstallComponentDefaults(header);
/* 293 */     header.remove(this.titleLabel);
/* 294 */     header.remove(this.descriptionPane);
/* 295 */     header.remove(this.imagePanel);
/* 296 */     this.titleLabel = null;
/* 297 */     this.descriptionPane = null;
/* 298 */     this.imagePanel = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installComponentDefaults(JXHeader header)
/*     */   {
/* 309 */     this.titleLabel.setFont(getAsNotUIResource(header.getTitleFont()));
/* 310 */     this.titleLabel.setForeground(getAsNotUIResource(header.getTitleForeground()));
/* 311 */     this.titleLabel.setText(header.getTitle());
/* 312 */     this.descriptionPane.setFont(getAsNotUIResource(header.getDescriptionFont()));
/* 313 */     this.descriptionPane.setForeground(getAsNotUIResource(header.getDescriptionForeground()));
/* 314 */     this.descriptionPane.setOpaque(false);
/* 315 */     this.descriptionPane.setText(header.getDescription());
/* 316 */     this.descriptionPane.setLineWrap(true);
/*     */     
/* 318 */     this.imagePanel.setIcon(header.getIcon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Font getAsNotUIResource(Font font)
/*     */   {
/* 329 */     if (!(font instanceof UIResource)) { return font;
/*     */     }
/* 331 */     return font.deriveFont(font.getAttributes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color getAsNotUIResource(Color color)
/*     */   {
/* 341 */     if (!(color instanceof UIResource)) { return color;
/*     */     }
/* 343 */     float[] rgb = color.getRGBComponents(null);
/* 344 */     return new Color(rgb[0], rgb[1], rgb[2], rgb[3]);
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
/*     */   private boolean isUIInstallable(Object property)
/*     */   {
/* 358 */     return (property == null) || ((property instanceof UIResource));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallComponentDefaults(JXHeader header) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installListeners(final JXHeader header)
/*     */   {
/* 371 */     this.propListener = new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 373 */         BasicHeaderUI.this.onPropertyChange(header, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
/*     */       }
/* 375 */     };
/* 376 */     this.boundsListener = new HierarchyBoundsAdapter()
/*     */     {
/*     */       public void ancestorResized(HierarchyEvent e) {
/* 379 */         if (header == e.getComponent()) {
/* 380 */           View v = (View)BasicHeaderUI.this.descriptionPane.getClientProperty("html");
/*     */           
/* 382 */           if (v == null) {
/* 383 */             BasicHeaderUI.this.descriptionPane.getMultiLineSupport();BasicHeaderUI.this.descriptionPane.putClientProperty("html", JXLabel.MultiLineSupport.createView(BasicHeaderUI.this.descriptionPane));
/*     */             
/* 385 */             v = (View)BasicHeaderUI.this.descriptionPane.getClientProperty("html");
/*     */           }
/* 387 */           if (v != null) {
/* 388 */             Container tla = header.getTopLevelAncestor();
/* 389 */             if (tla == null) {
/* 390 */               tla = header.getParent();
/* 391 */               while (tla.getParent() != null) {
/* 392 */                 tla = tla.getParent();
/*     */               }
/*     */             }
/* 395 */             int h = Math.max(BasicHeaderUI.this.descriptionPane.getHeight(), tla.getHeight());
/* 396 */             int w = Math.min(tla.getWidth(), header.getParent().getWidth());
/*     */             
/* 398 */             w -= 35 + header.getInsets().left + header.getInsets().right + BasicHeaderUI.this.descriptionPane.getInsets().left + BasicHeaderUI.this.descriptionPane.getInsets().right + BasicHeaderUI.this.imagePanel.getInsets().left + BasicHeaderUI.this.imagePanel.getInsets().right + BasicHeaderUI.this.imagePanel.getWidth() + BasicHeaderUI.this.descriptionPane.getBounds().x;
/* 399 */             v.setSize(w, h);
/* 400 */             BasicHeaderUI.this.descriptionPane.setSize(w, (int)Math.ceil(v.getPreferredSpan(1)));
/*     */           }
/*     */         }
/* 403 */       } };
/* 404 */     header.addPropertyChangeListener(this.propListener);
/* 405 */     header.addHierarchyBoundsListener(this.boundsListener);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners(JXHeader h) {
/* 409 */     h.removePropertyChangeListener(this.propListener);
/* 410 */     h.removeHierarchyBoundsListener(this.boundsListener);
/*     */   }
/*     */   
/*     */   protected void onPropertyChange(JXHeader h, String propertyName, Object oldValue, Object newValue) {
/* 414 */     if ("title".equals(propertyName)) {
/* 415 */       this.titleLabel.setText(h.getTitle());
/* 416 */     } else if ("description".equals(propertyName)) {
/* 417 */       this.descriptionPane.setText(h.getDescription());
/* 418 */     } else if ("icon".equals(propertyName)) {
/* 419 */       this.imagePanel.setIcon(h.getIcon());
/* 420 */     } else if ("enabled".equals(propertyName)) {
/* 421 */       boolean enabled = h.isEnabled();
/* 422 */       this.titleLabel.setEnabled(enabled);
/* 423 */       this.descriptionPane.setEnabled(enabled);
/* 424 */       this.imagePanel.setEnabled(enabled);
/* 425 */     } else if ("titleFont".equals(propertyName)) {
/* 426 */       this.titleLabel.setFont((Font)newValue);
/* 427 */     } else if ("descriptionFont".equals(propertyName)) {
/* 428 */       this.descriptionPane.setFont((Font)newValue);
/* 429 */     } else if ("titleForeground".equals(propertyName)) {
/* 430 */       this.titleLabel.setForeground((Color)newValue);
/* 431 */     } else if ("descriptionForeground".equals(propertyName)) {
/* 432 */       this.descriptionPane.setForeground((Color)newValue);
/* 433 */     } else if ("iconPosition".equals(propertyName)) {
/* 434 */       resetLayout(h);
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetLayout(JXHeader h) {
/* 439 */     h.remove(this.titleLabel);
/* 440 */     h.remove(this.descriptionPane);
/* 441 */     h.remove(this.imagePanel);
/* 442 */     if ((h.getIconPosition() == null) || (h.getIconPosition() == JXHeader.IconPosition.RIGHT)) {
/* 443 */       h.add(this.titleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 21, 2, new Insets(12, 12, 0, 11), 0, 0));
/* 444 */       h.add(this.descriptionPane, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 23, 1, new Insets(0, 24, 12, 11), 0, 0));
/* 445 */       h.add(this.imagePanel, new GridBagConstraints(1, 0, 1, 2, 0.0D, 1.0D, 24, 0, new Insets(12, 0, 11, 11), 0, 0));
/*     */     } else {
/* 447 */       h.add(this.titleLabel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 21, 2, new Insets(12, 12, 0, 11), 0, 0));
/* 448 */       h.add(this.descriptionPane, new GridBagConstraints(1, 1, 1, 1, 1.0D, 1.0D, 23, 1, new Insets(0, 24, 12, 11), 0, 0));
/* 449 */       h.add(this.imagePanel, new GridBagConstraints(0, 0, 1, 2, 0.0D, 1.0D, 24, 0, new Insets(12, 11, 0, 11), 0, 0));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Painter createBackgroundPainter()
/*     */   {
/* 456 */     MattePainter p = new MattePainter(new GradientPaint(0.0F, 0.0F, this.gradientLightColor, 1.0F, 0.0F, this.gradientDarkColor));
/* 457 */     p.setPaintStretched(true);
/* 458 */     return new PainterUIResource(p);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicHeaderUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */