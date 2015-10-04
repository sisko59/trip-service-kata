package org.craftedsw.tripservicekata.trip;

import static org.hamcrest.core.Is.is;

import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TripServiceTest {

	private static final User GUEST = null;
	private static final User UNUSED_USER = null;
	private static final User USER_LOGGED = new User();
	private static final User ANOTHER_USER = new User();
	private static final Trip TO_BARCELONA = new Trip();
	private User loggedInUser;
	private TripService tripService;

	@Before
	public void initialise() {
		tripService = new TestableTipService();
	}

	@Test(expected = UserNotLoggedInException.class)
	public void should_throw_exception_when_user_is_not_logged() {
		loggedInUser = GUEST;

		tripService.getTripsByUser(UNUSED_USER);
	}

	@Test
	public void should_no_trip_when_user_is_not_a_friend() {
		loggedInUser = USER_LOGGED;

		User friend = new User();
		friend.addFriend(ANOTHER_USER);
		friend.addTrip(TO_BARCELONA);

		List<Trip> friendTrips = tripService.getTripsByUser(friend);

		Assert.assertThat(friendTrips.size(), is(0));
	}

	private class TestableTipService extends TripService {

		@Override
		protected User getUserLogged() {
			return loggedInUser;
		}

	}
}
