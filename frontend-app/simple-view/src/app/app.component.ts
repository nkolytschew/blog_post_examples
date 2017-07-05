import {Component, OnChanges, OnInit} from "@angular/core";
import {Headers, Http, RequestOptions} from "@angular/http";
import {Subscription} from "rxjs/Subscription";

/**
 * THIS IS FOR DEMONSTRATION PURPOSES ONLY.
 * PLEASE DO NOT USE IN PRODUCTION.
 */

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnChanges, OnInit {

  // main configuration url
  private configurationUrl: string = "http://localhost:8080/configurations";
  private ratingUrl: string = "http://localhost:8080/ratings";

  // to show how to un-subscribe from an observerable
  disposable: Subscription;

  // show 'configuration detail' div
  showConfigurationDetailDiv: boolean = false;
  // switch between 'load configuration delayed' and 'load configuration fast' div
  showDelayed: boolean = false;
  // show 'create configuration' form
  showCreateNewConfiguration: boolean;
  // show 'create rating' form
  showCreateRating: boolean = false;

  // represents the whole json from spring-data-rest
  configurationListJson: any;
  configurationListJsonDelayed: any;
  configurationJson: any;

  /**
   *  use string fields for base64 of files
   */
  imageAsString: String;
  configAsString: String;

  // use array to iterate to a number instead of over collection
  ratingRangeArray: Array<number> = []
  // the selected rating
  currentRating: number = 1;

  constructor(private http: Http) {
  }

  // ------------------------------------------------------------------------------------------

  /**
   * angular lifecycle
   */

  /**
   * define what happens when component loads
   */
  ngOnInit() {
    this.ngOnChanges()
  }

  /**
   * define what happens when changes occur
   */
  ngOnChanges() {
    // reset vars
    this.showDelayed = false;
    this.showCreateNewConfiguration = false;
    this.showConfigurationDetailDiv = false;
    this.showCreateRating = false;
    this.configurationListJsonDelayed = null;
    this.resetBase64String()

    // reset view by reloading configurations
    this.getConfigList()
  }

  /**
   * end angular lifecycle
   */

  // ------------------------------------------------------------------------------------------

  /**
   * begin CRUD operation
   */

  /**
   *  load configurations from backend
   */
  getConfigList() {
    return this.http.get(this.configurationUrl)
      .subscribe(
        data => this.configurationListJson = data.json(),
        error => console.log(error),
        () => console.log("completed default reload!")
      );
  }

  /**
   * reload configuration
   */
  reloadConfigurationWithoutDelay() {
    console.log("start default reload")
    this.ngOnChanges()
  }

  /**
   * reload configurations with a delay
   */
  reloadConfigurationWithDelay() {
    console.log("start delayed reload");
    this.showDelayed = true;

    const url: string = `${this.configurationUrl}/delay/15`;

    this.disposable = this.http.get(url).subscribe(
      data => {
        this.configurationListJsonDelayed = data.json();
        // set explicit to true. value might have changed in the meantime
        this.showDelayed = true
      },
      error => console.log(error),
      () => console.log("completed delayed reload!")
    );
  }

  /**
   * cancel delayed reload
   */
  abortDelayedConfigurationLoad() {
    console.log("abort delayed reload")
    this.disposable.unsubscribe()
  }

  /**
   *  create new configuration by json
   *  read values from form-controls;
   *  create json representation;
   */
  saveConfig(config): void {
    // set converted base64-string manually
    config.thumbImage = this.imageAsString;
    config.configItem = this.configAsString;

    // set header for post to json
    const options = new RequestOptions({headers: new Headers({'Content-Type': 'application/json'})});

    // execute post and update view
    this.http.post(this.configurationUrl, JSON.stringify(config), options)
      .subscribe(
        data => this.ngOnChanges(),
        error => console.log(error)
      );
  }

  /**
   * load single configuration
   *
   * setup simple array with 10 elements.
   * use array to iterate and print stars
   */
  getConfigDetails(id) {
    this.ratingRangeArray = Array(10).fill(0).map((e, i) => i + 1);
    this.showCreateRating = false;
    this.showConfigurationDetail();
    this.http.get(`${this.configurationUrl}/details/${id}`)
      .subscribe(
        data => {
          this.showConfigurationDetailDiv = true
          this.configurationJson = data.json()
        },
        error => console.log(error),
        () => console.log("completed load detail!")
      );
  }

  /**
   * update single configuration
   */
  updateConfig(config, doc) {
    // set converted base64-string manually
    this.imageAsString === "" ? config.thumbImage = doc.thumbImage : config.thumbImage = this.imageAsString;
    this.configAsString === "" ? config.configItem = doc.configItem : config.configItem = this.configAsString;

    // set header for post to json
    const options = new RequestOptions({headers: new Headers({'Content-Type': 'application/json'})});
    this.http.put(this.configurationUrl, JSON.stringify(config), options)
      .subscribe(
        data => this.ngOnChanges(),
        error => console.log(error),
        () => console.log("completed load detail!")
      );
  }

  /**
   * delete singel configuration
   */
  deleteConfig(id) {
    this.http.delete(`${this.configurationUrl}/${id}`)
      .subscribe(() => this.ngOnChanges());
  }

  /**
   * end CRUD operation
   */

  // ------------------------------------------------------------------------------------------

  /**
   * rating
   */

  saveRating(rating, configurationId) {

    // set header for post to json
    const options = new RequestOptions({headers: new Headers({'Content-Type': 'application/json'})});
    this.http.put(`${this.ratingUrl}/${configurationId}`, JSON.stringify(rating), options)
      .subscribe(() => {
          console.log("completed load rating comments!");
          this.getConfigDetails(configurationId);
        }
      );
  }

  deleteRatingComment(configurationId, ratingId) {

  }

  /**
   * end ratings
   */

  // ------------------------------------------------------------------------------------------

  /**
   * begin helper methods
   */

  /**
   * make hidden divs visible
   */
  addNewConfiguration(): void {
    this.showCreateNewConfiguration = true;
    this.showConfigurationDetailDiv = false;
  }

  showConfigurationDetail(): void {
    this.showConfigurationDetailDiv = true;
    this.showCreateNewConfiguration = false;
  }

  showCreateRatingForm() {
    this.showCreateRating = true;
  }

  /**
   * use simple mime type checking, without check on server.
   * assert that images are only "image/jpeg" and product are only "text/xml".
   * simple file ending check is enough for prototype.
   *
   * @param fileInput {@link any}
   */
  checkAndAddImageFileOnChangeEvent(fileInput: any) {
    const fileArray = <Array<File>> fileInput.target.files;

    // check if array contains file
    // direct access index 0, since there can only be one file
    if (fileArray[0].size > 0) {
      var reader = new FileReader();
      reader.onload = this._handleReaderLoadedJpg.bind(this);
      reader.readAsBinaryString(fileArray[0]);

      console.log("filestring: " + this.imageAsString.substr(0, 64))
    } else {
      window.alert("Please use only JPG-files");
      fileInput.target.value = ''
    }
  }

  /**
   * async: load and convert string to binary
   */
  _handleReaderLoadedJpg(readerEvt) {
    var binaryString = readerEvt.target.result;
    this.imageAsString = btoa(binaryString);  // Converting binary string data.
  }

  /**
   * use simple mime type checking, without check on server.
   * assert that images are only "image/jpeg" and product are only "text/xml".
   * simple file ending check is enough for prototype.
   *
   * @param fileInput {@link any}
   */
  checkAndAddXmlFileOnChangeEvent(fileInput: any) {
    const fileArray = <Array<File>> fileInput.target.files;

    // check if array contains file
    // direct access index 0, since there can only be one file
    if (fileArray[0].size > 0 && fileArray[0].type === "text/xml") {
      var reader = new FileReader();
      reader.onload = this._handleReaderLoadedXml.bind(this);
      reader.readAsBinaryString(fileArray[0]);

      console.log("filestring: " + this.configAsString.substr(0, 64))
    } else {
      fileInput.target.value = ''
    }
  }

  /**
   * async: load and convert string to binary
   */
  _handleReaderLoadedXml(readerEvt) {
    var binaryString = readerEvt.target.result;
    this.configAsString = btoa(binaryString);  // Converting binary string data.
  }

  /**
   * close form
   */
  close() {
    this.showCreateNewConfiguration = false
    this.resetBase64String()
  }

  closeUpdateForm() {
    this.showConfigurationDetailDiv = false
    this.resetBase64String()
  }

  resetCreateRatingForm() {
    this.showCreateRating = false;
    this.currentRating = 0
  }

  /**
   * reset strings
   */
  private resetBase64String() {
    this.imageAsString = "";
    this.configAsString = "";
  }

  // set current rating to selected "star" value
  updateRating(ratingValue: number): void {
    this.currentRating = ratingValue;
  }

  /**
   * end helper methods
   */
}

