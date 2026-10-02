package digitallibrary.structures;
import java.util.NoSuchElementException;
public final class ArrayStack<T>{private final DynamicArray<T>a=new DynamicArray<>();public void push(T v){a.add(v);}public T pop(){if(a.isEmpty())throw new NoSuchElementException();return a.removeAt(a.size()-1);}public T peek(){if(a.isEmpty())throw new NoSuchElementException();return a.get(a.size()-1);}public int size(){return a.size();}public boolean isEmpty(){return a.isEmpty();}}
