/*     */ package org.jdesktop.swingx.plaf.macosx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import org.jdesktop.swingx.JXErrorPane;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
/*     */ import org.jdesktop.swingx.error.ErrorInfo;
/*     */ import org.jdesktop.swingx.error.ErrorLevel;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.plaf.basic.BasicErrorPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacOSXErrorPaneUI
/*     */   extends BasicErrorPaneUI
/*     */ {
/*     */   private JLabel titleLabel;
/*     */   private JEditorPane disclaimerText;
/*     */   
/*     */   protected void configureDetailsButton(boolean expanded)
/*     */   {
/*  85 */     if (expanded) {
/*  86 */       this.detailButton.setText(UIManagerExt.getString("JXErrorPane.details_contract_text", this.detailButton.getLocale()));
/*  87 */       this.detailButton.setIcon(UIManager.getIcon("Tree.expandedIcon"));
/*     */     } else {
/*  89 */       this.detailButton.setText(UIManagerExt.getString("JXErrorPane.details_expand_text", this.detailButton.getLocale()));
/*  90 */       this.detailButton.setIcon(UIManager.getIcon("Tree.collapsedIcon"));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void configureReportAction(AbstractActionExt reportAction)
/*     */   {
/*  96 */     reportAction.setName(UIManagerExt.getString("JXErrorPane.report_button_text", this.pane.getLocale()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/* 103 */     return new MacOSXErrorPaneUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JFrame getErrorFrame(Component owner)
/*     */   {
/* 111 */     JFrame frame = super.getErrorFrame(owner);
/* 112 */     frame.setTitle(" ");
/* 113 */     return frame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JDialog getErrorDialog(Component owner)
/*     */   {
/* 121 */     JDialog dlg = super.getErrorDialog(owner);
/* 122 */     dlg.setTitle(" ");
/* 123 */     return dlg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JInternalFrame getErrorInternalFrame(Component owner)
/*     */   {
/* 131 */     JInternalFrame frame = super.getErrorInternalFrame(owner);
/* 132 */     frame.setTitle(" ");
/* 133 */     return frame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LayoutManager createErrorPaneLayout()
/*     */   {
/* 141 */     createExtraComponents();
/* 142 */     GridBagLayout layout = new GridBagLayout();
/*     */     try {
/* 144 */       layout.addLayoutComponent(this.iconLabel, new GridBagConstraints(0, 0, 1, 2, 0.0D, 0.0D, 11, 0, new Insets(0, 0, 0, 17), 0, 0));
/* 145 */       layout.addLayoutComponent(this.titleLabel, new GridBagConstraints(1, 0, 2, 1, 1.0D, 0.0D, 11, 2, new Insets(0, 0, 12, 0), 0, 0));
/* 146 */       layout.addLayoutComponent(this.errorScrollPane, new GridBagConstraints(1, 1, 2, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 10, 0), 0, 0));
/* 147 */       layout.addLayoutComponent(this.detailButton, new GridBagConstraints(0, 2, 3, 1, 1.0D, 0.0D, 21, 0, new Insets(0, 0, 6, 0), 0, 0));
/* 148 */       layout.addLayoutComponent(this.detailsPanel, new GridBagConstraints(0, 3, 3, 1, 1.0D, 1.0D, 21, 1, new Insets(0, 0, 6, 0), 0, 0));
/* 149 */       layout.addLayoutComponent(this.disclaimerText, new GridBagConstraints(0, 4, 3, 1, 1.0D, 0.0D, 21, 2, new Insets(0, 0, 6, 0), 0, 0));
/* 150 */       layout.addLayoutComponent(this.closeButton, new GridBagConstraints(1, 5, 1, 1, 1.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 5), 0, 0));
/* 151 */       layout.addLayoutComponent(this.reportButton, new GridBagConstraints(2, 5, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     } catch (Exception e) {
/* 153 */       e.printStackTrace();
/*     */     }
/* 155 */     return layout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LayoutManager createDetailPanelLayout()
/*     */   {
/* 163 */     GridBagLayout layout = new GridBagLayout();
/* 164 */     layout.addLayoutComponent(this.detailsScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/* 165 */     this.copyToClipboardButton.setVisible(false);
/* 166 */     return layout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reinit()
/*     */   {
/* 174 */     super.reinit();
/* 175 */     ErrorInfo info = this.pane == null ? null : this.pane.getErrorInfo();
/* 176 */     this.titleLabel.setText(info == null ? "Unknown Error" : info.getTitle());
/*     */     
/* 178 */     Object finePrint = this.pane.getClientProperty("fine-print");
/* 179 */     String text = finePrint == null ? null : finePrint.toString();
/* 180 */     this.disclaimerText.setText(text);
/* 181 */     this.disclaimerText.setVisible(text != null);
/*     */     
/* 183 */     if ((info != null) && (info.getErrorLevel() == ErrorLevel.FATAL)) {
/* 184 */       this.closeButton.setText(UIManagerExt.getString("JXErrorPane.fatal_button_text", this.closeButton.getLocale()));
/*     */     } else {
/* 186 */       this.closeButton.setText(UIManagerExt.getString("JXErrorPane.ok_button_text", this.closeButton.getLocale()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDetailsHeight()
/*     */   {
/* 195 */     return 150;
/*     */   }
/*     */   
/*     */   private void createExtraComponents() {
/* 199 */     this.titleLabel = new JLabel("Unknown Error");
/* 200 */     this.titleLabel.setFont(this.titleLabel.getFont().deriveFont(1));
/* 201 */     this.pane.add(this.titleLabel);
/*     */     
/* 203 */     Font f = this.errorMessage.getFont();
/* 204 */     if (f != null) {
/* 205 */       this.errorMessage.setFont(f.deriveFont(f.getSize() - 2.0F));
/*     */     }
/*     */     
/* 208 */     this.disclaimerText = new JEditorPane();
/* 209 */     this.disclaimerText.setContentType("text/html");
/* 210 */     this.disclaimerText.setVisible(false);
/* 211 */     this.disclaimerText.setEditable(false);
/* 212 */     this.disclaimerText.setOpaque(false);
/* 213 */     this.disclaimerText.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);
/* 214 */     if (f != null) {
/* 215 */       this.disclaimerText.setFont(f.deriveFont(f.getSize() - 2.0F));
/*     */     }
/* 217 */     this.pane.add(this.disclaimerText);
/*     */     
/* 219 */     this.detailButton.setBorderPainted(false);
/* 220 */     this.detailButton.setContentAreaFilled(false);
/* 221 */     this.detailButton.setBorder(BorderFactory.createEmptyBorder());
/* 222 */     this.detailButton.setMargin(new Insets(0, 0, 0, 0));
/* 223 */     this.detailButton.setIcon(UIManager.getIcon("Tree.collapsedIcon"));
/* 224 */     this.detailButton.setText(UIManagerExt.getString("JXErrorPane.details_expand_text", this.detailButton.getLocale()));
/*     */     
/* 226 */     this.closeButton.setText(UIManagerExt.getString("JXErrorPane.ok_button_text", this.closeButton.getLocale()));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\macosx\MacOSXErrorPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */