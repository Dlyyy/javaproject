/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LiquidSplitPaneDivider
/*     */   extends BasicSplitPaneDivider
/*     */ {
/*  49 */   private int inset = 0;
/*     */   
/*  51 */   Skin skinVertSplitter = null;
/*  52 */   Skin skinHorzSplitter = null;
/*     */   
/*     */   public LiquidSplitPaneDivider(BasicSplitPaneUI ui)
/*     */   {
/*  56 */     super(ui);
/*  57 */     setLayout(new LiquidDividerLayout());
/*     */   }
/*     */   
/*     */   Skin getHorizontalSplitter()
/*     */   {
/*  62 */     if (this.skinHorzSplitter == null)
/*  63 */       this.skinHorzSplitter = new Skin("horizsplitter.png", 1, 5, 5, 5, 5);
/*  64 */     return this.skinHorzSplitter;
/*     */   }
/*     */   
/*     */   Skin getVerticalSplitter()
/*     */   {
/*  69 */     if (this.skinVertSplitter == null)
/*  70 */       this.skinVertSplitter = new Skin("vertsplitter.png", 1, 5, 5, 5, 5);
/*  71 */     return this.skinVertSplitter;
/*     */   }
/*     */   
/*     */   public void paint(Graphics g)
/*     */   {
/*  76 */     Dimension size = getSize();
/*  77 */     Rectangle clip = new Rectangle(size.width, size.height);
/*  78 */     Insets insets = getInsets();
/*  79 */     if (this.orientation == 1)
/*     */     {
/*  81 */       int xOffset = (clip.width - 5) / 2;
/*  82 */       getHorizontalSplitter().draw(g, 0, clip.x + xOffset, clip.y + 1, 5, clip.height - 1);
/*     */     }
/*     */     else {
/*  85 */       int yOffset = (clip.height - 5) / 2;
/*  86 */       getVerticalSplitter().draw(g, 0, clip.x + 1, clip.y + yOffset, clip.width - 1, 5);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     super.paint(g);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createLeftOneTouchButton()
/*     */   {
/* 113 */     JButton b = new JButton()
/*     */     {
/*     */ 
/* 116 */       int[][] buffer = { { 0, 0, 0, 2, 2, 0, 0, 0, 0 }, { 0, 0, 2, 1, 1, 1, 0, 0, 0 }, { 0, 2, 1, 1, 1, 1, 1, 0, 0 }, { 2, 1, 1, 1, 1, 1, 1, 1, 0 }, { 0, 3, 3, 3, 3, 3, 3, 3, 3 } };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void setBorder(Border b) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void paint(Graphics g)
/*     */       {
/* 130 */         JSplitPane splitPane = LiquidSplitPaneDivider.this.getSplitPaneFromSuper();
/* 131 */         if (splitPane != null)
/*     */         {
/* 133 */           int oneTouchSize = LiquidSplitPaneDivider.this.getOneTouchSizeFromSuper();
/* 134 */           int orientation = LiquidSplitPaneDivider.this.getOrientationFromSuper();
/* 135 */           int blockSize = Math.min(LiquidSplitPaneDivider.this.getDividerSize(), oneTouchSize);
/*     */           
/*     */ 
/* 138 */           Color[] colors = { getBackground(), (Color)LiquidLookAndFeel.uiDefaults.get("controlDkShadow"), (Color)LiquidLookAndFeel.uiDefaults.get("info"), (Color)LiquidLookAndFeel.uiDefaults.get("controlHighlight") };
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */           g.setColor(getBackground());
/*     */           
/*     */ 
/*     */ 
/* 150 */           if (getModel().isPressed())
/*     */           {
/*     */ 
/* 153 */             colors[1] = colors[2];
/*     */           }
/* 155 */           if (orientation == 0)
/*     */           {
/*     */ 
/* 158 */             for (int i = 1; i <= this.buffer[0].length; i++)
/*     */             {
/* 160 */               for (int j = 1; j < blockSize; j++)
/*     */               {
/* 162 */                 if (this.buffer[(j - 1)][(i - 1)] != 0)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/* 167 */                   g.setColor(colors[this.buffer[(j - 1)][(i - 1)]]);
/*     */                   
/* 169 */                   g.drawLine(i, j, i, j);
/*     */ 
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 180 */             for (int i = 1; i <= this.buffer[0].length; i++)
/*     */             {
/* 182 */               for (int j = 1; j < blockSize; j++)
/*     */               {
/* 184 */                 if (this.buffer[(j - 1)][(i - 1)] != 0)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */                   g.setColor(colors[this.buffer[(j - 1)][(i - 1)]]);
/*     */                   
/*     */ 
/* 196 */                   g.drawLine(j, i, j, i);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean isFocusTraversable()
/*     */       {
/* 206 */         return false;
/*     */       }
/* 208 */     };
/* 209 */     b.setRequestFocusEnabled(false);
/* 210 */     b.setCursor(Cursor.getPredefinedCursor(0));
/* 211 */     b.setFocusPainted(false);
/* 212 */     b.setBorderPainted(false);
/* 213 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createRightOneTouchButton()
/*     */   {
/* 223 */     JButton b = new JButton()
/*     */     {
/*     */ 
/* 226 */       int[][] buffer = { { 2, 2, 2, 2, 2, 2, 2, 2 }, { 0, 1, 1, 1, 1, 1, 1, 3 }, { 0, 0, 1, 1, 1, 1, 3, 0 }, { 0, 0, 0, 1, 1, 3, 0, 0 }, { 0, 0, 0, 0, 3, 0, 0, 0 } };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void setBorder(Border border) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void paint(Graphics g)
/*     */       {
/* 240 */         JSplitPane splitPane = LiquidSplitPaneDivider.this.getSplitPaneFromSuper();
/* 241 */         if (splitPane != null)
/*     */         {
/* 243 */           int oneTouchSize = LiquidSplitPaneDivider.this.getOneTouchSizeFromSuper();
/* 244 */           int orientation = LiquidSplitPaneDivider.this.getOrientationFromSuper();
/* 245 */           int blockSize = Math.min(LiquidSplitPaneDivider.this.getDividerSize(), oneTouchSize);
/*     */           
/*     */ 
/* 248 */           Color[] colors = { getBackground(), (Color)LiquidLookAndFeel.uiDefaults.get("controlDkShadow"), (Color)LiquidLookAndFeel.uiDefaults.get("info"), (Color)LiquidLookAndFeel.uiDefaults.get("controlHighlight") };
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 256 */           g.setColor(getBackground());
/*     */           
/*     */ 
/*     */ 
/* 260 */           if (getModel().isPressed())
/*     */           {
/*     */ 
/* 263 */             colors[1] = colors[2];
/*     */           }
/* 265 */           if (orientation == 0)
/*     */           {
/*     */ 
/* 268 */             for (int i = 1; i <= this.buffer[0].length; i++)
/*     */             {
/* 270 */               for (int j = 1; j < blockSize; j++)
/*     */               {
/* 272 */                 if (this.buffer[(j - 1)][(i - 1)] != 0)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/* 277 */                   g.setColor(colors[this.buffer[(j - 1)][(i - 1)]]);
/*     */                   
/* 279 */                   g.drawLine(i, j, i, j);
/*     */ 
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 290 */             for (int i = 1; i <= this.buffer[0].length; i++)
/*     */             {
/* 292 */               for (int j = 1; j < blockSize; j++)
/*     */               {
/* 294 */                 if (this.buffer[(j - 1)][(i - 1)] != 0)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 303 */                   g.setColor(colors[this.buffer[(j - 1)][(i - 1)]]);
/*     */                   
/*     */ 
/* 306 */                   g.drawLine(j, i, j, i);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean isFocusTraversable()
/*     */       {
/* 316 */         return false;
/*     */       }
/* 318 */     };
/* 319 */     b.setCursor(Cursor.getPredefinedCursor(0));
/* 320 */     b.setFocusPainted(false);
/* 321 */     b.setBorderPainted(false);
/* 322 */     b.setRequestFocusEnabled(false);
/* 323 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public class LiquidDividerLayout
/*     */     implements LayoutManager
/*     */   {
/*     */     public LiquidDividerLayout() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void layoutContainer(Container c)
/*     */     {
/* 339 */       JButton leftButton = LiquidSplitPaneDivider.this.getLeftButtonFromSuper();
/* 340 */       JButton rightButton = LiquidSplitPaneDivider.this.getRightButtonFromSuper();
/* 341 */       JSplitPane splitPane = LiquidSplitPaneDivider.this.getSplitPaneFromSuper();
/* 342 */       int orientation = LiquidSplitPaneDivider.this.getOrientationFromSuper();
/* 343 */       int oneTouchSize = LiquidSplitPaneDivider.this.getOneTouchSizeFromSuper();
/* 344 */       int oneTouchOffset = LiquidSplitPaneDivider.this.getOneTouchOffsetFromSuper();
/* 345 */       Insets insets = LiquidSplitPaneDivider.this.getInsets();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */       if ((leftButton != null) && (rightButton != null) && (c == LiquidSplitPaneDivider.this))
/*     */       {
/* 353 */         if (splitPane.isOneTouchExpandable())
/*     */         {
/* 355 */           if (orientation == 0)
/*     */           {
/* 357 */             int extraY = insets != null ? insets.top : 0;
/* 358 */             int blockSize = LiquidSplitPaneDivider.this.getDividerSize();
/*     */             
/* 360 */             if (insets != null)
/*     */             {
/* 362 */               blockSize -= insets.top + insets.bottom;
/*     */             }
/* 364 */             blockSize = Math.min(blockSize, oneTouchSize);
/* 365 */             leftButton.setBounds(oneTouchOffset, extraY, blockSize * 2, blockSize);
/* 366 */             rightButton.setBounds(oneTouchOffset + oneTouchSize * 2, extraY, blockSize * 2, blockSize);
/*     */           }
/*     */           else {
/* 369 */             int blockSize = LiquidSplitPaneDivider.this.getDividerSize();
/* 370 */             int extraX = insets != null ? insets.left : 0;
/*     */             
/* 372 */             if (insets != null)
/*     */             {
/* 374 */               blockSize -= insets.left + insets.right;
/*     */             }
/* 376 */             blockSize = Math.min(blockSize, oneTouchSize);
/* 377 */             leftButton.setBounds(extraX, oneTouchOffset, blockSize, blockSize * 2);
/* 378 */             rightButton.setBounds(extraX, oneTouchOffset + oneTouchSize * 2, blockSize, blockSize * 2);
/*     */           }
/*     */         }
/*     */         else {
/* 382 */           leftButton.setBounds(-5, -5, 1, 1);
/* 383 */           rightButton.setBounds(-5, -5, 1, 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public Dimension minimumLayoutSize(Container c)
/*     */     {
/* 390 */       return new Dimension(0, 0);
/*     */     }
/*     */     
/*     */     public Dimension preferredLayoutSize(Container c)
/*     */     {
/* 395 */       return new Dimension(0, 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void removeLayoutComponent(Component c) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void addLayoutComponent(String string, Component c) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int getOneTouchSizeFromSuper()
/*     */   {
/* 415 */     return 6;
/*     */   }
/*     */   
/*     */   int getOneTouchOffsetFromSuper()
/*     */   {
/* 420 */     return 2;
/*     */   }
/*     */   
/*     */   int getOrientationFromSuper()
/*     */   {
/* 425 */     return this.orientation;
/*     */   }
/*     */   
/*     */   JSplitPane getSplitPaneFromSuper()
/*     */   {
/* 430 */     return this.splitPane;
/*     */   }
/*     */   
/*     */   JButton getLeftButtonFromSuper()
/*     */   {
/* 435 */     return this.leftButton;
/*     */   }
/*     */   
/*     */   JButton getRightButtonFromSuper()
/*     */   {
/* 440 */     return this.rightButton;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidSplitPaneDivider.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */