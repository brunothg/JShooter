package com.github.brunothg.jshooter.gui.home;

import javax.annotation.PostConstruct;
import javax.swing.JMenuBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.brunothg.jshooter.utils.I18N;

@Component
@Scope("prototype")
public class GameMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;

	@Autowired
	private I18N language;

	@PostConstruct
	public void build() {

	}
}
