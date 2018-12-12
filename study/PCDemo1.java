class PCDemo1 {    //生产消费关系 productor consumer
	public static void main(String[] args) 	{
		//创建集合  list
		java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		//向集合中添加元素(对象)
		list.add(new Integer(1));
		list.add(new Integer(2));
		list.add(new Integer(3));
		list.add(new Integer(4));
		System.out.println(list.size());    //4
		System.out.println(list.isEmpty());  
		System.out.println(list.remove(0));   //取第一个元素  1
		System.out.println(list.remove(0));  //2
		System.out.println(list.remove(0));  //3
		System.out.println(list.remove(0));  //4
		System.out.println(list.isEmpty());  
		//System.out.println(list.remove(0));



	}
}