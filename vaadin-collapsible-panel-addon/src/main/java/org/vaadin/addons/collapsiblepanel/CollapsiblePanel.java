package org.vaadin.addons.collapsiblepanel;

import com.vaadin.event.SerializableEventListener;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Panel;
import com.vaadin.util.ReflectTools;
import org.vaadin.addons.collapsiblepanel.client.CollapsiblePanelServerRpc;
import org.vaadin.addons.collapsiblepanel.client.CollapsiblePanelState;

import java.lang.reflect.Method;

/**
 * The CollapsiblePanel extends the usual {@link Panel} by the ability to
 * collapse and so make its content disappear.
 * <p>
 * <p>
 * Additionally everything is observable with listeners.
 *
 * @author cthiel
 */
@SuppressWarnings("serial")
public class CollapsiblePanel extends AbstractExtension {

	@FunctionalInterface
	public static interface ExpandListener extends SerializableEventListener {
		public static final Method PANEL_EXPAND_METHOD = ReflectTools.findMethod(ExpandListener.class, "expand", ExpandEvent.class);

		public void expand(ExpandEvent event);

	}

	@FunctionalInterface
	public static interface CollapseListener extends SerializableEventListener {
		public static final Method PANEL_COLLAPSE_METHOD = ReflectTools.findMethod(CollapseListener.class, "collapse", CollapseEvent.class);

		public void collapse(CollapseEvent event);
	}

	// does nothing but being an event
	public static class ExpandEvent extends Event {
		public ExpandEvent(Component component) {
			super(component);
		}
	}

	// does nothing but being an event
	public static class CollapseEvent extends Event {
		public CollapseEvent(Component component) {
			super(component);
		}
	}

	private final Panel targetPanel;

	CollapsiblePanelServerRpc rpc = new CollapsiblePanelServerRpc() {
		@Override
		public void onExpand() {
			((CollapsiblePanelState) getState(true)).collapsed = false;
			fireExpandEvent();
		}

		@Override
		public void onCollapse() {
			((CollapsiblePanelState) getState(true)).collapsed = true;
			fireCollapseEvent();
		}
	};

	public CollapsiblePanel(final Panel panel) {
		this.targetPanel = panel;

		extend(panel);
		registerRpc(this.rpc);
	}

	public Panel getTargetPanel() {
		return this.targetPanel;
	}

	protected CollapsiblePanelState getState() {
		return (CollapsiblePanelState) super.getState(true);
	}

	private void fireExpandEvent() {
		fireEvent(new ExpandEvent(this.targetPanel));
	}

	private void fireCollapseEvent() {
		fireEvent(new CollapseEvent(this.targetPanel));
	}

	public void addExpandListener(ExpandListener listener) {
		addListener(ExpandEvent.class, listener, ExpandListener.PANEL_EXPAND_METHOD);
	}

	public void addCollapseListener(CollapseListener listener) {
		addListener(CollapseEvent.class, listener, CollapseListener.PANEL_COLLAPSE_METHOD);
	}

	public void setCollapsed(boolean collapsed) {
		getState().collapsed = collapsed;
	}

	public boolean getCollapsed() {
		return getState().collapsed;
	}
}
