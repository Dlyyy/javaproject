class ClassDemo2 {
	public static void main(String[] args) 	{
		Dog d1 = new Dog();
		Dog d2 = new Dog();
		d1.name = "����" ;
		d2.name = "С��" ;
		d1.color = "white" ;
		
		System.out.println(d1.name);
		System.out.println(d2.name);

		System.out.println("��������1.-------------");
		//��������
		new Dog().catchMouse();
		System.out.println("��������2.-------------");
		//
		Dog d3 = buyDog(d1);
		System.out.println(d3.name);
	}
	
	//
	public static Dog buyDog(Dog d){
		System.out.println("��ʼ��....");
		d.name = "����" ;
		System.out.println("ëɫ : " + d.color + " , name : " + d.name);
		System.out.println("��������!");
		return d ;
	}
}

class Dog{
	private String name ;
	String color ; 
	void watch(){
		System.out.println("�������ˣ�����...");
	}

	void catchMouse(){
		System.out.println("ץ��ֻ����, :))");
	}
}
