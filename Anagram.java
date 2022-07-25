import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Anagram {
    static class vocab {
        public String vocab;
        public String key_string;
    }

    private static vocab[] hash_table;
    private static int[] prime_arr = {101, 1009, 10009, 100003, 1000003, 10000019, 100000007};

    public static void produce_anagrams(String input) {//for producing anagrams of the given input string
        String temp = sort_string(input);
        int index = hash_function(temp, hash_table.length);
        int temp1 = index;
        int a = 0;
        int temp2 = -1;
        int flag = 1;
        while (hash_table[temp1].key_string != null && temp2 != index && hash_table[temp1].key_string.equals(temp)) {
            a++;
            System.out.println(hash_table[index].vocab);
            temp1 = (index + a * a) % hash_table.length;
            if (flag == 1) {
                flag = 0;
                continue;
            }
            temp2 = temp1;
        }
    }

    static String sort_string(String str) {//for sorting the string
        char arr[] = str.toCharArray();
        char temp;
        int i = 0;
        while (i < arr.length) {
            int j = i + 1;
            while (j < arr.length) {
                if (arr[j] < arr[i]) {
                    // Comparing the characters one by one
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
                j += 1;
            }
            i += 1;
        }
        return String.valueOf(arr);
    }

    static void sort_arrays(String[] array) {//radix sort for sorting the array of strings
        for (int digit = array.length; digit > 0; digit--) {
            countSort(array, array.length, digit - 1);
        }
    }

    static void countSort(String array[], int size, int pos) {//used in radix sort for sorting array of strings
        String[] b = null;
        int[] c = null;
        b = new String[size];
        c = new int[257];

        for (int i = 0; i < 257; i++) {
            c[i] = 0;
        }

        for (int j = 0; j < size; j++) {
            c[pos < array[j].length() ? (int) (char) array[j].charAt(pos) + 1 : 0]++;
        }

        for (int f = 1; f < 257; f++) {
            c[f] += c[f - 1];
        }

        for (int r = size - 1; r >= 0; r--) {
            b[c[pos < array[r].length() ? (int) (char) array[r].charAt(pos) + 1 : 0] - 1] = array[r];
            c[pos < array[r].length() ? (int) (char) array[r].charAt(pos) + 1 : 0]--;
        }

        for (int l = 0; l < size; l++) {
            array[l] = b[l];
        }
    }

    public static void create_lexo_arrays(String[] VocabTemp) {//for creating words using the words given in vocabulary
        String tempStr, rtk;
        int a;
        for (int j = 0; j < VocabTemp.length; j++) {//for one word
            rtk = VocabTemp[j];
            tempStr = sort_string(rtk);
            a = hash_key_val(tempStr, hash_table.length);
            hash_table[a].vocab = rtk;
            hash_table[a].key_string = tempStr;
        }
        for (int i = 0; i < VocabTemp.length; i++) {//for 2 words
            for (int j = 0; j < VocabTemp.length; j++) {
                if (i != j) {
                    rtk = VocabTemp[i] + " " + VocabTemp[j];
                    tempStr = sort_string(VocabTemp[i] + VocabTemp[j]);
                    a = hash_key_val(tempStr, hash_table.length);
                    hash_table[a].key_string = tempStr;
                    hash_table[a].vocab = rtk;
                }
            }
        }
        for (int k = 0; k < VocabTemp.length; k++) {//for 3 words
            for (int i = 0; i < VocabTemp.length; i++) {
                for (int j = 0; j < VocabTemp.length; j++) {
                    if (k != j && i != k && i != j) {
                        rtk = VocabTemp[k] + " " + VocabTemp[i] + " " + VocabTemp[j];
                        tempStr = sort_string(VocabTemp[k] + VocabTemp[i] + VocabTemp[j]);
                        a = hash_key_val(tempStr, hash_table.length);
                        hash_table[a].key_string = tempStr;
                        hash_table[a].vocab = rtk;
                    }
                }
            }
        }
    }

    public static int hash_key_val(String S, int m) {//for finding the key of the given String "S"
        int val1 = hash_function(S, m);
        if (hash_table[val1].key_string != null) {//error prone area problem when value repeats
            val1 = check_collision(val1);
        }
        return val1;
    }

    public static int hash_function(String s, int m) {//function for calculating the index of hash table
        int p = 31;
        int hash_value = 0;
        int p_pow = 1;
        for (int i = 0; i < s.length(); i++) {
            hash_value = (hash_value + (s.charAt(i) + 1) * p_pow) % m;
            p_pow = (p_pow * p) % m;
        }
        return hash_value;
    }

    public static int find_prime(int l) {//for finding the lenght of hashtable if quadratic probing leads to secondary clustering
        for (int i = 0; i < prime_arr.length; i++) {
            if (l < prime_arr[i]) {
                return prime_arr[i];
            }
        }
        return prime_arr[prime_arr.length - 1];
    }

    public static int check_collision(int hash_fun_val) {//function for checking collision
        int new_hash_val = hash_fun_val;
        int quad_probing = 1;
//        long a = new_hash_val;
        while (hash_table[new_hash_val].vocab != null && hash_table[new_hash_val].key_string != null && quad_probing < hash_table.length) {
            new_hash_val = (hash_fun_val + quad_probing * quad_probing) % hash_table.length;//for quadratic probing
            quad_probing++;
        }
        if (quad_probing >= hash_table.length) {//if secondary clustering occurs we will create new array having prime numbered size greater than the previous hash table
            int l = find_prime(hash_table.length);
            vocab[] new_hash_table = new vocab[hash_table.length];
            for (int i = 0; i < hash_table.length; i++) {
                new_hash_table[i] = new vocab();
                new_hash_table[i] = hash_table[i];
            }
            hash_table = new vocab[l];
            for (int i = 0; i < l; i++) {
                System.out.println("i is "+i);
                hash_table[i] = new vocab();
                hash_table[i].key_string = null;
                hash_table[i].vocab = null;
            }
            for(int rti = 0; rti < new_hash_table.length; rti++) {
                if (new_hash_table[rti].key_string != null&&new_hash_table[rti].vocab!=null) {//problem begins
                    int b = hash_key_val(new_hash_table[rti].key_string, l);
                    hash_table[b] = new_hash_table[rti];
                }
            }
            new_hash_val = check_collision(hash_fun_val);
        }
        return new_hash_val;
    }

    public static void main(String[] args) {
        File Vocabulary = new File("C:\\Users\\Dell\\Desktop\\2020MT1200838\\vocabulary.txt");
        try {
            Scanner sc = new Scanner(Vocabulary);
            int l = 0;
            if (sc.hasNextLine()) {
                l = Integer.parseInt(sc.nextLine());
            }
            String[] VocabTemp = new String[l];//vocabulary table
            hash_table = new vocab[101];//hash table having size 2 times size of vocabulary
            for (int k = 0; k < hash_table.length; k++) {
                hash_table[k] = new vocab();
            }
            int i = 0;
            while (sc.hasNextLine()) {
                String S = sc.nextLine();
                VocabTemp[i] = S;
                i++;
            }
            sort_arrays(VocabTemp);//sorted vocabulary in ascending order
            create_lexo_arrays(VocabTemp);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        File input = new File(args[1]);
//        try {
//            Scanner sc2 = new Scanner(input);
//            int l = 0;
//            if (sc2.hasNextLine()) {
//                l = Integer.parseInt(sc2.nextLine());
//            }
//            while (sc2.hasNextLine()) {
//                String inputString = sc2.nextLine();
                String inputString = "abc";
                produce_anagrams(inputString);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}


