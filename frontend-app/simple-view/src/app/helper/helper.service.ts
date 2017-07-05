import {NgZone} from "@angular/core";
import {Observable} from "rxjs/Observable";

import * as EventSource from "eventsource";

/**
 * experimental.
 */
// export
class HelperService {

  /**
   * howto: stream configurations.
   */
  zone: NgZone;

  configurationListJsonDelayed: Array<any>;

  reloadConfigurationWithDelay() {
    this.zone = new NgZone({enableLongStackTrace: false});
    const url: string = "http://localhost:8080/configurations/infiniteStream/3";

    console.log("start streaming");
    this.configurationListJsonDelayed = [];

    const observable = Observable.create(observer => {
      const eventSource = new EventSource(url);
      eventSource.onmessage = x => observer.next(x.data);
      eventSource.onerror = x => observer.error(x);

      return () => eventSource.close();
    });
    observable.subscribe({
      next: guid => {
        this.zone.run(() => {
            this.configurationListJsonDelayed.push(JSON.parse(guid))
          }
        );
      },
      error: err => console.error('something wrong occurred: ' + err)
    });
  }
}
