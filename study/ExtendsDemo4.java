/**
 * extends�̳�,���칹������ + ���캯��
 */
class ExtendsDemo4{
	public static void main(String[] args){
		//��������
		BMWSportCar mycar = new BMWSportCar();
//		mycar.velocity = 200;
//		mycar.price = 300;
//		mycar.run();
//		mycar.setColorPro("���ٺ�ɫ����");
//		System.out.println(mycar.getColor());
	}
}

class Car{
	//˽������
	private String color ;
	{
		System.out.println("Cons Block in Car");
	}
	public Car(){
		System.out.println("new Car()");
	}
	//���з��������߾ȹ�
	public String  getColor(){
		return color ;
	}
	public void setColor(String color){
		this.color = color ;
	}
	public void run(){
		System.out.println("����~~");
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
