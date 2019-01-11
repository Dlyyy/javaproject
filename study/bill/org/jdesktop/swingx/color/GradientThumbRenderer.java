/*    */ package org.jdesktop.swingx.color;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.JComponent;
/*    */ import org.jdesktop.swingx.JXMultiThumbSlider;
/*    */ import org.jdesktop.swingx.multislider.MultiThumbModel;
/*    */ import org.jdesktop.swingx.multislider.Thumb;
/*    */ import org.jdesktop.swingx.multislider.ThumbRenderer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GradientThumbRenderer
/*    */   extends JComponent
/*    */   implements ThumbRenderer
/*    */ {
/*    */   private Image thumb_black;
/*    */   private Image thumb_gray;
/*    */   private boolean selected;
/*    */   
/*    */   public GradientThumbRenderer()
/*    */   {
/*    */     try
/*    */     {
/* 41 */       this.thumb_black = ImageIO.read(GradientThumbRenderer.class.getResourceAsStream("/icons/thumb_black.png"));
/* 42 */       this.thumb_gray = ImageIO.read(GradientThumbRenderer.class.getResourceAsStream("/icons/thumb_gray.png"));
/*    */     }
/*    */     catch (Exception ex) {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void paintComponent(Graphics g)
/*    */   {
/* 51 */     JComponent thumb = this;
/* 52 */     int w = thumb.getWidth();
/* 53 */     g.setColor(getForeground());
/* 54 */     g.fillRect(0, 0, w - 1, w - 1);
/* 55 */     if (this.selected) {
/* 56 */       g.drawImage(this.thumb_black, 0, 0, null);
/*    */     } else {
/* 58 */       g.drawImage(this.thumb_gray, 0, 0, null);
/*    */     }
/*    */   }
/*    */   
/*    */   public JComponent getThumbRendererComponent(JXMultiThumbSlider slider, int index, boolean selected) {
/* 63 */     Color c = (Color)slider.getModel().getThumbAt(index).getObject();
/* 64 */     c = ColorUtil.removeAlpha(c);
/* 65 */     setForeground(c);
/* 66 */     this.selected = selected;
/* 67 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\color\GradientThumbRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */