package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.peyton.eagler.minecraft.suppliers.PacketSupplier;

public abstract class Packet {
	private static MCHash packetIdToClassMap = new MCHash();
	private static Map packetClassToIdMap = new HashMap();
	private static Set clientPacketIdList = new HashSet();
	private static Set serverPacketIdList = new HashSet();
	public final long creationTimeMillis = System.currentTimeMillis();
	public boolean isChunkDataPacket = false;
	private static MCHash totalPacketsCount;
	private static int packetStats;

	static void addIdClassMapping(int var0, boolean var1, boolean var2, Class var3, PacketSupplier var4) {
		if(packetIdToClassMap.func_35858_b(var0)) {
			throw new IllegalArgumentException("Duplicate packet id:" + var0);
		} else if(packetClassToIdMap.containsKey(var3)) {
			throw new IllegalArgumentException("Duplicate packet class:" + var3);
		} else {
			packetIdToClassMap.addKey(var0, var4);
			packetClassToIdMap.put(var3, Integer.valueOf(var0));
			if(var1) {
				clientPacketIdList.add(Integer.valueOf(var0));
			}

			if(var2) {
				serverPacketIdList.add(Integer.valueOf(var0));
			}

		}
	}

	public static Packet getNewPacket(int var0) {
		try {
			PacketSupplier var1 = (PacketSupplier)packetIdToClassMap.lookup(var0);
			return var1 == null ? null : var1.createPacket();
		} catch (Exception var2) {
			var2.printStackTrace();
			System.out.println("Skipping packet with id " + var0);
			return null;
		}
	}

	public final int getPacketId() {
		return ((Integer)packetClassToIdMap.get(this.getClass())).intValue();
	}

	public static Packet readPacket(DataInputStream var0, boolean var1) throws IOException {
		boolean var2 = false;
		Packet var3 = null;

		int var6;
		try {
			var6 = var0.read();
			if(var6 == -1) {
				return null;
			}

			if(var1 && !serverPacketIdList.contains(Integer.valueOf(var6)) || !var1 && !clientPacketIdList.contains(Integer.valueOf(var6))) {
				throw new IOException("Bad packet id " + var6);
			}

			var3 = getNewPacket(var6);
			if(var3 == null) {
				throw new IOException("Bad packet id " + var6);
			}

			var3.readPacketData(var0);
		} catch (EOFException var5) {
			System.out.println("Reached end of stream");
			return null;
		}

		PacketCounter var4 = (PacketCounter)totalPacketsCount.lookup(var6);
		if(var4 == null) {
			var4 = new PacketCounter((Empty1)null);
			totalPacketsCount.addKey(var6, var4);
		}

		var4.addPacket(var3.getPacketSize());
		++packetStats;
		if(packetStats % 1000 == 0) {
		}

		return var3;
	}

	public static void writePacket(Packet var0, DataOutputStream var1) throws IOException {
		var1.write(var0.getPacketId());
		var0.writePacketData(var1);
	}

	public static void writeString(String var0, DataOutputStream var1) throws IOException {
		if(var0.length() > Short.MAX_VALUE) {
			throw new IOException("String too big");
		} else {
			var1.writeShort(var0.length());
			var1.writeChars(var0);
		}
	}

	public static String readString(DataInputStream var0, int var1) throws IOException {
		short var2 = var0.readShort();
		if(var2 > var1) {
			throw new IOException("Received string length longer than maximum allowed (" + var2 + " > " + var1 + ")");
		} else if(var2 < 0) {
			throw new IOException("Received string length is less than zero! Weird string!");
		} else {
			StringBuilder var3 = new StringBuilder();

			for(int var4 = 0; var4 < var2; ++var4) {
				var3.append(var0.readChar());
			}

			return var3.toString();
		}
	}

	public abstract void readPacketData(DataInputStream var1) throws IOException;

	public abstract void writePacketData(DataOutputStream var1) throws IOException;

	public abstract void processPacket(NetHandler var1);

	public abstract int getPacketSize();

