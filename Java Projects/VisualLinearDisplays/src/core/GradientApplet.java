package core;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import tools.FileIn;

public class GradientApplet extends PApplet {
	
	// main and initialization of applet
	public static void main(String args[]) {
		PApplet.main("core.GradientApplet");
	}
	// Settings of applet
	public void settings() {
		size(windowSize, windowSize, P3D);
	}
	
	// Processing Variables
	private static int cyclesPerSecond = 60;			// Default fps 60
	private static int windowSize = 900;
	private boolean pause = true;
	
	// Camera variables
	private float cameraY = PI / 6;
	private float cameraZ = 0;
	private float cameraIncre = 0.0174533f;
	private PVector initialClick;
	private boolean isDragged = false;
	
	// Graph variables
	private PVector[] data;								// data is training file
	private float maxWeight = 0, maxHP = 0, maxMPG = 0;
	
	// Guess Variables
	// Try not to mess with these as they change while its running.
	private float alpha = 0.05f;
	private float slope = 0;
	private float intercept = 0;
	
	// Setup and initial variable assigning.
	public void setup() {
		surface.setTitle("Visual Gradiant Decent");
		frameRate(cyclesPerSecond);
		
		initialClick = new PVector();
		
		handleFileInput();
	}
	
	// Processing main draw loop override.
	public void draw() {
		background(40);
		noFill();

		drawUI();
		// camera translations
		translate(width / 2, 0, -windowSize);
		rotateY(cameraY);
		rotateZ(cameraZ);
		
		pushMatrix();
		translate(-width / 2, 0, -windowSize / 2);
		
		// Draw 3d grid
		strokeWeight(1);
		stroke(150,0,0);
		line(0, height, 0, width, height, 0);
		line(0, height, 0, 0, 0, 0);
		line(0, height, 0, 0, height, windowSize);
		
		// Draw data points
		stroke(255);
		strokeWeight(4);
		
		if (!pause) {
			gradientDescent();
			drawLine();
		}
		drawData();
		popMatrix();
		
		if (isDragged) {
			cameraY += map(mouseX - initialClick.x, 0, windowSize, 0, cameraIncre);
			if (!mousePressed) {
				isDragged = false;
			}
		}
	}
	
	private void gradientDescent() {
		for (int i = 0; i < data.length; i++) {
			float px = data[i].x;
			float py = data[i].y;
			float guess = (slope * px) + intercept;
			float error = py - guess;
			
			slope = (slope + ((error * px) * alpha));
			intercept = (intercept + (error * alpha));
		}
	}
	
	private void drawLine() {
		float x1 = 0;
		float y1 = slope * x1 + intercept;
		float z1 = slope * x1;
		float x2 = 1;
		float y2 = slope * x2 + intercept;
		float z2 = slope * x2 + intercept;
		
		x1 = map(x1, 0, 1, 0, width);
		y1 = map(y1, 0, 1, height, 0);
		z1 = map(z1, 0, 1, 0, windowSize);
		
		x2 = map(x2, 0, 1, 0, width);
		y2 = map(y2, 0, 1, height, 0);
		z2 = map(z2, 0, 1, 0, windowSize);
		
		stroke(255);
		strokeWeight(1);
		line(x1, y1, z1, x2, y2, z2);
	}
	
	// Draws the data from the fileInput
	private void drawData() {
		for (int i = 0; i < data.length; i++) {
			float x = map(data[i].x, 0, 1, 0, width);
			float y = map(data[i].y, 0, 1, height, 0);
			float z = map(data[i].z, 0, 1, height, 0);

			pushMatrix();
			translate(x, y, z);
			noStroke();
			fill(150, 255, 255);
			sphere(4);
			popMatrix();
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
	
	// This method takes in the trainingData.csv and organizes it cleanly
	// It populates the data PVector array where x = weight, y = hp, z = mpg
	private void handleFileInput() {
		String[][] sData = FileIn.getData("res/trainingData.csv");
		ArrayList<PVector> rawData = new ArrayList<PVector>();
		
		for (int i = 0; i < sData.length; i++) {
			try {
				float x = Float.isNaN(Float.parseFloat(sData[i][0])) ? 0/0 : Float.parseFloat(sData[i][0]);
				float y = Float.isNaN(Float.parseFloat(sData[i][1])) ? 0/0 : Float.parseFloat(sData[i][1]);
				float z = Float.isNaN(Float.parseFloat(sData[i][2])) ? 0/0 : Float.parseFloat(sData[i][2]);
				
				// Check for new max values for normalizing
				if (x > maxWeight)
					maxWeight = x;
				if (y > maxHP)
					maxHP = y;
				if (z > maxMPG)
					maxMPG = z;
				
				rawData.add(new PVector(x, y, z));
				
			} catch(ArithmeticException e) {
				println("NaN happened at line " + (i + 1));
			}
		}
		
		data = new PVector[rawData.size()];
		for (int i = 0; i < data.length; i++) {
			float x = map(rawData.get(i).x, 0, maxWeight, 0, 1);
			float y = map(rawData.get(i).y, 0, maxHP, 0, 1);
			float z = map(rawData.get(i).z, 0, maxMPG, 0, 1);
			
			data[i] = new PVector(x, y, z);
		}
	}
	
	public void mouseDragged() {
		isDragged = true;
	}
	
	public void mousePressed() {
		initialClick = new PVector(mouseX, mouseY);
	}
	
	// Processing keypressed method override
	public void keyPressed() {
		// Spacebar
		if (keyCode == 32) {
			pause = !pause;
		}
		// Down arrow
		if (keyCode == 40) {
			cameraY += 0.0174533f;
		}
		// Up arrow
		if (keyCode == 38) {
			cameraY -= 0.0174533f;
		}
		// R key
		if (keyCode == 82) {
//			reset();
		}
		// Set frameRate limit again
		frameRate(cyclesPerSecond);
	}
}
