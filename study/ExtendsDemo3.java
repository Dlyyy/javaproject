/**
 * extends�̳�
 * super()���ø���Ĺ��캯��.
 */
class ExtendsDemo3{
	public static void main(String[] args){
		//��������
		BMWSportCar mycar = new BMWSportCar();
		mycar.velocity = 200;
		mycar.price = 300;
		mycar.run();
		mycar.setColorPro("���ٺ�ɫ����");
		System.out.println(mycar.getColor());
	}
}

class Car{
	//˽������
	private String color ;
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
	public SportCar(){
		System.out.println("new SportCar()");
	}
	public void setColorPro(String color){
		System.out.println("�Ǻ����𣿱����Ǹ����ᣡ��");
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
