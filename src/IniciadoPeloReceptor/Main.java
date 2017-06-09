package IniciadoPeloReceptor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
	private static final int LimiteDeProcessosPorProcessador = 3;
	static int N = 4;
	static Processador[] processadores;
	static int NCiclosDeProcessamento = 42;
	public static void main(String[] args) {
		Map<Integer,Processo> ListaDeProcessos = new HashMap<Integer,Processo>();
		processadores = new Processador[N];
		int ProcessosPendentes = 0;
		Processo pAux;
		
		try{
			FileReader arq = new FileReader("entrada.txt");
		    BufferedReader lerArq = new BufferedReader(arq);
		    String linha = lerArq.readLine();
		    while (linha != null) {
		    	pAux = new Processo(Integer.parseInt(linha.split(" ")[0]),Integer.parseInt(linha.split(" ")[1]),Integer.parseInt(linha.split(" ")[2]));
		    	ListaDeProcessos.put(ProcessosPendentes++, pAux);
		    	System.out.printf(pAux.HoraCriacaoTarefa+" "+pAux.NumeroCPUqueCriou+" "+pAux.TempoCPUNecessario+"\n");
		    	linha = lerArq.readLine(); // lê da segunda até a última linha
		    }
		    arq.close();
	    } catch (IOException e) {
	    	System.err.printf("Erro na abertura do arquivo: %s.\n",
	        e.getMessage());
	    }
		
		//Inicialização dos processadores.
		for(int i = 0; i<N; i++){
			processadores[i] = new Processador(LimiteDeProcessosPorProcessador,i);
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
