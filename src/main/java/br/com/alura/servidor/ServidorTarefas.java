package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

	private ServerSocket servidor;
	private ExecutorService threadPool;
	private AtomicBoolean isAtivo;
	private BlockingQueue<String> filaComandos;

	public ServidorTarefas() throws IOException {
		System.out.println("---- Iniciando Servidor ----");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newCachedThreadPool(new FabricaDeThreads());
		this.isAtivo = new AtomicBoolean(true);
		this.filaComandos = new ArrayBlockingQueue<>(2);
		iniciarConsumidores(2);
	}

	private void iniciarConsumidores(int qtdCondumidores) {
		for (int i = 0; i < qtdCondumidores; i++) {
			MessageListener listener = new MessageListener(filaComandos);
			this.threadPool.execute(listener);
		}
	}

	public void start() throws IOException {
		while (this.isAtivo.get()) {
			try {
				Socket socket = this.servidor.accept();
				System.out.println("Aceitando novo cliente na porta " + socket.getPort());
				DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool, filaComandos, socket, this);
				this.threadPool.execute(distribuirTarefas);
			} catch (SocketException e) {
				System.out.println("SocketException, esta ativo? " + this.isAtivo);
			}

		}
	}

	public void stop() throws IOException {
		this.isAtivo.set(false);
		this.threadPool.shutdown();
		this.servidor.close();
	}

	public static void main(String[] args) throws Exception {
		ServidorTarefas servidor = new ServidorTarefas();
		servidor.start();
		servidor.stop();
	}

}
