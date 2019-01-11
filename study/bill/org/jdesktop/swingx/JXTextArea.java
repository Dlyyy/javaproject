/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.JTextArea;
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
/*     */ public class JXTextArea
/*     */   extends JTextArea
/*     */ {
/*     */   public JXTextArea()
/*     */   {
/*  21 */     this(null);
/*     */   }
/*     */   
/*     */   public JXTextArea(String promptText) {
/*  25 */     this(promptText, null);
/*     */   }
/*     */   
/*     */   public JXTextArea(String promptText, Color promptForeground) {
/*  29 */     this(promptText, promptForeground, null);
/*     */   }
/*     */   
/*     */   public JXTextArea(String promptText, Color promptForeground, Color promptBackground)
/*     */   {
/*  34 */     PromptSupport.init(promptText, promptForeground, promptBackground, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PromptSupport.FocusBehavior getFocusBehavior()
/*     */   {
/*  42 */     return PromptSupport.getFocusBehavior(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPrompt()
/*     */   {
/*  49 */     return PromptSupport.getPrompt(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Color getPromptForeground()
/*     */   {
/*  56 */     return PromptSupport.getForeground(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Color getPromptBackground()
/*     */   {
/*  63 */     return PromptSupport.getBackground(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getPromptFontStyle()
/*     */   {
/*  70 */     return PromptSupport.getFontStyle(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFocusBehavior(PromptSupport.FocusBehavior focusBehavior)
/*     */   {
/*  77 */     PromptSupport.setFocusBehavior(focusBehavior, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPrompt(String labelText)
/*     */   {
/*  84 */     PromptSupport.setPrompt(labelText, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPromptForeground(Color promptTextColor)
/*     */   {
/*  91 */     PromptSupport.setForeground(promptTextColor, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPromptBackround(Color promptTextColor)
/*     */   {
/*  98 */     PromptSupport.setBackground(promptTextColor, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPromptFontStyle(Integer fontStyle)
/*     */   {
/* 105 */     PromptSupport.setFontStyle(fontStyle, this);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTextArea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */