/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import java.util.EventObject;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractCellEditor;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.TreeCellEditor;
/*     */ import org.jdesktop.swingx.JXDatePicker;
/*     */ import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatePickerCellEditor
/*     */   extends AbstractCellEditor
/*     */   implements TableCellEditor, TreeCellEditor
/*     */ {
/*     */   protected JXDatePicker datePicker;
/*     */   protected DateFormat dateFormat;
/*  61 */   protected int clickCountToStart = 2;
/*     */   
/*     */   private ActionListener pickerActionListener;
/*     */   
/*     */   protected boolean ignoreAction;
/*     */   
/*  67 */   private static Logger logger = Logger.getLogger(DatePickerCellEditor.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DatePickerCellEditor()
/*     */   {
/*  79 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DatePickerCellEditor(DateFormat dateFormat)
/*     */   {
/*  91 */     this.dateFormat = (dateFormat != null ? dateFormat : DateFormat.getDateInstance());
/*     */     
/*  93 */     this.datePicker = new JXDatePicker();
/*     */     
/*  95 */     this.datePicker.getEditor().setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
/*     */     
/*     */ 
/*  98 */     this.datePicker.setFont(UIManager.getDefaults().getFont("TextField.font"));
/*  99 */     if (dateFormat != null) {
/* 100 */       this.datePicker.setFormats(new DateFormat[] { dateFormat });
/*     */     }
/* 102 */     this.datePicker.addActionListener(getPickerActionListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getCellEditorValue()
/*     */   {
/* 114 */     return this.datePicker.getDate();
/*     */   }
/*     */   
/*     */   public boolean isCellEditable(EventObject anEvent)
/*     */   {
/* 119 */     if ((anEvent instanceof MouseEvent)) {
/* 120 */       return ((MouseEvent)anEvent).getClickCount() >= getClickCountToStart();
/*     */     }
/* 122 */     return super.isCellEditable(anEvent);
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
/*     */   public boolean stopCellEditing()
/*     */   {
/* 136 */     this.ignoreAction = true;
/* 137 */     boolean canCommit = commitChange();
/* 138 */     this.ignoreAction = false;
/* 139 */     if (canCommit) {
/* 140 */       return super.stopCellEditing();
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClickCountToStart(int count)
/*     */   {
/* 153 */     this.clickCountToStart = count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getClickCountToStart()
/*     */   {
/* 162 */     return this.clickCountToStart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
/*     */   {
/* 172 */     this.ignoreAction = true;
/* 173 */     this.datePicker.setDate(getValueAsDate(value));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */     this.ignoreAction = false;
/* 181 */     return this.datePicker;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
/*     */   {
/* 189 */     this.ignoreAction = true;
/* 190 */     this.datePicker.setDate(getValueAsDate(value));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */     this.ignoreAction = false;
/* 198 */     return this.datePicker;
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
/*     */   protected Date getValueAsDate(Object value)
/*     */   {
/* 214 */     if (isEmpty(value)) return null;
/* 215 */     if ((value instanceof Date)) {
/* 216 */       return (Date)value;
/*     */     }
/* 218 */     if ((value instanceof Long)) {
/* 219 */       return new Date(((Long)value).longValue());
/*     */     }
/* 221 */     if ((value instanceof String))
/*     */     {
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/* 227 */         return this.dateFormat.parse((String)value);
/*     */       } catch (ParseException e) {
/* 229 */         handleParseException(e);
/*     */       }
/*     */     }
/* 232 */     if ((value instanceof DefaultMutableTreeNode)) {
/* 233 */       return getValueAsDate(((DefaultMutableTreeNode)value).getUserObject());
/*     */     }
/* 235 */     if ((value instanceof AbstractMutableTreeTableNode)) {
/* 236 */       return getValueAsDate(((AbstractMutableTreeTableNode)value).getUserObject());
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void handleParseException(ParseException e)
/*     */   {
/* 245 */     logger.log(Level.SEVERE, e.getMessage(), e.getMessage());
/*     */   }
/*     */   
/*     */   protected boolean isEmpty(Object value) {
/* 249 */     return (value == null) || (((value instanceof String)) && (((String)value).length() == 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean commitChange()
/*     */   {
/*     */     try
/*     */     {
/* 262 */       this.datePicker.commitEdit();
/* 263 */       return true;
/*     */     }
/*     */     catch (ParseException e) {}
/* 266 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateFormat[] getFormats()
/*     */   {
/* 276 */     return this.datePicker.getFormats();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFormats(DateFormat... formats)
/*     */   {
/* 287 */     this.datePicker.setFormats(formats);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ActionListener getPickerActionListener()
/*     */   {
/* 296 */     if (this.pickerActionListener == null) {
/* 297 */       this.pickerActionListener = createPickerActionListener();
/*     */     }
/* 299 */     return this.pickerActionListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ActionListener createPickerActionListener()
/*     */   {
/* 307 */     ActionListener l = new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e)
/*     */       {
/* 311 */         if (DatePickerCellEditor.this.ignoreAction) {
/* 312 */           return;
/*     */         }
/*     */         
/*     */ 
/* 316 */         terminateEdit(e);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       private void terminateEdit(ActionEvent e)
/*     */       {
/* 323 */         if ((e != null) && ("datePickerCommit".equals(e.getActionCommand())))
/*     */         {
/* 325 */           DatePickerCellEditor.this.stopCellEditing();
/*     */         } else {
/* 327 */           DatePickerCellEditor.this.cancelCellEditing();
/*     */         }
/*     */       }
/* 330 */     };
/* 331 */     return l;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\DatePickerCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */