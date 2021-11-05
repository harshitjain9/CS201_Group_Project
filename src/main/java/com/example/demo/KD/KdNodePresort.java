package com.example.demo.KD;
//normal kd tree does not work - dino working on it - 
//kd tree with basic balance is not implemented yet - yash - DONE IN HALF AN HOUR
//kd tree with advanced balance does not return names and addressess - DONE

//mdf tree

//plot graph of the time taken for each data structure with different input n- 10, 100, 1000, 10000, ...
//look for a tool to measure space, check space taken with different input n - 10, 100, 1000, 10000, ....

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

/* @(#)KdTreeSingleThread.java	1.33 04/25/15 */

import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.example.demo.Distance;
import com.example.demo.Business;


/**
 * <p>
 * The k-d tree was described by Jon Bentley in "Multidimensional Binary Search Trees Used
 * for Associative Searching", CACM 18(9): 509-517, 1975.  For k dimensions and n elements
 * of data, a balanced k-d tree is built in O(kn log n) + O((k+1)n log n) time by first
 * sorting the data in each of k dimensions, then building the k-d tree in a manner that
 * preserves the order of the k sorts while recursively partitioning the data at each level
 * of the k-d tree.  No further sorting is necessary.  Moreover, it is possible to replace
 * the O((k+1)n log n) term with a O((k-1)n log n) term but this approach sacrifices the
 * generality of building the k-d tree for points of any number of dimensions.
 * </p>
 *
 * @author Russell A. Brown
 */


	/**
	 * <p>
	 * The {@code KdNode} class stores a point of any number of dimensions
	 * as well as references to the "less than" and "greater than" sub-trees.
	 * </p>
	 */
public class KdNodePresort {
	
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
	private static void initializeReference(final ArrayList<Business> coordinates,
			final ArrayList<Business> reference, final int i, final int singleReferenceCapacity) {
		for (int j = 0; j < singleReferenceCapacity; j++) {
			reference.set(j, coordinates.get(j));
		}
	}

