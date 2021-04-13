package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ChangeText2 {

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(System.in);
			String nome = in.nextLine();
			FileReader arq = new FileReader("C:\\Users\\Daniel\\Documents\\Eclipse\\LogiquinhaBasiquinha\\src\\main\\" + nome.substring(0, nome.length()-1) + ".out");
			BufferedReader lerArq = new BufferedReader(arq);

			int X = Integer.parseInt(nome.charAt(nome.length()-1) + "");

			File arquivo = new File( "C:\\Users\\Daniel\\Documents\\Eclipse\\LogiquinhaBasiquinha\\src\\main\\Mexido" + X +"(2).out" );
			FileWriter fw = new FileWriter( arquivo );
			BufferedWriter bw = new BufferedWriter( fw );

			String linha;
			while((linha = lerArq.readLine()) != null) {
//				linha = lerArq.readLine();
				bw.write(linha.replaceAll(" ", ""));
				bw.newLine();
			}
			
			bw.close();
			fw.close();
			arq.close();
			
			in.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

	}

}
