import {Injectable} from '@angular/core';

declare var $: any;
import {environment} from '../../../environments/environment';


const {storagePrefix, apiUrl, predefToken, authorizationHeaderName, storageTokenLocation} = environment;

@Injectable()
export class SettingsService {
	public app = {storagePrefix, apiUrl, predefToken, authorizationHeaderName, storageTokenLocation};
	public global = {
		date: {
			full: 'yMMMMEEEEd',
			short: 'yMdjm',
			shortWoTime: 'dd/MM/yyyy',
			time: 'HH:mm'
		}
	};
}
