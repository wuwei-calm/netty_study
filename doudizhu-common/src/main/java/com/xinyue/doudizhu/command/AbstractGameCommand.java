package com.xinyue.doudizhu.command;
public abstract class AbstractGameCommand implements IGameCommand {
	private int commandId;
	private int errorCode;
	
	

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public int getCommandId() {
		return commandId;
	}

	@Override
	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

}
