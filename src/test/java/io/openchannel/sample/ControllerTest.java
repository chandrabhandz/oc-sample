package io.openchannel.sample;

import io.openchannel.sample.controller.AppViewController;
import io.openchannel.sample.form.AppFormModel;
import io.openchannel.sample.service.OpenChannelService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(AppViewController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OpenChannelService openChannelService;

    @Test
    public void testCreateApp() throws Exception {
        AppFormModel appFormModel = new AppFormModel();
        appFormModel.setName("Sample");
        appFormModel.setSummary("SampleSummary");
        appFormModel.setDescription("SampleDescription");
        appFormModel.setCategory(Arrays.asList("Sample Category 1", "Sample Category 2"));
        appFormModel.setIcon("This is sample icon");
        appFormModel.setPublish(String.valueOf(Boolean.TRUE));
        appFormModel.setTncFlag(String.valueOf(Boolean.TRUE));
        mockMvc.perform(post("/apps/create").session(new MockHttpSession()).param("name", "sample" + new Random().nextInt()).param("publish", "true"));
        Assert.assertTrue(true);
    }

    @Test
    public void testDeleteApp() throws Exception {
        mockMvc.perform(get("/apps/delete/59aa5476f960873ee103bdba/1"));
    }

    @Test
    public void testGetApp() throws Exception {
        AppFormModel app = openChannelService.getApp("59aa5476f960873ee103bdba", "1");
        System.out.println(app.getCategory());
    }

}
