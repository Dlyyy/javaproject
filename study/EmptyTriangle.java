class EmptyTriangle{
	public static void main(String[] arg){
		int line = 20 ;
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
}