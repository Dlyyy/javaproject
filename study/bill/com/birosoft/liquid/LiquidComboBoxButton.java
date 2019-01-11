/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.DefaultButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidComboBoxButton
/*     */   extends JButton
/*     */ {
/*     */   protected JComboBox comboBox;
/*     */   protected JList listBox;
/*     */   protected CellRendererPane rendererPane;
/*     */   protected Icon comboIcon;
/*  45 */   protected boolean iconOnly = false;
/*     */   BufferedImage focusImg;
/*     */   
/*     */   public final JComboBox getComboBox()
/*     */   {
/*  50 */     return this.comboBox;
/*     */   }
/*     */   
/*     */   public final void setComboBox(JComboBox cb) {
/*  54 */     this.comboBox = cb;
/*     */   }
/*     */   
/*     */   public final Icon getComboIcon()
/*     */   {
/*  59 */     return this.comboIcon;
/*     */   }
/*     */   
/*     */   public final void setComboIcon(Icon i) {
/*  63 */     this.comboIcon = i;
/*     */   }
/*     */   
/*     */   public final boolean isIconOnly()
/*     */   {
/*  68 */     return this.iconOnly;
/*     */   }
/*     */   
/*     */   public final void setIconOnly(boolean isIconOnly) {
/*  72 */     this.iconOnly = isIconOnly;
/*     */   }
/*     */   
/*     */   LiquidComboBoxButton()
/*     */   {
/*  77 */     super("");
/*  78 */     DefaultButtonModel model = new DefaultButtonModel()
/*     */     {
/*     */       public void setArmed(boolean armed)
/*     */       {
/*  82 */         super.setArmed(isPressed() ? true : armed);
/*     */       }
/*     */       
/*  85 */     };
/*  86 */     setModel(model);
/*  87 */     setOpaque(false);
/*     */     
/*     */ 
/*  90 */     setBackground(UIManager.getColor("ComboBox.background"));
/*  91 */     setForeground(UIManager.getColor("ComboBox.foreground"));
/*     */     
/*  93 */     ImageIcon icon = LiquidLookAndFeel.loadIcon("comboboxfocus.png", this);
/*  94 */     this.focusImg = new BufferedImage(2, 2, 1);
/*  95 */     Graphics g3 = this.focusImg.getGraphics();
/*  96 */     icon.paintIcon(this, g3, 0, 0);
/*     */   }
/*     */   
/*     */   public LiquidComboBoxButton(JComboBox cb, Icon i, CellRendererPane pane, JList list)
/*     */   {
/* 101 */     this();
/* 102 */     this.comboBox = cb;
/* 103 */     this.comboIcon = i;
/* 104 */     this.rendererPane = pane;
/* 105 */     this.listBox = list;
/* 106 */     setEnabled(this.comboBox.isEnabled());
/*     */   }
/*     */   
/*     */   public LiquidComboBoxButton(JComboBox cb, Icon i, boolean onlyIcon, CellRendererPane pane, JList list)
/*     */   {
/* 111 */     this(cb, i, pane, list);
/*     */   }
/*     */   
/*     */ 
/* 115 */   SkinSimpleButtonIndexModel indexModel = new SkinSimpleButtonIndexModel();
/*     */   Skin skinArrow;
/*     */   Skin skinButton;
/*     */   
/*     */   public int getIndexForState()
/*     */   {
/* 121 */     return this.indexModel.getIndexForState(this.model.isEnabled(), this.model.isRollover(), (this.model.isArmed()) && ((this.model.isPressed() | this.model.isSelected())));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintComponent(Graphics g)
/*     */   {
/* 132 */     boolean leftToRight = getComponentOrientation().isLeftToRight();
/*     */     
/* 134 */     int index = this.indexModel.getIndexForState(this.model.isEnabled(), this.model.isRollover(), (this.model.isArmed()) && ((this.model.isPressed() | this.model.isSelected())));
/*     */     
/*     */ 
/* 137 */     index = (this.comboBox.hasFocus()) && (!this.iconOnly) ? 1 : index;
/*     */     
/*     */ 
/* 140 */     Skin arrowSkin = getSkinArrow();
/*     */     
/* 142 */     int middle = (getHeight() - arrowSkin.getVsize()) / 2;
/* 143 */     arrowSkin.draw(g, index, getWidth() - arrowSkin.getHsize() - 6, middle, arrowSkin.getHsize(), arrowSkin.getVsize());
/*     */     
/*     */ 
/*     */ 
/* 147 */     Insets insets = new Insets(0, 12, 2, 2);
/*     */     
/* 149 */     int width = getWidth() - (insets.left + insets.right);
/* 150 */     int widthFocus = width;
/* 151 */     int height = getHeight() - (insets.top + insets.bottom);
/*     */     
/* 153 */     if ((height <= 0) || (width <= 0))
/*     */     {
/* 155 */       return;
/*     */     }
/*     */     
/* 158 */     int left = insets.left;
/* 159 */     int top = insets.top;
/* 160 */     int right = left + (width - 1);
/* 161 */     int bottom = top + (height - 1);
/*     */     
/* 163 */     int iconWidth = LiquidComboBoxUI.comboBoxButtonSize;
/* 164 */     int iconLeft = leftToRight ? right : left;
/*     */     
/*     */ 
/* 167 */     Component c = null;
/* 168 */     boolean mustResetOpaque = false;
/* 169 */     boolean savedOpaque = false;
/* 170 */     boolean paintFocus = this.comboBox.hasFocus();
/* 171 */     if ((!this.iconOnly) && (this.comboBox != null))
/*     */     {
/* 173 */       ListCellRenderer renderer = this.comboBox.getRenderer();
/* 174 */       boolean renderPressed = getModel().isPressed();
/* 175 */       c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, renderPressed, false);
/* 176 */       c.setFont(this.rendererPane.getFont());
/*     */       
/* 178 */       if ((this.model.isArmed()) && (this.model.isPressed()))
/*     */       {
/* 180 */         if (isOpaque())
/*     */         {
/* 182 */           c.setBackground(UIManager.getColor("Button.select"));
/*     */         }
/* 184 */         c.setForeground(this.comboBox.getForeground());
/* 185 */       } else if (!this.comboBox.isEnabled())
/*     */       {
/* 187 */         if (isOpaque())
/*     */         {
/* 189 */           c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
/*     */         }
/* 191 */         c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
/*     */       }
/*     */       else {
/* 194 */         c.setForeground(this.comboBox.getForeground());
/* 195 */         c.setBackground(this.comboBox.getBackground());
/*     */       }
/* 197 */       if ((!mustResetOpaque) && ((c instanceof JComponent)))
/*     */       {
/* 199 */         mustResetOpaque = true;
/* 200 */         JComponent jc = (JComponent)c;
/* 201 */         savedOpaque = jc.isOpaque();
/* 202 */         jc.setOpaque(false);
/*     */       }
/*     */       
/* 205 */       int cWidth = width - (insets.right + iconWidth);
/*     */       
/*     */ 
/* 208 */       boolean shouldValidate = false;
/* 209 */       if ((c instanceof JPanel))
/*     */       {
/* 211 */         shouldValidate = true;
/*     */       }
/*     */       
/* 214 */       if (leftToRight)
/*     */       {
/* 216 */         this.rendererPane.paintComponent(g, c, this, left, top, cWidth, height, shouldValidate);
/*     */       }
/*     */       else {
/* 219 */         this.rendererPane.paintComponent(g, c, this, left + iconWidth, top, cWidth, height, shouldValidate);
/*     */       }
/* 221 */       if (paintFocus)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */         Graphics2D g2d = (Graphics2D)g;
/* 236 */         Rectangle bounds = this.comboBox.getBounds();
/* 237 */         int offset = bounds.height / 2 - 6;
/* 238 */         g.setColor(new Color(196, 195, 194));
/* 239 */         g2d.drawLine(6, offset, 11, offset + 5);
/* 240 */         g.setColor(new Color(175, 174, 174));
/* 241 */         g2d.drawLine(6, offset + 1, 6, offset + 11);
/* 242 */         g2d.drawLine(6, offset + 11, 11, offset + 6);
/*     */       }
/*     */     }
/* 245 */     if (mustResetOpaque)
/*     */     {
/* 247 */       JComponent jc = (JComponent)c;
/* 248 */       jc.setOpaque(savedOpaque);
/*     */     }
/*     */   }
/*     */   
/*     */   public Skin getSkinArrow()
/*     */   {
/* 254 */     if (this.skinArrow == null)
/*     */     {
/* 256 */       this.skinArrow = new Skin("comboboxarrow.png", 4, 0);
/*     */     }
/*     */     
/* 259 */     return this.skinArrow;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidComboBoxButton.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */