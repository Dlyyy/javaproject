package dly.myjava;

public class HelloWorld {
/**
 * 
 * @param args
 */
	public HelloWorld(){
		System.out.println("new helloworld");
	}
	public static void main(String[] args) {
		System.out.println("hello world!");
		sayHello1();
		new HelloWorld().sayHello2();
	}
	public static void sayHello1() {
		System.out.println("hello1");
	}
	public void sayHello2() {
		System.out.println("hello2");
	}
}
