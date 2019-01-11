/*     */ package org.jdesktop.swingx.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.login.AccountExpiredException;
/*     */ import javax.security.auth.login.CredentialExpiredException;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginContext;
/*     */ import javax.security.auth.login.LoginException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAASLoginService
/*     */   extends LoginService
/*     */ {
/*  45 */   private static final Logger LOG = Logger.getLogger(JAASLoginService.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */   protected LoginContext loginContext;
/*     */   
/*     */ 
/*     */ 
/*     */   public JAASLoginService(String server)
/*     */   {
/*  55 */     super(server);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JAASLoginService() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean authenticate(String name, char[] password, String server)
/*     */     throws Exception
/*     */   {
/*  73 */     if ((server != null) && 
/*  74 */       (!server.equals(getServer()))) {
/*  75 */       setServer(server);
/*     */     }
/*     */     
/*     */ 
/*  79 */     this.loginContext = null;
/*     */     
/*     */     try
/*     */     {
/*  83 */       this.loginContext = new LoginContext(getServer(), new JAASCallbackHandler(name, password));
/*     */       
/*  85 */       this.loginContext.login();
/*  86 */       return true;
/*     */     }
/*     */     catch (AccountExpiredException e) {
/*  89 */       LOG.log(Level.WARNING, "", e);
/*  90 */       return false;
/*     */     }
/*     */     catch (CredentialExpiredException e) {
/*  93 */       LOG.log(Level.WARNING, "", e);
/*  94 */       return false;
/*     */     }
/*     */     catch (FailedLoginException e) {
/*  97 */       LOG.log(Level.WARNING, "", e);
/*  98 */       return false;
/*     */     }
/*     */     catch (LoginException e) {
/* 101 */       LOG.log(Level.WARNING, "", e);
/* 102 */       return false;
/*     */     }
/*     */     catch (Throwable e) {
/* 105 */       LOG.log(Level.WARNING, "", e); }
/* 106 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LoginContext getLoginContext()
/*     */   {
/* 116 */     return this.loginContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Subject getSubject()
/*     */   {
/* 126 */     if (this.loginContext == null)
/* 127 */       return null;
/* 128 */     return this.loginContext.getSubject();
/*     */   }
/*     */   
/*     */   class JAASCallbackHandler implements CallbackHandler
/*     */   {
/*     */     private String name;
/*     */     private char[] password;
/*     */     
/*     */     public JAASCallbackHandler(String name, char[] passwd)
/*     */     {
/* 138 */       this.name = name;
/* 139 */       this.password = passwd;
/*     */     }
/*     */     
/*     */     public void handle(Callback[] callbacks) throws IOException {
/* 143 */       for (int i = 0; i < callbacks.length; i++) {
/* 144 */         if ((callbacks[i] instanceof NameCallback)) {
/* 145 */           NameCallback cb = (NameCallback)callbacks[i];
/* 146 */           cb.setName(this.name);
/* 147 */         } else if ((callbacks[i] instanceof PasswordCallback)) {
/* 148 */           PasswordCallback cb = (PasswordCallback)callbacks[i];
/* 149 */           cb.setPassword(this.password);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\JAASLoginService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */