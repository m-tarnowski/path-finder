package pl.kurs.pathfinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kurs.pathfinder.exceptions.IllegalPlaceException;
import pl.kurs.pathfinder.exceptions.RoadNotFoundException;
import pl.kurs.pathfinder.model.Point;
import pl.kurs.pathfinder.model.comand.CreatePointCommand;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PathFinderServiceTest {

    private static final int[][] STAGE = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
    };

    private PathFinderService pathFinderService;

    @BeforeEach
    void init() {
        pathFinderService = new PathFinderService();
    }


    @Test
    public void shouldExpectedRoadNotFoundExceptionWhenPointTheSame() {
        CreatePointCommand command = new CreatePointCommand();
        command.setStart(new Point(0, 1));
        command.setEnd(new Point(0, 1));

        assertThrows(RoadNotFoundException.class, () -> pathFinderService.getPath(command.getStart(), command.getEnd()));
    }

    @Test
    public void shouldExpectedIllegalPlaceExceptionWhenTheStartOrEndPointIsOffStage() {
        CreatePointCommand command = new CreatePointCommand();
        command.setStart(new Point(-2, 5));
        command.setEnd(new Point(0, 1));

        assertThrows(IllegalPlaceException.class, () -> pathFinderService.getPath(command.getStart(), command.getEnd()));
    }

    @Test
    public void shouldExpectedIllegalPlaceExceptionWhenTheStartOrEndIsForbiddenOnStage() {
        CreatePointCommand command = new CreatePointCommand();
        command.setStart(new Point(0, 0));
        command.setEnd(new Point(2, 2));

        assertThrows(IllegalPlaceException.class, () -> pathFinderService.getPath(command.getStart(), command.getEnd()));
    }

    @Test
    public void correctListWhenStartIs0_0AndEnd2_9() {
        List<Point> expected = new LinkedList<>(List.of(new Point(0, 0), new Point(0, 1), new Point(0, 2),
                new Point(0, 3), new Point(0, 4), new Point(0, 5), new Point(0, 6), new Point(0, 7),
                new Point(1, 7), new Point(2, 7), new Point(3, 7), new Point(3, 8), new Point(3, 9), new Point(2, 9)));
        List<Point> paths = pathFinderService.getPath(new Point(0, 0), new Point(2, 9));

        assertEquals(expected, paths);
    }

    @Test
    public void correctListWhenStartIs0_15AndEnd3_15() {
        List<Point> expected = new LinkedList<>(List.of(new Point(0, 15), new Point(1, 15), new Point(2, 15), new Point(3, 15)));
        List<Point> paths = pathFinderService.getPath(new Point(0, 15), new Point(3, 15));

        assertEquals(expected, paths);
    }
}