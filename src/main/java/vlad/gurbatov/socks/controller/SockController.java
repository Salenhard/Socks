package vlad.gurbatov.socks.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.util.EnumUtils;
import vlad.gurbatov.socks.entity.Sock;
import vlad.gurbatov.socks.entity.dto.SockDto;
import vlad.gurbatov.socks.entity.dto.mapper.SockMapper;
import vlad.gurbatov.socks.service.SockService;
import vlad.gurbatov.socks.util.SortType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SockController {

    private final SockService sockService;
    private final SockMapper sockMapper;
    private final JobLauncher jobLauncher;
    private final Job job;
    private final String TEMP_STORAGE = "/tmp/";


    @PostMapping("/income")
    public ResponseEntity<?> income(@Valid @RequestBody SockDto dto) {
        Sock sock = sockMapper.toEntity(dto);
        return ResponseEntity.ok(sockMapper.toDto(sockService.income(sock)));
    }

    @PostMapping("/outcome")
    public ResponseEntity<?> outcome(@Valid @RequestBody SockDto dto) {
        Sock sock = sockMapper.toEntity(dto);
        return ResponseEntity.ok(sockMapper.toDto(sockService.outcome(sock)));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batch(@RequestParam("file") MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            File fileToImport = new File(TEMP_STORAGE + originalFileName);
            file.transferTo(fileToImport);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        sockService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Sock sock = sockService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sock with id `%d` not found".formatted(id)));
        SockDto sockDto = sockMapper.toDto(sock);
        return ResponseEntity.ok(sockDto);
    }

    @GetMapping
    public ResponseEntity<List<SockDto>> getAll(@RequestParam(required = false) String color,
                                                @RequestParam(required = false) Integer moreThenCotton,
                                                @RequestParam(required = false) Integer lessThenCotton,
                                                @RequestParam(required = false) Integer equalCotton,
                                                @RequestParam(required = false) String sortType) {
        List<Sock> socks = sockService.getAll(color, moreThenCotton, lessThenCotton, equalCotton, sortType);
        List<SockDto> sockDtos = socks.stream()
                .map(sockMapper::toDto)
                .toList();
        return ResponseEntity.ok(sockDtos);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody SockDto dto) {
        Sock resultSock = sockService.save(sockMapper.toEntity(dto));
        SockDto sockDto = sockMapper.toDto(resultSock);
        return ResponseEntity.ok(sockDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody SockDto dto) {
        Sock resultSock = sockService.update(id, sockMapper.toEntity(dto));
        SockDto sockDto = sockMapper.toDto(resultSock);
        return ResponseEntity.ok(sockDto);
    }
}

