package application.persistance;

public abstract class PersistanceFactory {

	public abstract PersistanceHandler createPersistanceHandler(String persistanceType);
	
}
