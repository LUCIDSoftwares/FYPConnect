package application.services;

import java.sql.ResultSet;

import application.datamodel.User;

// Implements the Factory pattern in the context of user
public abstract class UserFactory {

	public abstract User createUser(ResultSet result);
	public abstract User createUser(String userType);
}

