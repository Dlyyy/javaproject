/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import javax.swing.Icon;
/*     */ import org.jdesktop.swingx.plaf.HeaderAddon;
/*     */ import org.jdesktop.swingx.plaf.HeaderUI;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
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
/*     */ public class JXHeader
/*     */   extends JXPanel
/*     */ {
/*     */   private static final long serialVersionUID = 3593838231433068954L;
/*     */   public static final String uiClassID = "HeaderUI";
/*     */   private String title;
/*     */   private String description;
/*     */   private Icon icon;
/*     */   private Font titleFont;
/*     */   private Font descriptionFont;
/*     */   private Color titleForeground;
/*     */   private Color descriptionForeground;
/*     */   
/*     */   static
/*     */   {
/*  87 */     LookAndFeelAddons.contribute(new HeaderAddon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum IconPosition
/*     */   {
/*  97 */     LEFT, 
/*     */     
/*     */ 
/*     */ 
/* 101 */     RIGHT;
/*     */     
/*     */ 
/*     */ 
/*     */     private IconPosition() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 110 */   private IconPosition iconPosition = IconPosition.RIGHT;
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
/*     */   public JXHeader(String title, String description)
/*     */   {
/* 124 */     this(title, description, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXHeader(String title, String description, Icon icon)
/*     */   {
/* 135 */     setTitle(title);
/* 136 */     setDescription(description);
/* 137 */     setIcon(icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeaderUI getUI()
/*     */   {
/* 147 */     return (HeaderUI)super.getUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUI(HeaderUI ui)
/*     */   {
/* 157 */     super.setUI(ui);
/*     */   }
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
/* 169 */     return "HeaderUI";
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
/* 181 */     setUI((HeaderUI)LookAndFeelAddons.getUI(this, HeaderUI.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 192 */     String old = getTitle();
/* 193 */     this.title = title;
/* 194 */     firePropertyChange("title", old, getTitle());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 203 */     return this.title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/* 213 */     String old = getDescription();
/* 214 */     this.description = description;
/* 215 */     firePropertyChange("description", old, getDescription());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 224 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIcon(Icon icon)
/*     */   {
/* 234 */     Icon old = getIcon();
/* 235 */     this.icon = icon;
/* 236 */     firePropertyChange("icon", old, getIcon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getIcon()
/*     */   {
/* 245 */     return this.icon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFont(Font font)
/*     */   {
/* 254 */     super.setFont(font);
/* 255 */     setTitleFont(font);
/* 256 */     setDescriptionFont(font);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitleFont(Font font)
/*     */   {
/* 264 */     Font old = getTitleFont();
/* 265 */     this.titleFont = font;
/* 266 */     firePropertyChange("titleFont", old, getTitleFont());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Font getTitleFont()
/*     */   {
/* 275 */     return this.titleFont;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescriptionFont(Font font)
/*     */   {
/* 283 */     Font old = getDescriptionFont();
/* 284 */     this.descriptionFont = font;
/* 285 */     firePropertyChange("descriptionFont", old, getDescriptionFont());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Font getDescriptionFont()
/*     */   {
/* 294 */     return this.descriptionFont;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getTitleForeground()
/*     */   {
/* 302 */     return this.titleForeground;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitleForeground(Color titleForeground)
/*     */   {
/* 310 */     Color old = getTitleForeground();
/* 311 */     this.titleForeground = titleForeground;
/* 312 */     firePropertyChange("titleForeground", old, getTitleForeground());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getDescriptionForeground()
/*     */   {
/* 320 */     return this.descriptionForeground;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescriptionForeground(Color descriptionForeground)
/*     */   {
/* 328 */     Color old = getDescriptionForeground();
/* 329 */     this.descriptionForeground = descriptionForeground;
/* 330 */     firePropertyChange("descriptionForeground", old, getDescriptionForeground());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IconPosition getIconPosition()
/*     */   {
/* 338 */     return this.iconPosition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIconPosition(IconPosition iconPosition)
/*     */   {
/* 347 */     IconPosition old = getIconPosition();
/* 348 */     this.iconPosition = iconPosition;
/* 349 */     firePropertyChange("iconPosition", old, getIconPosition());
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 354 */     Dimension s = super.getPreferredSize();
/*     */     
/* 356 */     s.width += 5;
/* 357 */     return s;
/*     */   }
/*     */   
/*     */   public JXHeader() {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */