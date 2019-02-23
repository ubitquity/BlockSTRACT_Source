import { Component, OnInit, Input, ViewChild, AfterViewChecked, ChangeDetectorRef, Output, EventEmitter, OnDestroy } from '@angular/core';
import { LogInit } from '@decorators/loginit';
import {InboxService} from '../inbox.service';
import {Logger} from '@wuja/logger';
import {Remember} from '@decorators/remember';
import {faAngleLeft, faPaperclip} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../_services/utils/utils.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { faFileAlt } from '@fortawesome/free-regular-svg-icons';
import { interval } from 'rxjs';


@LogInit()
// @Remember()
@Component({
	selector: 'app-conversation',
	templateUrl: './conversation.component.html',
	styleUrls: ['./conversation.component.scss']
})
export class ConversationComponent implements OnInit, AfterViewChecked, OnDestroy {
	public convId;
	form: FormGroup;
	public faAngleLeft = faAngleLeft;
	public faPaperclip = faPaperclip;
	public faFileAlt = faFileAlt;
	public files;
	public resetFiles = false;
	public pending = false;
	public inbox;
	@ViewChild('conversation') conversationBlock;
	@Output() refreshList = new EventEmitter<string>();
	showMessages = true;
	public intervalToggle;


	constructor (private log: Logger, private service: InboxService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router, private cd: ChangeDetectorRef) {
		this.route.params.subscribe(res => {
			this.convId = res.id;
			if (this.convId) this.getMessages();
		});
		if (this.convId) this.intervalToggle = interval(6000).pipe()
			.subscribe(response => this.getMessages());
	}

	ngOnInit() {
		this.form = this.fb.group({
			message: null,
			files: ''
		});
	}

	ngOnDestroy(): void {
		if (this.intervalToggle) this.intervalToggle.unsubscribe();
	}


	getMessages() {
		this.service.getMessages(this.convId).subscribe(res => {
			this.inbox = res;
			this.pending = false;
			this.showMessages = this.inbox.messages.length ? true : false;
			this.scrollToBottom();

		}, err => {
			this.log.log('err', err);
			this.showMessages = false;
			// this.inbox = {};
		});
	}

	closeConversation() {
		this.router.navigate(['inbox']);
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
		this.resetFiles = false;
		this.files = $event;
	}

	submit() {
		if (this.pending) return;
		this.pending = true;
		this.log.log('send', this.form.value);
		if (!this.form.value.message && !this.files) return;
		if (!this.form.value.message && this.files) this.form.value.message = '';
		this.service.sendMessage(this.convId, this.form.value.message, this.files).subscribe(res => {
			if (!this.inbox.messages) {
				this.refreshList.emit();
			}
			this.getMessages();
			this.form.reset();
			this.files = null;
			this.resetFiles = true;
			this.cd.detectChanges();
			this.scrollToBottom();
		}, err => this.log.log('err', err));
	}

}
