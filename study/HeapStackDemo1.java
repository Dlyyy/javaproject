class HeapStackDemo1{
	public static void main(String[] args) {
		//testStack(1);
		testHeap();
	}

	//²âÊÔÕ»¿Õ¼ä
	public static void testStack(int i){
		if(i >= 6595){
			return ;
		}
		System.out.println(i + " : hello world");
		int j = i + 1;
		testStack(j);
	}
	//java -Xmx500m -Xms500m 
	public static void testHeap(){
		int size = 1024 * 1024 * 1024 ;
		byte[][] arr = new byte[4][size];
		//arr[0][] = new byte[size];
		//arr[1] = new byte[size];
		//arr[2] = new byte[size];
		//arr[3] = new byte[size];

		System.out.println(arr);

	}
}
