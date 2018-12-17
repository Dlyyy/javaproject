package dly.myjava;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *  µ¥Ôª²âÊÔ 
 */
public class TestDemo {

	@BeforeClass
	public static void ini() {
		System.out.println("preparing...");
	}
	
/*	@Before
	public void ini() {
		System.out.println("preparing......");
	}*/
	
	
	@Test
	public void test() {
		System.out.println("test");
	}
	
	@Test
	public void test1() {
		System.out.println("test1");
	}
}
