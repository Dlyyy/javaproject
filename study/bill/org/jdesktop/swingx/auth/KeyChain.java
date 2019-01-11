/*     */ package org.jdesktop.swingx.auth;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStore.PasswordProtection;
/*     */ import java.security.KeyStore.SecretKeyEntry;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableEntryException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeyChain
/*     */ {
/*  61 */   private static final Logger LOG = Logger.getLogger(KeyChain.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private KeyStore store;
/*     */   
/*     */ 
/*     */ 
/*     */   private char[] masterPassword;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public KeyChain(char[] masterPassword, InputStream inputStream)
/*     */     throws IOException
/*     */   {
/*  78 */     this.masterPassword = masterPassword;
/*     */     try
/*     */     {
/*  81 */       this.store = KeyStore.getInstance("JCEKS");
/*  82 */       this.store.load(inputStream, masterPassword);
/*     */     }
/*     */     catch (KeyStoreException ex) {
/*  85 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (CertificateException ex) {
/*  87 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (NoSuchAlgorithmException ex) {
/*  89 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (EOFException ex) {
/*  91 */       LOG.log(Level.WARNING, "", ex);
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
/*     */   public String getPassword(String user, String server)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       KeyStore.SecretKeyEntry entry2 = (KeyStore.SecretKeyEntry)this.store.getEntry(user + "@" + server, new KeyStore.PasswordProtection(this.masterPassword));
/*     */       
/*     */ 
/* 110 */       return new String(entry2.getSecretKey().getEncoded());
/*     */     } catch (KeyStoreException ex) {
/* 112 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (UnrecoverableEntryException ex) {
/* 114 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (NoSuchAlgorithmException ex) {
/* 116 */       LOG.log(Level.WARNING, "", ex);
/*     */     }
/*     */     
/* 119 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPassword(String user, String server, char[] password)
/*     */   {
/* 131 */     String pass = new String(password);
/* 132 */     SecretKeySpec passwordKey = new SecretKeySpec(pass.getBytes(), "JCEKS");
/* 133 */     KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(passwordKey);
/*     */     try {
/* 135 */       this.store.setEntry(user + "@" + server, entry, new KeyStore.PasswordProtection(this.masterPassword));
/*     */     }
/*     */     catch (KeyStoreException e) {
/* 138 */       LOG.log(Level.WARNING, "", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePassword(String user, String server)
/*     */   {
/*     */     try
/*     */     {
/* 150 */       this.store.deleteEntry(user + "@" + server);
/*     */     } catch (KeyStoreException e) {
/* 152 */       LOG.log(Level.WARNING, "", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void store(OutputStream ostream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 165 */       this.store.store(ostream, this.masterPassword);
/*     */     } catch (KeyStoreException ex) {
/* 167 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (CertificateException ex) {
/* 169 */       LOG.log(Level.WARNING, "", ex);
/*     */     } catch (NoSuchAlgorithmException ex) {
/* 171 */       LOG.log(Level.WARNING, "", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try {
/* 178 */       File file = new File("c:\\test.txt");
/*     */       FileInputStream fis;
/* 180 */       FileInputStream fis; if (!file.exists()) {
/* 181 */         file.createNewFile();
/* 182 */         fis = null;
/*     */       } else {
/* 184 */         fis = new FileInputStream(file);
/*     */       }
/* 186 */       KeyChain kc = new KeyChain("test".toCharArray(), fis);
/* 187 */       kc.addPassword("bino", "sun-ds.sfbay", "test123".toCharArray());
/* 188 */       LOG.fine("pass = " + kc.getPassword("bino", "sun-ds.sfbay"));
/*     */       
/*     */ 
/* 191 */       LOG.fine("More testing :");
/* 192 */       for (int i = 0; i < 100; i++) {
/* 193 */         kc.addPassword("" + i, "sun-ds.sfbay", ("" + i).toCharArray());
/*     */       }
/* 195 */       for (int i = 0; i < 100; i++) {
/* 196 */         LOG.fine("key =" + i + " pass =" + kc.getPassword(new StringBuilder().append("").append(i).toString(), "sun-ds.sfbay"));
/*     */       }
/*     */       
/* 199 */       kc.store(new FileOutputStream(file));
/*     */     } catch (Exception e) {
/* 201 */       LOG.log(Level.WARNING, "", e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\KeyChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */