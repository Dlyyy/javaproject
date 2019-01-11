/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FlowLayout;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.KeyStroke;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXFindBar
/*     */   extends JXFindPanel
/*     */ {
/*     */   protected Color previousBackgroundColor;
/*     */   protected Color previousForegroundColor;
/*  71 */   protected Color notFoundBackgroundColor = Color.decode("#FF6666");
/*     */   
/*  73 */   protected Color notFoundForegroundColor = Color.white;
/*     */   
/*     */   protected JButton findNext;
/*     */   protected JButton findPrevious;
/*     */   
/*     */   public JXFindBar()
/*     */   {
/*  80 */     this(null);
/*     */   }
/*     */   
/*     */   public JXFindBar(Searchable searchable) {
/*  84 */     super(searchable);
/*  85 */     getPatternModel().setIncremental(true);
/*  86 */     getPatternModel().setWrapping(true);
/*     */   }
/*     */   
/*     */   public void setSearchable(Searchable searchable)
/*     */   {
/*  91 */     super.setSearchable(searchable);
/*  92 */     match();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void showNotFoundMessage()
/*     */   {
/* 101 */     if (this.searchField == null) return;
/* 102 */     this.searchField.setForeground(this.notFoundForegroundColor);
/* 103 */     this.searchField.setBackground(this.notFoundBackgroundColor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void showFoundMessage()
/*     */   {
/* 112 */     if (this.searchField == null) return;
/* 113 */     this.searchField.setBackground(this.previousBackgroundColor);
/* 114 */     this.searchField.setForeground(this.previousForegroundColor);
/*     */   }
/*     */   
/*     */   public void addNotify()
/*     */   {
/* 119 */     super.addNotify();
/* 120 */     if (this.previousBackgroundColor == null) {
/* 121 */       this.previousBackgroundColor = this.searchField.getBackground();
/* 122 */       this.previousForegroundColor = this.searchField.getForeground();
/*     */     } else {
/* 124 */       this.searchField.setBackground(this.previousBackgroundColor);
/* 125 */       this.searchField.setForeground(this.previousForegroundColor);
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
/*     */   public void cancel() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initExecutables()
/*     */   {
/* 146 */     getActionMap().put("close", createBoundAction("close", "cancel"));
/*     */     
/* 148 */     super.initExecutables();
/*     */   }
/*     */   
/*     */   protected void bind()
/*     */   {
/* 153 */     super.bind();
/* 154 */     this.searchField.addActionListener(getAction("execute"));
/*     */     
/* 156 */     this.findNext.setAction(getAction("findNext"));
/* 157 */     this.findPrevious.setAction(getAction("findPrevious"));
/* 158 */     KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
/* 159 */     getInputMap(1).put(stroke, "close");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void build()
/*     */   {
/* 165 */     setLayout(new FlowLayout(10));
/* 166 */     add(this.searchLabel);
/* 167 */     add(new JLabel(":"));
/* 168 */     add(new JLabel("  "));
/* 169 */     add(this.searchField);
/* 170 */     add(this.findNext);
/* 171 */     add(this.findPrevious);
/*     */   }
/*     */   
/*     */   protected void initComponents()
/*     */   {
/* 176 */     super.initComponents();
/* 177 */     this.findNext = new JButton();
/* 178 */     this.findPrevious = new JButton();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXFindBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */