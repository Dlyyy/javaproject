/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.plaf.AbstractUIChangeHandler;
/*     */ import org.jdesktop.swingx.util.OS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NativeSearchFieldSupport
/*     */ {
/*     */   public static final String FIND_POPUP_PROPERTY = "JTextField.Search.FindPopup";
/*     */   public static final String FIND_ACTION_PROPERTY = "JTextField.Search.FindAction";
/*     */   public static final String MAC_SEARCH_VARIANT = "search";
/*     */   public static final String MAC_TEXT_FIELD_VARIANT_PROPERTY = "JTextField.variant";
/*     */   public static final String CANCEL_ACTION_PROPERTY = "JTextField.Search.CancelAction";
/*     */   
/*     */   public static boolean isNativeSearchFieldSupported()
/*     */   {
/*     */     try
/*     */     {
/*  30 */       String versionString = System.getProperty("os.version");
/*     */       
/*  32 */       if (versionString.length() < 4) {
/*  33 */         return false;
/*     */       }
/*     */       
/*  36 */       versionString = versionString.substring(0, 4);
/*     */       
/*  38 */       return (OS.isMacOSX()) && (Float.parseFloat(versionString) >= 10.5D) && (UIManager.getLookAndFeel().getName().equals("Mac OS X"));
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */ 
/*  43 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setSearchField(JTextField txt, boolean isSearchField)
/*     */   {
/*  50 */     if (isSearchField == isSearchField(txt)) {
/*  51 */       txt.putClientProperty("JTextField.variant", "_triggerevent_");
/*  52 */     } else if (isSearchField)
/*     */     {
/*     */ 
/*  55 */       uiChangeHandler.install(txt);
/*     */     }
/*     */     else {
/*  58 */       uiChangeHandler.uninstall(txt);
/*     */     }
/*     */     
/*  61 */     if (isSearchField) {
/*  62 */       txt.putClientProperty("JTextField.variant", "search");
/*  63 */       txt.putClientProperty("Quaqua.TextField.style", "search");
/*     */     }
/*     */     else {
/*  66 */       txt.putClientProperty("JTextField.variant", "default");
/*  67 */       txt.putClientProperty("Quaqua.TextField.style", "default");
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isSearchField(JTextField txt) {
/*  72 */     return "search".equals(txt.getClientProperty("JTextField.variant"));
/*     */   }
/*     */   
/*     */   public static boolean isNativeSearchField(JTextField txt) {
/*  76 */     return (isSearchField(txt)) && (isNativeSearchFieldSupported());
/*     */   }
/*     */   
/*     */   public static void setFindPopupMenu(JTextField txt, JPopupMenu popupMenu) {
/*  80 */     txt.putClientProperty("JTextField.Search.FindPopup", popupMenu);
/*     */   }
/*     */   
/*     */   public static JPopupMenu getFindPopupMenu(JTextField txt) {
/*  84 */     return (JPopupMenu)txt.getClientProperty("JTextField.Search.FindPopup");
/*     */   }
/*     */   
/*     */   public static void setFindAction(JTextField txt, ActionListener findAction) {
/*  88 */     txt.putClientProperty("JTextField.Search.FindAction", findAction);
/*     */   }
/*     */   
/*     */   public static ActionListener getFindAction(JTextField txt) {
/*  92 */     return (ActionListener)txt.getClientProperty("JTextField.Search.FindAction");
/*     */   }
/*     */   
/*     */   public static void setCancelAction(JTextField txt, ActionListener cancelAction) {
/*  96 */     txt.putClientProperty("JTextField.Search.CancelAction", cancelAction);
/*     */   }
/*     */   
/*     */   public static ActionListener getCancelAction(JTextField txt) {
/* 100 */     return (ActionListener)txt.getClientProperty("JTextField.Search.CancelAction");
/*     */   }
/*     */   
/* 103 */   private static final SearchFieldUIChangeHandler uiChangeHandler = new SearchFieldUIChangeHandler(null);
/*     */   
/*     */   private static final class SearchFieldUIChangeHandler extends AbstractUIChangeHandler {
/*     */     public void propertyChange(PropertyChangeEvent evt) {
/* 107 */       JTextField txt = (JTextField)evt.getSource();
/*     */       
/*     */ 
/* 110 */       NativeSearchFieldSupport.setSearchField(txt, NativeSearchFieldSupport.isSearchField(txt));
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\NativeSearchFieldSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */