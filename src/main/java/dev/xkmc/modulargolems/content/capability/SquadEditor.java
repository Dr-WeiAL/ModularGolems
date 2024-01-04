package dev.xkmc.modulargolems.content.capability;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record SquadEditor(GolemConfigEditor editor) {

	@Nullable
	public UUID getCaptainId() {
		return editor().entry().squadConfig.getCaptainId();
	}

	public double getRadius() {
		return editor().entry().squadConfig.getRadius();
	}

	public void setCaptainId(@Nullable UUID captainId) {
		editor().entry().squadConfig.setCaptainId(captainId);
		editor.sync();
	}

	public void setRadius(double Radius) {
		editor().entry().squadConfig.setRadius(Radius);
		editor.sync();
	}
}
