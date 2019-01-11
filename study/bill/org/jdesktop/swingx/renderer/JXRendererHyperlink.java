/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import org.jdesktop.swingx.JXHyperlink;
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
/*     */ public class JXRendererHyperlink
/*     */   extends JXHyperlink
/*     */   implements PainterAware
/*     */ {
/*     */   protected Painter painter;
/*     */   
/*     */   public void setPainter(Painter painter)
/*     */   {
/*  46 */     Painter old = getPainter();
/*  47 */     this.painter = painter;
/*  48 */     if (painter != null)
/*     */     {
/*     */ 
/*     */ 
/*  52 */       setContentAreaFilled(false);
/*     */     }
/*  54 */     firePropertyChange("painter", old, getPainter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Painter getPainter()
/*     */   {
/*  61 */     return this.painter;
/*     */   }
/*     */   
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/*  66 */     if (this.painter != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  71 */       paintComponentWithPainter((Graphics2D)g);
/*     */     }
/*     */     else {
/*  74 */       super.paintComponent(g);
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
/*     */   private void paintPainter(Graphics g)
/*     */   {
/*  90 */     Graphics2D scratch = (Graphics2D)g.create();
/*     */     try {
/*  92 */       this.painter.paint(scratch, this, getWidth(), getHeight());
/*     */     }
/*     */     finally {
/*  95 */       scratch.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponentWithPainter(Graphics2D g)
/*     */   {
/* 107 */     if (this.ui != null)
/*     */     {
/*     */ 
/*     */ 
/* 111 */       Graphics scratchGraphics = g.create();
/*     */       try {
/* 113 */         scratchGraphics.setColor(getBackground());
/* 114 */         scratchGraphics.fillRect(0, 0, getWidth(), getHeight());
/* 115 */         paintPainter(g);
/* 116 */         this.ui.paint(scratchGraphics, this);
/*     */       } finally {
/* 118 */         scratchGraphics.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 126 */     super.updateUI();
/* 127 */     setBorderPainted(true);
/* 128 */     setOpaque(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToolTipText(String text)
/*     */   {
/* 139 */     putClientProperty("ToolTipText", text);
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
/* 203 */     if ("text".equals(propertyName)) {
/* 204 */       super.firePropertyChange(propertyName, oldValue, newValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\JXRendererHyperlink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */