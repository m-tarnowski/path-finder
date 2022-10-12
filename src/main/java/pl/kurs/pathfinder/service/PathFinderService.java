package pl.kurs.pathfinder.service;

import org.springframework.stereotype.Service;
import pl.kurs.pathfinder.exceptions.IllegalPlaceException;
import pl.kurs.pathfinder.exceptions.RoadNotFoundException;
import pl.kurs.pathfinder.model.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

@Service
public class PathFinderService {

    private static final int[][] STAGE = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
    };

    //    public List<Point> getPath(Point start, Point end, int m, int n) throws IllegalAccessException {
    public List<Point> getPath(Point start, Point end) {

//        int[][] stage = generateMap(m,n);

        int m = 4;
        int n = 17;

        if (start.getX() < 0 || start.getX() >= m || end.getX() < 0 || end.getX() >= m || start.getY() < 0 || start.getY() > n || end.getY() < 0 || end.getY() > n) {
            throw new IllegalPlaceException("Jedna z wartosci START/END znajduje sie poza plansza!");
        }
        if (STAGE[start.getX()][start.getY()] == 1 || STAGE[end.getX()][end.getY()] == 1) {
            throw new IllegalPlaceException("Niedozwolone miejsce startowe lub koncowe!");
        }
        if (start.equals(end)) {
            throw new RoadNotFoundException("Brak sciezki. Poczatek i koniec w tym samym miejscu!");
        }

        int[][] stage = addPotentialRoadToStage(start, end, m, n, STAGE);
//        stage = addPotentialRoadToStage(start, end, m, n, stage);


        List<Point> theBestRoad = new LinkedList<>();
        Point nextPoint = end;

        while (true) {
            //LEFT
            if (nextPoint.getX() >= 0 && nextPoint.getY() - 1 >= 0) {
                if (new Point(nextPoint.getX(), nextPoint.getY() - 1).equals(start)) {
                    theBestRoad.add(nextPoint);
                    theBestRoad.add(new Point(nextPoint.getX(), nextPoint.getY() - 1));
                    break;
                } else if (stage[nextPoint.getX()][nextPoint.getY() - 1] == stage[nextPoint.getX()][nextPoint.getY()] - 1) {
                    theBestRoad.add(nextPoint);
                    nextPoint = new Point(nextPoint.getX(), nextPoint.getY() - 1);
                    continue;
                }
            }
            //RIGHT
            if (nextPoint.getX() >= 0 && nextPoint.getY() + 1 >= 0 && nextPoint.getY() + 1 < n) {
                if (new Point(nextPoint.getX(), nextPoint.getY() + 1).equals(start)) {
                    theBestRoad.add(nextPoint);
                    theBestRoad.add(new Point(nextPoint.getX(), nextPoint.getY() + 1));
                    break;
                } else if (stage[nextPoint.getX()][nextPoint.getY() + 1] == stage[nextPoint.getX()][nextPoint.getY()] - 1) {
                    theBestRoad.add(nextPoint);
                    nextPoint = new Point(nextPoint.getX(), nextPoint.getY() + 1);
                    continue;
                }
            }
            //UP
            if (nextPoint.getX() - 1 >= 0 && nextPoint.getY() >= 0) {
                if (new Point(nextPoint.getX() - 1, nextPoint.getY()).equals(start)) {
                    theBestRoad.add(nextPoint);
                    theBestRoad.add(new Point(nextPoint.getX() - 1, nextPoint.getY()));
                    break;
                } else if (stage[nextPoint.getX() - 1][nextPoint.getY()] == stage[nextPoint.getX()][nextPoint.getY()] - 1) {
                    theBestRoad.add(nextPoint);
                    nextPoint = new Point(nextPoint.getX() - 1, nextPoint.getY());
                    continue;
                }
            }
            //DOWN
            if (nextPoint.getX() + 1 >= 0 && nextPoint.getX() + 1 < m && nextPoint.getY() >= 0) {
                if (new Point(nextPoint.getX() + 1, nextPoint.getY()).equals(start)) {
                    theBestRoad.add(nextPoint);
                    theBestRoad.add(new Point(nextPoint.getX() + 1, nextPoint.getY()));
                    break;
                } else if (stage[nextPoint.getX() + 1][nextPoint.getY()] == stage[nextPoint.getX()][nextPoint.getY()] - 1) {
                    theBestRoad.add(nextPoint);
                    nextPoint = new Point(nextPoint.getX() + 1, nextPoint.getY());
                }
            }
        }
        Collections.reverse(theBestRoad);
        return theBestRoad;
    }

    public byte[] getPathWithImage(Point start, Point end) throws IOException {
        BufferedImage image = new BufferedImage(17 * 30, 4 * 30, BufferedImage.TYPE_INT_RGB);
        Graphics2D g;
        g = image.createGraphics();

        List<Point> theBestRoad = getPath(start, end);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 17; j++) {
                if (STAGE[i][j] == 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * 30, i * 30, 30, 30);
                } else if (STAGE[i][j] == 1) {
                    g.setColor(Color.black);
                    g.fillRect(j * 30, i * 30, 30, 30);
                }
                if (theBestRoad.contains(new Point(i, j))) {
                    g.setColor(Color.green);
                    g.fillRect(j * 30, i * 30, 30, 30);
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    private int[][] addPotentialRoadToStage(Point start, Point end, int m, int n, int[][] oryginalStage) {
        List<Point> visited = new LinkedList<>();
        Queue<Point> pointQueue = new LinkedList<>(List.of(start));

        int[][] stage = new int[oryginalStage.length][];
        for (int i = 0; i < oryginalStage.length; i++)
            stage[i] = oryginalStage[i].clone();

        stage[start.getX()][start.getY()] = 2;

        while (!pointQueue.isEmpty()) {
            Point p = pointQueue.poll();
            //LEFT
            if (p.getX() >= 0 && p.getY() - 1 >= 0) {
                if (new Point(p.getX(), p.getY() - 1).equals(end)) {
                    visited.add(p);
                    visited.add(new Point(p.getX(), p.getY() - 1));
                    stage[p.getX()][p.getY() - 1] = stage[p.getX()][p.getY()] + 1;
                    break;
                } else if (stage[p.getX()][p.getY() - 1] == 0 && !visited.contains(new Point(p.getX(), p.getY() - 1))) {
                    pointQueue.add(new Point(p.getX(), p.getY() - 1));
                    stage[p.getX()][p.getY() - 1] = stage[p.getX()][p.getY()] + 1;
                }
            }
            //RIGHT
            if (p.getX() >= 0 && p.getY() + 1 >= 0 && p.getY() + 1 < n) {
                if (new Point(p.getX(), p.getY() + 1).equals(end)) {
                    visited.add(p);
                    visited.add(new Point(p.getX(), p.getY() + 1));
                    stage[p.getX()][p.getY() + 1] = stage[p.getX()][p.getY()] + 1;
                    break;
                } else if (stage[p.getX()][p.getY() + 1] == 0 && !visited.contains(new Point(p.getX(), p.getY() + 1))) {
                    pointQueue.add(new Point(p.getX(), p.getY() + 1));
                    stage[p.getX()][p.getY() + 1] = stage[p.getX()][p.getY()] + 1;
                }
            }
            //UP
            if (p.getX() - 1 >= 0 && p.getY() >= 0) {
                if (new Point(p.getX() - 1, p.getY()).equals(end)) {
                    visited.add(p);
                    visited.add(new Point(p.getX() - 1, p.getY()));
                    stage[p.getX() - 1][p.getY()] = stage[p.getX()][p.getY()] + 1;
                    break;
                } else if (stage[p.getX() - 1][p.getY()] == 0 && !visited.contains(new Point(p.getX() - 1, p.getY()))) {
                    pointQueue.add(new Point(p.getX() - 1, p.getY()));
                    stage[p.getX() - 1][p.getY()] = stage[p.getX()][p.getY()] + 1;
                }
            }
            //DOWN
            if (p.getX() + 1 >= 0 && p.getX() + 1 < m && p.getY() >= 0) {
                if (new Point(p.getX() + 1, p.getY()).equals(end)) {
                    visited.add(p);
                    visited.add(new Point(p.getX() + 1, p.getY()));
                    stage[p.getX() + 1][p.getY()] = stage[p.getX()][p.getY()] + 1;
                    break;
                } else if (stage[p.getX() + 1][p.getY()] == 0 && !visited.contains(new Point(p.getX() + 1, p.getY()))) {
                    pointQueue.add(new Point(p.getX() + 1, p.getY()));
                    stage[p.getX() + 1][p.getY()] = stage[p.getX()][p.getY()] + 1;
                }
            }
            visited.add(p);
        }
        if (!visited.contains(end)) {
            throw new RoadNotFoundException("Brak dostepnej sciezki!");
        }
        return stage;
    }

    private int[][] generateMap(int m, int n) {
        int[][] map = new int[m][n];
        Random random = new Random();

        for (int i = 0; i < m; i++) {
            map[i][0] = random.nextInt(2);
            for (int j = 0; j < n; j++) {
                map[i][j] = random.nextInt(2);
            }
        }
        return map;
    }
}
