package org.craftedsw.tripservicekata.trip;

//@formatter:off
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
	private static final Trip TO_LONDON = new Trip();
	private User loggedInUser;
	private TripService tripService;

	@Before
	public void initialise() {
		tripService = new TestableTipService();
		loggedInUser = USER_LOGGED;
	}

	@Test(expected = UserNotLoggedInException.class)
	public void should_throw_exception_when_user_is_not_logged() {
		loggedInUser = GUEST;

		tripService.getTripsByUser(UNUSED_USER);
	}

	@Test
	public void should_no_trip_when_user_is_not_a_friend() {
		User friend = UserBuilder.aUser()
				.friendsWith(ANOTHER_USER)
				.withTrips(TO_BARCELONA)
				.build();

		List<Trip> friendTrips = tripService.getTripsByUser(friend);

		Assert.assertThat(friendTrips.size(), is(0));
	}

	@Test
	public void should_friend_trips_when_user_is_a_friend() throws Exception {
		User friend = UserBuilder.aUser()
				.friendsWith(USER_LOGGED, ANOTHER_USER)
				.withTrips(TO_BARCELONA, TO_LONDON)
				.build();

		List<Trip> friendTrips = tripService.getTripsByUser(friend);

		Assert.assertThat(friendTrips.size(), is(2));
	}

	public static class UserBuilder {

		private Trip[] trips = new Trip[] {};
		private User[] friends = new User[] {};

		public static UserBuilder aUser() {
			return new UserBuilder();
		}

		public UserBuilder withTrips(Trip... trips) {
			this.trips = trips;
			return this;
		}

		public UserBuilder friendsWith(User... friends) {
			this.friends = friends;
			return this;
		}

		public User build() {
			User user = new User();
			addFriends(user);
			addTripsTo(user);
			return user;
		}

		private void addTripsTo(User user) {
			for (Trip trip : trips) {
				user.addTrip(trip);
			}
		}

		private void addFriends(User user) {
			for (User friend : friends) {
				user.addFriend(friend);
			}
		}
	}

	private class TestableTipService extends TripService {

		@Override
		protected User getUserLogged() {
			return loggedInUser;
		}

		@Override
		protected List<Trip> tripsBy(User user) {
			return user.trips();
		}

	}
}
