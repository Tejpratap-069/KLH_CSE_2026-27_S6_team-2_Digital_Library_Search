package digitallibrary.structures;
public final class StringHashTable<V>{
    private static final class Entry<V>{String k;V v;Entry<V>n;Entry(String k,V v,Entry<V>n){this.k=k;this.v=v;this.n=n;}}
    private Entry<V>[] buckets; private int size;
    @SuppressWarnings("unchecked") public StringHashTable(int capacity){buckets=(Entry<V>[])new Entry[Math.max(4,capacity)];}
    public StringHashTable(){this(1024);} public int size(){return size;}
    private int idx(String k){int h=0x811c9dc5;for(int i=0;i<k.length();i++){h^=k.charAt(i);h*=0x01000193;}return (h&0x7fffffff)%buckets.length;}
    public void put(String k,V v){if(k==null)throw new IllegalArgumentException("null key");if((size+1)*4>buckets.length*3)rehash();int i=idx(k);for(Entry<V>e=buckets[i];e!=null;e=e.n)if(e.k.equals(k)){e.v=v;return;}buckets[i]=new Entry<>(k,v,buckets[i]);size++;}
    public V get(String k){if(k==null)return null;int i=idx(k);for(Entry<V>e=buckets[i];e!=null;e=e.n)if(e.k.equals(k))return e.v;return null;}
    public boolean containsKey(String k){return get(k)!=null;}
    public V remove(String k){if(k==null)return null;int i=idx(k);Entry<V>p=null,c=buckets[i];while(c!=null){if(c.k.equals(k)){if(p==null)buckets[i]=c.n;else p.n=c.n;size--;return c.v;}p=c;c=c.n;}return null;}
    @SuppressWarnings("unchecked") private void rehash(){Entry<V>[]old=buckets;buckets=(Entry<V>[])new Entry[old.length*2+1];size=0;for(Entry<V>e:old)for(;e!=null;e=e.n)put(e.k,e.v);}
}
