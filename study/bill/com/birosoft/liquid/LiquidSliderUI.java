/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSliderUI;
/*     */ import javax.swing.plaf.basic.BasicSliderUI.TrackListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidSliderUI
/*     */   extends BasicSliderUI
/*     */ {
/*     */   private static Skin skinThumbHoriz;
/*     */   private static Skin skinThumbVert;
/*     */   private static Skin skinHorizSlider;
/*     */   private static Skin skinVertSlider;
/*     */   private Skin skinSlider;
/*  40 */   private SkinSimpleButtonIndexModel skinIndexModel = new SkinSimpleButtonIndexModel();
/*  41 */   protected boolean isRollover = false;
/*  42 */   protected boolean wasRollover = false;
/*  43 */   protected boolean isDragging = false;
/*     */   protected BasicSliderUI.TrackListener trackListener;
/*     */   
/*     */   public LiquidSliderUI() {
/*  47 */     super(null);
/*     */   }
/*     */   
/*     */   protected BasicSliderUI.TrackListener createTrackListener(JSlider slider) {
/*  51 */     return new MyTrackListener();
/*     */   }
/*     */   
/*     */   protected Dimension getThumbSize() {
/*  55 */     Dimension size = getSkinThumb().getSize();
/*     */     
/*  57 */     return size;
/*     */   }
/*     */   
/*     */   public void paintThumb(Graphics g) {
/*  61 */     Rectangle knobBounds = this.thumbRect;
/*     */     
/*  63 */     int index = this.skinIndexModel.getIndexForState(this.slider.isEnabled(), this.isRollover, this.isDragging);
/*     */     
/*  65 */     getSkinThumb().drawCentered(g, index, knobBounds.x, knobBounds.y, knobBounds.width, knobBounds.height);
/*     */   }
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  70 */     return new LiquidSliderUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getTrackWidth()
/*     */   {
/*  79 */     double kIdealTrackWidth = 7.0D;
/*  80 */     double kIdealThumbHeight = 16.0D;
/*  81 */     double kWidthScalar = 0.4375D;
/*     */     
/*  83 */     if (this.slider.getOrientation() == 0) {
/*  84 */       return (int)(0.4375D * this.thumbRect.height);
/*     */     }
/*  86 */     return (int)(0.4375D * this.thumbRect.width);
/*     */   }
/*     */   
/*     */   protected int getThumbOverhang()
/*     */   {
/*  91 */     if (this.slider.getOrientation() == 1) {
/*  92 */       return (int)(getThumbSize().getWidth() - getTrackWidth()) / 2;
/*     */     }
/*  94 */     return (int)(getThumbSize().getHeight() - getTrackWidth()) / 2;
/*     */   }
/*     */   
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/*  99 */     c.setOpaque(false);
/* 100 */     g.setColor(new Color(0, 0, 0, 0));
/* 101 */     g.fillRect(0, 0, c.getWidth(), c.getHeight());
/* 102 */     super.paint(g, c);
/*     */   }
/*     */   
/*     */   public void paintTicks(Graphics g) {
/* 106 */     Rectangle tickBounds = this.tickRect;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 111 */     int w = tickBounds.width;
/* 112 */     int h = tickBounds.height;
/*     */     
/*     */ 
/* 115 */     boolean leftToRight = this.slider.getComponentOrientation().isLeftToRight();
/*     */     
/* 117 */     g.setColor(new Color(0, 0, 0, 0));
/* 118 */     g.fillRect(tickBounds.x, tickBounds.y, tickBounds.width, tickBounds.height);
/*     */     
/* 120 */     g.setColor(Color.black);
/*     */     
/* 122 */     int maj = this.slider.getMajorTickSpacing();
/* 123 */     int min = this.slider.getMinorTickSpacing();
/*     */     
/* 125 */     if (this.slider.getOrientation() == 0) {
/* 126 */       g.translate(0, tickBounds.y);
/*     */       
/* 128 */       int value = this.slider.getMinimum();
/* 129 */       int xPos = 0;
/*     */       
/* 131 */       if (this.slider.getMinorTickSpacing() > 0) {
/* 132 */         while (value <= this.slider.getMaximum()) {
/* 133 */           xPos = xPositionForValue(value);
/* 134 */           paintMinorTickForHorizSlider(g, tickBounds, xPos);
/* 135 */           value += this.slider.getMinorTickSpacing();
/*     */         }
/*     */       }
/*     */       
/* 139 */       if (this.slider.getMajorTickSpacing() > 0) {
/* 140 */         value = this.slider.getMinimum();
/*     */         
/* 142 */         while (value <= this.slider.getMaximum()) {
/* 143 */           xPos = xPositionForValue(value);
/* 144 */           paintMajorTickForHorizSlider(g, tickBounds, xPos);
/* 145 */           value += this.slider.getMajorTickSpacing();
/*     */         }
/*     */       }
/*     */       
/* 149 */       g.translate(0, -tickBounds.y);
/*     */     } else {
/* 151 */       g.translate(tickBounds.x, 0);
/*     */       
/* 153 */       int value = this.slider.getMinimum();
/* 154 */       int yPos = 0;
/*     */       
/* 156 */       if (this.slider.getMinorTickSpacing() > 0) {
/* 157 */         int offset = 0;
/*     */         
/* 159 */         if (!leftToRight) {
/* 160 */           offset = tickBounds.width - tickBounds.width / 2;
/* 161 */           g.translate(offset, 0);
/*     */         }
/*     */         
/* 164 */         while (value <= this.slider.getMaximum()) {
/* 165 */           yPos = yPositionForValue(value);
/* 166 */           paintMinorTickForVertSlider(g, tickBounds, yPos);
/* 167 */           value += this.slider.getMinorTickSpacing();
/*     */         }
/*     */         
/* 170 */         if (!leftToRight) {
/* 171 */           g.translate(-offset, 0);
/*     */         }
/*     */       }
/*     */       
/* 175 */       if (this.slider.getMajorTickSpacing() > 0) {
/* 176 */         value = this.slider.getMinimum();
/*     */         
/* 178 */         if (!leftToRight) {
/* 179 */           g.translate(2, 0);
/*     */         }
/*     */         
/* 182 */         while (value <= this.slider.getMaximum()) {
/* 183 */           yPos = yPositionForValue(value);
/* 184 */           paintMajorTickForVertSlider(g, tickBounds, yPos);
/* 185 */           value += this.slider.getMajorTickSpacing();
/*     */         }
/*     */         
/* 188 */         if (!leftToRight) {
/* 189 */           g.translate(-2, 0);
/*     */         }
/*     */       }
/*     */       
/* 193 */       g.translate(-tickBounds.x, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void paintTrack(Graphics g) {
/* 198 */     Color trackColor = Color.red;
/*     */     
/* 200 */     boolean leftToRight = this.slider.getComponentOrientation().isLeftToRight();
/*     */     
/* 202 */     g.translate(this.trackRect.x, this.trackRect.y);
/*     */     
/* 204 */     int trackLeft = 0;
/* 205 */     int trackTop = 0;
/* 206 */     int trackRight = 0;
/* 207 */     int trackBottom = 0;
/*     */     
/*     */ 
/* 210 */     if (this.slider.getOrientation() == 0) {
/* 211 */       trackBottom = this.trackRect.height - 1 - getThumbOverhang();
/* 212 */       trackTop = trackBottom - (getTrackWidth() - 1);
/* 213 */       trackRight = this.trackRect.width - 1;
/*     */       
/* 215 */       int h = (trackBottom - trackTop - getSkinHorizSlider().getVsize()) / 2;
/* 216 */       getSkinHorizSlider().draw(g, 0, trackLeft, trackTop + h, trackRight - trackLeft, getSkinHorizSlider().getVsize());
/*     */     }
/*     */     else {
/* 219 */       if (leftToRight) {
/* 220 */         trackLeft = this.trackRect.width - getThumbOverhang() - getTrackWidth();
/*     */         
/* 222 */         trackRight = this.trackRect.width - getThumbOverhang() - 1;
/*     */       } else {
/* 224 */         trackLeft = getThumbOverhang();
/* 225 */         trackRight = getThumbOverhang() + getTrackWidth() - 1;
/*     */       }
/*     */       
/* 228 */       trackBottom = this.trackRect.height - 1;
/*     */       
/* 230 */       int w = (trackRight - trackLeft - getSkinVertSlider().getHsize()) / 2;
/* 231 */       getSkinVertSlider().draw(g, 0, trackLeft + w, trackTop, getSkinVertSlider().getHsize(), trackBottom - trackTop);
/*     */     }
/*     */     
/*     */ 
/* 235 */     g.translate(-this.trackRect.x, -this.trackRect.y);
/*     */   }
/*     */   
/*     */   protected void paintMinorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x)
/*     */   {
/* 240 */     g.setColor(LiquidLookAndFeel.getDarkControl());
/* 241 */     g.drawLine(x, 0, x, tickBounds.height / 2 - 1);
/*     */   }
/*     */   
/*     */   protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x)
/*     */   {
/* 246 */     g.setColor(LiquidLookAndFeel.getDarkControl());
/* 247 */     g.drawLine(x, 0, x, tickBounds.height - 2);
/*     */   }
/*     */   
/*     */   protected void paintMinorTickForVertSlider(Graphics g, Rectangle tickBounds, int y)
/*     */   {
/* 252 */     g.setColor(LiquidLookAndFeel.getDarkControl());
/* 253 */     g.drawLine(0, y, tickBounds.width / 2 - 1, y);
/*     */   }
/*     */   
/*     */   protected void paintMajorTickForVertSlider(Graphics g, Rectangle tickBounds, int y)
/*     */   {
/* 258 */     g.setColor(LiquidLookAndFeel.getDarkControl());
/* 259 */     g.drawLine(0, y, tickBounds.width - 2, y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinHorizSlider()
/*     */   {
/* 267 */     if (skinHorizSlider == null) {
/* 268 */       skinHorizSlider = new Skin("sliderhorizbackground.png", 1, 6, 0, 6, 0);
/*     */     }
/*     */     
/*     */ 
/* 272 */     return skinHorizSlider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinThumbHoriz()
/*     */   {
/* 280 */     if (skinThumbHoriz == null) {
/* 281 */       skinThumbHoriz = new Skin("sliderhoriz.png", 4, 0);
/*     */     }
/*     */     
/* 284 */     return skinThumbHoriz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinThumbVert()
/*     */   {
/* 292 */     if (skinThumbVert == null) {
/* 293 */       skinThumbVert = new Skin("slidervert.png", 4, 0);
/*     */     }
/*     */     
/* 296 */     return skinThumbVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinVertSlider()
/*     */   {
/* 304 */     if (skinVertSlider == null) {
/* 305 */       skinVertSlider = new Skin("slidervertbackground.png", 1, 0, 6, 0, 6);
/*     */     }
/*     */     
/* 308 */     return skinVertSlider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin getSkinThumb()
/*     */   {
/* 316 */     if (this.skinSlider == null) {
/* 317 */       this.skinSlider = (this.slider.getOrientation() == 0 ? getSkinThumbHoriz() : getSkinThumbVert());
/*     */     }
/*     */     
/*     */ 
/* 321 */     return this.skinSlider;
/*     */   }
/*     */   
/*     */ 
/*     */   class MyTrackListener
/*     */     extends BasicSliderUI.TrackListener
/*     */   {
/* 328 */     MyTrackListener() { super(); }
/*     */     
/* 330 */     public void mouseReleased(MouseEvent e) { super.mouseReleased(e);
/* 331 */       LiquidSliderUI.this.isDragging = false;
/* 332 */       LiquidSliderUI.this.slider.repaint();
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 336 */       super.mousePressed(e);
/*     */       
/* 338 */       if (LiquidSliderUI.this.thumbRect.contains(e.getX(), e.getY())) {
/* 339 */         LiquidSliderUI.this.isDragging = true;
/*     */       }
/*     */       
/* 342 */       LiquidSliderUI.this.slider.repaint();
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {
/* 346 */       LiquidSliderUI.this.isRollover = false;
/* 347 */       LiquidSliderUI.this.wasRollover = false;
/*     */       
/* 349 */       if (LiquidSliderUI.this.thumbRect.contains(e.getX(), e.getY())) {
/* 350 */         LiquidSliderUI.this.isRollover = true;
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 355 */       LiquidSliderUI.this.isRollover = false;
/*     */       
/* 357 */       if (LiquidSliderUI.this.isRollover != LiquidSliderUI.this.wasRollover) {
/* 358 */         LiquidSliderUI.this.slider.repaint();
/* 359 */         LiquidSliderUI.this.wasRollover = LiquidSliderUI.this.isRollover;
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 364 */       if (LiquidSliderUI.this.thumbRect.contains(e.getX(), e.getY())) {
/* 365 */         LiquidSliderUI.this.isRollover = true;
/*     */       }
/*     */       
/* 368 */       super.mouseDragged(e);
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 372 */       if (LiquidSliderUI.this.thumbRect.contains(e.getX(), e.getY())) {
/* 373 */         LiquidSliderUI.this.isRollover = true;
/*     */         
/* 375 */         if (LiquidSliderUI.this.isRollover != LiquidSliderUI.this.wasRollover) {
/* 376 */           LiquidSliderUI.this.slider.repaint();
/* 377 */           LiquidSliderUI.this.wasRollover = LiquidSliderUI.this.isRollover;
/*     */         }
/*     */       } else {
/* 380 */         LiquidSliderUI.this.isRollover = false;
/*     */         
/* 382 */         if (LiquidSliderUI.this.isRollover != LiquidSliderUI.this.wasRollover) {
/* 383 */           LiquidSliderUI.this.slider.repaint();
/* 384 */           LiquidSliderUI.this.wasRollover = LiquidSliderUI.this.isRollover;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidSliderUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */