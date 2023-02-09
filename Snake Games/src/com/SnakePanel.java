package com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener {

	static final int ScreenWidth=500;
	static final int ScreenHeight=500;
	static final int UnitSize=20; // the size of the object
	static final int GameUnits= (ScreenHeight*ScreenWidth)/UnitSize;  // the no. of objects that can be fit into the screen
	static final int delay=75;

	final int x[]=new int[GameUnits]; // will contain all the body parts of x-coordinates
	final int y[]=new int[GameUnits]; // will contain all the body parts of y-coordinates

	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;

	char direction = 'R';

	boolean running = false;

	Timer timer;
	Random random;

	SnakePanel(){
		random =new Random();
		this.setPreferredSize(new Dimension(ScreenWidth,ScreenHeight));
		this.setBackground(Color.WHITE);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer=new Timer(delay,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {

		if(running) {
			
//			for(int i=0;i<ScreenHeight/UnitSize;i++) {
//				g.drawLine(i*UnitSize, 0, i*UnitSize, ScreenHeight);
//				g.drawLine(0, i*UnitSize, ScreenWidth, i*UnitSize);
//			}

			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UnitSize, UnitSize);

			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.BLACK);
					g.fillRect(x[i], y[i], UnitSize, UnitSize);
				}else {
					g.setColor(Color.BLACK);
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UnitSize, UnitSize);
				}
			}
			
			g.setColor(Color.blue);
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(ScreenWidth - metrics.stringWidth("Score: "+applesEaten))/2 , g.getFont().getSize());
			
		}else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt((int)(ScreenWidth/UnitSize))*UnitSize;
		appleY = random.nextInt((int)(ScreenHeight/UnitSize))*UnitSize;
		System.out.println(appleX+" "+appleY);
	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0]=y[0]-UnitSize;
			break;
		case 'D':
			y[0]=y[0]+UnitSize;
			break;
		case 'L':
			x[0]=x[0]-UnitSize;
			break;
		case 'R':
			x[0]=x[0]+UnitSize;
			break;
		}
	}
	public void checkApple() {
		if(x[0]==appleX && y[0]==appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {

		//checks if head collides with body
		for(int i=bodyParts;i>0;i--) {
			if(x[0]==x[i] && y[0]==y[i]) {
				running=false;
			}
		}

		//check if head touches left border
		if(x[0]<0) {
			running=false;
		}

		//checks if head touches right border
		if(x[0]>ScreenWidth) {
			running=false;
		}

		//checks if head touches top border
		if(y[0]<0) {
			running=false;
		}

		//checks if head touches bottom border
		if(y[0]>ScreenWidth) {
			running=false;
		}

		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		
		//display Score
		g.setColor(Color.blue);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten,(ScreenWidth - metrics1.stringWidth("Score: "+applesEaten))/2 , g.getFont().getSize());
		
		//display Game Over
		g.setColor(Color.blue);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over",(ScreenWidth - metrics2.stringWidth("Game Over"))/2 , ScreenHeight/2);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {	

		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}

	public class MyKeyAdapter extends KeyAdapter { //inner class

		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction ='L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction ='R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction ='U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction ='D';
				}
				break;	
			}
		}

	}
}
