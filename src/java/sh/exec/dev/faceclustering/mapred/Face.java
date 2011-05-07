package info.iylk.dev.faceclustering.mapred;

import de.offis.faint.model.Region;

public class Face {

	private static final long serialVersionUID = -2298863311467678659L;
	
	protected String image;
	protected int x;
	protected int y;
	protected int height;
	protected int width;
	protected int angle;
	protected double value;

	
	
	public Face() {
		
	}
	
	public Face(Region region, Double value) {
		
		
		this.image = region.getImage();
		this.x = region.getX();
		this.y = region.getY();
		this.height = region.getHeight();
		this.width = region.getWidth();
		this.value = value;
		
	}
	
	public String getImage() {
		return this.image;
	}

	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	public int getHeight() {
		return this.height;
	}
	public int getWidth() {
		return this.width;
	}
	public int getAngle() {
		return this.angle;
	}
	
	public double getValue() {
		return this.value;
	}
	
	
}

