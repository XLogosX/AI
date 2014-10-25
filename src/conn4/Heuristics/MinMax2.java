/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4.Heuristics;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author wa
 */
public class MinMax2{}
/*
    private int MAX_LEVEL;
    private Random ran = new Random();

    public MinMax2() {
        super();
        MAX_LEVEL = 8;
    }

    public MinMax2(int level) {
        super();
        if (level <= 0) {
            throw new RuntimeException("KIMinMax: level must be >= 1");
        }
        MAX_LEVEL = level;
    }

    @Override
    protected void requestMove() {
        if (lastCol == -1) {
            move(3);
            return;
        }

        int bestScore = Integer.MIN_VALUE;
        ArrayList<Integer> best = new ArrayList<Integer>();

        for (int x = 0; x < Field.WIDTH; ++x) {
            if (field.isColFull(x)) {
                continue;
            }

            int y = field.add(x, 1);

            int score;
            if (field.hasPlayerWon(1, x, y)) {
                score = 1;
            } else {
                score = minmax(field, 2, 1, x, y);
            }

            if (score > bestScore) {
                best.clear();
                best.add(x);
                bestScore = score;
            } else if (score == bestScore) {
                best.add(x);
            }

            field.set(x, y, 0);
        }

        if (bestScore == -1) {
            for (int x1 = 0; x1 < Field.WIDTH; ++x1) {
                if (field.isColFull(x1)) {
                    continue;
                }

                int y1 = field.add(x1, 1);
                int won = 0;
                for (int x2 = 0; x2 < Field.WIDTH; ++x2) {
                    if (field.isColFull(x2)) {
                        continue;
                    }

                    ++won;

                    int y2 = field.add(x2, 2);
                    if (!field.hasPlayerWon(2, x2, y2)) {
                        --won;
                    }
                    field.set(x2, y2, 0);
                }
                field.set(x1, y1, 0);

                if (won == 0) {
                    best.clear();
                    best.add(x1);
                    break;
                }
            }
        }

        if (best.size() == 1) {
            move(best.get(0));
        } else {
            move(best.get(ran.nextInt(best.size())));
        }
    }

    private int minmax(Field f, int player, int level, int lastX, int lastY) {
        if (level != MAX_LEVEL) {
            int bestScore = (player == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (int x = 0; x < Field.WIDTH; ++x) {
                if (f.isColFull(x)) {
                    continue;
                }

                int y = f.add(x, player);

                int score;
                if (f.hasPlayerWon(player, x, y)) {
                    score = (player == 1) ? 1 : -1;
                } else {
                    score = minmax(f, (player == 1) ? 2 : 1, level + 1, x, y);
                }

                if ((player == 1 && score > bestScore)
                        || (player == 2 && score < bestScore)) {
                    bestScore = score;
                }

                f.set(x, y, 0);

                if ((player == 1 && bestScore == 1)
                        || (player == 2 && bestScore == -1)) {
                    break;
                }
            }

            if (bestScore == Integer.MIN_VALUE || bestScore == Integer.MAX_VALUE) {
                return 0;
            } else {
                return bestScore;
            }
        } else {
            return 0;
            /*if (f.hasPlayerWon(player, lastX, lastY))
             return (player==1) ? 1 : -1;
             else
             return 0;*/
