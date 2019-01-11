/*     */ package org.jdesktop.swingx.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.synth.ColorType;
/*     */ import javax.swing.plaf.synth.Region;
/*     */ import javax.swing.plaf.synth.SynthContext;
/*     */ import javax.swing.plaf.synth.SynthLookAndFeel;
/*     */ import javax.swing.plaf.synth.SynthPainter;
/*     */ import javax.swing.plaf.synth.SynthStyle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynthUtils
/*     */ {
/*  51 */   private static SynthPainter NULL_PAINTER = new SynthPainter() {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SynthContext getContext(JComponent c, Region region, SynthStyle style, int state)
/*     */   {
/*  66 */     return new SynthContext(c, region, style, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SynthContext getContext(SynthContext context, SynthStyle style)
/*     */   {
/*  75 */     if (context.getStyle().equals(style)) return context;
/*  76 */     return getContext(context.getComponent(), context.getRegion(), style, context.getComponentState());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SynthContext getContext(SynthContext context, int state)
/*     */   {
/*  86 */     if (context.getComponentState() == state) return context;
/*  87 */     return getContext(context.getComponent(), context.getRegion(), context.getStyle(), state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SynthPainter getPainter(SynthContext context)
/*     */   {
/*  98 */     SynthPainter painter = context.getStyle().getPainter(context);
/*  99 */     return painter != null ? painter : NULL_PAINTER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean shouldUpdateStyle(PropertyChangeEvent event)
/*     */   {
/* 110 */     String eName = event.getPropertyName();
/* 111 */     if ("name" == eName)
/*     */     {
/* 113 */       return true;
/*     */     }
/* 115 */     if ("componentOrientation" == eName)
/*     */     {
/* 117 */       return true;
/*     */     }
/* 119 */     if (("ancestor" == eName) && (event.getNewValue() != null))
/*     */     {
/*     */ 
/* 122 */       LookAndFeel laf = UIManager.getLookAndFeel();
/* 123 */       return ((laf instanceof SynthLookAndFeel)) && (((SynthLookAndFeel)laf).shouldUpdateStyleOnAncestorChanged());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */     if ("Nimbus.Overrides" == eName)
/*     */     {
/*     */ 
/* 135 */       return true;
/*     */     }
/* 137 */     if ("Nimbus.Overrides.InheritDefaults" == eName)
/*     */     {
/*     */ 
/* 140 */       return true;
/*     */     }
/* 142 */     if ("JComponent.sizeVariant" == eName)
/*     */     {
/*     */ 
/* 145 */       return true;
/*     */     }
/* 147 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int getComponentState(JComponent c)
/*     */   {
/* 153 */     if (c.isEnabled()) {
/* 154 */       if (c.isFocusOwner()) {
/* 155 */         return 257;
/*     */       }
/* 157 */       return 1;
/*     */     }
/* 159 */     return 8;
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
/*     */   public static void update(SynthContext context, Graphics g)
/*     */   {
/* 172 */     update(context, g, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void update(SynthContext context, Graphics g, Rectangle bounds)
/*     */   {
/* 184 */     JComponent c = context.getComponent();
/* 185 */     SynthStyle style = context.getStyle();
/*     */     int height;
/*     */     int x;
/* 188 */     int y; int width; int height; if (bounds == null) {
/* 189 */       int x = 0;
/* 190 */       int y = 0;
/* 191 */       int width = c.getWidth();
/* 192 */       height = c.getHeight();
/*     */     } else {
/* 194 */       x = bounds.x;
/* 195 */       y = bounds.y;
/* 196 */       width = bounds.width;
/* 197 */       height = bounds.height;
/*     */     }
/*     */     
/*     */ 
/* 201 */     boolean subregion = context.getRegion().isSubregion();
/* 202 */     if (((subregion) && (style.isOpaque(context))) || ((!subregion) && (c.isOpaque())))
/*     */     {
/* 204 */       g.setColor(style.getColor(context, ColorType.BACKGROUND));
/* 205 */       g.fillRect(x, y, width, height);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\synth\SynthUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */