import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Podaj A: ");
        double a = Double.parseDouble(bufferedReader.readLine());
        if(a>=1 || a<=0){
            System.out.println("A wynosi od 0 do 1");
            return;
        }
        while (true) {
            System.out.println("[1] Dokonać klasyfikacji \n" +
                    "[2] Własny wektor\n" +
                    "[3] Wyjście");
            String ans = bufferedReader.readLine();
            switch (ans) {
                case "1":
                    Perceptron perceptron = new Perceptron(a);
                    try {
                        perceptron.predict();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "2":
                    System.out.println("Wprowadź swój wektor z separatorem [,]:");
                    String str = bufferedReader.readLine();
                    double[] userVector = Arrays.stream(str.split(","))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                    Perceptron userPerceptron = new Perceptron(a);
                    try {
                        userPerceptron.predictUser(userVector);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3":
                    System.exit(1);
                default:
                    break;

            }
        }
    }
}