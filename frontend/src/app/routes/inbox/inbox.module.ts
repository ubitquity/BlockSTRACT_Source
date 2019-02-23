import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {SharedModule} from '../../_modules/shared/shared.module';
import { InboxService } from './inbox.service';
import { InboxComponent } from './inbox.component';
import { ConversationComponent } from './conversation/conversation.component';
import { NewMessageModalComponent } from './new-message-modal/new-message-modal.component';

const routes: Routes = [
	{path: '', component: InboxComponent},
	{path: 'chat/:id', component: InboxComponent},
];

@NgModule({
	imports: [
		CommonModule,
		SharedModule,
		RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	declarations: [InboxComponent, ConversationComponent, NewMessageModalComponent],
	entryComponents: [NewMessageModalComponent],
	providers: [InboxService]
})
export class InboxModule {}
