package IniciadoPeloReceptor;

import java.util.HashMap;
import java.util.Map;

public class Processador{
	int ID;
	Map<Integer, Processo> ListaDeProcessosASerExecutados;
	String Status;
	int Limite;
	int NProcessoEmExecucao;
	int var = 0;
	
	Processador(int limite, int id){
		ListaDeProcessosASerExecutados = new HashMap<Integer, Processo>();
		Limite = limite;
		ID = id;
	}
	
	Processo passaProcesso(){
		Processo aux = null;
		for(int n:ListaDeProcessosASerExecutados.keySet()){
			if(n!=NProcessoEmExecucao){
				aux = ListaDeProcessosASerExecutados.get(n);
				ListaDeProcessosASerExecutados.remove(n);
			}
		}
		return aux;
	}
	
	public void criaProcesso(Processo pAux) {
		if(ListaDeProcessosASerExecutados.size()==0){
			NProcessoEmExecucao = var;
			Status = "Executando";
		}
		ListaDeProcessosASerExecutados.put(var++, pAux);
	}
	
	public void executa(){
		ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).TempoCPUNecessario--;
		if(ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).TempoCPUNecessario==0){
			System.out.println("Processo que deveria ser executado pela CPU "+ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).NumeroCPUqueCriou+
					" foi executado por "+ID);
			ListaDeProcessosASerExecutados.remove(NProcessoEmExecucao);
			if(ListaDeProcessosASerExecutados.isEmpty()){
				Status = "Livre";
			} else{
				for(int i:ListaDeProcessosASerExecutados.keySet()){
					NProcessoEmExecucao = i;
					return;
				}
			}
		}
	}
	
}
