import java.util.Vector;

/*
 * @(#)QSortAlgorithm.java	1.5 98/06/29
 *
 * Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

/**
 * A quick sort algorithm. Copyright (c) 1997 Sun Microsystems, Inc. All Rights Reserved.<BR>
 * Modified from the original to sort a vector instead of an array.
 *
 * @author James Gosling
 * @author Kevin A. Smith
 * @author Eric M. White Information Systems Experts, Inc.
 * @author John T. Ellis Information Systems Experts, Inc.
 * @version 2.0
 */
public class QuickSort {

/** This is a generic version of C.A.R Hoare's Quick Sort
 * algorithm, sorting objects in descending order.
 * This will handle arrays that are already sorted, and arrays with 
 * duplicate keys.<BR>
 *
 * @param a A Vector
 * @param compType The type of comparison to make (the type of key to chose)
 */
public static void revSort(Vector a, String compType) throws Exception {
  Vector newVec = (Vector)a.clone();
  int j = 0;
  
  sort(newVec, 0, newVec.size() - 1, compType);
  for (int i = newVec.size() - 1; i >= 0; i--)
    a.setElementAt(newVec.elementAt(i), j++);
}

/** This is a generic version of C.A.R Hoare's Quick Sort
 * algorithm, sorting objects in ascending order.
 * This will handle arrays that are already sorted, and arrays with 
 * duplicate keys.<BR>
 *
 * @param a A Vector
 * @param compType The type of comparison to make (the type of key to chose)
 */
public static void sort(Vector a, String compType) throws Exception {
  sort(a, 0, a.size() - 1, compType);
}

/** This is a generic version of C.A.R Hoare's Quick Sort
* algorithm.  This will handle arrays that are already
* sorted, and arrays with duplicate keys.<BR>
*
* If you think of a one dimensional array as going from
* the lowest index on the left to the highest index on the right
* then the parameters to this function are lowest index or
* left and highest index or right.  The first time you call
* this function it will be with the parameters 0, a.length - 1.
*
* @param a       an integer array
* @param lo0     left boundary of array partition
* @param hi0     right boundary of array partition
* @param compType They type of comparison to make (type of key to use)
*/
static void sort(Vector a, int lo0, int hi0, String compType) throws Exception {
  int lo = lo0;
  int hi = hi0;
  Comparable mid, current;
  if (hi0 > lo0) {

    /* Arbitrarily establishing partition element as the midpoint of
     * the array.
     */
    mid = (Comparable)a.elementAt((lo0 + hi0) / 2);

    // loop through the array until indices cross
    while (lo <= hi) {
      /* find the first element that is greater than or equal to
       * the partition element starting from the left Index.
       */
      current=(Comparable)a.elementAt(lo);
      while ((lo < hi0) && (current.compareTo(mid, compType) < 0))
        current=(Comparable)a.elementAt(++lo);

      /* find an element that is smaller than or equal to
       * the partition element starting from the right Index.
       */
      current=(Comparable)a.elementAt(hi);
      while ((hi > lo0) && (current.compareTo(mid, compType) > 0))
        current=(Comparable)a.elementAt(--hi);

      // if the indexes have not crossed, swap
      if (lo <= hi) {
        swap(a, lo, hi);
        ++lo;
        --hi;
      }
    }

    /* If the right index has not reached the left side of array
     * must now sort the left partition.
     */
    if (lo0 < hi) sort(a, lo0, hi, compType);

    /* If the left index has not reached the right side of array
     * must now sort the right partition.
     */
    if (lo < hi0) sort(a, lo, hi0, compType);
  }
}

private static void swap(Vector a, int i, int j) {
  Object T;
  T = a.elementAt(i);
  a.setElementAt(a.elementAt(j),i);
  a.setElementAt(T,j);
}      
}
