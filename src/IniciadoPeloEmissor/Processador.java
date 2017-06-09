package IniciadoPeloEmissor;

import java.util.ArrayList;

public class Processador {
	private ArrayList<Processo> filaProcessos;
	private int ID;
	private int numProcessos;
	
	Processador (int id) {
		this.filaProcessos = new ArrayList<Processo> ();
		this.ID = id;
		numProcessos = 0;
	}
	
	/* Função que adiciona processos a lista de processos do processador */
	
	public void addProcesso (Processo p) {
		filaProcessos.add(p);
		numProcessos++;
	}
	
	public int getNumProcessos () {return this.numProcessos;}
	
	/* Enquanto o primeiro processo da fila de processos tem tempo de CPU restando
	   ele é processado. Quando ele é finalizado, é removido da lista de processos 
	   e é sinalizado o seu término retornando false. */
	
	public boolean processar () {
		if (filaProcessos.get(0).getTempo() > 0) {
			filaProcessos.get(0).processa();
			return true;
		}
		else {
			System.out.println("Processo do processador " + filaProcessos.get(0).getNumeroCPU() + 
					" processado pelo processador " + ID);
			filaProcessos.remove(0);
			numProcessos--;
			return false;
		}
	}

	/* Função para trnaferência de processos. Retira um processo da fila de 
	   processos e retorna o processo a ser transferido */
	
	public Processo transfereProcesso() {
		Processo procTransferido = filaProcessos.get(0);
		filaProcessos.remove(0);
		numProcessos--;
		return procTransferido;
	}
}
