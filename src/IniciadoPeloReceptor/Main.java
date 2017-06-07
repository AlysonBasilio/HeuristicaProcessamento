package IniciadoPeloReceptor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
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
		
		for(int i = 0; i<N; i++){
			processadores[i] = new Processador(2,i);
		}
		
		for(int i=0; i<NCiclosDeProcessamento; i++){
			System.out.println("Clock = "+i);
			Set<Integer> aux = new HashSet<Integer>();
			for(int j : ListaDeProcessos.keySet()){
				aux.add(j);
			}
			for(int j : aux){
				pAux = ListaDeProcessos.get(j);
				if(pAux.HoraCriacaoTarefa==i){
					processadores[pAux.NumeroCPUqueCriou].criaProcesso(pAux);
					ListaDeProcessos.remove(j);
				}
			}
			for(int k = 0; k<N; k++){
				if(processadores[k].Status == "Executando")
					processadores[k].executa();
			}
		}
		// TODO Auto-generated method stub
	}

}
