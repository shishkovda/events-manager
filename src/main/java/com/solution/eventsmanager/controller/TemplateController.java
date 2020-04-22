package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.representation.EventRepresentation;
import com.solution.eventsmanager.representation.UsersEventsTemplateRepresentation;
import com.solution.eventsmanager.service.EventService;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    UserEventsTemplateService userEventsTemplateService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("")
    public void processMessage(@RequestBody String request){
        UsersEventsTemplate usersEventsTemplate = new UsersEventsTemplate();

        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        usersEventsTemplate.setUserId(Long.valueOf(jsonObject.get("userId").getAsString()));
        usersEventsTemplate.setEventId(Long.valueOf(jsonObject.get("eventId").getAsString()));

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, request);
        Request newRequest = new Request.Builder()
                .url("http://84.201.175.97:8080/api/templates")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(newRequest).execute();
            String bodyResponse = response.body().string();
            usersEventsTemplate.setTemplateId(Long.valueOf(bodyResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }

        userEventsTemplateService.createUserEventsTemplate(usersEventsTemplate);
    }

    @GetMapping("/{eventId}/{userId}")
    public String getTemplate(@PathVariable String eventId, @PathVariable String userId){
        UsersEventsTemplate usersEventsTemplate = new UsersEventsTemplate();

//        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
//        Long userId = Long.valueOf(jsonObject.get("userId").getAsString());
//        Long eventId = Long.valueOf(jsonObject.get("eventId").getAsString());

        usersEventsTemplate = userEventsTemplateService
                .getUsersEventsTemplateByEventIdAndUserId(Long.valueOf(eventId), Long.valueOf(userId));

        Long templateId = usersEventsTemplate.getTemplateId();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request newRequest = new Request.Builder()
                .url("http://84.201.175.97:8080/api/templates/"+templateId.toString())
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        String result = null;
        try {
            Response response = client.newCall(newRequest).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
