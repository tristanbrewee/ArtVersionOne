package com.tristanbrewee.sounds;

import com.tristanbrewee.ioutils.IOUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SoundProcessor {

    public static void main(String[] args) {
        File file = new File("src/com/tristanbrewee/input/testAudio.wav");

        double[] real = fromWavToDoubleArray(file);
        real = cutArray(real);

        double[] imaginary = new double[real.length];
        for (int i = 0; i < imaginary.length; i++){
            imaginary[i] = 0;
        }

        double[] fftResult = fft(real, imaginary, true);

        IOUtils.doubleArrayToFile(fftResult);
        System.out.println("FINISHED!!!");
    }

    public static double[] fromWavToDoubleArray(File wavFile){
        return floatArrayToDoubleArray(shortArrayToFloatArray(byteArrayToShortArray(wavFileToByteArray(wavFile))));
    }

    private static byte[] wavFileToByteArray(File wavFile){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);

            int read;
            byte[] buffer = new byte[1024];

            while ((read = audioInputStream.read(buffer)) > 0){
                byteArrayOutputStream.write(buffer, 0, read);
            }
            byteArrayOutputStream.flush();
            byte[] audioBytes = byteArrayOutputStream.toByteArray();

            return audioBytes;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static short[] byteArrayToShortArray(byte[] byteArray){
        short[] shortArray = new short[byteArray.length / 2]; // will drop last byte if odd number
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        for (int i = 0; i < shortArray.length; i++) {
            shortArray[i] = byteBuffer.getShort();
        }
        return shortArray;
    }

    private static float[] shortArrayToFloatArray(short[] shortArray){
        float[] floatArray = new float[shortArray.length];
        for (int i = 0; i < shortArray.length; i++) {
            floatArray[i] = shortArray[i];
        }
        return floatArray;
    }

    private static double[] floatArrayToDoubleArray(float[] floatArray){
        double[] doubleArray = new double[floatArray.length];
        for (int i = 0; i < floatArray.length; i++){
            doubleArray[i] = floatArray[i];
        }
        return doubleArray;
    }

    /**
     * The Fast Fourier Transform (generic version, with NO optimizations).
     *
     * @param inputReal
     *            an array of length n, the real part
     * @param inputImag
     *            an array of length n, the imaginary part
     * @param DIRECT
     *            TRUE = direct transform, FALSE = inverse transform
     * @return a new array of length 2n
     */
    public static double[] fft(final double[] inputReal, double[] inputImag,
                               boolean DIRECT) {
        // - n is the dimension of the problem
        // - nu is its logarithm in base e
        int n = inputReal.length;

        // If n is a power of 2, then ld is an integer (_without_ decimals)
        double ld = Math.log(n) / Math.log(2.0);

        // Here I check if n is a power of 2. If exist decimals in ld, I quit
        // from the function returning null.
        if (((int) ld) - ld != 0) {
            System.out.println("The number of elements is not a power of 2.");
            return null;
        }

        // Declaration and initialization of the variables
        // ld should be an integer, actually, so I don't lose any information in
        // the cast
        int nu = (int) ld;
        int n2 = n / 2;
        int nu1 = nu - 1;
        double[] xReal = new double[n];
        double[] xImag = new double[n];
        double tReal, tImag, p, arg, c, s;

        // Here I check if I'm going to do the direct transform or the inverse
        // transform.
        double constant;
        if (DIRECT)
            constant = -2 * Math.PI;
        else
            constant = 2 * Math.PI;

        // I don't want to overwrite the input arrays, so here I copy them. This
        // choice adds \Theta(2n) to the complexity.
        for (int i = 0; i < n; i++) {
            xReal[i] = inputReal[i];
            xImag[i] = inputImag[i];

            System.out.println("loop1: "+i+"/"+n);
        }

        // First phase - calculation
        int k = 0;
        for (int l = 1; l <= nu; l++) {
            while (k < n) {
                for (int i = 1; i <= n2; i++) {
                    p = bitreverseReference(k >> nu1, nu);
                    // direct FFT or inverse FFT
                    arg = constant * p / n;
                    c = Math.cos(arg);
                    s = Math.sin(arg);
                    tReal = xReal[k + n2] * c + xImag[k + n2] * s;
                    tImag = xImag[k + n2] * c - xReal[k + n2] * s;
                    xReal[k + n2] = xReal[k] - tReal;
                    xImag[k + n2] = xImag[k] - tImag;
                    xReal[k] += tReal;
                    xImag[k] += tImag;
                    k++;

                    System.out.println("loop2 inner: "+i+"/"+n2);
                }
                k += n2;
            }
            k = 0;
            nu1--;
            n2 /= 2;

            System.out.println("loop2 outer: "+l+"/"+nu);
        }

        // Second phase - recombination
        k = 0;
        int r;
        while (k < n) {
            r = bitreverseReference(k, nu);
            if (r > k) {
                tReal = xReal[k];
                tImag = xImag[k];
                xReal[k] = xReal[r];
                xImag[k] = xImag[r];
                xReal[r] = tReal;
                xImag[r] = tImag;
            }
            k++;
        }

        // Here I have to mix xReal and xImag to have an array (yes, it should
        // be possible to do this stuff in the earlier parts of the code, but
        // it's here to readibility).
        double[] newArray = new double[xReal.length * 2];
        double radice = 1 / Math.sqrt(n);
        for (int i = 0; i < newArray.length; i += 2) {
            int i2 = i / 2;
            // I used Stephen Wolfram's Mathematica as a reference so I'm going
            // to normalize the output while I'm copying the elements.
            newArray[i] = xReal[i2] * radice;
            newArray[i + 1] = xImag[i2] * radice;

            System.out.println("loop3: "+i+"/"+newArray.length);
        }
        return newArray;
    }

    private static int bitreverseReference(int j, int nu) {
        int j2;
        int j1 = j;
        int k = 0;
        for (int i = 1; i <= nu; i++) {
            j2 = j1 / 2;
            k = 2 * k + j1 - 2 * j2;
            j1 = j2;
        }
        return k;
    }

    private static double[] cutArray(double[] originalArray){
        ArrayList<Integer> powersOfTwo = new ArrayList<>();
        int power = 2;
        int counter = 2;
        while (power < Integer.MAX_VALUE){
            counter++;
            powersOfTwo.add(power);
            power = (int) Math.pow(2, counter);
        }

        int lastPower = 2;
        for (Integer currentPower: powersOfTwo){
            if (currentPower > originalArray.length){
                double[] newArray = new double[lastPower];
                for (int i = 0; i < lastPower; i++){
                    newArray[i] = originalArray[i];
                }
                originalArray = newArray;
            }
            else {
                lastPower = currentPower;
            }
        }
        return originalArray;
    }
}
