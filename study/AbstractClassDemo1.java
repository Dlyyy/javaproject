class AbstractClassDemo1 {
	public static void main(String[] args) 	{
		Benz benz = new Benz();
		System.out.println(benz.color);
	}
}
//³éÏóÀà
abstract class Car{
	public String color  = "red";
	public int tires ;
}

class Benz extends Car{
}