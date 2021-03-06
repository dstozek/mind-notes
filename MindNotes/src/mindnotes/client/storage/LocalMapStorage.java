package mindnotes.client.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mindnotes.shared.model.MindMap;
import mindnotes.shared.model.MindMapInfo;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Store mind maps using HTML5 LocalStorage mechanism.
 * 
 * @author dominik
 * 
 */
public class LocalMapStorage implements Storage {

	private static final String MINDMAP_KEY_PREFIX = "mm_";
	private static final String MINDMAP_KEY_CONTENT_SUFFIX = "_content";
	private static final String MINDMAP_KEY_TITLE_SUFFIX = "_title";
	protected static final String SEQUENCE_KEY = "seq_key";

	private com.google.code.gwt.storage.client.Storage _storage;

	@Override
	public void getStoredMaps(final AsyncCallback<List<MindMapInfo>> callback) {

		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {
				try {
					if (_storage == null) {
						_storage = com.google.code.gwt.storage.client.Storage
								.getLocalStorage();
					}

					Map<String, MindMapInfo> map = new HashMap<String, MindMapInfo>();

					// iterate over all existing keys; look for title keys;
					// fill out a list of available keys.
					for (int i = 0; i < _storage.getLength(); i++) {
						String key = _storage.key(i);
						if (!key.startsWith(MINDMAP_KEY_PREFIX))
							continue;
						if (key.endsWith(MINDMAP_KEY_TITLE_SUFFIX)) {
							String realkey = key.substring(
									MINDMAP_KEY_PREFIX.length(), key.length()
											- MINDMAP_KEY_TITLE_SUFFIX.length());

							map.put(realkey,
									new MindMapInfo(realkey, _storage
											.getItem(key)));
						}

					}

					callback.onSuccess(new ArrayList<MindMapInfo>(map.values()));
				} catch (Throwable t) {
					callback.onFailure(t);
				}
			}

		});
	}

	@Override
	public void loadMindMap(final MindMapInfo map,
			final AsyncCallback<MindMap> callback) {
		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {
				try {
					String realKey = MINDMAP_KEY_PREFIX + map.getKey()
							+ MINDMAP_KEY_CONTENT_SUFFIX;
					String json = _storage.getItem(realKey);
					JSONMindMapBuilder jmmb = new JSONMindMapBuilder(JSONParser
							.parse(json).isObject());
					MindMap mm = new MindMap();
					jmmb.copyTo(mm);
					callback.onSuccess(mm);
				} catch (Throwable t) {
					callback.onFailure(t);
				}

			}
		});
	}

	@Override
	public void saveMindMap(String key, final MindMap map,
			final AsyncCallback<MindMapInfo> callback) {
		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {
				try {
					if (_storage == null) {
						_storage = com.google.code.gwt.storage.client.Storage
								.getLocalStorage();
					}
					int k = 0;

					try {
						k = Integer.parseInt(_storage.getItem(SEQUENCE_KEY));
					} catch (NumberFormatException nfe) {
					} catch (NullPointerException npe) {
					}
					k++;

					JSONMindMapBuilder jmmb = new JSONMindMapBuilder();
					map.copyTo(jmmb);

					_storage.setItem(SEQUENCE_KEY, Integer.toString(k));
					_storage.setItem(MINDMAP_KEY_PREFIX + k
							+ MINDMAP_KEY_TITLE_SUFFIX,
							map.getTitle() == null ? "" : map.getTitle());
					_storage.setItem(MINDMAP_KEY_PREFIX + k
							+ MINDMAP_KEY_CONTENT_SUFFIX, jmmb.getJSON());

					callback.onSuccess(new MindMapInfo("" + k, map.getTitle()));
				} catch (Throwable t) {
					callback.onFailure(t);
				}
			}
		});
	}

	@Override
	public void remove(MindMapInfo map, AsyncCallback<Void> asyncCallback) {
		_storage.removeItem(MINDMAP_KEY_PREFIX + map.getKey()
				+ MINDMAP_KEY_CONTENT_SUFFIX);
		_storage.removeItem(MINDMAP_KEY_PREFIX + map.getKey()
				+ MINDMAP_KEY_TITLE_SUFFIX);

	}
}
