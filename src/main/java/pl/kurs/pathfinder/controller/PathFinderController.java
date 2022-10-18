package pl.kurs.pathfinder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.pathfinder.aop.LogExecutionTime;
import pl.kurs.pathfinder.model.comand.CreatePointCommand;
import pl.kurs.pathfinder.service.PathFinderService;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/api/v1/paths")
@RestController
@RequiredArgsConstructor
public class PathFinderController {
    private final PathFinderService pathFinderService;

    @PostMapping
    @LogExecutionTime
    public ResponseEntity getPaths(@RequestBody @Valid CreatePointCommand command) {
        return ResponseEntity.ok(pathFinderService.getPath(command.getStart(), command.getEnd()));
    }

    @PostMapping("/image")
    @LogExecutionTime
    public ResponseEntity getPathsImage(@RequestBody @Valid CreatePointCommand command) throws IOException {
        byte[] bytes = pathFinderService.getPathWithImage(command.getStart(), command.getEnd());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
