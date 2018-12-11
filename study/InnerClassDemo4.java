/**
 * 外部类访问其他类的内部类，使用static修饰内部类，通过Outer.InnerClass
 */
class InnerClassDemo4 {
	public static void main(String[] args) 	{
		//
		Benz b = new Benz();
		new Benz.Engine().fire();
	}
}

class Benz{
	static class Engine{
		public void fire(){
			System.out.println("点火");
		}
	}
}
