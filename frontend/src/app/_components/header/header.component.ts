import { Component, HostBinding, Input } from '@angular/core';

/**
 * Breadcrumb jest wykorzystywany do budowania breadcrumbów w HeaderComponent.
 */
export interface Breadcrumb {
	/** nazwa wyświetlana */
	name: string;
	/** pole path w przypadku ostatniego elementu jest ignorowane */
	path: string;
}
/**
 * Komponent używany głównie w panelach, można dowolnie modyfikować na inne potrzeby
 * @example
 * <app-header name="Animals" [breadcrumbs]="[{name: 'List', path: '/animals'},{name: 'animal', path: ''}]" class="no-border"></app-header>
 */
@Component( {
	selector: 'app-header',
	template: `
		<div class="bg-secondary">
			<ng-content></ng-content>
		</div>
	`,
	styleUrls: [ './header.component.scss' ]
} )
export class HeaderComponent {
	/** Lista obiektów `Breadcrumb`
	 * @type {Breadcrumb}
	 */
	public bc: Breadcrumb[];
	/** zmienna używana na widoku do wyświetlania nazwy
	 * @type {string}
	 */
	public viewName = '– no name –';
	/** czy link powrotu ma być widoczny ?
	 * @type {boolean}
	 */
	public viewBack = true;
	/** bindowanie klasy `breadcrumbs` na podstawie flagi classCreadcrumbs
	 * @type {boolean}
	 */

	@HostBinding('class.breadcrumbs') classBreadcrumbs: boolean;

	/** co to
	 *
	 * @type {string}
	 */
	@Input() set name (n: string) {
		this.viewName = n;
	}


	@Input() set back (b: boolean) {
		this.viewBack = b;
	}

	/** czy brakujące ogniwo
	 *
	 * @param {Breadcrumb[]} bc
	 */
	@Input() set breadcrumbs (bc: Breadcrumb[]) {
		this.bc = bc;
		this.classBreadcrumbs = !!bc;
	}
}
