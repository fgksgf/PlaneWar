import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlanePanel extends JPanel implements MouseMotionListener, MouseListener, Runnable {
    public int width = 400;
    public int height = 600;

    // 游戏开始画面和结束画面
    private static Image startImage;
    private static Image overImage;
    private static Image gameImage[] = new Image[3];

    // 玩家飞机图片
    private static BufferedImage planeImage[] = new BufferedImage[2];
    // 敌机图片
    private static BufferedImage enemyImage[] = new BufferedImage[5];
    // boss机图片
    private static BufferedImage bossImage[] = new BufferedImage[3];

    // 背景画面左上角坐标
    private int bgX = 0;
    private int bgY = 0;

    // 当前关卡
    private int level = 0;
    private int count = 0;

    // 游戏线程
    private Thread t;

    // 游戏得分
    private int score = 0;

    // 游戏是否开始
    private boolean begin = false;
    // 游戏是否结束
    private boolean over = false;
    // 是否出现boss
    private boolean hasBoss = false;

    private Font font = new Font("黑体", Font.BOLD, 25);
    private PlayerPlane player;

    private List<EnemyPlane> enemies = new ArrayList<>();
    // 奖励集合
    private List<Award> awards = new ArrayList<>();
    private Award award;
    // 奖励图片
    private static BufferedImage awardImage[] = new BufferedImage[3];
    // 奖励个数
    private int awardNum = 4;
    // 定义玩家飞机接收奖励的变量
    private PlayerAndAward paa = new PlayerAndAward();

    static {
        try {
            // 加载开始和结束图片
            startImage = ImageIO.read(new File("images/GameInterface/interface_1.png"));
            overImage = ImageIO.read(new File("images/GameInterface/jeimian_2.png"));
            // 加载玩家飞机图片
            planeImage[0] = ImageIO.read(new File("images/1.png"));
            planeImage[1] = ImageIO.read(new File("images/2.png"));
            // 加载游戏背景画面
            for (int i = 0; i < 3; i++) {
                gameImage[i] = ImageIO.read(new File("images/background/background_" + (i + 1) + ".png"));
            }
            // 加载敌机图片
            for (int i = 0; i < 5; i++) {
                enemyImage[i] = ImageIO.read(new File("images/LittlePlane/plane" + (i + 1) + ".png"));
            }
            // 加载boss机图片
            for (int i = 0; i < 3; i++) {
                bossImage[i] = ImageIO.read(new File("images/BossPlane/plane_" + (i + 1) + ".png"));
            }
            // 加载奖励的图片
            for (int i = 0; i < 3; i++) {
                awardImage[i] = ImageIO.read(new File("images/award/award_" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PlanePanel() {
        // 监听鼠标
        addMouseListener(this);
        addMouseMotionListener(this);

        // 产生玩家飞机
        player = new PlayerPlane(150, 500, planeImage[0], planeImage[1]);

        // 根据当前关卡产生一定数量敌机
        for (int i = 0; i < level + 4; i++) {
            addEnemy();
        }

        t = new Thread(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!begin && !over) {
            // 画开始界面
            g.drawImage(startImage, 0, 0, null);
        } else if (begin && !over) {
            // 画背景画面
            g.drawImage(gameImage[level], bgX, bgY, null);
            // 画玩家飞机
            player.drawPlane(g);
            // 画炮弹
            player.drawBullet(g);

            // 如果敌机血量大于0，画敌机；否则画爆炸画面
            for (int i = 0; i < enemies.size(); i++) {
                EnemyPlane p = enemies.get(i);
                p.drawBullet(g);
                if (p.hp > 0) {
                    p.drawPlane(g);
                } else {
                    p.drawBlast(g);
                }
            }

            // 画奖励
            for (Award award : awards) {
                award.drawAward(g);
            }

            // 显示boss血量
            if (hasBoss && enemies.size() > 0) {
                g.setColor(Color.MAGENTA);
                g.setFont(font);
                g.drawString("BOSS HP: " + enemies.get(0).hp, 40, 30);
            }

            // 显示得分，血量，当前关卡
            g.setColor(Color.RED);
            g.setFont(font);
            g.drawString("血量: " + player.hp, 250, 25);
            g.drawString("得分: " + score, 250, 50);
            g.drawString("关卡: " + (level + 1), 250, 75);
            g.drawString("大招: " + player.bigFire, 250, 100);
        } else {
            // 画结束画面
            g.drawImage(overImage, 0, 0, null);
        }
    }

    // 产生boss机
    private void addBoss() {
        enemies.clear();
        int x = 250;
        int y = 30;
        EnemyPlane boss = new EnemyPlane(x, y, level + 1, level, bossImage[level]);
        enemies.add(boss);
        hasBoss = true;
    }

    // 产生敌机
    private void addEnemy() {
        int x = (int) (Math.random() * 300);
        int y = (int) (Math.random() * 30);
        int p = (int) (Math.random() * 5);
        EnemyPlane plane = new EnemyPlane(x, y, 0, level, enemyImage[p]);
        enemies.add(plane);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!begin && e.getModifiers() == InputEvent.BUTTON1_MASK && e.getX() >= 130 && e.getX() <= 260 && e.getY() >= 394
                && e.getY() <= 432) {
            // 点击左键开始游戏
            begin = true;
            bgY = -5400;
            if (!t.isAlive()) {
                t.start();
            }
        } else if (begin && e.getModifiers() == InputEvent.BUTTON1_MASK && e.getX() <= 304 && e.getY() >= 310 && e.getY() <= 374) {
            // 点击左键重新开始游戏，重置游戏数据
            resetData();
            repaint();
        } else if (begin && !over && player.bigFire > 0 && e.getModifiers() == InputEvent.BUTTON1_MASK) {
            // 放大招
            player.bullets.clear();
            player.bigFire();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!over && !begin && e.getX() >= 130 && e.getX() <= 260 && e.getY() >= 394 && e.getY() <= 432) {
            // 当鼠标移至开始游戏按钮处时，将鼠标改为手形
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else if (begin && !over) {
            // 游戏进行中设置空光标以达到隐藏鼠标的效果
            URL classUrl = this.getClass().getResource("");
            Image imageCursor = Toolkit.getDefaultToolkit().getImage(classUrl);
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(imageCursor, new Point(0, 0), "cursor"));
            player.x = e.getX() - planeImage[0].getWidth() / 2;
            player.y = e.getY() - planeImage[1].getWidth() / 2;
        } else if (over && !begin && e.getX() >= 113 && e.getX() <= 304 && e.getY() >= 310 && e.getY() <= 374) {
            // 当鼠标移至重新开始按钮处时，将鼠标改为手形
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            // 将鼠标设置为默认形态
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    // 重置游戏数据
    private void resetData() {
        bgY = -5400;
        begin = false;
        over = false;
        enemies.clear();
        level = 0;
        player.bullets.clear();
        player.hp = 500;
        player.bulletLevel = 0;
        player.chooseBullet = 0;
        score = 0;
        hasBoss = false;

        awardNum = 4;
        awards.clear();
    }

    // 检测敌机是否被玩家飞机发射的炮弹击中
    private void checkHitEnemies() {
        List<Bullet> bullets = player.bullets;

        if (player.bulletLevel == 1 || player.bulletLevel == 2) {
            bullets.addAll(player.bullets2);
            bullets.addAll(player.bullets3);
        }

        for (EnemyPlane p : enemies) {
            for (Bullet b : bullets) {
                if (p.hp > 0 && p.eatBullet(b)) {
                    // 炮弹击中敌机
                    p.hp -= b.dam;

                    // 如果该炮弹不是大招，炮弹集中后将存在标志置为false
                    if (!b.isBig) {
                        b.exist = false;
                    }
                }
            }
        }
    }

    // 清除飞出界面之外的所有炮弹
    private void clearBullets() {
        // 清除飞出界面之外的玩家炮弹
        player.clearBullet();
        // 清除飞出界面之外的敌机炮弹
        for (EnemyPlane enemy : enemies) {
            enemy.clearBullet();
        }
    }

    // 更新关卡
    private void updateLevel() {
        // 通关之后增加玩家血量和大招次数
        if (hasBoss && enemies.size() > 0 && enemies.get(0).hp <= 0 && level < 2) {
            level++;
            bgY = -5400;
            hasBoss = false;
            player.hp += 100;
            player.bigFire += 1;

            if (level == 1 || level == 2) {
                awardNum = 5;
            }
        }
    }

    // 根据关数调整移动背景图片的速度
    private void adjustBackground() {
        if (bgY >= 0 && !hasBoss) {
            bgY = 0;
            addBoss();
            player.bigFire += level + 1;
            player.dam += 2;
        } else if (!hasBoss) {
            bgY += (level + 1);
        }
    }

    private void enemyAction() {
        // 当敌机数量不足时，产生敌机
        if (enemies.size() < level + 3 && !hasBoss) {
            addEnemy();
        }

        for (EnemyPlane p : enemies) {
            // 遍历敌机炮弹判断是否击中玩家飞机
            for (int j = 0; j < p.bullets.size(); j++) {
                Bullet b = p.bullets.get(j);
                if (player.eatBullet(b)) {
                    player.hp -= b.dam;
                    b.exist = false;
                }
            }

            if (p.hp > 0) {
                // 敌机移动
                p.movePlane();

                // 敌机碰撞玩家飞机
                if (p.hitPlane(player)) {
                    if (hasBoss) {
                        p.hp -= player.hp;
                        player.hp = 0;
                    } else {
                        player.hp -= p.score % 5;
                        p.hp = 0;
                    }
                }
            }

            // 敌机炮弹移动
            p.moveBullet();
        }
    }

    private void awardAction() {
        // 遍历玩家飞机是否接收到奖励
        for (Award award : awards) {
            paa.playerAndAwardCollision(player, award);
        }

        // 清除出界的奖励
        for (int i = 0; i < awards.size(); i++) {
            award = awards.get(i);
            if (!award.exist) {
                awards.remove(award);
                break;
            }
        }

        // 奖励移动
        for (Award award : awards) {
            award.moveAward();
        }
    }

    // 在敌机被击毁的位置以一定概率产生奖励
    private void generateAward(EnemyPlane ep) {
        double chance = Math.random();
        if (chance > 0.9 && awardNum > 0) {
            // 定义奖励类型的变量
            int awardType = (int) (Math.random() * 3);
            award = new Award(ep.x, ep.y, awardImage[awardType], awardType, awardType);
            award.moveAward();
            awards.add(award);
            awardNum--;
        }
    }

    // 清除被击毁的敌机并使爆炸效果残留
    private void clearEnemy() {
        for (int i = 0; i < enemies.size(); i++) {
            EnemyPlane enemy = enemies.get(i);
            if (enemy.hp <= 0) {
                // 击毁敌机加分
                score += enemy.score * (level + 3);

                generateAward(enemy);
                enemies.remove(i);
                break;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            // 判断游戏是否结束
            if ((player.hp <= 0) || (hasBoss && enemies.size() > 0 && enemies.get(0).hp <= 0 && level == 2)) {
                over = true;
                begin = false;
            } else {
                // count用于控制开火的速度
                count++;
                if (count == 1000) {
                    count = 0;
                }

                clearBullets();
                checkHitEnemies();

                // 每当得分为500的倍数时，增加一次大招次数
                if (score % 500 == 0 && score != 0) {
                    player.bigFire++;
                }

                enemyAction();

                // 玩家炮弹移动
                player.moveBullet();

                if (count % 22 == 0) {
                    // 每隔一定时间产生炮弹
                    player.fire();
                    for (EnemyPlane enemy : enemies) {
                        enemy.fire();
                    }

                    clearEnemy();
                }

                awardAction();
                adjustBackground();
                updateLevel();
            }

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
