import com.example.notificationservice.kafka.TaskEventConsumer;
import com.example.notificationservice.model.TaskEvent;
import com.example.notificationservice.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskEventConsumerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TaskEventConsumer taskEventConsumer;

    private static final Logger log = LoggerFactory.getLogger(TaskEventConsumer.class);

    @BeforeEach
    void setUp() {
        taskEventConsumer = new TaskEventConsumer(objectMapper, emailService);
    }

    @Test
    void testConsume() throws Exception {
        String recordValue = "{\"id\":1,\"name\":\"Test Task\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("task-events", 0, 0L, null, recordValue);
        TaskEvent taskEvent = TaskEvent.builder()
                                       .taskId(1)
                                       .title("Test Task")
                                       .action("CREATED")
                                       .emailId("test@example.com")
                                       .build();

        when(objectMapper.readValue(record.value(), TaskEvent.class)).thenReturn(taskEvent);

        taskEventConsumer.consume(record);

        verify(objectMapper, times(1)).readValue(record.value(), TaskEvent.class);
        verify(emailService, times(1)).sendTaskNotification(taskEvent);
    }

}