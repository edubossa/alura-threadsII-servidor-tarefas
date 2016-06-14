package br.com.alura.servidor;

import java.util.concurrent.BlockingQueue;

public class MessageListener implements Runnable {

	private BlockingQueue<String> filaComandos;

	public MessageListener(BlockingQueue<String> filaComandos) {
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {
        try {
            String comando = null;
            while ((comando = filaComandos.take()) != null) {
                System.out.println("MessageListener.run - consumindo comando " + comando + ", "
                        + Thread.currentThread().getName());
				Thread.sleep(10000);
			}

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
