package src.graphalg;

public class Heapsort {
	Object[] heap;
	int[] key;
	int end;
	
	public Heapsort(int l) {
		heap =new Object[l];
		key = new int[l];
		end = 0;
	}
	
	public void insert(Object o,int k) {
		heap[end] = o;
		key[end] = k;
		int parent = (end-1)/2;
		int child = end;
		while(parent>=0) {
			if(key[parent] > k) {
				key[child] = key[parent];
				key[parent] = k;
				heap[child] = heap[parent];
				heap[parent] = o;
				child = parent;
				parent = (parent-1)/2;
			} else {
				break;
			}
		}
		end++;
	}
	
	public void sort() {
		while(end>0) {
			Object o = heap[end-1];
			heap[end-1] = heap[0];
			int k = key[end-1];
			key[end-1] = key[0];
			heap[0] = o;
			key[0] = k;
			end--;
			int parent = 0;
			int lchild = parent*2+1, rchild = parent*2+2;
			while(lchild<end) {
				if(rchild<end&&key[rchild]<key[lchild]) {
					lchild = rchild;
				}
				if(key[lchild]<key[parent]) {
					key[parent] = key[lchild];
					key[lchild] = k;
					heap[parent] = heap[lchild];
					heap[lchild] = o;
					parent = lchild;
					lchild = parent*2+1;
					rchild = parent*2+2;
				} else {
					break;
				}
			}
		}
	}
	
	public static void main(String[] arg) {
		Heapsort h = new Heapsort(10);
		int[] d = {55,10,20,3,0,100,55,66,77,88};
		for(int i = 0; i < 10; i++) {
			h.insert(new Edge(d,d,d[i]),d[i]);
		}
		h.sort();
		for(int i = 0; i < 10; i++) {
			Edge e = (Edge) h.heap[i];
			System.out.println(e.weight);
		}
	}
}
