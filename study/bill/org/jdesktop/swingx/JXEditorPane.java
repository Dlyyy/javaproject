/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.MatchResult;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.event.UndoableEditEvent;
/*     */ import javax.swing.event.UndoableEditListener;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.Segment;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.StyledEditorKit;
/*     */ import javax.swing.text.html.HTML.Tag;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import javax.swing.text.html.HTMLEditorKit;
/*     */ import javax.swing.undo.CannotRedoException;
/*     */ import javax.swing.undo.CannotUndoException;
/*     */ import javax.swing.undo.UndoManager;
/*     */ import org.jdesktop.swingx.action.ActionManager;
/*     */ import org.jdesktop.swingx.action.Targetable;
/*     */ import org.jdesktop.swingx.action.TargetableSupport;
/*     */ import org.jdesktop.swingx.search.SearchFactory;
/*     */ import org.jdesktop.swingx.search.Searchable;
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
/*     */ public class JXEditorPane
/*     */   extends JEditorPane
/*     */   implements Targetable
/*     */ {
/* 144 */   private static final Logger LOG = Logger.getLogger(JXEditorPane.class.getName());
/*     */   
/*     */ 
/*     */   private UndoableEditListener undoHandler;
/*     */   
/*     */ 
/*     */   private UndoManager undoManager;
/*     */   
/*     */ 
/*     */   private CaretListener caretHandler;
/*     */   
/*     */ 
/*     */   private JComboBox selector;
/*     */   
/*     */ 
/*     */   private static final String ACTION_FIND = "find";
/*     */   
/*     */ 
/*     */   private static final String ACTION_UNDO = "undo";
/*     */   
/*     */ 
/*     */   private static final String ACTION_REDO = "redo";
/*     */   
/*     */ 
/*     */   private static final String ACTION_CUT = "cut";
/*     */   
/*     */ 
/*     */   private static final String ACTION_COPY = "copy";
/*     */   
/*     */   private static final String ACTION_PASTE = "paste";
/*     */   
/* 175 */   private TargetableSupport targetSupport = new TargetableSupport(this);
/*     */   
/*     */ 
/*     */   private Searchable searchable;
/*     */   
/*     */ 
/*     */   public JXEditorPane()
/*     */   {
/* 183 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXEditorPane(String url)
/*     */     throws IOException
/*     */   {
/* 195 */     super(url);
/* 196 */     init();
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
/*     */   public JXEditorPane(String type, String text)
/*     */   {
/* 210 */     super(type, text);
/* 211 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXEditorPane(URL initialPage)
/*     */     throws IOException
/*     */   {
/* 222 */     super(initialPage);
/* 223 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/* 227 */     putClientProperty("JEditorPane.honorDisplayProperties", Boolean.valueOf(true));
/* 228 */     setEditorKitForContentType("text/html", new SloppyHTMLEditorKit(null));
/* 229 */     addPropertyChangeListener(new PropertyHandler(null));
/* 230 */     getDocument().addUndoableEditListener(getUndoableEditListener());
/* 231 */     initActions();
/*     */   }
/*     */   
/*     */   private class PropertyHandler implements PropertyChangeListener { private PropertyHandler() {}
/*     */     
/* 236 */     public void propertyChange(PropertyChangeEvent evt) { String name = evt.getPropertyName();
/* 237 */       if (name.equals("document")) {
/* 238 */         Document doc = (Document)evt.getOldValue();
/* 239 */         if (doc != null) {
/* 240 */           doc.removeUndoableEditListener(JXEditorPane.this.getUndoableEditListener());
/*     */         }
/*     */         
/* 243 */         doc = (Document)evt.getNewValue();
/* 244 */         if (doc != null) {
/* 245 */           doc.addUndoableEditListener(JXEditorPane.this.getUndoableEditListener());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   CaretListener getCaretListener()
/*     */   {
/* 254 */     return this.caretHandler;
/*     */   }
/*     */   
/*     */   UndoableEditListener getUndoableEditListener()
/*     */   {
/* 259 */     if (this.undoHandler == null) {
/* 260 */       this.undoHandler = new UndoHandler(null);
/* 261 */       this.undoManager = new UndoManager();
/*     */     }
/* 263 */     return this.undoHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEditorKit(EditorKit kit)
/*     */   {
/* 271 */     super.setEditorKit(kit);
/*     */     
/* 273 */     if ((kit instanceof StyledEditorKit)) {
/* 274 */       if (this.caretHandler == null) {
/* 275 */         this.caretHandler = new CaretHandler(null);
/*     */       }
/* 277 */       addCaretListener(this.caretHandler);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initActions()
/*     */   {
/* 285 */     ActionMap map = getActionMap();
/* 286 */     map.put("find", new Actions("find"));
/* 287 */     map.put("undo", new Actions("undo"));
/* 288 */     map.put("redo", new Actions("redo"));
/* 289 */     map.put("cut", new Actions("cut"));
/* 290 */     map.put("copy", new Actions("copy"));
/* 291 */     map.put("paste", new Actions("paste"));
/*     */     
/* 293 */     KeyStroke findStroke = SearchFactory.getInstance().getSearchAccelerator();
/* 294 */     getInputMap(1).put(findStroke, "find");
/*     */   }
/*     */   
/*     */   private class UndoHandler implements UndoableEditListener {
/*     */     private UndoHandler() {}
/*     */     
/*     */     public void undoableEditHappened(UndoableEditEvent evt) {
/* 301 */       JXEditorPane.this.undoManager.addEdit(evt.getEdit());
/* 302 */       JXEditorPane.this.updateActionState();
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
/*     */   private void updateActionState()
/*     */   {
/* 320 */     Runnable doEnabled = new Runnable() {
/*     */       public void run() {
/* 322 */         ActionManager manager = ActionManager.getInstance();
/* 323 */         manager.setEnabled("undo", JXEditorPane.this.undoManager.canUndo());
/* 324 */         manager.setEnabled("redo", JXEditorPane.this.undoManager.canRedo());
/*     */       }
/* 326 */     };
/* 327 */     SwingUtilities.invokeLater(doEnabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class Actions
/*     */     extends UIAction
/*     */   {
/*     */     Actions(String name)
/*     */     {
/* 338 */       super();
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent evt) {
/* 342 */       String name = getName();
/* 343 */       if ("find".equals(name)) {
/* 344 */         JXEditorPane.this.find();
/*     */       }
/* 346 */       else if ("undo".equals(name)) {
/*     */         try {
/* 348 */           JXEditorPane.this.undoManager.undo();
/*     */         } catch (CannotUndoException ex) {
/* 350 */           JXEditorPane.LOG.info("Could not undo");
/*     */         }
/* 352 */         JXEditorPane.this.updateActionState();
/*     */       }
/* 354 */       else if ("redo".equals(name)) {
/*     */         try {
/* 356 */           JXEditorPane.this.undoManager.redo();
/*     */         } catch (CannotRedoException ex) {
/* 358 */           JXEditorPane.LOG.info("Could not redo");
/*     */         }
/* 360 */         JXEditorPane.this.updateActionState();
/* 361 */       } else if ("cut".equals(name)) {
/* 362 */         ActionMap map = JXEditorPane.this.getActionMap();
/* 363 */         map.remove("cut");
/* 364 */         JXEditorPane.this.cut();
/* 365 */         map.put("cut", this);
/* 366 */       } else if ("copy".equals(name)) {
/* 367 */         ActionMap map = JXEditorPane.this.getActionMap();
/* 368 */         map.remove("copy");
/* 369 */         JXEditorPane.this.copy();
/* 370 */         map.put("copy", this);
/* 371 */       } else if ("paste".equals(name)) {
/* 372 */         ActionMap map = JXEditorPane.this.getActionMap();
/* 373 */         map.remove("paste");
/* 374 */         JXEditorPane.this.paste();
/* 375 */         map.put("paste", this);
/*     */       }
/*     */       else {
/* 378 */         JXEditorPane.LOG.fine("ActionHandled: " + name);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isEnabled(Object sender)
/*     */     {
/* 385 */       String name = getName();
/* 386 */       if ("undo".equals(name)) {
/* 387 */         return (JXEditorPane.this.isEditable()) && (JXEditorPane.this.undoManager.canUndo());
/*     */       }
/* 389 */       if ("redo".equals(name)) {
/* 390 */         return (JXEditorPane.this.isEditable()) && (JXEditorPane.this.undoManager.canRedo());
/*     */       }
/* 392 */       if ("paste".equals(name)) {
/* 393 */         if (!JXEditorPane.this.isEditable()) { return false;
/*     */         }
/* 395 */         boolean dataOnClipboard = false;
/*     */         try {
/* 397 */           dataOnClipboard = JXEditorPane.this.getToolkit().getSystemClipboard().getContents(null) != null;
/*     */         }
/*     */         catch (Exception e) {}
/*     */         
/*     */ 
/* 402 */         return dataOnClipboard;
/*     */       }
/* 404 */       boolean selectedText = JXEditorPane.this.getSelectionEnd() - JXEditorPane.this.getSelectionStart() > 0;
/*     */       
/* 406 */       if ("cut".equals(name)) {
/* 407 */         return (JXEditorPane.this.isEditable()) && (selectedText);
/*     */       }
/* 409 */       if ("copy".equals(name)) {
/* 410 */         return selectedText;
/*     */       }
/* 412 */       if ("find".equals(name)) {
/* 413 */         return JXEditorPane.this.getDocument().getLength() > 0;
/*     */       }
/* 415 */       return true;
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
/*     */   public JComboBox getParagraphSelector()
/*     */   {
/* 428 */     if (this.selector == null) {
/* 429 */       this.selector = new ParagraphSelector();
/*     */     }
/* 431 */     return this.selector;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ParagraphSelector
/*     */     extends JComboBox
/*     */     implements ItemListener
/*     */   {
/*     */     private Map<HTML.Tag, String> itemMap;
/*     */     
/*     */ 
/*     */     public ParagraphSelector()
/*     */     {
/* 445 */       this.itemMap = new HashMap();
/* 446 */       this.itemMap.put(HTML.Tag.P, "Paragraph");
/* 447 */       this.itemMap.put(HTML.Tag.H1, "Heading 1");
/* 448 */       this.itemMap.put(HTML.Tag.H2, "Heading 2");
/* 449 */       this.itemMap.put(HTML.Tag.H3, "Heading 3");
/* 450 */       this.itemMap.put(HTML.Tag.H4, "Heading 4");
/* 451 */       this.itemMap.put(HTML.Tag.H5, "Heading 5");
/* 452 */       this.itemMap.put(HTML.Tag.H6, "Heading 6");
/* 453 */       this.itemMap.put(HTML.Tag.PRE, "Preformatted");
/*     */       
/*     */ 
/* 456 */       Vector<HTML.Tag> items = new Vector();
/* 457 */       items.addElement(HTML.Tag.P);
/* 458 */       items.addElement(HTML.Tag.H1);
/* 459 */       items.addElement(HTML.Tag.H2);
/* 460 */       items.addElement(HTML.Tag.H3);
/* 461 */       items.addElement(HTML.Tag.H4);
/* 462 */       items.addElement(HTML.Tag.H5);
/* 463 */       items.addElement(HTML.Tag.H6);
/* 464 */       items.addElement(HTML.Tag.PRE);
/*     */       
/* 466 */       setModel(new DefaultComboBoxModel(items));
/* 467 */       setRenderer(new ParagraphRenderer());
/* 468 */       addItemListener(this);
/* 469 */       setFocusable(false);
/*     */     }
/*     */     
/*     */     public void itemStateChanged(ItemEvent evt) {
/* 473 */       if (evt.getStateChange() == 1) {
/* 474 */         JXEditorPane.this.applyTag((HTML.Tag)evt.getItem());
/*     */       }
/*     */     }
/*     */     
/*     */     private class ParagraphRenderer extends DefaultListCellRenderer
/*     */     {
/*     */       public ParagraphRenderer() {
/* 481 */         setOpaque(true);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*     */       {
/* 490 */         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*     */         
/*     */ 
/* 493 */         setText((String)JXEditorPane.ParagraphSelector.this.itemMap.get(value));
/*     */         
/* 495 */         return this;
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
/*     */   protected void applyTag(HTML.Tag tag)
/*     */   {
/* 508 */     Document doc = getDocument();
/* 509 */     if (!(doc instanceof HTMLDocument)) {
/* 510 */       return;
/*     */     }
/* 512 */     HTMLDocument hdoc = (HTMLDocument)doc;
/* 513 */     int start = getSelectionStart();
/* 514 */     int end = getSelectionEnd();
/*     */     
/* 516 */     Element element = hdoc.getParagraphElement(start);
/* 517 */     MutableAttributeSet newAttrs = new SimpleAttributeSet(element.getAttributes());
/* 518 */     newAttrs.addAttribute(StyleConstants.NameAttribute, tag);
/*     */     
/* 520 */     hdoc.setParagraphAttributes(start, end - start, newAttrs, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paste()
/*     */   {
/* 529 */     Clipboard clipboard = getToolkit().getSystemClipboard();
/* 530 */     Transferable content = clipboard.getContents(this);
/* 531 */     if (content != null) {
/* 532 */       DataFlavor[] flavors = content.getTransferDataFlavors();
/*     */       try {
/* 534 */         for (int i = 0; i < flavors.length; i++) {
/* 535 */           if (String.class.equals(flavors[i].getRepresentationClass())) {
/* 536 */             Object data = content.getTransferData(flavors[i]);
/*     */             
/* 538 */             if (flavors[i].isMimeTypeEqual("text/plain"))
/*     */             {
/* 540 */               replaceSelection(data.toString());
/* 541 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 547 */         LOG.log(Level.FINE, "What can produce a problem with data flavor?", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void find() {
/* 553 */     SearchFactory.getInstance().showFindInput(this, getSearchable());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Searchable getSearchable()
/*     */   {
/* 561 */     if (this.searchable == null) {
/* 562 */       this.searchable = new DocumentSearchable();
/*     */     }
/* 564 */     return this.searchable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSearchable(Searchable searchable)
/*     */   {
/* 574 */     this.searchable = searchable;
/*     */   }
/*     */   
/*     */   public class DocumentSearchable implements Searchable
/*     */   {
/*     */     public DocumentSearchable() {}
/*     */     
/*     */     public int search(String searchString) {
/* 582 */       return search(searchString, -1);
/*     */     }
/*     */     
/*     */     public int search(String searchString, int columnIndex) {
/* 586 */       return search(searchString, columnIndex, false);
/*     */     }
/*     */     
/*     */     public int search(String searchString, int columnIndex, boolean backward) {
/* 590 */       Pattern pattern = null;
/* 591 */       if (!isEmpty(searchString)) {
/* 592 */         pattern = Pattern.compile(searchString, 0);
/*     */       }
/* 594 */       return search(pattern, columnIndex, backward);
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
/*     */     protected boolean isEmpty(String searchString)
/*     */     {
/* 607 */       return (searchString == null) || (searchString.length() == 0);
/*     */     }
/*     */     
/*     */     public int search(Pattern pattern) {
/* 611 */       return search(pattern, -1);
/*     */     }
/*     */     
/*     */     public int search(Pattern pattern, int startIndex) {
/* 615 */       return search(pattern, startIndex, false);
/*     */     }
/*     */     
/* 618 */     int lastFoundIndex = -1;
/*     */     
/*     */     MatchResult lastMatchResult;
/*     */     
/*     */     String lastRegEx;
/*     */     
/*     */ 
/*     */     public int search(Pattern pattern, int startIndex, boolean backwards)
/*     */     {
/* 627 */       if ((pattern == null) || (JXEditorPane.this.getDocument().getLength() == 0) || ((startIndex > -1) && (JXEditorPane.this.getDocument().getLength() < startIndex)))
/*     */       {
/*     */ 
/* 630 */         updateStateAfterNotFound();
/* 631 */         return -1;
/*     */       }
/*     */       
/* 634 */       int start = startIndex;
/* 635 */       if (maybeExtendedMatch(startIndex)) {
/* 636 */         if (foundExtendedMatch(pattern, start)) {
/* 637 */           return this.lastFoundIndex;
/*     */         }
/* 639 */         start++;
/*     */       }
/*     */       int length;
/*     */       int length;
/* 643 */       if (backwards) {
/* 644 */         start = 0;
/* 645 */         int length; if (startIndex < 0) {
/* 646 */           length = JXEditorPane.this.getDocument().getLength() - 1;
/*     */         } else {
/* 648 */           length = -1 + startIndex;
/*     */         }
/*     */       }
/*     */       else {
/* 652 */         if (start < 0)
/* 653 */           start = 0;
/* 654 */         length = JXEditorPane.this.getDocument().getLength() - start;
/*     */       }
/* 656 */       Segment segment = new Segment();
/*     */       try
/*     */       {
/* 659 */         JXEditorPane.this.getDocument().getText(start, length, segment);
/*     */       } catch (BadLocationException ex) {
/* 661 */         JXEditorPane.LOG.log(Level.FINE, "this should not happen (calculated the valid start/length) ", ex);
/*     */       }
/*     */       
/*     */ 
/* 665 */       Matcher matcher = pattern.matcher(segment.toString());
/* 666 */       MatchResult currentResult = getMatchResult(matcher, !backwards);
/* 667 */       if (currentResult != null) {
/* 668 */         updateStateAfterFound(currentResult, start);
/*     */       } else {
/* 670 */         updateStateAfterNotFound();
/*     */       }
/* 672 */       return this.lastFoundIndex;
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
/*     */     private boolean foundExtendedMatch(Pattern pattern, int start)
/*     */     {
/* 690 */       if (pattern.pattern().equals(this.lastRegEx)) {
/* 691 */         return false;
/*     */       }
/* 693 */       int length = JXEditorPane.this.getDocument().getLength() - start;
/* 694 */       Segment segment = new Segment();
/*     */       try
/*     */       {
/* 697 */         JXEditorPane.this.getDocument().getText(start, length, segment);
/*     */       } catch (BadLocationException ex) {
/* 699 */         JXEditorPane.LOG.log(Level.FINE, "this should not happen (calculated the valid start/length) ", ex);
/*     */       }
/*     */       
/* 702 */       Matcher matcher = pattern.matcher(segment.toString());
/* 703 */       MatchResult currentResult = getMatchResult(matcher, true);
/* 704 */       if (currentResult != null)
/*     */       {
/*     */ 
/*     */ 
/* 708 */         if ((currentResult.start() == 0) && (!this.lastMatchResult.group().equals(currentResult.group())))
/*     */         {
/* 710 */           updateStateAfterFound(currentResult, start);
/* 711 */           return true;
/*     */         }
/*     */       }
/* 714 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean maybeExtendedMatch(int startIndex)
/*     */     {
/* 725 */       return (startIndex >= 0) && (startIndex == this.lastFoundIndex);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int updateStateAfterFound(MatchResult currentResult, int offset)
/*     */     {
/* 734 */       int end = currentResult.end() + offset;
/* 735 */       int found = currentResult.start() + offset;
/* 736 */       JXEditorPane.this.select(found, end);
/* 737 */       JXEditorPane.this.getCaret().setSelectionVisible(true);
/* 738 */       this.lastFoundIndex = found;
/* 739 */       this.lastMatchResult = currentResult;
/* 740 */       this.lastRegEx = ((Matcher)this.lastMatchResult).pattern().pattern();
/* 741 */       return found;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private MatchResult getMatchResult(Matcher matcher, boolean useFirst)
/*     */     {
/* 750 */       MatchResult currentResult = null;
/* 751 */       while (matcher.find()) {
/* 752 */         currentResult = matcher.toMatchResult();
/* 753 */         if (useFirst) break;
/*     */       }
/* 755 */       return currentResult;
/*     */     }
/*     */     
/*     */ 
/*     */     private void updateStateAfterNotFound()
/*     */     {
/* 761 */       this.lastFoundIndex = -1;
/* 762 */       this.lastMatchResult = null;
/* 763 */       this.lastRegEx = null;
/* 764 */       JXEditorPane.this.setCaretPosition(JXEditorPane.this.getSelectionEnd());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasCommand(Object command)
/*     */   {
/* 770 */     return this.targetSupport.hasCommand(command);
/*     */   }
/*     */   
/*     */   public Object[] getCommands() {
/* 774 */     return this.targetSupport.getCommands();
/*     */   }
/*     */   
/*     */   public boolean doCommand(Object command, Object value) {
/* 778 */     return this.targetSupport.doCommand(command, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
/*     */   {
/* 786 */     switch (orientation) {
/*     */     case 1: 
/* 788 */       return getFontMetrics(getFont()).getHeight();
/*     */     case 0: 
/* 790 */       return getFontMetrics(getFont()).charWidth('M');
/*     */     }
/* 792 */     throw new IllegalArgumentException("Invalid orientation: " + orientation);
/*     */   }
/*     */   
/*     */ 
/*     */   private class CaretHandler
/*     */     implements CaretListener
/*     */   {
/*     */     private CaretHandler() {}
/*     */     
/*     */ 
/*     */     public void caretUpdate(CaretEvent evt)
/*     */     {
/* 804 */       StyledDocument document = (StyledDocument)JXEditorPane.this.getDocument();
/* 805 */       int dot = evt.getDot();
/*     */       
/* 807 */       dot = dot > 0 ? dot - 1 : dot;
/*     */       
/* 809 */       Element elem = document.getCharacterElement(dot);
/* 810 */       AttributeSet set = elem.getAttributes();
/*     */       
/*     */ 
/* 813 */       ActionManager manager = ActionManager.getInstance();
/* 814 */       manager.setSelected("font-bold", StyleConstants.isBold(set));
/* 815 */       manager.setSelected("font-italic", StyleConstants.isItalic(set));
/* 816 */       manager.setSelected("font-underline", StyleConstants.isUnderline(set));
/*     */       
/* 818 */       elem = document.getParagraphElement(dot);
/* 819 */       set = elem.getAttributes();
/*     */       
/*     */ 
/* 822 */       if (JXEditorPane.this.selector != null) {
/* 823 */         JXEditorPane.this.selector.setSelectedItem(set.getAttribute(StyleConstants.NameAttribute));
/*     */       }
/*     */       
/* 826 */       switch (StyleConstants.getAlignment(set))
/*     */       {
/*     */ 
/*     */ 
/*     */       case 0: 
/* 831 */         manager.setSelected("left-justify", true);
/* 832 */         break;
/*     */       
/*     */       case 1: 
/* 835 */         manager.setSelected("center-justify", true);
/* 836 */         break;
/*     */       
/*     */       case 2: 
/* 839 */         manager.setSelected("right-justify", true);
/*     */       }
/*     */       
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
/*     */   private static final class SloppyHTMLEditorKit
/*     */     extends HTMLEditorKit
/*     */   {
/*     */     public void read(Reader in, Document doc, int pos)
/*     */       throws IOException, BadLocationException
/*     */     {
/* 860 */       StringBuffer buffer = new StringBuffer();
/*     */       
/* 862 */       char[] data = new char['Ѐ'];
/* 863 */       int length; while ((length = in.read(data)) != -1) {
/* 864 */         buffer.append(data, 0, length);
/*     */       }
/*     */       
/* 867 */       StringReader reader = new StringReader(buffer.toString().replaceAll("/>", ">"));
/* 868 */       super.read(reader, doc, pos);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXEditorPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */