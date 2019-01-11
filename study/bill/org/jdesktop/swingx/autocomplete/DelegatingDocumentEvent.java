/*    */ package org.jdesktop.swingx.autocomplete;
/*    */ 
/*    */ import javax.swing.event.DocumentEvent;
/*    */ import javax.swing.event.DocumentEvent.ElementChange;
/*    */ import javax.swing.event.DocumentEvent.EventType;
/*    */ import javax.swing.text.Document;
/*    */ import javax.swing.text.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DelegatingDocumentEvent
/*    */   implements DocumentEvent
/*    */ {
/*    */   private final Document resourcedDocument;
/*    */   private final DocumentEvent sourceEvent;
/*    */   
/*    */   public DelegatingDocumentEvent(Document resourcedDocument, DocumentEvent sourceEvent)
/*    */   {
/* 19 */     this.resourcedDocument = resourcedDocument;
/* 20 */     this.sourceEvent = sourceEvent;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DocumentEvent.ElementChange getChange(Element elem)
/*    */   {
/* 27 */     return this.sourceEvent.getChange(elem);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Document getDocument()
/*    */   {
/* 34 */     return this.resourcedDocument;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getLength()
/*    */   {
/* 41 */     return this.sourceEvent.getLength();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getOffset()
/*    */   {
/* 48 */     return this.sourceEvent.getOffset();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DocumentEvent.EventType getType()
/*    */   {
/* 55 */     return this.sourceEvent.getType();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\DelegatingDocumentEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */