import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnimalChess {
    static int tile[][] = new int[7][9];
    static int animal[][] = new int[7][9];
    static int animalHistory[][][] = new int[1000][7][9];
    static Scanner inputString = new Scanner(System.in);

    public static void main(String[] args) {
        printRule();
        help();
        start:
        while (true) {
            boolean player = true;           //初始化玩家
            int currentStep = 0;             //初始化当前步数
            int lastStep = 0;                //初始化最大步数
            //初始化游戏地图
            readMap();
            adjust();
            recordHistory(currentStep);
            System.out.println("游戏开始");
            //对游戏进程中的返回值分别进行操作
            switch (operation(player, currentStep, lastStep)) {
                case 0:
                    while (true) {
                        System.out.print("输入restart再来一局，输入exit退出程序");
                        String input = inputString.next();
                        if (input.equals("exit")) {
                            System.exit(0);
                        } else if (input.equals("restart")) {
                            continue start;
                        } else {
                            System.out.print("请输入规定指令！");
                            continue;
                        }
                    }
                case 1:
                    continue start;
                case 2:
                    System.out.print("游戏结束");
                    System.exit(0);
            }
        }
    }

    //输出斗兽棋规则
    public static void printRule() {
        Scanner ruleInput = null;
        try {
            ruleInput = new Scanner(new File("rule.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (ruleInput.hasNext()) {
            System.out.println(ruleInput.nextLine());
        }
    }

    //输出 help文档
    public static void help() {
        Scanner helpInput = null;
        try {
            helpInput = new Scanner(new File("help.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (helpInput.hasNext()) {
            System.out.println(helpInput.nextLine());
        }
        System.out.println(" ");
    }

    //读取 tile.txt 和 animal.txt
    public static void readMap() {
        Scanner tileInput = null;
        try {
            tileInput = new Scanner(new File("tile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner animalInput = null;
        try {
            animalInput = new Scanner(new File("animal.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <= 6; i++) {
            String string = tileInput.nextLine();
            for (int j = 0; j <= 8; j++) {
                tile[i][j] = Integer.parseInt(string.substring(j, j + 1));
            }
        }
        for (int i = 0; i <= 6; i++) {
            String string = animalInput.nextLine();
            for (int j = 0; j <= 8; j++) {
                animal[i][j] = Integer.parseInt(string.substring(j, j + 1));
            }
        }
    }

    //对animal数组中的数进行一些调整以便区分阵营
    public static void adjust() {
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 8; j++) {
                if (animal[i][j] != 0) {
                    if (j < 4) {
                        animal[i][j] = animal[i][j] + 10;
                    }
                    if (j > 4) {
                        animal[i][j] = animal[i][j] + 20;
                    }
                } else {
                    animal[i][j] = animal[i][j];
                }
            }
        }
    }

    //整合动物地图与地形地图，输出游戏地图
    public static void printMap() {
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 8; j++) {
                if (animal[i][j] == 0) {
                    printTile(i, j);
                } else {
                    printAnimal(i, j);
                }
            }
            System.out.println();
        }
    }

    //输出地形地图
    public static void printTile(int i, int j) {
        switch (tile[i][j]) {
            case 0:
                System.out.print(" 　 ");
                break;
            case 1:
                System.out.print(" 水 ");
                break;
            case 2:
            case 4:
                System.out.print(" 陷 ");
                break;
            case 3:
            case 5:
                System.out.print(" 家 ");
                break;
            default:
                System.out.println("wrong");
        }
    }

    //输出动物地图
    public static void printAnimal(int i, int j) {
        if (animal[i][j] / 10 == 1) {
            System.out.print(animal[i][j] % 10 + switchAnimal(i, j) + " ");
        } else if (animal[i][j] / 10 == 2) {
            System.out.print(" " + switchAnimal(i, j) + animal[i][j] % 10);
        } else {
            System.out.print(switchAnimal(i, j));
        }
    }

    //将动物名称与代表数字对应
    public static String switchAnimal(int i, int j) {
        switch (animal[i][j] % 10) {
            case 0:
                return (" 　 ");
            case 1:
                return ("鼠");
            case 2:
                return ("猫");
            case 3:
                return ("狼");
            case 4:
                return ("狗");
            case 5:
                return ("豹");
            case 6:
                return ("虎");
            case 7:
                return ("狮");
            case 8:
                return ("象");
            default:
                return ("wrong");
        }
    }

    //将动物地图记录到三维数组中，与对应步数匹配
    public static void recordHistory(int currentStep) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                animalHistory[currentStep][i][j] = animal[i][j];
            }
        }
    }

    //判断玩家输入的指令，并进行相应操作
    public static int operation(boolean player, int currentStep, int lastStep) {
        int camp;                       //己方阵营
        int camp_opposite;              //敌方阵营
        int trap;                       //己方陷阱
        int trap_opposite;              //敌方陷阱
        int home;                       //己方兽穴
        play:
        while (true) {
            printMap();
            //若分出胜负则返回0，否则继续游戏
            switch (victoryJudge()) {
                case 1:
                    return 0;
                case 0:
                    break;
            }
            //对不同玩家给予初始条件不同的赋值
            if (player) {
                System.out.println("左方玩家行动：");
                camp = 1;
                camp_opposite = 2;
                home = 3;
                trap = 2;
                trap_opposite = 4;
            } else {
                System.out.println("右方玩家行动：");
                camp = 2;
                camp_opposite = 1;
                home = 5;
                trap = 4;
                trap_opposite = 2;
            }
            String input = inputString.next();
            //判断各种指令并进行不同操作
            if (input.equals("help")) {
                help();
            } else if (input.equals("restart")) {
                return 1;
            } else if (input.equals("exit")) {
                return 2;
            } else if (input.equals("undo")) {
                if (currentStep - 1 >= 0) {
                    currentStep--;
                    player = !player;
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 9; j++) {
                            animal[i][j] = animalHistory[currentStep][i][j];
                        }
                    }
                } else {
                    System.out.println("已回到开局，不能再悔棋了");
                }
            } else if (input.equals("redo")) {
                if (currentStep + 1 <= lastStep) {
                    currentStep++;
                    player = !player;
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 9; j++) {
                            animal[i][j] = animalHistory[currentStep][i][j];
                        }
                    }
                } else {
                    System.out.println("棋盘已经是最新状态，不能再撤销悔棋了");
                }
            } else {
                if (input.length() != 2) {
                    System.out.println("指令错误！请输入正确的指令");
                } else {
                    //对移动指令进行判断及操作
                    int ani = input.charAt(0) - '0';
                    char dir = input.charAt(1);
                    if (ani == 0 || ani == 9) {
                        System.out.println(ani + "不对应任何动物");
                    } else if (ani > 0 && ani < 9) {
                        for (int i = 0; i <= 6; i++) {
                            for (int j = 0; j <= 8; j++) {
                                if (animal[i][j] / 10 == camp && ani == animal[i][j] % 10) {
                                    int x;          //行数i变化值
                                    int y;          //列数j变化值
                                    //根据移动方向不同会对x，y进行不同的赋值
                                    switch (dir) {
                                        case 'a':
                                            x = 0;
                                            y = -1;
                                            //移动返回1时，进行玩家交替以及animal数组记录
                                            if (move(x, y, i, j, home, camp, camp_opposite, trap, trap_opposite) == 1) {
                                                player = !player;
                                                currentStep++;
                                                lastStep = currentStep;
                                                recordHistory(currentStep);
                                            }
                                            continue play;
                                        case 'd':
                                            x = 0;
                                            y = 1;
                                            //移动返回1时，进行玩家交替以及animal数组记录
                                            if (move(x, y, i, j, home, camp, camp_opposite, trap, trap_opposite) == 1) {
                                                player = !player;
                                                currentStep++;
                                                lastStep = currentStep;
                                                recordHistory(currentStep);
                                            }
                                            continue play;
                                        case 's':
                                            x = 1;
                                            y = 0;
                                            //移动返回1时，进行玩家交替以及animal数组记录
                                            if (move(x, y, i, j, home, camp, camp_opposite, trap, trap_opposite) == 1) {
                                                player = !player;
                                                currentStep++;
                                                lastStep = currentStep;
                                                recordHistory(currentStep);
                                            }
                                            continue play;
                                        case 'w':
                                            x = -1;
                                            y = 0;
                                            //移动返回1时，进行玩家交替以及animal数组记录
                                            if (move(x, y, i, j, home, camp, camp_opposite, trap, trap_opposite) == 1) {
                                                player = !player;
                                                currentStep++;
                                                lastStep = currentStep;
                                                recordHistory(currentStep);
                                            }
                                            continue play;
                                        default:
                                            System.out.println(dir + "不代表任何方向");
                                            continue play;
                                    }
                                }
                            }
                        }
                        System.out.println("该动物已经被吃掉了，请选择另一种动物");
                    } else {
                        System.out.println("指令错误：首位不是数字");
                    }
                }
            }
        }
    }

    /**
     * 判断动物能否移动以及移动动物
     * 若动物成功移动返回1，否则返回0并告知不能移动的原因
     */
    public static int move(int x, int y, int i, int j, int home, int camp, int camp_opposite, int trap, int trap_opposite) {
        if (j + y < 0 || j + y > 8 || i + x < 0 || i + x > 6) {
            //优先判断是否走出边界
            System.out.println("不能走出边界");
        } else if (animal[i + x][j + y] / 10 == camp) {
            //其次判断是否会与己方单位重合
            //狮虎跳河与己方水中老鼠相邻情况进行狮虎跳河判断
            if (animal[i + x][j + y] % 10 == 1 && (animal[i][j] % 10 == 7 || animal[i][j] % 10 == 6) && tile[i + x][j + y] == 1) {
                if (jumpRiver(x, y, i, j, camp, camp_opposite) == 1) {
                    return 1;
                }
            } else {
                System.out.println("不能与己方单位重合");
            }
        } else if (tile[i + x][j + y] == trap) {
            //若将要移动至陷阱，则无视等级，直接进入
            animal[i + x][j + y] = animal[i][j];
            animal[i][j] = 0;
            return 1;
        } else if (tile[i + x][j + y] == home) {
            //判断是否会移动至己方兽穴
            System.out.println("不能进入自己家");
        } else if (animal[i][j] % 10 == 1) {
            //对老鼠移动进行判断
            if (tile[i][j] == 1 && animal[i + x][j + y] / 10 == camp_opposite) {
                //对水中老鼠移动，且目标位置有敌方单位时进行判断
                if (animal[i + x][j + y] % 10 == 8) {
                    System.out.println("小河中的鼠不能吃陆上的象");
                } else if (animal[i + x][j + y] % 10 == 1) {
                    animal[i + x][j + y] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                } else {
                    System.out.println("不能送死");
                }
            } else if (tile[i][j] == 0 && animal[i + x][j + y] / 10 == camp_opposite) {
                //对陆地老鼠移动，且目标位置有敌方单位时进行判断
                if (animal[i + x][j + y] % 10 == 8 || animal[i + x][j + y] % 10 == 1) {
                    animal[i + x][j + y] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                } else {
                    System.out.println("不能送死");
                }
            } else if (tile[i][j] == trap_opposite && animal[i + x][j + y] / 10 == camp_opposite) {
                //对在敌方陷阱中老鼠移动，且目标位置有敌方单位时进行判断
                if (animal[i + x][j + y] % 10 == 8 || animal[i + x][j + y] % 10 == 1) {
                    animal[i + x][j + y] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                } else {
                    System.out.println("不能送死");
                }
            } else {
                //目标位置无敌方单位，直接移动
                animal[i + x][j + y] = animal[i][j];
                animal[i][j] = 0;
                return 1;
            }
        } else if (animal[i][j] % 10 == 6 || animal[i][j] % 10 == 7) {
            //对狮虎移动进行判断
            if (tile[i + x][j + y] == 1) {
                //目标位置为水，进行狮虎跳河判断
                if (jumpRiver(x, y, i, j, camp, camp_opposite) == 1) {
                    return 1;
                }
            } else {
                //目标位置非水时的狮虎移动判断
                if (animal[i + x][j + y] % 10 > animal[i][j] % 10) {
                    System.out.println("不能送死");
                } else {
                    animal[i + x][j + y] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                }
            }
        } else {
            //对其他动物移动进行判断
            if (tile[i + x][j + y] == 1) {
                //目标位置为水，提示普通动物不能入水
                String name = switchAnimal(i, j);
                System.out.println(name + "不能下水");
            } else if (animal[i + x][j + y] / 10 == camp_opposite) {
                //目标位置有敌方单位，进行大小判断以及吃的操作
                if (animal[i][j] % 10 == 8 && animal[i + x][j + y] % 10 == 1) {
                    System.out.println("不能送死");
                } else if (animal[i][j] % 10 < animal[i + x][j + y] % 10) {
                    System.out.println("不能送死");
                } else {
                    animal[i + x][j + y] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                }
            } else {
                //目标位置无敌方单位，直接移动
                animal[i + x][j + y] = animal[i][j];
                animal[i][j] = 0;
                return 1;
            }
        }
        return 0;
    }

    /**
     * 判断狮虎跳河
     * 可以跳河返回1，否则返回0
     */
    public static int jumpRiver(int x, int y, int i, int j, int camp, int camp_opposite) {
        if (x == 0) {
            //狮虎横跳判断
            if ((animal[i][j + y] % 10 == 1 && animal[i][j + y] / 10 == camp_opposite) ||
                    (animal[i][j + 2 * y] % 10 == 1 && animal[i][j + 2 * y] / 10 == camp_opposite) ||
                    (animal[i][j + 3 * y] % 10 == 1 && animal[i][j + 3 * y] / 10 == camp_opposite)) {
                //路线上有敌方老鼠提示不能跳河
                System.out.println("有鼠阻挡，无法跳河");
            } else if (animal[i][j + 4 * y] / 10 == camp) {
                //判断跳河后是否会与己方单位重合
                System.out.println("不能与己方单位重合");
            } else {
                //判断跳河后是否会遇到比自己大的动物
                if (animal[i][j + 4 * y] % 10 > animal[i][j] % 10) {
                    System.out.println("不能送死");
                } else {
                    animal[i][j + 4 * y] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                }
            }
        } else {
            //狮虎竖跳判断
            if ((animal[i + x][j] % 10 == 1 && animal[i + x][j] / 10 == camp_opposite) ||
                    (animal[i + 2 * x][j] % 10 == 1 && animal[i + 2 * x][j] / 10 == camp_opposite)) {
                //路线上有敌方老鼠提示不能跳河
                System.out.println("有鼠阻挡，无法跳河");
            } else if (animal[i + 3 * x][j] / 10 == camp) {
                //判断跳河后是否会与己方单位重合
                System.out.println("不能与己方单位重合");
            } else {
                //判断跳河后是否会遇到比自己大的动物
                if (animal[i + 3 * x][j] % 10 > animal[i][j] % 10) {
                    System.out.println("不能送死");
                } else {
                    animal[i + 3 * x][j] = animal[i][j];
                    animal[i][j] = 0;
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * 用于胜负判断
     * 分出胜负返回1，否则返回0
     */
    public static int victoryJudge() {
        if (animal[3][0] / 10 == 2) {
            System.out.println("右方玩家获胜，游戏结束！");
            return 1;
        } else if (animal[3][8] / 10 == 1) {
            System.out.println("左方玩家获胜，游戏结束！");
            return 1;
        } else if (alive(1) == 0) {
            System.out.println("右方玩家获胜，游戏结束！");
            return 1;
        } else if (alive(2) == 0) {
            System.out.println("左方玩家获胜，游戏结束！");
            return 1;
        }
        return 0;
    }

    //计算一个阵营中动物存活数
    public static int alive(int camp) {
        int aliveAnimal = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animal[i][j] / 10 == camp) {
                    aliveAnimal++;
                }
            }
        }
        return aliveAnimal;
    }

}
