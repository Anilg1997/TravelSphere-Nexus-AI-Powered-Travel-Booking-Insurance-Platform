import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [MatToolbarModule],
  template: `
    <footer class="app-footer">
      <div class="footer-content">
        <div class="footer-brand">
          <h3>🌍 TravelSphere</h3>
          <p>Your all-in-one travel booking & insurance platform powered by AI.</p>
        </div>
        <div class="footer-links">
          <div class="footer-col">
            <h4>Services</h4>
            <a href="/flights">Flights</a>
            <a href="/hotels">Hotels</a>
            <a href="/cars">Car Rental</a>
            <a href="/insurance">Insurance</a>
          </div>
          <div class="footer-col">
            <h4>Support</h4>
            <a href="#">Help Center</a>
            <a href="#">Contact Us</a>
            <a href="#">Cancellation</a>
            <a href="#">Refund Policy</a>
          </div>
          <div class="footer-col">
            <h4>Company</h4>
            <a href="#">About Us</a>
            <a href="#">Careers</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Terms of Service</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2026 TravelSphere. All rights reserved.</p>
      </div>
    </footer>
  `,
  styles: [`
    .app-footer {
      background: #1a1a2e;
      color: rgba(255,255,255,0.8);
      padding: 48px 24px 0;
      margin-top: 48px;
    }
    .footer-content {
      max-width: 1280px;
      margin: 0 auto;
      display: grid;
      grid-template-columns: 1fr 2fr;
      gap: 48px;
      padding-bottom: 32px;
    }
    .footer-brand h3 { color: white; margin: 0 0 8px; font-size: 1.3rem; }
    .footer-brand p { margin: 0; line-height: 1.6; font-size: 0.9rem; }
    .footer-links { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
    .footer-col h4 { color: white; margin: 0 0 12px; font-size: 1rem; }
    .footer-col a { display: block; color: rgba(255,255,255,0.7); text-decoration: none; margin-bottom: 8px; font-size: 0.9rem; transition: color 0.2s; }
    .footer-col a:hover { color: white; }
    .footer-bottom {
      border-top: 1px solid rgba(255,255,255,0.1);
      padding: 16px 24px;
      text-align: center;
      font-size: 0.85rem;
    }
    .footer-bottom p { margin: 0; }
    @media (max-width: 768px) {
      .footer-content { grid-template-columns: 1fr; gap: 24px; }
      .footer-links { grid-template-columns: 1fr 1fr; }
    }
  `]
})
export class FooterComponent {}
