class PCDemo1 {    //�������ѹ�ϵ productor consumer
	public static void main(String[] args) 	{
		//��������  list
		java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		//�򼯺������Ԫ��(����)
		list.add(new Integer(1));
		list.add(new Integer(2));
		list.add(new Integer(3));
		list.add(new Integer(4));
		System.out.println(list.size());    //4
		System.out.println(list.isEmpty());  
		System.out.println(list.remove(0));   //ȡ��һ��Ԫ��  1
		System.out.println(list.remove(0));  //2
		System.out.println(list.remove(0));  //3
		System.out.println(list.remove(0));  //4
		System.out.println(list.isEmpty());  
		//System.out.println(list.remove(0));



	}
}