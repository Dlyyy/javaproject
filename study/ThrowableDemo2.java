class ThrowableDemo2 {
	public static void main(String[] args) 	{
		try{
			sayHello();
		}
		//StackOverflowError
		catch(VirtualMachineError e){
			System.out.println("������" + e.getMessage());
		}
	}

	public static void sayHello(){
		System.out.println("hello world");
		sayHello();
	}
}
