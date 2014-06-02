package Temp;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;


/**
 * @author graval
 * 
 */
public class PairSum // extends MapTest
{

  /**
   * @param args
   */

  public static void main(final String[] args) {

    MapTest.main(args);
    int[] inputNums = {0, -1, 233, 1, 3, 5, 2, 234, 6, 230, 6, 7, 245};
    int ite = 0;
    int sum = 5;
    boolean startFlag = false;
    System.out.println("Going to find all possible combinations of " + sum + " in array "
        + Arrays.toString(inputNums));
    HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

    int inputNum = 0;
    System.out.print("{");
    for (ite = 0; ite < inputNums.length; ite++) {
      int startValue = 0;

      int diff = 0;
      // initilize start input num to ite. Arguments array and location
      inputNum = (Integer) Array.get(inputNums, ite);
      diff = (sum - inputNum);

      if (diff >= 0) {
        // D System.out.println("ITE is:: " + ite + "inputNumber:: " + inputNum);

        for (int j = ite; j < inputNums.length; j++) {
          // D System.out.println("J is:: " + j);
          int intAtJ = (Integer) Array.get(inputNums, j);
          if (diff == intAtJ) {
            if (startFlag == false) {

              System.out.print("{" + inputNum + "," + diff + "}");
              startFlag = true;
            } else {
              System.out.print(", {" + inputNum + "," + diff + "}");
            }

            result.put(inputNum, diff);

          }
        }

      }

    }

    // System.out.println("RESULT>>>" + result);
    /*  Iterator<Entry<Integer, Integer>> itrMap = result.entrySet().iterator();
      System.out.print("\n{");
      while (itrMap.hasNext()) {
        Entry<Integer, Integer> data = itrMap.next();
        System.out.print("{" + data.getKey() + "," + data.getValue() + "}");
        if (itrMap.hasNext()) {
          System.out.print(", ");
        }

      }
      System.out.print("}");
      System.out.print("}");*/
    System.out.print("\nResult:>");// \n{");

    Gson gson = new Gson();
    String jsonOutput = gson.toJson(result);
    System.out.println(jsonOutput);
    System.out.print("\n{");
    Boolean first = true;
    for (Integer itgrKey : result.keySet()) {
      if (first != true) {
        System.out.print(",");
      }
      first = false;
      System.out.print("{" + itgrKey + "," + result.get(itgrKey) + "}");

    }
    System.out.print("}");

  }
}
