package org.ryanvaneck;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.Scanner;

/**
 * Created by ryanvaneck on 12/2/15.
 */
public class IntegerToStringTest {

    //parses file (each line is number, string) and checks runs IntegerToString.convert(number), compares to the line's string
    //files contains all ints from -1000 to 1000, then a selection of larger numbers
    @Test
    public void testFromFile() {
        try {
            File in = new File("src/test/resources/numbersAndStrings.txt");
            Scanner scanner = new Scanner(in);
            while (scanner.hasNextLine()) {
                String[] pieces = scanner.nextLine().split(",");
                int i = Integer.parseInt(pieces[0]);
                String converted = pieces[1].trim();
                boolean pass = IntegerToString.convert(i).equals(converted);
                if (!pass) {
                    System.out.println("test failed on " + i + " : {" + IntegerToString.convert(i) + "}");
                    System.out.println("should be: {" + converted + "}");
                }
                Assert.assertTrue(pass);
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void maxAndMin() {
        Assert.assertTrue(
                IntegerToString.convert(Integer.MAX_VALUE).equals("two billion one hundred forty-seven million four hundred eighty-three thousand six hundred forty-seven"));
        Assert.assertTrue(IntegerToString.convert(Integer.MIN_VALUE)
                .equals("negative two billion one hundred forty-seven million four hundred eighty-three thousand six hundred forty-eight"));
    }

    @Test
    public void speedTest() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            IntegerToString.convert((int) (Math.random() * Integer.MAX_VALUE));
        }
        System.out.println("Ran 100,000 conversions of random integers in " + (System.currentTimeMillis() - now) + " milliseconds");
    }
}
