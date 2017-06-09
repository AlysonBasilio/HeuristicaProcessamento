package IniciadoPeloReceptor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {
	
	private static final int AMOUNT = 8;
	private static final int TMT = 50;
	private static final int N = 4;
	static int QtdDeProcessosEmExecucao = 0;
	
	static Processador[] processadores;
	static int NCiclosDeProcessamento = 42;
	static Random gerador = new Random ();
	
	/* Gerador de processos. A cada TMT clocks gera AMOUNT processos. */
	
	public static void geradorDeProcessos (int tempoDeClock) {
		int tempo = 0;
		int numCPU;
		for (int n = 0; n < AMOUNT; n++){
			tempo = (gerador.nextInt(20)+1)*1000000;
			numCPU =  (gerador.nextInt(N)+1);
			Processo aux = new Processo(numCPU, tempoDeClock, tempo);
			processadores[numCPU-1].criaProcesso(aux);
			System.out.println("Processo para a CPU " + numCPU + " de tempo " + tempo);
			QtdDeProcessosEmExecucao++;
		}
	}
	
	public static void main(String[] args) {
		Map<Integer,Processo> ListaDeProcessos = new HashMap<Integer,Processo>();
		processadores = new Processador[N];
		Processo pAux;
		
		//Inicialização dos processadores.
		for(int i = 0; i<N; i++){
			processadores[i] = new Processador(AMOUNT/N,i);
		}
		
		//Aqui começa o processamento.		
		for(int i=0; i<NCiclosDeProcessamento; i++){
			System.out.println("Clock = "+i);
			//Pega a keySet da lista de processos.
			Set<Integer> aux = new HashSet<Integer>();
			for(int j : ListaDeProcessos.keySet()){
				aux.add(j);
			}
			//Verifica se algum processo será criado. Se for, adiciona o processo à sua CPU
			//e remove o processo da Lista.
			for(int j : aux){
				pAux = ListaDeProcessos.get(j);
				if(pAux.HoraCriacaoTarefa==i){
					processadores[pAux.NumeroCPUqueCriou].criaProcesso(pAux);
					ListaDeProcessos.remove(j);
				}
			}
			//Aqui os processadores rodam um clock do processo em execução.
			for(int k = 0; k<N; k++){
				//System.out.println("Processador = "+k+" N de processos = "+processadores[k].ListaDeProcessosASerExecutados.size()+
				//		" Status da Fila = "+processadores[k].StatusFila);
				if(processadores[k].StatusProcessador == "Executando")
					processadores[k].executa();
				//Aqui se o processador em questão está livre ou com espaço para processos na sua fila
				//ele deve procurar esvaziar a fila de processadores com a fila cheia.
				if(processadores[k].StatusProcessador=="Livre" || processadores[k].StatusFila=="Não Cheia"){
					for(int j = 0; j< N; j++){
						if(j!=k && processadores[j].StatusFila=="Cheia"){
							processadores[k].criaProcesso(processadores[j].passaProcesso());
							System.out.println("Passando Processo de "+j+" para "+k);
							break;
						}
					}
				}
			}
		}
		// TODO Auto-generated method stub
	}

}
