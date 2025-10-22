package org.lwjgl.util.glu;

import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;

public class GLU {

	public static String gluErrorString(int param) {
		return EaglercraftGPU.gluErrorString(param);
	}

	public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
		GlStateManager.gluPerspective(fovy, aspect, zNear, zFar);
	}

}
