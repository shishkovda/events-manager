package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import com.solution.eventsmanager.utils.HttpRequestor;
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
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Long userId = Long.valueOf(jsonObject.get("userId").getAsString());
        Long eventId = Long.valueOf(jsonObject.get("eventId").getAsString());
        Long templateId = userEventsTemplateService.getUsersEventsTemplateByEventIdAndUserId(eventId, userId).getTemplateId();

        jsonObject.remove("eventId");
        jsonObject.remove("userId");
        jsonObject.addProperty("templateId", templateId.toString());

        HttpRequestor httpRequestor = new HttpRequestor();

        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonObject.toString());
        String responseBody = httpRequestor.sendRequest("/api/messages", body, "POST");
    }

    @GetMapping("/{eventId}/{userId}")
    public String getTemplate(@PathVariable String eventId, @PathVariable String userId){
        UsersEventsTemplate usersEventsTemplate = new UsersEventsTemplate();

        usersEventsTemplate = userEventsTemplateService
                .getUsersEventsTemplateByEventIdAndUserId(Long.valueOf(eventId), Long.valueOf(userId));

        Long templateId = usersEventsTemplate.getTemplateId();

        HttpRequestor httpRequestor = new HttpRequestor();

        String responseBody = httpRequestor.sendRequest("/api/templates/"+templateId.toString(),
                null, "GET");

        String result = responseBody;

        return result;
    }

}
