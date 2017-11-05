import java.nio.file.Path;
import java.nio.file.Paths;
/*
** DEFINE AS CONSTANTES DE PROGRAMA**
*/
public class Constantes {
		private Constantes() {
			
		}
		static Path currentRelativePath = Paths.get("");
		public static final String file_path = currentRelativePath.toAbsolutePath().toString();
		public static final int qtdade_assentos = 30;
		public static final int qtdade_clientes = 50;
		public static final int tempo_programa = 10000;//tempo de execução do programa (EM MILISSEGUNDOS )
		public static final int fator = 10000;
		public static volatile boolean fim_programa = false;
		public static final String arquivo_saida = "/res/log.txt";
		public static final String full_path = file_path + arquivo_saida;//"/res/log.txt";
	
}
