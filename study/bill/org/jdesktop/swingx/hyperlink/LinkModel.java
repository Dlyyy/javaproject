/*     */ package org.jdesktop.swingx.hyperlink;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkModel
/*     */   implements Comparable
/*     */ {
/*  40 */   private static final Logger LOG = Logger.getLogger(LinkModel.class.getName());
/*     */   
/*     */ 
/*     */   private String text;
/*     */   
/*     */   private URL url;
/*     */   
/*     */   private String target;
/*     */   
/*  49 */   private boolean visited = false;
/*     */   
/*     */ 
/*     */   private PropertyChangeSupport propertyChangeSupport;
/*     */   
/*     */ 
/*     */   public static final String VISITED_PROPERTY = "visited";
/*     */   
/*  57 */   private static String defaultURLString = "https://jdnc.dev.java.net";
/*     */   
/*     */ 
/*     */ 
/*     */   private static URL defaultURL;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkModel(String text, String target, URL url)
/*     */   {
/*  68 */     setText(text);
/*  69 */     setTarget(target);
/*  70 */     setURL(url != null ? url : getDefaultURL());
/*     */   }
/*     */   
/*     */   public LinkModel() {
/*  74 */     this(" ", null, null);
/*     */   }
/*     */   
/*     */   public LinkModel(String text) {
/*  78 */     this(text, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkModel(String text, String target, String template, String[] args)
/*     */   {
/*  89 */     setText(text);
/*  90 */     setTarget(target);
/*  91 */     setURL(createURL(template, args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setText(String text)
/*     */   {
/*  98 */     String old = getText();
/*  99 */     this.text = text;
/* 100 */     firePropertyChange("text", old, getText());
/*     */   }
/*     */   
/*     */   public String getText() {
/* 104 */     if (this.text != null)
/* 105 */       return this.text;
/* 106 */     if (this.url != null) {
/* 107 */       return getURL().toString();
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */   
/*     */   public void setURLString(String howToURLString) {
/* 113 */     URL url = null;
/*     */     try {
/* 115 */       url = new URL(howToURLString);
/*     */     } catch (MalformedURLException e) {
/* 117 */       url = getDefaultURL();
/* 118 */       LOG.warning("the given urlString is malformed: " + howToURLString + "\n falling back to default url: " + url);
/*     */     }
/*     */     
/* 121 */     setURL(url);
/*     */   }
/*     */   
/*     */   private URL getDefaultURL() {
/* 125 */     if (defaultURL == null) {
/*     */       try {
/* 127 */         defaultURL = new URL(defaultURLString);
/*     */       } catch (MalformedURLException e) {
/* 129 */         LOG.fine("should not happen - defaultURL is wellFormed: " + defaultURLString);
/*     */       }
/*     */     }
/*     */     
/* 133 */     return defaultURL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setURL(URL url)
/*     */   {
/* 142 */     if (url == null) {
/* 143 */       throw new IllegalArgumentException("URL for link cannot be null");
/*     */     }
/* 145 */     if (url.equals(getURL()))
/* 146 */       return;
/* 147 */     URL old = getURL();
/* 148 */     this.url = url;
/* 149 */     firePropertyChange("URL", old, url);
/* 150 */     setVisited(false);
/*     */   }
/*     */   
/*     */   public URL getURL() {
/* 154 */     return this.url;
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
/*     */   private URL createURL(String template, String[] args)
/*     */   {
/* 173 */     URL url = null;
/*     */     try {
/* 175 */       String urlStr = template;
/* 176 */       for (int i = 0; i < args.length; i++) {
/* 177 */         urlStr = urlStr.replaceAll("@\\{" + (i + 1) + "\\}", args[i]);
/*     */       }
/* 179 */       url = new URL(urlStr);
/*     */     }
/*     */     catch (MalformedURLException ex) {}
/*     */     
/* 183 */     return url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTarget(String target)
/*     */   {
/* 192 */     this.target = target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTarget()
/*     */   {
/* 201 */     if (this.target != null) {
/* 202 */       return this.target;
/*     */     }
/* 204 */     return "_blank";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVisited(boolean visited)
/*     */   {
/* 213 */     boolean old = getVisited();
/* 214 */     this.visited = visited;
/* 215 */     firePropertyChange("visited", old, getVisited());
/*     */   }
/*     */   
/*     */   public boolean getVisited() {
/* 219 */     return this.visited;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener l)
/*     */   {
/* 225 */     getPropertyChangeSupport().addPropertyChangeListener(l);
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener l)
/*     */   {
/* 230 */     if (this.propertyChangeSupport == null)
/* 231 */       return;
/* 232 */     this.propertyChangeSupport.removePropertyChangeListener(l);
/*     */   }
/*     */   
/*     */   protected void firePropertyChange(String property, Object oldValue, Object newValue)
/*     */   {
/* 237 */     if (this.propertyChangeSupport == null)
/* 238 */       return;
/* 239 */     this.propertyChangeSupport.firePropertyChange(property, oldValue, newValue);
/*     */   }
/*     */   
/*     */   protected void firePropertyChange(String property, boolean oldValue, boolean newValue)
/*     */   {
/* 244 */     if (this.propertyChangeSupport == null)
/* 245 */       return;
/* 246 */     this.propertyChangeSupport.firePropertyChange(property, oldValue, newValue);
/*     */   }
/*     */   
/*     */   private PropertyChangeSupport getPropertyChangeSupport()
/*     */   {
/* 251 */     if (this.propertyChangeSupport == null) {
/* 252 */       this.propertyChangeSupport = new PropertyChangeSupport(this);
/*     */     }
/* 254 */     return this.propertyChangeSupport;
/*     */   }
/*     */   
/*     */   public int compareTo(Object obj)
/*     */   {
/* 259 */     if (obj == null) {
/* 260 */       return 1;
/*     */     }
/* 262 */     if (obj == this) {
/* 263 */       return 0;
/*     */     }
/* 265 */     return this.text.compareTo(((LinkModel)obj).text);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 270 */     if (this == obj) {
/* 271 */       return true;
/*     */     }
/* 273 */     if ((obj != null) && ((obj instanceof LinkModel))) {
/* 274 */       LinkModel other = (LinkModel)obj;
/* 275 */       if (!getText().equals(other.getText())) {
/* 276 */         return false;
/*     */       }
/*     */       
/* 279 */       if (!getTarget().equals(other.getTarget())) {
/* 280 */         return false;
/*     */       }
/*     */       
/* 283 */       return getURL().equals(other.getURL());
/*     */     }
/* 285 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 290 */     int result = 7;
/*     */     
/* 292 */     result = 37 * result + (getText() == null ? 0 : getText().hashCode());
/* 293 */     result = 37 * result + (getTarget() == null ? 1 : getTarget().hashCode());
/*     */     
/* 295 */     result = 37 * result + (getURL() == null ? 2 : getURL().hashCode());
/*     */     
/* 297 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 303 */     StringBuffer buffer = new StringBuffer("[");
/*     */     
/*     */ 
/* 306 */     buffer.append("url=");
/* 307 */     buffer.append(this.url);
/* 308 */     buffer.append(", target=");
/* 309 */     buffer.append(this.target);
/* 310 */     buffer.append(", text=");
/* 311 */     buffer.append(this.text);
/* 312 */     buffer.append("]");
/*     */     
/* 314 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\hyperlink\LinkModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */