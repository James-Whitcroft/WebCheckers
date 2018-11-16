package com.webcheckers.Model;
import java.util.*;

/**
 * Creates a row of spaceList to manage the board.
 * Iterates over the spaceList in the row
 * @author Jacob Manelius
 */
public class Row {
  private int index;
  private ArrayList<Space> spaceList;

  /**
   * Creates a row at a given index with given spaces
   * @param index the number of the row
   * @param spaces an array of all the spaces in the row
   */

  public Row(int index, Space[] spaces) {
    this.index = index;
    spaceList = new ArrayList<>();
    spaceList.addAll(Arrays.asList(spaces));
  }

  /**
   * @return index of row
   */
  public int getIndex()
  {
    return this.index;
  }

  /**
   * @return space iterator to traverse the spaces in the row
   */
  public ListIterator<Space> iterator()
  {
    return spaceList.listIterator();
  }
}
