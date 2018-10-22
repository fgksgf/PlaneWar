import java.awt.Graphics;
import java.awt.Image;

class Bullet {
	// 子弹的坐标
	int x, y;
	// 每发子弹的伤害量
	int dam;
	// 子弹图片
	Image image;
	// 子弹是否存在
	boolean exist;
	// 是否是大招
	boolean isBig;

	// 普通子弹的构造函数
	Bullet(int x, int y, int dam, Image image) {
		super();
		this.x = x;
		this.y = y;
		this.dam = dam;
		this.image = image;
		this.exist = true;
		this.isBig = false;
	}

	// 大招炮弹的构造函数
	Bullet(int x, int y, int dam, boolean isBig, Image image) {
		this.x = x;
		this.y = y;
		this.dam = dam;
		this.image = image;
		this.exist = true;
		this.isBig = isBig;
	}

	//画炮弹
	void drawBullet(Graphics g){
		g.drawImage(image, x, y, null);
	}
}
