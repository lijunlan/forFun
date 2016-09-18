import command.*;
import drawer.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame implements ActionListener,
		MouseMotionListener, WindowListener {
	// 绘制记录
	private MacroCommand history = new MacroCommand();
	// 绘制区域
	private DrawCanvas canvas = new DrawCanvas(400, 400, history);
	// 刪除键
	private JButton clearButton = new JButton("clear");
	private JButton undoButton = new JButton("Undo");
	/* Redo键 */
	private JButton redoButton = new JButton("Redo");

	// 构造子
	public Main(String title) {
		super(title);

		this.addWindowListener(this);
		canvas.addMouseMotionListener(this);
		clearButton.addActionListener(this);
		undoButton.addActionListener(this);
		/* 添加Redo事件监听 */
		redoButton.addActionListener(this);

		Box buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.add(clearButton);
		buttonBox.add(undoButton);
		/* 添加Redo键 */
		buttonBox.add(redoButton);

		Box mainBox = new Box(BoxLayout.Y_AXIS);
		mainBox.add(buttonBox);
		mainBox.add(canvas);
		getContentPane().add(mainBox);

		pack();
		setVisible(true);
	}

	// ActionListener用
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clearButton) {
			history.clear();
			canvas.repaint();
		}
		if (e.getSource() == undoButton) {
			// history.clear();
			history.undo();
			history.execute();
			canvas.repaint();
		}
		/* 添加Redo键事件响应 */
		if (e.getSource() == redoButton) {
			history.redo();
			history.execute();
			canvas.repaint();
		}

	}

	// MouseMotionListener用
	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		Command cmd = new DrawCommand(canvas, e.getPoint());
		history.append(cmd);
		cmd.execute();
	}

	// WindowListener用
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public static void main(String[] args) {
		new Main("Command Pattern Sample");
	}
}
