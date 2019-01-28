package nwHacks2019;
import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class testOfList {
	public static void main(String args[])
	{
		
    Scanner in = new Scanner(System.in);
		
		String[] frenchArray = new String[1000];
		String[] frenchRandom = new String[1000];
		Random rand = new Random();

		//french words
		frenchArray[0]=scanner.nextLine();
		frenchArray[1]=scanner.nextLine();
		frenchArray[2]=scanner.nextLine();
		frenchArray[3]=scanner.nextLine();
		
		int n = 0;
		int m = 0;
		
		//french choices
		for(int i=0;i<frenchArray.length;i++)
		{
			for(int j=0;j<3;j++)
			{
				StringBuilder myName = new StringBuilder(frenchArray[i]);
				n = rand.nextInt(frenchArray[i].length()) + 1 ;
				m = rand.nextInt(frenchArray[i].length()) + 1;
				n--;
				m--;
				char o = frenchArray[i].charAt(n);
				myName.setCharAt(n, frenchArray[i].charAt(m));
				myName.setCharAt(m, o);
				System.out.println(myName);
			}
		}
	  System.out.println();
    in.close();
		
		}
}
