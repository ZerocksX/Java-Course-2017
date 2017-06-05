package hr.fer.zemris.java.hw06.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import static hr.fer.zemris.java.hw06.crypto.Util.bytetohex;
import static hr.fer.zemris.java.hw06.crypto.Util.hextobyte;

/**
 * Program that takes given arguments(can be 2 or 3)<br/>
 * If first argument is "checksha" then program takes only one more argument, a file and then checks its SHA digest with <br/>
 * given digest that program reads from standard input<br/>
 * If first argument is "encrypt" or "decrypt" then program takes 2 more arguments, a file, and a new file name and then<br/>
 * the program encrypts or decrypts  first file with 2 arguments read from standard input<br/>
 * First is password, second is initialization vector. Both these values are 32 hex digits
 *
 * @author Pavao Jerebić
 */
public class Crypto {
    /**
     * Starting method
     *
     * @param args given arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (args.length == 2) {
            checksha(args, sc);
            sc.close();
        } else if (args.length == 3) {

            crypting(args, sc);
            sc.close();
        } else {
            System.out.println("Invalid number of arguments");
        }

    }

    /**
     * Helping method that reads from standard input, calculates given file's sha digest and compares it with given digest
     *
     * @param args arguments
     * @param sc   scanner
     */
    private static void checksha(String[] args, Scanner sc) {
        if (args[0].equals("checksha")) {

            String input = args[1];
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Failed to find given algorithm: \"SHA-256\"");
                return;
            }

            try {


                System.out.printf("Please provide expected sha-256 digest for %s:%n> ", input);
                String givenHex = sc.next();

                String hex = getFileDigest(input, md);
                System.out.printf("Digesting completed. ");

                if (hex.equals(givenHex)) {
                    System.out.printf("Digest of hw06test.bin matches expected digest.%n");
                } else {
                    System.out.printf(" Digest of hw06test.bin does not match the expected digest. Digest was: %s%n", hex);
                }


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


        } else {
            System.out.println("Wrong key word: " + args[0]);
        }
    }

    /**
     * Helping method that calculates digest from given file name and message digest
     *
     * @param input file name that is in resources
     * @param md    message digest
     * @return calculated digest
     * @throws IOException if the method could not find given file
     */
    private static String getFileDigest(String input, MessageDigest md) throws IOException {
        String path = null;
        try {
            path = findInResources(input);
        } catch (NullPointerException e) {
            throw new IOException("Could not find given file in resources");
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        byte[] buffer = new byte[4096];
        while (true) {
            int bytes = bis.read(buffer);
            if (bytes < 0) {
                break;
            }
            md.update(buffer, 0, bytes);

        }
        bis.close();
        return bytetohex(md.digest());
    }

    /**
     * Helping method that encrypts or decrypts given input and produces an output file in same directory(usually target/classes)
     *
     * @param args arguments, first is encrypt or decrypt, second is input file name, third is output file name
     * @param sc   scanner used to read from standard input
     */
    private static void crypting(String[] args, Scanner sc) {

        boolean encrypt;
        if (args[0].equals("encrypt")) {
            encrypt = true;
        } else if (args[0].equals("decrypt")) {
            encrypt = false;
        } else {
            System.out.println("Wrong key word: " + args[0]);
            return;
        }
        System.out.printf("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
        String keyText = sc.next();
        System.out.printf("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
        String ivText = sc.next();
        String input = args[1];
        String output = args[2];
        SecretKeySpec keySpec = null;
        AlgorithmParameterSpec paramSpec = null;
        try {
            keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
            paramSpec = new IvParameterSpec(hextobyte(ivText));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("Could not get given transformation: \"AES/CBC/PKCS5Padding\" ");
            return;
        }
        try {
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            System.out.println("Could not initialize the cipher. Message was: " + e.getMessage());
            return;
        }
        try {
            process(cipher, findInResources(input), output);

            System.out.printf("%sion completed. Generated file %s based on file %s.%n",
                    encrypt ? "Encypt" : "Decrypt",
                    output,
                    input);

        } catch (IOException | BadPaddingException | IllegalBlockSizeException | NullPointerException e) {
            if (e instanceof NullPointerException) {
                System.out.println("Could not find given file in resources");
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method that takes given input file name and then encrypts or decrypts it using given cipher creating a new file with given name
     *
     * @param cipher     a Cipher
     * @param input      input file name
     * @param outputName output file name
     * @throws IOException               if method can not read or write
     * @throws BadPaddingException       if padding is incorrect
     * @throws IllegalBlockSizeException if input file block size is invalid
     */
    private static void process(Cipher cipher, String input, String outputName) throws IOException, BadPaddingException, IllegalBlockSizeException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input));
        File output = new File(new File(input).getParent(), outputName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
        byte[] buffer = new byte[4096];
        while (true) {
            int bytes = bis.read(buffer);
            if (bytes < 0) {
                bos.write(cipher.doFinal());
                break;
            }
            bos.write(cipher.update(buffer, 0, bytes));
        }
        bos.flush();
        bis.close();
        bos.close();
    }

    /**
     * Finds given name of a file in resources
     *
     * @param s name of the file
     * @return path to the file
     * @throws NullPointerException if file can not be found
     */
    private static String findInResources(String s) {
        ClassLoader classLoader = Util.class.getClassLoader();
        return classLoader.getResource(s).getFile();
    }
}
