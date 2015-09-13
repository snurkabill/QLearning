package examples.gridworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridWorldRunner {

    public static void main(String [] args) {
        int N = 5;

        List<GridCell> first = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            first.add(new GridCell(0, i, -5, true));
        }

        List<GridCell> second = new ArrayList<>();
        second.add(new GridCell(1, 0, -5, true));
        second.add(new GridCell(1, 1, 0, false));
        second.add(new GridCell(1, 2, -50, false));
        second.add(new GridCell(1, 3, 100, false));
        second.add(new GridCell(1, 4, -5, true));

        List<GridCell> third = new ArrayList<>();
        third.add(new GridCell(2, 0, -5, true));
        third.add(new GridCell(2, 1, 0, false));
        third.add(new GridCell(2, 2, -5, true));
        third.add(new GridCell(2, 3, 0, false));
        third.add(new GridCell(2, 4, -5, true));

        List<GridCell> fourth = new ArrayList<>();
        fourth.add(new GridCell(3, 0, -5, true));
        fourth.add(new GridCell(3, 1, 0, false));
        fourth.add(new GridCell(3, 2, 0, false));
        fourth.add(new GridCell(3, 3, 0, false));
        fourth.add(new GridCell(3, 4, -5, true));

        List<GridCell> fifth = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            fifth.add(new GridCell(4, i, -5, true));
        }

        GridWorld gridWorld = new GridWorld(Arrays.asList(new GridWorldAction(0), new GridWorldAction(1),
                new GridWorldAction(2), new GridWorldAction(3)), 0,
                0.1, 0.5, 0.5, Arrays.asList(fifth, second, third, fourth, fifth), 3, 1);

        gridWorld.run(1000, 100000, 0);
        gridWorld.setDiscountFactor(0);
        gridWorld.setLearningRate(0);
        gridWorld.setRandomFactor(0);
        gridWorld.run(100, 100);


    }
}
