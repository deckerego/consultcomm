/**
 * The <I>Comparable</I> interface allows to objects to be compared against each other.
 * This is useful when implementing sorting algorithms such as QuickSort.
 * @author Eric M. White, Information Systems Experts, Inc.
 */
public interface Comparable {
  /**
   * @param comp The object to compare to this one.
   * @param type The type of comparison to make 
   *       (for example, multiple types can correspond to different fields).
   * @return Greater than 0 if object > this, 0 if object = this, 
   *        less than 0 if object < this
   */
  public int compareTo(Comparable comp, String type) throws Exception;
}
