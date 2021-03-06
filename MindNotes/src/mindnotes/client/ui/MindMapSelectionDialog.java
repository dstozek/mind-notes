package mindnotes.client.ui;

import java.util.List;

import mindnotes.client.presentation.MindMapSelectionView;
import mindnotes.shared.model.MindMapInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class MindMapSelectionDialog implements MindMapSelectionView {

	private static final int DECK_WAIT = 0;
	private static final int DECK_EMPTY = 1;
	private static final int DECK_NA = 2;
	private static final int DECK_TABLE = 3;

	private PositionCallback _positionCallback;
	private PopupPanel _panel;

	private Resources _resources = GWT.create(Resources.class);

	private class ListSelectionHandler implements ClickHandler {

		private MindMapInfo _document;
		private boolean _local;

		public ListSelectionHandler(MindMapInfo mmi, boolean local) {
			_document = mmi;
			_local = local;
		}

		@Override
		public void onClick(ClickEvent event) {
			if (_listener != null)
				_listener.mindMapChosen(_document, _local);
			_panel.hide();
		}

	}

	public class ListRemoveHandler implements ClickHandler {

		private MindMapInfo _document;
		private boolean _local;

		public ListRemoveHandler(MindMapInfo mmi, boolean local) {
			_document = mmi;
			_local = local;
		}

		@Override
		public void onClick(ClickEvent event) {
			if (_listener != null)
				_listener.mindMapRemove(_document, _local);
			_panel.hide();
		}
	}

	interface Resources extends ClientBundle {
		ImageResource removeIcon();
	}

	interface Styles extends CssResource {
		String removeImg();

		String mindmapListLink();
	}

	private static MindMapSelectionDialogUiBinder uiBinder = GWT
			.create(MindMapSelectionDialogUiBinder.class);

	interface MindMapSelectionDialogUiBinder extends
			UiBinder<PopupPanel, MindMapSelectionDialog> {
	}

	public MindMapSelectionDialog() {
		_panel = uiBinder.createAndBindUi(this);

		_panel.setAutoHideEnabled(true);
	}

	@UiField
	protected FlexTable availableDocumentsTable;
	@UiField
	protected FlexTable availableLocalDocumentsTable;

	@UiField
	protected DeckPanel cloudMapsDeck;

	@UiField
	protected DeckPanel localMapsDeck;

	@UiField
	protected Styles style;

	private Listener _listener;

	@Override
	public void setListener(Listener listener) {
		_listener = listener;
	}

	@Override
	public void setMindMaps(List<MindMapInfo> mindmaps) {

		fillTable(availableDocumentsTable, mindmaps, false);

	}

	/**
	 * @param mindmaps
	 */
	private void fillTable(FlexTable table, List<MindMapInfo> mindmaps,
			boolean local) {

		table.clear();

		if (mindmaps == null) {
			(local ? localMapsDeck : cloudMapsDeck).showWidget(DECK_NA);
			return;
		}
		if (mindmaps.isEmpty()) {
			(local ? localMapsDeck : cloudMapsDeck).showWidget(DECK_EMPTY);
			return;
		}
		(local ? localMapsDeck : cloudMapsDeck).showWidget(DECK_TABLE);

		// populate the table with values for mindmaps
		int i = 0;
		for (MindMapInfo doc : mindmaps) {
			Anchor link = new Anchor(doc.getTitle());
			link.setStyleName(style.mindmapListLink());
			link.addClickHandler(new ListSelectionHandler(doc, local));
			table.setWidget(i, 0, link);

			Image removeLink = new Image(_resources.removeIcon());
			removeLink.addClickHandler(new ListRemoveHandler(doc, local));
			removeLink.getElement().addClassName(style.removeImg());
			table.setWidget(i, 1, removeLink);
			i++;
		}

	}

	@Override
	public void setLocalMindMaps(List<MindMapInfo> mindmaps) {
		fillTable(availableLocalDocumentsTable, mindmaps, true);
	}

	@Override
	public void askForCloudDocumentSelection() {
		cloudMapsDeck.showWidget(DECK_WAIT);
		localMapsDeck.showWidget(DECK_WAIT);

		if (_positionCallback == null) {
			_positionCallback = new PositionCallback() {

				@Override
				public void setPosition(int offsetWidth, int offsetHeight) {
					_panel.setPopupPosition(
							(Window.getClientWidth() - offsetWidth) / 2,
							(Window.getClientHeight() - offsetHeight) / 2);

				}
			};
		}
		// center the dialog window
		_panel.setPopupPositionAndShow(_positionCallback);

	}

	public void setPositionCallback(PositionCallback positionCallback) {
		_positionCallback = positionCallback;
	}

	public void setPopupPosition(int left, int top) {
		_panel.setPopupPosition(left, top);
	}

}
