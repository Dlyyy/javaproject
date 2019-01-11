/*    */ package org.jdesktop.swingx.treetable;
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
/*    */ public class DefaultMutableTreeTableNode
/*    */   extends AbstractMutableTreeTableNode
/*    */ {
/*    */   public DefaultMutableTreeTableNode() {}
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
/*    */   public DefaultMutableTreeTableNode(Object userObject)
/*    */   {
/* 46 */     super(userObject);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultMutableTreeTableNode(Object userObject, boolean allowsChildren)
/*    */   {
/* 54 */     super(userObject, allowsChildren);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getValueAt(int column)
/*    */   {
/* 62 */     return getUserObject();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getColumnCount()
/*    */   {
/* 70 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEditable(int column)
/*    */   {
/* 78 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setValueAt(Object aValue, int column)
/*    */   {
/* 86 */     setUserObject(aValue);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\DefaultMutableTreeTableNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */