package digitallibrary.structures;
public final class SinglyLinkedList<T> {
    private static final class Node<T>{T v;Node<T> n;Node(T v){this.v=v;}}
    private Node<T> head,tail; private int size;
    public int size(){return size;} public boolean isEmpty(){return size==0;}
    public void add(T v){Node<T>x=new Node<>(v);if(head==null)head=tail=x;else{tail.n=x;tail=x;}size++;}
    public void addFirst(T v){Node<T>x=new Node<>(v);x.n=head;head=x;if(tail==null)tail=x;size++;}
    public T get(int idx){if(idx<0||idx>=size)throw new IndexOutOfBoundsException(idx);Node<T>c=head;for(int i=0;i<idx;i++)c=c.n;return c.v;}
    public boolean remove(T v){Node<T>p=null,c=head;while(c!=null){if(v==null?c.v==null:v.equals(c.v)){if(p==null)head=c.n;else p.n=c.n;if(c==tail)tail=p;size--;return true;}p=c;c=c.n;}return false;}
}
