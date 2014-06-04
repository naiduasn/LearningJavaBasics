package Temp;

/**
 * @author graval
 * 
 */
// Finding whether a number is a perfect square number in Java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PerfectSquareNumber {

  public static void main(final String[] args) throws IOException {
    int number = 12;
    while (number != 111) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Please enter an integer : ");
      number = Integer.parseInt(reader.readLine());

      int sqrt = (int) Math.sqrt(number);
      if (sqrt * sqrt == number) {
        System.out.println(number + " is a perfect square number!");
      } else {
        System.out.println(number + " is NOT a perfect square number!");
      }

    }
  }
}
