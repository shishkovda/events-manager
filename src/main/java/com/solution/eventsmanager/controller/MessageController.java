package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.EventView;
import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.service.EventService;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import com.solution.eventsmanager.service.UserService;
import com.solution.eventsmanager.utils.HttpRequestor;
import com.solution.eventsmanager.utils.ParserImpl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    UserEventsTemplateService userEventsTemplateService;

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("")
    public void processMessage(@RequestBody String request){
        logger.info("processMessage(): request= = " + request);

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

    String template = "VISA8872 11.05.20 Зачисление {attribute} руб. от отправителя {attribute} Сообщение:";

    @PostMapping("/bulk")
    public void processMessage2(@RequestBody String request){
        logger.info("processMessage2(): request= = " + request);

        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        String message = jsonObject.get("message").getAsString();

        ParserImpl parser = new ParserImpl();
        Integer sum = Integer.valueOf(parser.parse(message).get(0));
        String username = parser.parse(message).get(1);

        List<Event> events = eventService.getAllEvents();

        for(Event event:events){
            User user = userService.getUSerById(event.getAssignee());

            UsersEventsTemplate usersEventsTemplate = userEventsTemplateService.
                    getUsersEventsTemplateByEventIdAndUserId(event.getId(), event.getAssignee());

            if(usersEventsTemplate!=null){
                HttpRequestor httpRequestor = new HttpRequestor();

                String responseBody = httpRequestor.sendRequest("/api/templates/"+usersEventsTemplate.getTemplateId()+"/1",
                        null, "GET");
                String attrId = responseBody;

                responseBody = httpRequestor.sendRequest("/api/messages?templateId="+usersEventsTemplate.getTemplateId()+"&attrId="+attrId,
                        null, "GET");

                List<String> lastNames = new ArrayList<>();

                JsonParser jsonParser = new JsonParser();
                JsonElement tradeElement = jsonParser.parse(responseBody);
                JsonArray trade = tradeElement.getAsJsonArray();
                for (int i = 0; i < trade.size(); i++) {
                    lastNames.add(trade.get(i).getAsString());
                }

                if(!lastNames.contains(username.replaceAll(" ", "%20"))){
                    if(sum<300){
                        break;
                    }
                    message = message + "Сообщение: " + username.replaceAll(" ", "%20") + " " + event.getId();
                    sum-=300;
                    jsonObject.remove("message");
                    jsonObject.addProperty("message", message);
                    jsonObject.addProperty("userId", user.getId());
                    jsonObject.addProperty("eventId", event.getId());
                    processMessage(jsonObject.toString());
                }
            }
        }
    }

    @GetMapping("/{eventId}/{userId}")
    public String getTemplate(@PathVariable String eventId, @PathVariable String userId){
        logger.info("getTemplate(): eventId = " + eventId);
        logger.info("getTemplate(): userId = " + userId);

        UsersEventsTemplate usersEventsTemplate = new UsersEventsTemplate();

        usersEventsTemplate = userEventsTemplateService
                .getUsersEventsTemplateByEventIdAndUserId(Long.valueOf(eventId), Long.valueOf(userId));

        Long templateId = usersEventsTemplate.getTemplateId();

        HttpRequestor httpRequestor = new HttpRequestor();

        String responseBody = httpRequestor.sendRequest("/api/templates/"+templateId.toString(),
                null, "GET");

        String result = responseBody;

        logger.info("getTemplate(): result = " + result);

        return result;
    }

}
