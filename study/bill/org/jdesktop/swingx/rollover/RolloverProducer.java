/*     */ package org.jdesktop.swingx.rollover;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JComponent;
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
/*     */ public abstract class RolloverProducer
/*     */   implements MouseListener, MouseMotionListener, ComponentListener
/*     */ {
/*  50 */   private static final Logger LOG = Logger.getLogger(RolloverProducer.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String CLICKED_KEY = "swingx.clicked";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String ROLLOVER_KEY = "swingx.rollover";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void install(JComponent component)
/*     */   {
/*  72 */     component.addMouseListener(this);
/*  73 */     component.addMouseMotionListener(this);
/*  74 */     component.addComponentListener(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void release(JComponent component)
/*     */   {
/*  84 */     component.removeMouseListener(this);
/*  85 */     component.removeMouseMotionListener(this);
/*  86 */     component.removeComponentListener(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseReleased(MouseEvent e)
/*     */   {
/*  95 */     updateRollover(e, "swingx.clicked", true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseEntered(MouseEvent e)
/*     */   {
/* 104 */     updateRollover(e, "swingx.rollover", false);
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
/*     */   public void mouseExited(MouseEvent e)
/*     */   {
/* 118 */     ((JComponent)e.getSource()).putClientProperty("swingx.rollover", null);
/* 119 */     ((JComponent)e.getSource()).putClientProperty("swingx.clicked", null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseClicked(MouseEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mousePressed(MouseEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseDragged(MouseEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseMoved(MouseEvent e)
/*     */   {
/* 149 */     updateRollover(e, "swingx.rollover", false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void componentShown(ComponentEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void componentResized(ComponentEvent e)
/*     */   {
/* 161 */     updateRollover(e);
/*     */   }
/*     */   
/*     */   public void componentMoved(ComponentEvent e)
/*     */   {
/* 166 */     updateRollover(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void updateRollover(ComponentEvent e)
/*     */   {
/* 173 */     Point componentLocation = e.getComponent().getMousePosition();
/* 174 */     if (componentLocation == null) {
/* 175 */       componentLocation = new Point(-1, -1);
/*     */     }
/*     */     
/* 178 */     updateRolloverPoint((JComponent)e.getComponent(), componentLocation);
/* 179 */     updateClientProperty((JComponent)e.getComponent(), "swingx.rollover", true);
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
/*     */   public void componentHidden(ComponentEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateRollover(MouseEvent e, String property, boolean fireAlways)
/*     */   {
/* 202 */     updateRolloverPoint((JComponent)e.getComponent(), e.getPoint());
/* 203 */     updateClientProperty((JComponent)e.getComponent(), property, fireAlways);
/*     */   }
/*     */   
/*     */ 
/* 207 */   protected Point rollover = new Point(-1, -1);
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
/*     */   protected void updateClientProperty(JComponent component, String property, boolean fireAlways)
/*     */   {
/* 220 */     if (fireAlways)
/*     */     {
/* 222 */       component.putClientProperty(property, null);
/* 223 */       component.putClientProperty(property, new Point(this.rollover));
/*     */     } else {
/* 225 */       Point p = (Point)component.getClientProperty(property);
/* 226 */       if ((p == null) || (this.rollover.x != p.x) || (this.rollover.y != p.y)) {
/* 227 */         component.putClientProperty(property, new Point(this.rollover));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void updateRolloverPoint(JComponent paramJComponent, Point paramPoint);
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\RolloverProducer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */