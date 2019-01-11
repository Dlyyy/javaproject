/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.KeyStroke;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractActionExt
/*     */   extends AbstractAction
/*     */   implements ItemListener
/*     */ {
/*     */   public static final String LARGE_ICON = "__LargeIcon__";
/*     */   public static final String GROUP = "__Group__";
/*     */   public static final String IS_STATE = "__State__";
/*  58 */   private boolean selected = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractActionExt() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractActionExt(AbstractActionExt action)
/*     */   {
/*  71 */     Object[] keys = action.getKeys();
/*  72 */     for (int i = 0; i < keys.length; i++) {
/*  73 */       putValue((String)keys[i], action.getValue((String)keys[i]));
/*     */     }
/*  75 */     this.selected = action.selected;
/*  76 */     this.enabled = action.enabled;
/*     */     
/*     */ 
/*  79 */     PropertyChangeListener[] listeners = action.getPropertyChangeListeners();
/*  80 */     for (int i = 0; i < listeners.length; i++) {
/*  81 */       addPropertyChangeListener(listeners[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public AbstractActionExt(String name) {
/*  86 */     super(name);
/*     */   }
/*     */   
/*     */   public AbstractActionExt(String name, Icon icon) {
/*  90 */     super(name, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractActionExt(String name, String command)
/*     */   {
/* 100 */     this(name);
/* 101 */     setActionCommand(command);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractActionExt(String name, String command, Icon icon)
/*     */   {
/* 110 */     super(name, icon);
/* 111 */     setActionCommand(command);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShortDescription()
/*     */   {
/* 119 */     return (String)getValue("ShortDescription");
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
/*     */   public void setShortDescription(String desc)
/*     */   {
/* 134 */     putValue("ShortDescription", desc);
/* 135 */     if ((desc != null) && (getLongDescription() == null)) {
/* 136 */       setLongDescription(desc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLongDescription()
/*     */   {
/* 146 */     return (String)getValue("LongDescription");
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
/*     */   public void setLongDescription(String desc)
/*     */   {
/* 161 */     putValue("LongDescription", desc);
/* 162 */     if ((desc != null) && (getShortDescription() == null)) {
/* 163 */       setShortDescription(desc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getSmallIcon()
/*     */   {
/* 173 */     return (Icon)getValue("SmallIcon");
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
/*     */   public void setSmallIcon(Icon icon)
/*     */   {
/* 187 */     putValue("SmallIcon", icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getLargeIcon()
/*     */   {
/* 196 */     return (Icon)getValue("__LargeIcon__");
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
/*     */   public void setLargeIcon(Icon icon)
/*     */   {
/* 210 */     putValue("__LargeIcon__", icon);
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
/*     */   public void setName(String name)
/*     */   {
/* 224 */     putValue("Name", name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 233 */     return (String)getValue("Name");
/*     */   }
/*     */   
/*     */   public void setMnemonic(String mnemonic) {
/* 237 */     if ((mnemonic != null) && (mnemonic.length() > 0)) {
/* 238 */       putValue("MnemonicKey", new Integer(mnemonic.charAt(0)));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMnemonic(int mnemonic)
/*     */   {
/* 258 */     putValue("MnemonicKey", new Integer(mnemonic));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMnemonic()
/*     */   {
/* 267 */     Integer value = (Integer)getValue("MnemonicKey");
/* 268 */     if (value != null) {
/* 269 */       return value.intValue();
/*     */     }
/* 271 */     return 0;
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
/*     */   public void setActionCommand(Object key)
/*     */   {
/* 286 */     putValue("ActionCommandKey", key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getActionCommand()
/*     */   {
/* 295 */     return getValue("ActionCommandKey");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public KeyStroke getAccelerator()
/*     */   {
/* 305 */     return (KeyStroke)getValue("AcceleratorKey");
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
/*     */   public void setAccelerator(KeyStroke key)
/*     */   {
/* 320 */     putValue("AcceleratorKey", key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGroup(Object group)
/*     */   {
/* 328 */     putValue("__Group__", group);
/*     */   }
/*     */   
/*     */   public Object getGroup() {
/* 332 */     return getValue("__Group__");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 342 */     PropertyChangeListener[] listeners = getPropertyChangeListeners();
/* 343 */     for (int i = 0; i < listeners.length; i++) {
/* 344 */       removePropertyChangeListener(listeners[i]);
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
/*     */ 
/*     */   public boolean isStateAction()
/*     */   {
/* 358 */     Boolean state = (Boolean)getValue("__State__");
/* 359 */     if (state != null) {
/* 360 */       return state.booleanValue();
/*     */     }
/* 362 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setStateAction()
/*     */   {
/* 369 */     setStateAction(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStateAction(boolean state)
/*     */   {
/* 378 */     putValue("__State__", Boolean.valueOf(state));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/* 385 */     return this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setSelected(boolean newValue)
/*     */   {
/* 393 */     boolean oldValue = this.selected;
/* 394 */     if (oldValue != newValue) {
/* 395 */       this.selected = newValue;
/* 396 */       firePropertyChange("selected", Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 403 */     StringBuffer buffer = new StringBuffer("[");
/*     */     
/*     */ 
/* 406 */     buffer.append(getClass().toString());
/* 407 */     buffer.append(":");
/*     */     try {
/* 409 */       Object[] keys = getKeys();
/* 410 */       for (int i = 0; i < keys.length; i++) {
/* 411 */         buffer.append(keys[i]);
/* 412 */         buffer.append('=');
/* 413 */         buffer.append(getValue((String)keys[i]).toString());
/* 414 */         if (i < keys.length - 1) {
/* 415 */           buffer.append(',');
/*     */         }
/*     */       }
/* 418 */       buffer.append(']');
/*     */     }
/*     */     catch (Exception ex) {}
/*     */     
/*     */ 
/* 423 */     return buffer.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemStateChanged(ItemEvent e)
/*     */   {
/* 443 */     if (isStateAction()) {
/* 444 */       setSelected(1 == e.getStateChange());
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\AbstractActionExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */