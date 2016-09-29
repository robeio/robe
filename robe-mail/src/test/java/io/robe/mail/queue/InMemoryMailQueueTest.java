package io.robe.mail.queue;

import io.robe.mail.MailItem;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 29/09/16.
 */
public class InMemoryMailQueueTest {

    @Test
    public void queue() {
        InMemoryMailQueue queue = new InMemoryMailQueue();
        Assert.assertTrue(queue.isEmpty());
        Assert.assertTrue(queue.size() == 0);

        MailItem mailItem = new MailItem();
        mailItem.setId("1");

        queue.add(mailItem);

        Assert.assertTrue(!queue.isEmpty());
        Assert.assertTrue(queue.size() != 0);

        Assert.assertTrue(queue.peek().getId().equals(mailItem.getId()));

        Assert.assertTrue(queue.poll().getId().equals(mailItem.getId()));

        Assert.assertTrue(queue.isEmpty());
        Assert.assertTrue(queue.size() == 0);

        queue.add(mailItem);

        Assert.assertTrue(queue.iterator().next().getId().equals(mailItem.getId()));
        Assert.assertTrue(queue.remove(mailItem));
    }
}
