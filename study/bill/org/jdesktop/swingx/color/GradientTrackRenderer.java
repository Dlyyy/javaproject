/*     */ package org.jdesktop.swingx.color;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.MultipleGradientPaint;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.List;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.JXMultiThumbSlider;
/*     */ import org.jdesktop.swingx.multislider.MultiThumbModel;
/*     */ import org.jdesktop.swingx.multislider.Thumb;
/*     */ import org.jdesktop.swingx.multislider.TrackRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GradientTrackRenderer
/*     */   extends JComponent
/*     */   implements TrackRenderer
/*     */ {
/*     */   private Paint checker_paint;
/*     */   private JXMultiThumbSlider slider;
/*     */   
/*     */   public GradientTrackRenderer()
/*     */   {
/*  48 */     this.checker_paint = ColorUtil.getCheckerPaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/*  55 */     super.paint(g);
/*  56 */     paintComponent(g);
/*     */   }
/*     */   
/*     */   protected void paintComponent(Graphics gfx)
/*     */   {
/*  61 */     Graphics2D g = (Graphics2D)gfx;
/*     */     
/*     */ 
/*  64 */     List<Thumb<Color>> stops = this.slider.getModel().getSortedThumbs();
/*  65 */     int len = stops.size();
/*     */     
/*     */ 
/*  68 */     float[] fractions = new float[len];
/*  69 */     Color[] colors = new Color[len];
/*  70 */     int i = 0;
/*  71 */     for (Thumb<Color> thumb : stops) {
/*  72 */       colors[i] = ((Color)thumb.getObject());
/*  73 */       fractions[i] = thumb.getPosition();
/*  74 */       i++;
/*     */     }
/*     */     
/*     */ 
/*  78 */     int thumb_width = 12;
/*  79 */     int track_width = this.slider.getWidth() - thumb_width;
/*  80 */     g.translate(thumb_width / 2, 12);
/*  81 */     Rectangle2D rect = new Rectangle(0, 0, track_width, 20);
/*     */     
/*     */ 
/*  84 */     g.setPaint(this.checker_paint);
/*  85 */     g.fill(rect);
/*     */     
/*     */ 
/*  88 */     Point2D start = new Point2D.Float(0.0F, 0.0F);
/*  89 */     Point2D end = new Point2D.Float(track_width, 0.0F);
/*  90 */     MultipleGradientPaint paint = new LinearGradientPaint((float)start.getX(), (float)start.getY(), (float)end.getX(), (float)end.getY(), fractions, colors);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     g.setPaint(paint);
/*  97 */     g.fill(rect);
/*     */     
/*     */ 
/* 100 */     g.setColor(Color.black);
/* 101 */     g.draw(rect);
/* 102 */     g.translate(-thumb_width / 2, -12);
/*     */   }
/*     */   
/*     */   public JComponent getRendererComponent(JXMultiThumbSlider slider) {
/* 106 */     this.slider = slider;
/* 107 */     return this;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\color\GradientTrackRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */