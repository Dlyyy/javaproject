/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompoundHighlighter
/*     */   extends AbstractHighlighter
/*     */   implements UIDependent
/*     */ {
/*  45 */   public static final Highlighter[] EMPTY_HIGHLIGHTERS = new Highlighter[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<Highlighter> highlighters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChangeListener highlighterChangeListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompoundHighlighter(Highlighter... inList)
/*     */   {
/*  62 */     this(null, inList);
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
/*     */   public CompoundHighlighter(HighlightPredicate predicate, Highlighter... inList)
/*     */   {
/*  75 */     super(predicate);
/*  76 */     this.highlighters = new ArrayList();
/*  77 */     setHighlighters(inList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHighlighters(Highlighter... inList)
/*     */   {
/*  89 */     Contract.asNotNull(inList, "Highlighter must not be null");
/*  90 */     if ((this.highlighters.isEmpty()) && (inList.length == 0)) return;
/*  91 */     removeAllHighlightersSilently();
/*  92 */     for (Highlighter highlighter : inList) {
/*  93 */       addHighlighterSilently(highlighter, false);
/*     */     }
/*  95 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeAllHighlightersSilently()
/*     */   {
/* 103 */     for (Highlighter highlighter : this.highlighters) {
/* 104 */       highlighter.removeChangeListener(getHighlighterChangeListener());
/*     */     }
/* 106 */     this.highlighters.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addHighlighter(Highlighter highlighter)
/*     */   {
/* 116 */     addHighlighter(highlighter, false);
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
/*     */   public void addHighlighter(Highlighter highlighter, boolean prepend)
/*     */   {
/* 129 */     addHighlighterSilently(highlighter, prepend);
/* 130 */     fireStateChanged();
/*     */   }
/*     */   
/*     */   private void addHighlighterSilently(Highlighter highlighter, boolean prepend) {
/* 134 */     Contract.asNotNull(highlighter, "Highlighter must not be null");
/* 135 */     if (prepend) {
/* 136 */       this.highlighters.add(0, highlighter);
/*     */     } else {
/* 138 */       this.highlighters.add(this.highlighters.size(), highlighter);
/*     */     }
/* 140 */     updateUI(highlighter);
/* 141 */     highlighter.addChangeListener(getHighlighterChangeListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeHighlighter(Highlighter hl)
/*     */   {
/* 151 */     boolean success = this.highlighters.remove(hl);
/* 152 */     if (success)
/*     */     {
/* 154 */       hl.removeChangeListener(getHighlighterChangeListener());
/* 155 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Highlighter[] getHighlighters()
/*     */   {
/* 166 */     if (this.highlighters.isEmpty()) return EMPTY_HIGHLIGHTERS;
/* 167 */     return (Highlighter[])this.highlighters.toArray(new Highlighter[this.highlighters.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 178 */     for (Highlighter highlighter : this.highlighters) {
/* 179 */       updateUI(highlighter);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChangeListener getHighlighterChangeListener()
/*     */   {
/* 191 */     if (this.highlighterChangeListener == null) {
/* 192 */       this.highlighterChangeListener = createHighlighterChangeListener();
/*     */     }
/* 194 */     return this.highlighterChangeListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChangeListener createHighlighterChangeListener()
/*     */   {
/* 206 */     this. = new ChangeListener()
/*     */     {
/*     */       public void stateChanged(ChangeEvent e) {
/* 209 */         CompoundHighlighter.this.fireStateChanged();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateUI(Highlighter hl)
/*     */   {
/* 221 */     if ((hl instanceof UIDependent)) {
/* 222 */       ((UIDependent)hl).updateUI();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component stamp, ComponentAdapter adapter)
/*     */   {
/* 234 */     for (Highlighter highlighter : this.highlighters) {
/* 235 */       stamp = highlighter.highlight(stamp, adapter);
/*     */     }
/* 237 */     return stamp;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\CompoundHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */