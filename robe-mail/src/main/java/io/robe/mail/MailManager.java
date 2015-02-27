package io.robe.mail;

import io.robe.mail.queue.InMemoryMailQueue;
import io.robe.mail.queue.MailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A queue and consumer thread implementation.
 * Sender thread wakes up on every item insert and works until queue goes empty.
 * Every item in queue will be send only once. If any error occurs it is the developers responsibility to handle it. {@link io.robe.mail.MailEvent}.
 */
public class MailManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailManager.class);
    private static final MailQueue<MailItem> queue = new InMemoryMailQueue();
    private static MailSender sender;
    private static Thread consumerThread = new Thread();
    private static Runnable consumer = new Runnable() {
        @Override
        public void run() {
            LOGGER.debug("Starting mail thread.");

            // Check queue and continue until it is empty.
            while (!queue.isEmpty()) {

                // Take first and remove from queue
                MailItem item = queue.poll();
                LOGGER.info("Sending: " + item.getId() + " (" + queue.size() + ")");

                // Decide to call events.
                boolean hasEvent = item.getEvent() != null;
                if (hasEvent) {
                    item.getEvent().before(item);
                }
                try {
                    // Send it!
                    sender.sendMessage(item);
                    LOGGER.info("Success: " + item.getId() + " (" + queue.size() + ")");
                    if (hasEvent) {
                        item.getEvent().success(item);
                    }
                } catch (Exception e) {
                    LOGGER.info("Failed: " + item.getId() + " (" + queue.size() + ")");
                    LOGGER.debug(item.toString(), e);
                    if (hasEvent) {
                        item.getEvent().error(item, e);
                    }
                }
            }
            LOGGER.debug("Stopping mail thread.");


        }
    };

    static void setSender(MailSender sender) {
        MailManager.sender = sender;
    }

    public static boolean hasConfiguration() {
        try {
            return sender.getConfiguration() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Takes the mail item into the queue and manages the mail sender thread.
     * If thread is alive it will send the mail at the end of current thread queue.
     * Else a new thread will be created and started.
     *
     * @param item MailItem to send.
     * @return true if item added to the queue successfully.
     */
    public static boolean sendMail(MailItem item) {
        LOGGER.debug("Mail : " + item.toString());
        boolean result = queue.add(item);
        LOGGER.info("Adding mail to queue. Queue size: " + queue.size());

        // If thread is alive leave the job to it
        // Else create new thread and start.
        if (!consumerThread.isAlive()) {
            consumerThread = new Thread(consumer);
            consumerThread.start();
        }
        return result;
    }
}
