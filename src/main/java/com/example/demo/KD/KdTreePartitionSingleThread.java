/*
 * Copyright (c) 2015, Russell A. Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSEARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/* @(#)KdTreePartitionSingleThread.java	1.14 04/25/15 */

import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.Business;


/**
 * <p>
 * The k-d tree is described by Jon Bentley in "Multidimensional Binary Search Trees Used
 * for Associative Searching", CACM 18(9): 509-517, 1975.  One approach to building a
 * balanced k-d tree involves finding the median of the data at each level of recursive
 * subdivision of those data.  A O(n) algorithm that may be used to find the median and
 * partition data about that median is described by Manuel Blum, Robert W. Floyd, Vaughan
 * Pratt, Ronald L. Rivest and Robert E. Tarjan in "Time Bounds for Selection", Journal of
 * Computer and System Sciences 7: 448-461, 1973 as well as by Thomas H. Cormen, Charles E.
 * Leiserson, Ronald L. Rivest and Clifford Stein in Section 9.3, "Selection in worst-case
 * linear time", pages 220-222 in "Introduction to Algorithms, Third Edition", MIT Press,
 * Cambridge, Massachusetts, 2009.
 * </p>
 *
 * @author Russell A. Brown
 */
class KdTreePartitionSingleThread {

	/**
	 * <p>
	 * The {@code KdNode} class stores a point of any number of dimensions
	 * as well as references to the "less than" and "greater than" sub-trees.
	 * </p>
	 */
	static class KdNodePresort {
		
		// private final double[] point;
	public final Business business;
	private KdNodePresort ltChild, gtChild;
	/**
	 * <p>
	 * The {@code KdNode} constructor returns one {@code KdNode}.
	 * </p>
	 * 
	 * @param point - the multi-dimensional point to store in the {@code KdNode}
	 */
	public KdNodePresort (final Business business) {
		this.business = business;
		ltChild = null;
		gtChild = null;
	}
	
	/**
	 * <p>
	 * The {@code initializeReference} method initializes one reference array.
	 * </p>
	 * 
	 * @param coordinates - an array of (x,y,z,w...) coordinates
	 * @param reference - an array of references to the (x,y,z,w...) coordinates
	 * @param i - the index of the most significant coordinate in the super key
	 */
	private static void initializeReference(final double[][] coordinates,
			final double[][] reference, final int i) {
		for (int j = 0; j < reference.length; j++) {
			reference[j] = coordinates[j];
		}
	}

	/**
	 * <p>
	 * The {@code superKeyCompare} method compares two int[] in as few coordinates as possible
	 * and uses the sorting or partition coordinate as the most significant coordinate.
	 * </p>
	 * 
	 * @param a - a double[]
	 * @param b - a double[]
	 * @param p - the most significant dimension
	 * @returns an int that represents the result of comparing two super keys
	 */
	private static int superKeyCompare(final double[] a, final double[] b, final int p) {
		for (int i = 0; i < a.length; i++) {
			// A fast alternative to the modulus operator for (i + p) < 2 * a.length.
			final int r = (i + p < a.length) ? i + p : i + p - a.length;
			if (a[r] > b[r]) {
				return 1;
			}
			if (a[r] < b[r]) {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * <p>
	 * The {@code mergeSort} function recursively subdivides the array to be sorted then merges the elements.
	 * Adapted from Robert Sedgewick's "Algorithms in C++" p. 166. Addison-Wesley, Reading, MA, 1992.
	 * </p>
	 * 
	 * @param reference - an array of references to the (x,y,z,w...) coordinates
	 * @param temporary - a scratch array into which to copy references;
	 * this array must be as large as the reference array
	 * @param low - the start index of the region of reference to sort
	 * @param high - the high index of the region of reference to sort
	 * @param p - the sorting partition (x, y, z, w...)
	 */
	private static void mergeSort(final double[][] reference, final double[][] temporary,
			final int low, final int high, final int p) {

		int i, j, k;

		if (high > low) {

			// Avoid overflow when calculating the median address.
			final int mid = low + ( (high - low) >> 1 );

			// Recursively subdivide the lower half of the array.
			mergeSort(reference, temporary, low, mid, p);

			// Recursively subdivide the upper half of the array.
			mergeSort(reference, temporary, mid + 1, high, p);


			// Merge the results of this level of subdivision.
			for (i = mid + 1; i > low; i--) {
				temporary[i - 1] = reference[i - 1];
			}
			for (j = mid; j < high; j++) {
				temporary[mid + (high - j)] = reference[j + 1]; // Avoid address overflow.
			}
			for (k = low; k <= high; k++) {
				reference[k] =
					(superKeyCompare(temporary[i], temporary[j], p) < 0) ? temporary[i++] : temporary[j--];
			}
		}
	}

	/*
	 * <p>
	 * The {@code removeDuplicates} method} checks the validity of the merge sort
	 * and removes from the reference array all but one of a set of references that
	 * reference duplicate tuples.
	 * </p>
	 * 
	 * @param reference - an array of references to the (x,y,z,w...) coordinates
	 * @param p - the index of the most significant coordinate in the super key
	 * @returns the address of the last element of the references array following duplicate removal
	 */
	private static int removeDuplicates(final double[][] reference, final int p) {
		int end = 0;
		for (int j = 1; j < reference.length; j++) {
			double compare = superKeyCompare(reference[j], reference[j-1], p);
			if (compare < 0) {
				throw new RuntimeException( "merge sort failure: superKeyCompare(ref[" +
						Integer.toString(j) + "], ref[" + Integer.toString(j-1) +
						"], (" + Integer.toString(p) + ") = " + Double.toString(compare) );
			} else if (compare > 0) {
				reference[++end] = reference[j];
			}
		}
		return end;
	}


		/**
		 * <p>
		 * The {@code swap} method swaps two array elements.
		 * </p>
		 * 
		 * @param a - array of references to the (x,y,z,w...) coordinates
		 * @param i - the index of the first element
		 * @param j - the index of the second element
		 */
		private static void swap(final int[][] a, final int i, final int j) {
			final int[] t = a[i];
			a[i] = a[j];
			a[j] = t;
		}
		
		/**
		 * <p>
		 * This {@code insertionSort} method is adapted from an insertion sort function
		 * that Jon Bentley proposed in "Programming Pearls", p. 115-116, Addison-Wesley, 1999.
		 * </p>
		 * 
         * @param a - array of references to the (x,y,z,w...) coordinates 
         * @param s - the start index for the elements to be sorted
         * @param n - the number of elements to be sorted
         * @param p - the most significant dimension or the partition coordinate
		 */
		private static void insertionSort(final double[][] a, final int s, final int n, final int p) {
		    for (int i = s + 1; i < s + n; i++) {
		        double[] tmp = a[i];
		        int j;
		        for (j = i; j > s && superKeyCompare(a[j-1], tmp, p) > 0; j--) {
		            a[j] = a[j-1];
		        }
		        a[j] = tmp;
		    }
		}

		/**
		 * <p>
		 * The {@code partition} method partitions an array of references to (x,y,z,w...)
		 * tuples about its kth element and returns the array index of the kth element.
		 * See https://yiqi2.wordpress.com/2013/07/03/median-of-medians-selection-algorithm/
		 * that contains a bug that causes a java.lang.ArrayIndexOutOfBoundsException.
		 * </p>
		 * 
		 * @param a - arrays of references to the (x,y,z,w...) coordinates 
		 * @param start - the start index for the elements to be considered
		 * @param n - the number of elements to consider
		 * @param k - the element to find
		 * @param medians - a scratch array for the medians
		 * @param first - the first index for the scratch array
		 * @param p - the most significant dimension or the partition coordinate
		 * @return the index of the kth element in the array about which the array has been partitioned
		 */
		private static int partition(final int[][] a, final int start, final int n, final int k,
				final int[][] medians, final int first, final int p) {
			
			// The number of elements in a group, which must be ODD and should be > 1.
			final int GROUP_SIZE = 5;
			
			if (n <=0 || n > a.length) {
				throw new IllegalArgumentException("n = " + n);
			}
			if (k <= 0 || k > n) {
				throw new IllegalArgumentException("k = " + k);
			}
			if (start + n > a.length) {
				throw new IllegalArgumentException("s = " + start + "  n = " + n + "  length = " + a.length);
			}
			
			// This trivial case terminates recursion.
		    if ( n == 1 && k == 1 ) {
		        return start;
		    }
		    
		    // Determine how many medians to find.  Round down to count
		    // only groups that comprise fully GROUP_SIZE elements.  Any
		    // remaining group of elements that doesn't comprise GROUP_SIZE
		    // elements will be processed after the following 'for' loop.
		    int m = n / GROUP_SIZE;
		    
		    // Initialize the index of the start of a group then process each group.
		    int startOfGroup = 0;
		    for (int i = 0; i < m; i++) {
		    	
	            // Insertion sort the group of GROUP_SIZE elements to find the median.
	            insertionSort(a, start + startOfGroup, GROUP_SIZE, p);
	        	
	        	// Avoid overflow when computing the address of the median element.
	            medians[first + i] = a[start + ( startOfGroup + ( (GROUP_SIZE - 1) >> 1 ) )];
	            
		        // Update the index of the beginning of the group of GROUP_SIZE elements.
		    	startOfGroup += GROUP_SIZE;
		    }
	    	
	    	// Calculate and check the number of remaining elements.
	    	int remainingElements = n - startOfGroup;
	    	if ( remainingElements < 0 || remainingElements >= GROUP_SIZE ) {
	    		throw new RuntimeException("incorrect group calculation");
	    	}
	    	
	    	// Does a group that comprises less than GROUP_SIZE elements remain?
	        if ( remainingElements > 0 ) {
	        	
	        	// Yes, insertion sort the remaining elements to find the median.
	            insertionSort(a, start + startOfGroup, remainingElements, p);

	        	// Avoid overflow when computing the address of the median element.
	            medians[first + m] = a[start + ( startOfGroup + ( (remainingElements) - 1 >> 1 ) )];
	            
	        	// Count the remainder group.
	        	m++;
	        }
	        
		     
		    // Select the median of medians for partitioning the elements.  Note that (m + 1) >> 1
		    // correctly designates the median element as the "kth" element instead of the address
		    // of the median element in the medians array.  The medians array must start with element
		    // first + m for the next level of recursion to avoid overwriting the median elements.
		    // The medians array will have adequate capacity for all of the medians for all of the
		    // recursive calls because the initial call to this partition() method from the buildKdTree()
		    // method provides a medians array that is the same size as the reference array.  Each
		    // recursive call creates the (1 / GROUP_SIZE) fraction of the medians as the call at the
		    // prior level of recursion, so the total requirement for storage of medians is the following
		    // fraction of the temporary array for the following values of GROUP_SIZE:
		    //
		    // for GROUP_SIZE = 3, the fraction is 1/3 + 1/9 + 1/27 + 1/81 + ... < 1/2
		    // for GROUP_SIZE = 5, the fraction is 1/5 + 1/25 + 1/125 + 1/625 + ... < 1/4
		    // for GROUP_SIZE = 7, the fraction is 1/7 + 1/49 + 1/243 + 1/1701 + ... < 1/8
		    //
		    // Note: it is possible to allocate the medians array locally to this partition() method
		    // instead of providing it via a calling parameter to this method; however, because the
		    // mergeSort() method requires a temporary array, that array is re-used as the medians array.
		    // But local allocation of the medians array appears to promote marginally faster execution.
		    final int[] medianOfMedians =
		    		medians[ partition(medians, first, m, (m + 1) >> 1, medians, first + m, p) ];
		     
		    // Find the address of the median of medians and swap the median of medians with
		    // the last element to get it out of the way for the ensuing partition operation.
		    for (int i = 0; i < n; i++) {
		        if (a[start + i] == medianOfMedians) {
		            swap(a, start + i, start + n - 1);
		            break;
		        }
		    }

		    // Partition the a array relative to the median of medians into < and > subsets.
		    int lessThanIndex = 0;
		    for (int i = 0; i < n - 1; i++) {
		    	if ( superKeyCompare(a[start + i], medianOfMedians, p) < 0 ) {
		    		if (i != lessThanIndex) {
		    			swap(a, start + i, start + lessThanIndex);
		    		}
		    		lessThanIndex++;
		    	}
		    }

		    // Swap the median of medians into a[start + lessThanIndex] between the < and > subsets.
		    swap(a, start + lessThanIndex, start + n - 1);

		    // k is 1-based but lessThanIndex is 0-based, so compare k to lessThanIndex + 1
		    // and determine which subset (if any) must be partitioned recursively.
		    if (k < lessThanIndex + 1) {
		    	
		    	// The kth element occupies a position below the lessThanIndex, so
		        // partition the array elements of the < subset; for this subset,
		    	// the original kth element is still the kth element of this subset.
		    	return partition(a, start, lessThanIndex, k, medians, first, p);
		    	
		    } else if (k > lessThanIndex + 1) {
		    	
		    	// The kth element occupies a position above the lessThanIndex, so
		        // partition the array elements of the > subset; for this subset,
		    	// the original kth element is not the kth element of this subset
		    	// because lessThanIndex + 1 elements are in the < subset.
		    	return partition(a, start + lessThanIndex + 1, n - lessThanIndex - 1,
		    			k - lessThanIndex - 1, medians, first, p);
		    	
		    } else {
		    	
		        // The kth element occupies a[start + lessThanIndex] because k == lessThanIndex + 1,
		    	// so no further partitioning is necessary.  Return start + lessThanIndex as the
		    	// index of the kth element under the definition that start is the zeroth index.
		    	return start + lessThanIndex;
		    }
		}
		
		/**
		 * <p>
		 * Build a k-d tree by recursively partitioning the reference arrays and adding nodes to the tree.
		 * These arrays are permuted cyclically for successive levels of the tree in order that sorting use
		 * x, y, z, etc. as the most significant portion of the sorting or partitioning key.  The contents
		 * of the reference arrays are scrambled by each recursive partitioning.
		 * </p>
		 *
		 * @param references - arrays of references to the (x,y,z,w...) coordinates
		 * @param temporary - a scratch array into which to copy references
		 * @param start - the first element of the reference array a
		 * @param end - the last element of the reference array a
		 * @param depth - the depth in the k-d tree
		 * @return a {@link KdNodePresort}
		 */
		private static KdNodePresort buildKdTree(final double[][][] references, final double[][] temporary,
				final int start, final int end, final int depth) {

			final KdNodePresort node;

			// The partition cycles as x, y, z, etc.
			final int p = depth % references.length;

			if (end == start) {

				// Only one reference is passed to this method, so store it at this level of the tree.
				Business startBusiness = new Business("no name", "no address", references[0][start]);
				node = new KdNodePresort(startBusiness);

			} else if (end == start + 1) {

				// Two references are passed to this method in sorted order, so store the start
				// element at this level of the tree and store the end element as the > child. 
				Business startBusiness = new Business("no name", "no address", references[0][start]);
				Business endBusiness = new Business("no name", "no address", references[0][end]);
				node = new KdNodePresort(startBusiness);
				node.gtChild = new KdNodePresort(endBusiness);
				
			} else if (end == start + 2) {
				
				// Three references are passed to this method in sorted order, so
				// store the median element at this level of the tree, store the start
				// element as the < child and store the end element as the > child.
				Business startBusiness = new Business("no name", "no address", references[0][start]);
				Business startPlusOneBusiness = new Business("no name", "no address", references[0][start + 1]);
				Business endBusiness = new Business("no name", "no address", references[0][end]);
				node = new KdNodePresort(startPlusOneBusiness);
				node.ltChild = new KdNodePresort(startBusiness);
				node.gtChild = new KdNodePresort(endBusiness);
				
			} else if (end > start + 2) {
				
				// Four or more references are passed to this method.  Partitioning of the other reference
				// arrays will occur about the median element of references[0].  Avoid overflow when
				// calculating the median.  Store the median element of references[0] in a new k-d node.
				final int median = start + ( (end - start) >> 1 );
				if (median <= start || median >= end) {
					throw new RuntimeException("error in median calculation at depth = " + depth +
						" : start = " + start + "  median = " + median + "  end = " + end);
				}
				Business medianBusiness = new Business("no name", "no address", references[0][median]); 
				node = new KdNodePresort(medianBusiness);

				// Copy a[0] to the temporary array before partitioning.
				for (int i = start; i <= end; i++) {
					temporary[i] = references[0][i];
				}

				// Sweep through each of the other reference arrays in its a priori sorted order
				// and partition it into "less than" and "greater than" halves by comparing
				// super keys.  Store the result from references[i] in references[i-1], thus
				// permuting the reference arrays.  Skip the element of references[i] that
				// references a point that equals the point that is stored in the new k-d node.
				int lower = -1;
				int upper = -1;
				int lowerSave = -1;
				int upperSave = -1;
				for (int i = 1; i < references.length; i++) {
					lower = start - 1;
					upper = median;
					for (int j = start; j <= end; j++) {
						
						// Process one reference array.  Compare once only.
						final double compare = superKeyCompare(references[i][j], node.business.getCoordinates(), p);
						if (compare < 0) {
							references[i-1][++lower] = references[i][j];
						} else if (compare > 0) {
							references[i-1][++upper] = references[i][j];
						}
					}

					// Check the new indices for the reference array.
					if (lower != median - 1) {
						throw new RuntimeException("incorrect range for lower at depth - " + depth +
								" : first = " + start + "  lower = " + lower + "  median = " + median);
					}

					if (upper != end) {
						throw new RuntimeException("incorrect range for upper at depth = " + depth +
								" : median = " + median + "  upper = " + upper + "  end = " + end);
					}
					if (i > 1 && lower != lowerSave) {
						throw new RuntimeException("lower = " + lower + "  !=  lowerSave = " + lowerSave);
					}

					if (i > 1 && upper != upperSave) {
						throw new RuntimeException("upper = " + upper + "  !=  upperSave = " + upperSave);
					}

					lowerSave = lower;
					upperSave = upper;
				}

				// Copy the temporary array to the last reference array to finish permutation.  The copies to
				// and from the temporary array produce the O((k+1)n log n)  term of the computational
				// complexity.  This term may be reduced to a O((k-1)n log n) term for (x,y,z) coordinates
				// by eliminating these copies and explicitly passing x, y, z and t (temporary) arrays to this
				// buildKdTree method, then copying t<-x, x<-y and y<-z, then explicitly passing x, y, t and z
				// to the next level of recursion.  However, this approach would sacrifice the generality
				// of sorting points of any number of dimensions because explicit calling parameters
				// would need to be passed to this method for each specific number of dimensions.
				for (int i = start; i <= end; i++) {
					references[references.length - 1][i] = temporary[i];
				}

				// Recursively build the < branch of the tree.
				node.ltChild = buildKdTree(references, temporary, start, lower, depth + 1);

				// Recursively build the > branch of the tree.
				node.gtChild = buildKdTree(references, temporary, median + 1, upper, depth + 1);
				
			} else 	if (end < start) {
				
				// This is an illegal condition that should never occur, so test for it last.
				throw new RuntimeException("end < start");
				
			} else {
				
				// This final else block is added to keep the Java compiler from complaining.
				throw new RuntimeException("unknown configuration of  start and end");
			}
			
			return node;
		}
	/**
	 * <p>
	 * The {@code verifyKdTree} method checks that the children of each node of the k-d tree
	 * are correctly sorted relative to that node.
	 * </p>
	 * 
	 * @param depth - the depth in the k-d tree
	 * @return the number of nodes in the k-d tree
	 */
	private int verifyKdTree(final int depth) {

		if (business.getCoordinates()== null) {
			throw new RuntimeException("point is null");
		}

		// The partition cycles by the number of coordinates.
		final int p = depth % business.getCoordinates().length;

		// Count this node.
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

	/**
	 * <p>
	 * The {@code createKdTree} method builds a k-d tree from an int[][] of points,
	 * where the coordinates of each point are stored as an int[].
	 * </p>
	 *  
	 * @param coordinates - the int[][] of points
	 */
	public static KdNodePresort createKdTree(List<Business> businesses) {


		
		// Declare and initialize the reference arrays.  The number of dimensions may be
		// obtained from either coordinates[0].length or references.length.  The number
		// of points may be obtained from either coordinates.length or references[0].length.
		long initTime = System.currentTimeMillis();
		final double[][][] references = new double[businesses.get(0).getCoordinates().length][businesses.size()][];
		double[][] coordinates = new double[businesses.size()][];
		for (int i = 0; i<businesses.size();i++) {
			coordinates[i] = businesses.get(i).getCoordinates();
		}
		for (int i = 0; i < references.length; i++) {
			initializeReference(coordinates, references[i], i);
		}
		initTime = System.currentTimeMillis() - initTime;
		
		// Merge sort the index arrays.
		long sortTime = System.currentTimeMillis();
		final double[][] temporary = new double[coordinates.length][];
		for (int i = 0; i < references.length; i++) {
			mergeSort(references[i], temporary, 0, coordinates.length - 1, i);
		}
		sortTime = System.currentTimeMillis() - sortTime;
		
		// Remove references to duplicate coordinates via one pass through each reference array.
		long removeTime = System.currentTimeMillis();
		final int[] end = new int[references.length];
		for (int i = 0; i < references.length; i++) {
			end[i] = removeDuplicates(references[i], i);
		}
		removeTime = System.currentTimeMillis() - removeTime;
		
		// Check that the same number of references was removed from each reference array.
		for (int i = 0; i < end.length - 1; i++) {
			for (int j = i + 1; j < end.length; j++) {
				if (end[i] != end[j]) {
					throw new RuntimeException("reference removal error");
				}
			}
		}
		
		// Build the k-d tree.
		long kdTime = System.currentTimeMillis();
		final KdNodePresort root = buildKdTree(references, temporary, 0, end[0], 0);
		kdTime = System.currentTimeMillis() - kdTime;
		
		//  Verify the k-d tree and report the number of KdNodes.
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
		
		// Return the root of the tree.
		return root;
	}

	/**
	 * <p>
	 * The {@code searchKdTree} method searches the k-d tree and finds the KdNodes
	 * that lie within a cutoff distance from a query node in all k dimensions.
	 * </p>
	 *
	 * @param query - the query node
	 * @param cut - the cutoff distance
	 * @param depth - the depth in the k-d tree
	 * @return a List<KdNode>
	 * that contains the k-d nodes that lie within the cutoff distance of the query node
	 */
	public List<KdNodePresort> searchKdTree(final double[] query, final double cut, final int depth) {

		// The partition cycles as x, y, z, etc.
		final int p = depth % business.getCoordinates().length;

		// If the distance from the query node to the k-d node is within the cutoff distance
		// in all k dimensions, add the k-d node to a list.
		List<KdNodePresort> result = new ArrayList<KdNodePresort>();
		//-------------------------------------------------------------------------------------------
		// if (Distance.distance(business.getCoordinates()[0], business.getCoordinates()[1], query[0], query[1]) <= cut) {
		// 	result.add(this);
		// }
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
		//-------------------------------------------------------------------------------------------
		// Search the < branch of the k-d tree if the partition coordinate of the query point minus
		// the cutoff distance is <= the partition coordinate of the k-d node.  The < branch must be
		// searched when the cutoff distance equals the partition coordinate because the super key
		// may assign a point to either branch of the tree if the sorting or partition coordinate,
		// which forms the most significant portion of the super key, shows equality.
		if (ltChild != null) {
			if ( (query[p] - cut) <= business.getCoordinates()[p] ) {
				result.addAll( ltChild.searchKdTree(query, cut, depth + 1) );
			}
		}
		//point input = (5,5)
		//point node in the kd tree = (6,1000)
		//cutoff = 100

		// Search the > branch of the k-d tree if the partition coordinate of the query point plus
		// the cutoff distance is >= the partition coordinate of the k-d node.  The < branch must be
		// searched when the cutoff distance equals the partition coordinate because the super key
		// may assign a point to either branch of the tree if the sorting or partition coordinate,
		// which forms the most significant portion of the super key, shows equality.
		if (gtChild != null) {
			if ( (query[p] + cut) >= business.getCoordinates()[p] ) {
				result.addAll( gtChild.searchKdTree(query, cut, depth + 1) );
			}
		}
		return result;
	}


	/**
	 * <p>
	 * The {@code printKdTree} method prints the k-d tree "sideways" with the root at the left.
	 * </p>
	 */
	public void printKdTree(final int depth) {
		if (gtChild != null) {
			gtChild.printKdTree(depth+1);
		}
		for (int i = 0; i < depth; i++) {
			System.out.print("         ");
		}
		printTuple(business.getCoordinates());
		System.out.println();
		// if (ltChild != null) {
		// 	ltChild.printKdTree(depth+1);
		// }
	}

	/**
	 * <p>
	 * The {@code printTuple} method prints a tuple.
	 * </p>
	 * 
	 * @param p - the tuple
	 */
	public static void printTuple(final double[] p) {
		System.out.print("(");
		for (int i = 0; i < p.length; i++) {
			System.out.println(p[i]);
			if (i < (p.length - 1)) {
				System.out.println(", ");
			}
		}
		System.out.print(")");
	}

	public double[] getPoint() {
		return business.getCoordinates();
	}
}


	/**
	 * <p>
	 * Define a simple data set then build a k-d tree.
	 * </p>
	 */
// 	public static void main(String[] args) {

// 		// Declare the coordinates array of three-dimensional points and define (x,y,z) coordinates.  This array
// 		// may store points of any number of dimensions because he KdNode.buildKdTree method obtains the number of
// 		// dimensions from references.length = coordinates[0].length.
// 		int[][] coordinates = {
// 				{2,3,3}, {5,4,2}, {9,6,7}, {4,7,9}, {8,1,5},
// 				{7,2,6}, {9,4,1}, {8,4,2}, {9,7,8}, {6,3,1},
// 				{3,4,5}, {1,6,8}, {9,5,3}, {2,1,3}, {8,7,6},
// 				{5,4,2}, {6,3,1}, {8,7,6}, {9,6,7}, {2,1,3},
// 				{7,2,6}, {4,7,9}, {1,6,8}, {3,4,5}, {9,4,1} };
		
// 		// Build the k-d tree.
// 		final KdNode root = KdNode.createKdTree(coordinates);

// 		// Print and search the k-d tree.
// 		System.out.println("Sideways k-d tree with root on the left:\n");
// 		root.printKdTree(0);
// 		final int maximumSearchDistance = 2;
// 		final int[] query = new int[] {4, 3, 1};
// 		List<KdNode> kdNodes = root.searchKdTree(query, maximumSearchDistance, 0);
// 		System.out.print("\n" + kdNodes.size() + " nodes within " + maximumSearchDistance + " units of ");
// 		KdNode.printTuple(query);
// 		System.out.println(" in all dimensions.\n");
// 		if ( !kdNodes.isEmpty() ) {
// 			System.out.println("List of k-d nodes within " + maximumSearchDistance + "-unit search distance follows:\n");
// 			for (int i = 0; i < kdNodes.size(); i++) {
// 				KdNode node = kdNodes.get(i);
// 				KdNode.printTuple(node.point);
// 				if (i < kdNodes.size() - 1) {
// 					System.out.print("  ");
// 				}
// 			}
// 			System.out.println("\n");
// 		}
// 	}
}
