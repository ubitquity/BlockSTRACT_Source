import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { menus, Menus } from '../../routes';
import { Logger } from '@wuja/logger';
import { environment } from '../../../environments/environment.prod';
import { LoginUtils } from '../../_services/loginutils/loginutils.service';


@Component({
	selector: 'app-drop-menu',
	templateUrl: './drop-menu.component.html',
	styleUrls: ['./drop-menu.component.scss']
})
export class DropdownMenuComponent  {
	menus: Menus[] = menus;
	activeMenu: Menus;
	user;

	routerLink = '';
	lastPath;

	ariaExpanded = false;
	public localName = environment.localName;

	constructor(private router: Router, private loginutils: LoginUtils) {
		this.user = this.loginutils.user;
	}

	isCurrent(menu) {
		menu.active = true;
	}

	toggleMenu(menu: Menus) {
		if (menu.visible) {
			menu.visible = false;
			this.ariaExpanded = false;
		} else {
			menu.visible = true;
			this.ariaExpanded = true;
			this.getRoute(menu);
		}
	}

	hideMenu(menu: Menus) {
		if (!menu) {
			menus.forEach(item => {
				item.visible = false;
			});
			return;
		}

		menu.visible = false;
		this.ariaExpanded = false;
	}

	getRoute (menu: Menus) {
		this.routerLink = this.routerLink + '/' + menu.path;
	}
}
