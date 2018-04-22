package de.butzlabben.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;

/**
 * @author Butzlabben
 * @since 26.02.2018
 */
public class GameProfileBuilder {

	private static Gson gson = new GsonBuilder().disableHtmlEscaping()
			.registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
			.registerTypeAdapter(GameProfile.class, new GameProfileSerializer())
			.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
	private static HashMap<UUID, CachedProfile> cache = new HashMap<>();
	private static long cacheTime = -1L;

	public static GameProfile fetch(UUID uuid) throws IOException {
		return fetch(uuid, false);
	}

	public static GameProfile fetch(UUID uuid, boolean forceNew) throws IOException {
		if ((!forceNew) && (cache.containsKey(uuid)) && (((CachedProfile) cache.get(uuid)).isValid())) {
			return ((CachedProfile) cache.get(uuid)).profile;
		}
		HttpURLConnection connection = (HttpURLConnection) new URL(
				String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false",
						new Object[] { UUIDTypeAdapter.fromUUID(uuid) })).openConnection();
		connection.setReadTimeout(5000);
		if (connection.getResponseCode() == 200) {
			String json = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

			GameProfile result = (GameProfile) gson.fromJson(json, GameProfile.class);
			cache.put(uuid, new CachedProfile(result));
			return result;
		}
		if ((!forceNew) && (cache.containsKey(uuid))) {
			return ((CachedProfile) cache.get(uuid)).profile;
		}
		JsonObject error = (JsonObject) new JsonParser()
				.parse(new BufferedReader(new InputStreamReader(connection.getErrorStream())).readLine());
		throw new IOException(error.get("error").getAsString() + ": " + error.get("errorMessage").getAsString());
	}

	public static GameProfile getProfile(UUID uuid, String name, String skin) {
		return getProfile(uuid, name, skin, null);
	}

	public static GameProfile getProfile(UUID uuid, String name, String skinUrl, String capeUrl) {
		GameProfile profile = new GameProfile(uuid, name);
		boolean cape = (capeUrl != null) && (!capeUrl.isEmpty());

		List<Object> args = new ArrayList<>();
		args.add(Long.valueOf(System.currentTimeMillis()));
		args.add(UUIDTypeAdapter.fromUUID(uuid));
		args.add(name);
		args.add(skinUrl);
		if (cape) {
			args.add(capeUrl);
		}
		profile.getProperties().put("textures",
				new Property("textures",
						Base64Coder.encodeString(String.format(
								cape ? "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}"
										: "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}",
								args.toArray(new Object[args.size()])))));
		return profile;
	}

	public static void setCacheTime(long time) {
		cacheTime = time;
	}

	private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

		public GameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject object = (JsonObject) json;
			UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
			String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
			GameProfile profile = new GameProfile(id, name);
			if (object.has("properties")) {
				for (Map.Entry<String, Property> prop : ((PropertyMap) context.deserialize(object.get("properties"),
						PropertyMap.class)).entries()) {
					profile.getProperties().put((String) prop.getKey(), (Property) prop.getValue());
				}
			}
			return profile;
		}

		public JsonElement serialize(GameProfile profile, Type type, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			if (profile.getId() != null) {
				result.add("id", context.serialize(profile.getId()));
			}
			if (profile.getName() != null) {
				result.addProperty("name", profile.getName());
			}
			if (!profile.getProperties().isEmpty()) {
				result.add("properties", context.serialize(profile.getProperties()));
			}
			return result;
		}
	}

	private static class CachedProfile {
		private GameProfile profile;

		public CachedProfile(GameProfile profile) {
			this.profile = profile;
		}

		public boolean isValid() {
			return GameProfileBuilder.cacheTime < 0L;
		}
	}
}
