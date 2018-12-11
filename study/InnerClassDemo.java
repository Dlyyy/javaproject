class InnerClassDemo {
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
		String key = "sss";
		new Engine().fire(key);
		System.out.println("开跑~~~");
	}
	//内部类
	class Engine{
		public void fire(String key){
			System.out.println("插入钥匙 : " + key);
			System.out.println("点火");
		}
	}
}