	static {
		addIdClassMapping(0, true, true, Packet0KeepAlive.class, Packet0KeepAlive::new);
		addIdClassMapping(1, true, true, Packet1Login.class, Packet1Login::new);
		addIdClassMapping(2, true, true, Packet2Handshake.class, Packet2Handshake::new);
		addIdClassMapping(3, true, true, Packet3Chat.class, Packet3Chat::new);
		addIdClassMapping(4, true, false, Packet4UpdateTime.class, Packet4UpdateTime::new);
		addIdClassMapping(5, true, false, Packet5PlayerInventory.class, Packet5PlayerInventory::new);
		addIdClassMapping(6, true, false, Packet6SpawnPosition.class, Packet6SpawnPosition::new);
		addIdClassMapping(7, false, true, Packet7UseEntity.class, Packet7UseEntity::new);
		addIdClassMapping(8, true, false, Packet8UpdateHealth.class, Packet8UpdateHealth::new);
		addIdClassMapping(9, true, true, Packet9Respawn.class, Packet9Respawn::new);
		addIdClassMapping(10, true, true, Packet10Flying.class, Packet10Flying::new);
		addIdClassMapping(11, true, true, Packet11PlayerPosition.class, Packet11PlayerPosition::new);
		addIdClassMapping(12, true, true, Packet12PlayerLook.class, Packet12PlayerLook::new);
		addIdClassMapping(13, true, true, Packet13PlayerLookMove.class, Packet13PlayerLookMove::new);
		addIdClassMapping(14, false, true, Packet14BlockDig.class, Packet14BlockDig::new);
		addIdClassMapping(15, false, true, Packet15Place.class, Packet15Place::new);
		addIdClassMapping(16, false, true, Packet16BlockItemSwitch.class, Packet16BlockItemSwitch::new);
		addIdClassMapping(17, true, false, Packet17Sleep.class, Packet17Sleep::new);
		addIdClassMapping(18, true, true, Packet18Animation.class, Packet18Animation::new);
		addIdClassMapping(19, false, true, Packet19EntityAction.class, Packet19EntityAction::new);
		addIdClassMapping(20, true, false, Packet20NamedEntitySpawn.class, Packet20NamedEntitySpawn::new);
		addIdClassMapping(21, true, false, Packet21PickupSpawn.class, Packet21PickupSpawn::new);
		addIdClassMapping(22, true, false, Packet22Collect.class, Packet22Collect::new);
		addIdClassMapping(23, true, false, Packet23VehicleSpawn.class, Packet23VehicleSpawn::new);
		addIdClassMapping(24, true, false, Packet24MobSpawn.class, Packet24MobSpawn::new);
		addIdClassMapping(25, true, false, Packet25EntityPainting.class, Packet25EntityPainting::new);
		addIdClassMapping(26, true, false, Packet26EntityExpOrb.class, Packet26EntityExpOrb::new);
		addIdClassMapping(27, false, true, Packet27Position.class, Packet27Position::new);
		addIdClassMapping(28, true, false, Packet28EntityVelocity.class, Packet28EntityVelocity::new);
		addIdClassMapping(29, true, false, Packet29DestroyEntity.class, Packet29DestroyEntity::new);
		addIdClassMapping(30, true, false, Packet30Entity.class, Packet30Entity::new);
		addIdClassMapping(31, true, false, Packet31RelEntityMove.class, Packet31RelEntityMove::new);
		addIdClassMapping(32, true, false, Packet32EntityLook.class, Packet32EntityLook::new);
		addIdClassMapping(33, true, false, Packet33RelEntityMoveLook.class, Packet33RelEntityMoveLook::new);
		addIdClassMapping(34, true, false, Packet34EntityTeleport.class, Packet34EntityTeleport::new);
		addIdClassMapping(38, true, false, Packet38EntityStatus.class, Packet38EntityStatus::new);
		addIdClassMapping(39, true, false, Packet39AttachEntity.class, Packet39AttachEntity::new);
		addIdClassMapping(40, true, false, Packet40EntityMetadata.class, Packet40EntityMetadata::new);
		addIdClassMapping(41, true, false, Packet41EntityEffect.class, Packet41EntityEffect::new);
		addIdClassMapping(42, true, false, Packet42RemoveEntityEffect.class, Packet42RemoveEntityEffect::new);
		addIdClassMapping(43, true, false, Packet43Experience.class, Packet43Experience::new);
		addIdClassMapping(50, true, false, Packet50PreChunk.class, Packet50PreChunk::new);
		addIdClassMapping(51, true, false, Packet51MapChunk.class, Packet51MapChunk::new);
		addIdClassMapping(52, true, false, Packet52MultiBlockChange.class, Packet52MultiBlockChange::new);
		addIdClassMapping(53, true, false, Packet53BlockChange.class, Packet53BlockChange::new);
		addIdClassMapping(54, true, false, Packet54PlayNoteBlock.class, Packet54PlayNoteBlock::new);
		addIdClassMapping(60, true, false, Packet60Explosion.class, Packet60Explosion::new);
		addIdClassMapping(61, true, false, Packet61DoorChange.class, Packet61DoorChange::new);
		addIdClassMapping(70, true, false, Packet70Bed.class, Packet70Bed::new);
		addIdClassMapping(71, true, false, Packet71Weather.class, Packet71Weather::new);
		addIdClassMapping(100, true, false, Packet100OpenWindow.class, Packet100OpenWindow::new);
		addIdClassMapping(101, true, true, Packet101CloseWindow.class, Packet101CloseWindow::new);
		addIdClassMapping(102, false, true, Packet102WindowClick.class, Packet102WindowClick::new);
		addIdClassMapping(103, true, false, Packet103SetSlot.class, Packet103SetSlot::new);
		addIdClassMapping(104, true, false, Packet104WindowItems.class, Packet104WindowItems::new);
		addIdClassMapping(105, true, false, Packet105UpdateProgressbar.class, Packet105UpdateProgressbar::new);
		addIdClassMapping(106, true, true, Packet106Transaction.class, Packet106Transaction::new);
		addIdClassMapping(107, true, true, Packet107CreativeSetSlot.class, Packet107CreativeSetSlot::new);
		addIdClassMapping(130, true, true, Packet130UpdateSign.class, Packet130UpdateSign::new);
		addIdClassMapping(131, true, false, Packet131MapData.class, Packet131MapData::new);
		addIdClassMapping(200, true, false, Packet200Statistic.class, Packet200Statistic::new);
		addIdClassMapping(201, true, false, Packet201PlayerInfo.class, Packet201PlayerInfo::new);
		addIdClassMapping(254, false, true, Packet254ServerPing.class, Packet254ServerPing::new);
		addIdClassMapping(255, true, true, Packet255KickDisconnect.class, Packet255KickDisconnect::new);
		totalPacketsCount = new MCHash();
		packetStats = 0;
	}
}
