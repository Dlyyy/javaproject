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

	//����add
	public static int add(int a,int b ,int c){
		return a + b + c ;
	}
	//����add
	public static double add(int a,float b){
		return a + b ;
	}
	//����add
	public static float add(float a,int b){
		return a + b ;
	}

	//����,�ɱ����
	public static void out(int... xyz){
		for(int i = 0 ; i < xyz.length ; i ++){
			System.out.println(xyz[i]);
		}
	}
}
