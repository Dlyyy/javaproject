/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLayoutPainter<T>
/*     */   extends AbstractPainter<T>
/*     */ {
/*  79 */   private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;
/*  80 */   private HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
/*  81 */   private Insets insets = new Insets(0, 0, 0, 0);
/*  82 */   private boolean fillVertical = false;
/*  83 */   private boolean fillHorizontal = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum HorizontalAlignment
/*     */   {
/*  94 */     LEFT,  CENTER,  RIGHT;
/*     */     
/*     */     private HorizontalAlignment() {}
/*     */   }
/*     */   
/*  99 */   public static enum VerticalAlignment { TOP,  CENTER,  BOTTOM;
/*     */     
/*     */ 
/*     */     private VerticalAlignment() {}
/*     */   }
/*     */   
/*     */   public HorizontalAlignment getHorizontalAlignment()
/*     */   {
/* 107 */     return this.horizontalAlignment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Insets getInsets()
/*     */   {
/* 115 */     return this.insets;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public VerticalAlignment getVerticalAlignment()
/*     */   {
/* 124 */     return this.verticalAlignment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFillHorizontal()
/*     */   {
/* 133 */     return this.fillHorizontal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFillVertical()
/*     */   {
/* 142 */     return this.fillVertical;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHorizontalAlignment(HorizontalAlignment horizontal)
/*     */   {
/* 151 */     HorizontalAlignment old = getHorizontalAlignment();
/* 152 */     this.horizontalAlignment = horizontal;
/* 153 */     setDirty(true);
/* 154 */     firePropertyChange("horizontalAlignment", old, getHorizontalAlignment());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFillHorizontal(boolean fillHorizontal)
/*     */   {
/* 164 */     boolean old = isFillHorizontal();
/* 165 */     this.fillHorizontal = fillHorizontal;
/* 166 */     setDirty(true);
/* 167 */     firePropertyChange("fillHorizontal", Boolean.valueOf(old), Boolean.valueOf(isFillHorizontal()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInsets(Insets insets)
/*     */   {
/* 175 */     Insets old = getInsets();
/* 176 */     this.insets = insets;
/* 177 */     setDirty(true);
/* 178 */     firePropertyChange("insets", old, getInsets());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVerticalAlignment(VerticalAlignment vertical)
/*     */   {
/* 187 */     VerticalAlignment old = getVerticalAlignment();
/* 188 */     this.verticalAlignment = vertical;
/* 189 */     setDirty(true);
/* 190 */     firePropertyChange("verticalAlignment", old, getVerticalAlignment());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFillVertical(boolean verticalStretch)
/*     */   {
/* 200 */     boolean old = isFillVertical();
/* 201 */     this.fillVertical = verticalStretch;
/* 202 */     setDirty(true);
/* 203 */     firePropertyChange("fillVertical", Boolean.valueOf(old), Boolean.valueOf(isFillVertical()));
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
/*     */   protected final Rectangle calculateLayout(int contentWidth, int contentHeight, int width, int height)
/*     */   {
/* 221 */     Rectangle rect = new Rectangle();
/* 222 */     rect.width = contentWidth;
/* 223 */     rect.height = contentHeight;
/*     */     
/* 225 */     if (isFillHorizontal()) {
/* 226 */       rect.width = (width - this.insets.left - this.insets.right);
/*     */     }
/*     */     
/* 229 */     if (isFillVertical()) {
/* 230 */       rect.height = (height - this.insets.top - this.insets.bottom);
/*     */     }
/* 232 */     rect.x = calculateX(rect.width, width);
/* 233 */     rect.y = calculateY(rect.height, height);
/* 234 */     return rect;
/*     */   }
/*     */   
/*     */   private int calculateY(int imgHeight, int height) {
/* 238 */     int y = 0;
/* 239 */     if (getVerticalAlignment() == VerticalAlignment.TOP) {
/* 240 */       y = 0;
/* 241 */       y += this.insets.top;
/*     */     }
/* 243 */     if (getVerticalAlignment() == VerticalAlignment.CENTER) {
/* 244 */       y = (height - imgHeight) / 2;
/* 245 */       y += this.insets.top;
/*     */     }
/* 247 */     if (getVerticalAlignment() == VerticalAlignment.BOTTOM) {
/* 248 */       y = height - imgHeight;
/* 249 */       y -= this.insets.bottom;
/*     */     }
/* 251 */     return y;
/*     */   }
/*     */   
/*     */   private int calculateX(int imgWidth, int width) {
/* 255 */     int x = 0;
/* 256 */     if (getHorizontalAlignment() == HorizontalAlignment.LEFT) {
/* 257 */       x = 0;
/* 258 */       x += this.insets.left;
/*     */     }
/* 260 */     if (getHorizontalAlignment() == HorizontalAlignment.CENTER) {
/* 261 */       x = (width - imgWidth) / 2;
/* 262 */       x += this.insets.left;
/*     */     }
/* 264 */     if (getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
/* 265 */       x = width - imgWidth;
/* 266 */       x -= this.insets.right;
/*     */     }
/* 268 */     return x;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\AbstractLayoutPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */