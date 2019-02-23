export interface StorageEngine {
	prefix: string;
	set(key: string, item);
	get(key: string);
	delete(key: string);
	getAll();
}
