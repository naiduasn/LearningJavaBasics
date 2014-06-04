package Temp;

import java.util.HashMap;


/**
 * @author graval
 * 
 */
public class MapTest
{

    /**
     * @param args
     */
    /**
     * @param args
     */
    /**
     * @param args
     */
    private MapTest()
    {
        MapTest temp = new MapTest();

        // return temp;
    }

    public MapTest getOBJ()
    {
        MapTest instance = new MapTest();
        return instance;
    }

    public static void main(final String[] args)
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        Integer x = new Integer(2);
        Integer y = new Integer(2);
        String x_String = new String("2");
        String y_String = new String("2");
        // System.out.println("Z is:: " + z);
        // map.put("zero", 0);
        if (x_String == y_String)
        {
            System.out.println("Strings are ==");

        }
        if (x_String.equals(y_String))
        {
            System.out.println("Strings are equal");
        }
        if (x == y)
        {
            System.out.println("ints are ==");

        }
        if (x.equals(y))
        {
            System.out.println("Intengers are equals");
        }
        map.put(null, "TEst");

    }
}
