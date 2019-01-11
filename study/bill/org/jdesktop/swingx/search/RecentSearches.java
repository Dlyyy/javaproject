/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.prefs.BackingStoreException;
/*     */ import java.util.prefs.Preferences;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import org.jdesktop.swingx.JXSearchField;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecentSearches
/*     */   implements ActionListener
/*     */ {
/*     */   private Preferences prefsNode;
/*  33 */   private int maxRecents = 5;
/*     */   
/*  35 */   private List<String> recentSearches = new ArrayList();
/*     */   
/*  37 */   private List<ChangeListener> listeners = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RecentSearches(String saveName)
/*     */   {
/*  48 */     this(null, saveName);
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
/*     */   public RecentSearches(Preferences prefs, String saveName)
/*     */   {
/*  66 */     if (prefs == null) {
/*     */       try {
/*  68 */         prefs = Preferences.userRoot();
/*     */       }
/*     */       catch (AccessControlException ace)
/*     */       {
/*  72 */         Logger.getLogger(getClass().getName()).warning("cannot acces preferences. persistency disabled.");
/*     */       }
/*     */     }
/*     */     
/*  76 */     if ((prefs != null) && (saveName != null)) {
/*  77 */       this.prefsNode = prefs.node(saveName);
/*  78 */       load();
/*     */     }
/*     */   }
/*     */   
/*     */   private void load()
/*     */   {
/*     */     try {
/*  85 */       String[] recent = new String[this.prefsNode.keys().length];
/*  86 */       for (String key : this.prefsNode.keys()) {
/*  87 */         recent[this.prefsNode.getInt(key, -1)] = key;
/*     */       }
/*  89 */       this.recentSearches.addAll(Arrays.asList(recent));
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */   private void save()
/*     */   {
/*  96 */     if (this.prefsNode == null) {
/*  97 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 101 */       this.prefsNode.clear();
/*     */     }
/*     */     catch (BackingStoreException e) {}
/*     */     
/*     */ 
/* 106 */     int i = 0;
/* 107 */     for (String search : this.recentSearches) {
/* 108 */       this.prefsNode.putInt(search, i++);
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
/*     */   public void put(String searchString)
/*     */   {
/* 126 */     if ((searchString == null) || (searchString.trim().length() == 0)) {
/* 127 */       return;
/*     */     }
/*     */     
/* 130 */     int lastIndex = this.recentSearches.indexOf(searchString);
/* 131 */     if (lastIndex != -1) {
/* 132 */       this.recentSearches.remove(lastIndex);
/*     */     }
/* 134 */     this.recentSearches.add(0, searchString);
/* 135 */     if (getLength() > getMaxRecents()) {
/* 136 */       this.recentSearches.remove(this.recentSearches.size() - 1);
/*     */     }
/* 138 */     save();
/* 139 */     fireChangeEvent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRecentSearches()
/*     */   {
/* 148 */     return (String[])this.recentSearches.toArray(new String[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 157 */     return this.recentSearches.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 164 */     this.recentSearches.clear();
/* 165 */     save();
/* 166 */     fireChangeEvent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxRecents()
/*     */   {
/* 176 */     return this.maxRecents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxRecents(int maxRecents)
/*     */   {
/* 187 */     this.maxRecents = maxRecents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addChangeListener(ChangeListener l)
/*     */   {
/* 198 */     this.listeners.add(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeChangeListener(ChangeListener l)
/*     */   {
/* 208 */     this.listeners.remove(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 217 */     return (ChangeListener[])this.listeners.toArray(new ChangeListener[0]);
/*     */   }
/*     */   
/*     */   private void fireChangeEvent() {
/* 221 */     ChangeEvent e = new ChangeEvent(this);
/*     */     
/* 223 */     for (ChangeListener l : this.listeners) {
/* 224 */       l.stateChanged(e);
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
/*     */   protected JPopupMenu createPopupMenu(JTextField searchField)
/*     */   {
/* 240 */     return new RecentSearchesPopup(this, searchField);
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
/*     */   public void install(JTextField searchField)
/*     */   {
/* 257 */     searchField.addActionListener(this);
/* 258 */     NativeSearchFieldSupport.setFindPopupMenu(searchField, createPopupMenu(searchField));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstall(JXSearchField searchField)
/*     */   {
/* 270 */     searchField.removeActionListener(this);
/* 271 */     if ((searchField.getFindPopupMenu() instanceof RecentSearchesPopup)) {
/* 272 */       removeChangeListener((ChangeListener)searchField.getFindPopupMenu());
/* 273 */       searchField.setFindPopupMenu(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/* 282 */     put(e.getActionCommand());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class RecentSearchesPopup
/*     */     extends JPopupMenu
/*     */     implements ActionListener, ChangeListener
/*     */   {
/*     */     private RecentSearches recentSearches;
/*     */     
/*     */ 
/*     */     private JTextField searchField;
/*     */     
/*     */ 
/*     */     private JMenuItem clear;
/*     */     
/*     */ 
/*     */ 
/*     */     public RecentSearchesPopup(RecentSearches recentSearches, JTextField searchField)
/*     */     {
/* 304 */       this.searchField = searchField;
/* 305 */       this.recentSearches = recentSearches;
/*     */       
/* 307 */       recentSearches.addChangeListener(this);
/* 308 */       buildMenu();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void buildMenu()
/*     */     {
/* 315 */       setVisible(false);
/* 316 */       removeAll();
/*     */       
/* 318 */       if (this.recentSearches.getLength() == 0) {
/* 319 */         JMenuItem noRecent = new JMenuItem(UIManagerExt.getString("SearchField.noRecentsText"));
/* 320 */         noRecent.setEnabled(false);
/* 321 */         add(noRecent);
/*     */       } else {
/* 323 */         JMenuItem recent = new JMenuItem(UIManagerExt.getString("SearchField.recentsMenuTitle"));
/* 324 */         recent.setEnabled(false);
/* 325 */         add(recent);
/*     */         
/* 327 */         for (String searchString : this.recentSearches.getRecentSearches()) {
/* 328 */           JMenuItem mi = new JMenuItem(searchString);
/* 329 */           mi.addActionListener(this);
/* 330 */           add(mi);
/*     */         }
/*     */         
/* 333 */         addSeparator();
/* 334 */         this.clear = new JMenuItem(UIManagerExt.getString("SearchField.clearRecentsText"));
/* 335 */         this.clear.addActionListener(this);
/* 336 */         add(this.clear);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 348 */       if (e.getSource() == this.clear) {
/* 349 */         this.recentSearches.removeAll();
/*     */       } else {
/* 351 */         this.searchField.setText(e.getActionCommand());
/* 352 */         this.searchField.postActionEvent();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void stateChanged(ChangeEvent e)
/*     */     {
/* 361 */       buildMenu();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\RecentSearches.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */