package core;

import core.entities.BuyableEntity;

public class BuyAndSpawnEntityCommand extends SpawnEntityCommand {
	
	public BuyAndSpawnEntityCommand(BuyableEntity entity) {
		super(entity);
	}
	
	@Override
	public void execute(World world) {
		BuyableEntity entity = (BuyableEntity) entityToSpawn;
		int cost = entity.getBaseCost();
		Team team = entity.getTeam();
		
		PlayerInfo playerInfo = world.getPlayerInfo(team);
		double playerMoney = playerInfo.getMoney();
		if(playerMoney >= cost) {
			playerInfo.removeMoney(cost);
		}
		super.execute(world);
	}
	
}
