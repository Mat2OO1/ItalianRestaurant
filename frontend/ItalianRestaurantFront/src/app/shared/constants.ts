import {environment} from "../../environments/environment";

export class AppConstants {
  private static API_BASE_URL = environment.apiUrl + "/";
  private static OAUTH2_URL = AppConstants.API_BASE_URL + "oauth2/authorize/";
  private static REDIRECT_URL = `?redirect_uri=${environment.frontUrl}/oauth2/redirect`
  public static GOOGLE_AUTH_URL = AppConstants.OAUTH2_URL + "google" + AppConstants.REDIRECT_URL;
  public static GITHUB_AUTH_URL = AppConstants.OAUTH2_URL + "github" + AppConstants.REDIRECT_URL;
}
