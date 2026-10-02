package digitallibrary.structures;
import java.util.Iterator;
import java.util.NoSuchElementException;
public final class DynamicArray<T> implements Iterable<T> {
    private Object[] data; private int size;
    public DynamicArray(){this(16);} public DynamicArray(int cap){data=new Object[Math.max(1,cap)];}
    public int size(){return size;} public boolean isEmpty(){return size==0;}
    public void add(T v){ensure(size+1);data[size++]=v;}
    public void add(int index,T v){checkInsert(index);ensure(size+1);System.arraycopy(data,index,data,index+1,size-index);data[index]=v;size++;}
    @SuppressWarnings("unchecked") public T get(int i){check(i);return (T)data[i];}
    public void set(int i,T v){check(i);data[i]=v;}
    @SuppressWarnings("unchecked") public T removeAt(int i){check(i);T old=(T)data[i];int moved=size-i-1;if(moved>0)System.arraycopy(data,i+1,data,i,moved);data[--size]=null;return old;}
    public void clear(){for(int i=0;i<size;i++)data[i]=null;size=0;}
    private void ensure(int need){if(need<=data.length)return;int n=data.length*2;while(n<need)n*=2;Object[] x=new Object[n];System.arraycopy(data,0,x,0,size);data=x;}
    private void check(int i){if(i<0||i>=size)throw new IndexOutOfBoundsException(i);} private void checkInsert(int i){if(i<0||i>size)throw new IndexOutOfBoundsException(i);}
    public Iterator<T> iterator(){return new Iterator<>(){int i;public boolean hasNext(){return i<size;}public T next(){if(!hasNext())throw new NoSuchElementException();return get(i++);}};}
}
