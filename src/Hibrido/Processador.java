package Hibrido;

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
	int NDeMsgsEnviadas;
	int NDeMsgsRecebidas;
	
	Processador(int limite, int id){
		ListaDeProcessosASerExecutados = new HashMap<Integer, Processo>();
		Limite = limite;
		ID = id;
		NDeMsgsEnviadas = 0;
		NDeMsgsRecebidas = 0;
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
			StatusFila = "Não Cheia";
		}
		return aux;
	}
	
	public void criaProcesso(Processo aux) {
		if(ListaDeProcessosASerExecutados.size()==0){
			NProcessoEmExecucao = var;
			StatusProcessador = "Executando";
			StatusFila = "Não Cheia";
			ListaDeProcessosASerExecutados.put(var++, aux);
		}else{
			ListaDeProcessosASerExecutados.put(var++, aux);
			if(ListaDeProcessosASerExecutados.size()>=Limite)
				StatusFila = "Cheia";
			else
				StatusFila = "Não Cheia";
		}
	}
	
	public String executa(){
		String aux = "";
		//System.out.println("Processador = "+ID+" Num de Processos = "+ListaDeProcessosASerExecutados.size());
		ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).TempoCPUNecessario--;
		//Se esse processo acabar, o processador deve avisar que está livre ou iniciar o próximo
		//processo.
		if(ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).TempoCPUNecessario==0){
			aux = "Procura";
			//System.out.println("Processo que deveria ser executado pela CPU "+ListaDeProcessosASerExecutados.get(NProcessoEmExecucao).NumeroCPUqueCriou+
			//		" foi executado por "+ID);
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
				StatusFila = "Não Cheia";	
		}
		return aux;
	}
	
}
