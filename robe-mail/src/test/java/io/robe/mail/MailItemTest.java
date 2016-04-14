package io.robe.mail;

import org.junit.FixMethodOrder;
import org.junit.Test;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.util.*;

import static org.junit.Assert.assertEquals;

@FixMethodOrder
public class MailItemTest {
    MailItem item = new MailItem();

    @Test
    public void getId() throws Exception {
        item.setId("id");
        assertEquals("id", item.getId());

    }

    @Test
    public void getTitle() throws Exception {
        item.setTitle("title");
        assertEquals("title", item.getTitle());
    }


    @Test
    public void getBody() throws Exception {
        item.setBody("body");
        assertEquals("body", item.getBody());
    }

    @Test
    public void getAttachments() throws Exception {
        List<DataSource> sources = new LinkedList<DataSource>();
        sources.add(new ByteArrayDataSource("attachment1", "text"));
        sources.add(new ByteArrayDataSource("attachment2", "text"));

        item.setAttachments(sources);
        assertEquals(sources, item.getAttachments());
    }

    @Test
    public void getSender() throws Exception {
        item.setSender("sender");
        assertEquals("sender", item.getSender());
    }


    @Test
    public void getReceivers() throws Exception {
        List<String> receivers = new LinkedList<>();
        receivers.add("receiver1");
        receivers.add("receiver2");
        item.setReceivers(receivers);
        assertEquals(receivers, item.getReceivers());
        receivers.add("receiver3");

        item.setReceivers("receiver1", "receiver2", "receiver3");
        assertEquals(receivers, item.getReceivers());

    }

    @Test
    public void getEvent() throws Exception {
        MailEvent event = new MailEvent() {
            @Override
            public void before(MailItem item) {

            }

            @Override
            public void success(MailItem item) {

            }

            @Override
            public void error(MailItem item, Exception e) {

            }
        };
        item.setEvent(event);
        assertEquals(event, item.getEvent());
    }

    @Test
    public void getHeaders() throws Exception {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("1", new String[]{"a", "b"});
        headers.put("2", new String[]{"c", "d"});

        item.setHeaders(headers);
        assertEquals(headers, item.getHeaders());

    }

    @Test
    public void constructorAndToString() throws Exception {
        MailItem item1 = new MailItem("title", "body", (DataSource) null, "sender", "receiver");
        MailItem item2 = new MailItem("title", "body", (DataSource) null , "sender", Arrays.asList("receiver"));
        assertEquals(item1, item2);

        MailItem item3 = new MailItem("title", "body", (List<DataSource>) null, "sender", "receiver");
        MailItem item4 = new MailItem("title", "body", (List<DataSource>) null , "sender", Arrays.asList("receiver"));
        assertEquals(item3, item4);
        item1.setId("1");
        item2.setId("1");
        assertEquals(item1.toString(),item2.toString());
    }
}