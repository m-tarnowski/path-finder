package pl.kurs.pathfinder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.pathfinder.model.comand.CreatePointCommand;
import pl.kurs.pathfinder.service.PathFinderService;

import javax.validation.Valid;

@RequestMapping("/api/v1/paths")
@RestController
@RequiredArgsConstructor
public class PathFinderController {
    private final PathFinderService pathFinderService;

    @PostMapping
    public ResponseEntity getPaths(@RequestBody @Valid CreatePointCommand command) {
        return ResponseEntity.ok(pathFinderService.getPath(command.getStart(), command.getEnd()));
    }
}
