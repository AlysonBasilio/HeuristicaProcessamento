package Hibrido;

import java.util.Random;

public class Main {
	
	private static final int AMOUNT = 10;
	private static final int TMT = 10;
	private static final int N = 8;
	private static final int RETRY = 2;
	static int QtdDeProcessosEmExecucao = 0;
	
	static Processador[] processadores;
	static int NCiclosDeProcessamento = 300;
	static Random gerador = new Random ();
	static int metrica;
	
	/* Gerador de processos. A cada TMT clocks gera AMOUNT processos. */
	
	public static void geradorDeProcessos (int tempoDeClock) {
		int tempo = 0;
		int numCPU;
		for (int n = 0; n < AMOUNT; n++){
			tempo = (gerador.nextInt(TMT)+TMT/2);
			numCPU =  gerador.nextInt(N);
			Processo aux = new Processo(numCPU, tempoDeClock, tempo);
			processadores[numCPU].criaProcesso(aux);
			//System.out.println("Processo para a CPU " + numCPU + " de tempo " + tempo);
			QtdDeProcessosEmExecucao++;
		}
		//Atualiza métrica.
		metrica = 0;
		for(int i=0; i<N; i++){
			metrica+=processadores[i].ListaDeProcessosASerExecutados.size();
		}
		metrica=metrica/N;
		for(int i=0; i<N; i++){
			processadores[i].Limite=metrica+1;
		}
	}
	
	public static void main(String[] args) {
		processadores = new Processador[N];
		
		//Inicialização dos processadores.
		for(int i = 0; i<N; i++){
			processadores[i] = new Processador(AMOUNT/N,i);
		}
		
		//Aqui começa o processamento.		
		for(int i=0; i<NCiclosDeProcessamento; i++){
			System.out.println("Clock = "+i);
			
			//Verifica se algum processo será criado. Se for, adiciona o processo à sua CPU
			//e remove o processo da Lista.
			if(i%TMT==0){
				geradorDeProcessos(i);
			}
			
			//Aqui os processadores rodam um clock do processo em execução.
			for(int k = 0; k<N; k++){
				//System.out.println("Processador = "+k+" N de processos = "+processadores[k].ListaDeProcessosASerExecutados.size()+
				//		" Status da Fila = "+processadores[k].StatusFila);
				String s = "";
				if(processadores[k].StatusProcessador == "Executando")
					s = processadores[k].executa();
				//Aqui se o processador em questão está livre ou com espaço para processos na sua fila
				//ele deve procurar esvaziar a fila de processadores com a fila cheia.
				if(processadores[k].StatusProcessador=="Livre" ||(s=="Procura" && processadores[k].StatusFila=="Não Cheia")){
					int[] busca = new int[RETRY+1];
					busca[0]=k;
					for(int j = 0; j< RETRY; j++){
						int a = gerador.nextInt(N);
						while(JaPassouNesseP(a, busca, j)){
							a = gerador.nextInt(N);
						}
						busca[j+1] = a;
						processadores[k].NDeMsgsEnviadas++;
						processadores[a].NDeMsgsRecebidas++;
						if(processadores[a].StatusFila=="Cheia"){
							processadores[k].criaProcesso(processadores[a].passaProcesso());
							System.out.println("Passando Processo de "+a+" para "+k);
							break;
						}
					}
				} else if((s=="Procura" && processadores[k].StatusFila=="Cheia")){
					int[] busca = new int[RETRY+1];
					busca[0]=k;
					for(int j = 0; j< RETRY; j++){
						int a = gerador.nextInt(N);
						while(JaPassouNesseP(a, busca, j)){
							a = gerador.nextInt(N);
						}
						busca[j+1] = a;
						processadores[k].NDeMsgsEnviadas++;
						processadores[a].NDeMsgsRecebidas++;
						if(processadores[a].StatusFila=="Não Cheia"){
							processadores[a].criaProcesso(processadores[k].passaProcesso());
							System.out.println("Passando Processo de "+k+" para "+a);
							break;
						}
					}
				}
			}
		}
		int a=0,b=0;
		for(int i=0;i<N;i++){
			System.out.println("Processador "+i+": Msgs Enviadas = "+processadores[i].NDeMsgsEnviadas+"; Msgs Recebidas = "+processadores[i].NDeMsgsRecebidas);
			a+=processadores[i].NDeMsgsEnviadas;
			b+=processadores[i].NDeMsgsRecebidas;
		}
		System.out.println("Total Enviadas = "+a+" Total Recebidas = "+b);
	}

	private static boolean JaPassouNesseP(int a, int[] busca, int j) {
		for(int i=0; i<j; i++)
			if(a==busca[i])
				return true;
		return false;
	}

}
