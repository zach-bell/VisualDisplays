package core;

import processing.core.PApplet;
import processing.core.PVector;

public class LinearApplet extends PApplet {
	
	// main and initialization of applet
	public static void main(String args[]) {
		PApplet.main("core.LinearApplet");
	}
	// Settings of applet
	public void settings() {
		size(800, 800);
	}
	
	// Graph variables
	private PVector[] points = new PVector[100];	// Default 100 points
	private float slope = 1.1f;						// Default 1 for slope
	private int scale = 8;							// Default 10 for size
	private int pointScatter = 120;					// Default 50 for deviance
	
	// Setup and initial variable assigning.
	public void setup() {
		surface.setTitle("Linear R Assumption");
		frameRate(60);
		
		// Initializers
		int x = 0, y = 0;
		for (int i = 0; i < points.length; i ++) {
			points[i] = new PVector(x + (int) random(-pointScatter, pointScatter),
									y + (int) random(-pointScatter, pointScatter));
			x += scale;
			y = (int) (x * slope);
		}
	}
	

	// Guess Variables
	private float guessSlope = 4;
	private float minimumDistance = 9999999;
	private float minimumSlope = 1;
	
	public void draw() {
		background(40);
		noFill();
		
		pushMatrix();
		rotate(PI / -2);
		translate(-width, 0);
		// Draw Graph
		stroke(100);
		strokeWeight(1);
		for (int x = 0; x < width; x += scale) {
			for (int y = 0; y < height; y += scale) {
				rect(x, y, scale, scale);
			}
		}

		// Draw points
		stroke(255);
		strokeWeight(4);
		beginShape(POINTS);
		for (PVector point : points) {
			vertex(point.x, point.y);
		}
		endShape();
		
		drawGuessData();
		drawMinSlope();
		
		popMatrix();
		drawUI();
	}
	
	private void drawGuessData() {
		stroke(0, 200, 255);
		line(0, 0, width, height * guessSlope);
		strokeWeight(0.5f);
		
		float sumOfDistance = 0;
		if (guessSlope > 0) {
			for (PVector point : points) {
				line(point.x, point.y, point.x, (point.x * guessSlope));
				sumOfDistance += getDistance(point, new PVector(point.x, (point.x * guessSlope)));
			}
			if (sumOfDistance < minimumDistance) {
				minimumDistance = sumOfDistance;
				minimumSlope = guessSlope;
			}
			guessSlope -= 0.01f;
		}
	}
	
	private void drawMinSlope() {
		strokeWeight(2);
		stroke(200, 0, 255);
		line(0, 0, width, height * minimumSlope);
	}
	
	private float getDistance(PVector p1, PVector p2) {
		return sqrt(pow((p2.x - p1.x), 2) + pow((p2.y - p1.y), 2));
	}
	
	// Draws the text on screen
	private void drawUI() {
		noStroke();
		fill(40);
		rect(width - 110, height - 36, 110, 36);
		fill(255);
		textSize(14);
		text("Slope: " + guessSlope, width - 100, height - 18);
		text("Min: " + minimumSlope, width - 100, height - 2);
	}
}
