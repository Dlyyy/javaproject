class ThisDemo1 {
	public static void main(String[] args) {
		Dog d = new Dog();
		String name = "���" ;
		//d.setName(name);
		name = "����" ;
		System.out.println(name);
	}
}

class Dog{
	private String name ;
	private String color ;
	public String getName(){
		return this.name ;
	}
	public void setName(String name){
		 name = "����" ;
	}
}
