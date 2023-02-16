import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Reader {
    public static void main(String[] args) {
        char[][] levelline;
        int i,j;
        try {
            String path="/export/home/users1/licence/licence2/l2info/12101912/UEP/outlevels/1by1_clone_flower.txt";
            File file= new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(path));
            int c=0;

            while ((c=reader.read()) != -1) {
                
                System.out.println(c);

            }
        
        } catch (Exception e) {
            System.out.println("HSomething went wrong");
        }

    }

}

