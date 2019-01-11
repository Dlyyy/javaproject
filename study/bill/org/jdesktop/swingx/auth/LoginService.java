/*     */ package org.jdesktop.swingx.auth;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.SwingWorker;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import org.jdesktop.beans.AbstractBean;
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
/*     */ public abstract class LoginService
/*     */   extends AbstractBean
/*     */ {
/*  47 */   private Logger LOG = Logger.getLogger(LoginService.class.getName());
/*     */   
/*     */ 
/*  50 */   private EventListenerList listenerList = new EventListenerList();
/*     */   
/*     */ 
/*     */   private SwingWorker<Boolean, Void> loginWorker;
/*     */   
/*     */ 
/*     */   private boolean synchronous;
/*     */   
/*     */   private String server;
/*     */   
/*     */ 
/*     */   public LoginService() {}
/*     */   
/*     */ 
/*     */   public LoginService(String server)
/*     */   {
/*  66 */     setServer(server);
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
/*     */   public abstract boolean authenticate(String paramString1, char[] paramArrayOfChar, String paramString2)
/*     */     throws Exception;
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
/*     */   public String[] getUserRoles()
/*     */   {
/*  96 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cancelAuthentication()
/*     */   {
/* 106 */     if (this.loginWorker != null) {
/* 107 */       this.loginWorker.cancel(true);
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
/*     */   public void startAuthentication(final String user, final char[] password, final String server)
/*     */     throws Exception
/*     */   {
/* 125 */     if (getSynchronous()) {
/*     */       try {
/* 127 */         if (authenticate(user, password, server)) {
/* 128 */           fireLoginSucceeded(new LoginEvent(this));
/*     */         } else {
/* 130 */           fireLoginFailed(new LoginEvent(this));
/*     */         }
/*     */       } catch (Throwable e) {
/* 133 */         fireLoginFailed(new LoginEvent(this, e));
/*     */       }
/*     */     } else {
/* 136 */       this.loginWorker = new SwingWorker()
/*     */       {
/*     */         protected Boolean doInBackground() throws Exception {
/*     */           try {
/* 140 */             final boolean result = LoginService.this.authenticate(user, password, server);
/*     */             
/* 142 */             if (isCancelled()) {
/* 143 */               EventQueue.invokeLater(new Runnable() {
/*     */                 public void run() {
/* 145 */                   LoginService.this.fireLoginCanceled(new LoginEvent(this));
/*     */                 }
/* 147 */               });
/* 148 */               return Boolean.valueOf(false);
/*     */             }
/* 150 */             EventQueue.invokeLater(new Runnable() {
/*     */               public void run() {
/* 152 */                 if (result) {
/* 153 */                   LoginService.this.fireLoginSucceeded(new LoginEvent(LoginService.this));
/*     */                 }
/*     */                 else {
/* 156 */                   LoginService.this.fireLoginFailed(new LoginEvent(LoginService.this));
/*     */                 }
/*     */                 
/*     */               }
/* 160 */             });
/* 161 */             return Boolean.valueOf(result);
/*     */           } catch (Throwable failed) {
/* 163 */             if (!isCancelled()) {
/* 164 */               SwingUtilities.invokeLater(new Runnable() {
/*     */                 public void run() {
/* 166 */                   LoginService.this.fireLoginFailed(new LoginEvent(LoginService.this, failed));
/*     */                 }
/*     */                 
/*     */               });
/*     */             } else
/* 171 */               EventQueue.invokeLater(new Runnable() {
/*     */                 public void run() {
/* 173 */                   LoginService.this.fireLoginCanceled(new LoginEvent(this));
/*     */                 }
/*     */               });
/*     */           }
/* 177 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/* 180 */       };
/* 181 */       this.loginWorker.execute();
/* 182 */       fireLoginStarted(new LoginEvent(this));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getSynchronous()
/*     */   {
/* 192 */     return this.synchronous;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSynchronous(boolean synchronous)
/*     */   {
/* 202 */     boolean old = getSynchronous();
/* 203 */     this.synchronous = synchronous;
/* 204 */     firePropertyChange("synchronous", Boolean.valueOf(old), Boolean.valueOf(getSynchronous()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLoginListener(LoginListener listener)
/*     */   {
/* 215 */     this.listenerList.add(LoginListener.class, listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeLoginListener(LoginListener listener)
/*     */   {
/* 225 */     this.listenerList.remove(LoginListener.class, listener);
/*     */   }
/*     */   
/*     */   void fireLoginStarted(LoginEvent source)
/*     */   {
/* 230 */     Object[] listeners = this.listenerList.getListenerList();
/*     */     
/*     */ 
/* 233 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 234 */       if (listeners[i] == LoginListener.class) {
/* 235 */         ((LoginListener)listeners[(i + 1)]).loginStarted(source);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void fireLoginSucceeded(LoginEvent source)
/*     */   {
/* 242 */     Object[] listeners = this.listenerList.getListenerList();
/*     */     
/*     */ 
/* 245 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 246 */       if (listeners[i] == LoginListener.class) {
/* 247 */         ((LoginListener)listeners[(i + 1)]).loginSucceeded(source);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void fireLoginFailed(LoginEvent source)
/*     */   {
/* 254 */     Object[] listeners = this.listenerList.getListenerList();
/*     */     
/*     */ 
/* 257 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 258 */       if (listeners[i] == LoginListener.class) {
/* 259 */         ((LoginListener)listeners[(i + 1)]).loginFailed(source);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void fireLoginCanceled(LoginEvent source)
/*     */   {
/* 266 */     Object[] listeners = this.listenerList.getListenerList();
/*     */     
/*     */ 
/* 269 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 270 */       if (listeners[i] == LoginListener.class) {
/* 271 */         ((LoginListener)listeners[(i + 1)]).loginCanceled(source);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getServer()
/*     */   {
/* 280 */     return this.server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServer(String server)
/*     */   {
/* 288 */     String old = getServer();
/* 289 */     this.server = server;
/* 290 */     firePropertyChange("server", old, getServer());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\LoginService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */