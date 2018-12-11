class CarCaveDemo{
	public static void main(String[] args){
		Cave cave = new Cave();
		Car c1 = new Car(cave,8000,"ţ��");
		Car c2 = new Car(cave,6000,"��");
		Car c3 = new Car(cave,7000,"���ֳ�");
		Car c4 = new Car(cave,4000,"������");
		Car c5 = new Car(cave,1000,"��");
		c1.start();
		c2.start();
		c3.start();
		c4.start();
		c5.start();
	}
}
//ɽ����
class Cave{
	//����ͨ������
	public synchronized void crossCar(Car car){
		try{
			System.out.println(car.name + " : ��ʼ��ɽ��!");
			Thread.sleep(car.time);
			System.out.println(car.name + " : ����ɽ��!");
		}
		catch(Exception e){
		}
	}
}
//
class Car extends Thread{
	public Cave cave ;
	public int time ;
	public String name ;
	public Car(Cave cave,int time,String name){
		this.cave = cave ;
		this.time = time ;
		this.name = name ;
	}

	public void run(){
		cave.crossCar(this);
	}
}