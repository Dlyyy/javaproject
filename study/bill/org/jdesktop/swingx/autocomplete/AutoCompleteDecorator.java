/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.TextAction;
/*     */ import org.jdesktop.swingx.autocomplete.workarounds.MacOSXPopupLocationFix;
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
/*     */ public class AutoCompleteDecorator
/*     */ {
/*  72 */   private static final List<String> COMBO_BOX_ACTIONS = Collections.unmodifiableList(Arrays.asList(new String[] { "selectNext", "selectNext2", "selectPrevious", "selectPrevious2", "pageDownPassThrough", "pageUpPassThrough", "homePassThrough", "endPassThrough" }));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private static final Object errorFeedbackAction = new TextAction("provide-error-feedback") {
/*     */     public void actionPerformed(ActionEvent e) {
/*  81 */       UIManager.getLookAndFeel().provideErrorFeedback(getTextComponent(e));
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void installMap(InputMap componentMap, boolean strict)
/*     */   {
/*  90 */     InputMap map = new AutoComplete.InputMap();
/*     */     
/*  92 */     if (strict) {
/*  93 */       map.put(KeyStroke.getKeyStroke(8, 0), "selection-backward");
/*     */       
/*  95 */       map.put(KeyStroke.getKeyStroke(127, 0), errorFeedbackAction);
/*  96 */       map.put(KeyStroke.getKeyStroke(88, 128), errorFeedbackAction);
/*     */     }
/*     */     else
/*     */     {
/* 100 */       map.put(KeyStroke.getKeyStroke(8, 0), "nonstrict-backspace");
/*     */     }
/*     */     
/*     */ 
/* 104 */     map.setParent(componentMap.getParent());
/* 105 */     componentMap.setParent(map);
/*     */   }
/*     */   
/*     */ 
/*     */   static AutoCompleteDocument createAutoCompleteDocument(AbstractAutoCompleteAdaptor adaptor, boolean strictMatching, ObjectToStringConverter stringConverter, Document delegate)
/*     */   {
/* 111 */     if ((delegate instanceof StyledDocument)) {
/* 112 */       return new AutoCompleteStyledDocument(adaptor, strictMatching, stringConverter, (StyledDocument)delegate);
/*     */     }
/*     */     
/*     */ 
/* 116 */     return new AutoCompleteDocument(adaptor, strictMatching, stringConverter, delegate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void decorate(JComboBox comboBox)
/*     */   {
/* 128 */     decorate(comboBox, null);
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
/*     */   public static void decorate(JComboBox comboBox, ObjectToStringConverter stringConverter)
/*     */   {
/* 154 */     undecorate(comboBox);
/*     */     
/* 156 */     boolean strictMatching = !comboBox.isEditable();
/*     */     
/* 158 */     comboBox.setEditable(true);
/*     */     
/* 160 */     MacOSXPopupLocationFix.install(comboBox);
/*     */     
/*     */ 
/* 163 */     JTextComponent editorComponent = (JTextComponent)comboBox.getEditor().getEditorComponent();
/* 164 */     AbstractAutoCompleteAdaptor adaptor = new ComboBoxAdaptor(comboBox);
/* 165 */     AutoCompleteDocument document = createAutoCompleteDocument(adaptor, strictMatching, stringConverter, editorComponent.getDocument());
/*     */     
/* 167 */     decorate(editorComponent, document, adaptor);
/*     */     
/* 169 */     editorComponent.addKeyListener(new AutoComplete.KeyAdapter(comboBox));
/*     */     
/*     */ 
/* 172 */     comboBox.setEditor(new AutoCompleteComboBoxEditor(comboBox.getEditor(), document.stringConverter));
/*     */     
/*     */ 
/*     */ 
/* 176 */     AutoComplete.PropertyChangeListener pcl = new AutoComplete.PropertyChangeListener(comboBox);
/* 177 */     comboBox.addPropertyChangeListener("editor", pcl);
/* 178 */     comboBox.addPropertyChangeListener("enabled", pcl);
/*     */     ActionMap map;
/* 180 */     if (!strictMatching) {
/* 181 */       map = comboBox.getActionMap();
/*     */       
/* 183 */       for (String key : COMBO_BOX_ACTIONS) {
/* 184 */         Action a = map.get(key);
/* 185 */         map.put(key, new AutoComplete.SelectionAction(a));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void undecorate(JComboBox comboBox) {
/* 191 */     JTextComponent editorComponent = (JTextComponent)comboBox.getEditor().getEditorComponent();
/*     */     
/* 193 */     if ((editorComponent.getDocument() instanceof AutoCompleteDocument)) {
/* 194 */       AutoCompleteDocument doc = (AutoCompleteDocument)editorComponent.getDocument();
/*     */       ActionMap map;
/* 196 */       if (doc.strictMatching) {
/* 197 */         map = comboBox.getActionMap();
/*     */         
/* 199 */         for (String key : COMBO_BOX_ACTIONS) {
/* 200 */           map.put(key, null);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 205 */       for (PropertyChangeListener l : comboBox.getPropertyChangeListeners("editor")) {
/* 206 */         if ((l instanceof AutoComplete.PropertyChangeListener)) {
/* 207 */           comboBox.removePropertyChangeListener("editor", l);
/*     */         }
/*     */       }
/*     */       
/* 211 */       for (PropertyChangeListener l : comboBox.getPropertyChangeListeners("enabled")) {
/* 212 */         if ((l instanceof AutoComplete.PropertyChangeListener)) {
/* 213 */           comboBox.removePropertyChangeListener("enabled", l);
/*     */         }
/*     */       }
/*     */       
/* 217 */       AutoCompleteComboBoxEditor editor = (AutoCompleteComboBoxEditor)comboBox.getEditor();
/* 218 */       comboBox.setEditor(editor.wrapped);
/*     */       
/*     */ 
/* 221 */       for (KeyListener l : editorComponent.getKeyListeners()) {
/* 222 */         if ((l instanceof AutoComplete.KeyAdapter)) {
/* 223 */           editorComponent.removeKeyListener(l);
/* 224 */           break;
/*     */         }
/*     */       }
/*     */       
/* 228 */       undecorate(editorComponent);
/*     */       
/* 230 */       for (ActionListener l : comboBox.getActionListeners()) {
/* 231 */         if ((l instanceof ComboBoxAdaptor)) {
/* 232 */           comboBox.removeActionListener(l);
/* 233 */           break;
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
/*     */   public static void decorate(JList list, JTextComponent textComponent)
/*     */   {
/* 252 */     decorate(list, textComponent, null);
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
/*     */   public static void decorate(JList list, JTextComponent textComponent, ObjectToStringConverter stringConverter)
/*     */   {
/* 265 */     undecorate(list);
/*     */     
/* 267 */     AbstractAutoCompleteAdaptor adaptor = new ListAdaptor(list, textComponent, stringConverter);
/* 268 */     AutoCompleteDocument document = createAutoCompleteDocument(adaptor, true, stringConverter, textComponent.getDocument());
/* 269 */     decorate(textComponent, document, adaptor);
/*     */   }
/*     */   
/*     */   static void undecorate(JList list) {
/* 273 */     for (ListSelectionListener l : list.getListSelectionListeners()) {
/* 274 */       if ((l instanceof ListAdaptor)) {
/* 275 */         list.removeListSelectionListener(l);
/* 276 */         break;
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
/*     */   public static void decorate(JTextComponent textComponent, List<?> items, boolean strictMatching)
/*     */   {
/* 290 */     decorate(textComponent, items, strictMatching, null);
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
/*     */   public static void decorate(JTextComponent textComponent, List<?> items, boolean strictMatching, ObjectToStringConverter stringConverter)
/*     */   {
/* 303 */     AbstractAutoCompleteAdaptor adaptor = new TextComponentAdaptor(textComponent, items);
/* 304 */     AutoCompleteDocument document = createAutoCompleteDocument(adaptor, strictMatching, stringConverter, textComponent.getDocument());
/* 305 */     decorate(textComponent, document, adaptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void decorate(JTextComponent textComponent, AutoCompleteDocument document, AbstractAutoCompleteAdaptor adaptor)
/*     */   {
/* 317 */     undecorate(textComponent);
/*     */     
/*     */ 
/* 320 */     textComponent.setDocument(document);
/*     */     
/*     */ 
/*     */ 
/* 324 */     textComponent.addFocusListener(new AutoComplete.FocusAdapter(adaptor));
/*     */     
/*     */ 
/* 327 */     InputMap editorInputMap = textComponent.getInputMap();
/*     */     
/* 329 */     while (editorInputMap != null) {
/* 330 */       InputMap parent = editorInputMap.getParent();
/*     */       
/* 332 */       if ((parent instanceof UIResource)) {
/* 333 */         installMap(editorInputMap, document.isStrictMatching());
/* 334 */         break;
/*     */       }
/*     */       
/* 337 */       editorInputMap = parent;
/*     */     }
/*     */     
/* 340 */     ActionMap editorActionMap = textComponent.getActionMap();
/* 341 */     editorActionMap.put("nonstrict-backspace", new NonStrictBackspaceAction(editorActionMap.get("delete-previous"), editorActionMap.get("selection-backward"), adaptor));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void undecorate(JTextComponent textComponent)
/*     */   {
/* 348 */     Document doc = textComponent.getDocument();
/*     */     
/* 350 */     if ((doc instanceof AutoCompleteDocument))
/*     */     {
/* 352 */       InputMap map = textComponent.getInputMap();
/*     */       
/* 354 */       while (map.getParent() != null) {
/* 355 */         InputMap parent = map.getParent();
/*     */         
/* 357 */         if ((parent instanceof AutoComplete.InputMap)) {
/* 358 */           map.setParent(parent.getParent());
/*     */         }
/*     */         
/* 361 */         map = parent;
/*     */       }
/*     */       
/* 364 */       textComponent.getActionMap().put("nonstrict-backspace", null);
/*     */       
/*     */ 
/* 367 */       for (FocusListener l : textComponent.getFocusListeners()) {
/* 368 */         if ((l instanceof AutoComplete.FocusAdapter)) {
/* 369 */           textComponent.removeFocusListener(l);
/* 370 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 375 */       textComponent.setDocument(((AutoCompleteDocument)doc).delegate);
/*     */     }
/*     */   }
/*     */   
/*     */   static class NonStrictBackspaceAction extends TextAction {
/*     */     Action backspace;
/*     */     Action selectionBackward;
/*     */     AbstractAutoCompleteAdaptor adaptor;
/*     */     
/*     */     public NonStrictBackspaceAction(Action backspace, Action selectionBackward, AbstractAutoCompleteAdaptor adaptor) {
/* 385 */       super();
/* 386 */       this.backspace = backspace;
/* 387 */       this.selectionBackward = selectionBackward;
/* 388 */       this.adaptor = adaptor;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 392 */       if (this.adaptor.listContainsSelectedItem()) {
/* 393 */         this.selectionBackward.actionPerformed(e);
/*     */       } else {
/* 395 */         this.backspace.actionPerformed(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\AutoCompleteDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */