
import {tap} from 'rxjs/operators';
import {Component, OnInit} from '@angular/core';
import {HttpAuth} from '../../../_services/httpauth/http-auth.service';
import {Logger} from '@wuja/logger';

@Component({
	selector: 'app-infinite-scroll',
	template: `
		<app-header name="Infinite scroll" class="no-border"></app-header>
		<div class="card">
			<div class="card-body">
				<h3 class="mt-0 mb-3">content…</h3>
				<div infiniteScroll
					 [infiniteScrollDistance]="2"
					 [infiniteScrollThrottle]="50"
					 (scrolled)="getStories()"
					 [fromRoot]="false">
					<div class="list-group mt-3 mb-2">
						<div class="list-group-item list-group-item-action flex-column align-items-start" *ngFor="let post of feed">
							{{ post.time | date }} — {{ post.title }} <br>
							<span class="small"><span class="text-muted mr-3">{{ post.domain }}</span><a [href]="post.url">{{ post.url }}</a></span>
						</div>
					</div>
				</div>
			</div>
		</div>

	`
})
export class InfiniteScrollComponent implements OnInit {
	BASE_URL = 'http://node-hnapi.herokuapp.com';
	currentPage = 1;
	feed: Array<any> = [];

	constructor(private http: HttpAuth, private log: Logger) {
		// this.scrollCallback = this.getStories.bind(this);
	}

	ngOnInit() {
		this.getStories();
	}

	getStories() {
		this.http.get(`!${this.BASE_URL}/news?page=${this.currentPage}`).pipe(tap(this.processData))
			.subscribe(res => {
				this.log.log('stories', res);
			});
	}

	private processData = (news) => {
		this.currentPage++;
		this.feed = this.feed.concat(news);
	}
}
