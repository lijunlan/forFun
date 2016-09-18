package com.sdll18.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sdll18.data.MyPoint;
import com.sdll18.tool.MyTools;

public class MyFrame {

	private JFrame mFrame;
	private JPanel panelSelect;
	private JPanel panelImage;
	private MyLabel labelImage;
	private JTextField textDis;
	private JTextField textNo;
	private Point point;
	private List<MyPoint> points;
	private List<List<MyPoint>> pointsPerPic;
	private double distance;
	private int clickSum;
	private double scale;
	private double coss;
	private double sinn;

	private boolean isSetMode = false;

	public MyFrame() {
		pointsPerPic = new ArrayList<List<MyPoint>>();
		point = new Point();
		mFrame = new JFrame("ImageClick");
		// mFrame.setResizable(false);
		mFrame.setSize(700, 100);
		mFrame.setTitle("ImageClick");
		mFrame.setLayout(new BorderLayout());
		mFrame.setLocationRelativeTo(null);
		panelSelect = new JPanel();
		panelSelect.setSize(700, 100);
		JButton btnSelect = new JButton("Image");
		btnSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "Choose an image");
				File file = jfc.getSelectedFile();
				if (file == null)
					return;
				if (file.getName().endsWith(".png")
						|| file.getName().endsWith(".jpg")
						|| file.getName().endsWith(".bmp")) {
					try {
						BufferedImage image = ImageIO.read(file);
						labelImage.setImage(image);
						int w = image.getWidth() > 700 ? image.getWidth() : 700;
						mFrame.setSize(w, image.getHeight() + 100);
						panelSelect.setSize(w, panelSelect.getParent()
								.getHeight());
						panelImage.setSize(w, image.getHeight());
						points = new ArrayList<MyPoint>();
						pointsPerPic.add(points);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(mFrame, "Choose an image！");
				}
			}
		});
		JButton btnReSelect = new JButton("ReselectImage");
		btnReSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pointsPerPic.size()==0)return;
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "Choose an image");
				File file = jfc.getSelectedFile();
				if (file == null)
					return;
				if (file.getName().endsWith(".png")
						|| file.getName().endsWith(".jpg")
						|| file.getName().endsWith(".bmp")) {
					try {
						BufferedImage image = ImageIO.read(file);
						labelImage.setImage(image);
						int w = image.getWidth() > 700 ? image.getWidth() : 700;
						mFrame.setSize(w, image.getHeight() + 100);
						panelSelect.setSize(w, panelSelect.getParent()
								.getHeight());
						panelImage.setSize(w, image.getHeight());
						pointsPerPic.remove(pointsPerPic.size()-1);
						points = new ArrayList<MyPoint>();
						pointsPerPic.add(points);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(mFrame, "Choose an image！");
				}
			}
		});
		panelImage = new JPanel();
		labelImage = new MyLabel();
		labelImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isSetMode) {
					int x = e.getX();
					int y = e.getY();
					double actualX = ((double) (x - point.x)) * scale;
					double actualY = -((double) (y - point.y)) * scale;
					double[] actualC = MyTools.rotate(coss, sinn, new double[] {
							actualX, actualY, 1 });
					points.add(new MyPoint(actualC[0], actualC[1]));
					System.out.println(new MyPoint(actualC[0], actualC[1]));
					labelImage.addPoint(x, y);
				} else {
					clickSum++;
					switch (clickSum) {
					case 1:
						point.setLocation(e.getX(), e.getY());
						Graphics g1 = labelImage.getGraphics().create();
						g1.setColor(Color.ORANGE);
						g1.fillOval(e.getX() - 4, e.getY() - 4, 8, 8);
						g1.dispose();
						labelImage.addCoordinate(point.x, point.y);
						break;
					case 2:
						Point p = new Point(e.getX(), e.getY());
						labelImage.addCoordinate(p.x, p.y);
						double d = p.distance(point);
						scale = distance / d;
						labelImage.setScale(scale);
						clickSum = 0;
						isSetMode = false;
						double dis = Math.sqrt((p.x - point.x)
								* (p.x - point.x) + (p.y - point.y)
								* (p.y - point.y));
						coss = Math.abs(p.x - point.x) / dis;
						sinn = Math.abs(p.y - point.y) / dis;
						Graphics g2 = labelImage.getGraphics().create();
						g2.setColor(Color.ORANGE);
						g2.fillOval(p.x - 4, p.y - 4, 8, 8);
						g2.dispose();
						break;
					default:
						break;
					}
				}
			}
		});
		panelImage.add(labelImage);
		JLabel labelPage = new JLabel("No.");
		textNo = new JTextField(2);
		panelSelect.add(labelPage);
		panelSelect.add(textNo);
		JLabel labelDis = new JLabel("Dis:");
		textDis = new JTextField(4);
		JButton btnPoint = new JButton("Point");
		btnPoint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					distance = Double.parseDouble(textDis.getText());
					isSetMode = true;
					clickSum = 0;
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(mFrame,
							"Please Input Doubles！");
				}
			}
		});
		panelSelect.add(labelDis);
		panelSelect.add(textDis);
		panelSelect.add(btnPoint);
		panelSelect.add(btnSelect);
		panelSelect.add(btnReSelect);

		JButton btnShowLast = new JButton("Old");
		btnShowLast.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				labelImage.showLastPoints();
			}
		});
		panelSelect.add(btnShowLast);
		JButton btnFinalDraw = new JButton("OK");
		btnFinalDraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				labelImage.finalDraw();
			}
		});
		panelSelect.add(btnFinalDraw);
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = labelImage.getImage();
				try {
					// TODO
					ImageIO.write(image, "png", new File("picture_"
							+ (pointsPerPic.size()) + ".png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panelSelect.add(btnSave);
		JButton btnOutput = new JButton("Output");
		btnOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
				File file = new File("./points.txt");
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
					int no = 0;
					if (textNo.getText() != null && textNo.getText() != "") {
						try {
							no = Integer.parseInt(textNo.getText());
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(mFrame,
									"Please Input Integers！");
						}

					}
					for (int i = 0; i < pointsPerPic.size(); i++) {
						List<MyPoint> ps = pointsPerPic.get(i);
						pw.println("NO." + (i + no + 1) + "   picture_"
								+ (i + no + 1) + ".png");
						// System.out.print("NO." + i + "  Image:\n");
						for (MyPoint p : ps) {
							pw.println("x: " + p.x + "\t" + "y: " + p.y);
							// System.out.print("x: " + p.x + "\t" + "y: " + p.y
							// + "\n");
						}
						pw.println("");
						// System.out.print("\n");
					}
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
