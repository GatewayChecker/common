package com.brandsure.common;

public class ChecksumUtils {
    /**
     * Subtract the last digit of the sum from 10. This is the checksum digit
     * @param sum
     * @return
     */
    private String tenMinusLastDigit(int sum) {

        String sumAsString = Integer.toString(sum);
        char lastDigitAsChar =  sumAsString.charAt( sumAsString.length() - 1);
        // subtract last digit from 10
        int lastDigit = lastDigitAsChar - '0';
        // Deal with case of 10 - 0 = 10 which is 2 digits, not 1
        if (lastDigit == 0) {
            return "0";
        } else {
            return Integer.toString(10 - lastDigit);
        }
    }

}
