import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { NotificationPanelComponent } from './components/notification-panel/notification-panel.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, NotificationPanelComponent],
  template: `
    <app-header />
    <main>
      <router-outlet />
    </main>
    <app-footer />
    <app-notification-panel />
  `,
  styles: [`
    :host {
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }
    main {
      flex: 1;
    }
  `],
})
export class AppComponent {}
