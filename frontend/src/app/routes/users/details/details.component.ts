import { Component, OnInit, Input } from '@angular/core';
import { Logger } from '@wuja/logger';
import { ToastrService } from 'ngx-toastr';
import {environment} from '../../../../environments/environment';
import {LoginUtils} from '../../../_services/loginutils/loginutils.service';
import {TranslateService} from '@ngx-translate/core';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import {faAngleLeft, faEdit, faCheck, faStar} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../_services/utils/utils.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
// import { NeweditComponent } from '../newedit/newedit.component';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../users.service';
import { RateModalComponent } from '../../../_components/rate-modal/rate-modal.component';


@Component( {
	selector: 'app-details',
	templateUrl: './details.component.html'
} )

@AutoUnsubscribe()
@LogInit()
export class DetailsComponent implements OnInit {

	public faAngleLeft = faAngleLeft;
	public faEdit = faEdit;
	public faCheck = faCheck;
	public faStar = faStar;

	public prevUrl;
	public user;
	public abstractor;

	public id;


	constructor(private log: Logger,
				private utils: UtilsService,
				private route: ActivatedRoute,
				private modalService: NgbModal,
				private service: UsersService) {
				this.id = this.route.snapshot.params.id;
	}

	ngOnInit() {
		this.log.init('UsersComponent / Details', this);
		this.prevUrl = this.utils.getPreviousUrl();
		this.getUser(this.id);
	}

	getUser (id) {
		this.service.getUser(id)
			.subscribe(res => {
				this.abstractor = res;
			}, e => this.log.error('getUser', e));
	}

	rateAbstractor() {
		const instance = this.modalService.open(RateModalComponent, {windowClass: 'transparent'});
		instance.result.then(
			res => this.getUser(this.id),
			close);
	}


}
