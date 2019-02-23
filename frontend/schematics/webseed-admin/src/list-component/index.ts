// tslint:disable

import {
	Rule,
	SchematicContext,
	Tree,
	apply,
	chain,
	mergeWith,
	template,
	url, move,
} from '@angular-devkit/schematics';
import {
	strings,
	normalize,
} from '@angular-devkit/core';
import {setupOptions} from '../utils/setup';

export default function(options: any): Rule {
	let movePath = options.path;
	return chain([
		(_tree: Tree, context: SchematicContext) => {
			setupOptions(_tree, options);
			context.logger.info('Generate List: ' + options.name);
			context.logger.info('...with options: ' + JSON.stringify(options));
		},
		mergeWith(apply(url('./files'), [
			template({
				...strings,
				...options
			}),
			move((() => {
				console.log('path', options.path, movePath);
				return normalize(options.path)
			})()),
		])),
	]);
}
