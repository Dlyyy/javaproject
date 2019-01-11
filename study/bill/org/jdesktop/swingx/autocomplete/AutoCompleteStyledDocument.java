/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.DefaultStyledDocument;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Style;
/*     */ import javax.swing.text.StyledDocument;
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
/*     */ public class AutoCompleteStyledDocument
/*     */   extends AutoCompleteDocument
/*     */   implements StyledDocument
/*     */ {
/*     */   public AutoCompleteStyledDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching, ObjectToStringConverter stringConverter, StyledDocument delegate)
/*     */   {
/*  48 */     super(adaptor, strictMatching, stringConverter, delegate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AutoCompleteStyledDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching, ObjectToStringConverter stringConverter)
/*     */   {
/*  58 */     super(adaptor, strictMatching, stringConverter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AutoCompleteStyledDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching)
/*     */   {
/*  67 */     super(adaptor, strictMatching);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Document createDefaultDocument()
/*     */   {
/*  75 */     return new DefaultStyledDocument();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Style addStyle(String nm, Style parent)
/*     */   {
/*  82 */     return ((StyledDocument)this.delegate).addStyle(nm, parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Color getBackground(AttributeSet attr)
/*     */   {
/*  89 */     return ((StyledDocument)this.delegate).getBackground(attr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Element getCharacterElement(int pos)
/*     */   {
/*  96 */     return ((StyledDocument)this.delegate).getCharacterElement(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Font getFont(AttributeSet attr)
/*     */   {
/* 103 */     return ((StyledDocument)this.delegate).getFont(attr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Color getForeground(AttributeSet attr)
/*     */   {
/* 110 */     return ((StyledDocument)this.delegate).getForeground(attr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Style getLogicalStyle(int p)
/*     */   {
/* 117 */     return ((StyledDocument)this.delegate).getLogicalStyle(p);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Element getParagraphElement(int pos)
/*     */   {
/* 124 */     return ((StyledDocument)this.delegate).getParagraphElement(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Style getStyle(String nm)
/*     */   {
/* 131 */     return ((StyledDocument)this.delegate).getStyle(nm);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeStyle(String nm)
/*     */   {
/* 138 */     ((StyledDocument)this.delegate).removeStyle(nm);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterAttributes(int offset, int length, AttributeSet s, boolean replace)
/*     */   {
/* 146 */     ((StyledDocument)this.delegate).setCharacterAttributes(offset, length, s, replace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLogicalStyle(int pos, Style s)
/*     */   {
/* 153 */     ((StyledDocument)this.delegate).setLogicalStyle(pos, s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParagraphAttributes(int offset, int length, AttributeSet s, boolean replace)
/*     */   {
/* 161 */     ((StyledDocument)this.delegate).setParagraphAttributes(offset, length, s, replace);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\AutoCompleteStyledDocument.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */