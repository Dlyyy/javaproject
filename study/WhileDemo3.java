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

//��1001��ʼ���ҵ���һ���ܹ���2,3,5,7ͬʱ�����������ɡ