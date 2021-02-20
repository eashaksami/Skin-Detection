import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Double.parseDouble;

public class Test {
    public static void main(String[] args) throws IOException {
        double a,b,c,d;
        String s;
        BufferedReader br=new BufferedReader(new FileReader("F:\\iris.txt"));
        String line=null;
        while ((line=br.readLine())!=null)
        {
            String temp[]=line.split(",");
            a= parseDouble(temp[0]);
            b= parseDouble(temp[1]);
            c= parseDouble(temp[2]);
            d= parseDouble(temp[3]);
            s=temp[4];
            System.out.println(a+" "+b+" "+c+" "+d+" "+s);
        }
        br.close();
    }
}
