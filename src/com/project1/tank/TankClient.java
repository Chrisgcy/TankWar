package com.project1.tank;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class used to be the window of the TankWar.
 * @author chrisgong
 *
 */

public class TankClient extends Frame{
	
	/**
	 * The width of the TankWar.
	 */
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	Tank myTank = new Tank(50, 50, true, Tank.Direction.STOP, this);
	
	Wall w1 = new Wall(300, 200, 20, 150, this), w2 = new Wall(400, 300, 300, 20, this);
	
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	
	Blood b = new Blood();
	
	@Override
	public void paint(Graphics g) {
		/*
		 * notify the bullet - explode - tanks number
		 * and the life of the tank.
		 */
		
		g.drawString("missiles count: " + missiles.size(), 10, 50);
		g.drawString("explodes count: " + explodes.size(), 10, 70);
		g.drawString("tanks    count: " + tanks.size(), 10, 90);
		g.drawString("tanks    life: " + myTank.getLife(), 10, 110);
		
		// check if the enemies are all dead, if so, add the new enemies.
		if(tanks.size() <= 0){
			for(int i = 0; i < 10; i++){
				tanks.add(new Tank(50 + 60 * (i + 1), 80, false, Tank.Direction.D, this));	//create new enemy tanks
			}
		}
		
		for(int i = 0; i < missiles.size(); i++){
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		for(int i = 0; i < explodes.size(); i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		
		myTank.draw(g);
		myTank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g); 
		
	}
	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.YELLOW);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	
	/**
	 * The method lanchFrame used to display the window of the game.
	 */
	public void lanchFrame() {
		
		for(int i = 0; i < 10; i++){
			tanks.add(new Tank(50 + 60 * (i + 1), 80, false, Tank.Direction.D, this));	//create new enemy tanks
		}
		
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}
	
	
	public static void main(String[] args){
		TankClient tc = new TankClient();
		tc.lanchFrame();
	}
	
	private class PaintThread implements Runnable {
		
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
}
