import {
	Rule,
	SchematicContext,
	Tree,
	apply,
	chain,
	mergeWith,
	template,
	url, move,
	schematic,
} from '@angular-devkit/schematics';
import {

	strings
} from '@angular-devkit/core';
import {setupOptions} from '../utils/setup';

export default function(options: any): Rule {
	if (options.dowolonyParametr) options.dowolonyParametr.lowerCase();

	return chain([
		(_tree: Tree, context: SchematicContext) => {
			setupOptions(_tree, options);
			context.logger.info('Generate module: ' + options.name);
			context.logger.info('...with options: ' + JSON.stringify(options));
		},

		mergeWith(apply(url('./files'), [
			template({
				...strings,
				...options,
			}),
			move(options.path)
		])),

		schematic('list-component', Object.assign(options, {withModule: true, path: options.path + '/' + strings.dasherize(options.name)})),
	]);
}
