/**
 * [app.name not defined in app.properties]
 * Admin API
 *
 * OpenAPI spec version: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { CityDto } from './cityDto';
import { CurrencyDto } from './currencyDto';


export interface CountryDto {
    cities?: Array<CityDto>;
    currency?: CurrencyDto;
    id?: number;
    name?: string;
}