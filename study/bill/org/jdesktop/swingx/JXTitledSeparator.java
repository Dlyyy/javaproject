/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
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
/*     */ public class JXTitledSeparator
/*     */   extends JXPanel
/*     */ {
/*     */   private JLabel label;
/*     */   private JSeparator leftSeparator;
/*     */   private JSeparator rightSeparator;
/*     */   
/*     */   public JXTitledSeparator()
/*     */   {
/* 101 */     this("Untitled");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTitledSeparator(String title)
/*     */   {
/* 110 */     this(title, 10, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTitledSeparator(String title, int horizontalAlignment)
/*     */   {
/* 119 */     this(title, horizontalAlignment, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTitledSeparator(String title, int horizontalAlignment, Icon icon)
/*     */   {
/* 128 */     setLayout(new GridBagLayout());
/*     */     
/* 130 */     this.label = new JLabel(title)
/*     */     {
/*     */       public void updateUI() {
/* 133 */         super.updateUI();
/* 134 */         JXTitledSeparator.this.updateTitle();
/*     */       }
/* 136 */     };
/* 137 */     this.label.setIcon(icon);
/* 138 */     this.label.setHorizontalAlignment(horizontalAlignment);
/* 139 */     this.leftSeparator = new JSeparator();
/* 140 */     this.rightSeparator = new JSeparator();
/*     */     
/* 142 */     layoutSeparator();
/*     */     
/* 144 */     updateTitle();
/* 145 */     setOpaque(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateTitle()
/*     */   {
/* 155 */     if (this.label == null) { return;
/*     */     }
/* 157 */     Color c = this.label.getForeground();
/* 158 */     if ((c == null) || ((c instanceof ColorUIResource))) {
/* 159 */       setForeground(UIManager.getColor("TitledBorder.titleColor"));
/*     */     }
/* 161 */     Font f = this.label.getFont();
/* 162 */     if ((f == null) || ((f instanceof FontUIResource))) {
/* 163 */       setFont(UIManager.getFont("TitledBorder.font"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void layoutSeparator()
/*     */   {
/* 173 */     removeAll();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 178 */     int alignment = getHorizontalAlignment();
/*     */     
/* 180 */     if (!getComponentOrientation().isLeftToRight()) {
/* 181 */       switch (alignment) {
/*     */       case 2: 
/* 183 */         alignment = 4;
/* 184 */         break;
/*     */       case 4: 
/* 186 */         alignment = 2;
/* 187 */         break;
/*     */       case 3: 
/* 189 */         alignment = 7;
/* 190 */         break;
/*     */       case 7: 
/* 192 */         alignment = 3;
/* 193 */         break;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 199 */     switch (alignment) {
/*     */     case 2: 
/*     */     case 7: 
/*     */     case 10: 
/* 203 */       add(this.label, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 204 */       add(Box.createHorizontalStrut(3), new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 205 */       add(this.rightSeparator, new GridBagConstraints(2, 0, 1, 1, 1.0D, 0.0D, 21, 2, new Insets(0, 0, 0, 0), 0, 0));
/* 206 */       break;
/*     */     case 3: 
/*     */     case 4: 
/*     */     case 11: 
/* 210 */       add(this.rightSeparator, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 21, 2, new Insets(0, 0, 0, 0), 0, 0));
/* 211 */       add(Box.createHorizontalStrut(3), new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 212 */       add(this.label, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 213 */       break;
/*     */     case 0: case 1: case 5: case 6: 
/*     */     case 8: case 9: default: 
/* 216 */       add(this.leftSeparator, new GridBagConstraints(0, 0, 1, 1, 0.5D, 0.0D, 21, 2, new Insets(0, 0, 0, 0), 0, 0));
/* 217 */       add(Box.createHorizontalStrut(3), new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 218 */       add(this.label, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 219 */       add(Box.createHorizontalStrut(3), new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/* 220 */       add(this.rightSeparator, new GridBagConstraints(4, 0, 1, 1, 0.5D, 0.0D, 21, 2, new Insets(0, 0, 0, 0), 0, 0));
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 231 */     String old = getTitle();
/* 232 */     this.label.setText(title);
/* 233 */     firePropertyChange("title", old, getTitle());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 244 */     return this.label.getText();
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
/*     */   public void setHorizontalAlignment(int alignment)
/*     */   {
/* 272 */     int old = getHorizontalAlignment();
/* 273 */     this.label.setHorizontalAlignment(alignment);
/* 274 */     if (old != getHorizontalAlignment()) {
/* 275 */       layoutSeparator();
/*     */     }
/* 277 */     firePropertyChange("horizontalAlignment", old, getHorizontalAlignment());
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
/*     */   public int getHorizontalAlignment()
/*     */   {
/* 295 */     return this.label.getHorizontalAlignment();
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
/*     */   public void setHorizontalTextPosition(int position)
/*     */   {
/* 313 */     int old = getHorizontalTextPosition();
/* 314 */     this.label.setHorizontalTextPosition(position);
/* 315 */     firePropertyChange("horizontalTextPosition", old, getHorizontalTextPosition());
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
/*     */   public int getHorizontalTextPosition()
/*     */   {
/* 333 */     return this.label.getHorizontalTextPosition();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComponentOrientation getComponentOrientation()
/*     */   {
/* 341 */     return this.label.getComponentOrientation();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setComponentOrientation(ComponentOrientation o)
/*     */   {
/* 349 */     ComponentOrientation old = this.label.getComponentOrientation();
/* 350 */     this.label.setComponentOrientation(o);
/* 351 */     firePropertyChange("componentOrientation", old, this.label.getComponentOrientation());
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
/*     */   public void setIcon(Icon icon)
/*     */   {
/* 364 */     Icon old = getIcon();
/* 365 */     this.label.setIcon(icon);
/* 366 */     firePropertyChange("icon", old, getIcon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getIcon()
/*     */   {
/* 377 */     return this.label.getIcon();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForeground(Color foreground)
/*     */   {
/* 385 */     if (this.label != null) {
/* 386 */       this.label.setForeground(foreground);
/*     */     }
/* 388 */     super.setForeground(foreground);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFont(Font font)
/*     */   {
/* 396 */     if (this.label != null) {
/* 397 */       this.label.setFont(font);
/*     */     }
/* 399 */     super.setFont(font);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTitledSeparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */