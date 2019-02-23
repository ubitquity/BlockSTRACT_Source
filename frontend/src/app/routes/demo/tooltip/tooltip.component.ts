import {Component, OnInit} from '@angular/core';

@Component({
	selector: 'app-tooltip',
	template: `
		<app-header name="Tooltip / Popover" class="no-border"></app-header>
		<div class="card mb-4">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Simple tooltip</h3>
				<button type="button" class="btn btn-outline-secondary" placement="top" ngbTooltip="Tooltip on top">
					Tooltip on top
				</button>
				<button type="button" class="btn btn-outline-secondary" placement="right" ngbTooltip="Tooltip on right">
					Tooltip on right
				</button>
				<button type="button" class="btn btn-outline-secondary" placement="bottom" ngbTooltip="Tooltip on bottom">
					Tooltip on bottom
				</button>
				<button type="button" class="btn btn-outline-secondary" placement="left" ngbTooltip="Tooltip on left">
					Tooltip on left
				</button>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Popover</h3>
				<p>
					Popovers can contain any arbitrary HTML, Angular bindings and even directives!
					Simply enclose desired content in a <code>&lt;ng-template&gt;</code> element.
				</p>

				<ng-template #popContent>Hello, <b>{{name}}</b>!</ng-template>
				<button type="button" class="btn btn-outline-secondary" [ngbPopover]="popContent" popoverTitle="Fancy content">
					I've got markup and bindings in my popover!
				</button>
			</div>
		</div>
	`
})
export class TooltipComponent {
	name: 'Something';
}
