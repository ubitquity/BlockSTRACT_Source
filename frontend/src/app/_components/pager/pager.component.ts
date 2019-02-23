import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output, ViewEncapsulation} from '@angular/core';

@Component({
	selector: 'app-pager',
	template: `
		<div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
			<div class="btn-group btn-group-sm mr-2" role="group" aria-label="First group">
				<button type="button" class="btn btn-page" *ngIf="number - 2 > - 1" (click)="jump(number - 2)">{{ number - 1 }}</button>
				<button type="button" class="btn btn-page" *ngIf="number - 1 > - 1" (click)="jump(number - 1)">{{ number }}</button>
				<button type="button" class="btn btn-page active">{{ number + 1 }}</button>
				<button type="button" class="btn btn-page" *ngIf="number + 1 < totalPages" (click)="jump(number + 1)">{{ number + 2 }}</button>
				<button type="button" class="btn btn-page" *ngIf="number + 2 < totalPages" (click)="jump(number + 2)">{{ number + 3 }}</button>
			</div>
			<div class="btn-group btn-group-sm mr-2" role="group" aria-label="Second group">
				<button type="button" class="btn btn-subtle" 
						[disabled]="number - 1 <= - 1"
						(click)="jump(number - 1)"><i class="fa fa-angle-left"></i></button>
				<button type="button" class="btn btn-subtle" 
						[disabled]="number + 1 >= totalPages" 
						(click)="jump(number + 1)"><i class="fa fa-angle-right"></i></button>
			</div>
		</div>
	`,
	changeDetection: ChangeDetectionStrategy.OnPush,
	encapsulation: ViewEncapsulation.None
})
export class PagerComponent {
	@Input() set pageable (s: Pageable) {
		if (!s) return;

		const {number, numberOfElements, totalElements, totalPages} = s;
		Object.assign(this, {number, numberOfElements, totalElements, totalPages});
	}
	@Output() change: EventEmitter<number> = new EventEmitter();

	number: number;
	numberOfElements: number;
	totalElements: number;
	totalPages: number;

	jump (page: number) {
		this.change.next(page);
	}
}

export interface Pageable {
	content?: any[];
	first: boolean;
	last: boolean;
	number: number;
	numberOfElements: number;
	size: number;
	sort: any[];
	totalElements: number;
	totalPages: number;
}
