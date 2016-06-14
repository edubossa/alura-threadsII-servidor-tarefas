package br.com.alura.servidor;

import java.util.concurrent.ThreadFactory;

public class FabricaDeThreads implements ThreadFactory {

	private static int numero = 1;

	@Override
	public Thread newThread(Runnable r) {

        System.out.println("FabricaDeThreads.newThread - Thread Servidor Tarefas " + numero);
        Thread thread = new Thread(r, "Thread Servidor Tarefas " + numero);

		numero++;

		thread.setUncaughtExceptionHandler(new TratadorDeExcecao());

		return thread;
	}

}