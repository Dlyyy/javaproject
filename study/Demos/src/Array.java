import java.util.Arrays;

public class Array {
    public static void main(String[] args) {
        int[] a; //声明一个数组
        a = new int[5]; //创建一个长度是5的数组，并且使用引用a指向该数组 []表示该变量是一个数组  int 表示数组里的每一个元素都是一个整数
        int[] b = new int[5];//声明的同时，指向一个数组

        a[0]= 1;  //下标0，代表数组里的第一个数
        a[1]= 2;
        a[2]= 3;
        a[3]= 4;
        a[4]= 5;
        System.out.println(a);//打印出来的是数组的首地址
        System.out.println(a[0]);
        System.out.println(Arrays.toString(a));//打印数组
        System.out.println(a.length);

        //写法一： 分配空间同时赋值
        int[] a1 = new int[]{100,102,444,836,3236};

        //写法二： 省略了new int[],效果一样
        int[] b1 = {100,102,444,836,3236};

        //写法三：同时分配空间，和指定内容
        //在这个例子里，长度是3，内容是5个，产生矛盾了
        //所以如果指定了数组的内容，就不能同时设置数组的长度
        //int[] c1 = new int[3]{100,102,444,836,3236};


        //初始化二维数组，
        int[][] a11 = new int[2][3]; //有两个一维数组，每个一维数组的长度是3
        a11[1][2] = 5;  //可以直接访问一维数组，因为已经分配了空间

        //只分配了二维数组
        int[][] b11 = new int[2][]; //有两个一维数组，每个一维数组的长度暂未分配
        b11[0]  =new int[3]; //必须事先分配长度，才可以访问
        b11[0][2] = 5;

        //指定内容的同时，分配空间
        int[][] c11 = new int[][]{
                {1,2,4},
                {4,5},
                {6,7,8,9}
        };

        //数组复制
        int a2[] = new int[] { 18, 62, 68, 82, 65, 9 };
        // copyOfRange(int[] original, int from, int to)
        // 第一个参数表示源数组
        // 第二个参数表示开始位置(取得到)
        // 第三个参数表示结束位置(取不到)
        int[] b2 = Arrays.copyOfRange(a, 0, 3);

        for (int i = 0; i < b2.length; i++) {
            System.out.print(b2[i] + " ");
        }

        //排序
        int a3[] = new int[] { 18, 62, 68, 82, 65, 9 };
        System.out.println("排序之前 :");
        System.out.println(Arrays.toString(a3));
        Arrays.sort(a3);
        System.out.println("排序之后:");
        System.out.println(Arrays.toString(a3));

        //搜索 查询元素出现的位置
        int a4[] = new int[] { 18, 62, 68, 82, 65, 9 };

        Arrays.sort(a4);

        System.out.println(Arrays.toString(a4));
        //使用binarySearch之前，必须先使用sort进行排序
        System.out.println("数字 62出现的位置:"+Arrays.binarySearch(a4, 62));

        //判断是否相同
        int a5[] = new int[] { 18, 62, 68, 82, 65, 9 };
        int b5[] = new int[] { 18, 62, 68, 82, 65, 8 };

        System.out.println(Arrays.equals(a5, b5));

        //填充  使用同一个值
        int a6[] = new int[10];

        Arrays.fill(a6 , 5);

        System.out.println(Arrays.toString(a6));
    }
}
