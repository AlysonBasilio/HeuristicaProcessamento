package IniciadoPeloEmissor;

import java.util.ArrayList;
import java.util.Random;

public class Computador {
	
	private static final int AMOUNT = 10;
	private static final int TMT = 100000000;
	private static final int N = 4;
	
	private static int numProcessos;
	private static int numProcessadores;
	private static int mediaProcessos;
	private static int numMensagens;
	static ArrayList<Processador> multiprocessador;
	static Random gerador = new Random ();
	
	/* Função que busca processador ocioso. */
	
	public static void vasculha (int indice) {
		for (int k = 0; k < numProcessadores; k++){
			if (k != indice){
				numMensagens++;
				if (multiprocessador.get(k).getNumProcessos() < mediaProcessos )
					multiprocessador.get(k).addProcesso(multiprocessador.get(indice).transfereProcesso());
			}
		}
	}
	
	/* Gerador de processos. A cada TMT clocks gera AMOUNT processos. */
	
	public static void geradorDeProcessos (int tempoDeClock) {
		int tempo = 0;
		int numCPU;
		for (int n = 0; n < AMOUNT; n++){
			tempo = (gerador.nextInt(20)+1)*1000000;
			numCPU =  (gerador.nextInt(numProcessadores)+1);
			Processo aux = new Processo(numCPU, tempoDeClock, tempo);
			multiprocessador.get(numCPU-1).addProcesso(aux);
			System.out.println("Processo para a CPU " + numCPU + " de tempo " + tempo);
			numProcessos++;
		}
	}
	
	/* Adiciona N processadores para compor o multiprocessador */
	
	public static void montaMultiprocessador (int numProc) {
		for (int n = 1; n <= numProc; n++) {
			Processador processador = new Processador (n);
			multiprocessador.add(processador);
		}
	}
	
	public static void main (String[] args) {
		multiprocessador = new ArrayList<Processador> (); 
		
		montaMultiprocessador (N);
		
		numProcessos = 0;
		numProcessadores = multiprocessador.size();
		numMensagens = 0;
		
		int gerarProcessos = TMT;
		
		for (int cont = 0; cont < 1000000000; cont++) {
			
			if (gerarProcessos == TMT) {
				System.out.println("Valor do contador :" + cont);
				geradorDeProcessos (cont);
				gerarProcessos = 0;
			}
			
			gerarProcessos++;
			
			for (int k = 0; k < numProcessadores; k++) 
				if (multiprocessador.get(k).getNumProcessos() > 0) 
					if (multiprocessador.get(k).processar() == false) {
						numProcessos -= 1;
						mediaProcessos = (int) Math.ceil(numProcessos/numProcessadores);
						if (multiprocessador.get(k).getNumProcessos() > mediaProcessos)
							vasculha (k);
					}
		}
		
		System.out.println("Numero de mensagens trocadas: " + numMensagens);
		
	}
}
