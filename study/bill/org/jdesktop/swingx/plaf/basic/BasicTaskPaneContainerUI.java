/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.JXTaskPaneContainer;
/*     */ import org.jdesktop.swingx.VerticalLayout;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.TaskPaneContainerUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicTaskPaneContainerUI
/*     */   extends TaskPaneContainerUI
/*     */ {
/*     */   protected JXTaskPaneContainer taskPane;
/*     */   
/*     */   protected class VerticalLayoutUIResource
/*     */     extends VerticalLayout
/*     */     implements UIResource
/*     */   {
/*     */     public VerticalLayoutUIResource() {}
/*     */     
/*     */     public VerticalLayoutUIResource(int gap)
/*     */     {
/*  62 */       super();
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
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  75 */     return new BasicTaskPaneContainerUI();
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
/*     */   public void installUI(JComponent c)
/*     */   {
/*  88 */     super.installUI(c);
/*  89 */     this.taskPane = ((JXTaskPaneContainer)c);
/*  90 */     installDefaults();
/*     */     
/*  92 */     LayoutManager manager = this.taskPane.getLayout();
/*     */     
/*  94 */     if ((manager == null) || ((manager instanceof UIResource))) {
/*  95 */       this.taskPane.setLayout(createDefaultLayout());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 104 */     LookAndFeel.installColors(this.taskPane, "TaskPaneContainer.background", "TaskPaneContainer.foreground");
/*     */     
/* 106 */     LookAndFeel.installBorder(this.taskPane, "TaskPaneContainer.border");
/* 107 */     LookAndFeelAddons.installBackgroundPainter(this.taskPane, "TaskPaneContainer.backgroundPainter");
/*     */     
/* 109 */     LookAndFeel.installProperty(this.taskPane, "opaque", Boolean.TRUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LayoutManager createDefaultLayout()
/*     */   {
/* 117 */     return new VerticalLayoutUIResource(14);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 125 */     uninstallDefaults();
/*     */     
/* 127 */     super.uninstallUI(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 135 */     LookAndFeel.uninstallBorder(this.taskPane);
/* 136 */     LookAndFeelAddons.uninstallBackgroundPainter(this.taskPane);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicTaskPaneContainerUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */