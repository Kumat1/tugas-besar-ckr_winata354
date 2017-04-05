package TerlaluSayang;

import java.lang.reflect.Array;

 /* Created by Ari on 4/4/2017.
 */

public class MyLovelyQueue<A> {
    public A[] array;
    public int x,y;

    public MyLovelyQueue(Class<?> classname){
        array =(A[]) Array.newInstance(classname,10);
        x = -1;
        y = 0;
    }

    public static void main(String[]args){
        MyLovelyQueue<Integer> o = new MyLovelyQueue<Integer>(Integer.class);
        o.enqueue(12);
        o.enqueue(5);
        o.enqueue(17);
        o.enqueue(1);
        System.out.println(o.dequeue());
    }

    public void enqueue(A item){
        array[++x]=item;
    }

    public A dequeue(){
        return array[y++];
    }
}

