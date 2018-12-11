class WhileDemo3{
	public static void main(String[] args) 	{
		int i = 200 ;
		while(true){
			i ++ ;
			if(i % 5 == 0 && i < 100){
				continue  ;
			}
			System.out.println(i);
		}

	}
}

//从1001开始，找到第一个能够被2,3,5,7同时整除的数即可。