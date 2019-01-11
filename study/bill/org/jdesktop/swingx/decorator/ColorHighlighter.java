/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import org.jdesktop.swingx.color.ColorUtil;
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
/*     */ public class ColorHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private Color background;
/*     */   private Color foreground;
/*     */   private Color selectedBackground;
/*     */   private Color selectedForeground;
/*     */   @Deprecated
/*     */   private boolean legacy;
/*     */   
/*     */   public ColorHighlighter()
/*     */   {
/*  61 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ColorHighlighter(HighlightPredicate predicate)
/*     */   {
/*  71 */     this(predicate, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ColorHighlighter(Color cellBackground, Color cellForeground)
/*     */   {
/*  83 */     this(null, cellBackground, cellForeground);
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
/*     */   public ColorHighlighter(HighlightPredicate predicate, Color cellBackground, Color cellForeground)
/*     */   {
/*  97 */     this(predicate, cellBackground, cellForeground, null, null);
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
/*     */   public ColorHighlighter(Color cellBackground, Color cellForeground, Color selectedBackground, Color selectedForeground)
/*     */   {
/* 112 */     this(null, cellBackground, cellForeground, selectedBackground, selectedForeground);
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
/*     */   public ColorHighlighter(HighlightPredicate predicate, Color cellBackground, Color cellForeground, Color selectedBackground, Color selectedForeground)
/*     */   {
/* 129 */     super(predicate);
/* 130 */     this.background = cellBackground;
/* 131 */     this.foreground = cellForeground;
/* 132 */     this.selectedBackground = selectedBackground;
/* 133 */     this.selectedForeground = selectedForeground;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 143 */     applyBackground(renderer, adapter);
/* 144 */     applyForeground(renderer, adapter);
/* 145 */     return renderer;
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
/*     */   protected void applyBackground(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 161 */     Color color = adapter.isSelected() ? getSelectedBackground() : getBackground();
/*     */     
/* 163 */     if ((isLegacy()) && (color != null)) {
/* 164 */       renderer.setBackground(color);
/*     */     } else {
/* 166 */       renderer.setBackground(ColorUtil.blend(renderer.getBackground(), color));
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
/*     */   protected void applyForeground(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 182 */     Color color = adapter.isSelected() ? getSelectedForeground() : getForeground();
/*     */     
/* 184 */     if ((isLegacy()) && (color != null)) {
/* 185 */       renderer.setForeground(color);
/*     */     } else {
/* 187 */       renderer.setForeground(ColorUtil.blend(renderer.getForeground(), color));
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
/*     */   public Color getBackground()
/*     */   {
/* 201 */     return this.background;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackground(Color color)
/*     */   {
/* 212 */     if (areEqual(color, getBackground())) return;
/* 213 */     this.background = color;
/* 214 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getForeground()
/*     */   {
/* 224 */     return this.foreground;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForeground(Color color)
/*     */   {
/* 235 */     if (areEqual(color, getForeground())) return;
/* 236 */     this.foreground = color;
/* 237 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getSelectedBackground()
/*     */   {
/* 247 */     return this.selectedBackground;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectedBackground(Color color)
/*     */   {
/* 258 */     if (areEqual(color, getSelectedBackground())) return;
/* 259 */     this.selectedBackground = color;
/* 260 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getSelectedForeground()
/*     */   {
/* 270 */     return this.selectedForeground;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectedForeground(Color color)
/*     */   {
/* 281 */     if (areEqual(color, getSelectedForeground())) return;
/* 282 */     this.selectedForeground = color;
/* 283 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isLegacy()
/*     */   {
/* 292 */     return this.legacy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setLegacy(boolean legacy)
/*     */   {
/* 301 */     this.legacy = legacy;
/* 302 */     fireStateChanged();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\ColorHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */