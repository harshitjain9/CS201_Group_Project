package com.example.demo.KD;

import java.lang.System;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Business;

public class KdNodePartition {
		
		public final Business business;
		private KdNodePartition ltChild, gtChild;
		public KdNodePartition (final Business business) {
			this.business = business;
			ltChild = null;
			gtChild = null;
		}
		private static double superKeyCompare(final double[] a, final double[] b, final int p) {
			double diff = 0;
			for (int i = 0; i < a.length; i++) {
				
				final int r = (i + p < a.length) ? i + p : i + p - a.length;
				diff = a[r] - b[r];
				if (diff != 0) {
					break;
				}
			}
			return diff;
		}

		private static void mergeSort(final ArrayList<Business> reference, final ArrayList<Business> temporary,
		final int low, final int high, final int p) {

		int i, j, k;

		if (high > low) {
			final int mid = low + ( (high - low) >> 1 );
			mergeSort(reference, temporary, low, mid, p);
			mergeSort(reference, temporary, mid + 1, high, p);

			for (i = mid + 1; i > low; i--) {
				temporary.set(i-1, reference.get(i-1));
			}
			for (j = mid; j < high; j++) {
				temporary.set(mid + (high - j), reference.get(j+1));
			}
			for (k = low; k <= high; k++) {
				reference.set(k, (superKeyCompare(temporary.get(i).getCoordinates(), temporary.get(j).getCoordinates(), p) < 0) ? temporary.get(i++) : temporary.get(j--));
			}
		}
	}

		private static int removeDuplicates(final ArrayList<Business> reference, final int p) {
			int end = 0;
			for (int j = 1; j < reference.size(); j++) {
				double compare = superKeyCompare(reference.get(j).getCoordinates(), reference.get(j-1).getCoordinates(), p);
				if (compare < 0) {
					throw new RuntimeException( "merge sort failure: superKeyCompare(ref[" +
							Integer.toString(j) + "], ref[" + Integer.toString(j-1) +
							"], (" + Integer.toString(p) + ") = " + Double.toString(compare) );
				} else if (compare > 0) {
					reference.set(++end, reference.get(j));
				}
			}
			return end;
		}

		private static void swap(final ArrayList<Business> reference, final int i, final int j) {
			final Business t = reference.get(i);
			reference.set(i, reference.get(j));
			reference.set(j, t);
		}
		

		private static void insertionSort(final ArrayList<Business> reference, final int s, final int n, final int p) {
		    for (int i = s + 1; i < s + n; i++) {
				Business tmp = reference.get(i);
		        int j;
		        for (j = i; j > s && superKeyCompare(reference.get(j-1).getCoordinates(), tmp.getCoordinates(), p) > 0; j--) {
					reference.set(j, reference.get(j-1));
		        }
				reference.set(j, tmp);
		    }
		}

