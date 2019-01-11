/*    */ package test;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class Test
/*    */ {
/*    */   public static void main(String[] args) throws IOException
/*    */   {
/* 11 */     URL u = ClassLoader.getSystemResource("img/restore.png");
/* 12 */     System.out.println(u.openStream().available());
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\test\Test.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */