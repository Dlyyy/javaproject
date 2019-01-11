/*     */ package util;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import javax.swing.JPanel;
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
/*     */ public class CircleProgressBar
/*     */   extends JPanel
/*     */ {
/*     */   private int minimumProgress;
/*     */   private int maximumProgress;
/*     */   private int progress;
/*     */   private String progressText;
/*     */   private Color backgroundColor;
/*     */   private Color foregroundColor;
/*     */   
/*     */   public CircleProgressBar()
/*     */   {
/*  33 */     this.minimumProgress = 0;
/*  34 */     this.maximumProgress = 100;
/*  35 */     this.progressText = "0%";
/*     */   }
/*     */   
/*     */   public void paint(Graphics g) {
/*  39 */     super.paint(g);
/*  40 */     Graphics2D graphics2d = (Graphics2D)g;
/*     */     
/*  42 */     graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*  43 */     int x = 0;
/*  44 */     int y = 0;
/*  45 */     int width = 0;
/*  46 */     int height = 0;
/*  47 */     int fontSize = 0;
/*  48 */     if (getWidth() >= getHeight()) {
/*  49 */       x = (getWidth() - getHeight()) / 2 + 25;
/*  50 */       y = 25;
/*  51 */       width = getHeight() - 50;
/*  52 */       height = getHeight() - 50;
/*  53 */       fontSize = getWidth() / 8;
/*     */     } else {
/*  55 */       x = 25;
/*  56 */       y = (getHeight() - getWidth()) / 2 + 25;
/*  57 */       width = getWidth() - 50;
/*  58 */       height = getWidth() - 50;
/*  59 */       fontSize = getHeight() / 8;
/*     */     }
/*  61 */     graphics2d.setStroke(new BasicStroke(20.0F));
/*  62 */     graphics2d.setColor(this.backgroundColor);
/*  63 */     graphics2d.drawArc(x, y, width, height, 0, 360);
/*  64 */     graphics2d.setColor(this.foregroundColor);
/*  65 */     graphics2d.drawArc(x, y, width, height, 90, 
/*  66 */       -(int)(360.0D * (this.progress * 1.0D / (this.maximumProgress - this.minimumProgress))));
/*  67 */     graphics2d.setFont(new Font("黑体", 1, fontSize));
/*  68 */     FontMetrics fontMetrics = graphics2d.getFontMetrics();
/*  69 */     int digitalWidth = fontMetrics.stringWidth(this.progressText);
/*  70 */     int digitalAscent = fontMetrics.getAscent();
/*  71 */     graphics2d.setColor(this.foregroundColor);
/*  72 */     graphics2d.drawString(this.progressText, getWidth() / 2 - digitalWidth / 2, getHeight() / 2 + digitalAscent / 2);
/*     */   }
/*     */   
/*     */   public int getProgress() {
/*  76 */     return this.progress;
/*     */   }
/*     */   
/*     */   public void setProgress(int progress) {
/*  80 */     if ((progress >= this.minimumProgress) && (progress <= this.maximumProgress))
/*  81 */       this.progress = progress;
/*  82 */     if (progress > this.maximumProgress) {
/*  83 */       this.progress = this.maximumProgress;
/*     */     }
/*  85 */     this.progressText = String.valueOf(progress + "%");
/*     */     
/*  87 */     repaint();
/*     */   }
/*     */   
/*     */   public Color getBackgroundColor() {
/*  91 */     return this.backgroundColor;
/*     */   }
/*     */   
/*     */   public void setBackgroundColor(Color backgroundColor) {
/*  95 */     this.backgroundColor = backgroundColor;
/*  96 */     repaint();
/*     */   }
/*     */   
/*     */   public Color getForegroundColor() {
/* 100 */     return this.foregroundColor;
/*     */   }
/*     */   
/*     */   public void setForegroundColor(Color foregroundColor) {
/* 104 */     this.foregroundColor = foregroundColor;
/* 105 */     repaint();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\CircleProgressBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */