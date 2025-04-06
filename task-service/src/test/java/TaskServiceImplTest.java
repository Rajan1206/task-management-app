import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.kafka.TaskEventProducer;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskEventProducer kafkaProducerService;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                   .id(1)
                   .title("Test Task")
                   .description("Test Description")
                   .userId(1)
                   .build();

        taskDto = TaskDto.builder()
                .id(1)
                .title("Test Task")
                .description("Test Description")
                .userId(1)
                .build();
    }

    @Test
    void createTask() {
        when(mapper.mapToEntity(any(TaskDto.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(mapper.mapToDto(any(Task.class))).thenReturn(taskDto);

        TaskDto createdTask = taskService.createTask(taskDto, "test@example.com");

        assertNotNull(createdTask);
        assertEquals(taskDto.getTitle(), createdTask.getTitle());
        verify(kafkaProducerService, times(1)).sendTaskEvent(any());
    }

    @Test
    void updateTask() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(mapper.mapToDto(any(Task.class))).thenReturn(taskDto);

        TaskDto updatedTask = taskService.updateTask(1, taskDto, "test@example.com");

        assertNotNull(updatedTask);
        assertEquals(taskDto.getTitle(), updatedTask.getTitle());
        verify(kafkaProducerService, times(1)).sendTaskEvent(any());
    }

    @Test
    void deleteTask() {
        when(taskRepository.findByIdAndUserId(anyInt(),anyInt())).thenReturn(Optional.of(task));

        taskService.deleteTask(1, 1);

        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    void getTaskById() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(mapper.mapToDto(any(Task.class))).thenReturn(taskDto);

        TaskDto foundTask = taskService.getTaskById(1);

        assertNotNull(foundTask);
        assertEquals(taskDto.getTitle(), foundTask.getTitle());
    }

    @Test
    void getAllTasks() {
        when(taskRepository.findAllByUserId(anyInt())).thenReturn(Optional.of(List.of(task)));
        when(mapper.mapToDto(any(Task.class))).thenReturn(taskDto);

        List<TaskDto> tasks = taskService.getAllTasks(1);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

}