		private static int partition(final ArrayList<Business> reference, final int start, final int n, final int k,
				final ArrayList<Business> medians, final int first, final int p) {
			final int GROUP_SIZE = 5;
			if (n <=0 || n > reference.size()) {
				throw new IllegalArgumentException("n = " + n);
			}
			if (k <= 0 || k > n) {
				throw new IllegalArgumentException("k = " + k);
			}
			if (start + n > reference.size()) {
				throw new IllegalArgumentException("s = " + start + "  n = " + n + "  length = " + reference.size());
			}
		    if ( n == 1 && k == 1 ) {
		        return start;
		    }
		    int m = n / GROUP_SIZE;
		    int startOfGroup = 0;
		    for (int i = 0; i < m; i++) {
	            insertionSort(reference, start + startOfGroup, GROUP_SIZE, p);
				medians.set(first + i, reference.get(start + ( startOfGroup + ( (GROUP_SIZE - 1) >> 1 ) )));
		    	startOfGroup += GROUP_SIZE;
		    }
	    	int remainingElements = n - startOfGroup;
	    	if ( remainingElements < 0 || remainingElements >= GROUP_SIZE ) {
	    		throw new RuntimeException("incorrect group calculation");
	    	}
	        if ( remainingElements > 0 ) {
	            insertionSort(reference, start + startOfGroup, remainingElements, p);
				medians.set(first + m, reference.get(start + ( startOfGroup + ( (remainingElements) - 1 >> 1 ) )));
	        	m++;
	        }

		    final Business medianOfMedians =
					medians.get(partition(medians, first, m, (m + 1) >> 1, medians, first + m, p));
	
		    for (int i = 0; i < n; i++) {
		        if (reference.get(start + i) == medianOfMedians) {
		            swap(reference, start + i, start + n - 1);
		            break;
		        }
		    }

		    int lessThanIndex = 0;
		    for (int i = 0; i < n - 1; i++) {
		    	if ( superKeyCompare(reference.get(start + i).getCoordinates(), medianOfMedians.getCoordinates(), p) < 0 ) {
		    		if (i != lessThanIndex) {
		    			swap(reference, start + i, start + lessThanIndex);
		    		}
		    		lessThanIndex++;
		    	}
		    }

		    swap(reference, start + lessThanIndex, start + n - 1);

		    if (k < lessThanIndex + 1) {

		    	return partition(reference, start, lessThanIndex, k, medians, first, p);
		    	
		    } else if (k > lessThanIndex + 1) {

		    	return partition(reference, start + lessThanIndex + 1, n - lessThanIndex - 1,
		    			k - lessThanIndex - 1, medians, first, p);
		    	
		    } else {
		    	
		        
		    	
		    	
		    	return start + lessThanIndex;
		    }
		}
		
		private static KdNodePartition buildKdTree(final ArrayList<Business> reference, final ArrayList<Business> temporary,
				final int start, final int end, final int depth) {

			final KdNodePartition node;

			
			final int p = depth % reference.get(0).getCoordinates().length;

			if (end == start) {

				
				node = new KdNodePartition(reference.get(start));

			} else if (end == start + 1) {
				
				
				
				
				node = new KdNodePartition(reference.get(start));
				if (superKeyCompare(reference.get(start).getCoordinates(), reference.get(end).getCoordinates(), p) > 0) {
					node.ltChild = new KdNodePartition(reference.get(end));
				} else {
					node.gtChild = new KdNodePartition(reference.get(end));
				}
				
			} else if (end == start + 2) {
				
				
				
				
				insertionSort(reference, start, end - start + 1, p);
				node = new KdNodePartition(reference.get(start + 1));
				node.ltChild = new KdNodePartition(reference.get(start));
				node.gtChild = new KdNodePartition(reference.get(end));
				
			} else if (end > start + 2) {
				
				
				
				
				final int n = end - start + 1;
				final int k = (n + 1) >> 1;
				final int median = partition(reference, start, n, k, temporary, start, p);
				node = new KdNodePartition(reference.get(median));

				
				node.ltChild = buildKdTree(reference, temporary, start, median - 1, depth + 1);

				
				node.gtChild = buildKdTree(reference, temporary, median + 1, end, depth + 1);
				
			} else 	if (end < start) {
				
				
				throw new RuntimeException("end < start");
				
			} else {
				
				
				throw new RuntimeException("unknown configuration of  start and end");
			}
			
			return node;
		}

		private int verifyKdTree(final int depth) {

			if (business == null) {
				throw new RuntimeException("business is null");
			}

			
			final int p = depth % business.getCoordinates().length;

			
			int count = 1 ;

			if (ltChild != null) {
				if (ltChild.business.getCoordinates()[p] > business.getCoordinates()[p]) {
					throw new RuntimeException("node is > partition!");
				}
				if (superKeyCompare(ltChild.business.getCoordinates(), business.getCoordinates(), p) >= 0) {
					throw new RuntimeException("node is >= partition!");
				}
				count += ltChild.verifyKdTree(depth + 1);
			}
			if (gtChild != null) {
				if (gtChild.business.getCoordinates()[p] < business.getCoordinates()[p]) {
					throw new RuntimeException("node is < partition!");
				}
				if (superKeyCompare(gtChild.business.getCoordinates(), business.getCoordinates(), p) <= 0) {
					throw new RuntimeException("node is <= partition!");
				}
				count += gtChild.verifyKdTree(depth + 1);
			}
			return count;
		}

