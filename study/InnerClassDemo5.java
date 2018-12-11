/**
 * 外部类访问其他类的内部类，使用static修饰内部类，通过Outer.InnerClass
 */
class InnerClassDemo5 {
	public static void main(String[] args) 	{
		Benz b = new Benz();
		b.new Engine().fire();
	}
}

class Benz{
	public int tires = 4;

	class Engine{
		public int tires  = 5;
		public void fire(){
			System.out.println(Benz.this.tires);
			System.out.println("点火");
		}
	}
}
