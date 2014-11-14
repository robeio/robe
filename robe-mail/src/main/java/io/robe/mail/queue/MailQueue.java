package io.robe.mail.queue;


import java.util.Iterator;

public interface MailQueue<T> {

	public boolean add(T item);

	public boolean isEmpty();

	public T peek();

	public T poll();

	public boolean remove(T item);

	public Iterator<T> iterator();

	public int size();
}
