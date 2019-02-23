
import {fromEvent as observableFromEvent,  Observable } from 'rxjs';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import * as _ from 'lodash';
import { Logger } from '@wuja/logger';
import { faPaperclip, faTimes, faPlusCircle } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';


export interface IFile extends File {
	preview?: string;
}

const fileIcon = 'data:image/svg+xml;utf8;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iaXNvLTg4NTktMSI/Pgo8IS0tIEdlbmVyYXRvcjogQWRvY' +
	'mUgSWxsdXN0cmF0b3IgMTkuMC4wLCBTVkcgRXhwb3J0IFBsdWctSW4gLiBTVkcgVmVyc2lvbjogNi4wMCBCdWlsZCAwKSAgLS0+CjxzdmcgeG1sbnM9Imh0dHA6Ly93d3' +
	'cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgdmVyc2lvbj0iMS4xIiBpZD0iQ2FwYV8xIiB4PSIwcHgiIHk' +
	'9IjBweCIgdmlld0JveD0iMCAwIDYwIDYwIiBzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA2MCA2MDsiIHhtbDpzcGFjZT0icHJlc2VydmUiIHdpZHRoPSIz' +
	'MnB4IiBoZWlnaHQ9IjMycHgiPgo8Zz4KCTxwYXRoIGQ9Ik00Mi41LDIyaC0yNWMtMC41NTIsMC0xLDAuNDQ3LTEsMXMwLjQ0OCwxLDEsMWgyNWMwLjU1MiwwLDEtMC40N' +
	'DcsMS0xUzQzLjA1MiwyMiw0Mi41LDIyeiIgZmlsbD0iIzAwMDAwMCIvPgoJPHBhdGggZD0iTTE3LjUsMTZoMTBjMC41NTIsMCwxLTAuNDQ3LDEtMXMtMC40NDgtMS0xLT' +
	'FoLTEwYy0wLjU1MiwwLTEsMC40NDctMSwxUzE2Ljk0OCwxNiwxNy41LDE2eiIgZmlsbD0iIzAwMDAwMCIvPgoJPHBhdGggZD0iTTQyLjUsMzBoLTI1Yy0wLjU1MiwwLTE' +
	'sMC40NDctMSwxczAuNDQ4LDEsMSwxaDI1YzAuNTUyLDAsMS0wLjQ0NywxLTFTNDMuMDUyLDMwLDQyLjUsMzB6IiBmaWxsPSIjMDAwMDAwIi8+Cgk8cGF0aCBkPSJNNDIu' +
	'NSwzOGgtMjVjLTAuNTUyLDAtMSwwLjQ0Ny0xLDFzMC40NDgsMSwxLDFoMjVjMC41NTIsMCwxLTAuNDQ3LDEtMVM0My4wNTIsMzgsNDIuNSwzOHoiIGZpbGw9IiMwMDAwM' +
	'DAiLz4KCTxwYXRoIGQ9Ik00Mi41LDQ2aC0yNWMtMC41NTIsMC0xLDAuNDQ3LTEsMXMwLjQ0OCwxLDEsMWgyNWMwLjU1MiwwLDEtMC40NDcsMS0xUzQzLjA1Miw0Niw0Mi' +
	'41LDQ2eiIgZmlsbD0iIzAwMDAwMCIvPgoJPHBhdGggZD0iTTM4LjkxNCwwSDYuNXY2MGg0N1YxNC41ODZMMzguOTE0LDB6IE0zOS41LDMuNDE0TDUwLjA4NiwxNEgzOS4' +
	'1VjMuNDE0eiBNOC41LDU4VjJoMjl2MTRoMTR2NDJIOC41eiIgZmlsbD0iIzAwMDAwMCIvPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+Cjxn' +
	'Pgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+CjxnPgo8L2c+C' +
	'jwvc3ZnPgo=';
const images = [ 'png', 'jpg', 'jpeg', 'gif', 'bmp' ];

@Component( {
	selector: 'app-file-uploader',
	templateUrl: './file-uploader.component.html',
	styleUrls: ['./file-uploader.component.scss']
} )
export class FileUploaderComponent implements OnInit {
	faPaperclip = faPaperclip;
	faPlusCircle = faPlusCircle;
	faTimes = faTimes;
	@Input() multiple = false;
	// @Input() disabled = false;
	@Input() pending = false;
	@Input() buttonText = '';
	@Input() format = 'classic';
	@ViewChild('fileinput') _fileinput;
	@Input() set allowed (t: string[]) {
		this._allowed = Array.isArray(t) ? t : null;
	}

	@Input() set reset (b: boolean) {
		this._reset = b;
		if (b) {
			this._fileinput.nativeElement.value = '';
			this.files = [];
			this.fileChanges.next( null );
		}
	}

	public _reset = false;
	public _allowed: string[] = null;
	@Output() fileChanges: EventEmitter<File[] | string> = new EventEmitter();
	public acceptString: string;
	public files: IFile[] = [];

	constructor( private log: Logger, private sanitizer: DomSanitizer, private toastr: ToastrService ) {}

	ngOnInit () {
		this.combineAllowed();
	}

	combineAllowed() {
		if ( this._allowed && this._allowed.indexOf( 'images' ) !== -1 ) {
			_.remove( this._allowed, i => i === 'images' );
			this._allowed.push( ...images );
		}
		this.getAcceptAttr();
	}

	getAcceptAttr (): void {
		this.acceptString = ( this._allowed || !_.isEmpty(this._allowed) )
			? _.map( this._allowed, item => `.${item}` ).join( ', ' ) : '';
	}

	checkIfAllowed( file ) {
		const name = file.name;
		const extension = (name.split( '.' )[ name.split( '.' ).length - 1 ]).toLowerCase();
		return (_.isNull( this._allowed ) || this._allowed.indexOf( extension ) !== -1) ? file : null;
	}

	updateFiles( $event ) {
		if (!$event) return;
		this._reset = false;
		$event.preventDefault();
		const target = <HTMLInputElement>$event.target;
		const cond = !!(_.isNull( target.files ) || target.files.length);
		this.files = _.toArray( target.files );
		if ( cond ) {
			_.forEach( this.files, ( file, index ) => {
				if ( this.checkIfAllowed( file ) ) this.readFile( file, index );
				else {
					this.toastr.error('Format not allowed! Allowed formats are:' + this._allowed.map(allowed => ' ' + allowed.toUpperCase()));
				}
			} );
		} else this.fileChanges.next( null );
	}

	public getKb (size) {
		return (+size / 1024).toFixed(2);
	}

	async readFile( file, index ) {
		const isImage = images.find( ext => file.type === `image/${ext}` );
		if ( isImage ) {
			const Reader = new FileReader();
			const preview$ = observableFromEvent( Reader, 'load' );

			await preview$.subscribe( preview => {
				file.preview = <FileReader>Reader.result;
				this.files[ index ] = file;
				this.fileChanges.next( this.files );
			} );

			Reader.readAsDataURL( file );
		} else {
			file.preview = this.sanitizer.bypassSecurityTrustUrl( fileIcon );
			this.files[ index ] = file;
			this.fileChanges.next( this.files );
		}
	}
}
