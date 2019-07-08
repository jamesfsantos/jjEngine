package com.gcstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.main.Game;
import com.gcstudios.world.Camera;
import com.gcstudios.world.World;

public class Player extends Entity{

	public boolean right,up,left,down;
	public int right_dir = 0,left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0,maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	private boolean arma = false;
	
	public int ammo = 0; 
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public  boolean shoot = false, mouseShoot = false;
	
	public double life = 100,maxlife=100;
	public int mx,my;
	
	public boolean jump = false;
	
	public boolean isJumping = false;
	
	public int z = 0;
	
	public int jumpFrames = 50,jumpCur = 0;
	
	public boolean jumpUp = false,jumpDown = false;
	
	public int jumpSpd = 1;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];		
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		for(int i =0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i =0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
	}

	public void tick() {
		
		if(jump) {
			if(isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		
		if(isJumping == true) {
			
				if(jumpUp) {
					jumpCur+=2;
				}
				else if(jumpDown) {
					jumpCur-=2;
				}
				
				if(jumpCur <= 0) {
					isJumping = false;
					jumpDown = false;
					jumpUp = false;
				}
				
				z = jumpCur;
				if( jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
					//System.out.println("Chegou na altura maxima");
				}
			
		}
		
		
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY(), z)) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}	
		else if(left && World.isFree((int)(x-speed),this.getY(), z)) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(),(int)(y-speed), z)) {
			moved = true;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed), z)) {
			moved = true;
			y+=speed;
		}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) 
					index = 0;
			}
		}
		
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			//Criar bala e atirar.
			if(arma && ammo > 0) {
			ammo--;
			int dx = 0;
			int px = 0;
			int py = 6;
			if(dir == right_dir) {
				px = 18;
				dx = 1;
			}else {
				px = -18;
				dx = -1;
			}
			
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
			Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
	
			mouseShoot = false;
			
			if(arma && ammo > 0) {	
			ammo--;
			//Criar bala e atirar.
		
		
			int px = 0, py = 8;
			double angle = 0;
			if(dir == right_dir) {
				 
				px = 18;
				angle =  Math.atan2( my -(this.getY()+py - Camera.y) ,mx -(this.getX()+px - Camera.x));
			
			}else {
				 
				px = -18;
				angle =  Math.atan2( my -(this.getY()+py - Camera.y) ,mx -(this.getX()+px - Camera.x));
			}
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy);
			Game.bullets.add(bullet);
			}
		}
		
		if(life<= 0) {
			life = 0;
			//Game Over!
			Game.gameState = "GAME_OVER";
			
		}
		
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
		Entity atual = Game.entities.get(i);
		if(atual instanceof Weapon) {
			if(Entity.isColidding(this, atual)) {
				arma = true;
				//System.out.println("Pegou a arma!");
				Game.entities.remove(atual);
			}
		}
	}

}
	
	public void checkCollisionAmmo() {
			for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+=1000;
					//System.out.println("Municao atual:" + ammo);
					Game.entities.remove(atual);
				}
			}
		}

	}
	
	public void checkCollisionLifePack() {
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity atual = Game.entities.get(i);
				if(atual instanceof LifePack) {
					if(Entity.isColidding(this, atual)) {
						life+=10;
						if(life > 100)
							life = 100;;;
						Game.entities.remove(atual);
					}
				}
			}
	}
		
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(arma) {
					//Desenhar arma para direita.
					g.drawImage(Entity.GUN_RIGHT, this.getX()+8 - Camera.x,this.getY()- Camera.y - z, null);
				}
			}else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(arma){
					//Desenhar arma para esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX()-8 - Camera.x,this.getY()- Camera.y - z, null);
				}
			}
		}else {
			g.drawImage(playerDamage,this.getX()- Camera.x, this.getY()- Camera.y -z, null);
			if(arma) {
				if(dir == left_dir) {
					g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX()-8 - Camera.x,this.getY() - Camera.y - z,null);
				}else {
					g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX()+8 - Camera.x,this.getY() - Camera.y - z,null);
				}
			}
		}
		if(isJumping) {
			g.setColor(Color.BLACK);
			g.fillOval(this.getX()- Camera.x + 8, this.getY() - Camera.y + 16 ,8,8);
		}
	}
	
}
