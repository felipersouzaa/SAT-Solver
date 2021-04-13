package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ChangeText {

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(System.in);
			System.out.println("Digite o nome do arquivo que voce deseja converter:");
			String nome = in.nextLine();
			FileReader arq = new FileReader("C:\\Users\\Daniel\\Documents\\Eclipse\\LogiquinhaBasiquinha\\src\\main\\" + nome + ".out");
			BufferedReader lerArq = new BufferedReader(arq);

			System.out.println("Digite o nome que voce deseja para o novo arquivo convertido:");
			String X = in.nextLine();

			File arquivo = new File( "C:\\Users\\Daniel\\Documents\\Eclipse\\LogiquinhaBasiquinha\\src\\main\\" + X +".out" );
			FileWriter fw = new FileWriter( arquivo );
			BufferedWriter bw = new BufferedWriter( fw );

			String linha;
			while((linha = lerArq.readLine()) != null) {
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
