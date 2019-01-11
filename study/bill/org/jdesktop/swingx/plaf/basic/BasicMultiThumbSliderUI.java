/*    */ package org.jdesktop.swingx.plaf.basic;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Polygon;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import org.jdesktop.swingx.JXMultiThumbSlider;
/*    */ import org.jdesktop.swingx.multislider.ThumbRenderer;
/*    */ import org.jdesktop.swingx.multislider.TrackRenderer;
/*    */ import org.jdesktop.swingx.plaf.MultiThumbSliderUI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicMultiThumbSliderUI
/*    */   extends MultiThumbSliderUI
/*    */ {
/*    */   protected JXMultiThumbSlider<?> slider;
/*    */   
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 45 */     return new BasicMultiThumbSliderUI();
/*    */   }
/*    */   
/*    */   public void installUI(JComponent c)
/*    */   {
/* 50 */     this.slider = ((JXMultiThumbSlider)c);
/* 51 */     this.slider.setThumbRenderer(new BasicThumbRenderer());
/* 52 */     this.slider.setTrackRenderer(new BasicTrackRenderer(null));
/*    */   }
/*    */   
/*    */   public void uninstallUI(JComponent c) {
/* 56 */     this.slider = null;
/*    */   }
/*    */   
/*    */   private class BasicThumbRenderer extends JComponent implements ThumbRenderer {
/*    */     public BasicThumbRenderer() {
/* 61 */       setPreferredSize(new Dimension(14, 14));
/*    */     }
/*    */     
/*    */     protected void paintComponent(Graphics g)
/*    */     {
/* 66 */       g.setColor(Color.green);
/* 67 */       Polygon poly = new Polygon();
/* 68 */       JComponent thumb = this;
/* 69 */       poly.addPoint(thumb.getWidth() / 2, 0);
/* 70 */       poly.addPoint(0, thumb.getHeight() / 2);
/* 71 */       poly.addPoint(thumb.getWidth() / 2, thumb.getHeight());
/* 72 */       poly.addPoint(thumb.getWidth(), thumb.getHeight() / 2);
/* 73 */       g.fillPolygon(poly);
/*    */     }
/*    */     
/*    */ 
/* 77 */     public JComponent getThumbRendererComponent(JXMultiThumbSlider slider, int index, boolean selected) { return this; }
/*    */   }
/*    */   
/*    */   private class BasicTrackRenderer extends JComponent implements TrackRenderer {
/*    */     private JXMultiThumbSlider<?> slider;
/*    */     
/*    */     private BasicTrackRenderer() {}
/*    */     
/* 85 */     public void paintComponent(Graphics g) { g.setColor(this.slider.getBackground());
/* 86 */       g.fillRect(0, 0, this.slider.getWidth(), this.slider.getHeight());
/* 87 */       g.setColor(Color.black);
/* 88 */       g.drawLine(0, this.slider.getHeight() / 2, this.slider.getWidth(), this.slider.getHeight() / 2);
/* 89 */       g.drawLine(0, this.slider.getHeight() / 2 + 1, this.slider.getWidth(), this.slider.getHeight() / 2 + 1);
/*    */     }
/*    */     
/*    */     public JComponent getRendererComponent(JXMultiThumbSlider slider) {
/* 93 */       this.slider = slider;
/* 94 */       return this;
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicMultiThumbSliderUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */