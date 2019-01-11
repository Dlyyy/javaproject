/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import org.jdesktop.swingx.painter.Painter;
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
/*     */ public class JRendererLabel
/*     */   extends JLabel
/*     */   implements PainterAware
/*     */ {
/*     */   protected Painter painter;
/*     */   
/*     */   public JRendererLabel()
/*     */   {
/*  73 */     setOpaque(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaque()
/*     */   {
/*  83 */     Color back = getBackground();
/*  84 */     Component p = getParent();
/*  85 */     if (p != null) {
/*  86 */       p = p.getParent();
/*     */     }
/*     */     
/*  89 */     boolean colorMatch = (back != null) && (p != null) && (back.equals(p.getBackground())) && (p.isOpaque());
/*     */     
/*     */ 
/*  92 */     return (!colorMatch) && (super.isOpaque());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPainter(Painter painter)
/*     */   {
/* 104 */     Painter old = getPainter();
/* 105 */     this.painter = painter;
/* 106 */     firePropertyChange("painter", old, getPainter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Painter getPainter()
/*     */   {
/* 113 */     return this.painter;
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
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 126 */     if ((this.painter != null) || (isNimbus()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 131 */       if (isOpaque())
/*     */       {
/* 133 */         paintComponentWithPainter((Graphics2D)g);
/*     */       }
/*     */       else {
/* 136 */         paintPainter(g);
/* 137 */         super.paintComponent(g);
/*     */       }
/*     */     }
/*     */     else {
/* 141 */       super.paintComponent(g);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isNimbus()
/*     */   {
/* 152 */     return UIManager.getLookAndFeel().getName().contains("Nimbus");
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
/*     */   private void paintPainter(Graphics g)
/*     */   {
/* 165 */     if (this.painter == null) { return;
/*     */     }
/*     */     
/*     */ 
/* 169 */     Graphics2D scratch = (Graphics2D)g.create();
/*     */     try {
/* 171 */       this.painter.paint(scratch, this, getWidth(), getHeight());
/*     */     }
/*     */     finally {
/* 174 */       scratch.dispose();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponentWithPainter(Graphics2D g)
/*     */   {
/* 200 */     if (this.ui != null)
/*     */     {
/*     */ 
/*     */ 
/* 204 */       Graphics2D scratchGraphics = (Graphics2D)g.create();
/*     */       try {
/* 206 */         scratchGraphics.setColor(getBackground());
/* 207 */         scratchGraphics.fillRect(0, 0, getWidth(), getHeight());
/* 208 */         paintPainter(g);
/* 209 */         this.ui.paint(scratchGraphics, this);
/*     */       }
/*     */       finally {
/* 212 */         scratchGraphics.dispose();
/*     */       }
/*     */     }
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
/*     */   public void setToolTipText(String text)
/*     */   {
/* 227 */     putClientProperty("ToolTipText", text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void revalidate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repaint(long tm, int x, int y, int width, int height) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repaint(Rectangle r) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repaint() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
/*     */   {
/* 291 */     if ("text".equals(propertyName)) {
/* 292 */       super.firePropertyChange(propertyName, oldValue, newValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\JRendererLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */