/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternModel
/*     */ {
/*     */   public static final String SEARCH_PREFIX = "Search.";
/*     */   public static final String REGEX_UNCHANGED = "regex";
/*     */   public static final String REGEX_ANCHORED = "anchored";
/*     */   public static final String REGEX_WILDCARD = "wildcard";
/*     */   public static final String REGEX_MATCH_RULES = "explicit";
/*     */   public static final String MATCH_RULE_CONTAINS = "contains";
/*     */   public static final String MATCH_RULE_EQUALS = "equals";
/*     */   public static final String MATCH_RULE_ENDSWITH = "endsWith";
/*     */   public static final String MATCH_RULE_STARTSWITH = "startsWith";
/*     */   public static final String MATCH_BACKWARDS_ACTION_COMMAND = "backwardsSearch";
/*     */   public static final String MATCH_WRAP_ACTION_COMMAND = "wrapSearch";
/*     */   public static final String MATCH_CASE_ACTION_COMMAND = "matchCase";
/*     */   public static final String MATCH_INCREMENTAL_ACTION_COMMAND = "matchIncremental";
/*     */   private String rawText;
/*     */   private boolean backwards;
/*     */   private Pattern pattern;
/*     */   private int foundIndex;
/*     */   private boolean caseSensitive;
/*     */   private PropertyChangeSupport propertySupport;
/*     */   private String regexCreatorKey;
/*     */   private RegexCreator regexCreator;
/*     */   private boolean wrapping;
/*     */   private boolean incremental;
/*     */   
/*     */   public PatternModel()
/*     */   {
/* 131 */     this.foundIndex = -1;
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
/*     */   public int getFoundIndex()
/*     */   {
/* 149 */     return this.foundIndex;
/*     */   }
/*     */   
/*     */   public void setFoundIndex(int foundIndex) {
/* 153 */     int old = getFoundIndex();
/* 154 */     updateFoundIndex(foundIndex);
/* 155 */     firePropertyChange("foundIndex", Integer.valueOf(old), Integer.valueOf(getFoundIndex()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateFoundIndex(int newFoundIndex)
/*     */   {
/* 163 */     if (newFoundIndex < 0) {
/* 164 */       this.foundIndex = newFoundIndex;
/* 165 */       return;
/*     */     }
/* 167 */     if (isAutoAdjustFoundIndex()) {
/* 168 */       this.foundIndex = (this.backwards ? newFoundIndex - 1 : newFoundIndex + 1);
/*     */     } else {
/* 170 */       this.foundIndex = newFoundIndex;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAutoAdjustFoundIndex()
/*     */   {
/* 176 */     return !isIncremental();
/*     */   }
/*     */   
/*     */   public boolean isBackwards() {
/* 180 */     return this.backwards;
/*     */   }
/*     */   
/*     */   public void setBackwards(boolean backwards) {
/* 184 */     boolean old = isBackwards();
/* 185 */     this.backwards = backwards;
/* 186 */     firePropertyChange("backwards", Boolean.valueOf(old), Boolean.valueOf(isBackwards()));
/* 187 */     setFoundIndex(getFoundIndex());
/*     */   }
/*     */   
/*     */   public boolean isWrapping() {
/* 191 */     return this.wrapping;
/*     */   }
/*     */   
/*     */   public void setWrapping(boolean wrapping) {
/* 195 */     boolean old = isWrapping();
/* 196 */     this.wrapping = wrapping;
/* 197 */     firePropertyChange("wrapping", Boolean.valueOf(old), Boolean.valueOf(isWrapping()));
/*     */   }
/*     */   
/*     */   public void setIncremental(boolean incremental) {
/* 201 */     boolean old = isIncremental();
/* 202 */     this.incremental = incremental;
/* 203 */     firePropertyChange("incremental", Boolean.valueOf(old), Boolean.valueOf(isIncremental()));
/*     */   }
/*     */   
/*     */   public boolean isIncremental() {
/* 207 */     return this.incremental;
/*     */   }
/*     */   
/*     */   public boolean isCaseSensitive()
/*     */   {
/* 212 */     return this.caseSensitive;
/*     */   }
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/* 216 */     boolean old = isCaseSensitive();
/* 217 */     this.caseSensitive = caseSensitive;
/* 218 */     updatePattern(caseSensitive);
/* 219 */     firePropertyChange("caseSensitive", Boolean.valueOf(old), Boolean.valueOf(isCaseSensitive()));
/*     */   }
/*     */   
/*     */   public Pattern getPattern() {
/* 223 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public String getRawText() {
/* 227 */     return this.rawText;
/*     */   }
/*     */   
/*     */   public void setRawText(String findText) {
/* 231 */     String old = getRawText();
/* 232 */     boolean oldEmpty = isEmpty();
/* 233 */     this.rawText = findText;
/* 234 */     updatePattern(createRegEx(findText));
/* 235 */     firePropertyChange("rawText", old, getRawText());
/* 236 */     firePropertyChange("empty", Boolean.valueOf(oldEmpty), Boolean.valueOf(isEmpty()));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 240 */     return isEmpty(getRawText());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String createRegEx(String searchString)
/*     */   {
/* 252 */     if (isEmpty(searchString))
/* 253 */       return null;
/* 254 */     return getRegexCreator().createRegEx(searchString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isEmpty(String text)
/*     */   {
/* 264 */     return (text == null) || (text.length() == 0);
/*     */   }
/*     */   
/*     */   private void updatePattern(String regEx) {
/* 268 */     Pattern old = getPattern();
/* 269 */     if (isEmpty(regEx)) {
/* 270 */       this.pattern = null;
/* 271 */     } else if ((old == null) || (!old.pattern().equals(regEx))) {
/* 272 */       this.pattern = Pattern.compile(regEx, getFlags());
/*     */     }
/* 274 */     firePropertyChange("pattern", old, getPattern());
/*     */   }
/*     */   
/*     */   private int getFlags() {
/* 278 */     return isCaseSensitive() ? 0 : getCaseInsensitiveFlag();
/*     */   }
/*     */   
/*     */   private int getCaseInsensitiveFlag() {
/* 282 */     return 66;
/*     */   }
/*     */   
/*     */   private void updatePattern(boolean caseSensitive) {
/* 286 */     if (this.pattern == null)
/* 287 */       return;
/* 288 */     Pattern old = getPattern();
/* 289 */     int flags = old.flags();
/* 290 */     int flag = getCaseInsensitiveFlag();
/* 291 */     if ((caseSensitive) && ((flags & flag) != 0)) {
/* 292 */       this.pattern = Pattern.compile(this.pattern.pattern(), 0);
/* 293 */     } else if ((!caseSensitive) && ((flags & flag) == 0)) {
/* 294 */       this.pattern = Pattern.compile(this.pattern.pattern(), flag);
/*     */     }
/* 296 */     firePropertyChange("pattern", old, getPattern());
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener l) {
/* 300 */     if (this.propertySupport == null) {
/* 301 */       this.propertySupport = new PropertyChangeSupport(this);
/*     */     }
/* 303 */     this.propertySupport.addPropertyChangeListener(l);
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener l) {
/* 307 */     if (this.propertySupport == null)
/* 308 */       return;
/* 309 */     this.propertySupport.removePropertyChangeListener(l);
/*     */   }
/*     */   
/*     */   protected void firePropertyChange(String name, Object oldValue, Object newValue)
/*     */   {
/* 314 */     if (this.propertySupport == null)
/* 315 */       return;
/* 316 */     this.propertySupport.firePropertyChange(name, oldValue, newValue);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class RegexCreator
/*     */   {
/*     */     protected String matchRule;
/*     */     
/*     */     private List<String> rules;
/*     */     
/*     */ 
/*     */     public String getMatchRule()
/*     */     {
/* 329 */       if (this.matchRule == null) {
/* 330 */         this.matchRule = getDefaultMatchRule();
/*     */       }
/* 332 */       return this.matchRule;
/*     */     }
/*     */     
/*     */     public boolean isAutoDetect() {
/* 336 */       return false;
/*     */     }
/*     */     
/*     */     public String createRegEx(String searchString) {
/* 340 */       if ("contains".equals(getMatchRule())) {
/* 341 */         return createContainedRegEx(searchString);
/*     */       }
/* 343 */       if ("equals".equals(getMatchRule())) {
/* 344 */         return createEqualsRegEx(searchString);
/*     */       }
/* 346 */       if ("startsWith".equals(getMatchRule())) {
/* 347 */         return createStartsAnchoredRegEx(searchString);
/*     */       }
/* 349 */       if ("endsWith".equals(getMatchRule())) {
/* 350 */         return createEndAnchoredRegEx(searchString);
/*     */       }
/* 352 */       return searchString;
/*     */     }
/*     */     
/*     */     protected String createEndAnchoredRegEx(String searchString) {
/* 356 */       return Pattern.quote(searchString) + "$";
/*     */     }
/*     */     
/*     */     protected String createStartsAnchoredRegEx(String searchString) {
/* 360 */       return "^" + Pattern.quote(searchString);
/*     */     }
/*     */     
/*     */     protected String createEqualsRegEx(String searchString) {
/* 364 */       return "^" + Pattern.quote(searchString) + "$";
/*     */     }
/*     */     
/*     */     protected String createContainedRegEx(String searchString) {
/* 368 */       return Pattern.quote(searchString);
/*     */     }
/*     */     
/*     */     public void setMatchRule(String category) {
/* 372 */       this.matchRule = category;
/*     */     }
/*     */     
/*     */     protected String getDefaultMatchRule() {
/* 376 */       return "contains";
/*     */     }
/*     */     
/*     */     public List<String> getMatchRules() {
/* 380 */       if (this.rules == null) {
/* 381 */         this.rules = createAndInitRules();
/*     */       }
/* 383 */       return this.rules;
/*     */     }
/*     */     
/*     */     private List<String> createAndInitRules() {
/* 387 */       if (!supportsRules()) return Collections.emptyList();
/* 388 */       List<String> list = new ArrayList();
/* 389 */       list.add("contains");
/* 390 */       list.add("equals");
/* 391 */       list.add("startsWith");
/* 392 */       list.add("endsWith");
/* 393 */       return list;
/*     */     }
/*     */     
/*     */     private boolean supportsRules() {
/* 397 */       return true;
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
/*     */   public static class AnchoredSearchMode
/*     */     extends PatternModel.RegexCreator
/*     */   {
/*     */     public boolean isAutoDetect()
/*     */     {
/* 413 */       return true;
/*     */     }
/*     */     
/*     */     public String createRegEx(String searchExp)
/*     */     {
/* 418 */       if (isAutoDetect()) {
/* 419 */         StringBuffer buf = new StringBuffer(searchExp.length() + 4);
/* 420 */         if ((!hasStartAnchor(searchExp)) && 
/* 421 */           (isStartAnchored())) {
/* 422 */           buf.append("^");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 427 */         buf.append(searchExp);
/*     */         
/* 429 */         if ((!hasEndAnchor(searchExp)) && 
/* 430 */           (isEndAnchored())) {
/* 431 */           buf.append("$");
/*     */         }
/*     */         
/*     */ 
/* 435 */         return buf.toString();
/*     */       }
/* 437 */       return super.createRegEx(searchExp);
/*     */     }
/*     */     
/*     */     private boolean hasStartAnchor(String str) {
/* 441 */       return str.startsWith("^");
/*     */     }
/*     */     
/*     */     private boolean hasEndAnchor(String str) {
/* 445 */       int len = str.length();
/* 446 */       if (str.charAt(len - 1) != '$') {
/* 447 */         return false;
/*     */       }
/*     */       
/* 450 */       if (len == 1) {
/* 451 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 457 */       for (int n = len - 2; n >= 0; n--) {
/* 458 */         if (str.charAt(n) != '\\') {
/* 459 */           return (len - n) % 2 == 0;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 464 */       return len % 2 != 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isStartAnchored()
/*     */     {
/* 473 */       return ("equals".equals(getMatchRule())) || ("startsWith".equals(getMatchRule()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEndAnchored()
/*     */     {
/* 495 */       return ("equals".equals(getMatchRule())) || ("endsWith".equals(getMatchRule()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRegexCreatorKey(String mode)
/*     */   {
/* 541 */     if (getRegexCreatorKey().equals(mode)) return;
/* 542 */     String old = getRegexCreatorKey();
/* 543 */     this.regexCreatorKey = mode;
/* 544 */     createRegexCreator(getRegexCreatorKey());
/* 545 */     firePropertyChange("regexCreatorKey", old, getRegexCreatorKey());
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
/*     */   protected void createRegexCreator(String mode)
/*     */   {
/* 560 */     if ("anchored".equals(mode)) {
/* 561 */       setRegexCreator(new AnchoredSearchMode());
/*     */     } else {
/* 563 */       setRegexCreator(new RegexCreator());
/*     */     }
/*     */   }
/*     */   
/*     */   public String getRegexCreatorKey()
/*     */   {
/* 569 */     if (this.regexCreatorKey == null) {
/* 570 */       this.regexCreatorKey = getDefaultRegexCreatorKey();
/*     */     }
/* 572 */     return this.regexCreatorKey;
/*     */   }
/*     */   
/*     */   private String getDefaultRegexCreatorKey() {
/* 576 */     return "explicit";
/*     */   }
/*     */   
/*     */   private RegexCreator getRegexCreator() {
/* 580 */     if (this.regexCreator == null) {
/* 581 */       this.regexCreator = new RegexCreator();
/*     */     }
/* 583 */     return this.regexCreator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRegexCreator(RegexCreator regexCreator)
/*     */   {
/* 594 */     Object old = this.regexCreator;
/* 595 */     this.regexCreator = regexCreator;
/* 596 */     firePropertyChange("regexCreator", old, regexCreator);
/*     */   }
/*     */   
/*     */   public void setMatchRule(String category) {
/* 600 */     if (getMatchRule().equals(category)) {
/* 601 */       return;
/*     */     }
/* 603 */     String old = getMatchRule();
/* 604 */     getRegexCreator().setMatchRule(category);
/* 605 */     updatePattern(createRegEx(getRawText()));
/* 606 */     firePropertyChange("matchRule", old, getMatchRule());
/*     */   }
/*     */   
/*     */   public String getMatchRule() {
/* 610 */     return getRegexCreator().getMatchRule();
/*     */   }
/*     */   
/*     */   public List<String> getMatchRules() {
/* 614 */     return getRegexCreator().getMatchRules();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\PatternModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */