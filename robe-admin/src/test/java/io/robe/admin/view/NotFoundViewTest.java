package io.robe.admin.view;

import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hasanmumin on 10/10/2016.
 */
public class NotFoundViewTest extends BaseViewTest {

    @Test
    public void NotFoundView() throws IOException, TemplateException {

        String status = "Sorry your process is killed.";

        NotFoundView notFoundView = new NotFoundView(status);

        super.loadTemplate(notFoundView.getTemplateName());
        super.addParameter(notFoundView.getStatus().getName(), notFoundView.getStatus());

        String result = super.process();

        Assert.assertTrue(result.contains(status));
    }
}
