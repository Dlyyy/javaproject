class ConstructorDemo1{
	public static void main(String[] args) {
		Dog d = new Dog("С��","yellow");
		d.watch();
		new Dog();
	}
}

class Dog{

	static{
		System.out.println("��̬�����-1");
	}

	static{
		System.out.println("��̬�����-2");
	}


	//
	public String name = "����" ;

	static{
		
	}
	//��������
	{
		name = "Сǿ" ;
		//System.out.println("111" + name);
	}

	//��������
	{
		name = "����" ;
	}

	private String color ;

	public Dog(){

	}

	//public Dog Dog(){
	//	return null ;
	//}

	public Dog(String n){
		new Dog();
		name = n ;
		System.out.println("1");
	}

	public Dog(String c,String n){
		//name = n ;
		//color = c ;
		System.out.println("2 " + name);
	}
	
	//
	public void watch(){
		System.out.println("����~~~~");	
	}
}
