package io.robe.mail.queue;

import com.google.common.collect.Queues;
import io.robe.mail.MailItem;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A wrapper class for {@link java.util.concurrent.ConcurrentLinkedQueue}
 */
public class InMemoryMailQueue implements MailQueue<MailItem> {
	private ConcurrentLinkedQueue<MailItem> queue = Queues.<MailItem>newConcurrentLinkedQueue();

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean add(MailItem item) {
		return queue.add(item);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public MailItem peek() {
		return queue.peek();
	}

	@Override
	public MailItem poll() {
		return queue.poll();
	}

	@Override
	public boolean remove(MailItem item) {
		return queue.remove(item);
	}

	@Override
	public Iterator<MailItem> iterator() {
		return queue.iterator();
	}

}
