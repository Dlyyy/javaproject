/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Insets;
/*     */ import java.util.List;
/*     */ import javax.swing.JTextField;
/*     */ import org.jdesktop.swingx.prompt.BuddySupport;
/*     */ import org.jdesktop.swingx.prompt.BuddySupport.Position;
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
/*     */ public class JXTextField
/*     */   extends JTextField
/*     */ {
/*     */   public JXTextField()
/*     */   {
/*  25 */     this(null);
/*     */   }
/*     */   
/*     */   public JXTextField(String promptText) {
/*  29 */     this(promptText, null);
/*     */   }
/*     */   
/*     */   public JXTextField(String promptText, Color promptForeground) {
/*  33 */     this(promptText, promptForeground, null);
/*     */   }
/*     */   
/*     */   public JXTextField(String promptText, Color promptForeground, Color promptBackground)
/*     */   {
/*  38 */     PromptSupport.init(promptText, promptForeground, promptBackground, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PromptSupport.FocusBehavior getFocusBehavior()
/*     */   {
/*  46 */     return PromptSupport.getFocusBehavior(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPrompt()
/*     */   {
/*  53 */     return PromptSupport.getPrompt(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Color getPromptForeground()
/*     */   {
/*  60 */     return PromptSupport.getForeground(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Color getPromptBackground()
/*     */   {
/*  67 */     return PromptSupport.getBackground(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getPromptFontStyle()
/*     */   {
/*  74 */     return PromptSupport.getFontStyle(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFocusBehavior(PromptSupport.FocusBehavior focusBehavior)
/*     */   {
/*  81 */     PromptSupport.setFocusBehavior(focusBehavior, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPrompt(String labelText)
/*     */   {
/*  88 */     PromptSupport.setPrompt(labelText, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPromptForeground(Color promptTextColor)
/*     */   {
/*  95 */     PromptSupport.setForeground(promptTextColor, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPromptBackround(Color promptTextColor)
/*     */   {
/* 102 */     PromptSupport.setBackground(promptTextColor, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPromptFontStyle(Integer fontStyle)
/*     */   {
/* 109 */     PromptSupport.setFontStyle(fontStyle, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setOuterMargin(Insets margin)
/*     */   {
/* 116 */     BuddySupport.setOuterMargin(this, margin);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Insets getOuterMargin()
/*     */   {
/* 123 */     return BuddySupport.getOuterMargin(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addBuddy(Component buddy, BuddySupport.Position pos)
/*     */   {
/* 130 */     BuddySupport.add(buddy, pos, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addGap(int width, BuddySupport.Position pos)
/*     */   {
/* 137 */     BuddySupport.addGap(width, pos, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Component> getBuddies(BuddySupport.Position pos)
/*     */   {
/* 144 */     return BuddySupport.getBuddies(pos, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeAllBuddies()
/*     */   {
/* 151 */     BuddySupport.removeAll(this);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTextField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */