/**
 * extends继承
 * super()调用父类的构造函数.
 */
class ExtendsDemo3{
	public static void main(String[] args){
		//创建对象
		BMWSportCar mycar = new BMWSportCar();
		mycar.velocity = 200;
		mycar.price = 300;
		mycar.run();
		mycar.setColorPro("钢琴红色烤漆");
		System.out.println(mycar.getColor());
	}
}

class Car{
	//私有属性
	private String color ;
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
	public SportCar(){
		System.out.println("new SportCar()");
	}
	public void setColorPro(String color){
		System.out.println("是好漆吗？必须是钢琴漆！！");
		//this.setColor(color);
		super.setColor(color);
	}

	public void setColor(String color){
		System.out.println("SportCar.setColor("+color+")")
		super.setColor(color);
	}
}

class BMWSportCar extends SportCar{
	public int price ;

	public BMWSportCar(){
		System.out.println("new BMWSportCar()");
	}
}
