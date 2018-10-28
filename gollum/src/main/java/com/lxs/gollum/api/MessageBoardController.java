package com.lxs.gollum.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.lxs.gollum.api.vo.Comment;
import com.lxs.gollum.api.vo.Response;
import com.lxs.gollum.api.vo.Status;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author liuxinsi
 * @mail akalxs@gmail.com
 */
@Log4j2
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(path = "/api/mb/v1")
public class MessageBoardController {
    private final ObjectMapper objectMapper;
//    private final KafkaTemplate<String, Comment> kafkaTemplate;

    private static CopyOnWriteArrayList<Comment> messageBoards = new CopyOnWriteArrayList<>();

    @Autowired
    public MessageBoardController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

//    @Autowired
//    public MessageBoardController(KafkaTemplate<String, Comment> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    private File getCommentFile() {
        return new File(System.getProperty("user.dir"), "comments.json");
    }

    @GetMapping("/loadComments")
    public Mono<Response> loadComments() {
        File file = getCommentFile();
        if (file.exists()&&messageBoards.isEmpty()) {
            try {
                messageBoards = objectMapper.readValue(getCommentFile(), new TypeReference<CopyOnWriteArrayList<Comment>>() {
                });
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return Mono.just(new Response<>(Status.SUCCESS, messageBoards));
    }

    @PostMapping(value = "/addComment", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Response> addComment(@Valid @RequestBody Comment comment) {
        comment.setComment(HtmlUtils.htmlEscape(comment.getComment()));
        if (Strings.isNullOrEmpty(comment.getName())) {
            comment.setName("匿名");
        } else {
            comment.setName(HtmlUtils.htmlEscape(comment.getName()));
        }
        comment.setMail(HtmlUtils.htmlEscape(comment.getMail()));
        comment.setDate(new Date());

//        kafkaTemplate.send("mb", comment);
        messageBoards.add(comment);

        try {
            objectMapper.writeValue(getCommentFile(), messageBoards);
        } catch (IOException e) {
            return Mono.just(new Response<>(Status.ERROR, e.getMessage()));
        }
        return Mono.just(new Response<>(Status.SUCCESS, "Success"));
    }

//    @KafkaListener(topicPartitions = {@TopicPartition(topic = "mb", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
//    public void listen(ConsumerRecord<String, Comment> record) {
//        Optional<Comment> kafkaMessage = Optional.ofNullable(record.value());
//
//        kafkaMessage.ifPresent(o -> messageBoards.add(o));
//    }

}