		public static KdNodePartition createKdTree(ArrayList<Business> businesses) {
			
			
			long initTime = System.currentTimeMillis();
			final ArrayList<Business> reference = new ArrayList<Business>(businesses.size());
			int referenceCapacity = businesses.size();
			for (int j = 0; j < referenceCapacity; j++) {
				reference.add(new Business());
				reference.set(j, businesses.get(j));
            }
			initTime = System.currentTimeMillis() - initTime;
			
			
			long sortTime = System.currentTimeMillis();
			final ArrayList<Business> temporary = new ArrayList<Business>(businesses.size());
			for (int i = 0; i< businesses.size();i++) {
				temporary.add(new Business());
			}
			mergeSort(reference, temporary, 0, businesses.size() - 1, 0);
			sortTime = System.currentTimeMillis() - sortTime;
			
			
			long removeTime = System.currentTimeMillis();
			final int end = removeDuplicates(reference, 0);
			removeTime = System.currentTimeMillis() - removeTime;
			
			
			long kdTime = System.currentTimeMillis();
			final KdNodePartition root = buildKdTree(reference, temporary, 0, end, 0);
			kdTime = System.currentTimeMillis() - kdTime;
			
			
			long verifyTime = System.currentTimeMillis();
			System.out.println( "\nNumber of nodes = " + root.verifyKdTree(0) );
			verifyTime = System.currentTimeMillis() - verifyTime;
			
			final double iT = (double) initTime / 1000.;
			final double sT = (double) sortTime / 1000.;
			final double rT = (double) removeTime / 1000.;
			final double kT = (double) kdTime / 1000.;
			final double vT = (double) verifyTime / 1000.;
			System.out.printf("\ntotalTime = %.2f  initTime = %.2f  sortTime = %.2f"
					+ "  removeTime = %.2f  kdTime = %.2f  verifyTime = %.2f\n\n",
						iT + sT + rT + kT + vT, iT, sT, rT, kT, vT);
			
			
			return root;
		}

		public List<KdNodePartition> searchKdTree(final double[] query, final double cut, final int depth) {
			final int p = depth % business.getCoordinates().length;
			
			List<KdNodePartition> result = new ArrayList<KdNodePartition>();
			boolean inside = true;
			for (int i = 0; i < business.getCoordinates().length; i++) {
				if (Math.abs(query[i] - business.getCoordinates()[i]) > cut) {
					inside = false;
					break;
				}
			}
			if (inside) {
				result.add(this);
			}

			if (ltChild != null) {
				if ( (query[p] - cut) <= business.getCoordinates()[p] ) {
					result.addAll( ltChild.searchKdTree(query, cut, depth + 1) );
				}
			}

			if (gtChild != null) {
				if ( (query[p] + cut) >= business.getCoordinates()[p] ) {
					result.addAll( gtChild.searchKdTree(query, cut, depth + 1) );
				}
			}
			return result;
		}

		public void printKdTree(final int depth) {
			if (gtChild != null) {
				gtChild.printKdTree(depth+1);
			}
			for (int i = 0; i < depth; i++) {
				System.out.print("         ");
			}
			printTuple(business.getCoordinates());
			System.out.println();
			if (ltChild != null) {
				ltChild.printKdTree(depth+1);
			}
		}

		public static void printTuple(final double[] p) {
			System.out.print("(");
			for (int i = 0; i < p.length; i++) {
				System.out.print(p[i]);
				if (i < p.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.print(")");
		}
	}

	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
		
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
