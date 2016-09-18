package com.sdll18.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MyFrame {

	private JFrame frame;

	public MyFrame() {
		frame = new JFrame("test 2");
		frame.setSize(800, 600);
		JLabel label1 = new JLabel("绘制图形(1-线,2-矩形,3-圆形)");
		JLabel label2 = new JLabel("绘制图形的个数");
		final JTextField textField = new JTextField();
		final JTextField textField2 = new JTextField();
		JButton button = new JButton("绘制");
		label1.setBounds(0, 0, 200, 50);
		label2.setBounds(0, 60, 200, 50);
		textField.setBounds(210, 0, 100, 50);
		textField2.setBounds(210, 60, 100, 50);
		button.setBounds(300, 0, 100, 50);
		frame.setLayout(null);

		frame.add(label1);
		frame.add(label2);
		frame.add(textField);
		frame.add(textField2);
		frame.add(button);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int number = Integer.valueOf(textField2.getText());
				int kind = Integer.valueOf(textField.getText());
				Graphics g = frame.getGraphics();
				//int up = 350;
				Random random = new Random();
				g.setColor(Color.RED); 
				if (kind == 1) {
					for (int i = 1; i <= number; i++) {
						g.drawLine(random.nextInt(800),
								random.nextInt(300) + 300, random.nextInt(800),
								random.nextInt(300) + 300);
					}
				} else if (kind == 2) {
					for (int i = 1; i <= number; i++) {
						g.fillRect(random.nextInt(800),
								random.nextInt(300) + 300, 200, 100);
					}
				} else if (kind == 3) {
					for (int i = 1; i <= number; i++) {
						g.fillOval(random.nextInt(700) + 50,
								random.nextInt(200) + 50, 50, 50);
					}
				}
			}
		});
	}

	public void start() {
		frame.setVisible(true);
	}

}
