import { Component, OnInit, Input, ViewChild, AfterViewChecked, ChangeDetectorRef, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Logger } from '@wuja/logger';
import {AutoUnsubscribe} from '../../../../_decorators/autounsub';
import {LogInit} from '../../../../_decorators/loginit';
import { faAngleLeft, faPaperclip } from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../../_services/utils/utils.service';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../orders.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { LoginUtils } from '../../../../_services/loginutils/loginutils.service';
import { interval } from 'rxjs';


@Component( {
	selector: 'app-inbox',
	templateUrl: './inbox.component.html',
	styleUrls: ['./inbox.component.scss']
} )

@AutoUnsubscribe()
@LogInit()
export class InboxComponent implements OnInit, AfterViewChecked, OnDestroy {

	public faAngleLeft = faAngleLeft;
	public faPaperclip = faPaperclip;
	public id;
	public prevUrl;
	public convId;
	public pending = false;
	public resetFiles = false;
	public user;
	form: FormGroup;

	public files;
	public inbox;
	public showInbox = true;
	@Input() order;
	@ViewChild('conversation') conversationBlock;
	@Output() changeTab = new EventEmitter<any>();
	intervalToggle;


	constructor(private log: Logger,
		private utils: UtilsService,
		private fb: FormBuilder,
		private loginutils: LoginUtils,
		private route: ActivatedRoute,
		private cd: ChangeDetectorRef,
		private service: OrdersService) {
		this.id = this.route.snapshot.params.id;
		}


	ngOnInit() {
		this.intervalToggle = interval(6000).pipe()
			.subscribe(response => this.getMessages());
		this.user = this.loginutils.user;
		this.form = this.fb.group({
			message: null,
			files: ''
		});
		if (this.user.role !== 'ADMIN' && this.order.internalStatus === 'UNCLAIMED') {
			this.changeTab.emit('overview');
			return;
		}
		this.getMessages();
	}

	ngOnDestroy(): void {
		if (this.intervalToggle) this.intervalToggle.unsubscribe();
	}

	getMessages() {
		this.service.getMessages(this.id).subscribe(res => {
			this.inbox = res;
			this.pending = false;
			this.scrollToBottom();
			// this.showInbox =  res.messages && res.messages.length ? true : false;
			if (!res.user && res.messages && !res.messages.length) this.showInbox = false;
			if (!res.user) this.intervalToggle.unsubscribe();

		}, err => {
			this.log.log('err', err);
			this.showInbox = false;
		});
	}

	ngAfterViewChecked() {
		this.scrollToBottom();
	}

	scrollToBottom(): void {
		if (!this.conversationBlock) return;
		try {
			 this.conversationBlock.nativeElement.scrollTop = this.conversationBlock.nativeElement.scrollHeight;
		} catch (err) {
			this.log.log('scroll err', err);
		}
	}


	keyDownFunction(event) {
		if (!this.form.value.message && !this.files) return;
		if (event.keyCode === 13 && !event.shiftKey) {
			event.preventDefault();
			this.submit();
		}
	  }

	fileListener($event) {
		this.files = $event;
	}

	submit() {
		if (this.pending) return;
		this.pending = true;
		this.log.log('send', this.form.value);
		if (!this.form.value.message && !this.files) return;
		if (!this.form.value.message && this.files) this.form.value.message = '';
		this.service.sendMessage(this.id, this.form.value.message, this.files).subscribe(res => {
			this.getMessages();
			this.form.reset();
			this.files = null;
			this.resetFiles = true;
			this.cd.detectChanges();
			this.scrollToBottom();
		}, err => this.log.log('err', err));
	}


}
