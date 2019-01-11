/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JCheckBox;
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
/*     */ public class JRendererCheckBox
/*     */   extends JCheckBox
/*     */   implements PainterAware
/*     */ {
/*     */   protected Painter painter;
/*     */   
/*     */   public Painter getPainter()
/*     */   {
/*  48 */     return this.painter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPainter(Painter painter)
/*     */   {
/*  56 */     Painter old = getPainter();
/*  57 */     this.painter = painter;
/*  58 */     if (painter != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  63 */       setContentAreaFilled(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  68 */     firePropertyChange("painter", old, getPainter());
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
/*     */   public boolean isOpaque()
/*     */   {
/*  81 */     return this.painter == null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToolTipText(String text)
/*     */   {
/* 112 */     putClientProperty("ToolTipText", text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 120 */     if ((this.painter != null) || (isNimbus()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 125 */       paintComponentWithPainter((Graphics2D)g);
/*     */     }
/*     */     else {
/* 128 */       super.paintComponent(g);
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
/* 139 */     return UIManager.getLookAndFeel().getName().contains("Nimbus");
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
/* 152 */     if (this.painter == null) { return;
/*     */     }
/*     */     
/*     */ 
/* 156 */     Graphics2D scratch = (Graphics2D)g.create();
/*     */     try {
/* 158 */       this.painter.paint(scratch, this, getWidth(), getHeight());
/*     */     }
/*     */     finally {
/* 161 */       scratch.dispose();
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
/* 173 */     if (this.ui != null)
/*     */     {
/*     */ 
/*     */ 
/* 177 */       Graphics scratchGraphics = g.create();
/*     */       try {
/* 179 */         scratchGraphics.setColor(getBackground());
/* 180 */         scratchGraphics.fillRect(0, 0, getWidth(), getHeight());
/* 181 */         paintPainter(g);
/* 182 */         this.ui.paint(scratchGraphics, this);
/*     */       } finally {
/* 184 */         scratchGraphics.dispose();
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
/* 251 */     if ("text".equals(propertyName)) {
/* 252 */       super.firePropertyChange(propertyName, oldValue, newValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\JRendererCheckBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */