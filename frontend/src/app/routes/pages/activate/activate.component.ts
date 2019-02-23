import { OnInit, Component } from '@angular/core';
import { Logger } from '@wuja/logger';
import { ActivateService } from './activate.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


@Component({
	selector: 'app-activate',
	templateUrl: './activate.component.html',
	styleUrls: ['./activate.component.scss']
})

export class ActivateComponent implements OnInit {

	public tokenValid = false;

	constructor(private log: Logger, private service: ActivateService, private route: ActivatedRoute, private toastr: ToastrService, private router: Router) {}

	ngOnInit() {
		this.log.init('Pages / ActivateComponent', this);
		if (this.route.snapshot.params['token']) {
			const token = this.route.snapshot.params['token'];
			this.service.activateAbstractor(token).subscribe(res => {
				this.log.log('active!', res);
				this.tokenValid = true;
			}, err => {
				if (err.error._errorDetails === 'VALIDATION_ERROR: ApiException: User account is already activated') {
					this.toastr.error('Abstractor account is already activated');
				} else if (err.error._errorDetails === 'NOT_FOUND: ApiException: Invalid token') {
					this.toastr.error('Invalid activation token');
				} else {
					this.toastr.error('Error while activating account');
				}

				this.tokenValid = false;
				this.router.navigate(['/login']);
				this.log.log('error', err);
			});
		}

	}

}
