class FunctionDemo1 {
	public static void main(String[] args) {
		//���ú���
		//int ret = add(1,2);
		//System.out.println(ret);
		//printEmptyTriangle(5);
		//int r = getMax(1,2,3);
		//System.out.println(r);

		//System.out.println(getMax(getMax(getMax(1,2),3),4));
		//hello();
		//System.out.println(divide(1,0));
		System.out.println(fabric(0));
	}

	//���庯��
	public static int add(int a,int b){
		return a + b ;
	}

	/**
	 *��ӡ���������κ���
	 */
	public static void printEmptyTriangle(int line){
		//ѭ��������
		for(int i = 0 ; i < line ; i ++){
			//1.���n���ո�
			for(int j = 0 ; j < line - 1 - i ; j ++){
				System.out.print(" ");
			}
			//2.���һ��*
			System.out.print("*");
			//3.�������һ��,���2n��*
			if(i == (line - 1)){
				for(int j = 0 ; j < 2 * i ; j ++){
					System.out.print("*") ;
				}
			}
			//�м���
			if(i != 0 && i != (line - 1)){
				//���2n - 1���ո�
				for(int j = 0 ; j < 2 * i - 1 ; j ++){
					System.out.print(" ");
				}
				System.out.print("*");//����*
			}
			System.out.print("\r\n");
		}
	}

	public static int getMax(int a,int b){
		return a > b ? a : b ;
	}

	public static void hello(){
		System.out.println("how are you!");
		hello() ;
	}

	public static float divide(int a , int b){
		if(b == 0){
			return 0 ;
		}
		return (float)a / b ;
	}

	//�׳�,1 x 2 x 3 x 4 x ...x n
//	public static int fabric(int n){
//		int sum = 1 ;
//		for(int i = 1 ; i <= n ; i ++){
//			sum = sum * i ;
//		}
//		return sum ;
//	}

	//n * (n-1) * (n-1 -1) * .. * 1
	public static int fabric(int n){
		if(n == 1){
			return 1 ;
		}
		return n * fabric(n - 1);
	}
}
