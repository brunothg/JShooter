package com.github.brunothg.jshooter.gui.settings;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.config.ApplicationInfo;
import com.github.brunothg.jshooter.utils.I18N;

import net.miginfocom.swing.MigLayout;

@Component
@Scope("prototype")
public class VersionSettings extends JPanel implements SettingsPanel {
	private static final long serialVersionUID = 1L;

	@Autowired
	private I18N language;

	@Autowired
	private ApplicationInfo applicationInfo;

	private JTextField tfBuildName;
	private JTextField tfVersion;
	private JTextField tfBuildTimestamp;

	@PostConstruct
	public void build() {
		removeAll();
		setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

		JLabel lblBuildname = new JLabel(language.get("build-name"));
		add(lblBuildname, "cell 0 0");

		tfBuildName = new JTextField();
		tfBuildName.setEditable(false);
		add(tfBuildName, "cell 1 1,growx");
		tfBuildName.setColumns(10);

		JLabel lblVersion = new JLabel(language.get("build-version"));
		add(lblVersion, "cell 0 2");

		tfVersion = new JTextField();
		tfVersion.setEditable(false);
		add(tfVersion, "cell 1 3,growx");
		tfVersion.setColumns(10);

		JLabel lblBuildtimestamp = new JLabel(language.get("build-timestamp"));
		add(lblBuildtimestamp, "cell 0 4");

		tfBuildTimestamp = new JTextField();
		tfBuildTimestamp.setEditable(false);
		add(tfBuildTimestamp, "cell 1 5,growx");
		tfBuildTimestamp.setColumns(10);

		updateApplicationInfo();
	}

	protected void updateApplicationInfo() {
		tfBuildName.setText(applicationInfo.getName());
		tfVersion.setText(applicationInfo.getVersion());
		tfBuildTimestamp.setText(DateFormat.getDateTimeInstance().format(applicationInfo.getBuildTimestampDate()));
	}

	@Override
	public void applySettings() {
		// Nothing to edit - only view version information
	}

	@Override
	public String getTitle() {
		return language.get("application-info-title");
	}

	@Override
	public JPanel getSettingsPanel() {
		return VersionSettings.this;
	}

	public void showSettingsDialog(java.awt.Component parent) {
		Window windowForComponent = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;

		final JDialog dialog = new JDialog(windowForComponent);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		VersionSettings settingsPanel = VersionSettings.this;
		dialog.getContentPane().add(settingsPanel, BorderLayout.CENTER);
		dialog.setTitle(getTitle());

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		dialog.getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		JButton btnOk = new JButton(language.get("dialog-ok"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySettings();
				dialog.dispose();
			}
		});
		pnlButtons.add(btnOk);

		JButton btnCancel = new JButton(language.get("dialog-cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		pnlButtons.add(btnCancel);

		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}
