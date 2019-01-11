/*     */ package org.jdesktop.swingx.auth;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
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
/*     */ public class UserPermissions
/*     */ {
/*  43 */   private static final UserPermissions INSTANCE = new UserPermissions();
/*  44 */   private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
/*     */   
/*     */ 
/*     */   private String[] roles;
/*     */   
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/*  52 */     this.propertyChange.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
/*  56 */     this.propertyChange.addPropertyChangeListener(name, listener);
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener) {
/*  60 */     this.propertyChange.removePropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
/*  64 */     this.propertyChange.removePropertyChangeListener(name, listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UserPermissions getInstance()
/*     */   {
/*  72 */     return INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getRoles()
/*     */   {
/*  79 */     return this.roles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUserInRole(String role)
/*     */   {
/*  86 */     if (this.roles != null) {
/*  87 */       for (int iter = 0; iter < this.roles.length; iter++) {
/*  88 */         if (this.roles[iter].equals(role)) {
/*  89 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  93 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUserInARole(String[] roles)
/*     */   {
/* 100 */     for (int iter = 0; iter < roles.length; iter++) {
/* 101 */       if (isUserInRole(roles[iter])) {
/* 102 */         return true;
/*     */       }
/*     */     }
/* 105 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUserInRoles(String[] roles)
/*     */   {
/* 112 */     for (int iter = 0; iter < roles.length; iter++) {
/* 113 */       if (!isUserInRole(roles[iter])) {
/* 114 */         return false;
/*     */       }
/*     */     }
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   void setRoles(String[] roles) {
/* 121 */     String[] oldValue = this.roles;
/* 122 */     this.roles = roles;
/* 123 */     this.propertyChange.firePropertyChange("roles", oldValue, roles);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\UserPermissions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */