/*    */ package org.jdesktop.swingx.tree;
/*    */ 
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.tree.DefaultTreeCellRenderer;
/*    */ import org.jdesktop.swingx.SwingXUtilities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultXTreeCellRenderer
/*    */   extends DefaultTreeCellRenderer
/*    */ {
/*    */   public void updateUI()
/*    */   {
/* 48 */     super.updateUI();
/* 49 */     updateIcons();
/* 50 */     updateColors();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void updateColors()
/*    */   {
/* 57 */     if (SwingXUtilities.isUIInstallable(getTextSelectionColor())) {
/* 58 */       setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));
/*    */     }
/* 60 */     if (SwingXUtilities.isUIInstallable(getTextNonSelectionColor())) {
/* 61 */       setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));
/*    */     }
/* 63 */     if (SwingXUtilities.isUIInstallable(getBackgroundSelectionColor())) {
/* 64 */       setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
/*    */     }
/* 66 */     if (SwingXUtilities.isUIInstallable(getBackgroundNonSelectionColor())) {
/* 67 */       setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));
/*    */     }
/* 69 */     if (SwingXUtilities.isUIInstallable(getBorderSelectionColor())) {
/* 70 */       setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateIcons()
/*    */   {
/* 84 */     if (SwingXUtilities.isUIInstallable(getLeafIcon())) {
/* 85 */       setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
/*    */     }
/* 87 */     if (SwingXUtilities.isUIInstallable(getClosedIcon())) {
/* 88 */       setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
/*    */     }
/* 90 */     if (SwingXUtilities.isUIInstallable(getOpenIcon())) {
/* 91 */       setOpenIcon(UIManager.getIcon("Tree.openIcon"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tree\DefaultXTreeCellRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */