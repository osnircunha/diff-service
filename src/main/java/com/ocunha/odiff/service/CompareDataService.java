package com.ocunha.odiff.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * <code>{@link CompareDataService}</code> provides a method to compare two byte array and return the difference details.
 *
 * Created by osnircunha on 25/07/17.
 */
@Component
public class CompareDataService {

    /**
     * Compares two byte array data and returns a {@link String} message containing the difference details between the given data content.
     *
     * @param left  a byte array containing the data content
     * @param right a byte array containing the data content
     * @return a {@link String} value containing the difference details between given data content.
     */
    String compare(byte[] left, byte[] right) {

        // Compares the byte size
        if (left.length != right.length) {
            return String.format("Different size. Left is %d bytes and Right is %d bytes", left.length, right.length);
        }

        String contentLeft = getDecodedString(left);
        String contentRight = getDecodedString(right);

        // Compares content
        if (StringUtils.equals(contentLeft, contentRight)) {
            return "Content are equals.";
        }

        String[] linesLeft = splitLines(contentLeft);
        String[] linesRight = splitLines(contentRight);

        // Check line count
        if (linesLeft.length != linesRight.length) {
            return String.format("Different line numbers. Left has %d lines and Right has %d lines", linesLeft.length, linesRight.length);
        }

        return this.findDiffs(linesLeft, linesRight);
    }

    private String getDecodedString(byte[] data) {
        return new String(Base64.getDecoder().decode(data));
    }

    private String[] splitLines(String data) {
        return data.split(System.getProperty("line.separator"));
    }

    private String findDiffs(String[] linesLeft, String[] linesRight) {
        StringBuilder linesWithDiff = new StringBuilder();
        int diffCount = 0;

        for (int i = 0; i < linesLeft.length; i++) {
            if (!linesLeft[i].equals(linesRight[i])) {
                linesWithDiff.append(i + 1).append(", ");
                diffCount++;
            }
        }

        StringBuilder response = new StringBuilder();
        response.append(diffCount).append(" difference(s) found. ");

        if (diffCount > 0) {
            int lastComma = linesWithDiff.lastIndexOf(",");
            response.append("Line(s): ").append(linesWithDiff.substring(0, lastComma));
        }

        return response.toString();
    }
}
