class BubbleDemo1 {
	public static void main(String[] args) 	{
		int[] arr = {9,8,7,5,6} ;
		bubble(arr);
		for(int i = 0 ; i < arr.length ; i ++){
			System.out.print(arr[i] + " ");
		}
	}

	//Ã°ÅÝÅÅÐò
	public static void bubble(int[] arr){
		//Ñ­»·ÂÖ´Î
		int tmp = 0 ;
		for(int i = 0 ; i < arr.length - 1 ; i ++){
			for(int j = 0 ; j <  arr.length - 1 - i ; j ++){
				tmp = arr[j];
				if(arr[j] > arr[j + 1]){
					arr[j] = arr[j + 1];
					arr[j + 1] = tmp ;
				}
			}
		}
	}
}
