/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AutoComplete
/*     */ {
/*     */   static class InputMap
/*     */     extends InputMap
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */   }
/*     */   
/*     */   static class FocusAdapter
/*     */     extends FocusAdapter
/*     */   {
/*     */     private AbstractAutoCompleteAdaptor adaptor;
/*     */     
/*     */     public FocusAdapter(AbstractAutoCompleteAdaptor adaptor)
/*     */     {
/*  50 */       this.adaptor = adaptor;
/*     */     }
/*     */     
/*     */     public void focusGained(FocusEvent e)
/*     */     {
/*  55 */       this.adaptor.markEntireText();
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyAdapter extends KeyAdapter {
/*     */     private JComboBox comboBox;
/*     */     
/*     */     public KeyAdapter(JComboBox comboBox) {
/*  63 */       this.comboBox = comboBox;
/*     */     }
/*     */     
/*     */ 
/*     */     public void keyPressed(KeyEvent keyEvent)
/*     */     {
/*  69 */       if (keyEvent.isActionKey()) {
/*  70 */         return;
/*     */       }
/*     */       
/*     */ 
/*  74 */       if ((this.comboBox.isDisplayable()) && (!this.comboBox.isPopupVisible())) {
/*  75 */         int keyCode = keyEvent.getKeyCode();
/*     */         
/*  77 */         if ((keyCode == 16) || (keyCode == 17) || (keyCode == 18)) { return;
/*     */         }
/*  79 */         if ((keyCode == 10) || (keyCode == 27)) return;
/*  80 */         this.comboBox.setPopupVisible(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class PropertyChangeListener implements PropertyChangeListener {
/*     */     private JComboBox comboBox;
/*     */     
/*     */     public PropertyChangeListener(JComboBox comboBox) {
/*  89 */       this.comboBox = comboBox;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/*  97 */       if ("editor".equals(evt.getPropertyName())) {
/*  98 */         handleEditor(evt);
/*  99 */       } else if ("enabled".equals(evt.getPropertyName())) {
/* 100 */         handleEnabled(evt);
/*     */       }
/*     */     }
/*     */     
/*     */     private void handleEnabled(PropertyChangeEvent evt) {
/* 105 */       if (Boolean.TRUE.equals(evt.getNewValue())) {
/* 106 */         this.comboBox.setEditable(true);
/*     */       } else {
/* 108 */         JTextComponent textComponent = (JTextComponent)this.comboBox.getEditor().getEditorComponent();
/* 109 */         boolean strictMatching = ((AutoCompleteDocument)textComponent.getDocument()).strictMatching;
/*     */         
/* 111 */         this.comboBox.setEditable(!strictMatching);
/*     */       }
/*     */     }
/*     */     
/*     */     private void handleEditor(PropertyChangeEvent evt) {
/* 116 */       if ((evt.getNewValue() instanceof AutoCompleteComboBoxEditor)) {
/* 117 */         return;
/*     */       }
/*     */       
/* 120 */       AutoCompleteComboBoxEditor acEditor = (AutoCompleteComboBoxEditor)evt.getOldValue();
/* 121 */       boolean strictMatching = false;
/*     */       
/* 123 */       if (acEditor.getEditorComponent() != null) {
/* 124 */         JTextComponent textComponent = (JTextComponent)acEditor.getEditorComponent();
/* 125 */         strictMatching = ((AutoCompleteDocument)textComponent.getDocument()).strictMatching;
/*     */         
/* 127 */         AutoCompleteDecorator.undecorate(textComponent);
/*     */         
/* 129 */         for (KeyListener l : textComponent.getKeyListeners()) {
/* 130 */           if ((l instanceof AutoComplete.KeyAdapter)) {
/* 131 */             textComponent.removeKeyListener(l);
/* 132 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 137 */       JTextComponent editorComponent = (JTextComponent)this.comboBox.getEditor().getEditorComponent();
/* 138 */       AbstractAutoCompleteAdaptor adaptor = new ComboBoxAdaptor(this.comboBox);
/* 139 */       AutoCompleteDocument document = AutoCompleteDecorator.createAutoCompleteDocument(adaptor, strictMatching, acEditor.stringConverter, editorComponent.getDocument());
/*     */       
/* 141 */       AutoCompleteDecorator.decorate(editorComponent, document, adaptor);
/*     */       
/* 143 */       editorComponent.addKeyListener(new AutoComplete.KeyAdapter(this.comboBox));
/*     */       
/*     */ 
/* 146 */       this.comboBox.setEditor(new AutoCompleteComboBoxEditor(this.comboBox.getEditor(), document.stringConverter));
/*     */     }
/*     */   }
/*     */   
/*     */   static class SelectionAction implements Action {
/*     */     private Action delegate;
/*     */     
/*     */     public SelectionAction(Action delegate) {
/* 154 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 162 */       JComboBox comboBox = (JComboBox)e.getSource();
/* 163 */       JTextComponent textComponent = (JTextComponent)comboBox.getEditor().getEditorComponent();
/* 164 */       AutoCompleteDocument doc = (AutoCompleteDocument)textComponent.getDocument();
/*     */       
/*     */ 
/*     */ 
/* 168 */       doc.strictMatching = true;
/*     */       try {
/* 170 */         this.delegate.actionPerformed(e);
/*     */       } finally {
/* 172 */         doc.strictMatching = false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */     {
/* 181 */       this.delegate.addPropertyChangeListener(listener);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */     {
/* 189 */       this.delegate.removePropertyChangeListener(listener);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object getValue(String key)
/*     */     {
/* 197 */       return this.delegate.getValue(key);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void putValue(String key, Object value)
/*     */     {
/* 205 */       this.delegate.putValue(key, value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEnabled()
/*     */     {
/* 213 */       return this.delegate.isEnabled();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setEnabled(boolean b)
/*     */     {
/* 221 */       this.delegate.setEnabled(b);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\AutoComplete.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */