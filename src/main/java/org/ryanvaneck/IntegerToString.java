package org.ryanvaneck;

import java.util.*;

/**
 * Created by Ryan Van Eck on 12/2/15.
 *
 * Converts inputed integer into its String representation in English. Converts to a 'clean' string with only number names, no commas or connecting words, except for
 * a dash between 10's place and 1's place in two digit numbers, ie 55=fifty-five.
 * Puts digits of input into array on ints, then processes them, up to 3 digits at a time, from the tail end of the array. Uses StringBuilder to construct
 * result. Will successfully convert all possible int values, from Integer.MIN_VALUE(-2147483648) to Integer.MAX_VALUE(2147483647).
 * @param value
 * @return String representation of inputed integer.
 */

public class IntegerToString {

    private static Map<Integer, String> numbers;
    private static String[] multipliers;

    static {
        //initialize array of number names to be referenced when building result
        multipliers = new String[] { "", Constants.THOUSAND, Constants.MILLION, Constants.BILLION };

        //intialize map of numbers to Strings
        numbers = new HashMap<>();
        numbers.put(0, Constants.ZERO);
        numbers.put(1, Constants.ONE);
        numbers.put(2, Constants.TWO);
        numbers.put(3, Constants.THREE);
        numbers.put(4, Constants.FOUR);
        numbers.put(5, Constants.FIVE);
        numbers.put(6, Constants.SIX);
        numbers.put(7, Constants.SEVEN);
        numbers.put(8, Constants.EIGHT);
        numbers.put(9, Constants.NINE);
        numbers.put(10, Constants.TEN);
        numbers.put(11, Constants.ELEVEN);
        numbers.put(12, Constants.TWELVE);
        numbers.put(13, Constants.THIRTEEN);
        numbers.put(14, Constants.FOURTEEN);
        numbers.put(15, Constants.FIFTEEN);
        numbers.put(16, Constants.SIXTEEN);
        numbers.put(17, Constants.SEVENTEEN);
        numbers.put(18, Constants.EIGHTEEN);
        numbers.put(19, Constants.NINETEEN);
        numbers.put(20, Constants.TWENTY);
        numbers.put(30, Constants.THIRTY);
        numbers.put(40, Constants.FORTY);
        numbers.put(50, Constants.FIFTY);
        numbers.put(60, Constants.SIXTY);
        numbers.put(70, Constants.SEVENTY);
        numbers.put(80, Constants.EIGHTY);
        numbers.put(90, Constants.NINETY);
    }



