/*     */ package org.jdesktop.swingx.prompt;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.painter.Painters;
/*     */ import org.jdesktop.swingx.plaf.TextUIWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PromptSupport
/*     */ {
/*     */   public static final String PROMPT = "promptText";
/*     */   public static final String FOREGROUND = "promptForeground";
/*     */   public static final String BACKGROUND = "promptBackground";
/*     */   public static final String BACKGROUND_PAINTER = "promptBackgroundPainter";
/*     */   public static final String FOCUS_BEHAVIOR = "focusBehavior";
/*     */   public static final String FONT_STYLE = "promptFontStyle";
/*     */   
/*     */   public static enum FocusBehavior
/*     */   {
/*  80 */     SHOW_PROMPT, 
/*     */     
/*     */ 
/*     */ 
/*  84 */     HIGHLIGHT_PROMPT, 
/*     */     
/*     */ 
/*     */ 
/*  88 */     HIDE_PROMPT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private FocusBehavior() {}
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
/*     */   public static void init(String promptText, Color promptForeground, Color promptBackground, JTextComponent textComponent)
/*     */   {
/* 113 */     if ((promptText != null) && (promptText.length() > 0)) {
/* 114 */       setPrompt(promptText, textComponent);
/*     */     }
/* 116 */     if (promptForeground != null) {
/* 117 */       setForeground(promptForeground, textComponent);
/*     */     }
/* 119 */     if (promptBackground != null) {
/* 120 */       setBackground(promptBackground, textComponent);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static FocusBehavior getFocusBehavior(JTextComponent textComponent)
/*     */   {
/* 132 */     FocusBehavior fb = (FocusBehavior)textComponent.getClientProperty("focusBehavior");
/* 133 */     if (fb == null) {
/* 134 */       fb = FocusBehavior.HIDE_PROMPT;
/*     */     }
/* 136 */     return fb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setFocusBehavior(FocusBehavior focusBehavior, JTextComponent textComponent)
/*     */   {
/* 147 */     textComponent.putClientProperty("focusBehavior", focusBehavior);
/* 148 */     if (textComponent.isFocusOwner()) {
/* 149 */       textComponent.repaint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getPrompt(JTextComponent textComponent)
/*     */   {
/* 160 */     return (String)textComponent.getClientProperty("promptText");
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
/*     */   public static void setPrompt(String promptText, JTextComponent textComponent)
/*     */   {
/* 180 */     TextUIWrapper.getDefaultWrapper().install(textComponent, true);
/*     */     
/*     */ 
/* 183 */     if ((textComponent.getToolTipText() == null) || (textComponent.getToolTipText().equals(getPrompt(textComponent)))) {
/* 184 */       textComponent.setToolTipText(promptText);
/*     */     }
/*     */     
/* 187 */     textComponent.putClientProperty("promptText", promptText);
/* 188 */     textComponent.repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Color getForeground(JTextComponent textComponent)
/*     */   {
/* 200 */     if (textComponent.getClientProperty("promptForeground") == null) {
/* 201 */       return textComponent.getDisabledTextColor();
/*     */     }
/* 203 */     return (Color)textComponent.getClientProperty("promptForeground");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setForeground(Color promptTextColor, JTextComponent textComponent)
/*     */   {
/* 215 */     textComponent.putClientProperty("promptForeground", promptTextColor);
/* 216 */     textComponent.repaint();
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
/*     */   public static Color getBackground(JTextComponent textComponent)
/*     */   {
/* 229 */     if (textComponent.getClientProperty("promptBackground") == null) {
/* 230 */       return textComponent.getBackground();
/*     */     }
/* 232 */     return (Color)textComponent.getClientProperty("promptBackground");
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
/*     */   public static void setBackground(Color background, JTextComponent textComponent)
/*     */   {
/* 251 */     TextUIWrapper.getDefaultWrapper().install(textComponent, true);
/*     */     
/* 253 */     textComponent.putClientProperty("promptBackground", background);
/* 254 */     textComponent.repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T extends JTextComponent> Painter<? super T> getBackgroundPainter(T textComponent)
/*     */   {
/* 266 */     Painter<? super T> painter = (Painter)textComponent.getClientProperty("promptBackgroundPainter");
/*     */     
/* 268 */     if (painter == null) {
/* 269 */       painter = Painters.EMPTY_PAINTER;
/*     */     }
/*     */     
/* 272 */     return painter;
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
/*     */   public static <T extends JTextComponent> void setBackgroundPainter(Painter<? super T> background, T textComponent)
/*     */   {
/* 291 */     TextUIWrapper.getDefaultWrapper().install(textComponent, true);
/*     */     
/* 293 */     textComponent.putClientProperty("promptBackgroundPainter", background);
/* 294 */     textComponent.repaint();
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
/*     */   public static void setFontStyle(Integer fontStyle, JTextComponent textComponent)
/*     */   {
/* 313 */     textComponent.putClientProperty("promptFontStyle", fontStyle);
/* 314 */     textComponent.revalidate();
/* 315 */     textComponent.repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Integer getFontStyle(JTextComponent textComponent)
/*     */   {
/* 327 */     return (Integer)textComponent.getClientProperty("promptFontStyle");
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\prompt\PromptSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */