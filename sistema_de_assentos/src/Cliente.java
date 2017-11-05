import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

/*
** DEFINE CLASSE CLIENTE **
*/
public class Cliente implements Runnable {
	private int id;
	private Random random = new Random();
	private Assento[] assentos;// guardará todos os assentos
	private Integer[] assentos_livres = new Integer[Constantes.qtdade_assentos];// guardará os indices dos assentos

	public Cliente(int id, Assento[] assentos, PrintStream stream) throws FileNotFoundException {
		this.id = id;
		this.assentos = assentos;
		System.setOut(stream);

	}

	public void run() {
		while (!Constantes.fim_programa) {
			int acao = random.nextInt(3);

			if (acao == ACAO.RESERVAR_ASSENTO_ALEATORIO.getValor()) {
				// sorteia qualquer assento, livre ou nao:
				int numero_assento = random.nextInt(Constantes.qtdade_assentos);
				try {
					System.out.println("o " + this + " está tentando reservar o assento " + numero_assento);
					if (assentos[numero_assento].reserva(this)) {
						// ocupará o assento por um tempo aleatório:
						reserva_assento(numero_assento);

						// liberará o assento após esse tempo:
						assentos[numero_assento].libera(this);
					} else
						assento_ocupado(numero_assento);
				} catch (InterruptedException e) {
				}
			} else if (acao == ACAO.RESERVAR_ASSENTO_livre.getValor()) {
				// sorteia algum dos assentos livres:

				Integer indice_assento = random.nextInt(Constantes.qtdade_assentos);
				checa_assentos_livres();
				while (assentos_livres[indice_assento] == null) {
					// System.out.println("debugaaao " + indice_assento);
					indice_assento = random.nextInt(Constantes.qtdade_assentos);
				}
				// indice_assento = assentos_livres[indice_assento];
				try {
					if (assentos[assentos_livres[indice_assento]].reserva(this)) {
						System.out.println("o " + this + " irá reservar o assento livre " + indice_assento);
						// ocupará o assento por um tempo aleatório:
						// reserva_assento_livre(indice_assento);
						reserva_assento(indice_assento);
						// liberará o assento ou ocupará até o final do programa:
						assentos[indice_assento].libera(this);

					} else// NUNCA ESTAR'MAS ENFIM...
						assento_ocupado(indice_assento);
				} catch (InterruptedException e) {
				}
			} else if (acao == ACAO.VER_MAPA_ASSENTOS.getValor()) {
				Integer assento_alvo = random.nextInt(Constantes.qtdade_assentos);
				try {
					System.out.println(this + " irá visualizar mapa de assentos: ");
					// escreve_log(mensagem);
					for (int i = 0; i < 55; i++)
						System.out.print("-------------");
					assentos[assento_alvo].mostra_mapa(assentos, this);

				} catch (InterruptedException e) {
				}
			}
		}
	}

	public String toString() {
		return "cliente " + id;
	}

	public int get_id() {
		return id;
	}

	public void reserva_assento(int numero_assento) throws InterruptedException {
		if (!Constantes.fim_programa) {

			System.out.println("assento " + numero_assento + " reservado com sucesso pelo " + this);
			// mensagem = "assento " + numero_assento + " reservado com sucesso pelo " +
			Thread.sleep(random.nextInt(10000));
		}
	}

	public void reserva_assento_livre(int numero_assento) throws InterruptedException {
		if (!Constantes.fim_programa) {
			System.out.println("assento livre " + numero_assento + " será reservado pelo " + this);

			Thread.sleep(random.nextInt(10000));

		}

	}

	public void assento_ocupado(int numero_assento) throws InterruptedException {
		if (!Constantes.fim_programa) {
			System.out.println(this + " não conseguiu reservar o assento " + numero_assento
					+ " pois o mesmo está ocupado pelo cliente " + assentos[numero_assento].get_cliente_id());
		}

	}

	public void checa_assentos_livres() {
		for (int i = 0; i < Constantes.qtdade_assentos; i++) {
			if (assentos[i].get_status() == 0) // se o assento estiver livre
				assentos_livres[i] = assentos[i].get_id();
		}
	}

}
