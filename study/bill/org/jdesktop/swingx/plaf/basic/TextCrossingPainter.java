/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.jdesktop.swingx.painter.AbstractPainter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TextCrossingPainter<T extends JComponent>
/*     */   extends AbstractPainter<T>
/*     */ {
/*  42 */   Rectangle paintIconR = new Rectangle();
/*  43 */   Rectangle paintViewR = new Rectangle();
/*  44 */   Rectangle paintTextR = new Rectangle();
/*  45 */   Insets insetss = new Insets(0, 0, 0, 0);
/*     */   
/*     */ 
/*     */ 
/*     */   Color crossColor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, JComponent comp, int width, int height)
/*     */   {
/*  56 */     if (!(comp instanceof JLabel)) return;
/*  57 */     JLabel label = (JLabel)comp;
/*  58 */     Insets insets = label.getInsets(this.insetss);
/*  59 */     this.paintViewR.x = insets.left;
/*  60 */     this.paintViewR.y = insets.top;
/*  61 */     this.paintViewR.width = (width - (insets.left + insets.right));
/*  62 */     this.paintViewR.height = (height - (insets.top + insets.bottom));
/*  63 */     this.paintIconR.x = (this.paintIconR.y = this.paintIconR.width = this.paintIconR.height = 0);
/*  64 */     this.paintTextR.x = (this.paintTextR.y = this.paintTextR.width = this.paintTextR.height = 0);
/*  65 */     SwingUtilities.layoutCompoundLabel(label, label.getFontMetrics(label.getFont()), label.getText(), null, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), this.paintViewR, this.paintIconR, this.paintTextR, label.getIconTextGap());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  70 */     doPaint(g, this.paintTextR);
/*     */   }
/*     */   
/*     */   private void doPaint(Graphics2D g, Rectangle r) {
/*  74 */     Color old = g.getColor();
/*  75 */     g.setColor(getForeground());
/*  76 */     g.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
/*  77 */     g.drawLine(r.x + 1, r.y, r.x + r.width + 1, r.y + r.height);
/*  78 */     g.drawLine(r.x + r.width, r.y, r.x, r.y + r.height);
/*  79 */     g.drawLine(r.x + r.width - 1, r.y, r.x - 1, r.y + r.height);
/*  80 */     g.setColor(old);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForeground(Color crossColor)
/*     */   {
/*  89 */     Color old = getForeground();
/*  90 */     this.crossColor = crossColor;
/*  91 */     firePropertyChange("foreground", old, getForeground());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getForeground()
/*     */   {
/* 100 */     return this.crossColor;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\TextCrossingPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */