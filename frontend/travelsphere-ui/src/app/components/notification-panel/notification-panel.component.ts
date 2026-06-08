import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-notification-panel',
  standalone: true,
  imports: [MatIconModule, MatButtonModule],
  template: `
    <!-- Notification panel placeholder - will be expanded with WebSocket -->
  `,
  styles: ['']
})
export class NotificationPanelComponent {}
