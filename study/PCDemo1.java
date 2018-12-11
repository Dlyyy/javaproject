class PCDemo1 {
	public static void main(String[] args) 	{
		//创建集合
		java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		//向集合中添加元素(对象)
		list.add(new Integer(1));
		list.add(new Integer(2));
		list.add(new Integer(3));
		list.add(new Integer(4));
		System.out.println(list.size());
		System.out.println(list.isEmpty());
		System.out.println(list.remove(0));
		System.out.println(list.remove(0));
		System.out.println(list.remove(0));
		System.out.println(list.remove(0));
		System.out.println(list.isEmpty());
		//System.out.println(list.remove(0));



	}
}