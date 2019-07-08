package com.gcstudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = {"novo jogo","carregar jogo","sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up,down,enter;
	
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter) {
			//Sound.music.loop();
			enter = false;	
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}
			else if (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD,36));
		g.drawString(">JJ Adventure's<",(Game.WIDTH*Game.SCALE) /2 - 130 ,(Game.HEIGHT*Game.SCALE) /2 - 150 );
	
		//Opcoes de menu
		g.setColor(Color.green);
		g.setFont(new Font("arial",Font.BOLD,30));
		if(pause == false)
			g.drawString("Novo jogo",(Game.WIDTH*Game.SCALE) /2 - 50,160);
		else 
			g.drawString("Continuar jogo",(Game.WIDTH*Game.SCALE) /2 - 70,160);
		g.drawString("Carregar jogo",(Game.WIDTH*Game.SCALE) /2 - 70,200);
		g.drawString("Sair",(Game.WIDTH*Game.SCALE) /2 - 10,240);
		g.setColor(Color.white);
		g.drawString("Pressione Enter para começar o jogo!",(Game.WIDTH*Game.SCALE) /2 - 200,300);
		
		if(options[currentOption] == "novo jogo") {
			g.setColor(Color.blue);
			g.drawString(">",(Game.WIDTH*Game.SCALE) /2 - 90,160);
		}else if(options[currentOption] == "carregar jogo") {
			g.setColor(Color.blue);
			g.drawString(">",(Game.WIDTH*Game.SCALE) /2 - 90,200);
		}else if(options[currentOption] == "sair") {
			g.setColor(Color.blue);
			g.drawString(">",(Game.WIDTH*Game.SCALE) /2 - 40,240);
		}
			
	}
	
}
