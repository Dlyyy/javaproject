/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.TitledPanelAddon;
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
/*     */ public class JXTitledPanel
/*     */   extends JXPanel
/*     */ {
/*     */   public static final String uiClassID = "TitledPanelUI";
/*     */   public static final String LEFT_DECORATION = "JXTitledPanel.leftDecoration";
/*     */   public static final String RIGHT_DECORATION = "JXTitledPanel.rightDecoration";
/*     */   
/*     */   static
/*     */   {
/*  72 */     LookAndFeelAddons.contribute(new TitledPanelAddon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private String title = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Font titleFont;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color titleForeground;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Container contentPanel;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Painter titlePainter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTitledPanel()
/*     */   {
/* 105 */     this(" ");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTitledPanel(String title)
/*     */   {
/* 115 */     this(title, createDefaultContainer());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTitledPanel(String title, Container content)
/*     */   {
/* 126 */     setTitle(title);
/* 127 */     setContentContainer(content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TitledPanelUI getUI()
/*     */   {
/* 137 */     return (TitledPanelUI)this.ui;
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
/*     */   public void setUI(TitledPanelUI ui)
/*     */   {
/* 151 */     super.setUI(ui);
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
/*     */   public String getUIClassID()
/*     */   {
/* 166 */     return "TitledPanelUI";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 178 */     setUI((TitledPanelUI)LookAndFeelAddons.getUI(this, TitledPanelUI.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 188 */     return this.title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 198 */     String oldTitle = this.title;
/* 199 */     this.title = (title == null ? "" : title);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */     firePropertyChange("title", oldTitle, getTitle());
/*     */   }
/*     */   
/*     */   public Container getContentContainer() {
/* 210 */     if (this.contentPanel == null) {
/* 211 */       this.contentPanel = new JXPanel();
/* 212 */       ((JXPanel)this.contentPanel).setBorder(BorderFactory.createEmptyBorder());
/*     */       
/* 214 */       add(this.contentPanel, "Center");
/*     */     }
/* 216 */     return this.contentPanel;
/*     */   }
/*     */   
/*     */   public void setContentContainer(Container contentPanel) {
/* 220 */     if (this.contentPanel != null) {
/* 221 */       remove(this.contentPanel);
/*     */     }
/* 223 */     add(contentPanel, "Center");
/* 224 */     this.contentPanel = contentPanel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRightDecoration(JComponent decoration)
/*     */   {
/* 233 */     JComponent old = getRightDecoration();
/* 234 */     getUI().setRightDecoration(decoration);
/* 235 */     firePropertyChange("rightDecoration", old, getRightDecoration());
/*     */   }
/*     */   
/*     */   public JComponent getRightDecoration() {
/* 239 */     return getUI().getRightDecoration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLeftDecoration(JComponent decoration)
/*     */   {
/* 248 */     JComponent old = getLeftDecoration();
/* 249 */     getUI().setLeftDecoration(decoration);
/* 250 */     firePropertyChange("leftDecoration", old, getLeftDecoration());
/*     */   }
/*     */   
/*     */   public JComponent getLeftDecoration() {
/* 254 */     return getUI().getLeftDecoration();
/*     */   }
/*     */   
/*     */   public Font getTitleFont() {
/* 258 */     return this.titleFont;
/*     */   }
/*     */   
/*     */   public void setTitleFont(Font titleFont) {
/* 262 */     Font old = getTitleFont();
/* 263 */     this.titleFont = titleFont;
/* 264 */     firePropertyChange("titleFont", old, getTitleFont());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitlePainter(Painter p)
/*     */   {
/* 275 */     Painter old = getTitlePainter();
/* 276 */     this.titlePainter = p;
/* 277 */     firePropertyChange("titlePainter", old, getTitlePainter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Painter getTitlePainter()
/*     */   {
/* 284 */     return this.titlePainter;
/*     */   }
/*     */   
/*     */   public Color getTitleForeground() {
/* 288 */     return this.titleForeground;
/*     */   }
/*     */   
/*     */   public void setTitleForeground(Color titleForeground) {
/* 292 */     Color old = getTitleForeground();
/* 293 */     this.titleForeground = titleForeground;
/* 294 */     firePropertyChange("titleForeground", old, getTitleForeground());
/*     */   }
/*     */   
/*     */ 
/*     */   private static Container createDefaultContainer()
/*     */   {
/* 300 */     JXPanel p = new JXPanel();
/* 301 */     p.setOpaque(false);
/* 302 */     return p;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTitledPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */