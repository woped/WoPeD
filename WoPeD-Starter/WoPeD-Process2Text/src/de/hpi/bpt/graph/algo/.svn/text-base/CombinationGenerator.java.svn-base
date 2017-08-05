package de.hpi.bpt.graph.algo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Systematically generate combinations
 * @author Michael Gilleland, Artem Polyvyanyy
 *
 * @param <O>
 */
public class CombinationGenerator<O> {

	private O[] arr;
	private int[] a;
	private int n;
	private int r;
	private BigInteger numLeft;
	private BigInteger total;
	
	@SuppressWarnings("unchecked")
	public CombinationGenerator (Collection<O> objs, int r) {
		if (r > objs.size() || objs.size() < 1 || r < 1)
			throw new IllegalArgumentException ();
	 
		this.n		= objs.size();
		this.r		= r;
		this.arr	= (O[]) objs.toArray(); 
		this.a		= new int[r];
		BigInteger nFact = getFactorial(n);
		BigInteger rFact = getFactorial(r);
		BigInteger nminusrFact = getFactorial(n - r);
		this.total = nFact.divide (rFact.multiply (nminusrFact));
		reset();
	}
	
	/**
	 * Reset the generator
	 */
	public void reset () {
		for (int i = 0; i < this.a.length; i++) a[i] = i;
		
		this.numLeft = new BigInteger (this.total.toString ());
	}
	
	/**
	 * Return number of combinations not yet generated
	 */
	public BigInteger getNumLeft () {
		return this.numLeft;
	}
	
	/**
	 * Are there more combinations?
	 */
	public boolean hasMore () {
		return this.numLeft.compareTo(BigInteger.ZERO) == 1;
	}
	
	/**
	 * Return total number of combinations
	 */
	public BigInteger getTotal () {
		return this.total;
	}
	
	/**
	 * Compute factorial
	 */
	private static BigInteger getFactorial (int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--)
			fact = fact.multiply (new BigInteger (Integer.toString (i)));

		return fact;
	}
	
	/**
	 * Generate next combination (algorithm from Rosen p. 286)
	 */
	public Collection<O> getNextCombination () {
		if (this.numLeft.equals(total)) {
			this.numLeft = this.numLeft.subtract (BigInteger.ONE);
			return this.indicesToArray(a);
		}
		
		int i = r - 1;
		while (a[i] == n - r + i) i--;
		a[i] = a[i] + 1;
		for (int j = i + 1; j < r; j++) a[j] = a[i] + j - i;
		
		numLeft = numLeft.subtract(BigInteger.ONE);
		return this.indicesToArray(a);
	}
	
	private Collection<O> indicesToArray(int[] a) {
		Collection<O> result = new ArrayList<O>();
		for (int i = 0; i < this.r; i++) {
			result.add(arr[a[i]]);
		}
		
		return result;
	}
}
