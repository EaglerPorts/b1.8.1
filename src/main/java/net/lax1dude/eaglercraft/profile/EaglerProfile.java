/*
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.profile;

import java.io.IOException;
import java.util.ArrayList;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.lax1dude.eaglercraft.HString;
import net.lax1dude.eaglercraft.Random;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;

public class EaglerProfile {

	private static String username = getDefaultUsername();

	public static int presetSkinId;
	public static int customSkinId;
	public static ArrayList<EaglerProfileSkin> skins = new ArrayList<>();

	public static class EaglerProfileSkin {
		public String name;
		public byte[] data;
		public int glTex;
		public EaglerProfileSkin(String name, byte[] data, int glTex) {
			this.name = name;
			this.data = data;
			this.glTex = glTex;
		}
	}

	public static int addSkin(String name, byte[] data) {
		int i = -1;
		for (int j = 0, l = skins.size(); j < l; ++j) {
			if (skins.get(j).name.equalsIgnoreCase(name)) {
				i = j;
				break;
			}
		}
		
		int im = Minecraft.getMinecraft().renderEngine.allocateAndSetupTexture(ImageData.loadImageFile(data));
		if (i == -1) {
			i = skins.size();
			skins.add(new EaglerProfileSkin(name, data, im));
		} else {
			skins.get(i).glTex = im;
			skins.get(i).data = data;
		}
		return i;
	}

	public static String[] concatArrays(String[] a, String[] b) {
		String[] r = new String[a.length + b.length];
		System.arraycopy(a, 0, r, 0, a.length);
		System.arraycopy(b, 0, r, a.length, b.length);
		return r;
	}
	
	public static String getName() {
		return username;
	}

	public static void setName(String str) {
		username = str;
		Minecraft mc = Minecraft.getMinecraft();
		if(mc != null && mc.session != null) {
			mc.session.username = str;
		}
	}

	public static void read() {
		read(EagRuntime.getStorage("p"));
	}

	public static void read(byte[] profileStorage) {
		if (profileStorage == null) {
			return;
		}

		NBTTagCompound profile;
		try {
			profile = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new EaglerInputStream(profileStorage));
		}catch(IOException ex) {
			return;
		}

		if (profile == null || profile.hasNoTags()) {
			return;
		}

		String loadUsername = profile.getString("username").trim();
		int loadPresetSkin = profile.getInteger("presetSkin");

		if(!loadUsername.isEmpty()) {
			username = loadUsername.replaceAll("[^A-Za-z0-9]", "_");
		}

		if(loadPresetSkin > 0) {
			presetSkinId = loadPresetSkin;
		}
	}

	public static byte[] write() {
		NBTTagCompound profile = new NBTTagCompound();
		profile.setString("username", username);
		profile.setInteger("presetSkin", presetSkinId);
		EaglerOutputStream bao = new EaglerOutputStream();
		try {
			CompressedStreamTools.writeGzippedCompoundToOutputStream(profile, bao);
		} catch (IOException e) {
			return null;
		}
		return bao.toByteArray();
	}

	public static void save() {
		byte[] b = write();
		if(b != null) {
			EagRuntime.setStorage("p", b);
		}
	}

	static {
		read();
	}

	public static String getDefaultUsername() {
		String[] defaultNames = new String[] {
			"Yeeish", "Yeeish", "Yee", "Yee", "Yeer", "Yeeler", "Eagler", "Eagl",
			"Darver", "Darvler", "Vool", "Vigg", "Vigg", "Deev", "Yigg", "Yeeg"
		};
		
		Random rand = new Random();

		String name;

		do {
			name = HString.format("%s%s%04d", defaultNames[rand.nextInt(defaultNames.length)], defaultNames[rand.nextInt(defaultNames.length)], rand.nextInt(10000));
		} while (name.length() > 16);

		return name;
	}

	public static boolean isDefaultUsername(String str) {
		return str.toLowerCase().matches("^(yeeish|yee|yeer|yeeler|eagler|eagl|darver|darvler|vool|vigg|deev|yigg|yeeg){2}\\d{2,4}$");
	}

	static {
		read();
	}

}