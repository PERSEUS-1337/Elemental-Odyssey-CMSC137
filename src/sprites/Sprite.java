package sprites;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image img;
	protected int x, y, dx, dy;
	protected boolean visible;
	protected double width;
	protected double height;

	public static final int SPRITE_DEFAULT_WIDTH = 32;
	public static final int SPRITE_DEFAULT_HEIGHT = 32;
	public static final float SCALE = 1.5f;
	public static final int SPRITE_WIDTH = (int) (SPRITE_DEFAULT_WIDTH * SCALE);
	public static final int SPRITE_HEIGHT = (int) (SPRITE_DEFAULT_HEIGHT * SCALE);
	
	public Sprite(int xPos, int yPos){
		this.x = xPos;
		this.y = yPos;
		this.visible = true;
	}
	
	//method to set the object's image
	protected void loadImage(Image img){
		try{
			this.img = img;
	        this.setSize();
		} catch(Exception e){}
	}
	
	//method to set the image to the image view node
	public void render(GraphicsContext gc){
		gc.drawImage(this.img, this.x, this.y);
        
    }
	
	//method to set the object's width and height properties
	private void setSize(){
		this.width = this.img.getWidth();
	    this.height = this.img.getHeight();
	}
	
	//method that will check for collision of two sprites
	public boolean collidesWith(Sprite rect2)	{
		if (rect2 == null) return false;

		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

		// this.y -= rect2.height;

		boolean intersects = rectangle1.intersects(rectangle2);

		if (intersects){
			double distX = rect2.getCenterX() - this.getCenterX();
			double distY = rect2.getCenterY() - this.getCenterY();

			// determine which direction
			double absDx = Math.abs(distX);
			double absDy = Math.abs(distY);

			if (absDx > absDy){
				if (distX < 0){
					//right
					// setDX(Math.abs(dx));
					// x = rect2.getCenterX() + Sprite.SPRITE_WIDTH;
					this.x = (rect2.getX() + (int) this.getWidth());
				} else {
					//left
					// setDX(-Math.abs(dx));
					// x = rect2.getCenterX() - Sprite.SPRITE_WIDTH;
					this.x = (rect2.getX() - (int) this.getWidth());
				}

			} else {
				if (distY < 0){
					//bottom
					// this.setDY(Math.abs(dy));
					// y = rect2.getCenterY() + Sprite.SPRITE_HEIGHT;
					this.y = (rect2.getY() + (int) this.getHeight());
				} else {
					//top
					// this.setDY(-Math.abs(dy));
					// y = rect2.getCenterY() - Sprite.SPRITE_HEIGHT;
					this.y = (rect2.getY() - (int) this.getHeight());
				}
			}
		}

		// double myLeft = this.getCenterX();
		// double myRight = this.getCenterX() + Sprite.SPRITE_WIDTH;
		// double myTop = this.getCenterY();
		// double myBottom = this.getCenterY() + Sprite.SPRITE_WIDTH;
		
		// double otherLeft = rect2.getCenterX();
		// double otherRight = rect2.getCenterX() + Sprite.SPRITE_WIDTH;
		// double otherTop = rect2.getCenterY();
		// double otherBottom = rect2.getCenterY() + Sprite.SPRITE_HEIGHT;

		// if (myBottom <= otherTop) {
		// 	System.out.println("first");
		// 	setDY(-Math.abs(dy));
		// 	y = rect2.getCenterY() - Sprite.SPRITE_HEIGHT;
		// 	return true;
		// }

		// if (myTop >= otherBottom) {
		// 	System.out.println("second");
		// 	setDY(Math.abs(dy));
		// 	y = rect2.getCenterY() + Sprite.SPRITE_HEIGHT;
		// 	return true;
		// }

		// if (myRight <= otherLeft) {
		// 	System.out.println("third");
		// 	setDX(-Math.abs(dx));
		// 	x = rect2.getCenterX() - Sprite.SPRITE_WIDTH;
		// 	return true;
		// }

		// if (myLeft <= otherRight) {
		// 	System.out.println("fourth");
		// 	setDX(Math.abs(dx));
		// 	x = rect2.getCenterX() + Sprite.SPRITE_WIDTH;
		// 	return true;
		// }

		return intersects;
	}
	

	//method that will return the bounds of an image
	private Rectangle2D getBounds(){
		return new Rectangle2D(this.x, this.y, this.width, this.height);
	}
	
	//method to return the image
	Image getImage(){
		return this.img;
	}
	//getters
	public int getX() {
    	return this.x;
	}

	public int getY() {
    	return this.y;
	}
	
	public int getCenterX(){
		return this.x + (Sprite.SPRITE_WIDTH / 2);
	}

	public int getCenterY(){
		return this.y + (Sprite.SPRITE_HEIGHT / 2);
	}

	public double getWidth(){
		return this.width;
	}

	public double getHeight(){
		return this.height;
	}

	public boolean getVisible(){
		return visible;	
	}
	public boolean isVisible(){
		if(visible) return true;
		return false;
	}
	
	//setters
	public void setDX(int dx){
		this.dx = dx;
	}
	
	public void setDY(int dy){
		this.dy = dy;
	}
	
	public void setWidth(double val){
		this.width = val;
	}

	public void setHeight(double val){
		this.height = val;
	}
		
	public void setVisible(boolean value){
		this.visible = value;
	}
	

}
