/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BoundedRangeModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI.TrackListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XTraScrollBarUI
/*     */   extends BasicScrollBarUI
/*     */ {
/*     */   protected boolean useAlternateLayout;
/*     */   
/*     */   public XTraScrollBarUI()
/*     */   {
/*  27 */     this.useAlternateLayout = Boolean.TRUE.equals(UIManager.get("ScrollBar.alternateLayout"));
/*     */   }
/*     */   
/*  30 */   public static ComponentUI createUI(JComponent x) { return new XTraScrollBarUI(); }
/*     */   
/*     */   protected void layoutVScrollbar(JScrollBar sb)
/*     */   {
/*  34 */     if (this.useAlternateLayout) {
/*  35 */       alternateLayoutVScrollbar(sb);
/*     */     } else {
/*  37 */       super.layoutVScrollbar(sb);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void alternateLayoutVScrollbar(JScrollBar sb) {
/*  42 */     Dimension sbSize = sb.getSize();
/*  43 */     Insets sbInsets = sb.getInsets();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  48 */     int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
/*  49 */     int itemX = sbInsets.left;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  54 */     int incrButtonH = this.incrButton.getPreferredSize().height;
/*  55 */     int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
/*     */     
/*  57 */     int decrButtonH = this.decrButton.getPreferredSize().height;
/*  58 */     int decrButtonY = incrButtonY - decrButtonH;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  63 */     int sbInsetsH = sbInsets.top + sbInsets.bottom;
/*  64 */     int sbButtonsH = decrButtonH + incrButtonH;
/*  65 */     float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */     float min = sb.getMinimum();
/*  74 */     float extent = sb.getVisibleAmount();
/*  75 */     float range = sb.getMaximum() - min;
/*  76 */     float value = sb.getValue();
/*     */     
/*  78 */     int thumbH = range <= 0.0F ? getMaximumThumbSize().height : (int)(trackH * (extent / range));
/*     */     
/*  80 */     thumbH = Math.max(thumbH, getMinimumThumbSize().height);
/*  81 */     thumbH = Math.min(thumbH, getMaximumThumbSize().height);
/*     */     
/*  83 */     int thumbY = decrButtonY - thumbH;
/*  84 */     if (sb.getValue() < sb.getMaximum() - sb.getVisibleAmount()) {
/*  85 */       float thumbRange = trackH - thumbH;
/*  86 */       thumbY = (int)(0.5F + thumbRange * ((value - min) / (range - extent)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */     int sbAvailButtonH = sbSize.height - sbInsetsH;
/*  94 */     if (sbAvailButtonH < sbButtonsH) {
/*  95 */       incrButtonH = decrButtonH = sbAvailButtonH / 2;
/*  96 */       incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
/*     */     }
/*  98 */     this.decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
/*  99 */     this.incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
/*     */     
/*     */ 
/*     */ 
/* 103 */     int itrackY = sbInsets.top;
/* 104 */     int itrackH = decrButtonY - itrackY;
/* 105 */     this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */     if (thumbH >= (int)trackH) {
/* 112 */       setThumbBounds(0, 0, 0, 0);
/*     */     }
/*     */     else {
/* 115 */       if (thumbY + thumbH > decrButtonY) {
/* 116 */         thumbY = decrButtonY - thumbH;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 121 */       setThumbBounds(itemX, thumbY, itemW, thumbH);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void layoutHScrollbar(JScrollBar sb) {
/* 126 */     if (this.useAlternateLayout) {
/* 127 */       alternateLayoutHScrollbar(sb);
/*     */     } else {
/* 129 */       super.layoutHScrollbar(sb);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void alternateLayoutHScrollbar(JScrollBar sb) {
/* 134 */     Dimension sbSize = sb.getSize();
/* 135 */     Insets sbInsets = sb.getInsets();
/*     */     
/*     */ 
/*     */ 
/* 139 */     int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
/* 140 */     int itemY = sbInsets.top;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 145 */     int incrButtonW = this.incrButton.getPreferredSize().width;
/* 146 */     int incrButtonX = sbSize.width - (sbInsets.right + incrButtonW);
/*     */     
/* 148 */     int decrButtonW = this.decrButton.getPreferredSize().width;
/* 149 */     int decrButtonX = incrButtonX - decrButtonW;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */     int sbInsetsW = sbInsets.left + sbInsets.right;
/* 156 */     int sbButtonsW = decrButtonW + incrButtonW;
/* 157 */     float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */     float min = sb.getMinimum();
/* 166 */     float extent = sb.getVisibleAmount();
/* 167 */     float range = sb.getMaximum() - min;
/* 168 */     float value = sb.getValue();
/*     */     
/* 170 */     int thumbW = range <= 0.0F ? getMaximumThumbSize().width : (int)(trackW * (extent / range));
/*     */     
/* 172 */     thumbW = Math.max(thumbW, getMinimumThumbSize().width);
/* 173 */     thumbW = Math.min(thumbW, getMaximumThumbSize().width);
/*     */     
/* 175 */     int thumbX = decrButtonX - thumbW;
/* 176 */     if (sb.getValue() < sb.getMaximum() - sb.getVisibleAmount()) {
/* 177 */       float thumbRange = trackW - thumbW;
/* 178 */       thumbX = (int)(0.5F + thumbRange * ((value - min) / (range - extent)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 185 */     int sbAvailButtonW = sbSize.width - sbInsetsW;
/* 186 */     if (sbAvailButtonW < sbButtonsW) {
/* 187 */       incrButtonW = decrButtonW = sbAvailButtonW / 2;
/* 188 */       incrButtonX = sbSize.width - (sbInsets.right + incrButtonW);
/*     */     }
/*     */     
/* 191 */     this.decrButton.setBounds(decrButtonX, itemY, decrButtonW, itemH);
/* 192 */     this.incrButton.setBounds(incrButtonX, itemY, incrButtonW, itemH);
/*     */     
/*     */ 
/*     */ 
/* 196 */     int itrackX = sbInsets.left;
/* 197 */     int itrackW = decrButtonX - itrackX;
/* 198 */     this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 203 */     if (thumbW >= (int)trackW) {
/* 204 */       setThumbBounds(0, 0, 0, 0);
/*     */     }
/*     */     else {
/* 207 */       if (thumbX + thumbW > incrButtonX) {
/* 208 */         thumbX = incrButtonX - thumbW;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 213 */       setThumbBounds(thumbX, itemY, thumbW, itemH);
/*     */     }
/*     */   }
/*     */   
/*     */   protected BasicScrollBarUI.TrackListener createTrackListener() {
/* 218 */     if (this.useAlternateLayout) {
/* 219 */       return new MyTrackListener();
/*     */     }
/* 221 */     return super.createTrackListener();
/*     */   }
/*     */   
/*     */   public JButton decrButton() {
/* 225 */     return this.decrButton;
/*     */   }
/*     */   
/*     */   public JButton incrButton() {
/* 229 */     return this.incrButton;
/*     */   }
/*     */   
/*     */   public JScrollBar scrollbar() {
/* 233 */     return this.scrollbar;
/*     */   }
/*     */   
/*     */   public boolean isDragging() {
/* 237 */     return this.isDragging;
/*     */   }
/*     */   
/*     */   public Rectangle getThumbBounds() {
/* 241 */     return super.getThumbBounds();
/*     */   }
/*     */   
/*     */   public Rectangle getTrackBounds() {
/* 245 */     return super.getTrackBounds();
/*     */   }
/*     */   
/*     */   public void setThumbBounds(int x, int y, int w, int h) {
/* 249 */     super.setThumbBounds(x, y, w, h);
/*     */   }
/*     */   
/*     */   public class MyTrackListener extends BasicScrollBarUI.TrackListener
/*     */   {
/*     */     public MyTrackListener() {
/* 255 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void mouseDragged(MouseEvent e)
/*     */     {
/* 263 */       if ((!XTraScrollBarUI.this.scrollbar().isEnabled()) || (!XTraScrollBarUI.this.isDragging())) {
/* 264 */         return;
/*     */       }
/*     */       
/* 267 */       Insets sbInsets = XTraScrollBarUI.this.scrollbar().getInsets();
/*     */       
/* 269 */       BoundedRangeModel model = XTraScrollBarUI.this.scrollbar().getModel();
/* 270 */       Rectangle thumbR = XTraScrollBarUI.this.getThumbBounds();
/*     */       float trackLength;
/*     */       int thumbMin;
/*     */       int thumbMax;
/* 274 */       int thumbPos; float trackLength; if (XTraScrollBarUI.this.scrollbar().getOrientation() == 1) {
/* 275 */         int thumbMin = sbInsets.top;
/* 276 */         int thumbMax = XTraScrollBarUI.this.decrButton().getY() - XTraScrollBarUI.this.getThumbBounds().height;
/* 277 */         int thumbPos = Math.min(thumbMax, Math.max(thumbMin, e.getY() - this.offset));
/* 278 */         XTraScrollBarUI.this.setThumbBounds(thumbR.x, thumbPos, thumbR.width, thumbR.height);
/* 279 */         trackLength = XTraScrollBarUI.this.getTrackBounds().height;
/*     */       }
/*     */       else {
/* 282 */         thumbMin = sbInsets.left;
/* 283 */         thumbMax = XTraScrollBarUI.this.decrButton().getX() - XTraScrollBarUI.this.getThumbBounds().width;
/* 284 */         thumbPos = Math.min(thumbMax, Math.max(thumbMin, e.getX() - this.offset));
/* 285 */         XTraScrollBarUI.this.setThumbBounds(thumbPos, thumbR.y, thumbR.width, thumbR.height);
/* 286 */         trackLength = XTraScrollBarUI.this.getTrackBounds().width;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */       if (thumbPos == thumbMax) {
/* 294 */         XTraScrollBarUI.this.scrollbar().setValue(model.getMaximum() - model.getExtent());
/*     */       }
/*     */       else {
/* 297 */         float valueMax = model.getMaximum() - model.getExtent();
/* 298 */         float valueRange = valueMax - model.getMinimum();
/* 299 */         float thumbValue = thumbPos - thumbMin;
/* 300 */         float thumbRange = thumbMax - thumbMin;
/* 301 */         int value = (int)(0.5D + thumbValue / thumbRange * valueRange);
/* 302 */         XTraScrollBarUI.this.scrollbar().setValue(value + model.getMinimum());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\XTraScrollBarUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */