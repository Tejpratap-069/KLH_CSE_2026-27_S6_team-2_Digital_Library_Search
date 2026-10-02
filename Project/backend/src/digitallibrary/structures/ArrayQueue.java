package digitallibrary.structures;
import java.util.NoSuchElementException;
public final class ArrayQueue<T>{private Object[]a=new Object[16];private int h,t,s;public int size(){return s;}public boolean isEmpty(){return s==0;}public void offer(T v){if(s==a.length)grow();a[t]=v;t=(t+1)%a.length;s++;}@SuppressWarnings("unchecked")public T poll(){if(s==0)throw new NoSuchElementException();T v=(T)a[h];a[h]=null;h=(h+1)%a.length;s--;return v;}private void grow(){Object[]n=new Object[a.length*2];for(int i=0;i<s;i++)n[i]=a[(h+i)%a.length];a=n;h=0;t=s;}}
