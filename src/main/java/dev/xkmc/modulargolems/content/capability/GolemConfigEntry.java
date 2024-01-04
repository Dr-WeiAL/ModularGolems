package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SerialClass
public class GolemConfigEntry {

	public static GolemConfigEntry getDefault(UUID id, int color, Component name) {
		return new GolemConfigEntry(name).init(id, color);
	}

	public final SyncContainer sync = new SyncContainer();

	private UUID id;
	private int color;
	private Component nameComp;

	@SerialClass.SerialField
	protected String name;
	@SerialClass.SerialField
	public int defaultMode;
	@SerialClass.SerialField
	public boolean summonToPosition;
	@SerialClass.SerialField
	public boolean locked;

	@SerialClass.SerialField
	public PickupFilterConfig pickupFilter = new PickupFilterConfig();
	@SerialClass.SerialField
	public TargetFilterConfig targetFilter = new TargetFilterConfig();
	@SerialClass.SerialField
	public SquadConfig squadConfig = new SquadConfig();

	@Deprecated
	public GolemConfigEntry() {

	}

	private GolemConfigEntry(Component comp) {
		nameComp = comp;
		name = Component.Serializer.toJson(comp);
		targetFilter.initDefault();
	}

	public Component getDisplayName() {
		if (nameComp == null) {
			nameComp = Component.Serializer.fromJson(name);
		}
		if (nameComp == null) {
			nameComp = Component.literal("Unnamed");
		}
		return nameComp;
	}

	public GolemConfigEntry init(UUID id, int color) {
		this.id = id;
		this.color = color;
		return this;
	}

	public void heartBeat(ServerLevel level, ServerPlayer player) {
		if (sync.heartBeat(level, player.getUUID())) {
			ModularGolems.HANDLER.toClientPlayer(new ConfigSyncToClient(this), player);
		}
	}

	public UUID getID() {
		return id;
	}

	public int getColor() {
		return color;
	}

	public void clientTick(Level level, boolean updated) {
		sync.clientTick(this, level, updated);
	}

	public void sync(Level level) {
		if (level instanceof ServerLevel sl) {
			sync.sendToAllTracking(sl, new ConfigSyncToClient(this));
		} else {
			ModularGolems.HANDLER.toServer(new ConfigUpdateToServer(level, this));
		}
	}

	public GolemConfigEntry copyFrom(@Nullable GolemConfigEntry entry) {
		if (entry != null)
			sync.clientReplace(entry.sync);
		return this;
	}

	public void setName(Component hoverName, ServerLevel level) {
		nameComp = hoverName;
		name = Component.Serializer.toJson(hoverName);
		sync(level);
	}

}
