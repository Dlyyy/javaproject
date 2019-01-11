/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
/*     */ import org.jdesktop.swingx.action.ActionContainerFactory;
/*     */ import org.jdesktop.swingx.search.PatternModel;
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
/*     */ public class JXFindPanel
/*     */   extends AbstractPatternPanel
/*     */ {
/*     */   public static final String FIND_NEXT_ACTION_COMMAND = "findNext";
/*     */   public static final String FIND_PREVIOUS_ACTION_COMMAND = "findPrevious";
/*     */   protected Searchable searchable;
/*     */   protected JCheckBox wrapCheck;
/*     */   protected JCheckBox backCheck;
/*     */   private boolean initialized;
/*     */   
/*     */   public JXFindPanel()
/*     */   {
/*  60 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXFindPanel(Searchable searchable)
/*     */   {
/*  70 */     setName(getUIString("searchTitle"));
/*  71 */     setSearchable(searchable);
/*  72 */     initActions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSearchable(Searchable searchable)
/*     */   {
/*  84 */     if ((this.searchable != null) && (this.searchable.equals(searchable))) return;
/*  85 */     Searchable old = this.searchable;
/*  86 */     if (old != null) {
/*  87 */       old.search((Pattern)null);
/*     */     }
/*  89 */     this.searchable = searchable;
/*  90 */     getPatternModel().setFoundIndex(-1);
/*  91 */     firePropertyChange("searchable", old, this.searchable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 101 */     init();
/* 102 */     super.addNotify();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void init()
/*     */   {
/* 109 */     if (this.initialized) return;
/* 110 */     this.initialized = true;
/* 111 */     initComponents();
/* 112 */     build();
/* 113 */     bind();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void bind()
/*     */   {
/* 124 */     super.bind();
/* 125 */     getActionContainerFactory().configureButton(this.wrapCheck, getAction("wrapSearch"), null);
/*     */     
/*     */ 
/* 128 */     getActionContainerFactory().configureButton(this.backCheck, getAction("backwardsSearch"), null);
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
/*     */   protected void refreshEmptyFromModel()
/*     */   {
/* 143 */     super.refreshEmptyFromModel();
/* 144 */     boolean enabled = !getPatternModel().isEmpty();
/* 145 */     getAction("findNext").setEnabled(enabled);
/* 146 */     getAction("findPrevious").setEnabled(enabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void match()
/*     */   {
/* 157 */     doFind();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void findNext()
/*     */   {
/* 165 */     getPatternModel().setBackwards(false);
/* 166 */     doFind();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void findPrevious()
/*     */   {
/* 174 */     getPatternModel().setBackwards(true);
/* 175 */     doFind();
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
/*     */   protected void doFind()
/*     */   {
/* 189 */     if (this.searchable == null)
/* 190 */       return;
/* 191 */     int foundIndex = doSearch();
/* 192 */     boolean notFound = (foundIndex == -1) && (!getPatternModel().isEmpty());
/* 193 */     if ((notFound) && 
/* 194 */       (getPatternModel().isWrapping())) {
/* 195 */       notFound = doSearch() == -1;
/*     */     }
/*     */     
/* 198 */     if (notFound) {
/* 199 */       showNotFoundMessage();
/*     */     } else {
/* 201 */       showFoundMessage();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int doSearch()
/*     */   {
/* 211 */     int foundIndex = this.searchable.search(getPatternModel().getPattern(), getPatternModel().getFoundIndex(), getPatternModel().isBackwards());
/*     */     
/* 213 */     getPatternModel().setFoundIndex(foundIndex);
/* 214 */     return getPatternModel().getFoundIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void showFoundMessage() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void showNotFoundMessage()
/*     */   {
/* 231 */     JOptionPane.showMessageDialog(this, getUIString("notFound"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateLocaleState(Locale locale)
/*     */   {
/* 241 */     super.updateLocaleState(locale);
/* 242 */     setName(getUIString("searchTitle", locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initExecutables()
/*     */   {
/* 254 */     getActionMap().put("findNext", createBoundAction("findNext", "findNext"));
/*     */     
/* 256 */     getActionMap().put("findPrevious", createBoundAction("findPrevious", "findPrevious"));
/*     */     
/* 258 */     super.initExecutables();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initComponents()
/*     */   {
/* 270 */     super.initComponents();
/* 271 */     this.wrapCheck = new JCheckBox();
/* 272 */     this.backCheck = new JCheckBox();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void build()
/*     */   {
/* 281 */     Box lBox = new Box(2);
/* 282 */     lBox.add(this.searchLabel);
/* 283 */     lBox.add(new JLabel(":"));
/* 284 */     lBox.add(new JLabel("  "));
/* 285 */     lBox.setAlignmentY(0.0F);
/* 286 */     Box rBox = new Box(3);
/* 287 */     rBox.add(this.searchField);
/* 288 */     rBox.add(this.matchCheck);
/* 289 */     rBox.add(this.wrapCheck);
/* 290 */     rBox.add(this.backCheck);
/* 291 */     rBox.setAlignmentY(0.0F);
/*     */     
/* 293 */     setLayout(new BoxLayout(this, 2));
/*     */     
/* 295 */     add(lBox);
/* 296 */     add(rBox);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXFindPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */