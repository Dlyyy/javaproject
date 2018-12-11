class FunctionDemo2 {
	public static void main(String[] args) {
		//System.out.println(add(1,2.0f));
		add(1,2.0f);
		out(1,2,3);

	}
	
	//add
	public static int add(int a,int b){
		return a + b ;
	}

	//重载add
	public static int add(int a,int b ,int c){
		return a + b + c ;
	}
	//重载add
	public static double add(int a,float b){
		return a + b ;
	}
	//重载add
	public static float add(float a,int b){
		return a + b ;
	}

	//重载,可变参数
	public static void out(int... xyz){
		for(int i = 0 ; i < xyz.length ; i ++){
			System.out.println(xyz[i]);
		}
	}
}
