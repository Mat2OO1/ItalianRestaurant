import {environment} from "src/environments/environment";

export class AppConstants {
  private static OAUTH2_URL = `${environment.apiUrl}/oauth2/authorize`;
  private static REDIRECT_URL = `?redirect_uri=${environment.frontUrl}/oauth2/redirect`
  public static API_URL = `${environment.apiUrl} + /api`;
  public static AUTH_API = AppConstants.API_URL + "/auth";
  public static GOOGLE_AUTH_URL = AppConstants.OAUTH2_URL + "/google" + AppConstants.REDIRECT_URL;
  public static FACEBOOK_AUTH_URL =  AppConstants.OAUTH2_URL + "/facebook" + AppConstants.REDIRECT_URL;
  public static GITHUB_AUTH_URL =  AppConstants.OAUTH2_URL + "/github" + AppConstants.REDIRECT_URL;
  public static LINKEDIN_AUTH_URL = AppConstants.OAUTH2_URL + "/linkedin" + AppConstants.REDIRECT_URL;
}
