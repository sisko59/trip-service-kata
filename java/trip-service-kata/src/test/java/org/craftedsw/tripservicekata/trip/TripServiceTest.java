package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Test;

public class TripServiceTest {

	private static final User GUEST = null;
	private static final User UNUSED_USER = null;
	private User loggedInUser;

	@Test(expected = UserNotLoggedInException.class)
	public void should_throw_exception_when_user_is_not_logged() {
		TripService tripService = new TestableTipService();

		loggedInUser = GUEST;

		tripService.getTripsByUser(UNUSED_USER);
	}

	private class TestableTipService extends TripService {

		@Override
		protected User getUserLogged() {
			return loggedInUser;
		}

	}
}
