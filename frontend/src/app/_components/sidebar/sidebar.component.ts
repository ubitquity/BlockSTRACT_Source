import { Component, OnInit } from '@angular/core';
import { menus, Menus } from '../../routes';
import { Logger } from '@wuja/logger';

@Component({
	selector: 'app-sidebar',
	templateUrl: './sidebar.component.html',
	styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
	menus: Menus[] = menus;
	activeMenu: Menus;

	constructor(private log: Logger) {}

	ngOnInit() {
		this.log.init('SidebarComponent', this);
	}

	isCurrent(menu) {
		this.log.log('menu', menu);
		menu.active = true;
	}

	toggleVisibility(currentMenu: Menus) {
		if (!currentMenu.children) return;
		currentMenu.visible = !currentMenu.visible;
	}
}
