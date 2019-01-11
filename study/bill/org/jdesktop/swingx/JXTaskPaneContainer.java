/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.event.ContainerAdapter;
/*     */ import java.awt.event.ContainerEvent;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.TaskPaneContainerAddon;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXTaskPaneContainer
/*     */   extends JXPanel
/*     */ {
/*     */   public static final String uiClassID = "swingx/TaskPaneContainerUI";
/*     */   
/*     */   static
/*     */   {
/* 103 */     LookAndFeelAddons.contribute(new TaskPaneContainerAddon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JXTaskPaneContainer()
/*     */   {
/* 110 */     super(null);
/* 111 */     updateUI();
/*     */     
/* 113 */     addContainerListener(new ContainerAdapter()
/*     */     {
/*     */       public void componentRemoved(ContainerEvent e) {
/* 116 */         JXTaskPaneContainer.this.repaint();
/*     */       }
/* 118 */     });
/* 119 */     setScrollableHeightHint(ScrollableSizeHint.VERTICAL_STRETCH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaskPaneContainerUI getUI()
/*     */   {
/* 127 */     return (TaskPaneContainerUI)super.getUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 139 */     setUI((TaskPaneContainerUI)LookAndFeelAddons.getUI(this, TaskPaneContainerUI.class));
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
/*     */   public void setUI(TaskPaneContainerUI ui)
/*     */   {
/* 153 */     super.setUI(ui);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 165 */     return "swingx/TaskPaneContainerUI";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void add(JXTaskPane group)
/*     */   {
/* 176 */     super.add(group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void remove(JXTaskPane group)
/*     */   {
/* 187 */     super.remove(group);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTaskPaneContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */