class FunctionDemo1 {
	public static void main(String[] args) {
		//调用函数
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

	//定义函数
	public static int add(int a,int b){
		return a + b ;
	}

	/**
	 *打印空心三角形函数
	 */
	public static void printEmptyTriangle(int line){
		//循环所有行
		for(int i = 0 ; i < line ; i ++){
			//1.输出n个空格
			for(int j = 0 ; j < line - 1 - i ; j ++){
				System.out.print(" ");
			}
			//2.输出一个*
			System.out.print("*");
			//3.处理最后一行,输出2n个*
			if(i == (line - 1)){
				for(int j = 0 ; j < 2 * i ; j ++){
					System.out.print("*") ;
				}
			}
			//中间行
			if(i != 0 && i != (line - 1)){
				//输出2n - 1个空格
				for(int j = 0 ; j < 2 * i - 1 ; j ++){
					System.out.print(" ");
				}
				System.out.print("*");//最后的*
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

	//阶乘,1 x 2 x 3 x 4 x ...x n
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
