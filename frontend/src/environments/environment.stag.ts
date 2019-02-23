// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
	version: '1.6.0',
	appName: 'appstract',
	localName: 'Appstract',
	slugName: 'appstract',
	production: false,
	apiUrl: 'https://lab3.itcraft.pl/appstract-api',
	storagePrefix: 'APS',
	language: 'en',
	availableLangs: ['en'],
	predefToken: null,
	keepStates: true,
	authorizationHeaderName: 'Authorization',
	storageTokenLocation: 'credentials.token',
	routeWhiteList: ['/login', '/password/reset', '/403']
};
