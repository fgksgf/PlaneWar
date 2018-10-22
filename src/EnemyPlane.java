import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyPlane extends Plane {
    // 飞机图片
    private BufferedImage img;
    // 运动方向
    private int f;
    // 水平方向速度
    private int xSpeed;
    // 垂直方向速度
    private int ySpeed;
    // 价值分数
    int score;
    // 是否为boss机
    private int isBoss;
    // 等级
    private int level;

    EnemyPlane(int x, int y, int isBoss, int level, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.img = image;
        f = (int) (Math.random() * 2);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.level = level;
        this.isBoss = isBoss;
        if (isBoss > 0) {
            this.dam = (level + 1) * 10 - 3;
            this.hp = (level + 1) * 10000;
            this.canFire = true;
            this.xSpeed = 2;
            this.ySpeed = 2;
            this.chooseBlast = 2;
            this.score = 300;
            // 不同的boss选择不同的炮弹样式
            switch (isBoss) {
                case 1:
                    this.chooseBullet = 5;
                    break;
                case 2:
                    this.chooseBullet = 4;
                    break;
                case 3:
                    this.chooseBullet = 5;
                    break;
            }
        } else {
            this.dam = (level + 1) * 5;
            xSpeed = (int) (Math.random() * 3 + 1 + level);
            ySpeed = (int) (Math.random() * 3 + 1 + level);
            this.score = (int) (Math.random() * 10 * (level + 1) + 1);
            this.hp = this.score * (level + 5);
            // 随机选择产生的敌机能否开火
            if (score % 2 == 0) {
                canFire = false;
                this.chooseBlast = 0;
            } else {
                // 若能，选择炮弹类型
                canFire = true;
                this.chooseBullet = 6;
                this.chooseBlast = 1;
            }
        }
    }

    @Override
    public void drawPlane(Graphics g) {
        g.drawImage(img, x, y, null);
    }

    @Override
    public void moveBullet() {
        for (Bullet b : bullets) {
            b.y += 4;

            // 处理飞出界面外的炮弹
            if (b.y >= 600) {
                b.exist = false;
            }
        }
    }

    @Override
    public void fire() {
        Bullet bullet;
        if (canFire) {
            if (isBoss > 0) {
                // boss 开火方式
                switch (isBoss) {
                    case 1:
                        // 第一关boss开火方式
                        if (bullets.size() < 5) {
                            bullet = new Bullet(x + 20,
                                    y + 90, dam, bulletImg[chooseBullet]);
                            bullets.add(bullet);
                            bullet = new Bullet(x + 60,
                                    y + 90, dam, bulletImg[chooseBullet]);
                            bullets.add(bullet);
                        }
                        break;

                    case 2:
                        // 第二关boss开火方式
                        if (bullets.size() < 18) {
                            int bx = x - 10;
                            for (int i = 0; i < 6; i++) {
                                Bullet b = new Bullet(bx + i * bulletImg[chooseBullet].getWidth() + 5,
                                        y + height, dam - 5, bulletImg[chooseBullet]);
                                bullets.add(b);
                            }
                        }
                        break;
                    case 3:
                        // 第三关boss开火方式
                        int way = (int) (Math.random() * 11 + 1);
                        if (way % 4 == 0) {
                            int bx = x + width / 2;
                            int by = y + height / 2;
                            for (int i = 0; i < 3; i++) {
                                Bullet b = new Bullet(bx, by + i * bulletImg[chooseBullet].getHeight(),
                                        dam - 5, bulletImg[chooseBullet]);
                                bullets.add(b);
                            }
                        } else if (way % 4 == 1) {
                            int bx = x - 20;
                            for (int i = 0; i < 3; i++) {
                                Bullet b = new Bullet(bx, y + i * bulletImg[chooseBullet].getHeight() + 5, dam - 5, bulletImg[chooseBullet]);
                                bullets.add(b);
                            }
                            for (int i = 0; i < 3; i++) {
                                Bullet b = new Bullet(x + width + 20, y + i * bulletImg[chooseBullet].getHeight() + 5, dam, bulletImg[chooseBullet]);
                                bullets.add(b);
                            }
                        }
                        break;
                }

            } else {
                // 普通敌机开火
                if (bullets.size() < level + 2) {
                    bullet = new Bullet(x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2,
                            y + height, dam, bulletImg[chooseBullet]);
                    bullets.add(bullet);
                }
            }
        }
    }

    void movePlane() {
        switch (f) {
            case 0:// 右下移动
                x += xSpeed;
                y += ySpeed;
                break;
            case 1:// 左下移动
                x -= xSpeed;
                y += ySpeed;
                break;
            case 2:// 左上移动
                x -= xSpeed;
                y -= ySpeed;
                break;
            case 3:// 右上移动
                x += xSpeed;
                y -= ySpeed;
                break;
        }

        if (x <= 0) { // 达到左边界改变运动方向
            if ((int) (Math.random() * 10) % 2 == 0) {
                f = 3;
            } else {
                f = 0;
            }
        } else if (y <= 0) { // 达到上边界改变运动方向
            if ((int) (Math.random() * 10) % 2 == 0) {
                f = 1;
            } else {
                f = 0;
            }
        } else if (y + img.getHeight(null) >= 300) { // 达到下边界改变运动方向
            if ((int) (Math.random() * 10) % 2 == 0) {
                f = 3;
            } else {
                f = 2;
            }
        } else if (x + img.getWidth(null) >= 400) { // 达到右边界改变运动方向
            if ((int) (Math.random() * 10) % 2 == 0) {
                f = 1;
            } else {
                f = 2;
            }
        }
    }
}