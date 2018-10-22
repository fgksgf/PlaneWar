import java.awt.Graphics;
import java.awt.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Plane {
    // 飞机的坐标
    int x = 0;
    int y = 0;

    // 飞机的尺寸
    int width;
    int height;

    // 飞机的血量
    int hp;
    // 飞机炮弹的伤害量
    int dam;
    // 飞机被击毁的图片
    private static Image blastImg[] = new Image[3];
    // 随机选择一张爆炸图片
    int chooseBlast = (int) (Math.random() * 3);

    // 炮弹的图片
    static BufferedImage bulletImg[] = new BufferedImage[9];
    // 选择一张炮弹图片
    int chooseBullet;

    // 飞机能否开火
    boolean canFire;
    // 存储飞机发射的炮弹
    List<Bullet> bullets = new ArrayList<>();

    //飞机两边的炮弹
    List<Bullet> bullets2 = new ArrayList<>();
    List<Bullet> bullets3 = new ArrayList<>();

    // 玩家飞机的炮弹级别
    int bulletLevel = 0;

    static {
        try {
            // 加载爆炸的图片
            for (int i = 0; i < 3; i++) {
                blastImg[i] = ImageIO.read(new File("images/blast/blast_" + (i + 1) + ".png"));
            }
            // 加载炮弹的图片
            for (int i = 0; i < 9; i++) {
                bulletImg[i] = ImageIO.read(new File("images/bullet/bullet_" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 画飞机, 空方法，子类实现
    public abstract void drawPlane(Graphics g);

    // 移动炮弹，空方法，子类实现
    public abstract void moveBullet();

    // 发射炮弹，空方法，子类实现
    public abstract void fire();

    //清除飞出界面之外的炮弹
    void clearBullet() {
        bullets.removeIf(b -> !b.exist);
        bullets2.removeIf(b -> !b.exist);
        bullets3.removeIf(b -> !b.exist);
    }

    //画炮弹
    void drawBullet(Graphics g) {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).drawBullet(g);
        }

        for (int i = 0; i < bullets2.size(); i++) {
            bullets2.get(i).drawBullet(g);
        }

        for (int i = 0; i < bullets3.size(); i++) {
            bullets3.get(i).drawBullet(g);
        }
    }

    // 画爆炸画面
    void drawBlast(Graphics g) {
        // 随机选择一张爆炸画面
        g.drawImage(blastImg[chooseBlast], x, y, null);
    }

    // 是否被子弹击中
    boolean eatBullet(Bullet b) {
        boolean ret = false;
        if (b.x >= x && b.x <= x + width && b.y >= y && b.y <= y + height) {
            ret = true;
        }
        return ret;
    }

    //是否碰到飞机
    boolean hitPlane(Plane p) {
        boolean ret = false;
        if ((x >= p.x && x <= p.x + p.width && y >= p.y && y <= p.y + p.height)
                || (x + width >= p.x && x + width <= p.x + p.width && y >= p.y && y <= p.y + p.height)
                || (x >= p.x && x <= p.x + p.width && y + height >= p.y && y + height <= p.y + p.height)
                || (x + width >= p.x && x + width <= p.x + p.width && y + height >= p.y && y + height <= p.y + p.height)) {
            ret = true;
        } else if ((p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height)
                || (p.x + p.width >= x && p.x + p.width <= x + width && p.y >= y && p.y <= y + height)
                || (p.x >= x && p.x <= x + width && p.y + p.height >= y && p.y + p.height <= y + height)
                || (p.x + p.width >= x && p.x + p.width <= x + width && p.y + p.height >= y && p.y + p.height <= y + height)) {
            ret = true;
        }
        return ret;
    }
}
