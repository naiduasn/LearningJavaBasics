package Temp;

import java.util.Scanner;


/**
 * @author graval
 * 
 */
public class SumTest
{

    /**
     * @param args
     *            This is to calcualte sum of numbers.
     */
    public static void main(final String[] args)
    {
        String input = new String("abc234c3d64edefa9991b1.0c0.00d,e");

        while (!(input.equalsIgnoreCase("q")))
        {
            System.out.println("Enter String:: ");
            Scanner scanIn = new Scanner(System.in);

            input = scanIn.nextLine();
            System.out.println("Input is :: " + input);

            int inpLen = input.length();
            int sum = 0;

            char[] inpChars = input.toCharArray();
            int inCharLen = inpChars.length;
            Character inpChar = ' ';
            String numString = "";
            int i = 0;
            for (i = 0; i < inCharLen; i++)
            {
                inpChar = inpChars[i];
                if (Character.isDigit(inpChar))
                {

                    for (int j = i; j < inCharLen; j++)
                    {

                        if (!(Character.isDigit(inpChars[j])))
                        {
                            numString = input.substring(i, j);
                            int subNumber = Integer.parseInt(numString);
                            // System.out.println("====");
                            System.out.println("///" + subNumber);
                            // System.out.println("====");
                            i = j;
                            sum = sum + subNumber;
                            break;
                        }
                    }
                }
                System.out.println("." + input.substring(0, i + 1));// + " || " + input.substring(i + 1));
            }
            System.out.println("Total ::" + sum);
        }
    }
}
