class ArrayDemo1 {
	public static void main(String[] args){
		//1.�������鷽ʽһ
		int[] arr = new int[3];
		arr[0] = 1 ;
		arr[1] = 2 ;
		arr[2] = 3 ;
		
		//2.�������鷽ʽ��
		int[] arr2 = new int[]{4,5,6};

		//3.�������鷽ʽ��
		int[] arr3 ={7,8,9};
		int arr4[] = {10,11,12};
		
		//��ָ���쳣
		int[] arr5 = null ;
		//System.out.println(arr5[2]);

		int[] arr6 = {-1,-2,-3,-4,-5,-6};
		int[] arr7 = null ;
		System.out.println(getMax(arr7));

		int x = 1 ; 
		int y = 2 ;
		int temp = 0 ;
		temp = x ;
		x = y ;
		y = temp ;
		System.out.println("x=" + x);
		System.out.println("y=" + y);
	}
	//��ȡ���������ֵ
	public static int getMax(int[] arr){
		//������Ч���ж�
		if(arr != null && arr.length != 0){
			int temp = arr[0] ;
			for(int i = 0 ; i < arr.length ; i ++){
				if(temp < arr[i]){
					temp = arr[i] ;
				}
			}
			return temp ;
		}
		System.out.println("������Ч");
		return -1 ;
	}
}
