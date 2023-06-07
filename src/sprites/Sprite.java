package sprites;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image img;
	protected int x, y;
	protected boolean flipped;
	public void setY(int y) {
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	protected double dx, dy;
	protected boolean visible;
	protected double width;
	protected double height;

	public static final int SPRITE_DEFAULT_WIDTH = 32;
	public static final int SPRITE_DEFAULT_HEIGHT = 32;
	public static final float SCALE = 1.0f;
	public static final int SPRITE_WIDTH = (int) (SPRITE_DEFAULT_WIDTH * SCALE);
	public static final int SPRITE_HEIGHT = (int) (SPRITE_DEFAULT_HEIGHT * SCALE);
	
	public Sprite(int xPos, int yPos){
		this.x = xPos;
		this.y = yPos;
		this.visible = true;
		this.flipped = false;
	}

	// method to make the sprite face either left or right
	public void setFlipped(boolean flipped){
		this.flipped = flipped;
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
        if (flipped) { // flip the image
            gc.save();
            gc.scale(-1, 1);
            gc.drawImage(this.img, -this.x - this.width, this.y);
            gc.restore();
        } else { // render normally
            gc.drawImage(this.img, this.x, this.y);
        }
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

		return rectangle1.intersects(rectangle2);
	}
	
	// Getters
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	private Rectangle2D getBounds(){
		return new Rectangle2D(getX(), getY(), getWidth(), getHeight());
	}
	
	public Image getImage(){
		return this.img;
	}

	public int getCenterX(){
		return getX() + ((int) getWidth() / 2);
	}

	public int getCenterY(){
		return getY() + ((int) getHeight() / 2);
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
		if(getVisible()) return true;
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
