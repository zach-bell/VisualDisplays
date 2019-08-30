package core;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class VisualizerApp extends PApplet {
	
	public static void main(String[] args) {
		PApplet.main("core.VisualizerApp");
	}
	
// Noise
	private int cols, rows, slats, scale = 200;
	private float xIncre = 0, yIncre = 0, zIncre = 0, incre = 0.05f;
	private NoiseStruct noiseGrid[][][];
	
// Camera Vars
	private float rotateCameraZ = 0;
	private float rotateCameraX = PI / 2.5f;
	private int mouseLastX = 0, mouseLastY = 0;
	private float mouseScrollFactor = 1;
	private float mouseWheel = 0;
	private boolean dragedOneTime = true;
	
	public void settings() {
		size(1240, 720, P3D);
	}
	
	public void setup() {
		cols = 5;
		rows = 5;
		slats = 5;
		
		noiseGrid = new NoiseStruct[rows][cols][slats];
		// Init all them
		for (int x = 0; x < rows; x++)
			for (int y = 0; y < cols; y++)
				for (int z = 0; z < slats; z++)
					noiseGrid[x][y][z] = new NoiseStruct();
	}
	
	public void draw() {
		background(10);
		
		translate(width / 2, height / 2);
		rotateX(rotateCameraX);
		rotateZ(rotateCameraZ);
		scale(mouseScrollFactor, mouseScrollFactor, mouseScrollFactor);
		translate(-rows * scale / 2, -cols * scale / 2, -slats * scale / 2);
		
		getMouseDragging();
		
		noFill();
		strokeWeight(3);
		
		populate3Dnoise();
		
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				for (int z = 0; z < slats; z++) {
					pushMatrix();
					translate(x * scale, y * scale, z * scale);

					rotateX(map(noiseGrid[x][y][z].noiseValue, 0, 1, -(2 * PI), 2 * PI));
					rotateZ(map(-noiseGrid[x][y][z].noiseValue, -1, 0, -(2 * PI), 2 * PI));
					rotateY(map(noiseGrid[x][y][z].noiseValue, 0, 1, -(2 * PI), 2 * PI));
					
				// Color
					stroke(noiseGrid[x][y][z].r,		// r
						noiseGrid[x][y][z].g,		// g
						noiseGrid[x][y][z].b,		// b
						(int) (map(noiseGrid[x][y][z].noiseValue, 0, 1, 100, 255)));		// alpha
					
				// Drawing methods
//					line((scale / 2), (scale / 2), -(scale / 2), scale / 1.5f, scale / 1.5f, scale / 1.5f);
					box(scale);
					
					popMatrix();
				}
			}
		}
		xIncre += 0.005f;
		yIncre += 0.005f;
		zIncre += 0.005f;
	}
	
	private void populate3Dnoise() {
		float xOff = xIncre;
		for (int x = 0; x < rows; x++) {
			float yOff = yIncre;
			for (int y = 0; y < cols; y++) {
				float zOff = zIncre;
				for (int z = 0; z < slats; z++) {
					noiseGrid[x][y][z].noiseValue = noise(xOff, yOff, zOff);
					noiseGrid[x][y][z].r = (int) map(noise(xOff + 1, yOff + 1, zOff + 1), 0.25f, 0.75f, 100, 255);
					noiseGrid[x][y][z].g = (int) map(noise(xOff + 2, yOff + 2, zOff + 2), 0.25f, 0.75f, 100, 255);
					noiseGrid[x][y][z].b = (int) map(noise(xOff + 3, yOff + 3, zOff + 3), 0.25f, 0.75f, 100, 255);
					zOff += incre;
				}
				yOff += incre;
			}
			xOff += incre;
		}
	}
	
	private void getMouseDragging() {
		if (!mousePressed & (mouseX > 0) & (mouseY > 0) & (mouseX < width) & (mouseY < height)) {
			mouseLastX = mouseX;
			mouseLastY = mouseY;
		}
		if (mousePressed & (mouseX > 0) & (mouseY > 0) & (mouseX < width) & (mouseY < height)) {
			if (mouseX < (mouseLastX)-5) {
				rotateCameraZ += map(mouseX,mouseLastX,(mouseLastX - width),0,0.07f);
			} else if (mouseX > (mouseLastX)+5) {
				rotateCameraZ -= map(mouseX,mouseLastX,(mouseLastX + width),0,0.07f);
			}
			if (mouseY < (mouseLastY)-5) {
				rotateCameraX += map(mouseY,mouseLastY,(mouseLastY - width),0,0.07f);
			} else if (mouseY > (mouseLastY)+5) {
				rotateCameraX -= map(mouseY,mouseLastY,(mouseLastY + width),0,0.07f);
			}
			dragedOneTime = true;
		} else if (dragedOneTime) {
			dragedOneTime = false;
		}
	}
	
	// Listens for the mouse wheel to scroll the camera
	public void mouseWheel(MouseEvent event) {
		mouseWheel = event.getCount();
		// e > 0 means mouseWheelUp, while e < 0 means mouseWheelDown
		if (mouseWheel > 0) {
			mouseScrollFactor -= 0.07f;
		} else if (mouseWheel < 0) {
			mouseScrollFactor += 0.07f;
		}
	}
}
