package business.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;





public class TwoKeyHashMap<K,L,V> implements Cloneable {
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
	private HashMap<K,V> firstKeyMap = new HashMap<K,V>();
	private HashMap<L,V> secondKeyMap= new HashMap<L,V>();
	private HashMap<K,L> firstToSecondMap= new HashMap<K,L>();
	private HashMap<L,K> secondToFirstMap= new HashMap<L,K>();
	
	public void put(K firstKey,L secondKey, V value) {
		firstKeyMap.put(firstKey,value);
		secondKeyMap.put(secondKey,value);
		firstToSecondMap.put(firstKey,secondKey);
		secondToFirstMap.put(secondKey,firstKey);
	}
	public boolean isAFirstKey(K aFirstKey){
		return firstKeyMap.containsKey(aFirstKey);
	}
	public boolean isASecondKey(L aSecondKey){
		return secondKeyMap.containsKey(aSecondKey);
	}
	public Set<K> firstKeys() {
		return firstKeyMap.keySet();
	}
	public Set<L> secondKeys() {
		return secondKeyMap.keySet();
	}
	public Collection<V> values() {
		return firstKeyMap.values();
	}
	
	public Set<Map.Entry<K,L>> firstSecondKeys() {
		return firstToSecondMap.entrySet();
	}
	
	public V getValWithFirstKey(K firstKey) {
		return firstKeyMap.get(firstKey);
	}
	public V getValWithSecondKey(L secondKey){
		return secondKeyMap.get(secondKey);
	}
	public K getFirstKey(L secondKey){
		return secondToFirstMap.get(secondKey);
	}
	public L getSecondKey(K firstKey){
		return firstToSecondMap.get(firstKey);
	}
	@SuppressWarnings("unchecked")
	public TwoKeyHashMap<K,L,V> clone() {
		try {
			TwoKeyHashMap<K,L,V> map = 
				(TwoKeyHashMap<K,L,V>)super.clone();
			map.firstKeyMap = (HashMap<K,V>)firstKeyMap.clone();
			map.secondKeyMap = (HashMap<L,V>)secondKeyMap.clone();
			map.firstToSecondMap = (HashMap<K,L>)firstToSecondMap.clone();
			map.secondToFirstMap = (HashMap<L,K>)secondToFirstMap.clone();
			return map;
		}
		catch(CloneNotSupportedException e){
			log.severe("Can't clone TwoKeyHashMap");
			e.printStackTrace();
			return null;
		}
	}
	public String toString(){
		StringBuilder sb = new StringBuilder("[ ");
		Iterator<Map.Entry<K,L>> it1 = firstSecondKeys().iterator();
		while(it1.hasNext()){
			Map.Entry<K,L> me = it1.next();
			sb.append("(");
			sb.append(me.getKey().toString());
			sb.append(",");
			sb.append(me.getValue().toString());
			sb.append(",");
			sb.append(getValWithFirstKey(me.getKey()).toString());
			sb.append(") ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * This shows how to use TwoKeyHashMap
	 * 
	 * The intended use is to facilitate looking
	 * up products (or other things) both by
	 * name and by id
	 */
	/*
	public static void main(String[] args) {
		TwoKeyHashMap<String, String, String> tk = new TwoKeyHashMap<String, String, String>();
		tk.put("hello","goodbye","again");
		tk.put("1", "2", "3");
		System.out.println(tk);
		
		//load up some Products, as if from the db
		//Catalog c, String name, 
		//LocalDate date, int numAvail, double price
		Product prod1 = ProductSubsystemFacade.createProduct(DefaultData.CLOTHES_CATALOG, "Shirt", GuiUtils.localDateForString("11/11/2001"), 5, 20.00);
		//assume the product id is 1
		
		Product prod2 = ProductSubsystemFacade.createProduct(DefaultData.CLOTHES_CATALOG, "Hat", GuiUtils.localDateForString("1/1/2001"), 10, 12.00);
		//assume the product id is 2
		
		//now insert into a TwoKeyHashMap for later use
		TwoKeyHashMap<Integer, String, Product> twokey = new TwoKeyHashMap<Integer, String, Product> ();
		twokey.put(1,"Shirt", prod1);
		twokey.put(2, "Hat", prod2);
		
		//now later on, maybe you need to look up the object whose name is shirt
		//do this in the following way:
		twokey.getValWithSecondKey("Shirt");
		
		//at another time, maybe you need to look up the object whose id is 2
		//do this in the following way:
		twokey.getValWithFirstKey(2);
		
		
	
	}	*/

	//should implement several "remove" operations too
	
	private static final long serialVersionUID = 3832619603607434039L;
}
