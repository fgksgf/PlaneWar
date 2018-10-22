import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayerPlane extends Plane {
    private int pc;

    // 放大招次数
    int bigFire = 3;
    BufferedImage image[] = new BufferedImage[2];

    PlayerPlane(int x, int y, BufferedImage image1, BufferedImage image2) {
        this.x = x;
        this.y = y;
        this.image[0] = image1;
        this.image[1] = image2;
        this.pc = 0;
        this.hp = 300;
        this.dam = 10;
        this.width = image1.getWidth(null);
        this.height = image1.getHeight(null);
        this.chooseBullet = 0;
        this.canFire = true;
    }

    // 实现飞机的喷气动画效果
    @Override
    public void drawPlane(Graphics g) {
        pc = pc == 0 ? 1 : 0;
        g.drawImage(image[pc], x, y, null);
    }

    @Override
    public void moveBullet() {
        List<Bullet> bulletList = bullets;
        bulletList.addAll(bullets2);
        bulletList.addAll(bullets3);

        for (Bullet b : bulletList) {
            b.y -= 3;

            // 处理飞出界面外的炮弹
            if (b.y <= -b.image.getHeight(null)) {
                b.exist = false;
            }
        }
    }

    @Override
    public void fire() {
        switch (bulletLevel) {
            case 0:
                // 仅飞机中间发射普通炮弹
                Bullet bullet = new Bullet(x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2,
                        y + height / 2 - bulletImg[chooseBullet].getHeight(null) / 2,
                        dam, bulletImg[chooseBullet]);
                bullets.add(bullet);
                break;

            case 1:
                // 1级别炮弹,仅飞机中间发射弧形炮弹，伤害更高
                chooseBullet = 1;
                bullet = new Bullet(x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2,
                        y + height / 2 - bulletImg[chooseBullet].getHeight(null) / 2, dam * 2, bulletImg[chooseBullet]);
                bullets.add(bullet);
                break;

            case 2:
                // 2级别炮弹,飞机左右两翼各发射一普通炮弹
                chooseBullet = 0;
                // 左侧炮弹
                Bullet bullet2 = new Bullet(
                        x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2 - bulletImg[chooseBullet].getWidth()
                                - 10,
                        y + height / 2 - bulletImg[chooseBullet].getHeight(null) / 2 + 10, dam * 2, bulletImg[chooseBullet]);
                bullets2.add(bullet2);

                // 右侧炮弹
                Bullet bullet3 = new Bullet(
                        x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2 + bulletImg[chooseBullet].getWidth()
                                + 10,
                        y + height / 2 - bulletImg[chooseBullet].getHeight(null) / 2 + 10, dam * 2, bulletImg[chooseBullet]);
                bullets3.add(bullet3);
                break;

            case 3:
                // 3级别炮弹，飞机左右两翼各发射两个普通炮弹
                chooseBullet = 0;
                //左侧炮弹
                for (int i = 0; i < 2; i++) {
                    bullet2 = new Bullet(
                            x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2 - (i + 1) * bulletImg[chooseBullet].getWidth()
                                    - (i + 1) * 10,
                            y + height / 2 - bulletImg[chooseBullet].getHeight(null) / 2 + 10, dam * 3,
                            bulletImg[chooseBullet]);
                    bullets2.add(bullet2);
                }

                //右侧炮弹
                for (int i = 0; i < 2; i++) {
                    bullet3 = new Bullet(
                            x + width / 2 - bulletImg[chooseBullet].getWidth(null) / 2 + (i + 1) * bulletImg[chooseBullet].getWidth()
                                    + (i + 1) * 10,
                            y + height / 2 - bulletImg[chooseBullet].getHeight(null) / 2 + 10, dam * 3,
                            bulletImg[chooseBullet]);
                    bullets3.add(bullet3);
                }
                break;
        }

    }

    // 玩家飞机大招
    void bigFire() {
        bigFire--;
        for (int i = 0; i < 7; i++) {
            Bullet b = new Bullet(i * bulletImg[8].getWidth(null), y - 10, dam * 5,
                    true, bulletImg[8]);
            bullets.add(b);
        }
    }
}
