import java.awt.Toolkit;

import javax.swing.*;

public class PlaneFrame extends JFrame {

    private PlaneFrame() {
        // 游戏窗口大小
        int width = 400;
        int height = 600;

        // 获取屏幕的尺寸
        int pw = Toolkit.getDefaultToolkit().getScreenSize().width;
        int ph = Toolkit.getDefaultToolkit().getScreenSize().height;

        // 设置窗口标题
        this.setTitle("飞机大战");

        // 使画布位于屏幕中央
        this.setBounds((pw - width) / 2, (ph - height) / 2, width, height);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        PlanePanel pp = new PlanePanel();
        this.add(pp);
        this.setResizable(false);
    }

    public static void main(String[] args) {
        new PlaneFrame();
    }
}
