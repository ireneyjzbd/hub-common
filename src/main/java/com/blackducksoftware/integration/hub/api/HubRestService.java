package com.blackducksoftware.integration.hub.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Method;

import com.blackducksoftware.integration.hub.api.item.HubItem;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HubRestService<T extends HubItem> {
	private final RestConnection restConnection;
	private final Gson gson;
	private final JsonParser jsonParser;
	private final Type itemType;
	private final Type itemListType;

	public HubRestService(final RestConnection restConnection, final Gson gson, final JsonParser jsonParser,
			final Type itemType, final Type itemListType) {
		this.restConnection = restConnection;
		this.gson = gson;
		this.jsonParser = jsonParser;
		this.itemType = itemType;
		this.itemListType = itemListType;
	}

	public List<T> getAll(final JsonObject jsonObject, final HubRequest hubRequest)
			throws BDRestException, IOException, URISyntaxException {
		final List<T> allItems = new ArrayList<>();
		final int totalCount = getTotalCount(jsonObject);
		List<T> items = getItems(jsonObject);
		allItems.addAll(items);

		while (allItems.size() < totalCount) {
			final int currentOffset = hubRequest.getOffset();
			final int increasedOffset = currentOffset + items.size();

			hubRequest.setOffset(increasedOffset);
			final JsonObject nextResponse = hubRequest.executeForResponseJson();
			items = getItems(nextResponse);
			allItems.addAll(items);
		}

		return allItems;
	}

	public List<T> getItems(final JsonObject jsonObject) {
		final List<T> items = gson.fromJson(jsonObject.get("items"), itemListType);
		return items;
	}

	private int getTotalCount(final JsonObject jsonObject) {
		final int totalCount = jsonObject.get("totalCount").getAsInt();
		return totalCount;
	}

	public List<T> getItems(final String url) throws IOException, URISyntaxException, BDRestException {
		final HubRequest itemRequest = new HubRequest(getRestConnection(), getJsonParser());
		itemRequest.setMethod(Method.GET);
		itemRequest.setUrl(url);

		final String response = itemRequest.executeForResponseString();
		return getGson().fromJson(response, itemListType);
	}

	public T getItem(final String url) throws IOException, BDRestException, URISyntaxException {
		final HubRequest itemRequest = new HubRequest(getRestConnection(), getJsonParser());
		itemRequest.setMethod(Method.GET);
		itemRequest.setUrl(url);

		final String response = itemRequest.executeForResponseString();
		return getGson().fromJson(response, itemType);
	}

	public T getItem(final List<String> urlSegments) throws IOException, BDRestException, URISyntaxException {
		final HubRequest itemRequest = new HubRequest(getRestConnection(), getJsonParser());
		itemRequest.setMethod(Method.GET);
		itemRequest.addUrlSegments(urlSegments);

		final String response = itemRequest.executeForResponseString();
		return getGson().fromJson(response, itemType);
	}

	public RestConnection getRestConnection() {
		return restConnection;
	}

	public Gson getGson() {
		return gson;
	}

	public JsonParser getJsonParser() {
		return jsonParser;
	}

}
