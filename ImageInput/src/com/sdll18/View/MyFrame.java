package com.sdll18.View;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MyFrame {

	private JFrame mFrame;
	private JPanel panelSelect;
	private JPanel panelImage;

	public MyFrame() {
		mFrame = new JFrame("ImageInput");
		mFrame.setSize(600, 100);
		mFrame.setLayout(new BorderLayout());
		mFrame.setLocationRelativeTo(null);
		panelSelect = new JPanel();
		panelSelect.setSize(600, 100);
		panelImage = new JPanel();

		try {
			// TODO
			BufferedImage bufferedImage = ImageIO.read(new File("图片路径"));
			Graphics g = panelImage.getGraphics().create();
			g.drawImage(bufferedImage.getScaledInstance(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.SCALE_DEFAULT), 0, 0, null);
			g.dispose();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		JTextField textInput1 = new JTextField(10);
		panelImage.setLocation(20, 20);
		panelImage.add(textInput1);
		JTextField textInput2 = new JTextField(10);
		panelImage.setLocation(50, 20);
		panelImage.add(textInput2);
		JTextField textInput3 = new JTextField(10);
		panelImage.setLocation(100, 20);
		panelImage.add(textInput3);
		JTextField textInput4 = new JTextField(10);
		panelImage.setLocation(80, 20);
		panelImage.add(textInput4);

		JButton btnOutput = new JButton("Output");
		btnOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				File file = new File("./output.txt");
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				FileWriter fw = null;
				PrintWriter pw = null;
				try {
					fw = new FileWriter(file);
					pw = new PrintWriter(fw);
					// TODO

					pw.flush();
					fw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					if (fw != null) {
						try {
							fw.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		panelSelect.add(btnOutput);
		mFrame.add(panelSelect, BorderLayout.NORTH);
		mFrame.add(panelImage, BorderLayout.CENTER);
	}

	public void start() {
		mFrame.setVisible(true);
	}
}
