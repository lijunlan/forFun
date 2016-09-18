package com.sdll18.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class MyLabel extends JLabel {

	private BufferedImage mImage;

	private List<Point> points;

	private List<Point> lastPoints;

	private Point original;

	private Point lastOriginal;

	private int cNum;

	private double scale;

	private double lastScale;

	// private int pNum;

	public MyLabel() {
		super();
		points = new ArrayList<Point>();
	}

	public void setScale(double scale) {
		lastScale = this.scale;
		this.scale = scale;
	}

	public void setImage(BufferedImage image) {
		ImageIcon icon = new ImageIcon(image.getScaledInstance(
				image.getWidth(), image.getHeight(), Image.SCALE_FAST), ""
				+ new Random().nextInt(100000));
		mImage = image;
		setIcon(null);
		setIcon(icon);
		if (original != null) {
			lastOriginal = original;
		}
		lastPoints = points;
		points = new ArrayList<Point>();
		cNum = 0;
		// pNum = 0;
		points.clear();
	}

	public void addCoordinate(int x, int y) {
		Graphics g = mImage.createGraphics();
		g.setColor(Color.ORANGE);
		g.fillOval(x - 4, y - 4, 8, 8);
		g.dispose();
		cNum++;
		if (cNum == 1)
			original = new Point(x, y);
	}

	public void addPoint(int x, int y) {
		Graphics g = mImage.createGraphics();
		g.setColor(Color.RED);
		g.fillOval(x - 4, y - 4, 8, 8);
		g.dispose();
		g = getGraphics().create();
		g.setColor(Color.RED);
		g.fillOval(x - 4, y - 4, 8, 8);
		g.dispose();
		// pNum++;
		points.add(new Point(x, y));
	}

	public void finalDraw() {
		Graphics2D g1 = mImage.createGraphics();
		Graphics2D g2 = (Graphics2D) getGraphics().create();
		g1.setStroke(new BasicStroke(3.0f));
		g2.setStroke(new BasicStroke(3.0f));
		Point lastP = null;
		for (int i = 1; i <= points.size(); i++) {
			Point p = points.get(i - 1);
			g1.setColor(Color.RED);
			g2.setColor(Color.RED);
			g1.setFont(new Font("宋体", Font.BOLD, 20));
			g2.setFont(new Font("宋体", Font.BOLD, 20));
			g1.drawString("" + i, p.x - 15, p.y + 10);
			g2.drawString("" + i, p.x - 15, p.y + 10);
			if (i != 1) {
				g1.drawLine(lastP.x, lastP.y, p.x, p.y);
				g2.drawLine(lastP.x, lastP.y, p.x, p.y);
			}
			lastP = p;
		}
		g1.drawLine(points.get(0).x, points.get(0).y,
				points.get(points.size() - 1).x,
				points.get(points.size() - 1).y);
		g2.drawLine(points.get(0).x, points.get(0).y,
				points.get(points.size() - 1).x,
				points.get(points.size() - 1).y);
		g1.drawString("O", original.x - 10, original.y + 10);
		g2.drawString("O", original.x - 10, original.y + 10);
		g1.dispose();
		g2.dispose();
	}

	public BufferedImage getImage() {
		return mImage;
	}

	public void showLastPoints() {
		// TODO
		Graphics g = getGraphics().create();
		g.setColor(Color.CYAN);
		for (Point p : lastPoints) {
			int x = (int) ((double) (p.x - lastOriginal.x) * lastScale / scale + original.x);
			int y = (int) ((double) (p.y - lastOriginal.y) * lastScale / scale + original.y);
			g.fillOval(x - 4, y - 4, 8, 8);
		}
		g.dispose();
	}
}
