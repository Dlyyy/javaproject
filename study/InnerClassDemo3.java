/**
 * 外部类访问其他类的内部类，可以采用b.new Xxxx()
 */
class InnerClassDemo3 {
	public static void main(String[] args) 	{
		//1.
		Benz b = new Benz();
		b.new Engine().fire();
	}
}

class Benz{
	class Engine{
		public void fire(){
			System.out.println("点火");
		}
	}
}
