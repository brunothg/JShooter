package com.github.brunothg.jshooter.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.game.engine.image.InternalImage;
import com.github.brunothg.jshooter.utils.I18N;

@Component
@Scope("prototype")
public class GameDisplay extends JFrame {
	private static final long serialVersionUID = 1L;

	private I18N language;

	@Autowired
	public GameDisplay(I18N language) {
		this.language = language;

		build();
	}

	private void build() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle(language.get("app-name"));
		setIconImage(InternalImage.load("icon.png"));
	}

}
