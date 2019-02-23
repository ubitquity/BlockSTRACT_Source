import {AfterViewInit, Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { UsersService } from '../users.service';
import { UserDto } from '../../../_interfaces/models/UserDto';
import { ToastrService } from 'ngx-toastr';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../../_services/utils/utils.service';
import {TableColumn} from '@swimlane/ngx-datatable';
// import {DetailsComponent} from '../details/details.component';
import {ToggleAbstractorComponent} from '../toggle-abstractor/toggle-abstractor.component';
import { TranslateService } from '@ngx-translate/core';
import { faAngleLeft, faEdit, faTrash, faAngleRight } from '@fortawesome/free-solid-svg-icons';
import { faTimesCircle, faCheckCircle } from '@fortawesome/free-regular-svg-icons';
import { ConfirmModalComponent } from '../../../_components/confirm-modal/confirm-modal.component';
import { Router } from '@angular/router';
import {Logger} from '@wuja/logger';
import {Remember} from '@decorators/remember';
import { LogInit } from '@decorators/loginit';


// @LogInit()
// @Remember()

@Component( {
	selector: 'app-list',
	templateUrl: './list.component.html'
	// styleUrls: ['./list.component.scss']
} )
export class ListComponent implements OnInit, AfterViewInit {
	faAngleLeft = faAngleLeft;
	faAngleRight = faAngleRight;
	public faTimesCircle = faTimesCircle;
	public faCheckCircle = faCheckCircle;
	public faEdit = faEdit;
	public faTrash = faTrash;
	public users;
	public currentCard = 'datatables';
	public fullWidth = true;
	public addInline = false;
	@ViewChild('buttonsColumn') buttonsColumn: TemplateRef<any>;
	@ViewChild('hoursColumn') hoursColumn: TemplateRef<any>;
	@ViewChild('activeColumn') activeColumn: TemplateRef<any>;
	public tableColumns: TableColumn[];
	public prevUrl;
	public page = 1;
	public status = 'ALL';
	public pages = [];
	public size = {label: 10, value: 10};
	public sort = '';
	public totalPages;
	public totalElements;
	public selected = [];

	public users$;
	scrollBarHorizontal = (window.innerWidth < 1200);

	public options = this.service.options;

	constructor(private log: Logger,
				private toastr: ToastrService,
				private modalService: NgbModal,
				private translate: TranslateService,
				private router: Router,
				private utils: UtilsService,
				private service: UsersService ) {
					window.onresize = () => {
						this.scrollBarHorizontal = (window.innerWidth < 1200);
					  };
	}

	ngOnInit() {
		this.log.init('Users / ListComponent', this);
		this.prevUrl = this.utils.getPreviousUrl();
		this.getUsers();
	}

	ngAfterViewInit () {
		this.tableColumns = this.utils.columnsForDatatables(
			[
				{name: 'Company Name',  prop: 'companyName'},
				{name: this.translate.instant('users.firstName'), prop: 'firstName'},
				{name: this.translate.instant('users.lastName'), prop: 'lastName'},
				{name: this.translate.instant('users.email'), prop: 'email'},
				{name: 'Phone', prop: 'phoneNumber'},
				{name: 'Weekly Availability', prop: 'weeklyHoursAvailability', cellTemplate: this.hoursColumn},
				{name: 'Active', prop: 'enabled', cellTemplate: this.activeColumn, maxWidth: 70},

				{name: '', cellTemplate: this.buttonsColumn, cellClass: 'tools', maxWidth: 150}]
		);
	}

	getUsers (reset = false) {
		const {sort} = this;
		const size = this.size.value;
		let page = this.page;
		if (reset) page = 0;
		this.users$ =  this.service.getUsers({page, size, sort})
			.subscribe(res => {
				this.users = res;
				this.page = res.number + 1;
				this.totalPages = res.totalPages;
				this.pages.length = 0;
				for (let i = 1; i <= res.totalPages; i++) {
					this.pages.push(i);
				}
				this.totalElements = res.totalElements;
			});
	}

	deleteUser (id) {
		const instance = this.modalService.open(ConfirmModalComponent, {windowClass: 'transparent'});
		instance.componentInstance.text = 'Are you sure you want to delete this Abstractor?';
		instance.componentInstance.titleText = 'Delete Abstractor';
		instance.result.then(ok => {
			this.service.deleteUser(id)
				.subscribe(res => {
					this.toastr.success(this.translate.instant('users.deleted'));
					this.getUsers();
				});
		}, close => {
			return;
		});
	}

	openDetails($event) {
		this.router.navigate([`abstractors/details/${$event.selected[0].id}`]);
	}

	openEditModal(userId) {
		const instance = this.modalService.open(ToggleAbstractorComponent, {windowClass: 'transparent'});
		instance.componentInstance.getUser(userId);
		instance.result.then(
			res => this.getUsers(),
		close => this.getUsers()
	);
	}

	onUserAdd ($event) {
		this.log.log('user create', $event);
		this.addInline = false;
		this.getUsers();
	}

	setPage(direction) {

		if (direction === 'next') {
			if (this.page + 1 > this.totalPages) return;
			this.page = this.page + 1;
			this.log.log(direction, this.page);
			this.getUsers();
		}
		if (direction === 'prev') {
			this.log.log(direction, this.page);
			if (this.page - 1 <= 0) return;
			this.page = this.page - 1;
			this.getUsers();
		}
	}

	onSort($event) {
		this.sort = $event.column.prop + ',' + $event.newValue;
		this.getUsers(true);
	}


}
