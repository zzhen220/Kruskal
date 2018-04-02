/* HashTableChained.java */

package src.dict;

import src.list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained {
	
  private static double loadfactor = 0.75;
  protected int entrySize;
  protected int bucketSize;
  protected List[] buckets;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    boolean isPrime = true;
    if(sizeEstimate > 2) {
    	bucketSize = (int) (sizeEstimate/loadfactor);
    }
    do {
    	isPrime = true;
    	bucketSize ++;
    	for(int i = 2; i < bucketSize/2; i++) {
    		if(bucketSize % i == 0) {
    			isPrime = false;
    			break;
    		}
    	}
    } while(!isPrime);
    buckets = new SList[bucketSize];
    entrySize = 0;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    bucketSize = 103;
    buckets = new SList[bucketSize];
    entrySize = 0;
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
	int output = code % bucketSize;
	if(output < 0) {
		output += bucketSize;
	}
    return output;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return this.entrySize;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    return entrySize==0;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
	int num = compFunction(key.hashCode());
	Entry newEntry = new Entry();
	newEntry.key = key;
	newEntry.value = value;
    this.buckets[num].insertBack(newEntry);
    this.entrySize++;
    if(this.entrySize>=this.bucketSize) {
    	this.resize();
    }
    return newEntry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
	Entry output = null;
	boolean find = false;
    int num = compFunction(key.hashCode());
    if(!buckets[num].isEmpty()) {
    	ListNode cuNode = buckets[num].front();
    	while(cuNode.isValidNode()) {
    		try {
				output = (Entry) cuNode.item();
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(output.key.equals(key)) {
    			find = true;
    			break;
    		}
    		try {
				cuNode = cuNode.next();
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    if(find) {
    	return output;
    } else {
    	return null;
    }
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
	  	Entry output = null;
		boolean find = false;
	    int num = compFunction(key.hashCode());
	    if(!buckets[num].isEmpty()) {
	    	ListNode cuNode = buckets[num].front();
	    	while(cuNode.isValidNode()) {
	    		try {
					output = (Entry) cuNode.item();
					if(output.key.equals(key)) {
						find = true;
						cuNode.remove();
						entrySize--;
						break;
					}
					cuNode = cuNode.next();
				} catch (InvalidNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	    if(find) {
	    	return output;
	    } else {
	    	return null;
	    }
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    for(int i = 0; i < bucketSize; i++) {
    	buckets[i] = new SList();
    }
    entrySize = 0;
  }

  public void resize() {
	  List[] tempBuckets = this.buckets;
	  this.buckets = new HashTableChained(bucketSize).buckets;
	  this.bucketSize = this.buckets.length;
	  this.makeEmpty();
	  for(int i=0;i < tempBuckets.length; i++) {
		  if(!tempBuckets[i].isEmpty()) {
		    	ListNode cuNode = tempBuckets[i].front();
		    	Entry cuEntry;
		    	while(cuNode.isValidNode()) {
		    		try {
						cuEntry = (Entry) cuNode.item();
						this.insert(cuEntry.key, cuEntry.value);
					} catch (InvalidNodeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		try {
						cuNode = cuNode.next();
					} catch (InvalidNodeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
	  }
  }
  
  public static void main(String args[]) {
	  HashTableChained h = new HashTableChained(20);
	  h.makeEmpty();
	  for(int i = 0; i < 30; i++) {
		  Integer ii = new Integer(i);
		  h.insert(ii, ii);
	  }
	  System.out.println(h.bucketSize);
  }
}
