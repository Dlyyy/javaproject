class ThreadDemo6 {
	public static void main(String[] args) 	{
		System.out.println("Hello World!");
		Thread t = Thread.currentThread();
		t.setName("this is my main thread!");
		System.out.println(t.getName());
		
		t.setPriority(100);
		int p = t.getPriority();
		System.out.println(p);
	}
}
