class ArrayDemo1 {
	public static void main(String[] args){
		//1.创建数组方式一
		int[] arr = new int[3];
		arr[0] = 1 ;
		arr[1] = 2 ;
		arr[2] = 3 ;
		
		//2.创建数组方式二
		int[] arr2 = new int[]{4,5,6};

		//3.创建数组方式三
		int[] arr3 ={7,8,9};
		int arr4[] = {10,11,12};
		
		//空指针异常
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
	//提取数组中最大值
	public static int getMax(int[] arr){
		//数组有效性判断
		if(arr != null && arr.length != 0){
			int temp = arr[0] ;
			for(int i = 0 ; i < arr.length ; i ++){
				if(temp < arr[i]){
					temp = arr[i] ;
				}
			}
			return temp ;
		}
		System.out.println("数组无效");
		return -1 ;
	}
}
