
/**
 * final修饰类，不能被继承。
 */
class FinalDemo1 {
	public static void main(String[] args) {
		Jing8 j = new Jing8();
		j.name = "汪星人" ;
		j.watch();
		System.out.println(j.name);
	}
}

//
class Dog{
	//常量
	public final String name = "ERHA" ; 
	
	//final修饰方法，不能重写。
	public /*final*/ void watch(){
		System.out.println("来人了~~~");
	}
}

class Jing8 extends Dog{
	public void watch(){
		System.out.println("问一问~~~");
		super.watch();
	}
}
