package IniciadoPeloReceptor;

import java.util.HashMap;
import java.util.Map;

public class Processador{
	int ID;
	Map<Integer, Processo> ListaDeProcessosASerExecutados;
	String StatusProcessador;
	String StatusFila;
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
		int n1 = 0;
		for(int n:ListaDeProcessosASerExecutados.keySet()){
			if(n!=NProcessoEmExecucao){
				aux = ListaDeProcessosASerExecutados.get(n);
				n1 = n;
			}
		}
		ListaDeProcessosASerExecutados.remove(n1);
		if(ListaDeProcessosASerExecutados.size()<Limite){
			StatusFila = "N�o Cheia";
		}
		return aux;
	}
	
	public void criaProcesso(Processo pAux) {
		if(ListaDeProcessosASerExecutados.size()==0){
			NProcessoEmExecucao = var;
			StatusProcessador = "Executando";
			StatusFila = "N�o Cheia";
			ListaDeProcessosASerExecutados.put(var++, pAux);
		}else{
			ListaDeProcessosASerExecutados.put(var++, pAux);
			if(ListaDeProcessosASerExecutados.size()>=Limite)
				StatusFila = "Cheia";
			else
				StatusFila = "N�o Cheia";
		}
	}
	
	public void executa(){
		ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).TempoCPUNecessario--;
		//Se esse processo acabar, o processador deve avisar que est� livre ou iniciar o pr�ximo
		//processo.
		if(ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).TempoCPUNecessario==0){
			System.out.println("Processo que deveria ser executado pela CPU "+ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).NumeroCPUqueCriou+
					" foi executado por "+ID);
			ListaDeProcessosASerExecutados.remove(NProcessoEmExecucao);
			if(ListaDeProcessosASerExecutados.size()==0){
				StatusProcessador = "Livre";
			} else{
				for(int i:ListaDeProcessosASerExecutados.keySet()){
					NProcessoEmExecucao = i;
					StatusProcessador = "Executando";
					break;
				}
			}
			if(ListaDeProcessosASerExecutados.size()<Limite)
				StatusFila = "N�o Cheia";	
		}
	}
	
}
