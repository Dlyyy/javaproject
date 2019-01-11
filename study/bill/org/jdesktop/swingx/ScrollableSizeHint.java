/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ScrollableSizeHint
/*     */ {
/*  45 */   NONE(false), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   FIT(true), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   HORIZONTAL_STRETCH(0), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   VERTICAL_STRETCH(1);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final boolean tracks;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int orientation;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ScrollableSizeHint(boolean track)
/*     */   {
/*  94 */     this(track, -1);
/*     */   }
/*     */   
/*     */   private ScrollableSizeHint(int orientation) {
/*  98 */     this(false, orientation);
/*     */   }
/*     */   
/*     */   private ScrollableSizeHint(boolean tracks, int orientation) {
/* 102 */     this.tracks = tracks;
/* 103 */     this.orientation = orientation;
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
/*     */   public boolean getTracksParentSize(JComponent component)
/*     */   {
/* 118 */     Contract.asNotNull(component, "component must be not-null");
/* 119 */     if (this.orientation < 0) {
/* 120 */       return this.tracks;
/*     */     }
/* 122 */     return isSmallerThanParent(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHorizontalCompatible()
/*     */   {
/* 133 */     return this.orientation < 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVerticalCompatible()
/*     */   {
/* 144 */     return this.orientation < 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isSmallerThanParent(JComponent component)
/*     */   {
/* 156 */     return this.tracks;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\ScrollableSizeHint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */