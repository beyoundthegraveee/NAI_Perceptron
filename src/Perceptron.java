import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.*;

public class Perceptron {
    private String testSet = "Test-set.csv";

    private String trainSet = "Train-set.csv";

    public double A;

    public static String type1;

    public static String type2;


    public int trains = 0;

    public double accuracy;


    List<String> check = new ArrayList<>();

    public double [] weights;

     double threshold;

    public Perceptron(double a) {
        Random random = new Random();
        A = a;
        this.threshold = random.nextDouble()*2-1;
    }

    public List<double[]> getDataSet(String filename) throws IOException {
        List<double[]> dataset = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = bufferedReader.readLine())!=null){
            String [] values = line.split("[^\\d.\\d]+");
            double[] data = new double[values.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = Double.parseDouble(values[i]);
            }
            dataset.add(data);
        }

        return dataset;

    }

    private void initializeWeights(){
        weights = new double[4];
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextDouble()*2-1;
        }
    }

    public List<String> getTypes(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        List<String> types = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine())!=null){
            String [] parts = line.split(",");
            types.add(parts[parts.length-1]);
        }
        HashSet<String> set = new HashSet<>(types);
        int count = 1;
        for (String str : set){
            switch (count){
                case 1:  type1 = str;
                break;
                case 2: type2 = str;
                break;
            }
            count++;
        }

        return types;
    }

    public void predict() throws IOException, InterruptedException {
        initializeWeights();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Wprowadź liczbę iteracji treningowych od 1 do 5.");
        int trainings = Integer.parseInt(bufferedReader.readLine());
        if(trainings > 5 || trainings < 1){
            System.out.println("Error...");
            return;
        }
        for (int i = 0; i < trainings; i++) {
            train();
        }
        List<double[]> testSet = getDataSet(this.testSet);
        for (double[] vector : testSet){
            String type = computeActivation(vector) > threshold ? type2 : type1;
            String str = Arrays.toString(vector) + " " + type;
            check.add(str);
        }

        for (String str : check){
            System.out.println("Perceptron: "+str);
        }

        check("Test-set.csv", check);
    }

    public double computeActivation(double[] vector){
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum+=vector[i]*weights[i];
        }
        return sum;

    }

    public void train() throws IOException, InterruptedException {
        List<String> types = getTypes(this.trainSet);
        List<double[]> trainSet = getDataSet(this.trainSet);
        for (int i = 0; i < trainSet.size(); i++) {
            double[] vector = trainSet.get(i);
            int actual = computeActivation(vector) > threshold ? 1 : 0;
            int desired = types.get(i).equals(type2) ? 1 : 0;
            if (actual != desired) {
                updateWeights(vector, actual, desired);
            }
        }

        trains++;
        System.out.println("Sesja treningowa numer "+ trains + " zakończona");
        Thread.sleep(500);

    }



    public void updateWeights(double[] vector , double actual, double desired){
        double count = desired-actual;
        for (int i = 0; i < vector.length; i++) {
            weights[i] += count*vector[i]*A;
        }
        threshold +=(-1)*A*count;
    }


    public void predictUser(double[] vector) throws IOException, InterruptedException {
        initializeWeights();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Wprowadź liczbę iteracji treningowych od 1 do 5.");
        int trainings = Integer.parseInt(bufferedReader.readLine());
        if(trainings > 5 || trainings < 1){
            System.out.println("Error...");
            return;
        }
        for (int i = 0; i < trainings; i++) {
            train();
        }
        String type = computeActivation(vector) > threshold ? type2 : type1;
        String str = Arrays.toString(vector) + " " + type;
        System.out.println("Perceptron: "+str);
    }


    public void check(String filename, List<String> list) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        int lineNumber = 0;
        int count = 0;
        while ((line = bufferedReader.readLine())!=null){
            String trimLine = line.replaceAll("[\\[\\], ]", "").trim();
            String trimList = list.get(lineNumber).replaceAll("[\\[\\], ]", "").trim();
            if(trimList.equals(trimLine)){
                count++;
            }
            lineNumber++;
        }

        accuracy = ((double) count / lineNumber) * 100;
        System.out.println();
        System.out.println("Dokładność klasyfikacji: " + accuracy + "%");
    }

}
