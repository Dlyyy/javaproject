
/**
 * extends继承
 * super()调用父类的构造函数.
 */
class ExtendsDemo2{
	public static void main(String[] args){
		//创建对象
		BMWSportCar mycar = new BMWSportCar();
		mycar.color = "red";
		mycar.velocity = 200;
		mycar.price = 300;
		mycar.run();
	}
}

class Car{
	public String color ;
	public void run(){
		System.out.println("跑了~~");
	}
	public Car(){
		System.out.println("new Car()");
	}
	public Car(String color){
		this.color = color ;
		System.out.println("new Car("+color+")");
	}
}

class SportCar extends Car{
	public int velocity ;
	public SportCar(int velocity){
		this.velocity = velocity ;
		System.out.println("new SportCar("+velocity+")");
	}
}

class BMWSportCar extends SportCar{
	public int price ;

	public BMWSportCar(){
		super(220);
		System.out.println("new BMWSportCar()");
	}
}
