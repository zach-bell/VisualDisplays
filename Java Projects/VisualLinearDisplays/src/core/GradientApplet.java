package core;

import processing.core.PApplet;
import processing.core.PVector;

public class GradientApplet extends PApplet {
	
	// main and initialization of applet
	public static void main(String args[]) {
		PApplet.main("core.GradientApplet");
	}
	// Settings of applet
	public void settings() {
		size(windowSize, windowSize);
	}
	
	// Processing Variables
	private static int cyclesPerSecond = 60;				// Default fps 60
	private static int windowSize = 900;
	private boolean pause = true;
	
	// Graph variables
	private static int numberOfPoints = 2;				// Default 100 points
	private PVector[] points;
	
	// Guess Variables
	// Try not to mess with these as they change while its running.
	private float alpha = 0.05f;
	private float slope = 0;
	private float intercept = 0;
	
	// Setup and initial variable assigning.
	public void setup() {
		surface.setTitle("Visual Gradiant Decent");
		frameRate(cyclesPerSecond);
		
		// Initialize points
		points = new PVector[numberOfPoints];
		for (int i = 0; i < points.length; i++) {
			points[i] = new PVector(random(0, 1), random(0, 1));
		}
	}
	
	// Processing main draw loop override.
	public void draw() {
		background(40);
		noFill();
		
		// Draws borders
		strokeWeight(4);
		line(0, 0, width, 0);
		line(1, 0, 1, height);

		// Draw points
		stroke(255);
		strokeWeight(4);
		
		gradientDescent();
		drawPoints();
		drawLine();
		
		drawUI();
	}
	
	private void gradientDescent() {
		if (!pause) {
			for (int i = 0; i < points.length; i++) {
				float px = points[i].x;
				float py = points[i].y;
				float guess = (slope * px) + intercept;
				float error = py - guess;
				
				slope = (slope + ((error * px) * alpha));
				intercept = (intercept + (error * alpha));
			}
		}
	}
	
	private void drawLine() {
		float x1 = 0;
		float y1 = slope * x1 + intercept;
		float x2 = 1;
		float y2 = slope * x2 + intercept;
		
		x1 = map(x1, 0, 1, 0, width);
		y1 = map(y1, 0, 1, height, 0);
		x2 = map(x2, 0, 1, 0, width);
		y2 = map(y2, 0, 1, height, 0);
		
		stroke(255);
		strokeWeight(1);
		line(x1, y1, x2, y2);
	}
	
	private void drawPoints() {
		for (int i = 0; i < points.length; i++) {
			float x = map(points[i].x, 0, 1, 0, width);
			float y = map(points[i].y, 0, 1, height, 0);
			
			fill(150, 255, 255);
			noStroke();
			ellipse(x, y, 4, 4);
		}
	}
	
	// Draws the text on screen
	private void drawUI() {
		noStroke();
		textSize(20);
		textAlign(RIGHT);
		fill(255);

		text("FPS: " + (int) frameRate, width - 20, 20);
		text("Slope: " + slope, width - 20, height - 45);
		text("y-int: " + intercept, width - 20, height - 20);
		
		if (pause) {
			stroke(255,255,0);
			line(0, 1, width, 1);
			fill(255,255,0);
			textAlign(CENTER);
			text("PAUSED \t\t Press Space to Unpause", width / 2, 20);
		}
	}
	
	// Processing keypressed method override
	public void keyPressed() {
		// Spacebar
		if (keyCode == 32) {
			pause = !pause;
		}
		// Down arrow
		if (keyCode == 40) {
			if (cyclesPerSecond - 2 >= 2)
				cyclesPerSecond -= 2;
		}
		// Up arrow
		if (keyCode == 38) {
			if (cyclesPerSecond + 2 <= 100)
				cyclesPerSecond += 2;
		}
		// R key
		if (keyCode == 82) {
//			reset();
		}
		// Set frameRate limit again
		frameRate(cyclesPerSecond);
	}
}