    public static String convert(int value) {

        StringBuilder resultBuilder = new StringBuilder();

        //first we flip negative input values to positive, and note this to later prepend 'negative' to final result
        boolean negative = false;
        //if value is minimum value of integer, flipping the sign will fail(-MIN_VALUE > MAX_VALUE), so we cover this edge case by temporarily decreasing abs. value by 1
        boolean minValue = false;
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                value++;
                minValue = true;
            }
            value = -value;
            negative = true;
        }

        //first 21 ints from 0 have unique names, so we can just look them up
        if (value <= 20) {
            resultBuilder.insert(0, numbers.get(value));
        }
        else {
            //initialize place in array of names of 'multipliers'
            int multipliersIndex = 0;
            //convert input into array of digits
            int[] intArray = toIntArray(value);
            //increase last digit by one to adjust Integer.MIN_VALUE back to correct value
            if (minValue)
                intArray[intArray.length - 1]++;
            //process input from tail end, up to 3 digits at time, adjusting position in multipliers array each iteration
            while (intArray.length > 0) {
                intArray = process(intArray, resultBuilder, multipliers[multipliersIndex++]);
            }
        }
        //insert 'negative'
        if (negative) {
            resultBuilder.insert(0, Constants.NEGATIVE + " ");
        }

        return resultBuilder.toString().trim();
    }

    /**
     * Converts an int into array of ints of its digits. Takes modulus of input by 10 to get last digit. Then divides input by 10 to get remaining part of input.
     * Repeats process with remaining part until input is 0.
     * @param integer
     * @return
     */

    private static int[] toIntArray(int integer) {

        //get 'length' of input by converting to String
        int[] digits = new int[String.valueOf(integer).length()];

        //start at last digit
        int index = digits.length - 1;
        do {
            //place last digit of input in last position of result. decrement index.
            digits[index--] = integer % 10;
            //remove last digit from input
            integer = integer / 10;
        }
        while (integer > 0);

        return digits;
    }

    /**
     * Processes up to 3 last digits of input intArray into its string representation. Appends provided multiplier to result.
     * @param input array of single digit integers
     * @param result String being built from input
     * @param multiplier current multiplier word to appended to result
     * @return portion of input int array that was not processed in this step
     */
    private static int[] process(int[] input, StringBuilder result, String multiplier) {
        int len = input.length;

        if (len >= 3) {
            //process last 3 digits, prepending produced string to front of being-built String
            result.insert(0, getThreeDigitNumber(input[len - 3], input[len - 2], input[len - 1], multiplier));
            //copy and return remaining portion of input array
            return Arrays.copyOfRange(input, 0, len - 3);
        }
        else if (len >= 2) {
            result.insert(0, getTwoDigitNumber(input[len - 2], input[len - 1], multiplier));
            return Arrays.copyOfRange(input, 0, len - 2);
        }
        else {
            result.insert(0, getOneDigitNumber(input[len - 1], multiplier));
            return new int[0];
        }

    }

    /**
     * Converts 3 digits into string their combined string representation
     * @param hundredsPlace
     * @param tensPlace
     * @param onesPlace
     * @param multiplier
     * @return
     */
    private static CharSequence getThreeDigitNumber(int hundredsPlace, int tensPlace, int onesPlace, String multiplier) {

        //this prevents printing of unnessasary multipliers when number is even number of thousands or millions etc. ie 1000000
        if (hundredsPlace == 0 && tensPlace == 0 && onesPlace == 0)
            return "";

        if (hundredsPlace == 0)
            return getTwoDigitNumber(tensPlace, onesPlace, multiplier);

        //append number in hundreds place, followed by 'hundred', then append remaining two-digit number
        return new StringBuilder()
                .append(numbers.get(hundredsPlace))
                .append(" ")
                .append(Constants.HUNDRED)
                .append(" ")
                .append(getTwoDigitNumber(tensPlace, onesPlace, multiplier));
    }

    /**
     * Converts 2 digits into string their combined string representation, appending multiplier
     * @param tensPlace
     * @param onesPlace
     * @param multiplier
     * @return
     */
    private static CharSequence getTwoDigitNumber(int tensPlace, int onesPlace, String multiplier) {

        //need actual number as it may be found in numbers map
        int number = tensPlace * 10 + onesPlace;

        if (number == 0)
            return multiplier;

        StringBuilder result = new StringBuilder();

        //check if number can be looked up directly
        String special = numbers.get(number);
        if (special != null)
            result.append(special);
        else {
            // multiply tensPlace by ten and look up in numbers, to get first part. ie 'fifty-'
            result.append(numbers.get(tensPlace * 10)).append("-");
            //get second part if not 0
            if (onesPlace != 0)
                result.append(numbers.get(onesPlace));
        }

        //append multiplier
        return result.append(" ").append(multiplier).append(" ");
    }

    /**
     * Converts 1 digit into its string representation, appending multiplier
     * @param onesPlace
     * @param multiplier
     * @return
     */
    private static CharSequence getOneDigitNumber(int onesPlace, String multiplier) {
        if (onesPlace > 0)
            return new StringBuilder(numbers.get(onesPlace) + " " + multiplier + " ");
        else
            return "";
    }

    public static void main(String[] args) {

        for (String i : args) {
            try {
                int integer = Integer.parseInt(i);
                System.out.println(integer + " -> " + convert(integer));
            } catch (Exception e) {
                System.out.println("Invalid input: {" + i + "}. All arguments must be valid integers.");
            }
        }
    }
}
