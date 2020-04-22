package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.io.IOException;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    UserEventsTemplateService userEventsTemplateService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("")
    public void processMessage(@RequestBody String request){
        System.out.println("test: processMessage");
        UsersEventsTemplate usersEventsTemplate = new UsersEventsTemplate();

        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Long userId = Long.valueOf(jsonObject.get("userId").getAsString());
        Long eventId = Long.valueOf(jsonObject.get("eventId").getAsString());
        Long templateId = userEventsTemplateService.getUsersEventsTemplateByEventIdAndUserId(eventId, userId).getTemplateId();

        jsonObject.remove("eventId");
        jsonObject.remove("userId");
        jsonObject.addProperty("templateId", templateId.toString());

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonObject.toString());
        Request newRequest = new Request.Builder()
                .url("http://84.201.175.97:8080/api/messages")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(newRequest).execute();
            String bodyResponse = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
