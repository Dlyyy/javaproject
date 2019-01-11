/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.Highlighter.Highlight;
/*     */ import javax.swing.text.Highlighter.HighlightPainter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.View;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.prompt.PromptSupport;
/*     */ import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;
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
/*     */ public abstract class PromptTextUI
/*     */   extends TextUI
/*     */ {
/*     */   protected class PainterHighlighter
/*     */     implements Highlighter
/*     */   {
/*     */     private final Painter painter;
/*     */     private JTextComponent c;
/*     */     
/*     */     public PainterHighlighter(Painter painter)
/*     */     {
/*  56 */       this.painter = painter;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Object addHighlight(int p0, int p1, Highlighter.HighlightPainter p)
/*     */       throws BadLocationException
/*     */     {
/*  64 */       return new Object();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void changeHighlight(Object tag, int p0, int p1)
/*     */       throws BadLocationException
/*     */     {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void deinstall(JTextComponent c)
/*     */     {
/*  79 */       c = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Highlighter.Highlight[] getHighlights()
/*     */     {
/*  86 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void install(JTextComponent c)
/*     */     {
/*  93 */       this.c = c;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 100 */       Graphics2D g2d = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 103 */         this.painter.paint(g2d, this.c, this.c.getWidth(), this.c.getHeight());
/*     */       } finally {
/* 105 */         g2d.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void removeAllHighlights() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void removeHighlight(Object tag) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */   static final FocusHandler focusHandler = new FocusHandler(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TextUI delegate;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JTextComponent promptComponent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PromptTextUI(TextUI delegate)
/*     */   {
/* 145 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract JTextComponent createPromptComponent();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/* 162 */     this.delegate.installUI(c);
/*     */     
/* 164 */     JTextComponent txt = (JTextComponent)c;
/*     */     
/*     */ 
/*     */ 
/* 168 */     txt.addFocusListener(focusHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 176 */     this.delegate.uninstallUI(c);
/* 177 */     c.removeFocusListener(focusHandler);
/* 178 */     this.promptComponent = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JTextComponent getPromptComponent(JTextComponent txt)
/*     */   {
/* 190 */     if (this.promptComponent == null) {
/* 191 */       this.promptComponent = createPromptComponent();
/*     */     }
/* 193 */     if ((txt.isFocusOwner()) && (PromptSupport.getFocusBehavior(txt) == PromptSupport.FocusBehavior.HIDE_PROMPT))
/*     */     {
/* 195 */       this.promptComponent.setText(null);
/*     */     } else {
/* 197 */       this.promptComponent.setText(PromptSupport.getPrompt(txt));
/*     */     }
/*     */     
/* 200 */     this.promptComponent.getHighlighter().removeAllHighlights();
/* 201 */     if ((txt.isFocusOwner()) && (PromptSupport.getFocusBehavior(txt) == PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT))
/*     */     {
/* 203 */       this.promptComponent.setForeground(txt.getSelectedTextColor());
/*     */       try {
/* 205 */         this.promptComponent.getHighlighter().addHighlight(0, this.promptComponent.getText().length(), new DefaultHighlighter.DefaultHighlightPainter(txt.getSelectionColor()));
/*     */       }
/*     */       catch (BadLocationException e)
/*     */       {
/* 209 */         e.printStackTrace();
/*     */       }
/*     */     } else {
/* 212 */       this.promptComponent.setForeground(PromptSupport.getForeground(txt));
/*     */     }
/*     */     
/* 215 */     if (PromptSupport.getFontStyle(txt) == null) {
/* 216 */       this.promptComponent.setFont(txt.getFont());
/*     */     } else {
/* 218 */       this.promptComponent.setFont(txt.getFont().deriveFont(PromptSupport.getFontStyle(txt).intValue()));
/*     */     }
/*     */     
/*     */ 
/* 222 */     this.promptComponent.setBackground(PromptSupport.getBackground(txt));
/* 223 */     this.promptComponent.setHighlighter(new PainterHighlighter(PromptSupport.getBackgroundPainter(txt)));
/*     */     
/* 225 */     this.promptComponent.setEnabled(txt.isEnabled());
/* 226 */     this.promptComponent.setOpaque(txt.isOpaque());
/* 227 */     this.promptComponent.setBounds(txt.getBounds());
/* 228 */     Border b = txt.getBorder();
/*     */     
/* 230 */     if (b == null) {
/* 231 */       this.promptComponent.setBorder(txt.getBorder());
/*     */     } else {
/* 233 */       Insets insets = b.getBorderInsets(txt);
/* 234 */       this.promptComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
/*     */     }
/*     */     
/*     */ 
/* 238 */     this.promptComponent.setSelectedTextColor(txt.getSelectedTextColor());
/* 239 */     this.promptComponent.setSelectionColor(txt.getSelectionColor());
/* 240 */     this.promptComponent.setEditable(txt.isEditable());
/* 241 */     this.promptComponent.setMargin(txt.getMargin());
/*     */     
/* 243 */     return this.promptComponent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 254 */     JTextComponent txt = (JTextComponent)c;
/* 255 */     if (shouldPaintPrompt(txt)) {
/* 256 */       return getPromptComponent(txt).getPreferredSize();
/*     */     }
/* 258 */     return this.delegate.getPreferredSize(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 269 */     JTextComponent txt = (JTextComponent)c;
/*     */     
/* 271 */     if (shouldPaintPrompt(txt)) {
/* 272 */       paintPromptComponent(g, txt);
/*     */     } else {
/* 274 */       this.delegate.paint(g, c);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void paintPromptComponent(Graphics g, JTextComponent txt) {
/* 279 */     JTextComponent lbl = getPromptComponent(txt);
/* 280 */     lbl.paint(g);
/*     */     
/* 282 */     if (txt.getCaret() != null) {
/* 283 */       txt.getCaret().paint(g);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldPaintPrompt(JTextComponent txt)
/*     */   {
/* 295 */     return (txt.getText() == null) || (txt.getText().length() == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(Graphics g, JComponent c)
/*     */   {
/* 304 */     super.update(g, c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Rectangle modelToView(JTextComponent t, int pos, Position.Bias bias)
/*     */     throws BadLocationException
/*     */   {
/* 317 */     if (shouldPaintPrompt(t)) {
/* 318 */       return getPromptComponent(t).getUI().modelToView(t, pos, bias);
/*     */     }
/* 320 */     return this.delegate.modelToView(t, pos, bias);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Rectangle modelToView(JTextComponent t, int pos)
/*     */     throws BadLocationException
/*     */   {
/* 331 */     return modelToView(t, pos, Position.Bias.Forward);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(JComponent c, int x, int y)
/*     */   {
/* 339 */     return this.delegate.contains(c, x, y);
/*     */   }
/*     */   
/*     */ 
/*     */   public void damageRange(JTextComponent t, int p0, int p1, Position.Bias firstBias, Position.Bias secondBias)
/*     */   {
/* 345 */     this.delegate.damageRange(t, p0, p1, firstBias, secondBias);
/*     */   }
/*     */   
/*     */   public void damageRange(JTextComponent t, int p0, int p1)
/*     */   {
/* 350 */     this.delegate.damageRange(t, p0, p1);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 355 */     return this.delegate.equals(obj);
/*     */   }
/*     */   
/*     */   public Accessible getAccessibleChild(JComponent c, int i)
/*     */   {
/* 360 */     return this.delegate.getAccessibleChild(c, i);
/*     */   }
/*     */   
/*     */   public int getAccessibleChildrenCount(JComponent c)
/*     */   {
/* 365 */     return this.delegate.getAccessibleChildrenCount(c);
/*     */   }
/*     */   
/*     */   public EditorKit getEditorKit(JTextComponent t)
/*     */   {
/* 370 */     return this.delegate.getEditorKit(t);
/*     */   }
/*     */   
/*     */   public Dimension getMaximumSize(JComponent c)
/*     */   {
/* 375 */     return this.delegate.getMaximumSize(c);
/*     */   }
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c)
/*     */   {
/* 380 */     return this.delegate.getMinimumSize(c);
/*     */   }
/*     */   
/*     */   public int getNextVisualPositionFrom(JTextComponent t, int pos, Position.Bias b, int direction, Position.Bias[] biasRet)
/*     */     throws BadLocationException
/*     */   {
/* 386 */     return this.delegate.getNextVisualPositionFrom(t, pos, b, direction, biasRet);
/*     */   }
/*     */   
/*     */ 
/*     */   public View getRootView(JTextComponent t)
/*     */   {
/* 392 */     return this.delegate.getRootView(t);
/*     */   }
/*     */   
/*     */   public String getToolTipText(JTextComponent t, Point pt)
/*     */   {
/* 397 */     return this.delegate.getToolTipText(t, pt);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 402 */     return this.delegate.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 407 */     return String.format("%s (%s)", new Object[] { getClass().getName(), this.delegate.toString() });
/*     */   }
/*     */   
/*     */ 
/*     */   public int viewToModel(JTextComponent t, Point pt, Position.Bias[] biasReturn)
/*     */   {
/* 413 */     return this.delegate.viewToModel(t, pt, biasReturn);
/*     */   }
/*     */   
/*     */   public int viewToModel(JTextComponent t, Point pt)
/*     */   {
/* 418 */     return this.delegate.viewToModel(t, pt);
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
/*     */   public int getBaseline(JComponent c, int width, int height)
/*     */   {
/*     */     try
/*     */     {
/* 434 */       Method m = this.delegate.getClass().getMethod("getBaseline", new Class[] { JComponent.class, Integer.TYPE, Integer.TYPE });
/*     */       
/* 436 */       Object o = m.invoke(this.delegate, new Object[] { c, Integer.valueOf(width), Integer.valueOf(height) });
/* 437 */       return ((Integer)o).intValue();
/*     */     }
/*     */     catch (Exception ex) {}
/* 440 */     return -2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class FocusHandler
/*     */     extends FocusAdapter
/*     */   {
/*     */     public void focusGained(FocusEvent e)
/*     */     {
/* 450 */       e.getComponent().repaint();
/*     */     }
/*     */     
/*     */     public void focusLost(FocusEvent e)
/*     */     {
/* 455 */       e.getComponent().repaint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\PromptTextUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */