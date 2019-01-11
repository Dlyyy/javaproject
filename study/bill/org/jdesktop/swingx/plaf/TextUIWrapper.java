/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.jdesktop.swingx.JXSearchField;
/*     */ import org.jdesktop.swingx.prompt.BuddySupport;
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
/*     */ public abstract class TextUIWrapper<UI extends TextUI>
/*     */ {
/*  25 */   private static final DefaultWrapper defaultWrapper = new DefaultWrapper(null);
/*     */   private Class<UI> wrapperClass;
/*     */   
/*  28 */   public static final TextUIWrapper<? extends PromptTextUI> getDefaultWrapper() { return defaultWrapper; }
/*     */   
/*     */ 
/*     */ 
/*     */   protected TextUIWrapper(Class<UI> wrapperClass)
/*     */   {
/*  34 */     this.wrapperClass = wrapperClass;
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
/*     */   public final void install(JTextComponent textComponent, boolean stayOnUIChange)
/*     */   {
/*  49 */     replaceUIIfNeeded(textComponent);
/*  50 */     if (stayOnUIChange) {
/*  51 */       this.uiChangeHandler.install(textComponent);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean replaceUIIfNeeded(JTextComponent textComponent)
/*     */   {
/*  63 */     if (this.wrapperClass.isAssignableFrom(textComponent.getUI().getClass())) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     textComponent.setUI(wrapUI(textComponent));
/*     */     
/*  69 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract UI wrapUI(JTextComponent paramJTextComponent);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<UI> getWrapperClass()
/*     */   {
/*  86 */     return this.wrapperClass;
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
/*     */   public final void uninstall(JTextComponent textComponent)
/*     */   {
/*  99 */     this.uiChangeHandler.uninstall(textComponent);
/* 100 */     textComponent.updateUI();
/*     */   }
/*     */   
/* 103 */   private final TextUIWrapper<UI>.TextUIChangeHandler uiChangeHandler = new TextUIChangeHandler(null);
/*     */   
/*     */   private final class TextUIChangeHandler extends AbstractUIChangeHandler { private TextUIChangeHandler() {}
/*     */     
/* 107 */     public void propertyChange(PropertyChangeEvent evt) { JTextComponent txt = (JTextComponent)evt.getSource();
/*     */       
/* 109 */       TextUIWrapper.this.replaceUIIfNeeded(txt);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class DefaultWrapper extends TextUIWrapper<PromptTextUI> {
/*     */     private DefaultWrapper() {
/* 115 */       super();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public PromptTextUI wrapUI(JTextComponent textComponent)
/*     */     {
/* 137 */       TextUI textUI = textComponent.getUI();
/*     */       
/* 139 */       if ((textUI instanceof PromptTextUI))
/* 140 */         return (PromptTextUI)textUI;
/* 141 */       if ((textComponent instanceof JXSearchField))
/* 142 */         return new SearchFieldUI(textUI);
/* 143 */       if ((textComponent instanceof JTextField))
/* 144 */         return new BuddyTextFieldUI(textUI);
/* 145 */       if ((textComponent instanceof JTextArea)) {
/* 146 */         return new PromptTextAreaUI(textUI);
/*     */       }
/* 148 */       throw new IllegalArgumentException("ui implementation not supported: " + textUI.getClass());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean replaceUIIfNeeded(JTextComponent textComponent)
/*     */     {
/* 159 */       boolean replaced = super.replaceUIIfNeeded(textComponent);
/*     */       
/* 161 */       if ((replaced) && ((textComponent instanceof JTextField))) {
/* 162 */         BuddySupport.ensureBuddiesAreInComponentHierarchy((JTextField)textComponent);
/*     */       }
/* 164 */       return replaced;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TextUIWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */