import {Component} from '@angular/core';

@Component({
	selector: 'app-modal-modal',
	template: `
		<div class="card" [ngClass]="bg.value">
			<div class="card-body">
				<h1>testowy modal</h1>
				<pre class="p-3 mt-3 mb-0 bg-light">{{ someData | json }}</pre>
			</div>
		</div>
	`
})
export class ModalModalComponent {
	someData;
	bg;
}
