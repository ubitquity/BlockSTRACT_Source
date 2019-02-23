import {
	Component,
	OnInit,
	ViewChild,
	OnDestroy
} from '@angular/core';
import { LogInit } from '@decorators/loginit';
import { InboxService } from './inbox.service';
import { Logger } from '@wuja/logger';
import { Remember } from '@decorators/remember';
import {
	faAngleLeft,
} from '@fortawesome/free-solid-svg-icons';

import { UtilsService } from '../../_services/utils/utils.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { ConversationComponent } from './conversation/conversation.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NewMessageModalComponent } from './new-message-modal/new-message-modal.component';
import { interval } from 'rxjs';


@LogInit()
// @Remember()
@Component({
	selector: 'app-inbox',
	templateUrl: './inbox.component.html',
	styleUrls: ['./inbox.component.scss']
})
export class InboxComponent implements OnInit, OnDestroy {
	public faAngleLeft = faAngleLeft;
	public prevUrl;
	public currentCoversationId;
	public list;
	public data = true;
	public intervalToggle;
	@ViewChild('chat') chat: ConversationComponent;

	constructor(
		private utils: UtilsService,
		private service: InboxService,
		private log: Logger,
		private modalService: NgbModal,
		private route: ActivatedRoute,
		private router: Router
	) {
		this.route.params.subscribe(res => {
			this.currentCoversationId = res.id;
		});
		this.intervalToggle = interval(6000).pipe()
			.subscribe(response => this.getConversations());
	}
	ngOnInit() {
		this.prevUrl = this.utils.getPreviousUrl();
		this.getConversations();
	}

	ngOnDestroy(): void {
		if (this.intervalToggle) this.intervalToggle.unsubscribe();
	}

	getConversations() {
		this.service.getConversations().subscribe(
			res => {
				this.list = res.list;
				// this.list[0].hasNewMessages = true;
				this.data = this.list.length ? true : false;
			},
			err => {
				this.log.log('err', err);
				this.data = false;
			}
		);
	}

	showChat(id) {
		this.router.navigate(['inbox/chat/' + id]);
		// this.currentCoversationId = id;
		// this.chat.getMessages(id);
	}

	newMessage() {
		const instance = this.modalService.open(NewMessageModalComponent, {
			windowClass: 'transparent'
		});
		// instance.componentInstance.getConversations();
		instance.result.then(res => {
			this.currentCoversationId = res;
			this.showChat(res);
		}, close => this.getConversations());
	}
}
