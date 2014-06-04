package Temp;

/**
 * @author graval
 * 
 */
public class DP
{
    /*
     * Initialize: max_so_far = 0 max_ending_here = 0
     * 
     * Loop for each element of the array (a) max_ending_here = max_ending_here + a[i] (b) if(max_ending_here < 0)
     * max_ending_here = 0 (c) if(max_so_far < max_ending_here) max_so_f
     */
    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        // TODO Auto-generated method stub
        int maxSoFar = 0;
        int[] inputNums =
        { -2, -3, 4, -1, -2, 1, 5, -3 };// -2, -3, 4, -1, -2, -1, 1, 5, -3 };
        maxSoFar = inputNums[0];
        int max_ending_here = 0;
        for (int i = 0; i < inputNums.length; i++)
        {

            // System.out.println();

            if (inputNums[i] < max_ending_here + inputNums[i])
            {
                max_ending_here = max_ending_here + inputNums[i];
            }
            else
            {
                max_ending_here = inputNums[i];
            }
            if (maxSoFar > max_ending_here)
            {
                maxSoFar = maxSoFar;
            }
            else
            {
                maxSoFar = max_ending_here;
            }

            /*
             * max_ending_here = max_ending_here + inputNums[i]; if (max_ending_here < 0) { max_ending_here = 0; } else
             * if (max_ending_here >= maxSoFar) { maxSoFar = max_ending_here; // // System.out.println("Now Sum Is:: " +
             * maxSumSoFar + " Added element :: " + inputNums[i]); }
             */
            // Alternate

            System.out.println("Processing:: " + inputNums[i] + " i = " + i + " max_ending_Here:: " + max_ending_here
                    + " MaxSoFar:: " + maxSoFar + " max_ending_here+inputNum:: " + (max_ending_here + inputNums[i]));
        }
        System.out.println("Final Sum is:: " + maxSoFar);
    }
    // public max()
}
