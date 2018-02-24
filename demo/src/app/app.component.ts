import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

class PasswordCheck {
  compromised: boolean;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  constructor(private http: HttpClient) {}

  isCompromised = false;
  canSubmit = false;
  password: String = "";

  onPasswordChanged(event: any) {
    let password = event.target.value;
    if (password.length > 0) {
      let payload = JSON.stringify({password: password});
      let options = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
      this.http
        .post('http://localhost:1337/passwordChecks', payload, options)
        .subscribe(
          (data: PasswordCheck) => {
            this.password = password;

            if (data.compromised) {
              this.isCompromised = true;
              this.canSubmit = false;
            } else {
              this.isCompromised = false;
              this.canSubmit = true;
            }
          },
          error => {
            console.log('error: ' + JSON.stringify(error));
            this.isCompromised = false;
            this.canSubmit = false;
          }
        );
    } else {
      this.isCompromised = false;
      this.canSubmit = false;
    }
  }

}
