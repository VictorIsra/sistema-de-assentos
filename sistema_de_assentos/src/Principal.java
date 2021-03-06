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
		PrintStream stream = null;
		PrintStream console_stream = System.out;
		Cliente[] clientes = null;
		Assento[] assentos = null;

		try {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("TEMPO QUE IRÁ DURAR A EXECUÇÃO DO PROGRAMA: "
					+ Constantes.tempo_programa / Constantes.fator + " SEGUNDOS");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("número de assentoss: " + Constantes.qtdade_assentos);
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("número de threads clientes: " + Constantes.qtdade_clientes);
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("PARA MUDAR OS VALORES DE ENTRADA ALTERE AS CONSTANTES NA CLASSE " + "<Constantes.java>");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("arquivo de saída: " + Constantes.full_path + " conterá toda a atividade de programa");
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("EXECUTANDO PROGRAMA E GERANDO O LOG DE SAÍDA, POR FAVOR AGUARDE..");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
			// stream = new PrintStream(Constantes.full_path);       //SE DESCOMENTADO MOSTRATÁ A SAÍDA DAS THREADS EM UM ARQUIVO LOG
			stream = new PrintStream(console_stream);			 //SE DESCOMENTADO MOSTRARÁ A SAÍDA DAS THREADS NO PŔOPRIO CONSOLE DO ECLIPSE
			assentos = new Assento[Constantes.qtdade_assentos];
			clientes = new Cliente[Constantes.qtdade_clientes];
			// Runnable log_thread = new Salva_log(0,buffer);// thread log criada com id 0

			executor_service = Executors.newFixedThreadPool(Constantes.qtdade_clientes);
			// inicializa os assentos:
			for (int i = 0; i < Constantes.qtdade_assentos; i++)
				assentos[i] = new Assento(i, stream);
			// inicializa os clientes:
			for (int i = 0; i < Constantes.qtdade_clientes; i++) {
				clientes[i] = new Cliente(i + 1, assentos, stream);
				executor_service.execute(clientes[i]);
			}

			Thread.sleep(Constantes.tempo_programa);

		} finally {

			// gambiarra pra garantir que as threads finalizarao direito..tipo um join
			Constantes.fim_programa = true;

			executor_service.shutdownNow();
			Thread.sleep(1000);
			System.setOut(console_stream);
			System.out.println(
					"\n\n----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("PROGRAMA FINALIZADO COM SUCESSO!");
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(
					"PARA EXECUTAR NO MODO LOG DESCOMENTE A LINHA 36 E COMENTE A LINHA 37 DA CLASSE: Principal.java");
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(
					"VER LOG GERADO NO ARQUIVO: ( SOMENTE SE EXECUTOU PROGRAMA NO MODO LOG ) " + Constantes.full_path);
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------------------");
			System.setOut(stream);
			System.out.println("PROGRAMA FINALIZADO COM SUCESSO!");
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------------------");
			Runtime.getRuntime().exit(0);
		}
	}
}
