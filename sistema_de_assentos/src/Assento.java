import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
** DEFINE A CLASSE ASSENTO **
*/
public class Assento {
	private int id;// id do assento
	private int cliente_id;// id do cliente que reservou o assento
	private Lock lock;
	private int status = 0;// status do assento: 0 = livre 1 = ocupado
	private static ConcurrentMap<Integer, Integer> map;
	private PrintStream stream;
	private PrintStream console = System.out;
	static {
		map = new ConcurrentHashMap<>(Constantes.qtdade_assentos);
		for (int i = 0; i < Constantes.qtdade_assentos; i++)
			map.put(i, 0);// inicializa o mapa com todos os assentos livres
	}

	public Assento(int id, PrintStream stream) {
		this.id = id;
		lock = new ReentrantLock();
		this.stream = stream;
		System.setOut(console);
	}

	public boolean reserva(Cliente cliente) throws InterruptedException {
		if (lock.tryLock(30, TimeUnit.MILLISECONDS)) {// tempo que um cliente tentará reservar
			// System.out.println("o " + cliente + " está tentando reservar o assento " +
			// id);
			cliente_id = cliente.get_id();
			status = 1;
			map.put(id, 1);
			return true;
		}

		return false;
	}

	public void libera(Cliente cliente) throws InterruptedException {
		lock.unlock();
		status = 0;
		map.put(id, 0);
		if (!Constantes.fim_programa) {
			System.out.println("o " + cliente + " liberou o assento " + id);

		}
	}

	public int get_cliente_id() {
		return cliente_id;
	}

	public int get_id() {
		return id;
	}

	public int get_status() {
		return status;
	}

	public  void mostra_mapa(Assento[] assentos, Cliente cliente) throws InterruptedException {
		System.setOut(stream);// DESCOMENTE PARA VER SAIDA IDEAL PARA USAR POSTERIORMENTE NO PARSE
		System.out.print("\n");
		System.out.print("	***MAPA DE ASSENTOS SOCILICATO POR*** " + cliente);
		System.out.print("\n");

		for (Integer i : map.keySet())
			System.out.print("assento:     " + assentos[i].id + "   	| ");
		System.out.print("\n");
		for (Integer i : map.keySet())
			System.out.print("status:      " + map.get(i) + "   	| ");
		System.out.print("\n");
		for (Integer i : map.keySet()) {
			if (map.get(i) != 0) {
				System.out.print("reservado por: " + assentos[i].cliente_id + "   	| ");
			} else
				System.out.print("reservado por: " + "--" + "   	| ");
		}
		System.out.print("\n");
		for (int i = 0; i < 55; i++)
			System.out.print("-------------");
		System.out.print("\n");

	}

}
