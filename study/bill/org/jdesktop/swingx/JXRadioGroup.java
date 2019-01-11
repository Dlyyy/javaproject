/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
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
/*     */ public class JXRadioGroup<T>
/*     */   extends JPanel
/*     */ {
/*     */   private static final long serialVersionUID = 3257285842266567986L;
/*     */   private ButtonGroup buttonGroup;
/*  82 */   private final List<T> values = new ArrayList();
/*     */   
/*     */ 
/*     */   private JXRadioGroup<T>.ActionSelectionListener actionHandler;
/*     */   
/*     */   private List<ActionListener> actionListeners;
/*     */   
/*     */ 
/*     */   public JXRadioGroup()
/*     */   {
/*  92 */     setLayout(new BoxLayout(this, 0));
/*  93 */     this.buttonGroup = new ButtonGroup();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXRadioGroup(T[] radioValues)
/*     */   {
/* 102 */     this();
/* 103 */     for (int i = 0; i < radioValues.length; i++) {
/* 104 */       add(radioValues[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> JXRadioGroup<T> create(T[] radioValues)
/*     */   {
/* 116 */     return new JXRadioGroup(radioValues);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayoutAxis(int axis)
/*     */   {
/* 126 */     setLayout(new BoxLayout(this, axis));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValues(T[] radioValues)
/*     */   {
/* 137 */     clearAll();
/* 138 */     for (int i = 0; i < radioValues.length; i++) {
/* 139 */       add(radioValues[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearAll() {
/* 144 */     this.values.clear();
/* 145 */     this.buttonGroup = new ButtonGroup();
/*     */     
/* 147 */     removeAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(T radioValue)
/*     */   {
/* 155 */     if (this.values.contains(radioValue))
/*     */     {
/* 157 */       throw new IllegalArgumentException("cannot add the same value twice " + radioValue);
/*     */     }
/* 159 */     if ((radioValue instanceof AbstractButton)) {
/* 160 */       this.values.add(radioValue);
/* 161 */       addButton((AbstractButton)radioValue);
/*     */     } else {
/* 163 */       this.values.add(radioValue);
/*     */       
/* 165 */       addButton(new JRadioButton("" + radioValue));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addButton(AbstractButton button) {
/* 170 */     this.buttonGroup.add(button);
/* 171 */     super.add(button);
/* 172 */     if (this.actionHandler == null) {
/* 173 */       this.actionHandler = new ActionSelectionListener(null);
/*     */     }
/* 175 */     button.addActionListener(this.actionHandler);
/* 176 */     button.addItemListener(this.actionHandler);
/*     */   }
/*     */   
/*     */   private class ActionSelectionListener implements ActionListener, ItemListener {
/*     */     private ActionSelectionListener() {}
/*     */     
/* 182 */     public void actionPerformed(ActionEvent e) { JXRadioGroup.this.fireActionEvent(e); }
/*     */     
/*     */     public void itemStateChanged(ItemEvent e)
/*     */     {
/* 186 */       JXRadioGroup.this.fireActionEvent(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractButton getSelectedButton()
/*     */   {
/* 197 */     ButtonModel selectedModel = this.buttonGroup.getSelection();
/* 198 */     AbstractButton[] children = getButtonComponents();
/* 199 */     for (int i = 0; i < children.length; i++) {
/* 200 */       AbstractButton button = children[i];
/* 201 */       if (button.getModel() == selectedModel) {
/* 202 */         return button;
/*     */       }
/*     */     }
/* 205 */     return null;
/*     */   }
/*     */   
/*     */   private AbstractButton[] getButtonComponents() {
/* 209 */     Component[] children = getComponents();
/* 210 */     List<AbstractButton> buttons = new ArrayList();
/* 211 */     for (int i = 0; i < children.length; i++) {
/* 212 */       if ((children[i] instanceof AbstractButton)) {
/* 213 */         buttons.add((AbstractButton)children[i]);
/*     */       }
/*     */     }
/* 216 */     return (AbstractButton[])buttons.toArray(new AbstractButton[buttons.size()]);
/*     */   }
/*     */   
/*     */   private int getSelectedIndex() {
/* 220 */     ButtonModel selectedModel = this.buttonGroup.getSelection();
/* 221 */     Component[] children = getButtonComponents();
/* 222 */     for (int i = 0; i < children.length; i++) {
/* 223 */       AbstractButton button = (AbstractButton)children[i];
/* 224 */       if (button.getModel() == selectedModel) {
/* 225 */         return i;
/*     */       }
/*     */     }
/* 228 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getSelectedValue()
/*     */   {
/* 237 */     int index = getSelectedIndex();
/* 238 */     return (index < 0) || (index >= this.values.size()) ? null : this.values.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectedValue(T value)
/*     */   {
/* 248 */     int index = this.values.indexOf(value);
/* 249 */     AbstractButton button = getButtonComponents()[index];
/* 250 */     button.setSelected(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AbstractButton getChildButton(int index)
/*     */   {
/* 257 */     return getButtonComponents()[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AbstractButton getChildButton(T value)
/*     */   {
/* 264 */     int index = this.values.indexOf(value);
/* 265 */     return getButtonComponents()[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getChildButtonCount()
/*     */   {
/* 272 */     return getButtonComponents().length;
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
/*     */   public void addActionListener(ActionListener l)
/*     */   {
/* 285 */     if (this.actionListeners == null) {
/* 286 */       this.actionListeners = new ArrayList();
/*     */     }
/* 288 */     this.actionListeners.add(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeActionListener(ActionListener l)
/*     */   {
/* 298 */     if (this.actionListeners != null) {
/* 299 */       this.actionListeners.remove(l);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ActionListener[] getActionListeners()
/*     */   {
/* 311 */     if (this.actionListeners != null) {
/* 312 */       return (ActionListener[])this.actionListeners.toArray(new ActionListener[0]);
/*     */     }
/* 314 */     return new ActionListener[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireActionEvent(ActionEvent e)
/*     */   {
/* 326 */     if (this.actionListeners != null) {
/* 327 */       for (int i = 0; i < this.actionListeners.size(); i++) {
/* 328 */         ActionListener l = (ActionListener)this.actionListeners.get(i);
/* 329 */         l.actionPerformed(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/* 341 */     super.setEnabled(enabled);
/* 342 */     for (Enumeration<AbstractButton> en = this.buttonGroup.getElements(); en.hasMoreElements();) {
/* 343 */       AbstractButton button = (AbstractButton)en.nextElement();
/*     */       
/*     */ 
/* 346 */       if ((!enabled) || (button.getAction() == null) || (button.getAction().isEnabled()))
/*     */       {
/*     */ 
/*     */ 
/* 350 */         button.setEnabled(enabled);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXRadioGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */