import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Award {
    // 定义奖励出现的坐标
    int x, y;

    // 定义奖励的图片
    BufferedImage aImg;

    // 定义奖励的移动方向
    private int f;

    // 定义奖励的移动速度
    private final int SPEED = 2;

    // 定义奖励是否存在
    boolean exist = true;

    //定义奖励的类型
    int type;

    Award(int x, int y, BufferedImage aImg, int f, int type) {
        this.x = x;
        this.y = y;
        this.aImg = aImg;
        this.f = f;
        this.type = type;
    }

    // 奖励的移动
    void moveAward() {
        switch (f) {
            case 0:// 右下移动
                x += SPEED;
                y += SPEED;

                if (x + aImg.getWidth(null) >= 400) {
                    if ((int) (Math.random() * 10) % 2 == 0) {
                        f = 1;
                    } else {
                        f = 2;
                    }
                }
                if (y >= 600) {
                    exist = false;
                }
                break;

            case 1:// 左下移动
                x -= SPEED;
                y += SPEED;

                if (x <= 0) {
                    if ((int) (Math.random() * 10) % 2 == 0) {
                        f = 3;
                    } else {
                        f = 0;
                    }
                }
                if (y >= 600) {
                    exist = false;
                }
                break;

            case 2:// 左上移动
                x -= SPEED;
                y -= SPEED;
                if (x <= 0) {
                    if ((int) (Math.random() * 10) % 2 == 0) {
                        f = 3;
                    }
                }
                if (y <= 0) {
                    f = 1;
                }
                break;

            case 3:// 右上移动
                x += SPEED;
                y -= SPEED;

                if (x + aImg.getWidth(null) >= 400) {
                    if ((int) (Math.random() * 10) % 2 == 0) {
                        f = 1;
                    } else {
                        f = 2;
                    }
                }
                if (y <= 0) {
                    f = 0;
                }
                break;
        }
    }

    //画奖励
    void drawAward(Graphics g) {
        g.drawImage(aImg, x, y, null);
    }
}
