/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import org.jdesktop.swingx.search.PatternMatcher;
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
/*     */ public class JXSearchPanel
/*     */   extends AbstractPatternPanel
/*     */ {
/*     */   public static final String MATCH_RULE_ACTION_COMMAND = "selectMatchRule";
/*     */   private JComboBox searchCriteria;
/*     */   private List<PatternMatcher> patternMatchers;
/*     */   
/*     */   public JXSearchPanel()
/*     */   {
/*  88 */     initComponents();
/*  89 */     build();
/*  90 */     initActions();
/*  91 */     bind();
/*  92 */     getPatternModel().setIncremental(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPatternMatcher(PatternMatcher matcher)
/*     */   {
/* 104 */     getPatternMatchers().add(matcher);
/* 105 */     updateFieldName(matcher);
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
/*     */   public void setFieldName(String name)
/*     */   {
/* 126 */     String old = this.searchLabel.getText();
/* 127 */     this.searchLabel.setText(name);
/* 128 */     firePropertyChange("fieldName", old, this.searchLabel.getText());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFieldName()
/*     */   {
/* 136 */     return this.searchLabel.getText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pattern getPattern()
/*     */   {
/* 145 */     return this.patternModel.getPattern();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateFieldName(PatternMatcher matcher)
/*     */   {
/* 157 */     if (this.searchLabel.getText().length() == 0) {
/* 158 */       this.searchLabel.setText("Field");
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
/*     */   public void match()
/*     */   {
/* 171 */     for (Iterator<PatternMatcher> iter = getPatternMatchers().iterator(); iter.hasNext();) {
/* 172 */       ((PatternMatcher)iter.next()).setPattern(getPattern());
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
/*     */   public void updateMatchRule()
/*     */   {
/* 185 */     getPatternModel().setMatchRule((String)this.searchCriteria.getSelectedItem());
/*     */   }
/*     */   
/*     */   private List<PatternMatcher> getPatternMatchers()
/*     */   {
/* 190 */     if (this.patternMatchers == null) {
/* 191 */       this.patternMatchers = new ArrayList();
/*     */     }
/* 193 */     return this.patternMatchers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initExecutables()
/*     */   {
/* 200 */     super.initExecutables();
/* 201 */     getActionMap().put("selectMatchRule", createBoundAction("selectMatchRule", "updateMatchRule"));
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
/*     */   protected void bind()
/*     */   {
/* 215 */     super.bind();
/* 216 */     List<?> matchRules = getPatternModel().getMatchRules();
/*     */     
/* 218 */     ComboBoxModel model = new DefaultComboBoxModel(matchRules.toArray());
/* 219 */     model.setSelectedItem(getPatternModel().getMatchRule());
/* 220 */     this.searchCriteria.setModel(model);
/* 221 */     this.searchCriteria.setAction(getAction("selectMatchRule"));
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
/*     */   private void build()
/*     */   {
/* 234 */     add(this.searchLabel);
/* 235 */     add(this.searchCriteria);
/* 236 */     add(this.searchField);
/* 237 */     add(this.matchCheck);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initComponents()
/*     */   {
/* 247 */     super.initComponents();
/* 248 */     this.searchCriteria = new JComboBox();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXSearchPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */