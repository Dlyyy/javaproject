/*    */ import com.birosoft.liquid.LiquidLookAndFeel;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import java.io.File;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
/*    */ import javax.swing.UIManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Launcher
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 35 */     System.setProperty("sun.java2d.ddscale", "true");
/* 36 */     UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
/*    */     
/* 38 */     UIManager.addPropertyChangeListener(new PropertyChangeListener()
/*    */     {
/*    */       public void propertyChange(PropertyChangeEvent event)
/*    */       {
/* 42 */         Object newLF = event.getNewValue();
/*    */         
/* 44 */         if (!(newLF instanceof LiquidLookAndFeel)) {
/*    */           try {
/* 46 */             UIManager.setLookAndFeel(new LiquidLookAndFeel());
/*    */           } catch (Exception e) {
/* 48 */             e.printStackTrace();
/*    */           }
/*    */           
/*    */         }
/*    */         
/*    */       }
/* 54 */     });
/* 55 */     Class mainClass = null;
/*    */     
/* 57 */     if (args[0].toLowerCase().endsWith(".jar")) {
/* 58 */       File file = new File(args[0]);
/* 59 */       JarFile jarFile = new JarFile(file);
/*    */       
/* 61 */       Manifest manifest = jarFile.getManifest();
/* 62 */       String mainClassName = manifest.getMainAttributes().getValue("Main-Class");
/* 63 */       URLClassLoader loader = new URLClassLoader(new URL[] { file.toURL() }, Launcher.class.getClassLoader());
/*    */       
/* 65 */       mainClass = Class.forName(mainClassName, true, loader);
/*    */     } else {
/* 67 */       mainClass = Class.forName(args[0]);
/*    */     }
/*    */     
/* 70 */     Method m = mainClass.getMethod("main", new Class[] { new String[0].getClass() });
/*    */     
/* 72 */     String[] copyOfArgs = new String[args.length - 1];
/* 73 */     for (int i = 1; i < args.length; i++) {
/* 74 */       copyOfArgs[(i - 1)] = args[i];
/*    */     }
/* 76 */     m.invoke(mainClass, new Object[] { copyOfArgs });
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\Launcher.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */