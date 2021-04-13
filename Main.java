package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.println("    -- Ola, seja bem-vindo ao SAT-Solver :) --    ");
		System.out.println("         by: Daniel Bastos e Felipe Souza       ");
		System.out.println();
		System.out.println(
				"Antes de tudo, por favor, certifique-se de ter colocado as entradas que deseja testar\nna pasta \"entradas\" localizada no diretorio desse projeto.");
		System.out.println();
		System.out.print("Por favor, digite o número de arquivos que deseja testar: ");
		int entradas = Integer.parseInt(in.nextLine());
		System.out.println();
		
		System.out.println("     Os nomes dos arquivos que virao a seguir NAO precisam ser digitados com o formato, mas precisam estar na forma \".in\"");
		System.out.println("     Exemplo: Entrada1");
		System.out.println();
		String[] entraArquivos = new String[entradas];
		for (int i = 1; i <= entradas; i++) {
			System.out.print("Digite o nome do " + i + "º arquivo a ser utilizado como entrada: ");
			entraArquivos[i-1] = in.nextLine();
		}
		System.out.println();
		for (int i = 1; i <= entradas; i++) {
			try {
				FileReader arq = new FileReader("entradas\\" + entraArquivos[i-1] + ".in");
				BufferedReader lerArq = new BufferedReader(arq);

				String linha = lerArq.readLine();
				int times = Integer.parseInt(linha);
				int numTeste = 1, tt = 0, re = 0;

				// File arquivo = new File(
				// "C:\\Users\\Daniel\\Documents\\Eclipse\\LogiquinhaBasiquinha\\src\\main\\Resolucao.out"
				// );
				
				File arquivo = new File("saidas\\(" + entraArquivos[i-1] + ") Tabela.out");
				FileWriter fw = new FileWriter(arquivo);
				BufferedWriter bw = new BufferedWriter(fw);
				
//				File arquivo2 = new File("saidas\\Teste " + i + " (" + entraArquivos[i-1] + ") Resolucao.out");
				File arquivo2 = new File("saidas\\(" + entraArquivos[i-1] + ") Resolucao.out");
				FileWriter fw2 = new FileWriter(arquivo2);
				BufferedWriter bw2 = new BufferedWriter(fw2);
				while (times > 0) {
					linha = lerArq.readLine();
					if (linha.charAt(0) == 'T') {
						tt(linha.substring(3), numTeste, tt, bw);
						tt++;
					} else {
						re(linha.substring(3), numTeste, re, bw2);
						re++;
					}
					times--;
					numTeste++;
				}
				if (i > 1)
					System.out.println();
				System.out.printf("Teste %d completo com sucesso. :)\n", i);
				System.out.println("Os arquivos de saida foram salvos como:");
				System.out.println(" -> (" + entraArquivos[i-1] + ") Tabela.out");
				System.out.println(" -> (" + entraArquivos[i-1] + ") Resolucao.out");
				bw.close();
				fw.close();
				bw2.close();
				fw2.close();
				arq.close();
			} catch (IOException e) {
				System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
				System.err.printf("Teste %d falhou. :(\n", i);
			}
		}
		in.close();
	}

	public static void re(String linha, int numTeste, int realTime, BufferedWriter bw) throws IOException {
		linha = linha.replaceAll(" ", "");
		int parenteses = 0, positiva = 0;
		boolean fnc = true, horn = true;
		for (int i = 0; i < linha.length() && fnc; i++) {
			char chari = linha.charAt(i);
			if (chari == '(') {
				parenteses++;
			} else if (chari == ')') {
				parenteses--;
				if (parenteses == 0) {
					positiva = 0;
				}
			} else if (chari == '&' && parenteses > 0) {
				fnc = false;
			} else if (chari == '>' || linha.charAt(i) == '<') {
				fnc = false;
			} else if (chari == 'v' && parenteses > 1) {
				fnc = false;
			} else if (((chari == 'Q' || chari == 'P' || chari == 'R' || chari == 'S') && linha.charAt(i - 1) != '~')
					&& parenteses > 0) {
				positiva++;
				if (positiva > 1) {
					horn = false;
				}
			}
		}
		if (realTime > 0)
			bw.newLine();
		if (!fnc) {
			bw.write("Problema #" + numTeste);
			bw.newLine();
			bw.write("Não está na FNC.");
			bw.newLine();
		} else if (!horn) {
			bw.write("Problema #" + numTeste);
			bw.newLine();
			bw.write("Nem todas as cláusulas são de Horn.");
			bw.newLine();
		} else {
			boolean unitaria = false;
			linha = linha.replaceAll("\\)", "").replaceAll("\\(", "");
			String[] provisorio = linha.split("&");
			ArrayList<ArrayList<String>> clausulas = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < provisorio.length; i++) {
				String[] splitter = provisorio[i].split("v");
				clausulas.add(new ArrayList<String>());
				for (int k = 0; k < splitter.length; k++) {
					boolean naoTem = true;
					for (int j = 0; j < clausulas.get(i).size() && naoTem; j++) {
						if (clausulas.get(i).get(j).equals(splitter[k]))
							naoTem = false;
					}
					if (naoTem)
						clausulas.get(i).add(splitter[k]);
				}
				if (clausulas.get(i).size() == 1) {
					unitaria = true;
				}
			}
			if (!unitaria) {
				bw.write("Problema #" + numTeste);
				bw.newLine();
				bw.write("Sim, é satisfatível.");
				bw.newLine();
			} else {
				boolean isSat = true;
				ArrayList<String> tested = new ArrayList<>();
				for (int i = 0; i < clausulas.size() && isSat; i++) {
					if (clausulas.get(i).size() == 1) {
						String var = clausulas.get(i).get(0);
						boolean keepGoing = false;
						for (int l = 0; l < tested.size() && !keepGoing; l++) {
							if (var.equals(tested.get(l)))
								keepGoing = true;
						}
						if (keepGoing)
							continue;
						else
							tested.add(var);
						boolean falsa = false;
						if (var.length() == 2) {
							falsa = true;
							var = var.substring(1);
						}
						for (int j = 0; j < clausulas.size() && isSat; j++) {
							if (i == j) {
								continue;
							} else {
								boolean found = false;
								for (int k = 0; k < clausulas.get(j).size() && !found; k++) {
									if ((clausulas.get(j).get(k).equals(var) && falsa)
											|| (clausulas.get(j).get(k).equals("~" + var) && !falsa)) {
										found = true;
										if (clausulas.get(j).size() == 1) {
											isSat = false;
										} else {
											int position = clausulas.size();
											clausulas.add(new ArrayList<String>());
											for (int w = 0; w < clausulas.get(j).size(); w++) {
												if (w == k)
													continue;
												else
													clausulas.get(position).add(clausulas.get(j).get(w));
											}
										}
									}
								}
							}
						}
					}
				}
				if (isSat) {
					bw.write("Problema #" + numTeste);
					bw.newLine();
					bw.write("Sim, é satisfatível.");
					bw.newLine();
				} else {
					bw.write("Problema #" + numTeste);
					bw.newLine();
					bw.write("Não, não é satisfatível.");
					bw.newLine();
				}
			}
		}
	}

	public static void tt(String linha, int numTeste, int realTimes, BufferedWriter bw) throws IOException {
		boolean alltrue = true, allfalse = true;
		if (realTimes > 0)
			bw.newLine();
		bw.write("Problema #" + numTeste);
		bw.newLine();
		linha = linha.replaceAll(" ", "");
		ArrayList<String> temporario = new ArrayList<>();
		Stack<Integer> indexbegin = new Stack<Integer>();
		boolean p = false, q = false, r = false, s = false;
		if (linha.length() != 1) {
			for (int i = 0; i < linha.length(); i++) {
				if (linha.charAt(i) == '(') {
					indexbegin.push(i);
				} else if (linha.charAt(i) == ')') {
					temporario.add(linha.substring(indexbegin.pop(), i + 1));
				} else if (linha.charAt(i) == 'P')
					p = true;
				else if (linha.charAt(i) == 'Q')
					q = true;
				else if (linha.charAt(i) == 'R')
					r = true;
				else if (linha.charAt(i) == 'S')
					s = true;
			}
		} else {
			temporario.add(linha.charAt(0) + "");
			if (linha.charAt(0) == 'P')
				p = true;
			else if (linha.charAt(0) == 'Q')
				q = true;
			else if (linha.charAt(0) == 'R')
				r = true;
			else if (linha.charAt(0) == 'S')
				s = true;
		}
		String[] clauses = new String[temporario.size()];
		for (int i = 0; i < temporario.size(); i++) {
			clauses[i] = temporario.get(i);
		}
		MergeSort.sort(clauses, 0, clauses.length - 1);

		ArrayList<ArrayList<Integer>> containClauses = new ArrayList<>();
		for (int i = 0; i < clauses.length; i++) {
			for (int j = 0; j < clauses.length; j++) {
				if (i == 0)
					containClauses.add(new ArrayList<>());
				if (clauses[j].contains(clauses[i]) && i != j) {
					containClauses.get(j).add(i);
				}
			}
		}

		// THE RESOLUTION
		ArrayList<String> var = new ArrayList<>();
		if (p)
			var.add("p");
		if (q)
			var.add("q");
		if (r)
			var.add("r");
		if (s)
			var.add("s");

		for (int k = 0; k < var.size(); k++) {
			bw.write(var.get(k).toUpperCase() + " ");
		}
		bw.write("|");
		for (int k = 0; k < clauses.length; k++) {
			bw.append(" " + clauses[k] + " ");
		}

		Queue<String> nums = new LinkedList<String>();
		int rows = (int) Math.pow(2, var.size());
		for (int i = 0; i < rows; i++) {
			for (int j = var.size() - 1; j >= 0; j--) {
				String top = Integer.toString((i / (int) Math.pow(2, j)) % 2);
				nums.add(top);
			}
		}
		while (!nums.isEmpty()) {
			String atual = "";
			for (int i = 0; i < var.size(); i++)
				atual += nums.poll();
			String[] novaClausula = new String[clauses.length];
			String[] variables = new String[var.size()];
			String[] values = new String[clauses.length];
			for (int i = 0; i < variables.length; i++)
				variables[i] = atual.charAt(i) + "";
			for (int i = 0; i < clauses.length; i++) {
				novaClausula[i] = clauses[i];
				if (containClauses.get(i).size() > 0 && i > 0) {

					for (int w = containClauses.get(i).size() - 1; w > -1; w--)
						if (values[containClauses.get(i).get(w)] != null)
							novaClausula[i] = novaClausula[i].replaceAll(
									Pattern.quote(clauses[containClauses.get(i).get(w)]),
									values[containClauses.get(i).get(w)]);
				} else {

				}
				for (int l = 0; l < var.size(); l++) {
					for (int z = 0; z < novaClausula[i].length(); z++) {
						if (novaClausula[i].charAt(z) == var.get(l).toUpperCase().charAt(0)) {
							novaClausula[i] = novaClausula[i].replaceAll(var.get(l).toUpperCase(),
									variables[var.indexOf(var.get(l))]);
						}
					}
				}
				String claux = novaClausula[i].replaceAll("\\)", "").replaceAll("\\(", "");
				temporario.clear();
				indexbegin.clear();
				if (claux.length() > 1) {
					for (int j = 0; j < novaClausula[i].length(); j++) {
						if (novaClausula[i].charAt(j) == '(') {
							indexbegin.push(j);
						} else if (novaClausula[i].charAt(j) == ')') {
							claux = novaClausula[i].substring(indexbegin.peek(), j);
							values[i] = solveClaux(claux);
							if (indexbegin.peek() != 0 && novaClausula[i].charAt(indexbegin.pop() - 1) == '~') {
								values[i] = (Integer.parseInt(values[i]) + 1) % 2 + "";
							}
						}
					}
				} else {
					values[i] = solveClaux(claux);
				}
			}
			bw.newLine();
			for (int k = 0; k < var.size(); k++) {
				bw.write(variables[k] + " ");
			}
			bw.write("|");
			for (int k = 0; k < clauses.length; k++) {
				for (int space = 0; space < clauses[k].length() - 1; space++) {
					bw.write(" ");
				}
				bw.write(" " + values[k] + " ");
			}

			if (values[values.length - 1].equals("0")) {
				alltrue = false;
			} else {
				allfalse = false;
			}

		}
		bw.newLine();
		if (allfalse) {
			bw.write("Não, não é satisfatível.");
			bw.newLine();
		} else {
			bw.write("Sim, é satisfatível");
			if (alltrue) {
				bw.write(" e também é tautologia.");
			} else {
				bw.write(", mas é refutável.");
			}
			bw.newLine();
		}

	}

	public static String solveClaux(String claux) {
		if (claux.contains("~")) {
			return ((Integer.parseInt(claux.charAt(claux.indexOf("~") + 1) + "") + 1) % 2) + "";
		} else if (claux.contains(">")) {
			if (claux.charAt(claux.indexOf(">") - 1) == '0' || claux.charAt(claux.indexOf(">") + 1) == '1')
				return "1";
			else
				return "0";
		} else if (claux.contains("<")) {
			return (claux.charAt(claux.indexOf("<") - 1) == claux.charAt(claux.indexOf("<") + 1)) ? "1" : "0";
		} else if (claux.contains("&")) {
			return (claux.charAt(claux.indexOf("&") - 1) == '1' && claux.charAt(claux.indexOf("&") + 1) == '1') ? "1"
					: "0";
		} else if (claux.contains("v")) {
			return (claux.charAt(claux.indexOf("v") - 1) == '1' || claux.charAt(claux.indexOf("v") + 1) == '1') ? "1"
					: "0";
		} else {
			return claux;
		}
	}
}

class MergeSort {
	public static void merge(String arr[], int l, int m, int r) {
		int n1 = m - l + 1;
		int n2 = r - m;

		String L[] = new String[n1];
		String R[] = new String[n2];

		for (int i = 0; i < n1; ++i)
			L[i] = arr[l + i];
		for (int j = 0; j < n2; ++j)
			R[j] = arr[m + 1 + j];

		int i = 0, j = 0;
		int k = l;
		while (i < n1 && j < n2) {
			if (L[i].length() <= R[j].length()) {
				arr[k] = L[i];
				i++;
			} else {
				arr[k] = R[j];
				j++;
			}
			k++;
		}
		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}
		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}

	public static void sort(String arr[], int l, int r) {
		if (l < r) {
			int m = (l + r) / 2;
			sort(arr, l, m);
			sort(arr, m + 1, r);
			merge(arr, l, m, r);
		}
	}
}