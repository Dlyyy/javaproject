/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.UIColorHighlighterAddon;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
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
/*     */ public final class HighlighterFactory
/*     */ {
/*     */   public static Highlighter createSimpleStriping()
/*     */   {
/*  53 */     ColorHighlighter hl = new UIColorHighlighter(HighlightPredicate.ODD);
/*  54 */     return hl;
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
/*     */   public static Highlighter createSimpleStriping(int rowsPerGroup)
/*     */   {
/*  67 */     return new UIColorHighlighter(new HighlightPredicate.RowGroupHighlightPredicate(rowsPerGroup));
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
/*     */   public static Highlighter createSimpleStriping(Color stripeBackground)
/*     */   {
/*  80 */     ColorHighlighter hl = new ColorHighlighter(HighlightPredicate.ODD, stripeBackground, null);
/*  81 */     return hl;
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
/*     */   public static Highlighter createSimpleStriping(Color stripeBackground, int rowsPerGroup)
/*     */   {
/*  95 */     HighlightPredicate predicate = new HighlightPredicate.RowGroupHighlightPredicate(rowsPerGroup);
/*     */     
/*  97 */     ColorHighlighter hl = new ColorHighlighter(predicate, stripeBackground, null);
/*     */     
/*  99 */     return hl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Highlighter createAlternateStriping()
/*     */   {
/* 110 */     ColorHighlighter first = new ColorHighlighter(HighlightPredicate.EVEN, Color.WHITE, null);
/* 111 */     ColorHighlighter hl = new UIColorHighlighter(HighlightPredicate.ODD);
/* 112 */     return new CompoundHighlighter(new Highlighter[] { first, hl });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Highlighter createAlternateStriping(int rowsPerGroup)
/*     */   {
/* 124 */     HighlightPredicate predicate = new HighlightPredicate.RowGroupHighlightPredicate(rowsPerGroup);
/* 125 */     ColorHighlighter first = new ColorHighlighter(new HighlightPredicate.NotHighlightPredicate(predicate), Color.WHITE, null);
/* 126 */     ColorHighlighter hl = new UIColorHighlighter(predicate);
/* 127 */     return new CompoundHighlighter(new Highlighter[] { first, hl });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Highlighter createAlternateStriping(Color baseBackground, Color alternateBackground)
/*     */   {
/* 139 */     ColorHighlighter base = new ColorHighlighter(HighlightPredicate.EVEN, baseBackground, null);
/* 140 */     ColorHighlighter alternate = new ColorHighlighter(HighlightPredicate.ODD, alternateBackground, null);
/* 141 */     return new CompoundHighlighter(new Highlighter[] { base, alternate });
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
/*     */   public static Highlighter createAlternateStriping(Color baseBackground, Color alternateBackground, int linesPerStripe)
/*     */   {
/* 154 */     HighlightPredicate predicate = new HighlightPredicate.RowGroupHighlightPredicate(linesPerStripe);
/* 155 */     ColorHighlighter base = new ColorHighlighter(new HighlightPredicate.NotHighlightPredicate(predicate), baseBackground, null);
/* 156 */     ColorHighlighter alternate = new ColorHighlighter(predicate, alternateBackground, null);
/*     */     
/* 158 */     return new CompoundHighlighter(new Highlighter[] { base, alternate });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class UIColorHighlighter
/*     */     extends ColorHighlighter
/*     */     implements UIDependent
/*     */   {
/*     */     static
/*     */     {
/* 174 */       LookAndFeelAddons.contribute(new UIColorHighlighterAddon());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public UIColorHighlighter()
/*     */     {
/* 184 */       this(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public UIColorHighlighter(HighlightPredicate odd)
/*     */     {
/* 194 */       super(null, null);
/* 195 */       updateUI();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void updateUI()
/*     */     {
/* 203 */       setBackground(getUIColor());
/*     */     }
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
/*     */     private Color getUIColor()
/*     */     {
/* 221 */       Color color = null;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 226 */       if (color == null) {
/* 227 */         color = UIManager.getColor("UIColorHighlighter.stripingBackground");
/*     */       }
/* 229 */       if (color == null) {
/* 230 */         color = HighlighterFactory.GENERIC_GRAY;
/*     */       }
/* 232 */       return color;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 263 */   public static final Color BEIGE = new Color(245, 245, 220);
/* 264 */   public static final Color LINE_PRINTER = new Color(204, 204, 255);
/* 265 */   public static final Color CLASSIC_LINE_PRINTER = new Color(204, 255, 204);
/* 266 */   public static final Color FLORAL_WHITE = new Color(255, 250, 240);
/* 267 */   public static final Color QUICKSILVER = new Color(240, 240, 224);
/* 268 */   public static final Color GENERIC_GRAY = new Color(229, 229, 229);
/* 269 */   public static final Color LEDGER = new Color(245, 255, 245);
/* 270 */   public static final Color NOTEPAD = new Color(255, 255, 204);
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\HighlighterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */