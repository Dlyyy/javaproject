/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.GlyphVector;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.jdesktop.swingx.painter.effects.AreaEffect;
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
/*     */ public class TextPainter
/*     */   extends AbstractAreaPainter<Object>
/*     */ {
/*  46 */   private String text = "";
/*  47 */   private Font font = null;
/*     */   
/*     */   public TextPainter()
/*     */   {
/*  51 */     this("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextPainter(String text)
/*     */   {
/*  59 */     this(text, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextPainter(String text, Font font)
/*     */   {
/*  68 */     this(text, font, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextPainter(String text, Paint paint)
/*     */   {
/*  77 */     this(text, null, paint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextPainter(String text, Font font, Paint paint)
/*     */   {
/*  87 */     this.text = text;
/*  88 */     this.font = font;
/*  89 */     setFillPaint(paint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFont(Font f)
/*     */   {
/*  97 */     Font old = getFont();
/*  98 */     this.font = f;
/*  99 */     setDirty(true);
/* 100 */     firePropertyChange("font", old, getFont());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Font getFont()
/*     */   {
/* 108 */     return this.font;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setText(String text)
/*     */   {
/* 116 */     String old = getText();
/* 117 */     this.text = (text == null ? "" : text);
/* 118 */     setDirty(true);
/* 119 */     firePropertyChange("text", old, getText());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getText()
/*     */   {
/* 127 */     return this.text;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/* 135 */     Font font = calculateFont(component);
/* 136 */     if (font != null) {
/* 137 */       g.setFont(font);
/*     */     }
/*     */     
/* 140 */     Paint paint = getFillPaint();
/* 141 */     if ((paint == null) && 
/* 142 */       ((component instanceof JComponent))) {
/* 143 */       paint = ((JComponent)component).getForeground();
/*     */     }
/*     */     
/*     */ 
/* 147 */     String text = calculateText(component);
/*     */     
/*     */ 
/* 150 */     FontMetrics metrics = g.getFontMetrics(g.getFont());
/*     */     
/*     */ 
/* 153 */     int tw = metrics.stringWidth(text);
/* 154 */     int th = metrics.getHeight();
/* 155 */     Rectangle res = calculateLayout(tw, th, width, height);
/*     */     
/* 157 */     g.translate(res.x, res.y);
/*     */     
/* 159 */     if (isPaintStretched()) {
/* 160 */       paint = calculateSnappedPaint(paint, res.width, res.height);
/*     */     }
/*     */     
/* 163 */     if (paint != null) {
/* 164 */       g.setPaint(paint);
/*     */     }
/*     */     
/* 167 */     g.drawString(text, 0, 0 + metrics.getAscent());
/* 168 */     if (getAreaEffects() != null) {
/* 169 */       Shape shape = provideShape(g, component, width, height);
/* 170 */       for (AreaEffect ef : getAreaEffects()) {
/* 171 */         ef.apply(g, shape, width, height);
/*     */       }
/*     */     }
/* 174 */     g.translate(-res.x, -res.y);
/*     */   }
/*     */   
/*     */   private String calculateText(Object component)
/*     */   {
/* 179 */     String text = getText();
/*     */     
/* 181 */     if ((text != null) && (!text.trim().equals(""))) {
/* 182 */       return text;
/*     */     }
/* 184 */     if ((component instanceof JTextComponent)) {
/* 185 */       text = ((JTextComponent)component).getText();
/*     */     }
/* 187 */     if ((component instanceof JLabel)) {
/* 188 */       text = ((JLabel)component).getText();
/*     */     }
/* 190 */     if ((component instanceof AbstractButton)) {
/* 191 */       text = ((AbstractButton)component).getText();
/*     */     }
/* 193 */     return text;
/*     */   }
/*     */   
/*     */   private Font calculateFont(Object component)
/*     */   {
/* 198 */     Font font = getFont();
/* 199 */     if ((font == null) && 
/* 200 */       ((component instanceof JComponent))) {
/* 201 */       font = ((JComponent)component).getFont();
/*     */     }
/*     */     
/* 204 */     if (font == null) {
/* 205 */       font = new Font("Dialog", 0, 18);
/*     */     }
/* 207 */     return font;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Shape provideShape(Graphics2D g2, Object comp, int width, int height)
/*     */   {
/* 215 */     Font font = calculateFont(comp);
/* 216 */     String text = calculateText(comp);
/* 217 */     FontMetrics metrics = g2.getFontMetrics(font);
/* 218 */     GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), text);
/* 219 */     return vect.getOutline(0.0F, 0.0F + metrics.getAscent());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\TextPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */