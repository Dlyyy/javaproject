class InterfaceDemo3 {
	public static void main(String[] args) {
		Jing8 jing8 = new Jing8();
		jing8.meng();
		System.out.println(Jing8.LEGS);
	}
}


interface Pet{
	int LEGS = 4 ;
	void meng();
}

class Jing8 implements Pet{
	public void meng(){
		System.out.println(LEGS);
	}
}