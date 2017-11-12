import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game_manager {
	
	static String lvl1 = "lvl1.png";
	static String sprites = "sprite_sheet.png";
	private int w;
	private int h;
	private int taxa_tela;
	private Handler handler;
	private Game game;
	public boolean limpo;
	public BufferedImage level = null;
	private BufferedImage sprite_sheet = null;
	public SpriteSheet ss;
	private int fator;
	//public int fase_counter;
	public static int fase_counter = 0;
	public int qtdade_inimigos;
	public Player player;
	public int hp;
	public int ammo;
	public int tamanho_bala;
	public int medio = 50;
	public int baixo = 25;
	public static int yellow_keys_count = 0;
	public static int green_keys_count = 0;
	public static int boss_keys_count = 0;
	private BufferedImage floor = null;
	private BufferedImage floor_complement;
	public  ArrayList<String> fases;
	
	public Game_manager ( Game game) {
		this.game = game;
		this.taxa_tela = 62;
		limpo = false;
		//fase_counter = 0;
		fator = 32;	
		carrega_fases();
	}
	public void carrega_fases() {
		fases = new ArrayList<String>(5);
		fases.add("/lvl1.png");//ivnerta a ordem das fases que mudara o "mundo";D
		fases.add("/lvl2.png");//original lvl2.png
		fases.add("/lvl3.png");//original lvl3.png
		fases.add("/lvl4.png");
		fases.add("/lvl5.png");
		System.out.println("lvl (fase counter )" +fase_counter);
		prepara_level(fases.get(fase_counter),"/sprite_sheet.png");
	}
	public void select_floor(BufferedImageLoader loader) {
		System.out.println("ira n o case ( fase counter )" +fase_counter);
		switch(fase_counter) {
			case 0:
				floor = ss.grabImage(5, 13, fator, fator);
				floor_complement = ss.grabImage(1, 11, fator, fator);
				break;
			case 1:
				floor = ss.grabImage(5, 13, fator, fator);
				floor_complement = ss.grabImage(1, 11, fator, fator);
				break;
			case 2:		//4,2
				floor = ss.grabImage(8, 13, fator, fator);
				floor_complement = ss.grabImage(3, 9, fator, fator);
				break;
			case 3:		//4,2
				floor = ss.grabImage(8, 13, fator, fator);
				floor_complement = ss.grabImage(3, 9, fator, fator);
				break;	
			case 4:		//4,2
				floor = ss.grabImage(5, 13, fator, fator);
				floor_complement = ss.grabImage(1, 11, fator, fator);
				break;			
		}	
	}
	public void preenche_floor(Graphics g) {
		//TRO QUE (fator*2) por fator  se quiser um fundo de 32px em vez de 64px
		for(int xx = 0;xx < (taxa_tela+1)*fator + game.width; xx+=fator) { 
			for(int yy = 0; yy < (taxa_tela+1)*fator + game.height; yy+=fator) {
				if(xx > (taxa_tela+1)*fator || yy > (taxa_tela+1)*fator) 
					g.drawImage(floor_complement, xx, yy, null);
				else	
					g.drawImage(floor, xx, yy, null);	
			}
		}
	}
	public void gerencia_barra_vida(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(7, 7, 200, fator);
		g.setColor(Color.gray);
		g.fillRect(3, 3, 200, fator);
		gera_cor(g);
		g.fillRect(5, 5, (int) (hp*2), fator);
		g.setColor(Color.white);
		g.setColor(Color.black);
		
		g.drawString("Life ", 5, 25 );
		g.setColor(Color.yellow);
		g.drawString("Ammo: "+ ammo, 5, 50 );
		
		g.setColor(Color.yellow);
		g.drawString("Yellow Keys: "+ yellow_keys_count, 5, 85 );
		
		g.setColor(Color.yellow);
		g.drawString("Green Keys: "+ green_keys_count, 5, 105 );
		
		g.setColor(Color.yellow);
		g.drawString("Enemies left: "+ qtdade_inimigos, 5, 65 );

		
		
		
	}
	private void enemy_life(Graphics g) {
		for(GameObject go : handler.object_list) {
			if(go.getId() == ID.enemy) {
				Enemy enemy = (Enemy) go;
				enemy.gerencia_barra_vida(g);
			}
		}
	}
	private void gera_cor(Graphics g) {
		if(hp > medio)
			g.setColor(Color.green);
		if(hp < medio + 1  && hp > baixo + 1)
			g.setColor(Color.yellow);
		if(hp < baixo + 1 )
			g.setColor(Color.red);
	}
	public void restart() {
		handler.kill();
		prepara_level(fases.get(fase_counter),"/sprite_sheet.png");
		seta_valores();
		load_level(level);
	}
	public void next_level() {
		fase_counter ++;
		//if(fase_counter > 2)
			//fase_counter
		qtdade_inimigos = 0;
		//game.event_handler(); 
		//TESTANDO  Nww
		load_level(level);
	}
	public void prepara_level(String level_path, String ss_path) {
		System.out.println("entrou prepara levl..ta bugado" + fase_counter);
		handler = game.handler;
		limpo = false;
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.load_image(level_path);
		sprite_sheet = loader.load_image(ss_path);
		ss = new SpriteSheet(sprite_sheet);
		select_floor(loader);
	}
	public void seta_valores() {
		hp = 100;
		ammo = 45;
		tamanho_bala = 8;
		/*got_yellow_key = false;
		got_boss_key = false;
		got_green_key = false;*/
		//qtdade_inimigos = qtdade_inimigos;
	}
	public void load_level(BufferedImage image) {
		w = image.getWidth();
		h = image.getHeight();
		taxa_tela = w;
		//System.out.println(taxa_tela);
		for(int xx = 0; xx< w; xx++) {
			for(int yy=0; yy<h; yy++) {
				Color pixel = new Color(image.getRGB(xx, yy));
				gerar_pixel_map(image, pixel, xx, yy);
			}	
		}
	} 
	public void gerar_pixel_map(BufferedImage image, Color pixel,int xx, int yy) {
		int red = pixel.getRed();
		int green = pixel.getGreen();
		int blue = pixel.getBlue();
		
		if(red == 255 && green != 255 && blue != 255)//preenchimento (vermelho no png) 
			handler.add_object(new Block(xx*fator ,yy*fator,32,32, ID.block,ss,false,2,11,true));
		else if(green == 255 && red != 255 && blue != 255) {//bloco principal ( textura principal )cerca nerário ( verde no png){;,false
			if(fase_counter == 2 )
				handler.add_object(new Block(xx*fator,yy*fator,32,32, ID.block,ss,true, 4, 9,false));
			else if( fase_counter == 3 )
				handler.add_object(new Block(xx*fator,yy*fator,32,32, ID.block,ss,true, 4, 9,false));
			else	
				handler.add_object(new Block(xx*fator,yy*fator,fator,fator, ID.block,ss,true, 1, 11,true));
		}	
		else if(red == 63 && green == 169 && blue == 145) {
			if( fase_counter == 2 || fase_counter == 3)
				handler.add_object(new Block(xx*fator,yy*fator,32,32, ID.block,ss,false,3,9,false));
			else	
				handler.add_object(new Block(xx*fator,yy*fator,32,32, ID.block,ss,false,4,11,false));
		}	
		else if(red == 171 && blue == 131)
			handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.vida,ss, 12, 12, 1, 1));
		else if(red == 171 && blue == 255 && green == 17)
			handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.planta,ss, 8, 8, 2, 2));
		else if(red == 0 && green == 0 && blue == 0) 
			handler.add_object(new Generics(xx*fator,yy*fator,32,64, ID.placa,ss, 11, 11, 1, 1));//placa
		else if(red == 55 && green == 135 && blue == 217) {//objetos sorteaveis ( cogumelo, solo ) 
			if(fase_counter == 3 )
				handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.cogumelo,ss,4,4,9,9));
			if(fase_counter == 2 )
				handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.cogumelo,ss,1,2,11,11));
			else
				handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.cogumelo,ss,9,10,1,1));//COGUMELSO SORTEAVEIS
			
		}	
		else if(red == 55 && green == 135 && blue == 0) {
			if(fase_counter == 2 || fase_counter == 3)
				handler.add_object(new Generics(xx*fator,yy*fator,64,64, ID.grama,ss,1,2,13,13));
			else
				handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.grama,ss,8,8,1,1));//planta
		}	
		else if(red == 255 & green != 255 && blue == 255)//municao(rosa no png)
			handler.add_object(new Generics(xx*fator,yy*fator,32,32, ID.ammo,ss,7,7,13,13));
		else if(red == 163 && green == 193 && blue == 181) {
			if(fase_counter == 2 || fase_counter == 3) 
				handler.add_object(new Enemy(xx*fator,yy*fator,fator*5,fator*4, ID.normal_spider,handler,ss,game,5,5,21,4,1,1,3000,180,0,1,"vertical",100,15,4,false));//handler.add_object(new Perseguidor_shot_enemie(xx*fator,yy*fator,fator*5,fator*4, ID.enemy,handler,ss,game,5,5,21,4,1,0,1,3));//handler.add_object(new Perseguidor_shot_enemie(xx*fator,yy*fator,fator*4,fator*4, ID.enemy,handler,ss,this,5,5,21,4,1,0,1,3));//handler.add_object(new Perseguidor_shot_enemie(xx*fator,yy*fator,fator,fator*2, ID.enemy,handler,ss,this,1,6,21,4,0,1,0,4));//handler.add_object(new Perseguidor_shot_enemie(xx*fator,yy*fator,fator,fator*2, ID.enemy,handler,ss,this,1,6,21,4,0,1,0,4));//handler.add_object(new Spyder(xx*fator,yy*fator,fator*5,fator*4, ID.enemy,handler,ss,this, 5 ,7, 2));
			else if (fase_counter == 1 )
					handler.add_object(new Enemy(xx*fator,yy*fator,fator,fator*2, ID.dark_yuke,handler,ss,game,1,3,21,2,2,1,3000,180,0,0,"horizontal",100,5,5,true));//handler.add_object(new Spyder(xx*fator,yy*fator,fator*10,fator*6, ID.enemy,handler,ss,this));//DarkYuke aranhamedia: fator*4
			else  
				handler.add_object(new Enemy(xx*fator,yy*fator,fator,fator*2, ID.dark_yuke,handler,ss,game,1,2,21,2,1,1,3000,180,0,0,"horizontal",100,15,5,false));
			qtdade_inimigos ++;
			//System.out.println("criando normal_spider\ndark_yuke");//  int coluna, int linha, int coluna_bulet, int linha_bulet, int taxa_vx_bulet, int taxa_vy_bulet,int bulet_duracao,int bulet_max_dist,int range,int mov, String mode,int hp, int bleed, int sn
		}	
		else if(red == 90 && green == 147 && blue == 125) {
			//System.out.println("chora boy");
			//handler.add_object(new Enemy(xx*fator,yy*fator,fator,fator*2, ID.evil_bean,handler,ss,game,1,7,1,0,"horizontal",100, 20,2));//1,7,1,0
		}																		// ID.small_spider,handler,ss,game,6,2,4,1,"vertical",100,30,4));//,1,"vertical",100,35,3,false));	//ID.small_spider,handler,ss,game,6,2,4,1,"vertical",100,30,4))
		//so lvl2
		else if (red == 155 && green == 147 & blue == 60) {
			qtdade_inimigos ++;
			//System.out.println("criando giant_spider");
			handler.add_object(new Enemy(xx*fator,yy*fator,fator*11,fator*6, ID.giant_spider,handler,ss,game,10,2,21,2,1,1,3000,180,0,1,"vertical",100,2,4,false));
			
		}
		else if(red == 109 && green == 108 & blue == 0) {
			//System.out.println("criando yellow_key_altar");
			handler.add_object(new Generics(xx*fator,yy*fator,fator,fator*7, ID.yellow_key_altar,handler,ss,13,15,2,true));
			//handler.add_object(new Generics(xx*fator,yy*fator,fator,fator*2, ID.yellow_key_altar,ss,13,13,15,15));
			
		}
		else if(red == 143 && green == 75 & blue == 105) {
			//System.out.println("criando yellow_key");
			handler.add_object(new Generics(xx*fator,yy*fator,fator,fator, ID.yellow_key,ss,13,13,14,14));
			
		}
		else if(red == 49 && green == 177 & blue == 28) {
			//System.out.println("criando green_key");
			handler.add_object(new Generics(xx*fator,yy*fator,fator,fator, ID.green_key,ss,14,14,14,14));
			
		}
		else if(red == 9 && green == 70 & blue == 46) {
			//System.out.println("criando green_key_altar");
			handler.add_object(new Generics(xx*fator,yy*fator,fator,fator*7, ID.green_key_altar,handler,ss,18,15,2,true));
			
		}
		//else if(red == 0 && green == 109 & blue == 100) {
	
		else if (red == 244 && green == 110 & blue == 25) {
			//System.out.println("litle");
			qtdade_inimigos ++;
			handler.add_object(new Enemy(xx*fator,yy*fator,fator*3,fator*2, ID.small_spider,handler,ss,game,6,2,4,1,"vertical",100,30,4));//,1,"vertical",100,35,3,false));
		}
		else if(red == 255 && green == 255 && blue != 255) {//inimigo(amarelo no png)
			handler.add_object(new Enemy(xx*fator,yy*fator,32,32, ID.dark_dust,handler,ss,game,4,1,50,2,"circular",100,35,4));//"circular",4,4,1,50,2,100,35)); 
			qtdade_inimigos ++;		//int x, int y, int width, int height, ID id,Handler handler,SpriteSheet ss,Game game,int coluna, int linha, int coluna_bulet, int linha_bulet ,int range,int mov, String mode,int hp, int bleed, int sn
		}												//int x, int y, int width, int height, ID id, Handler handler, SpriteSheet ss, Game game, int coluna, int linha, int coluna_bulet, int linha_bulet,int mov, String mode,int hp, int bleed, int sn											
		else if(blue == 255 && red == 60 & green == 0) {						//int x, int y, int width, int height, ID id, Handler handler, SpriteSheet ss, Game game, int coluna, int linha, int coluna_bulet, int linha_bulet,int mov, String mode,int hp, int bleed, int sn			
			player = new Player(xx*fator, yy*fator, fator, fator*2, ID.player, handler,6,game,ss);
			handler.add_object(player);
			handler.setPlayer(player);		
		} 
		
	}
}