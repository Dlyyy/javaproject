/*     */ package org.jdesktop.swingx.auth;
/*     */ 
/*     */ import java.util.prefs.Preferences;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultUserNameStore
/*     */   extends UserNameStore
/*     */ {
/*     */   private static final String USER_KEY = "usernames";
/*     */   private static final String NUM_KEY = "usernames.length";
/*     */   private Preferences prefs;
/*     */   private String[] userNames;
/*     */   
/*     */   public DefaultUserNameStore()
/*     */   {
/*  56 */     this.userNames = new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadUserNames()
/*     */   {
/*  64 */     initPrefs();
/*  65 */     if (this.prefs != null) {
/*  66 */       int n = this.prefs.getInt("usernames.length", 0);
/*  67 */       String[] names = new String[n];
/*  68 */       for (int i = 0; i < n; i++) {
/*  69 */         names[i] = this.prefs.get("usernames." + i, null);
/*     */       }
/*  71 */       setUserNames(names);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveUserNames()
/*     */   {
/*  80 */     initPrefs();
/*  81 */     if (this.prefs != null) {
/*  82 */       this.prefs.putInt("usernames.length", this.userNames.length);
/*  83 */       for (int i = 0; i < this.userNames.length; i++) {
/*  84 */         this.prefs.put("usernames." + i, this.userNames[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getUserNames()
/*     */   {
/*  94 */     String[] copy = new String[this.userNames.length];
/*  95 */     System.arraycopy(this.userNames, 0, copy, 0, this.userNames.length);
/*     */     
/*  97 */     return copy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserNames(String[] userNames)
/*     */   {
/* 105 */     userNames = userNames == null ? new String[0] : userNames;
/* 106 */     String[] old = getUserNames();
/* 107 */     this.userNames = userNames;
/* 108 */     firePropertyChange("userNames", old, getUserNames());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addUserName(String name)
/*     */   {
/* 117 */     if (!containsUserName(name)) {
/* 118 */       String[] newNames = new String[this.userNames.length + 1];
/* 119 */       for (int i = 0; i < this.userNames.length; i++) {
/* 120 */         newNames[i] = this.userNames[i];
/*     */       }
/* 122 */       newNames[(newNames.length - 1)] = name;
/* 123 */       setUserNames(newNames);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeUserName(String name)
/*     */   {
/* 134 */     if (containsUserName(name)) {
/* 135 */       String[] newNames = new String[this.userNames.length - 1];
/* 136 */       int index = 0;
/* 137 */       for (String s : this.userNames) {
/* 138 */         if (!s.equals(name)) {
/* 139 */           newNames[(index++)] = s;
/*     */         }
/*     */       }
/* 142 */       setUserNames(newNames);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsUserName(String name)
/*     */   {
/* 151 */     for (String s : this.userNames) {
/* 152 */       if (s.equals(name)) {
/* 153 */         return true;
/*     */       }
/*     */     }
/* 156 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Preferences getPreferences()
/*     */   {
/* 163 */     return this.prefs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPreferences(Preferences prefs)
/*     */   {
/* 171 */     Preferences old = getPreferences();
/* 172 */     initPrefs();
/* 173 */     this.prefs = prefs;
/* 174 */     firePropertyChange("preferences", old, getPreferences());
/* 175 */     if (this.prefs != old)
/*     */     {
/* 177 */       loadUserNames();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initPrefs()
/*     */   {
/* 185 */     if (this.prefs == null) {
/* 186 */       this.prefs = Preferences.userNodeForPackage(DefaultUserNameStore.class);
/* 187 */       this.prefs = this.prefs.node("DefaultUserNameStore");
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\DefaultUserNameStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */