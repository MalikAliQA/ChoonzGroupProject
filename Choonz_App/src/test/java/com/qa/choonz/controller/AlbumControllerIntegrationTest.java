package com.qa.choonz.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.choonz.config.SingleTenantTest;
import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.rest.assembler.AlbumModelAssembler;
import com.qa.choonz.rest.model.AlbumModel;

@SingleTenantTest
@AutoConfigureMockMvc
public class AlbumControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper jsonifier;

	@Autowired
	private AlbumModelAssembler assembler;

	private Track track = new Track(1L, "name", null, null, null, 360, "lyrics");
	private List<Album> albums = Collections.emptyList();
	private List<Track> tracks = List.of(track);
	private Artist artist = new Artist(1L, "name", albums);

	private final String URI = "/api/albums";

	private final Album TEST_ALBUM_1 = new Album(1L, "We Shall All Be Healed", "some url", tracks, artist);

	@Test
	@WithMockUser(value = "user", password = "password")
	void createTest() throws Exception {
		AlbumModel testModel = this.assembler.toModel(TEST_ALBUM_1);
		String testModelJson = this.jsonifier.writeValueAsString(testModel);
		System.out.println(testModelJson);
		RequestBuilder request = post(URI).contentType(MediaType.APPLICATION_JSON).content(testModelJson);

		this.mvc.perform(request).andExpect(status().isCreated());
	}

	@Test
	void readAllTest() throws Exception {
		RequestBuilder request = get(URI + "/").contentType(MediaType.APPLICATION_JSON);
		this.mvc.perform(request).andExpect(status().isOk());
	}

	@Test
	void readOneTest() throws Exception {
		RequestBuilder request = get(URI + "/1").contentType(MediaType.APPLICATION_JSON);
		this.mvc.perform(request).andExpect(status().isOk()).andReturn();
	}

	@Test
	void updateTest() throws Exception {
		String toUpdate = "{\"name\":\"Hello\",\"cover\":\"www.world.com\"}";
		RequestBuilder request = put(URI + "/1").contentType(MediaType.APPLICATION_JSON).content(toUpdate);
		this.mvc.perform(request).andExpect(status().isAccepted());
	}

	@Test
	void deleteTest() throws Exception {
		RequestBuilder request = delete(URI + "/1");
		ResultMatcher checkStatus = status().isNoContent();

		this.mvc.perform(request).andExpect(checkStatus);
	}
}
