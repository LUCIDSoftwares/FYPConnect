package application.persistance;

public class ConcretePersistanceFactory extends PersistanceFactory {

	// written to implement the singleton pattern
	private static ConcretePersistanceFactory instance;
	
	private ConcretePersistanceFactory() {
		// written to implement the singleton pattern
	}
	
	public static synchronized ConcretePersistanceFactory getInstance() {
		if(instance == null)
			instance = new ConcretePersistanceFactory();
		return instance;
	}
	
	@Override
	public PersistanceHandler createPersistanceHandler(String persistanceType) {
		PersistanceHandler prsHandler = null;
		if(persistanceType.equalsIgnoreCase("DBHandler")) {
			prsHandler = new DBHandler();
		}
		
		return prsHandler;
	}

}
