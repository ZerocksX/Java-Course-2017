package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.math.Complex;
import hr.fer.zemris.java.math.ComplexPolynomial;
import hr.fer.zemris.java.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Program that calculates Newton-Raphson interation-based fractals and displays them<br/>
 * User needs to input roots of the complex polynom<br/>
 * If user has given at least 2 roots then the user can write done and fractal will be displayed<br/>
 * After a window is opened press 'F1' for further help
 *
 * @author Pavao Jerebić
 */
public class Newton {
    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
        Scanner sc = new Scanner(System.in);
        int nOfArguments = 0;
        List<Complex> arguments = new ArrayList<>();
        while (true) {
            System.out.printf("Root %d> ", nOfArguments + 1);
            String line = sc.nextLine();
            if (line.equals("done")) {
                if (nOfArguments < 2) {
                    System.out.println("Please input more arguments");
                } else {
                    System.out.println("Image of fractal will appear shortly. Thank you.");
                    break;
                }
            } else {
                try {
                    Complex c = parseComplex(line);
                    arguments.add(c);
                    nOfArguments++;
                } catch (Exception e) {
                    System.out.println("Invalid argument. Message was: " + e.getMessage());
                }
            }
        }
        sc.close();
        ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(arguments.toArray(new Complex[nOfArguments]));
        System.out.println(polynomial);
        FractalViewer.show(new MyProducer(polynomial));

    }


    /**
     * Helping method that parses a complex number from the given string<br/>
     * Format is (real)_(+/-)_i(imaginary) where '_' symbolizes whitespace
     *
     * @param s string
     * @return complex number
     * @throws IllegalArgumentException if string is malformed
     */
    private static Complex parseComplex(String s) {
        String[] data = s.trim().split(" ");
        if (data.length == 1) {
            if (data[0].charAt(0) == 'i') {
                String imaginary = data[0];
                return parseFromReSignIm("0", "+", imaginary);
            } else {
                return new Complex(Double.parseDouble(data[0]), 0);
            }
        } else if (data.length == 3) {

            String real = data[0], sign = data[1], imaginary = data[2];
            return parseFromReSignIm(real, sign, imaginary);
        } else if (data.length == 2) {

            String sign = data[0], imaginary = data[1];
            if (!sign.equals("+") && !sign.equals("-")) {
                throw new IllegalArgumentException("Expected a sign");
            }
            return parseFromReSignIm("0", sign, imaginary);
        } else {
            throw new IllegalArgumentException("Invalid input. Try separating real part, sign and imaginary part with spaces");
        }
    }

    /**
     * Creates a complex number from real part, sign and imaginary part
     *
     * @param real      real
     * @param sign      sign
     * @param imaginary imaginary
     * @return a complex number
     */
    private static Complex parseFromReSignIm(String real, String sign, String imaginary) {
        if (imaginary.charAt(0) != 'i') {
            throw new IllegalArgumentException("Expected imaginary constant");
        }
        double imaginaryValue = imaginary.length() == 1 ? 1.0 : Double.parseDouble(imaginary.substring(1));
        if (sign.equals("+")) {
            return new Complex(Double.parseDouble(real), imaginaryValue);
        } else if (sign.equals("-")) {
            return new Complex(Double.parseDouble(real), -imaginaryValue);
        } else {
            throw new IllegalArgumentException("Expected a sign");
        }
    }

    /**
     * Producer class with limit of 256 iterations
     */
    public static class MyProducer implements IFractalProducer {

        /**
         * Rooted polynomial
         */
        private ComplexRootedPolynomial polynomial;
        /**
         * Executor service pool
         */
        private ExecutorService pool;

        /**
         * Constructor
         *
         * @param polynomial rooted polynomial
         */
        public MyProducer(ComplexRootedPolynomial polynomial) {
            this.polynomial = polynomial;
            this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), runnable -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer) {
            System.out.println("Zapocinjem izracun...");
            int m = 16 * 16;
            short[] data = new short[width * height];
            int processors = Runtime.getRuntime().availableProcessors();
            final int stripCount = 8 * processors;
            int yCountPerStrip = height / stripCount;

            List<Future<Void>> results = new ArrayList<>();

            for (int i = 0; i < stripCount; i++) {
                int yMin = i * yCountPerStrip;
                int yMax = (i + 1) * yCountPerStrip - 1;
                if (i == stripCount - 1) {
                    yMax = height - 1;
                }
                NewtonCalculator calculate = new NewtonCalculator(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, polynomial);
                results.add(pool.submit(calculate));
            }
            for (Future<Void> posao : results) {
                try {
                    posao.get();
                } catch (InterruptedException | ExecutionException ignored) {
                }
            }
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
        }
    }

    /**
     * Helping class used to describe a callable operation that calculates a root to which the complex number converges
     */
    private static class NewtonCalculator implements Callable<Void> {

        /**
         * Minimum real
         */
        double reMin;
        /**
         * Maximum real
         */
        double reMax;
        /**
         * Minimum imaginary
         */
        double imMin;
        /**
         * Maximum imaginary
         */
        double imMax;
        /**
         * Width
         */
        int width;
        /**
         * Height
         */
        int height;
        /**
         * Minimum y coordinate
         */
        int yMin;
        /**
         * Maximum y coordinate
         */
        int yMax;
        /**
         * Maximum number of iterations
         */
        int m;
        /**
         * Result
         */
        short[] data;
        /**
         * Rooted polynomial
         */
        ComplexRootedPolynomial polynomial;
        /**
         * Polynom
         */
        ComplexPolynomial polynom;

        /**
         * Basic constructor
         *
         * @param reMin      minimum real
         * @param reMax      maximum real
         * @param imMin      minimum imaginary
         * @param imMax      maximum imaginary
         * @param width      width
         * @param height     height
         * @param yMin       minimum y coordinate
         * @param yMax       maximum y coordinate
         * @param m          maximum number of iterations
         * @param data       result
         * @param polynomial rooted polynomial
         */
        private NewtonCalculator(double reMin, double reMax, double imMin,
                                 double imMax, int width, int height, int yMin, int yMax,
                                 int m, short[] data, ComplexRootedPolynomial polynomial) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.polynomial = polynomial;
            this.polynom = polynomial.toComplexPolynom();
        }

        @Override
        public Void call() {
            int offset = yMin * width;
            StringBuilder sb = new StringBuilder();
            for (int y = yMin; y <= yMax; y++) {
                for (int x = 0; x < width; x++) {
                    double cre = (double) x / ((double) width - 1.0D) * (reMax - reMin) + reMin;
                    double cim = (double) (height - 1 - y) / ((double) height - 1.0D) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);
                    int iter = 0;
                    Complex zn1;
                    double convergenceTreshold = 0.001;
                    int maxIter = m;
                    double module;
                    do {
                        Complex numerator = polynom.apply(zn);
                        Complex denominator = polynom.derive().apply(zn);
                        Complex fraction = numerator.divide(denominator);
                        zn1 = zn.sub(fraction);
                        module = zn1.sub(zn).module();
                        zn = zn1;
                        iter++;
                    } while (module > convergenceTreshold && iter < maxIter);
                    double rootTreshold = 0.002;
                    int index = polynomial.indexOfClosestRootFor(zn1, rootTreshold);
                    sb.append(iter).append(": ").append(zn1).append(": ").append(index).append(" ");
                    if (index == -1) {
                        data[offset++] = 0;
                    } else {
                        data[offset++] = (short) index;
                    }
                }
            }
            return null;
        }
    }
}
