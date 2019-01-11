/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Locale;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.Document;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
/*     */ import org.jdesktop.swingx.action.ActionContainerFactory;
/*     */ import org.jdesktop.swingx.action.BoundAction;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.search.PatternModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPatternPanel
/*     */   extends JXPanel
/*     */ {
/*     */   public static final String SEARCH_FIELD_LABEL = "searchFieldLabel";
/*     */   public static final String SEARCH_FIELD_MNEMONIC = "searchFieldLabel.mnemonic";
/*     */   public static final String SEARCH_TITLE = "searchTitle";
/*     */   public static final String MATCH_ACTION_COMMAND = "match";
/*     */   protected JLabel searchLabel;
/*     */   protected JTextField searchField;
/*     */   protected JCheckBox matchCheck;
/*     */   protected PatternModel patternModel;
/*     */   private ActionContainerFactory actionFactory;
/*     */   
/*     */   static
/*     */   {
/*  67 */     LookAndFeelAddons.getAddon();
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
/*     */ 
/*     */   protected AbstractActionExt getAction(String key)
/*     */   {
/*  94 */     return (AbstractActionExt)getActionMap().get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initActions()
/*     */   {
/* 101 */     initPatternActions();
/* 102 */     initExecutables();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initExecutables()
/*     */   {
/* 113 */     Action execute = createBoundAction("match", "match");
/* 114 */     getActionMap().put("execute", execute);
/*     */     
/* 116 */     getActionMap().put("match", execute);
/* 117 */     refreshEmptyFromModel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initPatternActions()
/*     */   {
/* 124 */     ActionMap map = getActionMap();
/* 125 */     map.put("matchCase", createModelStateAction("matchCase", "setCaseSensitive", getPatternModel().isCaseSensitive()));
/*     */     
/*     */ 
/* 128 */     map.put("wrapSearch", createModelStateAction("wrapSearch", "setWrapping", getPatternModel().isWrapping()));
/*     */     
/*     */ 
/* 131 */     map.put("backwardsSearch", createModelStateAction("backwardsSearch", "setBackwards", getPatternModel().isBackwards()));
/*     */     
/*     */ 
/* 134 */     map.put("matchIncremental", createModelStateAction("matchIncremental", "setIncremental", getPatternModel().isIncremental()));
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
/*     */   protected String getUIString(String key)
/*     */   {
/* 149 */     return getUIString(key, getLocale());
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
/*     */   protected String getUIString(String key, Locale locale)
/*     */   {
/* 164 */     String text = UIManagerExt.getString("Search." + key, locale);
/* 165 */     return text != null ? text : key;
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
/*     */   protected AbstractActionExt createModelStateAction(String command, String methodName, boolean initial)
/*     */   {
/* 179 */     String actionName = getUIString(command);
/* 180 */     BoundAction action = new BoundAction(actionName, command);
/*     */     
/* 182 */     action.setStateAction();
/* 183 */     action.registerCallback(getPatternModel(), methodName);
/* 184 */     action.setSelected(initial);
/* 185 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractActionExt createBoundAction(String actionCommand, String methodName)
/*     */   {
/* 197 */     String actionName = getUIString(actionCommand);
/* 198 */     BoundAction action = new BoundAction(actionName, actionCommand);
/*     */     
/* 200 */     action.registerCallback(this, methodName);
/* 201 */     return action;
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
/*     */   public void setLocale(Locale l)
/*     */   {
/* 215 */     updateLocaleState(l);
/* 216 */     super.setLocale(l);
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
/*     */   protected void updateLocaleState(Locale locale)
/*     */   {
/* 231 */     for (Object key : getActionMap().allKeys()) {
/* 232 */       if ((key instanceof String)) {
/* 233 */         String keyString = getUIString((String)key, locale);
/* 234 */         if (!key.equals(keyString)) {
/* 235 */           getActionMap().get(key).putValue("Name", keyString);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 240 */     bindSearchLabel(locale);
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
/*     */   protected void refreshPatternFromModel()
/*     */   {
/* 254 */     if (getPatternModel().isIncremental()) {
/* 255 */       match();
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
/*     */   protected PatternModel getPatternModel()
/*     */   {
/* 268 */     if (this.patternModel == null) {
/* 269 */       this.patternModel = createPatternModel();
/* 270 */       this.patternModel.addPropertyChangeListener(getPatternModelListener());
/*     */     }
/* 272 */     return this.patternModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PatternModel createPatternModel()
/*     */   {
/* 283 */     return new PatternModel();
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
/*     */   protected PropertyChangeListener getPatternModelListener()
/*     */   {
/* 296 */     new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 298 */         String property = evt.getPropertyName();
/* 299 */         if ("pattern".equals(property)) {
/* 300 */           AbstractPatternPanel.this.refreshPatternFromModel();
/* 301 */         } else if ("rawText".equals(property)) {
/* 302 */           AbstractPatternPanel.this.refreshDocumentFromModel();
/* 303 */         } else if ("caseSensitive".equals(property)) {
/* 304 */           AbstractPatternPanel.this.getAction("matchCase").setSelected(((Boolean)evt.getNewValue()).booleanValue());
/*     */         }
/* 306 */         else if ("wrapping".equals(property)) {
/* 307 */           AbstractPatternPanel.this.getAction("wrapSearch").setSelected(((Boolean)evt.getNewValue()).booleanValue());
/*     */         }
/* 309 */         else if ("backwards".equals(property)) {
/* 310 */           AbstractPatternPanel.this.getAction("backwardsSearch").setSelected(((Boolean)evt.getNewValue()).booleanValue());
/*     */         }
/* 312 */         else if ("incremental".equals(property)) {
/* 313 */           AbstractPatternPanel.this.getAction("matchIncremental").setSelected(((Boolean)evt.getNewValue()).booleanValue());
/*     */ 
/*     */         }
/* 316 */         else if ("empty".equals(property)) {
/* 317 */           AbstractPatternPanel.this.refreshEmptyFromModel();
/*     */         }
/*     */       }
/*     */     };
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
/*     */   protected void refreshEmptyFromModel()
/*     */   {
/* 333 */     boolean enabled = !getPatternModel().isEmpty();
/* 334 */     getAction("match").setEnabled(enabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void refreshModelFromDocument()
/*     */   {
/* 343 */     getPatternModel().setRawText(this.searchField.getText());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void refreshDocumentFromModel()
/*     */   {
/* 351 */     if (this.searchField.getText().equals(getPatternModel().getRawText())) return;
/* 352 */     SwingUtilities.invokeLater(new Runnable() {
/*     */       public void run() {
/* 354 */         AbstractPatternPanel.this.searchField.setText(AbstractPatternPanel.this.getPatternModel().getRawText());
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DocumentListener getSearchFieldListener()
/*     */   {
/* 366 */     new DocumentListener()
/*     */     {
/*     */       public void changedUpdate(DocumentEvent ev) {
/* 369 */         AbstractPatternPanel.this.refreshModelFromDocument();
/*     */       }
/*     */       
/*     */       public void insertUpdate(DocumentEvent ev) {
/* 373 */         AbstractPatternPanel.this.refreshModelFromDocument();
/*     */       }
/*     */       
/*     */       public void removeUpdate(DocumentEvent ev) {
/* 377 */         AbstractPatternPanel.this.refreshModelFromDocument();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void bind()
/*     */   {
/* 389 */     bindSearchLabel(getLocale());
/* 390 */     this.searchField.getDocument().addDocumentListener(getSearchFieldListener());
/* 391 */     getActionContainerFactory().configureButton(this.matchCheck, (AbstractActionExt)getActionMap().get("matchCase"), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void bindSearchLabel(Locale locale)
/*     */   {
/* 403 */     this.searchLabel.setText(getUIString("searchFieldLabel", locale));
/* 404 */     String mnemonic = getUIString("searchFieldLabel.mnemonic", locale);
/* 405 */     if (mnemonic != "searchFieldLabel.mnemonic") {
/* 406 */       this.searchLabel.setDisplayedMnemonic(mnemonic.charAt(0));
/*     */     }
/* 408 */     this.searchLabel.setLabelFor(this.searchField);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ActionContainerFactory getActionContainerFactory()
/*     */   {
/* 416 */     if (this.actionFactory == null) {
/* 417 */       this.actionFactory = new ActionContainerFactory(null);
/*     */     }
/* 419 */     return this.actionFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initComponents()
/*     */   {
/* 426 */     this.searchLabel = new JLabel();
/* 427 */     this.searchField = new JTextField(getSearchFieldWidth())
/*     */     {
/*     */       public Dimension getMaximumSize() {
/* 430 */         Dimension superMax = super.getMaximumSize();
/* 431 */         superMax.height = getPreferredSize().height;
/* 432 */         return superMax;
/*     */       }
/* 434 */     };
/* 435 */     this.matchCheck = new JCheckBox();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int getSearchFieldWidth()
/*     */   {
/* 442 */     return 15;
/*     */   }
/*     */   
/*     */   public abstract void match();
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\AbstractPatternPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */