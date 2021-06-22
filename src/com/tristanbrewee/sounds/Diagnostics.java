package com.tristanbrewee.sounds;

import com.tristanbrewee.ioutils.IOUtils;

import java.util.ArrayList;
import java.util.OptionalDouble;

public class Diagnostics {

    public static void main(String[] args) {

        ArrayList<Double> fftResults = IOUtils.doubleArrayListFromFile("testAdioFftArray");//Make sure to make the file first
        OptionalDouble max = fftResults
                .stream()
                .mapToDouble(e -> e)
                .max();
        OptionalDouble min = fftResults
                .stream()
                .mapToDouble(e -> e)
                .min();
        OptionalDouble average = fftResults
                .stream()
                .mapToDouble(e -> e)
                .average();
        if (max.isPresent())
            System.out.println("max: " + max);
        if (min.isPresent())
            System.out.println("min: " + min);
        if (average.isPresent())
            System.out.println("avg: " + average);

        System.out.println("----------");

        double[] extremeValues = fftResults
                .stream()
                .mapToDouble(e -> e)
                .filter(e -> (e > 30000) || (e < -30000))
                .toArray();

        System.out.println("total length: "+fftResults.size());
        System.out.println("length: " + extremeValues.length);
    }
}