	/**
	 * <p>
	 * The {@code superKeyCompare} method compares two int[] in as few coordinates as possible
	 * and uses the sorting or partition coordinate as the most significant coordinate.
	 * </p>
	 * 
	 * @param a - a int[]
	 * @param b - a int[]
	 * @param p - the most significant dimension
	 * @returns an int that represents the result of comparing two super keys
	 */
	private static double superKeyCompare(final double[] a, final double[] b, final int p) {
		double diff = 0;
		for (int i = 0; i < a.length; i++) {
			// A fast alternative to the modulus operator for (i + p) < 2 * a.length.
			final int r = (i + p < a.length) ? i + p : i + p - a.length;
			diff = a[r] - b[r];
			if (diff != 0) {
				break;
			}
		}
		return diff;
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
	private static void mergeSort(final ArrayList<Business> reference, final ArrayList<Business> temporary,
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

	/**
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
	private static KdNodePresort buildKdTree(final ArrayList<ArrayList<Business>> references, final ArrayList<Business> temporary,
			final int start, final int end, final int depth) {

		final KdNodePresort node;

		// The partition cycles as x, y, z, etc.
		final int p = depth % references.size();

		if (end == start) {

			// Only one reference is passed to this method, so store it at this level of the tree.
			Business startBusiness = references.get(0).get(start);
			node = new KdNodePresort(startBusiness);

		} else if (end == start + 1) {

			// Two references are passed to this method in sorted order, so store the start
			// element at this level of the tree and store the end element as the > child. 
			Business startBusiness = references.get(0).get(start);
			Business endBusiness = references.get(0).get(end);
			node = new KdNodePresort(startBusiness);
			node.gtChild = new KdNodePresort(endBusiness);
			
		} else if (end == start + 2) {
			
			// Three references are passed to this method in sorted order, so
			// store the median element at this level of the tree, store the start
			// element as the < child and store the end element as the > child.
			Business startBusiness = references.get(0).get(start);
			Business startPlusOneBusiness = references.get(0).get(start + 1);
			Business endBusiness = references.get(0).get(end);
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
			Business medianBusiness = references.get(0).get(median);
			node = new KdNodePresort(medianBusiness);

			// Copy a[0] to the temporary array before partitioning.
			for (int i = start; i <= end; i++) {
				temporary.set(i, references.get(0).get(i));
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
			for (int i = 1; i < references.size(); i++) {
				lower = start - 1;
				upper = median;
				for (int j = start; j <= end; j++) {
					
					// Process one reference array.  Compare once only.
					final double compare = superKeyCompare(references.get(i).get(j).getCoordinates(), node.business.getCoordinates(), p);
					if (compare < 0) {
						references.get(i-1).set(++lower, references.get(i).get(j));
					} else if (compare > 0) {
						references.get(i-1).set(++upper, references.get(i).get(j));
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
				references.get(references.size() - 1).set(i, temporary.get(i));
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
	public static KdNodePresort createKdTree(ArrayList<Business> businesses) {
		// Declare and initialize the reference arrays.  The number of dimensions may be
		// obtained from either coordinates[0].length or references.length.  The number
		// of points may be obtained from either coordinates.length or references[0].length.
		long initTime = System.currentTimeMillis();
		final ArrayList<ArrayList<Business>> references = new ArrayList<ArrayList<Business>>(businesses.get(0).getCoordinates().length);
		int referencesCapacity = businesses.get(0).getCoordinates().length;
		int singleReferenceCapacity = businesses.size();
		for (int i = 0; i < referencesCapacity ;i++) {
			references.add(new ArrayList<Business>());
			for (int j = 0; j < singleReferenceCapacity; j++) {
				references.get(i).add(businesses.get(j));
			}
		}
		initTime = System.currentTimeMillis() - initTime;
		
		// Merge sort the index arrays.
		long sortTime = System.currentTimeMillis();
		final ArrayList<Business> temporary = new ArrayList<Business>(businesses.size());
		for (int i = 0; i< businesses.size();i++) {
			temporary.add(new Business());
		}
		for (int i = 0; i < referencesCapacity; i++) {
			mergeSort(references.get(i), temporary, 0, businesses.size() - 1, i);
		}
		sortTime = System.currentTimeMillis() - sortTime;
		
		// Remove references to duplicate coordinates via one pass through each reference array.
		long removeTime = System.currentTimeMillis();
		final int[] end = new int[referencesCapacity];
		for (int i = 0; i < referencesCapacity; i++) {
			end[i] = removeDuplicates(references.get(i), i);
		}
		removeTime = System.currentTimeMillis() - removeTime;
		// System.out.println("References size " + String.valueOf(referencesCapacity));
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


	// /**
	//  * <p>
	//  * The {@code printKdTree} method prints the k-d tree "sideways" with the root at the left.
	//  * </p>
	//  */
	// public void printKdTree(final int depth) {
	// 	// if (gtChild != null) {
	// 	// 	gtChild.printKdTree(depth+1);
	// 	// }
	// 	// for (int i = 0; i < depth; i++) {
	// 	// 	System.out.print("         ");
	// 	// }
	// 	// printTuple(business.getCoordinates());
	// 	// System.out.println();
	// 	// if (ltChild != null) {
	// 	// 	ltChild.printKdTree(depth+1);
	// 	// }
	// }
}

/**
 * <p>
 * Define a simple data set then build a k-d tree.
 * </p>
 */
// public static void main(String[] args) {

// 	// Declare the coordinates array of three-dimensional points and define (x,y,z) coordinates.  This array
// 	// may store points of any number of dimensions because he KdNode.buildKdTree method obtains the number of
// 	// dimensions from references.length = coordinates[0].length.
// 	int[][] coordinates = {
// 			{2,3,3}, {5,4,2}, {9,6,7}, {4,7,9}, {8,1,5},
// 			{7,2,6}, {9,4,1}, {8,4,2}, {9,7,8}, {6,3,1},
// 			{3,4,5}, {1,6,8}, {9,5,3}, {2,1,3}, {8,7,6},
// 			{5,4,2}, {6,3,1}, {8,7,6}, {9,6,7}, {2,1,3},
// 			{7,2,6}, {4,7,9}, {1,6,8}, {3,4,5}, {9,4,1} };
	
// 	// Build the k-d tree.
// 	KdNode root = KdNode.createKdTree(coordinates);

// 	// Print and search the k-d tree.
// 	System.out.println("Sideways k-d tree with root on the left:\n");
// 	root.printKdTree(0);
// 	int maximumSearchDistance = 2;
// 	int[] query = new int[] {4, 3, 1};
// 	List<KdNode> kdNodes = root.searchKdTree(query, maximumSearchDistance, 0);
// 	System.out.print("\n" + kdNodes.size() + " nodes within " + maximumSearchDistance + " units of ");
// 	KdNode.printTuple(query);
// 	System.out.println(" in all dimensions.\n");
// 	if ( !kdNodes.isEmpty() ) {
// 		System.out.println("List of k-d nodes within " + maximumSearchDistance + "-unit search distance follows:\n");
// 		for (int i = 0; i < kdNodes.size(); i++) {
// 			KdNode node = kdNodes.get(i);
// 			KdNode.printTuple(node.point);
// 			if (i < kdNodes.size() - 1) {
// 				System.out.print("  ");
// 			}
// 		}
// 		System.out.println("\n");
// 	}
// }





	// /**
	//  * <p>
	//  * Build a k-d tree by recursively partitioning the reference arrays and adding nodes to the tree.
	//  * These arrays are permuted cyclically for successive levels of the tree in order that sorting use
	//  * x, y, z, etc. as the most significant portion of the sorting or partitioning key.  The contents
	//  * of the reference arrays are scrambled by each recursive partitioning.
	//  * </p>
	//  *
	//  * @param references - arrays of references to the (x,y,z,w...) coordinates
	//  * @param temporary - a scratch array into which to copy references
	//  * @param start - the first element of the reference array a
	//  * @param end - the last element of the reference array a
	//  * @param depth - the depth in the k-d tree
	//  * @return a {@link KdNodePresort}
	//  */
	// private static KdNodePresort buildKdTree(final double[][][] references, final double[][] temporary,
	// 		final int start, final int end, final int depth) {

	// 	final KdNodePresort node;

	// 	// The partition cycles as x, y, z, etc.
	// 	final int p = depth % references.length;

	// 	if (end == start) {

	// 		// Only one reference is passed to this method, so store it at this level of the tree.
	// 		Business startBusiness = new Business("no name", "no address", references[0][start]);
	// 		node = new KdNodePresort(startBusiness);

	// 	} else if (end == start + 1) {

	// 		// Two references are passed to this method in sorted order, so store the start
	// 		// element at this level of the tree and store the end element as the > child. 
	// 		Business startBusiness = new Business("no name", "no address", references[0][start]);
	// 		Business endBusiness = new Business("no name", "no address", references[0][end]);
	// 		node = new KdNodePresort(startBusiness);
	// 		node.gtChild = new KdNodePresort(endBusiness);
			
	// 	} else if (end == start + 2) {
			
	// 		// Three references are passed to this method in sorted order, so
	// 		// store the median element at this level of the tree, store the start
	// 		// element as the < child and store the end element as the > child.
	// 		Business startBusiness = new Business("no name", "no address", references[0][start]);
	// 		Business startPlusOneBusiness = new Business("no name", "no address", references[0][start + 1]);
	// 		Business endBusiness = new Business("no name", "no address", references[0][end]);
	// 		node = new KdNodePresort(startPlusOneBusiness);
	// 		node.ltChild = new KdNodePresort(startBusiness);
	// 		node.gtChild = new KdNodePresort(endBusiness);
			
	// 	} else if (end > start + 2) {
			
	// 		// Four or more references are passed to this method.  Partitioning of the other reference
	// 		// arrays will occur about the median element of references[0].  Avoid overflow when
	// 		// calculating the median.  Store the median element of references[0] in a new k-d node.
	// 		final int median = start + ( (end - start) >> 1 );
	// 		if (median <= start || median >= end) {
	// 			throw new RuntimeException("error in median calculation at depth = " + depth +
	// 				" : start = " + start + "  median = " + median + "  end = " + end);
	// 		}
	// 		Business medianBusiness = new Business("no name", "no address", references[0][median]); 
	// 		node = new KdNodePresort(medianBusiness);

	// 		// Copy a[0] to the temporary array before partitioning.
	// 		for (int i = start; i <= end; i++) {
	// 			temporary[i] = references[0][i];
	// 		}

	// 		// Sweep through each of the other reference arrays in its a priori sorted order
	// 		// and partition it into "less than" and "greater than" halves by comparing
	// 		// super keys.  Store the result from references[i] in references[i-1], thus
	// 		// permuting the reference arrays.  Skip the element of references[i] that
	// 		// references a point that equals the point that is stored in the new k-d node.
	// 		int lower = -1;
	// 		int upper = -1;
	// 		int lowerSave = -1;
	// 		int upperSave = -1;
	// 		for (int i = 1; i < references.length; i++) {
	// 			lower = start - 1;
	// 			upper = median;
	// 			for (int j = start; j <= end; j++) {
					
	// 				// Process one reference array.  Compare once only.
	// 				final double compare = superKeyCompare(references[i][j], node.business.getCoordinates(), p);
	// 				if (compare < 0) {
	// 					references[i-1][++lower] = references[i][j];
	// 				} else if (compare > 0) {
	// 					references[i-1][++upper] = references[i][j];
	// 				}
	// 			}

	// 			// Check the new indices for the reference array.
	// 			if (lower != median - 1) {
	// 				throw new RuntimeException("incorrect range for lower at depth - " + depth +
	// 						" : first = " + start + "  lower = " + lower + "  median = " + median);
	// 			}

	// 			if (upper != end) {
	// 				throw new RuntimeException("incorrect range for upper at depth = " + depth +
	// 						" : median = " + median + "  upper = " + upper + "  end = " + end);
	// 			}
	// 			if (i > 1 && lower != lowerSave) {
	// 				throw new RuntimeException("lower = " + lower + "  !=  lowerSave = " + lowerSave);
	// 			}

	// 			if (i > 1 && upper != upperSave) {
	// 				throw new RuntimeException("upper = " + upper + "  !=  upperSave = " + upperSave);
	// 			}

	// 			lowerSave = lower;
	// 			upperSave = upper;
	// 		}

	// 		// Copy the temporary array to the last reference array to finish permutation.  The copies to
	// 		// and from the temporary array produce the O((k+1)n log n)  term of the computational
	// 		// complexity.  This term may be reduced to a O((k-1)n log n) term for (x,y,z) coordinates
	// 		// by eliminating these copies and explicitly passing x, y, z and t (temporary) arrays to this
	// 		// buildKdTree method, then copying t<-x, x<-y and y<-z, then explicitly passing x, y, t and z
	// 		// to the next level of recursion.  However, this approach would sacrifice the generality
	// 		// of sorting points of any number of dimensions because explicit calling parameters
	// 		// would need to be passed to this method for each specific number of dimensions.
	// 		for (int i = start; i <= end; i++) {
	// 			references[references.length - 1][i] = temporary[i];
	// 		}

	// 		// Recursively build the < branch of the tree.
	// 		node.ltChild = buildKdTree(references, temporary, start, lower, depth + 1);

	// 		// Recursively build the > branch of the tree.
	// 		node.gtChild = buildKdTree(references, temporary, median + 1, upper, depth + 1);
			
	// 	} else 	if (end < start) {
			
	// 		// This is an illegal condition that should never occur, so test for it last.
	// 		throw new RuntimeException("end < start");
			
	// 	} else {
			
	// 		// This final else block is added to keep the Java compiler from complaining.
	// 		throw new RuntimeException("unknown configuration of  start and end");
	// 	}
		
	// 	return node;
	// }