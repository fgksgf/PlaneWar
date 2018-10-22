class PlayerAndAward {
    // 判断飞机与奖励是否发生碰撞
	void playerAndAwardCollision(PlayerPlane pp, Award award) {
		//奖励的属性
		int ahp = 20;
		int abf = 1;

		// 分别取三个点
		int p1x = pp.x;
		int p1y = pp.y;
		int p2x = pp.x + pp.image[0].getWidth();
		int p3y = pp.y + pp.image[0].getHeight();

		int a1x = award.x;
		int a1y = award.y;
		int a2x = award.x + award.aImg.getWidth(null);
		int a3y = award.y + award.aImg.getHeight(null);

		if ((p1x >= a1x && p1y >= a1y && p2x <= a2x && p3y <= a3y) || (a1x >= p1x - award.aImg.getWidth(null)
				&& a1y >= p1y && a2x <= p2x + award.aImg.getWidth(null) && a3y >= p3y)) {
			//根据不同的奖励图标完成相应的奖励
			switch (award.type) {
				case 0:
				    // 增加血量
					pp.hp += ahp;
					break;

				case 1:
				    // 升级炮弹
					if(pp.bulletLevel < 3){
						pp.bulletLevel++;
					}
					break;

				case 2:
				    // 增加大招次数
					pp.bigFire += abf;
					break;
			}
			award.exist = false;
		}
	}
}
