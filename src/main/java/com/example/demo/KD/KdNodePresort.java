package com.example.demo.KD;

import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.example.demo.Distance;
import com.example.demo.Business;

public class KdNodePresort {
	
	public final Business business;
	private KdNodePresort ltChild, gtChild;

	public KdNodePresort (final Business business) {
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

	private static KdNodePresort buildKdTree(final ArrayList<ArrayList<Business>> references, final ArrayList<Business> temporary,
			final int start, final int end, final int depth) {

		final KdNodePresort node;
		final int p = depth % references.size();

		if (end == start) {
			Business startBusiness = references.get(0).get(start);
			node = new KdNodePresort(startBusiness);

		} else if (end == start + 1) {
			Business startBusiness = references.get(0).get(start);
			Business endBusiness = references.get(0).get(end);
			node = new KdNodePresort(startBusiness);
			node.gtChild = new KdNodePresort(endBusiness);
			
		} else if (end == start + 2) {
			Business startBusiness = references.get(0).get(start);
			Business startPlusOneBusiness = references.get(0).get(start + 1);
			Business endBusiness = references.get(0).get(end);
			node = new KdNodePresort(startPlusOneBusiness);
			node.ltChild = new KdNodePresort(startBusiness);
			node.gtChild = new KdNodePresort(endBusiness);
			
		} else if (end > start + 2) {
			final int median = start + ( (end - start) >> 1 );
			if (median <= start || median >= end) {
				throw new RuntimeException("error in median calculation at depth = " + depth +
					" : start = " + start + "  median = " + median + "  end = " + end);
			}
			Business medianBusiness = references.get(0).get(median);
			node = new KdNodePresort(medianBusiness);

			for (int i = start; i <= end; i++) {
				temporary.set(i, references.get(0).get(i));
			}
			int lower = -1;
			int upper = -1;
			int lowerSave = -1;
			int upperSave = -1;
			for (int i = 1; i < references.size(); i++) {
				lower = start - 1;
				upper = median;
				for (int j = start; j <= end; j++) {
					
					final double compare = superKeyCompare(references.get(i).get(j).getCoordinates(), node.business.getCoordinates(), p);
					if (compare < 0) {
						references.get(i-1).set(++lower, references.get(i).get(j));
					} else if (compare > 0) {
						references.get(i-1).set(++upper, references.get(i).get(j));
					}
				}

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

			for (int i = start; i <= end; i++) {
				references.get(references.size() - 1).set(i, temporary.get(i));
			}
			node.ltChild = buildKdTree(references, temporary, start, lower, depth + 1);

			node.gtChild = buildKdTree(references, temporary, median + 1, upper, depth + 1);
			
		} else 	if (end < start) {
			
			throw new RuntimeException("end < start");
			
		} else {
			
			throw new RuntimeException("unknown configuration of  start and end");
		}
		
		return node;
	}

	private int verifyKdTree(final int depth) {

		if (business.getCoordinates()== null) {
			throw new RuntimeException("point is null");
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

	public static KdNodePresort createKdTree(ArrayList<Business> businesses) {
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
		
		long sortTime = System.currentTimeMillis();
		final ArrayList<Business> temporary = new ArrayList<Business>(businesses.size());
		for (int i = 0; i< businesses.size();i++) {
			temporary.add(new Business());
		}
		for (int i = 0; i < referencesCapacity; i++) {
			mergeSort(references.get(i), temporary, 0, businesses.size() - 1, i);
		}
		sortTime = System.currentTimeMillis() - sortTime;
		
		long removeTime = System.currentTimeMillis();
		final int[] end = new int[referencesCapacity];
		for (int i = 0; i < referencesCapacity; i++) {
			end[i] = removeDuplicates(references.get(i), i);
		}
		removeTime = System.currentTimeMillis() - removeTime;

		for (int i = 0; i < end.length - 1; i++) {
			for (int j = i + 1; j < end.length; j++) {
				if (end[i] != end[j]) {
					throw new RuntimeException("reference removal error");
				}
			}
		}
		
		long kdTime = System.currentTimeMillis();
		final KdNodePresort root = buildKdTree(references, temporary, 0, end[0], 0);
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

	public List<KdNodePresort> searchKdTree(final double[] query, final double cut, final int depth) {
		final int p = depth % business.getCoordinates().length;
		List<KdNodePresort> result = new ArrayList<KdNodePresort>();
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

}



