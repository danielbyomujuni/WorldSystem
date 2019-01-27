package de.butzlabben.world.config;

public enum WorldPerm {
	
	MEMBER("ws.member"),
	GAMEMODE("ws.gamemode"), BUILD("ws.build"), TELEPORT("ws.teleport"),
	EDITMEMBERS("ws.edit"), ADMINISTRATEMEMBERS, ADMINISTRATEWORLD,   WORLDEDIT("ws.worldedit");
	
	private final String opPerm;
	
	WorldPerm() {
		this("ws.*");
	}
	
	WorldPerm(String opPerm) {
		this.opPerm = opPerm;
	}
	
	public String getOpPerm() {
		return opPerm;
	}

}
