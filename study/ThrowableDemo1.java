class ThrowableDemo1 {
	public static void main(String[] args) {
		//float r = divide(4,0);
		//System.out.println(r);
		int[] arr = new int[4] ;
		int l = getLength(arr) ;
		System.out.println(l);
	}

	public static float divide(int a , int b){
		return (float)a / b ;
	}

	public static int getLength(int[] arr){
		int len = -1;
		try{
			len = arr.length ;
			return -1 ;
		}
		catch(Exception e){
			System.out.println("�����ˣ�" + e.getMessage());
			return -2;
		}
		finally{
			System.out.println("����ִ�����ˣ�");
			len = len + 1 ;
			System.out.println("out of finally " + len);
			return -3 ;
		}
		//return len ;
	}
	//try-catch-finally
	public static int getLength2(int[] arr){
		int len = -1;
		try{
			len = arr.length ;
		}
		catch(Exception e){
			System.out.println("�����ˣ�" + e.getMessage());
		}
		finally{
			System.out.println("����ִ�����ˣ�");
		}
		return len ;
	}
}
