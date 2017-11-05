import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
** PREPARA E INICIALIZA O PROGRAMA **
*/
public class Principal {
	public static boolean stop_log = false;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		ExecutorService executor_service = null;
		ExecutorService log_thread_service = null;
		PrintStream stream = null;
		PrintStream stream2 = System.out;
		Cliente[] clientes = null;
		 
		try {
			
			System.out.println("TEMPO QUE IRÁ DURAR A EXECUÇÃO DO PROGRAMA: "
					+ Constantes.tempo_programa / Constantes.fator + " SEGUNDOS");
			System.out.println("arquivo de saída: " + Constantes.full_path + " conterá toda a atividade de programa");
			System.out.println("\n***EXECUTANDO PROGRAMA E GERANDO O LOG DE SAÍDA, POR FAVOR AGUARDE...***");
			stream = new PrintStream(System.out);//new File(Constantes.full_path));
			Assento[] assentos = new Assento[Constantes.qtdade_assentos];
			clientes = new Cliente[Constantes.qtdade_clientes];
			//Runnable log_thread = new Salva_log(0,buffer);// thread log criada com id 0
			
			 executor_service = Executors.newFixedThreadPool(Constantes.qtdade_clientes);
			// log_thread_service = Executors.newFixedThreadPool(1);
			// inicializa os assentos:
			for (int i = 0; i < Constantes.qtdade_assentos; i++)
				assentos[i] = new Assento(i, stream);
			// inicializa os clientes:
			for (int i = 0; i < Constantes.qtdade_clientes; i++) {
				clientes[i] = new Cliente(i + 1, assentos,stream);
				executor_service.execute(clientes[i]);
			}

			Thread.sleep(Constantes.tempo_programa);
			
		} finally {
			
			// gambiarra pra garantir que as threads finalizarao direito..tipo um join
			Constantes.fim_programa = true;
			
			executor_service.shutdownNow();
			Thread.sleep(1000);
			System.setOut(stream2);
			System.out.println("PROGRAMA FINALIZADO COM SUCESSO.");
			System.out.println("VER LOG GERADO NO ARQUIVO: " + Constantes.full_path);
			Runtime.getRuntime().exit(0);
		}
	}
}
