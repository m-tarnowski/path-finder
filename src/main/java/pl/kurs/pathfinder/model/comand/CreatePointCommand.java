package pl.kurs.pathfinder.model.comand;

import lombok.Data;
import pl.kurs.pathfinder.model.Point;

import javax.validation.constraints.NotNull;

@Data
public class CreatePointCommand {

    @NotNull(message = "START_NOT_NULL")
    private Point start;
    @NotNull(message = "END_NOT_NULL")
    private Point end;

}
