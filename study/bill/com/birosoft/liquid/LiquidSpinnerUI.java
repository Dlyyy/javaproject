/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.DateFormat.Field;
/*     */ import java.text.Format;
/*     */ import java.text.Format.Field;
/*     */ import java.text.ParseException;
/*     */ import java.util.Map;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JSpinner.DateEditor;
/*     */ import javax.swing.JSpinner.DefaultEditor;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SpinnerDateModel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSpinnerUI;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.InternationalFormatter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidSpinnerUI
/*     */   extends BasicSpinnerUI
/*     */ {
/*  55 */   private static final ArrowButtonHandler nextButtonHandler = new ArrowButtonHandler("increment", true);
/*     */   
/*  57 */   private static final ArrowButtonHandler previousButtonHandler = new ArrowButtonHandler("decrement", false);
/*     */   
/*  59 */   private static final Dimension zeroSize = new Dimension(0, 0);
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  62 */     return new LiquidSpinnerUI();
/*     */   }
/*     */   
/*     */   protected Component createPreviousButton() {
/*  66 */     JButton b = new SpecialUIButton(new LiquidSpinnerButtonUI(5));
/*     */     
/*  68 */     b.setFocusable(false);
/*  69 */     b.addActionListener(previousButtonHandler);
/*  70 */     b.addMouseListener(previousButtonHandler);
/*     */     
/*  72 */     return b;
/*     */   }
/*     */   
/*     */   protected Component createNextButton() {
/*  76 */     JButton b = new SpecialUIButton(new LiquidSpinnerButtonUI(1));
/*     */     
/*  78 */     b.setFocusable(false);
/*  79 */     b.addActionListener(nextButtonHandler);
/*  80 */     b.addMouseListener(nextButtonHandler);
/*     */     
/*  82 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JComponent createEditor()
/*     */   {
/*  89 */     JComponent editor = super.createEditor();
/*     */     
/*  91 */     if ((editor instanceof JSpinner.DefaultEditor)) {
/*  92 */       JSpinner.DefaultEditor de = (JSpinner.DefaultEditor)editor;
/*     */       
/*     */ 
/*     */ 
/*  96 */       de.getTextField().setBorder(new EmptyBorder(0, 0, 0, 0));
/*     */       
/*  98 */       Dimension prefSize = de.getPreferredSize();
/*  99 */       int compHeight = prefSize.height;
/*     */       
/* 101 */       int height = LiquidSpinnerButtonUI.getSkin().getVsize() * 2;
/* 102 */       int diff = height - compHeight;
/*     */       
/* 104 */       if (diff > 0) {
/* 105 */         int half = diff / 2;
/* 106 */         de.getTextField().setBorder(new EmptyBorder(half, 0, diff - half, 0));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 111 */     return editor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ArrowButtonHandler
/*     */     extends AbstractAction
/*     */     implements MouseListener
/*     */   {
/*     */     final Timer autoRepeatTimer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final boolean isNext;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     JSpinner spinner = null;
/*     */     
/*     */     ArrowButtonHandler(String name, boolean isNext) {
/* 140 */       super();
/* 141 */       this.isNext = isNext;
/* 142 */       this.autoRepeatTimer = new Timer(60, this);
/* 143 */       this.autoRepeatTimer.setInitialDelay(300);
/*     */     }
/*     */     
/*     */     private JSpinner eventToSpinner(AWTEvent e) {
/* 147 */       Object src = e.getSource();
/*     */       
/* 149 */       while (((src instanceof Component)) && (!(src instanceof JSpinner))) {
/* 150 */         src = ((Component)src).getParent();
/*     */       }
/*     */       
/* 153 */       return (src instanceof JSpinner) ? (JSpinner)src : null;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 157 */       JSpinner spinner = this.spinner;
/*     */       
/* 159 */       if (!(e.getSource() instanceof Timer))
/*     */       {
/* 161 */         spinner = eventToSpinner(e);
/*     */       }
/*     */       
/* 164 */       if (spinner != null) {
/*     */         try {
/* 166 */           int calendarField = getCalendarField(spinner);
/* 167 */           spinner.commitEdit();
/*     */           
/* 169 */           if (calendarField != -1) {
/* 170 */             ((SpinnerDateModel)spinner.getModel()).setCalendarField(calendarField);
/*     */           }
/*     */           
/* 173 */           Object value = this.isNext ? spinner.getNextValue() : spinner.getPreviousValue();
/*     */           
/*     */ 
/* 176 */           if (value != null) {
/* 177 */             spinner.setValue(value);
/* 178 */             select(spinner);
/*     */           }
/*     */         } catch (IllegalArgumentException iae) {
/* 181 */           UIManager.getLookAndFeel().provideErrorFeedback(spinner);
/*     */         } catch (ParseException pe) {
/* 183 */           UIManager.getLookAndFeel().provideErrorFeedback(spinner);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void select(JSpinner spinner)
/*     */     {
/* 193 */       JComponent editor = spinner.getEditor();
/*     */       
/* 195 */       if ((editor instanceof JSpinner.DateEditor)) {
/* 196 */         JSpinner.DateEditor dateEditor = (JSpinner.DateEditor)editor;
/* 197 */         JFormattedTextField ftf = dateEditor.getTextField();
/* 198 */         Format format = dateEditor.getFormat();
/*     */         
/*     */         Object value;
/* 201 */         if ((format != null) && ((value = spinner.getValue()) != null)) {
/* 202 */           SpinnerDateModel model = dateEditor.getModel();
/* 203 */           DateFormat.Field field = DateFormat.Field.ofCalendarField(model.getCalendarField());
/*     */           
/* 205 */           if (field != null) {
/*     */             try {
/* 207 */               AttributedCharacterIterator iterator = format.formatToCharacterIterator(value);
/*     */               
/* 209 */               if ((!select(ftf, iterator, field)) && (field == DateFormat.Field.HOUR0))
/*     */               {
/* 211 */                 select(ftf, iterator, DateFormat.Field.HOUR1);
/*     */               }
/*     */             }
/*     */             catch (IllegalArgumentException iae) {}
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean select(JFormattedTextField ftf, AttributedCharacterIterator iterator, DateFormat.Field field)
/*     */     {
/* 226 */       int max = ftf.getDocument().getLength();
/*     */       
/* 228 */       iterator.first();
/*     */       do
/*     */       {
/* 231 */         Map attrs = iterator.getAttributes();
/*     */         
/* 233 */         if ((attrs != null) && (attrs.containsKey(field))) {
/* 234 */           int start = iterator.getRunStart(field);
/* 235 */           int end = iterator.getRunLimit(field);
/*     */           
/* 237 */           if ((start != -1) && (end != -1) && (start <= max) && (end <= max))
/*     */           {
/* 239 */             ftf.select(start, end);
/*     */           }
/*     */           
/* 242 */           return true;
/*     */         }
/* 244 */       } while (iterator.next() != 65535);
/*     */       
/* 246 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int getCalendarField(JSpinner spinner)
/*     */     {
/* 255 */       JComponent editor = spinner.getEditor();
/*     */       
/* 257 */       if ((editor instanceof JSpinner.DateEditor)) {
/* 258 */         JSpinner.DateEditor dateEditor = (JSpinner.DateEditor)editor;
/* 259 */         JFormattedTextField ftf = dateEditor.getTextField();
/* 260 */         int start = ftf.getSelectionStart();
/* 261 */         JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
/*     */         
/* 263 */         if ((formatter instanceof InternationalFormatter)) {
/* 264 */           Format.Field[] fields = ((InternationalFormatter)formatter).getFields(start);
/*     */           
/* 266 */           for (int counter = 0; counter < fields.length; counter++) {
/* 267 */             if ((fields[counter] instanceof DateFormat.Field)) {
/*     */               int calendarField;
/*     */               int calendarField;
/* 270 */               if (fields[counter] == DateFormat.Field.HOUR1) {
/* 271 */                 calendarField = 10;
/*     */               } else {
/* 273 */                 calendarField = ((DateFormat.Field)fields[counter]).getCalendarField();
/*     */               }
/*     */               
/* 276 */               if (calendarField != -1) {
/* 277 */                 return calendarField;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 284 */       return -1;
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 288 */       if ((SwingUtilities.isLeftMouseButton(e)) && (e.getComponent().isEnabled()))
/*     */       {
/* 290 */         this.spinner = eventToSpinner(e);
/* 291 */         this.autoRepeatTimer.start();
/*     */         
/* 293 */         focusSpinnerIfNecessary();
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 298 */       this.autoRepeatTimer.stop();
/* 299 */       this.spinner = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseClicked(MouseEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseEntered(MouseEvent e) {}
/*     */     
/*     */ 
/*     */     public void mouseExited(MouseEvent e) {}
/*     */     
/*     */ 
/*     */     private void focusSpinnerIfNecessary()
/*     */     {
/* 316 */       Component fo = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*     */       
/*     */ 
/* 319 */       if ((this.spinner.isRequestFocusEnabled()) && ((fo == null) || (!SwingUtilities.isDescendingFrom(fo, this.spinner))))
/*     */       {
/*     */ 
/* 322 */         Container root = this.spinner;
/*     */         
/* 324 */         if (!root.isFocusCycleRoot()) {
/* 325 */           root = root.getFocusCycleRootAncestor();
/*     */         }
/*     */         
/* 328 */         if (root != null) {
/* 329 */           FocusTraversalPolicy ftp = root.getFocusTraversalPolicy();
/* 330 */           Component child = ftp.getComponentAfter(root, this.spinner);
/*     */           
/* 332 */           if ((child != null) && (SwingUtilities.isDescendingFrom(child, this.spinner)))
/*     */           {
/* 334 */             child.requestFocus();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidSpinnerUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */