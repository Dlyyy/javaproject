/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Icon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerAction
/*     */   extends AbstractAction
/*     */ {
/*  56 */   private static final Logger LOG = Logger.getLogger(ServerAction.class.getName());
/*     */   
/*     */   private static final String PARAMS = "action-params";
/*     */   private static final String HEADERS = "action-headers";
/*     */   private static final String URL = "action-url";
/*     */   private static final String URL_CACHE = "_URL-CACHE__";
/*     */   
/*     */   public ServerAction()
/*     */   {
/*  65 */     this("action");
/*     */   }
/*     */   
/*     */   public ServerAction(String name) {
/*  69 */     super(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerAction(String name, String command)
/*     */   {
/*  77 */     this(name, command, null);
/*     */   }
/*     */   
/*     */   public ServerAction(String name, Icon icon) {
/*  81 */     super(name, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerAction(String name, String command, Icon icon)
/*     */   {
/*  90 */     super(name, icon);
/*  91 */     putValue("ActionCommandKey", command);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setURL(String url)
/*     */   {
/* 100 */     putValue("action-url", url);
/* 101 */     putValue("_URL-CACHE__", null);
/*     */   }
/*     */   
/*     */   public String getURL() {
/* 105 */     return (String)getValue("action-url");
/*     */   }
/*     */   
/*     */   private Map<String, String> getParams()
/*     */   {
/* 110 */     return (Map)getValue("action-params");
/*     */   }
/*     */   
/*     */   private void setParams(Map<String, String> params) {
/* 114 */     putValue("action-params", params);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addParam(String name, String value)
/*     */   {
/* 122 */     Map<String, String> params = getParams();
/* 123 */     if (params == null) {
/* 124 */       params = new HashMap();
/* 125 */       setParams(params);
/*     */     }
/* 127 */     params.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getParamValue(String name)
/*     */   {
/* 134 */     Map<String, String> params = getParams();
/* 135 */     return params == null ? null : (String)params.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<String> getParamNames()
/*     */   {
/* 142 */     Map<String, String> params = getParams();
/* 143 */     return params == null ? null : params.keySet();
/*     */   }
/*     */   
/*     */   private Map<String, String> getHeaders()
/*     */   {
/* 148 */     return (Map)getValue("action-headers");
/*     */   }
/*     */   
/*     */   private void setHeaders(Map<String, String> headers) {
/* 152 */     putValue("action-headers", headers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 161 */     Map<String, String> map = getHeaders();
/* 162 */     if (map == null) {
/* 163 */       map = new HashMap();
/* 164 */       setHeaders(map);
/*     */     }
/* 166 */     map.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getHeaderValue(String name)
/*     */   {
/* 173 */     Map<String, String> headers = getHeaders();
/* 174 */     return headers == null ? null : (String)headers.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<String> getHeaderNames()
/*     */   {
/* 181 */     Map<String, String> headers = getHeaders();
/* 182 */     return headers == null ? null : headers.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 189 */     URL execURL = (URL)getValue("_URL-CACHE__");
/* 190 */     if ((execURL == null) && (!"".equals(getURL()))) {
/*     */       try {
/* 192 */         String url = getURL();
/* 193 */         if (url.startsWith("http")) {
/* 194 */           execURL = new URL(url);
/*     */         }
/*     */         
/* 197 */         if (execURL == null)
/*     */         {
/* 199 */           return;
/*     */         }
/*     */         
/* 202 */         putValue("_URL-CACHE__", execURL);
/*     */       }
/*     */       catch (MalformedURLException ex)
/*     */       {
/* 206 */         LOG.log(Level.WARNING, "something went wrong...", ex);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 211 */       URLConnection uc = execURL.openConnection();
/*     */       
/*     */ 
/* 214 */       Set<String> headerNames = getHeaderNames();
/* 215 */       if ((headerNames != null) && (!headerNames.isEmpty())) {
/* 216 */         Iterator<String> iter = headerNames.iterator();
/* 217 */         while (iter.hasNext()) {
/* 218 */           String name = (String)iter.next();
/* 219 */           uc.setRequestProperty(name, getHeaderValue(name));
/*     */         }
/*     */       }
/* 222 */       uc.setUseCaches(false);
/* 223 */       uc.setDoOutput(true);
/*     */       
/* 225 */       ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
/* 226 */       PrintWriter out = new PrintWriter(byteStream, true);
/* 227 */       out.print(getPostData());
/* 228 */       out.flush();
/*     */       
/*     */ 
/* 231 */       String length = String.valueOf(byteStream.size());
/* 232 */       uc.setRequestProperty("Content-length", length);
/*     */       
/*     */ 
/* 235 */       byteStream.writeTo(uc.getOutputStream());
/*     */       
/* 237 */       BufferedReader buf = null;
/* 238 */       if ((uc instanceof HttpURLConnection)) {
/* 239 */         HttpURLConnection huc = (HttpURLConnection)uc;
/* 240 */         int code = huc.getResponseCode();
/* 241 */         String message = huc.getResponseMessage();
/*     */         
/*     */ 
/* 244 */         if (code < 400)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 249 */           buf = new BufferedReader(new InputStreamReader(uc.getInputStream()));
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 254 */           buf = new BufferedReader(new InputStreamReader(huc.getErrorStream()));
/*     */         }
/*     */         
/*     */ 
/* 258 */         StringBuffer buffer = new StringBuffer();
/* 259 */         String line; while ((line = buf.readLine()) != null)
/*     */         {
/*     */ 
/* 262 */           buffer.append(line);
/* 263 */           buffer.append('\n');
/*     */         }
/*     */         
/* 266 */         LOG.finer("returned from connection\n" + buffer.toString());
/*     */       }
/*     */     } catch (UnknownHostException ex) {
/* 269 */       LOG.log(Level.WARNING, "UnknownHostException detected. Could it be a proxy issue?", ex);
/*     */     }
/*     */     catch (AccessControlException ex) {
/* 272 */       LOG.log(Level.WARNING, "AccessControlException detected", ex);
/*     */     } catch (IOException ex) {
/* 274 */       LOG.log(Level.WARNING, "IOException detected", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getPostData()
/*     */   {
/* 284 */     StringBuffer postData = new StringBuffer();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 289 */     Set<String> paramNames = getParamNames();
/* 290 */     if ((paramNames != null) && (!paramNames.isEmpty())) {
/* 291 */       Iterator<String> iter = paramNames.iterator();
/*     */       try {
/* 293 */         while (iter.hasNext()) {
/* 294 */           String name = (String)iter.next();
/* 295 */           postData.append('&').append(name).append('=');
/* 296 */           postData.append(getParamValue(name));
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {}
/*     */       
/*     */ 
/*     */ 
/* 303 */       postData.setCharAt(0, '?');
/*     */     }
/*     */     
/* 306 */     LOG.finer("ServerAction: POST data: " + postData.toString());
/* 307 */     return postData.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String createMessage(int code, String msg)
/*     */   {
/* 317 */     StringBuffer buffer = new StringBuffer("The action \"");
/* 318 */     buffer.append(getValue("Name"));
/*     */     
/* 320 */     if (code < 400) {
/* 321 */       buffer.append("\" has succeeded ");
/*     */     } else {
/* 323 */       buffer.append("\" has failed\nPlease check the Java console for more details.\n");
/*     */     }
/*     */     
/*     */ 
/* 327 */     buffer.append("\nServer response:\nCode: ");
/* 328 */     buffer.append(code);
/* 329 */     buffer.append(" Message: ");
/* 330 */     buffer.append(msg);
/*     */     
/* 332 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\ServerAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */