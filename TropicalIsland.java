import java.util.ArrayList;
import java.util.Scanner;

public class TropicalIsland {
    private class Data {
        private ArrayList<Island> Islands;

        public Data() {
            Islands = new ArrayList<>();
        }

        public void AddIsland(Island island) {
            Islands.add(island);
        }

        public void Calculate() {
            Islands.forEach(i -> System.out.println(i.GetWaterAfterFlood()));
        }
    }

    private class Island {
        private static final int StuckCount = 4096;
        private Cell[][] Landscape;
        private int N;
        private int M;
        private int MaxHeight;

        private void setMaxHeight(int h) {
            if (h > MaxHeight) {
                MaxHeight = h;
            }
        }

        // весь остров уходит под воду
        private void Flood() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    // не обращаем внимание на периметр острова, т.к. вода оттуда стечет в любом случае
                    if (i != 0 && i != N - 1 && j != 0 && j != M - 1) {
                        Landscape[i][j].Water = MaxHeight - Landscape[i][j].height();
                    }
                }
            }
        }

        // остров поднимается из воды
        private boolean DryUp() {
            boolean isDry = true;

            int s = 1;
            int n = N - 1;
            int m = M - 1;

            // проходим по периметру внутренних прямоугольников направляясь в центр и даем воде стечь с краев
            while (s < n && s < m) {
                // todo: пустить циклы только по границам
                for (int i = s; i < n; i++) {
                    for (int j = s; j < m; j++) {
                        if (Landscape[i][j].Water > 0) {
                            // на верхней или левой или правой границе текущего прямоугольника
                            if ((i == s || j == s || j == m - 1)
                                    // выше есть место, чтобы вода могла стечь
                                    && Landscape[i][j].heightWithWater() > Landscape[i - 1][j].heightWithWater()) {
                                Landscape[i][j].Pour(Landscape[i - 1][j].heightWithWater());
                                isDry = false;
                            }
                            // на левой или верхней или нижней границе текущего прямоугольника
                            if ((j == s || i == s || i == n - 1)
                                    // слева есть место, чтобы вода могла стечь
                                    && Landscape[i][j].heightWithWater() > Landscape[i][j - 1].heightWithWater()) {
                                Landscape[i][j].Pour(Landscape[i][j - 1].heightWithWater());
                                isDry = false;
                            }
                            // на нижней или левой или правой границе текущего прямоугольника
                            if ((i == n - 1 || j == s || j == m - 1)
                                    // ниже есть место, чтобы вода могла стечь
                                    && Landscape[i][j].heightWithWater() > Landscape[i + 1][j].heightWithWater()) {
                                Landscape[i][j].Pour(Landscape[i + 1][j].heightWithWater());
                                isDry = false;
                            }
                            // на правой или верхней или нижней границе текущего прямоугольника
                            if ((j == m - 1 || i == s || i == n - 1)
                                    // справа есть место, чтобы вода могла стечь
                                    && Landscape[i][j].heightWithWater() > Landscape[i][j + 1].heightWithWater()) {
                                Landscape[i][j].Pour(Landscape[i][j + 1].heightWithWater());
                                isDry = false;
                            }
                        }
                    }
                }

                s++;
                n--;
                m--;
            }

            return isDry;
        }

        // ищем воду на острове
        private int FindForWater() {
            int v = 0;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    v += Landscape[i][j].Water;
                }
            }

            return v;
        }

        public Island(int n, int m) {
            N = n;
            M = m;
            Landscape = new Cell[N][M];
            MaxHeight = 0;
        }

        public void AddCell(int r, int c, int h) {
            Landscape[r][c] = new Cell(h);
            setMaxHeight(h);
        }

        public int GetWaterAfterFlood() {
            Flood();

            int k = 0;

            // поднимаем остров из воды, пока стекает вода
            while (!DryUp() && k < StuckCount) {
                k++;
            }

            if (k == StuckCount) {
                System.err.println("Maybe there is an error :(, but let's still try to get answer...");
            }

            return FindForWater();
        }

        // for debug purpose
        public void PrintLandscape() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    System.out.print(Landscape[i][j].height() + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

        // for debug purpose
        public void PrintLandscapeWithWater() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    System.out.print((Landscape[i][j].height() + Landscape[i][j].Water) + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private class Cell {
        private int Height;
        public int Water;

        public Cell(int h) {
            Height = h;
            Water = 0;
        }

        public int height() {
            return Height;
        }

        public int heightWithWater() {
            return Height + Water;
        }

        // вода стекает
        public void Pour(int h) {
            Water -= heightWithWater() - h;
            if (Water < 0) {
                Water = 0;
            }
        }
    }

    public static void main(String[] argvs) {
        TropicalIsland _this = new TropicalIsland();

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter parameters in a proper format:");

        int k = scan.nextInt();

        Data data = _this.new Data();

        for (int q = 0; q < k; q++) {
            int n = scan.nextInt();
            int m = scan.nextInt();

            if (n < 1 || n > 50) {
                System.err.println("Error: N should be in range [1,50]");
                System.exit(1);
            }

            if (m < 1 || m > 50) {
                System.err.println("Error: M should be in range [1,50]");
                System.exit(1);
            }

            Island island = _this.new Island(n, m);
            data.AddIsland(island);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int h = scan.nextInt();

                    if (h < 1 || h > 1000) {
                        System.err.println("Error: H should be in range [1,1000]");
                        System.exit(1);
                    }

                    island.AddCell(i, j, h);
                }
            }
        }

        scan.close();

        data.Calculate();
    }
}
