package com.qa.choonz.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.choonz.config.SingleTenantTest;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.rest.assembler.TrackModelAssembler;
import com.qa.choonz.rest.controller.TrackController;
import com.qa.choonz.rest.model.TrackModel;
import com.qa.choonz.service.TrackService;

@SingleTenantTest
public class TrackControllerUnitTest {

	@Autowired
	private TrackController controller;

	@MockBean
	private TrackService service;

	@Autowired
	private TrackModelAssembler assembler;

	private final Track TEST_TRACK_1 = new Track(1L, "Cotton", null, null, null, 360, "This song is for the rats...");
	private final Track TEST_TRACK_2 = new Track(2L, "Oceanographers Choice", null, null, null, 400, "WELL...");
	private final Track TEST_TRACK_3 = new Track(3L, "Younger", null, null, null, 500, "Crank that siren high...");
	private final Track TEST_TRACK_4 = new Track(4L, "Broom People", null, null, null, 360,
			"36 Hudson in the garage...");
	private final Track TEST_TRACK_5 = new Track(5L, "This Is My House, This Is My Home", null, null, null, 545,
			"Woke when it was dark...");

	// list
	private final List<Track> LISTOFTRACK = List.of(TEST_TRACK_1, TEST_TRACK_2, TEST_TRACK_3, TEST_TRACK_4,
			TEST_TRACK_5);

	@Test
	void createTest() throws Exception {
		when(this.service.create(TEST_TRACK_1)).thenReturn(this.assembler.toModel(TEST_TRACK_1));
		assertThat(new ResponseEntity<TrackModel>(this.assembler.toModel(TEST_TRACK_1), HttpStatus.CREATED))
				.isEqualTo(this.controller.create(TEST_TRACK_1));
		verify(this.service, atLeastOnce()).create(TEST_TRACK_1);
	}

	@Test
	void readOneTest() throws Exception {
		when(this.service.findById(TEST_TRACK_1.getId())).thenReturn(this.assembler.toModel(TEST_TRACK_1));
		assertThat(new ResponseEntity<TrackModel>(this.assembler.toModel(TEST_TRACK_1), HttpStatus.OK))
				.isEqualTo(this.controller.findById(TEST_TRACK_1.getId()));
		verify(this.service, atLeastOnce()).findById(TEST_TRACK_1.getId());
	}

	@Test
	void readAllTest() throws Exception {
		when(this.service.findAll()).thenReturn(this.assembler.toCollectionModel(LISTOFTRACK));
		assertThat(this.controller.findAll())
				.isEqualTo(new ResponseEntity<>(this.assembler.toCollectionModel(LISTOFTRACK), HttpStatus.OK));
		verify(this.service, atLeastOnce()).findAll();
	}

	@Test
	void updateTest() throws Exception {
		when(this.service.update(TEST_TRACK_1, TEST_TRACK_1.getId())).thenReturn(this.assembler.toModel(TEST_TRACK_1));
		assertThat(new ResponseEntity<TrackModel>(this.assembler.toModel(TEST_TRACK_1), HttpStatus.ACCEPTED))
				.isEqualTo(this.controller.update(TEST_TRACK_1, (TEST_TRACK_1.getId())));
		verify(this.service, atLeastOnce()).update((TEST_TRACK_1), TEST_TRACK_1.getId());
	}

	@Test
	void deleteTest() throws Exception {
		when(this.service.delete(TEST_TRACK_1.getId())).thenReturn(false);
		assertThat(this.controller.delete(TEST_TRACK_1.getId()))
				.isEqualTo(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
		verify(this.service, atLeastOnce()).delete(TEST_TRACK_1.getId());
	}

	@Test
	void deleteTestTwo() throws Exception {
		when(this.service.delete(TEST_TRACK_1.getId())).thenReturn(true);
		assertThat(this.controller.delete(TEST_TRACK_1.getId())).isEqualTo(new ResponseEntity<>(HttpStatus.NO_CONTENT));
		verify(this.service, atLeastOnce()).delete(TEST_TRACK_1.getId());
	}

}
