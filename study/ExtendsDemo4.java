/**
 * extends继承,考察构造代码块 + 构造函数
 */
class ExtendsDemo4{
	public static void main(String[] args){
		//创建对象
		BMWSportCar mycar = new BMWSportCar();
//		mycar.velocity = 200;
//		mycar.price = 300;
//		mycar.run();
//		mycar.setColorPro("钢琴红色烤漆");
//		System.out.println(mycar.getColor());
	}
}

class Car{
	//私有属性
	private String color ;
	{
		System.out.println("Cons Block in Car");
	}
	public Car(){
		System.out.println("new Car()");
	}
	//公有方法，曲线救国
	public String  getColor(){
		return color ;
	}
	public void setColor(String color){
		this.color = color ;
	}
	public void run(){
		System.out.println("跑了~~");
	}
}

class SportCar extends Car{
	public int velocity ;
	{
		System.out.println("Cons Block in SportCar");
	}
	public SportCar(){
		System.out.println("new SportCar()");
	}
}

class BMWSportCar extends SportCar{
	public int price ;
	public int velocity ;
	{
		System.out.println("Cons Block in BMWSportCar");
	}

	public BMWSportCar(){
		System.out.println("new BMWSportCar()");
	}
}
