import {Injectable} from '@angular/core';
import {HttpAuth} from '../../_services/httpauth/http-auth.service';


@Injectable({
	providedIn: 'root'
})
export class ReportsService {
	constructor (private http: HttpAuth) {}

	mostExpensiveAbstractor() {
		return this.http.get('/most-expensive');
	}

	mostResponsiveAbstractor() {
		return this.http.get('/most-responsive');
	}

	cheapestAbstractor() {
		return this.http.get('/cheapest');
	}

	averageCost(startDate, month) {
		return this.http.get(`/average-cost/${startDate}/${month}`);
	}

	projectsDeclined() {
		return this.http.get('/projects-declined');
	}

	projectsRecalled() {
		return this.http.get('/projects-recalled');
	}

	projectsWon() {
		return this.http.get('/projects-won');
	}

	totalIncome() {
		return this.http.get('/total-income');
	}
}
