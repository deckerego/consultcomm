import java.util.Vector;

public class QuickSort {
  public static void revSort(Vector list, String compType) throws Exception {
    int i=0, j=list.size()-1;
    sort(list, compType);
    while(i<j) swap(list, i++, j--);
  }

  public static void sort(Vector list, String compType) throws Exception {
    sort(list, 0, list.size()-1, compType);
  }

  /**
   * Quicksort - bestcase O(n log n), worstcase O(n^2)
   */
  static void sort(Vector list, int low, int high, String compType) throws Exception {
    if (high > low) {
      int mid = partition(list, low, high, compType);
      sort(list, low, mid-1, compType);
      sort(list, mid+1, high, compType);
    }
  }

  private static int partition(Vector list, int low, int upp, String compType) throws Exception {
    int lastlow = low;
    Comparable pivot = (Comparable)list.elementAt(low);
    for(int i=low+1; i<=upp; i++) {
      Comparable current = (Comparable)list.elementAt(i);
      if(current.compareTo(pivot, compType) < 0) swap(list, ++lastlow, i);
    }
    swap(list, low, lastlow);
    return lastlow;
  }

  private static void swap(Vector list, int first, int second) {
    Object firstObj = list.elementAt(first);
    Object secondObj = list.elementAt(second);
    list.setElementAt(secondObj, first);
    list.setElementAt(firstObj, second);
  }
}
