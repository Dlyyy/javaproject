class InnerClassDemo2 {
	public static void main(String[] args) {
		Benz benz = new Benz();
		benz.run();
	}
}
class Benz{
	public String color = "黑色" ;
	public int tires ;
	public void run(){
		//局部变量
		final String key = "sss";
		//内部类
		class Engine{
			public void fire(){
				System.out.println("插入钥匙 : " + key);
				System.out.println("点火");
			}
		}
		new Engine().fire();
		System.out.println("开跑~~~");
		//key = "xxx" ;
	}
}