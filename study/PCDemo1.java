class PCDemo1 {
	public static void main(String[] args) 	{
		//��������
		java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		//�򼯺������Ԫ��(����)
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