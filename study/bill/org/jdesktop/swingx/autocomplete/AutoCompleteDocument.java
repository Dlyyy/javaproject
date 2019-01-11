/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.UndoableEditEvent;
/*     */ import javax.swing.event.UndoableEditListener;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.PlainDocument;
/*     */ import javax.swing.text.Position;
/*     */ import javax.swing.text.Segment;
/*     */ import org.jdesktop.swingx.util.Contract;
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
/*     */ public class AutoCompleteDocument
/*     */   implements Document
/*     */ {
/*     */   protected boolean strictMatching;
/*     */   protected final Document delegate;
/*     */   
/*     */   private class Handler
/*     */     implements DocumentListener, UndoableEditListener
/*     */   {
/*     */     private Handler() {}
/*     */     
/*  47 */     private final EventListenerList listenerList = new EventListenerList();
/*     */     
/*     */     public void addDocumentListener(DocumentListener listener) {
/*  50 */       this.listenerList.add(DocumentListener.class, listener);
/*     */     }
/*     */     
/*     */     public void addUndoableEditListener(UndoableEditListener listener) {
/*  54 */       this.listenerList.add(UndoableEditListener.class, listener);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void removeDocumentListener(DocumentListener listener)
/*     */     {
/*  61 */       this.listenerList.remove(DocumentListener.class, listener);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void removeUndoableEditListener(UndoableEditListener listener)
/*     */     {
/*  68 */       this.listenerList.remove(UndoableEditListener.class, listener);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void changedUpdate(DocumentEvent e)
/*     */     {
/*  75 */       e = new DelegatingDocumentEvent(AutoCompleteDocument.this, e);
/*     */       
/*     */ 
/*  78 */       Object[] listeners = this.listenerList.getListenerList();
/*     */       
/*     */ 
/*  81 */       for (int i = listeners.length - 2; i >= 0; i -= 2) {
/*  82 */         if (listeners[i] == DocumentListener.class)
/*     */         {
/*     */ 
/*     */ 
/*  86 */           ((DocumentListener)listeners[(i + 1)]).changedUpdate(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void insertUpdate(DocumentEvent e)
/*     */     {
/*  95 */       e = new DelegatingDocumentEvent(AutoCompleteDocument.this, e);
/*     */       
/*     */ 
/*  98 */       Object[] listeners = this.listenerList.getListenerList();
/*     */       
/*     */ 
/* 101 */       for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 102 */         if (listeners[i] == DocumentListener.class)
/*     */         {
/*     */ 
/*     */ 
/* 106 */           ((DocumentListener)listeners[(i + 1)]).insertUpdate(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void removeUpdate(DocumentEvent e)
/*     */     {
/* 115 */       e = new DelegatingDocumentEvent(AutoCompleteDocument.this, e);
/*     */       
/*     */ 
/* 118 */       Object[] listeners = this.listenerList.getListenerList();
/*     */       
/*     */ 
/* 121 */       for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 122 */         if (listeners[i] == DocumentListener.class)
/*     */         {
/*     */ 
/*     */ 
/* 126 */           ((DocumentListener)listeners[(i + 1)]).removeUpdate(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void undoableEditHappened(UndoableEditEvent e)
/*     */     {
/* 135 */       e = new UndoableEditEvent(AutoCompleteDocument.this, e.getEdit());
/*     */       
/*     */ 
/* 138 */       Object[] listeners = this.listenerList.getListenerList();
/*     */       
/*     */ 
/* 141 */       for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 142 */         if (listeners[i] == UndoableEditListener.class)
/*     */         {
/*     */ 
/*     */ 
/* 146 */           ((UndoableEditListener)listeners[(i + 1)]).undoableEditHappened(e);
/*     */         }
/*     */       }
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
/* 164 */   boolean selecting = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   AbstractAutoCompleteAdaptor adaptor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   ObjectToStringConverter stringConverter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Handler handler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AutoCompleteDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching, ObjectToStringConverter stringConverter, Document delegate)
/*     */   {
/* 186 */     this.adaptor = ((AbstractAutoCompleteAdaptor)Contract.asNotNull(adaptor, "adaptor cannot be null"));
/* 187 */     this.strictMatching = strictMatching;
/* 188 */     this.stringConverter = (stringConverter == null ? ObjectToStringConverter.DEFAULT_IMPLEMENTATION : stringConverter);
/* 189 */     this.delegate = (delegate == null ? createDefaultDocument() : delegate);
/*     */     
/* 191 */     this.handler = new Handler(null);
/* 192 */     this.delegate.addDocumentListener(this.handler);
/*     */     
/*     */ 
/* 195 */     Object selected = adaptor.getSelectedItem();
/* 196 */     if (selected != null) {
/* 197 */       String itemAsString = this.stringConverter.getPreferredStringForItem(selected);
/* 198 */       setText(itemAsString);
/* 199 */       adaptor.setSelectedItemAsString(itemAsString);
/*     */     }
/* 201 */     this.adaptor.markEntireText();
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
/*     */   public AutoCompleteDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching, ObjectToStringConverter stringConverter)
/*     */   {
/* 214 */     this(adaptor, strictMatching, stringConverter, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AutoCompleteDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching)
/*     */   {
/* 225 */     this(adaptor, strictMatching, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Document createDefaultDocument()
/*     */   {
/* 235 */     return new PlainDocument();
/*     */   }
/*     */   
/*     */   public void remove(int offs, int len) throws BadLocationException
/*     */   {
/* 240 */     if (this.selecting) return;
/* 241 */     this.delegate.remove(offs, len);
/* 242 */     if (!this.strictMatching) {
/* 243 */       setSelectedItem(getText(0, getLength()), getText(0, getLength()));
/* 244 */       this.adaptor.getTextComponent().setCaretPosition(offs);
/*     */     }
/*     */   }
/*     */   
/*     */   public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
/*     */   {
/* 250 */     if (this.selecting) { return;
/*     */     }
/* 252 */     this.delegate.insertString(offs, str, a);
/*     */     
/*     */ 
/* 255 */     String pattern = getText(0, getLength());
/*     */     LookupResult lookupResult;
/* 257 */     if ((pattern == null) || (pattern.length() == 0)) {
/* 258 */       LookupResult lookupResult = new LookupResult(null, "");
/* 259 */       setSelectedItem(lookupResult.matchingItem, lookupResult.matchingString);
/*     */     } else {
/* 261 */       lookupResult = lookupItem(pattern);
/*     */     }
/*     */     
/* 264 */     if (lookupResult.matchingItem != null) {
/* 265 */       setSelectedItem(lookupResult.matchingItem, lookupResult.matchingString);
/*     */     }
/* 267 */     else if (this.strictMatching)
/*     */     {
/* 269 */       lookupResult.matchingItem = this.adaptor.getSelectedItem();
/* 270 */       lookupResult.matchingString = this.adaptor.getSelectedItemAsString();
/*     */       
/*     */ 
/* 273 */       offs = str == null ? offs : offs - str.length();
/*     */       
/* 275 */       if ((str != null) && (!str.isEmpty()))
/*     */       {
/* 277 */         UIManager.getLookAndFeel().provideErrorFeedback(this.adaptor.getTextComponent());
/*     */       }
/*     */     }
/*     */     else {
/* 281 */       lookupResult.matchingItem = getText(0, getLength());
/* 282 */       lookupResult.matchingString = getText(0, getLength());
/* 283 */       setSelectedItem(lookupResult.matchingItem, lookupResult.matchingString);
/*     */     }
/*     */     
/*     */ 
/* 287 */     setText(lookupResult.matchingString);
/*     */     
/*     */ 
/* 290 */     int len = str == null ? 0 : str.length();
/* 291 */     offs = lookupResult.matchingString == null ? 0 : offs + len;
/* 292 */     this.adaptor.markText(offs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setText(String text)
/*     */   {
/*     */     try
/*     */     {
/* 303 */       this.delegate.remove(0, getLength());
/* 304 */       this.delegate.insertString(0, text, null);
/*     */     } catch (BadLocationException e) {
/* 306 */       throw new RuntimeException(e.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setSelectedItem(Object item, String itemAsString)
/*     */   {
/* 316 */     this.selecting = true;
/* 317 */     this.adaptor.setSelectedItem(item);
/* 318 */     this.adaptor.setSelectedItemAsString(itemAsString);
/* 319 */     this.selecting = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private LookupResult lookupItem(String pattern)
/*     */   {
/* 331 */     Object selectedItem = this.adaptor.getSelectedItem();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 336 */     int i = 0; for (int n = this.adaptor.getItemCount(); i < n; i++) {
/* 337 */       Object currentItem = this.adaptor.getItem(i);
/* 338 */       String[] possibleStrings = this.stringConverter.getPossibleStringsForItem(currentItem);
/* 339 */       if (possibleStrings != null)
/*     */       {
/* 341 */         for (int j = 0; j < possibleStrings.length; j++) {
/* 342 */           if (possibleStrings[j].equalsIgnoreCase(pattern)) {
/* 343 */             return new LookupResult(currentItem, possibleStrings[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 349 */     String[] possibleStrings = this.stringConverter.getPossibleStringsForItem(selectedItem);
/* 350 */     if (possibleStrings != null) {
/* 351 */       for (int i = 0; i < possibleStrings.length; i++) {
/* 352 */         if (startsWithIgnoreCase(possibleStrings[i], pattern)) {
/* 353 */           return new LookupResult(selectedItem, possibleStrings[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 358 */     int i = 0; for (int n = this.adaptor.getItemCount(); i < n; i++) {
/* 359 */       Object currentItem = this.adaptor.getItem(i);
/* 360 */       possibleStrings = this.stringConverter.getPossibleStringsForItem(currentItem);
/* 361 */       if (possibleStrings != null) {
/* 362 */         for (int j = 0; j < possibleStrings.length; j++) {
/* 363 */           if (startsWithIgnoreCase(possibleStrings[j], pattern)) {
/* 364 */             return new LookupResult(currentItem, possibleStrings[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 370 */     return new LookupResult(null, "");
/*     */   }
/*     */   
/*     */   private static class LookupResult {
/*     */     Object matchingItem;
/*     */     String matchingString;
/*     */     
/* 377 */     public LookupResult(Object matchingItem, String matchingString) { this.matchingItem = matchingItem;
/* 378 */       this.matchingString = matchingString;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean startsWithIgnoreCase(String base, String prefix)
/*     */   {
/* 390 */     if (base.length() < prefix.length()) return false;
/* 391 */     return base.regionMatches(true, 0, prefix, 0, prefix.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addDocumentListener(DocumentListener listener)
/*     */   {
/* 398 */     this.handler.addDocumentListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addUndoableEditListener(UndoableEditListener listener)
/*     */   {
/* 405 */     this.handler.addUndoableEditListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */   public Position createPosition(int offs)
/*     */     throws BadLocationException
/*     */   {
/* 412 */     return this.delegate.createPosition(offs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Element getDefaultRootElement()
/*     */   {
/* 419 */     return this.delegate.getDefaultRootElement();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Position getEndPosition()
/*     */   {
/* 426 */     return this.delegate.getEndPosition();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 433 */     return this.delegate.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getProperty(Object key)
/*     */   {
/* 440 */     return this.delegate.getProperty(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Element[] getRootElements()
/*     */   {
/* 447 */     return this.delegate.getRootElements();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Position getStartPosition()
/*     */   {
/* 454 */     return this.delegate.getStartPosition();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getText(int offset, int length)
/*     */     throws BadLocationException
/*     */   {
/* 461 */     return this.delegate.getText(offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */   public void getText(int offset, int length, Segment txt)
/*     */     throws BadLocationException
/*     */   {
/* 468 */     this.delegate.getText(offset, length, txt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void putProperty(Object key, Object value)
/*     */   {
/* 475 */     this.delegate.putProperty(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeDocumentListener(DocumentListener listener)
/*     */   {
/* 482 */     this.handler.removeDocumentListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeUndoableEditListener(UndoableEditListener listener)
/*     */   {
/* 489 */     this.handler.removeUndoableEditListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void render(Runnable r)
/*     */   {
/* 496 */     this.delegate.render(r);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isStrictMatching()
/*     */   {
/* 504 */     return this.strictMatching;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\AutoCompleteDocument.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */